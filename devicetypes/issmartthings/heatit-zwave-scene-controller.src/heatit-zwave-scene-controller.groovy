/**
 *  HeatIt Z-wave Scene Controller
 *  Copyright 2018 Ivar Sandstad
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
	definition (name: "HeatIt Z-wave Scene Controller", namespace: "issmartthings", author: "ivarsand") {
        capability "Actuator"
        capability "Button"
        capability "Configuration"
        capability "Holdable Button"
        capability "Indicator"
        capability "Switch"
        capability "Sensor"
        capability "Health Check"

        attribute "sequenceNumber", "number"
        attribute "numberOfButtons", "number"

        fingerprint mfr: "0234", prod: "0003", model: "010C"

		//fingerprint deviceId: "0x010C", inClusters: "0x5E,0x86,0x72,0x5A,0x73,0x7A,0x85,0x59,0x8E,0x70,0x60,0x25,0x28,0x26,0x5B,0x87"
	}

	simulator {
		status "button 1 pushed":  "command: 5B03, payload: 2B 00 01"
		status "button 1 held":  "command: 5B03, payload: 2B 02 01"
        status "button 1 released":  "command: 5B03, payload: 2C 01 01"
        status "button 1 double":  "command: 5B03, payload: 2F 03 01"
		status "button 2 pushed":  "command: 5B03, payload: 23 00 02"
		status "button 2 held":  "command: 5B03, payload: 2B 02 02"
        status "button 2 released":  "command: 5B03, payload: 2C 01 02"
        status "button 2 double":  "command: 5B03, payload: 2F 03 02"
		status "button 3 pushed":  "command: 5B03, payload: 23 00 03"
		status "button 3 held":  "command: 5B03, payload: 2B 02 03"
        status "button 3 released":  "command: 5B03, payload: 2C 01 03"
        status "button 3 double":  "command: 5B03, payload: 2F 03 03"
		status "button 4 pushed":  "command: 5B03, payload: 23 00 04"
		status "button 4 held":  "command: 5B03, payload: 2B 02 04"
        status "button 4 released":  "command: 5B03, payload: 2C 01 04"
        status "button 4 double":  "command: 5B03, payload: 2F 03 04"
        status "button 5 pushed":  "command: 5B03, payload: 23 00 05"
		status "button 5 held":  "command: 5B03, payload: 2B 02 05"
        status "button 5 released":  "command: 5B03, payload: 2C 01 05"
        status "button 5 double":  "command: 5B03, payload: 2F 03 05"
		status "button 6 pushed":  "command: 5B03, payload: 23 00 06"
		status "button 6 held":  "command: 5B03, payload: 2B 02 06"
        status "button 6 released":  "command: 5B03, payload: 2C 01 06"
        status "button 6 double":  "command: 5B03, payload: 2F 03 06"
	}
	tiles (scale: 2) {
		multiAttributeTile(name:"switch", type: "generic", width: 6, height: 4, canChangeIcon: true){
			tileAttribute("device.switch", key: "PRIMARY_CONTROL") {
				attributeState("on", label: '${name}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00a0dc")
				attributeState("off", label: '${name}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff")
			}
		}


		standardTile("B1", "device.button", width: 2, height: 2, decoration: "flat") {
			state "default", label: 'Button 1X', icon: "st.unknown.zwave.remote-controller"
		}
		standardTile("B1.push", "device.button", width: 2, height: 2, decoration: "flat") {
			state "default", label: 'Push', action: "push1"
		}
		standardTile("B1.hold", "device.button", width: 2, height: 2, decoration: "flat") {
			state "default", label: 'Hold', action: "hold1"
		}
		standardTile("B2", "device.button", width: 2, height: 2, decoration: "flat") {
			state "default", label: 'Button 2X', icon: "st.unknown.zwave.remote-controller"
		}
		standardTile("B2.push", "device.button", width: 2, height: 2, decoration: "flat") {
			state "default", label: 'Push', action: "push2"
		}
		standardTile("B2.hold", "device.button", width: 2, height: 2, decoration: "flat") {
			state "default", label: 'Hold', action: "hold2"
		}

		standardTile("B3", "device.button", width: 2, height: 2, decoration: "flat") {
			state "default", label: 'Button 3', icon: "st.unknown.zwave.remote-controller"
		}
		standardTile("B3.push", "device.button", width: 2, height: 2, decoration: "flat") {
			state "default", label: 'Push', action: "push3"
		}
		standardTile("B3.hold", "device.button", width: 2, height: 2, decoration: "flat") {
			state "default", label: 'Hold', action: "hold3"
		}
		standardTile("B4", "device.button", width: 2, height: 2, decoration: "flat") {
			state "default", label: 'Button 4', icon: "st.unknown.zwave.remote-controller"
		}
		standardTile("B4.push", "device.button", width: 2, height: 2, decoration: "flat") {
			state "default", label: 'Push', action: "push4"
		}
		standardTile("B4.hold", "device.button", width: 2, height: 2, decoration: "flat") {
			state "default", label: 'Hold', action: "hold4"
		}
		standardTile("B5", "device.button", width: 2, height: 2, decoration: "flat") {
			state "default", label: 'Button 5', icon: "st.unknown.zwave.remote-controller"
		}
		standardTile("B5.push", "device.button", width: 2, height: 2, decoration: "flat") {
			state "default", label: 'Push', action: "push5"
		}
		standardTile("B5.hold", "device.button", width: 2, height: 2, decoration: "flat") {
			state "default", label: 'Hold', action: "hold5"
		}
		standardTile("B6", "device.button", width: 2, height: 2, decoration: "flat") {
			state "default", label: 'Button 6', icon: "st.unknown.zwave.remote-controller"
		}
		standardTile("B6.push", "device.button", width: 2, height: 2, decoration: "flat") {
			state "default", label: 'Push', action: "push6"
		}
		standardTile("B6.hold", "device.button", width: 2, height: 2, decoration: "flat") {
			state "default", label: 'Hold', action: "hold6"
		}
		
standardTile("configure", "device.configure", inactiveLabel: false, width: 1, height: 1, decoration: "flat") {
			state "configure", label:'', action:"configuration.configure", icon:"st.secondary.configure"
		}
		main "switch"
		details(["switch", "B1", "B1.push",  "B1.hold", "B2", "B2.push",  "B2.hold",
                 "B3", "B3.push",  "B3.hold", "B4", "B4.push",  "B4.hold",
                 "B5", "B5.push",  "B5.hold", "B6", "B6.push",  "B6.hold", "configure"])
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
                defaultValue: "3", // iPhone users can uncomment these lines!
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

// parse events into attributes
def parseXX(String description) {
	log.debug('parse')
	def result = null
    def cmd = zwave.parse(description)
    if (cmd) {
        result = zwaveEvent(cmd)
        log.debug "Parsed ${cmd} to ${result.inspect()}"
    } else {
        log.debug "Non-parsed event: ${description}"
    }
    return result
}

def parse(String description) {
	def results = []
     logging("${description}")
	if (description.startsWith("Err")) {
	    results = createEvent(descriptionText:description, displayed:true)
	} else {
		//def cmd = zwave.parse(description, [0x2B: 1, 0x80: 1, 0x84: 1])
		def cmd = zwave.parse(description) //, [0x2B: 1, 0x80: 1, 0x84: 1])
		if(cmd) results += zwaveEvent(cmd)
		if(!results) results = [ descriptionText: cmd, displayed: false ]
	}

    if(state.isConfigured != "true") configure()

	return results
}

def zwaveEvent(physicalgraph.zwave.commands.centralscenev1.CentralSceneNotification cmd) {
        logging(cmd)
        logging("keyAttributes: $cmd.keyAttributes")
        logging("sceneNumber: $cmd.sceneNumber")
        logging("sequenceNumber: $cmd.sequenceNumber")

        sendEvent(name: "sequenceNumber", value: cmd.sequenceNumber, displayed:false)
        switch (cmd.keyAttributes) {
           case 0:
              pushEvent(cmd.sceneNumber, "pushed")
           break
           case 1:
              if (settings.holdMode == "2") holdEvent(cmd.sceneNumber, "held")
           break
           case 2:
              if (!settings.holdMode || settings.holdMode == "1") holdEvent(cmd.sceneNumber, "held")
           break
           case 3:
              pushEvent(cmd.sceneNumber + 8, "pushed")
           break
           default:
               logging("Unhandled CentralSceneNotification: ${cmd}")
           break
        }
}

/**
 *  zwaveEvent( COMMAND_CLASS_SECURITY (0x98) : SECURITY_MESSAGE_ENCAPSULATION (0x81) )
 *
 *  The Security Message Encapsulation command is used to encapsulate Z-Wave commands using AES-128.
 *
 *  Action: Extract the encapsulated command and pass to the appropriate zwaveEvent() handler.
 *    Set state.useSecurity flag to true.
 *
 *  cmd attributes:
 *    List<Short> commandByte         Parameters of the encapsulated command.
 *    Short   commandClassIdentifier  Command Class ID of the encapsulated command.
 *    Short   commandIdentifier       Command ID of the encapsulated command.
 *    Boolean secondFrame             Indicates if first or second frame.
 *    Short   sequenceCounter
 *    Boolean sequenced               True if the command is transmitted using multiple frames.
 **/
def zwaveEvent(physicalgraph.zwave.commands.securityv1.SecurityMessageEncapsulation cmd) {
    log.debug "zwaveEvent(): Security Encapsulated Command received: ${cmd}"

    state.useSecurity = true

    def encapsulatedCommand = cmd.encapsulatedCommand(getCommandClassVersions())
    if (encapsulatedCommand) {
        return zwaveEvent(encapsulatedCommand)
    } else {
        logger("zwaveEvent(): Unable to extract security encapsulated command from: ${cmd}","error")
    }
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd)
{
   	log.debug "basic report"
   	def result = []
    result << createEvent(name:"switch", value: cmd.value ? "on" : "off")

    result
}

private def logging(message) {
    //if (settings.debug == "true") 
    log.debug "$message"
}


def pushEvent(button, value) {
	logging("pushEvent: ${value}")
	//createEvent(name: "button", value: value, data: [buttonNumber: button], descriptionText: "$device.displayName button $button was $value", isStateChange: true)
	sendEvent(name: "button", value: "pushed", data: [buttonNumber: button, action: "pushed"], source: "COMMAND", descriptionText: "$device.displayName button $button was pushed", isStateChange: true)
}
def holdEvent(button, value) {
	logging("holdEvent: ${value}")
	//createEvent(name: "button", value: value, data: [buttonNumber: button], descriptionText: "$device.displayName button $button was $value", isStateChange: true)
    sendEvent(name: "button", value: "held", data: [buttonNumber: button, action: "held"], source: "COMMAND", descriptionText: "$device.displayName button $button was held", isStateChange: true)
}


def push1()
{
	logging("push1")
	pushEvent(1, "pushed")
}
def push2()
{
	pushEvent(2, "pushed")
}
def push3()
{
	logging("push3")
	pushEvent(3, "pushed")
}
def push4()
{
	pushEvent(4, "pushed")
}
def push5()
{
	pushEvent(5, "pushed")
}
def push6()
{
	pushEvent(6, "pushed")
}
def hold1()
{
	holdEvent(1, "held")
}
def hold2()
{
	holdEvent(2, "held")
}
def hold3()
{
	holdEvent(3, "held")
}
def hold4()
{
	holdEvent(4, "held")
}
def hold5()
{
	holdEvent(5, "held")
}
def hold6()
{
	holdEvent(6, "held")
}



def zwaveEvent(physicalgraph.zwave.Command cmd) {
	logging("Unhandled zwaveEvent: ${cmd}")
}

def installed() {
    logging("installed()")
    configure()
}

def updated() {
    logging("updated()")
    configure()
}

def configure() {
	logging("configure()")
	if (!state.updatedLastRanAt || now() >= state.updatedLastRanAt + 2000) {
		state.updatedLastRanAt = now()

        // Update Parameter target values:
        getParamsMd().findAll( {!it.readonly} ).each { // Exclude readonly parameters.
            state."paramTarget${it.id}" = settings."configParam${it.id}"?.toInteger()
        }
/*
        // Update Assoc Group target values:
        state.assocGroupTarget1 = [ zwaveHubNodeId ] // Assoc Group #1 is Lifeline and will contain controller only.
        getAssocGroupsMd().findAll( { it.id != 1} ).each {
            state."assocGroupTarget${it.id}" = parseAssocGroupInput(settings."configAssocGroup${it.id}", it.maxNodes)
        }
*/
        //sendEvent(name: "checkInterval", value: 2 * 60 * 12 * 60 + 5 * 60, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID])
        sendEvent(name: "numberOfButtons", value: 6, displayed: true)
        
        // Sync configuration with phyiscal device:
        sync(state.syncAll)

		state.isConfigured = "true"
    }
	else {
		log.trace "updated(): Ran within last 2 seconds so aborting."
	}
    
}

def ping() {
    logging("ping()")
	logging("Is ping necessary?")
}


def on() {
	log.debug "ON"
    delayBetween([
    	zwave.basicV1.basicSet(value: 0xFF).format(),
        zwave.basicV1.basicGet().format(),
    ], 100)
}
def off() {
	log.debug "OFF"
    delayBetween([
    	zwave.basicV1.basicSet(value: 0x00).format(),
        zwave.basicV1.basicGet().format(),
    ], 100)
}
def refresh() {
	log.debug "REFRESH"
    delayBetween([
        zwave.basicV1.basicGet().format(),
    ], 100)
}
def poll() {
	log.debug "POLL"
    delayBetween([
        zwave.basicV1.basicGet().format(),
    ], 100)
}

/**
 *  sync()
 *
 *  Manages synchronisation of parameters, association groups, and protection state with the physical device.
 *  The syncPending attribute advertises remaining number of sync operations.
 *
 *  Does not return a list of commands, it sends them immediately using sendSecureSequence(). This is required if
 *  triggered by schedule().
 *
 *  Parameters:
 *   forceAll    Force all items to be synced, otherwise only changed items will be synced.
 **/
private sync(forceAll = false) {
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
/*
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

/**
 *  secure(cmd)
 *
 *  Secures and formats a command using securityMessageEncapsulation.
 *
 *  Note: All commands are secured, there is little benefit to not securing commands that are not in
 *  state.secureCommandClasses.
 **/
private secure(physicalgraph.zwave.Command cmd) {
    //if ( state.secureCommandClasses.contains(cmd.commandClassId.toInteger()) ) {...
    return zwave.securityV1.securityMessageEncapsulation().encapsulate(cmd).format()
}

/**
 *  secureSequence()
 *
 *  Secure an array of commands. Returns a list of formatted commands.
 **/
private secureSequence(commands, delay = 200) {
    return delayBetween(commands.collect{ secure(it) }, delay)
}

/**
 *  sendSecureSequence()
 *
 *  Secure an array of commands and send them using sendHubCommand.
 **/
private sendSecureSequence(commands, delay = 200) {
    sendHubCommand(commands.collect{ response(secure(it)) }, delay)
}

/**
 *  generatePrefsParams()
 *
 *  Generates preferences (settings) for device parameters.
 **/
private generatePrefsParams() {
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

/**
 *  generatePrefsAssocGroups()
 *
 *  Generates preferences (settings) for Association Groups.
 **/
private generatePrefsAssocGroups() {
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

/**
 *  logger()
 *
 *  Wrapper function for all logging:
 *    Logs messages to the IDE (Live Logging), and also keeps a historical log of critical error and warning
 *    messages by sending events for the device's logMessage attribute.
 *    Configured using configLoggingLevelIDE and configLoggingLevelDevice preferences.
 **/
private logger(msg, level = "debug") {

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

/*****************************************************************************************************************
 *  Static Matadata Functions:
 *
 *  These functions encapsulate metadata about the device. Mostly obtained from:
 *   Z-wave Alliance Reference for Fibaro Dimmer 2: http://products.z-wavealliance.org/products/1729
 *****************************************************************************************************************/

/**
 *  getCommandClassVersions()
 *
 *  Returns a map of the command class versions supported by the device. Used by parse() and zwaveEvent() to
 *  extract encapsulated commands from MultiChannelCmdEncap, MultiInstanceCmdEncap, SecurityMessageEncapsulation,
 *  and Crc16Encap messages.
 *
 *  Reference: http://products.z-wavealliance.org/products/1729/classes
 **/
private getCommandClassVersions() {
    return [0x20: 1, // Basic V1
            0x22: 1, // Application Status V1
            0x26: 3, // Switch Multilevel V3
            // xx 0x27: 1, // Switch All V1
            0x28: 1, // Switch Toggle Binary
/*            0x2B: 1, // Scene Activation V1
            0x31: 4, // Sensor Multilevel V4
            0x32: 3, // Meter V3
            0x56: 1, // CRC16 Encapsulation V1
*/
            0x59: 1, // Association Group Information V1
            0x5A: 1, // Device Reset Locally V1
            0x5B: 1, // Central Scene
            //0x5E: 2, // Z-Wave Plus Info V2 (Not supported by SmartThings)
            0x60: 3, // Multi Channel V4 (Device supports V4, but SmartThings only supports V3)
            0x70: 1, // Configuration V1
            // xx 0x71: 3, // Notification V5 ((Device supports V5, but SmartThings only supports V3)
            0x72: 2, // Manufacturer Specific V2
            0x73: 1, // Powerlevel V1
            // xx 0x75: 2, // Protection V2
            0x7A: 2, // Firmware Update MD V3 (Device supports V3, but SmartThings only supports V2)
            0x85: 2, // Association V2
            0x86: 1, // Version V2 (Device supports V2, but SmartThings only supports V1)
            0x8E: 2, // Multi Channel Association V3 (Device supports V3, but SmartThings only supports V2)
            // xx 0x98: 1  // Security V1
           ]
}

/**
 *  getParamsMd()
 *
 *  Returns device parameters metadata. Used by sync(), updateSyncPending(),  and generatePrefsParams().
 *
 *  Reference: http://products.z-wavealliance.org/products/1729/configs
 **/
private getParamsMd() {
    return [
        [id: 1, size: 1, type: "enum", defaultValue: "0", required: false, readonly: false,
         name: "Upper paddle buttons mode",
         description : "Configuration of Pair Mode for the upper two buttons (button #1 and #2)",
         options: ["0" : "0: Separate mode (toggle mode). (Default)",
                   "1" : "1: In pair mode, left side sends on/up commands, right side sends off/down commands"]],
        [id: 2, size: 1, type: "enum", defaultValue: "0", required: false, readonly: false,
         name: "Middle paddle buttons mode",
         description : "Configuration of Pair Mode for the upper two buttons (button #3 and #4)",
         options: ["0" : "0: Separate mode (toggle mode). (Default)",
                   "1" : "1: In pair mode, left side sends on/up commands, right side sends off/down commands"]],
        [id: 3, size: 1, type: "enum", defaultValue: "0", required: false, readonly: false,
         name: "Lower paddle buttons mode",
         description : "Configuration of Pair Mode for the upper two buttons (button #5 and #6)",
         options: ["0" : "0: Separate mode (toggle mode). (Default)",
                   "1" : "1: In pair mode, left side sends on/up commands, right side sends off/down commands"]],

        [id: 4, size: 1, type: "enum", defaultValue: "1", required: false, readonly: false,
         name: "LED #1 mode",
         description : "Configuration of the internal operation of LED #1",
		 options: ["0" : "0: LED indication is disabled (LED can be controlled by Indicator Command Class Set commands)",
		           "1" : "1: LED indicates the status of the corresponding button. (Default)",
		           "2" : "2: LED indicates the status of the corresponding button, with inverted indication",
		           "3" : "3: LED indicates the status of corresponding paddle (in pair mode)",
		           "4" : "4: LED indicates the status of the corresponding paddle (in pair mode), with inverted indication",
		           "5" : "5: LED indicates the status of the built-in relay",
		           "6" : "6: LED indicates the status of the built-in relay, with inverted indication",
		           "7" : "7: LED show a 5 seconds indication when the corresponding button is pressed"]],
        [id: 5, size: 1, type: "enum", defaultValue: "1", required: false, readonly: false,
         name: "LED #2 mode",
         description : "Configuration of the internal operation of LED #2",
		 options: ["0" : "0: LED indication is disabled (LED can be controlled by Indicator Command Class Set commands)",
		           "1" : "1: LED indicates the status of the corresponding button. (Default)",
		           "2" : "2: LED indicates the status of the corresponding button, with inverted indication",
		           "3" : "3: LED indicates the status of corresponding paddle (in pair mode)",
		           "4" : "4: LED indicates the status of the corresponding paddle (in pair mode), with inverted indication",
		           "5" : "5: LED indicates the status of the built-in relay",
		           "6" : "6: LED indicates the status of the built-in relay, with inverted indication",
		           "7" : "7: LED show a 5 seconds indication when the corresponding button is pressed"]],
        [id: 6, size: 1, type: "enum", defaultValue: "1", required: false, readonly: false,
         name: "LED #3 mode",
         description : "Configuration of the internal operation of LED #3",
		 options: ["0" : "0: LED indication is disabled (LED can be controlled by Indicator Command Class Set commands)",
		           "1" : "1: LED indicates the status of the corresponding button. (Default)",
		           "2" : "2: LED indicates the status of the corresponding button, with inverted indication",
		           "3" : "3: LED indicates the status of corresponding paddle (in pair mode)",
		           "4" : "4: LED indicates the status of the corresponding paddle (in pair mode), with inverted indication",
		           "5" : "5: LED indicates the status of the built-in relay",
		           "6" : "6: LED indicates the status of the built-in relay, with inverted indication",
		           "7" : "7: LED show a 5 seconds indication when the corresponding button is pressed"]],
        [id: 7, size: 1, type: "enum", defaultValue: "1", required: false, readonly: false,
         name: "LED #4 mode",
         description : "Configuration of the internal operation of LED #4",
		 options: ["0" : "0: LED indication is disabled (LED can be controlled by Indicator Command Class Set commands)",
		           "1" : "1: LED indicates the status of the corresponding button. (Default)",
		           "2" : "2: LED indicates the status of the corresponding button, with inverted indication",
		           "3" : "3: LED indicates the status of corresponding paddle (in pair mode)",
		           "4" : "4: LED indicates the status of the corresponding paddle (in pair mode), with inverted indication",
		           "5" : "5: LED indicates the status of the built-in relay",
		           "6" : "6: LED indicates the status of the built-in relay, with inverted indication",
		           "7" : "7: LED show a 5 seconds indication when the corresponding button is pressed"]],
        [id: 8, size: 1, type: "enum", defaultValue: "1", required: false, readonly: false,
         name: "LED #5 mode",
         description : "Configuration of the internal operation of LED #5",
		 options: ["0" : "0: LED indication is disabled (LED can be controlled by Indicator Command Class Set commands)",
		           "1" : "1: LED indicates the status of the corresponding button. (Default)",
		           "2" : "2: LED indicates the status of the corresponding button, with inverted indication",
		           "3" : "3: LED indicates the status of corresponding paddle (in pair mode)",
		           "4" : "4: LED indicates the status of the corresponding paddle (in pair mode), with inverted indication",
		           "5" : "5: LED indicates the status of the built-in relay",
		           "6" : "6: LED indicates the status of the built-in relay, with inverted indication",
		           "7" : "7: LED show a 5 seconds indication when the corresponding button is pressed"]],
        [id: 9, size: 1, type: "enum", defaultValue: "1", required: false, readonly: false,
         name: "LED #6 mode",
         description : "Configuration of the internal operation of LED #6",
		 options: ["0" : "0: LED indication is disabled (LED can be controlled by Indicator Command Class Set commands)",
		           "1" : "1: LED indicates the status of the corresponding button. (Default)",
		           "2" : "2: LED indicates the status of the corresponding button, with inverted indication",
		           "3" : "3: LED indicates the status of corresponding paddle (in pair mode)",
		           "4" : "4: LED indicates the status of the corresponding paddle (in pair mode), with inverted indication",
		           "5" : "5: LED indicates the status of the built-in relay",
		           "6" : "6: LED indicates the status of the built-in relay, with inverted indication",
		           "7" : "7: LED show a 5 seconds indication when the corresponding button is pressed"]],

        [id: 10, size: 1, type: "enum", defaultValue: "1", required: false, readonly: false,
         name: "Relay mode",
         description : "This parameter configures which of the buttons that shall control the built-in relay, or if the relay only will be activated for one second, each time button #1 is used.",
		 options: ["0" : "0: Relay is disabled",
		           "1" : "1: Relay is controlled by button #1 or by upper paddle (Default)",
		           "2" : "2 Relay is controlled by button #2 or by upper paddle",
		           "3" : "3 Relay is controlled by button #3 or by middle paddle",
		           "4" : "4 Relay is controlled by button #4 or by middle paddle",
		           "5" : "5 Relay is controlled by button #5 or by lower paddle",
		           "6" : "6 Relay is controlled by button #6 or by lower paddle",
		           "7" : "7 Relay is activated for one second and is controlled by button #1 or by upper paddle"]],

        [id: 11, size: 1, type: "enum", defaultValue: "0", required: false, readonly: false,
         name: "Disable Central Scene notification",
         description : "Disables or enables the sending of Central Scene notifications.",
		 options: ["0" : "0: Central Scene notifications are enabled. (Default)",
		           "1" : "1: Central Scene notifications are disabled."]],
        [id: 12, size: 1, type: "enum", defaultValue: "0", required: false, readonly: false,
         name: "Disable House Cleaning Mode",
         description : "Disable or enables that a double-press on a button will send a command to activate 100% light.",
		 options: ["0" : "0: House Cleaning Mode is enabled (Default)",
		           "1" : "1: House Cleaning Mode is disabled; double-press button scene notifications will be disabled too."]],

		// Missing 13-18  Associated multilevel switch
 
 		// Missing 19-24  Control of relaying to association groups

        [id: 25, size: 1, type: "enum", defaultValue: "1", required: false, readonly: false,
         name: "Backlight control",
         description : "Configuration of backlight control.",
		 options: ["0" : "0: Backlight is only controlled by Indicator Command Class – commands send to endpoint 0",
		           "1" : "1: Backlight is turned on for 5 seconds when a button is pressed. (Default)"]],
        [id: 26, size: 1, type: "enum", defaultValue: "0", required: false, readonly: false,
         name: "Basic Set for endpoint 0",
         description : "Enable or disable that Basic Set commands to the root device will be able to control the backlight.",
		 options: ["0" : "0: Basic Set commands to endpoint 0 is forwarded to endpoint 1 (Default)",
		           "1" : "1: Basic Set commands to endpoint 0 controls backlight (on/off)."]],

/*
        [id:  1, size: 1, type: "number", range: "1..98", defaultValue: 1, required: false, readonly: false,
         name: "Minimum Brightness Level",
         description: "Set automatically during the calibration process, but can be changed afterwards.\n" +
         "Values: 1-98 = Brightness level (%)"],
*/




    ]
}

/**
 *  getAssocGroupsMd()
 *
 *  Returns association groups metadata. Used by sync(), updateSyncPending(), and generatePrefsAssocGroups().
 *
 *  Reference: http://products.z-wavealliance.org/products/1729/assoc
 **/
private getAssocGroupsMd() {
    return [
        [id:  1, maxNodes: 1, name: "Lifeline",
         description : "Reports device state. Main Z-Wave controller should be added to this group."],

		[id:  2, maxNodes: 5, name: "Basic Report On/Off (B1)",
         description : "Send Basic Report (On/Off) when button #1 is used."],
        [id:  3, maxNodes: 5, name: "Basic Set On/Off (B1)",
         description : "Sends Basic Set (On/Off) when button #1 is used."],
        [id:  4, maxNodes: 5, name: "Binary Switch Set On/Off (B1)",
         description : "Binary Switch Set (On/Off) when button #1 is used."],
        [id:  5, maxNodes: 5, name: "Binary Toggle (B1)",
         description : "Send Binary Toggle Switch Set when button #1 is used."],
        [id:  6, maxNodes: 5, name: "Multilevel Start Stop (B1)",
         description : "Sends Multilevel Switch Set / Multilevel Switch Start Level Change / Multilevel Switch Stop Level Change when button #1 is used."],

		[id:  7, maxNodes: 5, name: "Basic Report On/Off (B2)",
         description : "Send Basic Report (On/Off) when button #2 is used."],
        [id:  8, maxNodes: 5, name: "Basic Set On/Off (B2)",
         description : "Sends Basic Set (On/Off) when button #2 is used."],
        [id:  9, maxNodes: 5, name: "Binary Switch Set On/Off (B2)",
         description : "Binary Switch Set (On/Off) when button #2 is used."],
        [id: 10, maxNodes: 5, name: "Binary Toggle (B2)",
         description : "Send Binary Toggle Switch Set when button #2 is used."],
        [id: 11, maxNodes: 5, name: "Multilevel Start Stop (B2)",
         description : "Sends Multilevel Switch Set / Multilevel Switch Start Level Change / Multilevel Switch Stop Level Change when button #2 is used."],

		[id: 12, maxNodes: 5, name: "Basic Report On/Off (B3)",
         description : "Send Basic Report (On/Off) when button #3 is used."],
        [id: 13, maxNodes: 5, name: "Basic Set On/Off (B3)",
         description : "Sends Basic Set (On/Off) when button #3 is used."],
        [id: 14, maxNodes: 5, name: "Binary Switch Set On/Off (B3)",
         description : "Binary Switch Set (On/Off) when button #3 is used."],
        [id: 15, maxNodes: 5, name: "Binary Toggle (B3)",
         description : "Send Binary Toggle Switch Set when button #3 is used."],
        [id: 16, maxNodes: 5, name: "Multilevel Start Stop (B3)",
         description : "Sends Multilevel Switch Set / Multilevel Switch Start Level Change / Multilevel Switch Stop Level Change when button #3 is used."],

		[id: 17, maxNodes: 5, name: "Basic Report On/Off (B4)",
         description : "Send Basic Report (On/Off) when button #4 is used."],
        [id: 18, maxNodes: 5, name: "Basic Set On/Off (B4)",
         description : "Sends Basic Set (On/Off) when button #4 is used."],
        [id: 19, maxNodes: 5, name: "Binary Switch Set On/Off (B4)",
         description : "Binary Switch Set (On/Off) when button #4 is used."],
        [id: 20, maxNodes: 5, name: "Binary Toggle (B4)",
         description : "Send Binary Toggle Switch Set when button #4 is used."],
        [id: 21, maxNodes: 5, name: "Multilevel Start Stop (B4)",
         description : "Sends Multilevel Switch Set / Multilevel Switch Start Level Change / Multilevel Switch Stop Level Change when button #4 is used."],

		[id: 22, maxNodes: 5, name: "Basic Report On/Off (B5)",
         description : "Send Basic Report (On/Off) when button #5 is used."],
        [id: 23, maxNodes: 5, name: "Basic Set On/Off (B5)",
         description : "Sends Basic Set (On/Off) when button #5 is used."],
        [id: 24, maxNodes: 5, name: "Binary Switch Set On/Off (B5)",
         description : "Binary Switch Set (On/Off) when button #5 is used."],
        [id: 25, maxNodes: 5, name: "Binary Toggle (B5)",
         description : "Send Binary Toggle Switch Set when button #5 is used."],
        [id: 26, maxNodes: 5, name: "Multilevel Start Stop (B5)",
         description : "Sends Multilevel Switch Set / Multilevel Switch Start Level Change / Multilevel Switch Stop Level Change when button #5 is used."],

		[id: 27, maxNodes: 5, name: "Basic Report On/Off (B6)",
         description : "Send Basic Report (On/Off) when button #6 is used."],
        [id: 28, maxNodes: 5, name: "Basic Set On/Off (B6)",
         description : "Sends Basic Set (On/Off) when button #6 is used."],
        [id: 29, maxNodes: 5, name: "Binary Switch Set On/Off (B6)",
         description : "Binary Switch Set (On/Off) when button #6 is used."],
        [id: 30, maxNodes: 5, name: "Binary Toggle (B6)",
         description : "Send Binary Toggle Switch Set when button #6 is used."],
        [id: 31, maxNodes: 5, name: "Multilevel Start Stop (B6)",
         description : "Sends Multilevel Switch Set / Multilevel Switch Start Level Change / Multilevel Switch Stop Level Change when button #6 is used."],
		 
	]
}