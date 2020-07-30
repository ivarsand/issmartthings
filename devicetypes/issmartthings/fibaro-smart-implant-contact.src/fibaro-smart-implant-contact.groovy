/**
 *  Fibaro Smart Implant Contact
 *  (Model FGBS-222)
 *
 * 	Author: Ovidiu Pruteanu (ovidiupruteanu)
 *
 * 	Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * 	in compliance with the License. You may obtain a copy of the License at:
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * 	Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * 	on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 * 	for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
    definition(name: "Fibaro Smart Implant Contact", namespace: "issmartthings", author: "ivarsand") {
        capability "Contact Sensor"
        capability "Sensor"
		capability "Actuator"
        capability "Button"
		capability "Configuration" 
       	//capability "Refresh"
        //capability "Scene Controller"
        
    }

    simulator {
    }

    tiles(scale: 2) {
        multiAttributeTile(name: "buttonStatus", type: "lighting", width: 6, height: 4, canChangeIcon: false) {
            tileAttribute("device.buttonStatus", key: "PRIMARY_CONTROL") {
                attributeState("default", label: 'Released', action: "momentary.push", backgroundColor: "#ffffff", icon: "st.unknown.zwave.remote-controller")
                attributeState("held", label: 'Held', backgroundColor: "#00a0dc", icon: "st.unknown.zwave.remote-controller")
                attributeState("single-clicked", label: 'Single-clicked', backgroundColor: "#00a0dc", icon: "st.unknown.zwave.remote-controller")
                attributeState("double-clicked", label: 'Double-clicked', backgroundColor: "#00a0dc", icon: "st.unknown.zwave.remote-controller")
                attributeState("released", label: 'Released', action: "momentary.push", backgroundColor: "#ffffff", icon: "st.unknown.zwave.remote-controller")
            }
            tileAttribute("device.lastPressed", key: "SECONDARY_CONTROL") {
                attributeState "lastPressed", label: 'Last Pressed: ${currentValue}'
            }
        }
        standardTile("configure", "device.configure", width: 2, height: 2, decoration: "flat") {
			state "default", label: "", icon:"st.secondary.configure", backgroundColor: "#ffffff", action: "configuration.configure"
        }

        main "buttonStatus"
        details(["buttonStatus", "configure"])
    }
    
    preferences {

        section { // GENERAL:
            input (
                type: "paragraph",
                element: "paragraph",
                title: "GENERAL:",
                description: "General device handler settings."
            )

            input (
                name: "configLoggingLevelIDE",
                title: "IDE Live Logging Level: Messages with this level and higher will be logged to the IDE.",
                type: "enum",
                options: [
                    "0" : "None",
                    "1" : "Error",
                    "2" : "Warning",
                    "3" : "Info",
                    "4" : "Debug",
                    "5" : "Trace"
                ],
                defaultValue: "5", // iPhone users can uncomment these lines!
                required: true
            )

            input (
                name: "configLoggingLevelDevice",
                title: "Device Logging Level: Messages with this level and higher will be logged to the logMessage attribute.",
                type: "enum",
                options: [
                    "0" : "None",
                    "1" : "Error",
                    "2" : "Warning"
                ],
                defaultValue: "2", // iPhone users can uncomment these lines!
                required: true
            )
		}

        generatePrefsParams()

        generatePrefsAssocGroups()

    }
    
}

def parse(String description) {
	def results = []
     log.debug("RAW command: $description")
	if (description.startsWith("Err")) {
		log.debug("An error has occurred")
        // updateStatus() Inherited from other DH
 		} 
    else {
       	def cmd = zwave.parse(description.replace("98C1", "9881"), [0x98: 1, 0x20: 1, 0x84: 1, 0x80: 1, 0x60: 3, 0x2B: 1, 0x26: 1])
        log.debug "Parsed Command: $cmd"
        if (cmd) {
       	results = zwaveEvent(cmd)
        //updateStatus() Inherited from other DH
        }
    }
}

/*****************************************************************************************************************
 *  Z-wave Event Handlers.
 *****************************************************************************************************************/

def zwaveEvent(physicalgraph.zwave.commands.notificationv3.NotificationReport cmd) {
	logger("Notification Report")
    if (cmd.event == 2) {
        sendEvent(name: "contact", value: "open", descriptionText: "$device.displayName is open")
    } else if (cmd.event == 0) {
        sendEvent(name: "contact", value: "closed", descriptionText: "$device.displayName is closed")
    }
}

def zwaveEvent(physicalgraph.zwave.commands.centralscenev1.CentralSceneNotification cmd) {
	//log.debug( "sceneNumber: $cmd.sceneNumber  keyAttributes: $cmd.keyAttributes")
    pushed(cmd.sceneNumber, cmd.keyAttributes)
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
    // Handles all Z-Wave commands we aren't interested in
    //parse(cmd)
    logger("Other command")
    [:]
}

/*****************************************************************************************************************
 *  Button handling
 *****************************************************************************************************************/

def pushed(int button, int action) {
    final String[] actionsDescription = ['pushed', 'released', 'held']
    
    String motionDescription = actionsDescription[action]

	sendEvent(name: "button", value: "$motionDescription", data: [buttonNumber: button, action: "$motion"], source: "COMMAND", descriptionText: "$device.displayName button $button was $motionDescription", isStateChange: true)
    sendEvent(name: "Button Events", value: "#$button $motionDescription" as String, descriptionText: "$device.displayName button $button was $motionDescription", isStateChange: true, displayed: false)

	logger( "$device.displayName Button $button was $motionDescription", "debug")

}


/*****************************************************************************************************************
 *  SmartThings System Commands:
 *****************************************************************************************************************/

def installed() {
    /**
     *  Runs when the device is first installed.
     *
     *  Action: Set initial values for internal state, and request a full configuration report from the device.
     **/
    log.trace "installed()"

    state.installedAt = now()
    state.loggingLevelIDE     = 5
    state.loggingLevelDevice  = 2
    state.protectLocalTarget  = 0
    state.protectRFTarget     = 0
    state.numberOfButtons     = 2

    //sendEvent(name: "fault", value: "clear", descriptionText: "Fault cleared", displayed: false)

    refreshConfig()
}

def updated() {
    /**
     *  Runs when the user hits "Done" from Settings page.
     *
     *  Action: Process new settings, sync parameters and association group members with the physical device. Request
     *  Firmware Metadata, Manufacturer-Specific, and Version reports.
     *
     *  Note: Weirdly, update() seems to be called twice. So execution is aborted if there was a previous execution
     *  within two seconds. See: https://community.smartthings.com/t/updated-being-called-twice/62912
     **/
    logger("updated()","trace")

    def cmds = []

    if (!state.updatedLastRanAt || now() >= state.updatedLastRanAt + 2000) {
        state.updatedLastRanAt = now()

        // Update internal state:
        state.loggingLevelIDE     = settings.configLoggingLevelIDE.toInteger()
        state.loggingLevelDevice  = settings.configLoggingLevelDevice.toInteger()
        state.syncAll             = ("true" == settings.configSyncAll)
        state.proactiveReports    = ("true" == settings.configProactiveReports)

        // Update Parameter target values:
        getParamsMd().findAll( {!it.readonly} ).each { // Exclude readonly parameters.
            state."paramTarget${it.id}" = settings."configParam${it.id}"?.toInteger()
        }

        // Update Assoc Group target values:
        state.assocGroupTarget1 = [ zwaveHubNodeId ] // Assoc Group #1 is Lifeline and will contain controller only.
        getAssocGroupsMd().findAll( { it.id != 1} ).each {
            state."assocGroupTarget${it.id}" = parseAssocGroupInput(settings."configAssocGroup${it.id}", it.maxNodes)
        }


        // Sync configuration with phyiscal device:
        sync()
 
        // Request device medadata (this just seems the best place to do it):
        cmds << zwave.versionV1.versionGet()
    	cmds << zwave.batteryV1.batteryGet() //.format()

        return response(secureSequence(cmds))
    }
    else {
        logger("updated(): Ran within last 2 seconds so aborting.","debug")
    }
}

def configure() {
	logger("Configure")
}


/*****************************************************************************************************************
 *  Private Helper Functions:
 *****************************************************************************************************************/

private logger(msg, level = "debug") {
    /**
     *  Wrapper function for all logging:
     *    Logs messages to the IDE (Live Logging), and also keeps a historical log of critical error and warning
     *    messages by sending events for the device's logMessage attribute.
     *    Configured using configLoggingLevelIDE and configLoggingLevelDevice preferences.
     **/
    switch(level) {
        case "error":
            if (state.loggingLevelIDE >= 1) log.error msg
            if (state.loggingLevelDevice >= 1) sendEvent(name: "logMessage", value: "ERROR: ${msg}", displayed: false, isStateChange: true)
            break

        case "warn":
            if (state.loggingLevelIDE >= 2) log.warn msg
            if (state.loggingLevelDevice >= 2) sendEvent(name: "logMessage", value: "WARNING: ${msg}", displayed: false, isStateChange: true)
            break

        case "info":
            if (state.loggingLevelIDE >= 3) log.info msg
            break

        case "debug":
            if (state.loggingLevelIDE >= 4) log.debug msg
            break

        case "trace":
            if (state.loggingLevelIDE >= 5) log.trace msg
            break

        default:
            log.debug msg
            break
    }
}

private parseAssocGroupInput(string, maxNodes) {
    /**
     *  Converts a comma-delimited string of destinations (nodes and endpoints) into an array suitable for passing to
     *  multiChannelAssociationSet(). All numbers are interpreted as hexadecimal. Anything that's not a valid node or
     *  endpoint is discarded (warn). If the list has more than maxNodes, the extras are discarded (warn).
     *
     *  Example input strings:
     *    "9,A1"      = Nodes: 9 & 161 (no multi-channel endpoints)            => Output: [9, 161]
     *    "7,8:1,8:2" = Nodes: 7, Endpoints: Node8:endpoint1 & node8:endpoint2 => Output: [7, 0, 8, 1, 8, 2]
     */
    logger("parseAssocGroupInput(): Parsing Association Group Nodes: ${string}","trace")

    // First split into nodes and endpoints. Count valid entries as we go.
    if (string) {
        def nodeList = string.split(',')
        def nodes = []
        def endpoints = []
        def count = 0

        nodeList = nodeList.each { node ->
            node = node.trim()
            if ( count >= maxNodes) {
                logger("parseAssocGroupInput(): Number of nodes and endpoints is greater than ${maxNodes}! The following node was discarded: ${node}","warn")
            }
            else if (node.matches("\\p{XDigit}+")) { // There's only hexadecimal digits = nodeId
                def nodeId = Integer.parseInt(node,16)  // Parse as hex
                if ( (nodeId > 0) & (nodeId < 256) ) { // It's a valid nodeId
                    nodes << nodeId
                    count++
                }
                else {
                    logger("parseAssocGroupInput(): Invalid nodeId: ${node}","warn")
                }
            }
            else if (node.matches("\\p{XDigit}+:\\p{XDigit}+")) { // endpoint e.g. "0A:2"
                def endpoint = node.split(":")
                def nodeId = Integer.parseInt(endpoint[0],16) // Parse as hex
                def endpointId = Integer.parseInt(endpoint[1],16) // Parse as hex
                if ( (nodeId > 0) & (nodeId < 256) & (endpointId > 0) & (endpointId < 256) ) { // It's a valid endpoint
                    endpoints.addAll([nodeId,endpointId])
                    count++
                }
                else {
                    logger("parseAssocGroupInput(): Invalid endpoint: ${node}","warn")
                }
            }
            else {
                logger("parseAssocGroupInput(): Invalid nodeId: ${node}","warn")
            }
        }

        return (endpoints) ? nodes + [0] + endpoints : nodes
    }
    else {
        return []
    }
}

private sync(forceAll = false) {
    /**
     *  Manages synchronisation of parameters, association groups, and protection state with the physical device.
     *  The syncPending attribute advertises remaining number of sync operations.
     *
     *  Does not return a list of commands, it sends them immediately using sendSecureSequence(). This is required if
     *  triggered by schedule().
     *
     *  Parameters:
     *   forceAll    Force all items to be synced, otherwise only changed items will be synced.
     **/
    logger("sync(): Syncing configuration with the physical device.","info")

    def cmds = []
    def syncPending = 0

    if (forceAll) { // Clear all cached values.
        getParamsMd().findAll( {!it.readonly} ).each { state."paramCache${it.id}" = null }
        getAssocGroupsMd().each { state."assocGroupCache${it.id}" = null }
        state.protectLocalCache = null
        state.protectRFCache = null
    }

    getParamsMd().findAll( {!it.readonly} ).each { // Exclude readonly parameters.
        if ( (state."paramTarget${it.id}" != null) & (state."paramCache${it.id}" != state."paramTarget${it.id}") ) {
            cmds << zwave.configurationV1.configurationSet(parameterNumber: it.id, size: it.size, scaledConfigurationValue: state."paramTarget${it.id}".toInteger())
            cmds << zwave.configurationV1.configurationGet(parameterNumber: it.id)
            logger("sync(): Syncing parameter #${it.id} [${it.name}]: New Value: " + state."paramTarget${it.id}","info")
            syncPending++
            }
    }

    getAssocGroupsMd().each {
        def cachedNodes = state."assocGroupCache${it.id}"
        def targetNodes = state."assocGroupTarget${it.id}"

        if ( cachedNodes != targetNodes ) {
            // Display to user in hex format (same as IDE):
            def targetNodesHex  = []
            targetNodes.each { targetNodesHex.add(String.format("%02X", it)) }
            logger("sync(): Syncing Association Group #${it.id}: Destinations: ${targetNodesHex}","info")

            cmds << zwave.multiChannelAssociationV2.multiChannelAssociationRemove(groupingIdentifier: it.id, nodeId: []) // Remove All
            cmds << zwave.multiChannelAssociationV2.multiChannelAssociationSet(groupingIdentifier: it.id, nodeId: targetNodes)
            cmds << zwave.multiChannelAssociationV2.multiChannelAssociationGet(groupingIdentifier: it.id)
            syncPending++
        }
    }

/* From fibaro dimmer
    if ( (state.protectLocalTarget != null) & (state.protectRFTarget != null)
      & ( (state.protectLocalCache != state.protectLocalTarget) || (state.protectRFCache != state.protectRFTarget) ) ) {

        logger("sync(): Syncing Protection State: Local Protection: ${state.protectLocalTarget}, RF Protection: ${state.protectRFTarget}","info")
        cmds << zwave.protectionV2.protectionSet(localProtectionState : state.protectLocalTarget, rfProtectionState: state.protectRFTarget)
        cmds << zwave.protectionV2.protectionGet()
        syncPending++
    }
*/
    sendEvent(name: "syncPending", value: syncPending, displayed: false)
    sendSecureSequence(cmds,1000) // Need a delay of at least 1000ms.
}

private refreshConfig() {
    /**
     *  Request configuration reports from the physical device: [ Configuration, Association, Protection,
     *   SecuritySupportedCommands, Powerlevel, Manufacturer-Specific, Firmware Metadata, Version, etc. ]
     *
     *  Really only needed at installation or when debugging, as sync will request the necessary reports when the
     *  configuration is changed.
     */
    logger("refreshConfig()","trace")

    def cmds = []

    getParamsMd().each { cmds << zwave.configurationV1.configurationGet(parameterNumber: it.id) }
    getAssocGroupsMd().each { cmds << zwave.multiChannelAssociationV2.multiChannelAssociationGet(groupingIdentifier: it.id) }

    cmds << zwave.protectionV2.protectionGet()
    cmds << zwave.securityV1.securityCommandsSupportedGet()
    cmds << zwave.manufacturerSpecificV2.manufacturerSpecificGet()
    cmds << zwave.firmwareUpdateMdV2.firmwareMdGet()
    cmds << zwave.versionV1.versionGet()
    cmds << zwave.powerlevelV1.powerlevelGet()

    sendSecureSequence(cmds, 1000) // Delay must be at least 1000 to reliabilty get all results processed.
}

private secure(physicalgraph.zwave.Command cmd) {
    /**
     *  Secures and formats a command using securityMessageEncapsulation.
     *
     *  Note: All commands are secured, there is little benefit to not securing commands that are not in
     *  state.secureCommandClasses.
     **/
    //if ( state.secureCommandClasses.contains(cmd.commandClassId.toInteger()) ) {...
    return zwave.securityV1.securityMessageEncapsulation().encapsulate(cmd).format()
}

private secure(str) {
    /**
     *  Secures and formats a command using securityMessageEncapsulation.
     *
     *  Note: All commands are secured, there is little benefit to not securing commands that are not in
     *  state.secureCommandClasses.
     **/
    //if ( state.secureCommandClasses.contains(cmd.commandClassId.toInteger()) ) {...
    //return zwave.securityV1.securityMessageEncapsulation().encapsulate(cmd).format()
    logger("secure(): ${str}", "info")
    return
}


private secureSequence(commands, delay = 200) {
    /**
     *  Secure an array of commands. Returns a list of formatted commands.
     **/
    return delayBetween(commands.collect{ secure(it) }, delay)
}

private sendSecureSequence(commands, delay = 200) {
    /**
     *  Secure an array of commands and send them using sendHubCommand.
     **/
    sendHubCommand(commands.collect{ response(secure(it)) }, delay)
}

private generatePrefsParams() {
    /**
     *  Generates preferences (settings) for device parameters.
     **/
        section {
            input (
                type: "paragraph",
                element: "paragraph",
                title: "DEVICE PARAMETERS:",
                description: "Device parameters are used to customise the physical device. " +
                             "Refer to the product documentation for a full description of each parameter."
            )

    getParamsMd().findAll( {!it.readonly} ).each { // Exclude readonly parameters.

        def lb = (it.description.length() > 0) ? "\n" : ""

        switch(it.type) {
            case "number":
            input (
                name: "configParam${it.id}",
                title: "#${it.id}: ${it.name}: \n" + it.description + lb +"Default Value: ${it.defaultValue}",
                type: it.type,
                range: it.range,
//                defaultValue: it.defaultValue, // iPhone users can uncomment these lines!
                required: it.required
            )
            break

            case "enum":
            input (
                name: "configParam${it.id}",
                title: "#${it.id}: ${it.name}: \n" + it.description + lb + "Default Value: ${it.defaultValue}",
                type: it.type,
                options: it.options,
//                defaultValue: it.defaultValue, // iPhone users can uncomment these lines!
                required: it.required
            )
            break
        }
    }
        } // section
}

private generatePrefsAssocGroups() {
    /**
     *  Generates preferences (settings) for Association Groups.
     **/
    section {
        input (
                type: "paragraph",
                element: "paragraph",
                title: "ASSOCIATION GROUPS:",
                description: "Association groups enable the dimmer to control other Z-Wave devices directly, " +
                             "without participation of the main controller.\n" +
                             "Enter a comma-delimited list of destinations (node IDs and/or endpoint IDs) for " +
                             "each association group. All IDs must be in hexadecimal format. E.g.:\n" +
                             "Node destinations: '11, 0F'\n" +
                             "Endpoint destinations: '1C:1, 1C:2'"
        )

	getAssocGroupsMd().findAll( { it.id != 1} ).each { // Don't show AssocGroup1 (Lifeline).
            input (
                name: "configAssocGroup${it.id}",
                title: "Association Group #${it.id}: ${it.name}: \n" + it.description + " \n[MAX NODES: ${it.maxNodes}]",
                type: "text",
                defaultValue: "", // iPhone users can uncomment these lines!
                required: false
            )
        }
    }
}

/*****************************************************************************************************************
 *  Static Matadata Functions:
 *
 *  These functions encapsulate metadata about the device. Mostly obtained from:
 *   Z-wave Alliance Reference for Fibaro Dimmer 2: http://products.z-wavealliance.org/products/1729
 *****************************************************************************************************************/

private getCommandClassVersions() {
    /** 
     *  Returns a map of the command class versions supported by the device. Used by parse() and zwaveEvent() to
     *  extract encapsulated commands from MultiChannelCmdEncap, MultiInstanceCmdEncap, SecurityMessageEncapsulation,
     *  and Crc16Encap messages.
     *  Reference: http://products.z-wavealliance.org/products/1729/classes
     **/
    return [0x20: 1, // Basic V1
            0x22: 1, // Application Status V1
            0x25: 1, // Switch Binary
            //0x26: 3, // Switch Multilevel V3
            //0x27: 1, // Switch All V1
            //0x28: 1, // Switch Toggle Binary
            //0x2B: 1, // Scene Activation V1
            0x31: 11, // Sensor Multilevel V4
            //0x32: 3, // Meter V3
            0x55: 2, // Transport Service
            0x56: 1, // CRC16 Encapsulation V1
            0x59: 2, // Association Group Information V1
            0x5A: 1, // Device Reset Locally V1
            0x5B: 3, // Central Scene
            0x5E: 2, // Z-Wave Plus Info V2 (Not supported by SmartThings)
            0x60: 3, // Multi Channel V4 (Device supports V4, but SmartThings only supports V3)
            0x6C: 1, // Supervision
            0x70: 1, // Configuration V1
            0x71: 3, // Notification V8 ((Device supports V8, but SmartThings only supports V3)
            0x72: 2, // Manufacturer Specific V2
            0x73: 1, // Powerlevel V1
            0x75: 2, // Protection V2
            0x7A: 2, // Firmware Update MD V4 (Device supports V4, but SmartThings only supports V2)
            //0x80: 1, // Battery
            //0x84: 1, // Wake Up
            0x85: 2, // Association V2
            0x86: 1, // Version V2 (Device supports V2, but SmartThings only supports V1)
            0x8E: 3, // Multi Channel Association V3 (Device supports V3, but SmartThings only supports V2)
            0x98: 1, // Security V1
            0x9F: 1  // Security 2 V1            
           ]
}

private getParamsMd() {
    /** 
     *  Returns device parameters metadata. Used by sync(), updateSyncPending(),  and generatePrefsParams().
     *  Reference: http://products.z-wavealliance.org/products/1729/configs
     **/
    return [
        [id: 20, size: 1, type: "enum", defaultValue: "2", required: false, readonly: false,
         name: "Input 1 - operating mode",
         description : "Choose mode of 1st input (IN1). Change it depending on connected device.",
         options: ["0" : "0 – Normally closed alarm input (Notification)",
         	 	   "1" : "1 – Normally open alarm input (Notification)",
				   "2" : "2 – Monostable button (Central Scene)",
                   "3" : "3 – Bistable button (Central Scene)",
                   "4" : "4 – Analog input without internal pull-up (Sensor Multilevel)",
                   "5" : "5 – Analog input with internal pullup (Sensor Multilevel)",
                   ]],
        [id: 21, size: 1, type: "enum", defaultValue: "2", required: false, readonly: false,
         name: "Input 2 - operating mode",
         description : "Choose mode of 1st input (IN1). Change it depending on connected device.",
         options: ["0" : "0 – Normally closed alarm input (Notification)",
         	 	   "1" : "1 – Normally open alarm input (Notification)",
				   "2" : "2 – Monostable button (Central Scene)",
                   "3" : "3 – Bistable button (Central Scene)",
                   "4" : "4 – Analog input without internal pull-up (Sensor Multilevel)",
                   "5" : "5 – Analog input with internal pullup (Sensor Multilevel)",]],

        [id: 40, size: 1, type: "enum", defaultValue: "0", required: false, readonly: false,
         name: "Input 1 - sent scenes",
         description : "Choose mode of 1st input (IN1). Change it depending on connected device.",
         options: ["0" : "0 – (no scenes sent)",
         	 	   "1" : "1 – Key pressed 1 time",
				   "2" : "2 – Key pressed 2 times",
                   "4" : "4 – Key pressed 3 times",
                   "8" : "8 – Key hold down and key released",
                   ]],
        [id: 41, size: 1, type: "enum", defaultValue: "0", required: false, readonly: false,
         name: "Input 2 - sent scenes",
         description : "Choose mode of 1st input (IN2). Change it depending on connected device.",
         options: ["0" : "0 – (no scenes sent)",
         	 	   "1" : "1 – Key pressed 1 time",
				   "2" : "2 – Key pressed 2 times",
                   "4" : "4 – Key pressed 3 times",
                   "8" : "8 – Key hold down and key released",
                   ]],
	]
}

private getAssocGroupsMd() {
    /** 
     *  Returns association groups metadata. Used by sync(), updateSyncPending(), and generatePrefsAssocGroups().
     *  Reference: http://products.z-wavealliance.org/products/1729/assoc
     **/
    return [
        [id:  1, maxNodes: 1, name: "Lifeline",
         description : "Reports device state. Main Z-Wave controller should be added to this group."],

		[id:  2, maxNodes: 5, name: "On/Off (IN1)",
         description : "Send On/Off based on IN1 state change."],
		[id:  3, maxNodes: 5, name: "On/Off (IN2)",
         description : "Send On/Off based on IN2 state change."],
	]
}