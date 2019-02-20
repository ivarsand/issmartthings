/**
 *  Copyright 2019 ivarsand
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
 * V0.1.0 2016/02/19
 *
 *
 * Changelog:
 *
 * 0.1.0 - Started changing from AdamV version 0.0.3
 */
 
/*    
def updated() {
	if (settings.tempSen == null) settings.tempSen = "A - Room temperature mode"
    configure()
}
*/ 
metadata {
	definition (name: "HeatIt Z-Wave Thermostat", namespace: "issmartthings", author: "ivarsand") {
		capability "Actuator"
		capability "Temperature Measurement"
		//capability "Relative Humidity Measurement"
		capability "Thermostat"
        capability "Thermostat Mode"
        capability "Thermostat Heating Setpoint"
        capability "Thermostat Setpoint"
		capability "Configuration"
		capability "Polling"
		capability "Sensor"
		
		//attribute "thermostatFanState", "string"

		command "switchMode"
        command "heat"
        command "energySaveHeat"
        command "off"
		//command "switchFanMode"
        //command "quickSetCool"
        command "quickSetHeat"
        command "quickSetecoHeat"
        command "pressUp"
        command "pressDown"

		fingerprint deviceId: "0x0806"
		fingerprint inClusters: "0x5E, 0x43, 0x31, 0x86, 0x40, 0x59, 0x85, 0x73, 0x72, 0x5A, 0x70"
	}

	// simulator metadata
	simulator {
		status "off"			: "command: 4003, payload: 00"
		status "heat"			: "command: 4003, payload: 01"
		status "cool"			: "command: 4003, payload: 02"
		status "auto"			: "command: 4003, payload: 03"
		status "emergencyHeat"	: "command: 4003, payload: 04"

		status "fanAuto"		: "command: 4403, payload: 00"
		status "fanOn"			: "command: 4403, payload: 01"
		status "fanCirculate"	: "command: 4403, payload: 06"

		status "heat 60"        : "command: 4303, payload: 01 09 3C"
		status "heat 68"        : "command: 4303, payload: 01 09 44"
		status "heat 72"        : "command: 4303, payload: 01 09 48"

		status "cool 72"        : "command: 4303, payload: 02 09 48"
		status "cool 76"        : "command: 4303, payload: 02 09 4C"
		status "cool 80"        : "command: 4303, payload: 02 09 50"

		status "temp 58"        : "command: 3105, payload: 01 2A 02 44"
		status "temp 62"        : "command: 3105, payload: 01 2A 02 6C"
		status "temp 70"        : "command: 3105, payload: 01 2A 02 BC"
		status "temp 74"        : "command: 3105, payload: 01 2A 02 E4"
		status "temp 78"        : "command: 3105, payload: 01 2A 03 0C"
		status "temp 82"        : "command: 3105, payload: 01 2A 03 34"

		status "idle"			: "command: 4203, payload: 00"
		status "heating"		: "command: 4203, payload: 01"
		status "cooling"		: "command: 4203, payload: 02"
		status "fan only"		: "command: 4203, payload: 03"
		status "pending heat"	: "command: 4203, payload: 04"
		status "pending cool"	: "command: 4203, payload: 05"
		status "vent economizer": "command: 4203, payload: 06"

		// reply messages
		reply "2502": "command: 2503, payload: FF"
	}

	tiles (scale: 2){
		
        multiAttributeTile(name:"thermostatMulti", type:"thermostat", width:6, height:4) {
            tileAttribute("device.temperature", key: "PRIMARY_CONTROL") {
                attributeState("default", label:'${currentValue}°', unit:"C", action:"switchMode", icon:"st.Home.home1")
            }
            tileAttribute("device.heatingSetpoint", key: "VALUE_CONTROL") {
                attributeState("VALUE_UP", action: "pressUp")
                attributeState("VALUE_DOWN", action: "pressDown")
            }
  			//tileAttribute("device.heatingSetpoint", key: "SECONDARY_CONTROL") {
            tileAttribute("device.tempSenseMode", key: "SECONDARY_CONTROL") {
                attributeState("default", label:'${currentValue}', unit:"", icon:" ")
                //attributeState("default", label:'${currentValue}°', unit:"°", icon:"st.Weather.weather2")
        
	        }
  			tileAttribute("device.thermostatOperatingState", key: "OPERATING_STATE") {
                attributeState("idle", backgroundColor:"#44b621")
                attributeState("heating", backgroundColor:"#bc2323")
                attributeState("energySaveHeat", backgroundColor:"#ffa81e")
            }
  			tileAttribute("device.thermostatMode", key: "THERMOSTAT_MODE") {
                attributeState("off", label:'${name}', action:"switchMode", nextState:"heat")
                attributeState("heat", label:'${name}', action:"switchMode", nextState:"energySaveHeat")
                attributeState("energySaveHeat", label:'${name}', action:"switchMode", nextState:"off")
            }
            tileAttribute("device.heatingSetpoint", key: "HEATING_SETPOINT") {
    		attributeState("default", label:'${currentValue}')
	  		}
		}
        /*
        valueTile("temperature", "device.temperature", width: 2, height: 2) {
			state("temperature", label:'${currentValue}°',
				backgroundColors:[
					[value: 31, color: "#153591"],
					[value: 44, color: "#1e9cbb"],
					[value: 59, color: "#90d2a7"],
					[value: 74, color: "#44b621"],
					[value: 84, color: "#f1d801"],
					[value: 95, color: "#d04e00"],
					[value: 96, color: "#bc2323"]
				]
			)
		}		
        */

        valueTile("heat", "device.thermostatMode", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "heat", label:'Heat', nextState:""
			state "off", label:'Heat', action:"heat", nextState:"to_heat"
			state "energySaveHeat", label: "Heat", action:"heat", nextState:"to_heat"
		}

        valueTile("mode", "device.thermostatMode", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "off", label:'${name}', action:"switchMode", nextState:"to_heat"
			state "heat", label:'${name}', action:"switchMode", nextState:"energySaveHeat"
			//state "cool", label:'${name}', action:"switchMode", nextState:"..."
			// state "auto", label:'${name}', action:"switchMode", nextState:"energySaveHeat"
			//state "emergency heat", label:'${name}', action:"switchMode", nextState:"energySaveHeat"
			//state "to_heat", label: "heat", action:"switchMode", nextState:"energySaveHeat"
			//state "to_cool", label: "cool", action:"switchMode", nextState:"..."
			state "energySaveHeat", label: "eco heat", action:"switchMode", nextState:"off"
		}
        /*
		standardTile("fanMode", "device.thermostatFanMode", inactiveLabel: false, decoration: "flat") {
			state "fanAuto", label:'${name}', action:"switchFanMode"
			state "fanOn", label:'${name}', action:"switchFanMode"
			state "fanCirculate", label:'${name}', action:"switchFanMode"
		}
        */
        valueTile("heatLabel", "device.thermostatMode", inactiveLabel: false, decoration: "flat", height: 1, width: 4) {
			state "default", label:"Heat Set Point:" 
		}
		controlTile("heatSliderControl", "device.heatingSetpoint", "slider", height: 1, width: 4, inactiveLabel: false, range: "(5..40)") {
			state "setHeatingSetpoint", action:"quickSetHeat", backgroundColor:"#d04e00"
		}
     //   standardTile("blank", "device.thermostatMode", inactiveLabel: false, decoration: "flat", height: 2, width: 2) {
	//		state "default", label:"" 
	//	}
        valueTile("ecoLabel", "device.thermostatMode", inactiveLabel: false, decoration: "flat", height: 1, width: 4) {
			state "default", label:"Eco Mode Set Point:" 
		}
		controlTile("ecoheatSliderControl", "device.ecoheatingSetpoint", "slider", height: 1, width: 4, inactiveLabel: false, range: "(5..40)") {
			state "setecoHeatingSetpoint", action:"quickSetecoHeat", backgroundColor:"#d04e00"
		}
        /*
		valueTile("heatingSetpoint", "device.heatingSetpoint", inactiveLabel: false, decoration: "flat") {
			state "heat", label:'${currentValue}° heat', backgroundColor:"#ffffff"
		}
        /*
		controlTile("coolSliderControl", "device.coolingSetpoint", "slider", height: 1, width: 2, inactiveLabel: false) {
			state "setCoolingSetpoint", action:"quickSetCool", backgroundColor: "#1e9cbb"
		}
		valueTile("coolingSetpoint", "device.coolingSetpoint", inactiveLabel: false, decoration: "flat") {
			state "cool", label:'${currentValue}° cool', backgroundColor:"#ffffff"
		}
        */
        
		standardTile("refresh", "device.thermostatMode", inactiveLabel: false, decoration: "flat", height: 2, width: 2) {
			state "default", action:"polling.poll", icon:"st.secondary.refresh"
		}
		standardTile("configure", "device.configure", inactiveLabel: false, decoration: "flat", height: 2, width: 2) {
			state "configure", label:'', action:"configuration.configure", icon:"st.secondary.configure"
		}
        
		main "thermostatMulti"
		details(["thermostatMulti", "mode", "heatLabel", "heatSliderControl", "refresh", "ecoLabel", "ecoheatSliderControl", "configure"])
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

            input (
                name: "configSyncAll",
                title: "Force Full Sync: All device parameters, association groups, and protection settings will " +
                "be re-sent to the device. This will take several minutes and you may need to press the 'sync' " +
                "tile a few times.",
                type: "boolean",
//                defaultValue: false, // iPhone users can uncomment these lines!
                required: true
            )
		}

        generatePrefsParams()

        generatePrefsAssocGroups()

           
	}

}


def parse(String description) {
	def results = []
    // log.debug("RAW command: $description")
	if (description.startsWith("Err")) {
		log.debug("An error has occurred")
		} 
    else {
       
       	def cmd = zwave.parse(description.replace("98C1", "9881"), [0x98: 1, 0x20: 1, 0x84: 1, 0x80: 1, 0x60: 3, 0x2B: 1, 0x26: 1])
        // log.debug "Parsed Command: $cmd"
        if (cmd) {
       	results = zwaveEvent(cmd)
		}
    }
}
/*def parse(String description)
{
	log.debug(description)
	def map = createEvent(zwaveEvent(zwave.parse(description, [0x42:1, 0x43:2, 0x31:3])))
	if (!map) {
		return null
	}

	def result = [map]
	if (map.isStateChange && map.name in ["heatingSetpoint","coolingSetpoint","thermostatMode"]) {
		def map2 = [
			name: "thermostatSetpoint",
			unit: getTemperatureScale()
		]
		if (map.name == "thermostatMode") {
			state.lastTriedMode = map.value
			if (map.value == "cool") {
				map2.value = device.latestValue("coolingSetpoint")
				log.info "THERMOSTAT, latest cooling setpoint = ${map2.value}"
			}
			else {
				map2.value = device.latestValue("heatingSetpoint")
				log.info "THERMOSTAT, latest heating setpoint = ${map2.value}"
			}
		}
		else {
			def mode = device.latestValue("thermostatMode")
			log.info "THERMOSTAT, latest mode = ${mode}"
			if ((map.name == "heatingSetpoint" && mode == "heat") || (map.name == "coolingSetpoint" && mode == "cool")) {
				map2.value = map.value
				map2.unit = map.unit
			}
		}
		if (map2.value != null) {
			log.debug "THERMOSTAT, adding setpoint event: $map"
			result << createEvent(map2)
		}
	} else if (map.name == "thermostatFanMode" && map.isStateChange) {
		state.lastTriedFanMode = map.value
	}
	log.debug "Parse returned $result"
	result
}
*/

/*****************************************************************************************************************
 *  Z-wave Event Handlers.
 *****************************************************************************************************************/

def zwaveEvent(physicalgraph.zwave.commands.thermostatsetpointv2.ThermostatSetpointReport cmd) {
    
   	if (cmd.setpointType == 1){
        def heating = cmd.scaledValue
        sendEvent(name: "heatingSetpoint", value: heating)
    }
    if (cmd.setpointType == 2){
    	def energyHeating = cmd.scaledValue
        sendEvent(name: "ecoHeatingSetpoint", value: energyHeating)
        state.ecoheatingSetpoint = energyHeating
    }
   	
   // log.debug(heatingSetpoint)
   // state.heatingSetpoint = heatingSetpoint
    
   
    //def cmdScale = cmd.scale == 1 ? "F" : "C"
	/*def map = [:]
	map.value = convertTemperatureIfNeeded(cmd.scaledValue, cmdScale, cmd.precision)
	map.unit = getTemperatureScale()
	map.displayed = false
	switch (cmd.setpointType) {
		case 1:
			map.name = "heatingSetpoint"
			break;
		case 2:
			map.name = "coolingSetpoint"
			break;
		default:
			return [:]
	}
    */
	// So we can respond with same format
	state.size = cmd.size
	state.scale = cmd.scale
	state.precision = cmd.precision
	//map
}

def zwaveEvent(physicalgraph.zwave.commands.sensormultilevelv5.SensorMultilevelReport cmd) {
	log.debug("Sensor report: $cmd")
    
    //def precision = cmd.precision / 10
    log.debug("Precision: $precision °C")
    if (cmd.scale == 0){
    log.debug("Scale is Celcius")
    }
    log.debug("Air Temperature is: $cmd.scaledSensorValue °C")
    sendEvent(name: "temperature", value: cmd.scaledSensorValue) 

    
  /*  def map = [:]
	if (cmd.sensorType == 1) {
		map.value = convertTemperatureIfNeeded(cmd.scaledSensorValue, cmd.scale == 1 ? "F" : "C", cmd.precision)
		map.unit = getTemperatureScale()
		map.name = "temperature"
	} else if (cmd.sensorType == 5) {
		map.value = cmd.scaledSensorValue
		map.unit = "%"
		map.name = "humidity"
	}
	map*/
}

def zwaveEvent(physicalgraph.zwave.commands.thermostatoperatingstatev2.ThermostatOperatingStateReport cmd) {
	log.debug("operating rep: $cmd")
	def map = [:]
	switch (cmd.operatingState) {
		case physicalgraph.zwave.commands.thermostatoperatingstatev1.ThermostatOperatingStateReport.OPERATING_STATE_IDLE:
			map.value = "idle"
			break
		case physicalgraph.zwave.commands.thermostatoperatingstatev1.ThermostatOperatingStateReport.OPERATING_STATE_HEATING:
			map.value = "heating"
			break
            /*
		case physicalgraph.zwave.commands.thermostatoperatingstatev1.ThermostatOperatingStateReport.OPERATING_STATE_COOLING:
			map.value = "cooling"
			break
		case physicalgraph.zwave.commands.thermostatoperatingstatev1.ThermostatOperatingStateReport.OPERATING_STATE_FAN_ONLY:
			map.value = "fan only"
			break
            */
		case physicalgraph.zwave.commands.thermostatoperatingstatev1.ThermostatOperatingStateReport.OPERATING_STATE_PENDING_HEAT:
			map.value = "pending heat"
			break
		case physicalgraph.zwave.commands.thermostatoperatingstatev1.ThermostatOperatingStateReport.OPERATING_STATE_PENDING_COOL:
			map.value = "pending cool"
			break
		case physicalgraph.zwave.commands.thermostatoperatingstatev1.ThermostatOperatingStateReport.OPERATING_STATE_VENT_ECONOMIZER:
			map.value = "vent economizer"
			break
	}
	map.name = "thermostatOperatingState"
	map
}

/*
def zwaveEvent(physicalgraph.zwave.commands.thermostatfanstatev1.ThermostatFanStateReport cmd) {
	def map = [name: "thermostatFanState", unit: ""]
	switch (cmd.fanOperatingState) {
		case 0:
			map.value = "idle"
			break
		case 1:
			map.value = "running"
			break
		case 2:
			map.value = "running high"
			break
	}
	map
}
*/

def zwaveEvent(physicalgraph.zwave.commands.thermostatmodev2.ThermostatModeReport cmd) {
	def map = [:]
	switch (cmd.mode) {
		case physicalgraph.zwave.commands.thermostatmodev2.ThermostatModeReport.MODE_OFF:
			map.value = "off"
            sendEvent(name: "thermostatOperatingState", value: "idle")
			break
		case physicalgraph.zwave.commands.thermostatmodev2.ThermostatModeReport.MODE_HEAT:
			map.value = "heat"
            sendEvent(name: "thermostatOperatingState", value: "heating")
			break
		case physicalgraph.zwave.commands.thermostatmodev2.ThermostatModeReport.MODE_AUXILIARY_HEAT:
			map.value = "emergency heat"
			break
		case physicalgraph.zwave.commands.thermostatmodev2.ThermostatModeReport.MODE_COOL:
			map.value = "cool"
			break
		case physicalgraph.zwave.commands.thermostatmodev2.ThermostatModeReport.MODE_AUTO:
			map.value = "auto"
			break
        case physicalgraph.zwave.commands.thermostatmodev2.ThermostatModeReport.MODE_ENERGY_SAVE_HEAT:
			map.value = "energySaveHeat"
            sendEvent(name: "thermostatOperatingState", value: "energySaveHeat")
			break
	}
	map.name = "thermostatMode"
	map
}

def zwaveEvent(physicalgraph.zwave.commands.thermostatfanmodev3.ThermostatFanModeReport cmd) {
	def map = [:]
	switch (cmd.fanMode) {
		case physicalgraph.zwave.commands.thermostatfanmodev3.ThermostatFanModeReport.FAN_MODE_AUTO_LOW:
			map.value = "fanAuto"
			break
		case physicalgraph.zwave.commands.thermostatfanmodev3.ThermostatFanModeReport.FAN_MODE_LOW:
			map.value = "fanOn"
			break
		case physicalgraph.zwave.commands.thermostatfanmodev3.ThermostatFanModeReport.FAN_MODE_CIRCULATION:
			map.value = "fanCirculate"
			break
	}
	map.name = "thermostatFanMode"
	map.displayed = false
	map
}

def zwaveEvent(physicalgraph.zwave.commands.thermostatmodev2.ThermostatModeSupportedReport cmd) {
	log.debug("support reprt: $cmd")
    def supportedModes = ""
	if(cmd.off) { supportedModes += "off " }
	if(cmd.heat) { supportedModes += "heat " }
	if(cmd.auxiliaryemergencyHeat) { supportedModes += "emergency heat " }
	if(cmd.cool) { supportedModes += "cool " }
	if(cmd.auto) { supportedModes += "auto " }
    if(cmd.energySaveHeat) { supportedModes += "energySaveHeat " }

	state.supportedModes = supportedModes
}

def zwaveEvent(physicalgraph.zwave.commands.configurationv2.ConfigurationReport cmd) {
    
    if (cmd.parameterNumber == 1){
    	if (cmd.configurationValue == [0, 0]){
            log.debug("Current Mode is Off")
            sendEvent(name: "thermostatMode", value: "off")
            sendEvent(name: "thermostatOperatingState", value: "idle")
        }
        else if (cmd.configurationValue == [0, 1]){
            log.debug("Current Mode is Heat")
            sendEvent(name: "thermostatMode", value: "heat")
            sendEvent(name: "thermostatOperatingState", value: "heating")
        }
        else if (cmd.configurationValue == [0, 2]){
            log.debug("Current Mode is Cool")
        }
        else if (cmd.configurationValue == [0, 11]){
            log.debug("Current Mode is Energy Save Heat")
            sendEvent(name: "thermostatMode", value: "energySaveHeat")
            sendEvent(name: "thermostatOperatingState", value: "energySaveHeat")
        }
	}
    if (cmd.parameterNumber == 2){
    	if (cmd.configurationValue == [0, 0]){
            log.debug("Temperature Sensor F - Floor mode: Regulation is based on the floor temperature sensor reading")
        	sendEvent(name: "tempSenseMode", value: "F")
        }
        else if (cmd.configurationValue == [0, 1]){
            log.debug("Temperature Sensor A - Room temperature mode: Regulation is based on the measured room temperature using the internal sensor (Default)")
        	sendEvent(name: "tempSenseMode", value: "A")
        }
        else if (cmd.configurationValue == [0, 2]){
            log.debug("Temperature Sensor AF - Room mode w/floor limitations: Regulation is based on internal room sensor but limited by the floor temperature sensor (included) ensuring that the floor temperature stays within the given limits (FLo/FHi")
        	sendEvent(name: "tempSenseMode", value: "AF")
        }
        else if (cmd.configurationValue == [0, 3]){
            log.debug("Temperature Sensor 2 - Room temperature mode: Regulation is based on the measured room temperature using the external sensor")
        	sendEvent(name: "tempSenseMode", value: "A2")
        }
        else if (cmd.configurationValue == [0, 4]){
            log.debug("Temperature Sensor P (Power regulator): Constant heating power is supplied to the floor. Power rating is selectable in 10% increments ( 0% - 100%)")
        	sendEvent(name: "tempSenseMode", value: "P")
        }
         else if (cmd.configurationValue == [0, 5]){
            log.debug("Temperature Sensor FP - Floor mode with minimum power limitation: Regulation is based on the floor temperature sensor reading, but will always heat with a minimum power setting (PLo)")
        	sendEvent(name: "tempSenseMode", value: "FP")
        }
	}
    if (cmd.parameterNumber == 3){
    	if (cmd.configurationValue == [0, 0]){
            log.debug("Floor sensor type 10k ntc (Default)")
        }
        else if (cmd.configurationValue == [0, 1]){
            log.debug("Floor sensor type 12k ntc")
        }
        else if (cmd.configurationValue == [0, 2]){
            log.debug("Floor sensor type 15k ntc")
        }
        else if (cmd.configurationValue == [0, 3]){
            log.debug("Floor sensor type 22k ntc")
        }
        else if (cmd.configurationValue == [0, 4]){
            log.debug("Floor sensor type 33k ntc")
        }
         else if (cmd.configurationValue == [0, 5]){
            log.debug("Floor sensor type 47k ntc")
        }
	}
    if (cmd.parameterNumber == 4){
    	
       def val = cmd.configurationValue[1]
       def diff = val / 10
       def newHys = 0.2 + diff
       log.debug("DIFF l. Temperature control Hysteresis is $newHys °C")
	}
    if (cmd.parameterNumber == 5){
    	
        def valX = cmd.configurationValue[0]
       	def valY = cmd.configurationValue[1]
       	def diff = binaryToDegrees(valX, valY)
       
       //log.debug("the command is: $cmd")
       log.debug("FLo: Floor min limit is $diff °C")
	}
    if (cmd.parameterNumber == 6){
    	
     	def valX1 = cmd.configurationValue[0]
       	def valY1 = cmd.configurationValue[1]
       	def diff = binaryToDegrees(valX1, valY1)
      // def val = cmd.configurationValue[1]
      // def diff = val / 10
       log.debug("FHi: Floor max limit is $diff °C")
	}
    if (cmd.parameterNumber == 7){
    	
        def valX = cmd.configurationValue[0]
       	def valY = cmd.configurationValue[1]
       	def diff = binaryToDegrees(valX, valY)
       log.debug("ALo: Air min limit is $diff °C")
	}
    if (cmd.parameterNumber == 8){
    	
        def valX1 = cmd.configurationValue[0]
       	def valY1 = cmd.configurationValue[1]
       	def diff = binaryToDegrees(valX1, valY1)
       log.debug("AHi: Air max limit is $diff °C")
	}
    if (cmd.parameterNumber == 9){
    	
       def val = cmd.configurationValue[1]
       log.debug("PLo: Min temperature in Power Reg Mode is $val °C")
	}
    if (cmd.parameterNumber == 10){
    	
        def valX = cmd.configurationValue[0]
       	def valY = cmd.configurationValue[1]
       	def diff = binaryToDegrees(valX, valY)
       log.debug("CO mode setpoint is $diff °C")
	}
    if (cmd.parameterNumber == 11){
    	
        def valX = cmd.configurationValue[0]
       	def valY = cmd.configurationValue[1]
       	def diff = binaryToDegrees(valX, valY)
       log.debug("ECO mode setpoint is $diff °C")
       sendEvent(name: "ecoheatingSetpoint", value: diff)
	}
    if (cmd.parameterNumber == 12){
    	
       def val = cmd.configurationValue[0]
       def diff = val * 10
       log.debug("P (Power regulator) is $diff %")
	}
}
    

/*
def zwaveEvent(physicalgraph.zwave.commands.thermostatfanmodev3.ThermostatFanModeSupportedReport cmd) {
	def supportedFanModes = ""
	if(cmd.auto) { supportedFanModes += "fanAuto " }
	if(cmd.low) { supportedFanModes += "fanOn " }
	if(cmd.circulation) { supportedFanModes += "fanCirculate " }
	state.supportedFanModes = supportedFanModes
}
*/
def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd) {
	log.debug "Basic Zwave event received: $cmd.payload"
}

/*def zwaveEvent(physicalgraph.zwave.Command cmd) {
	log.warn "Unexpected zwave command $cmd"
}
*/


/*****************************************************************************************************************
 *  Capability-related Commands:
 *****************************************************************************************************************/
	
def pressUp() {
	log.debug("pressed Up")
	def currTemp = device.latestValue("heatingSetpoint")
    log.debug(" pressed up currently $currTemp")
    def newTemp = currTemp + 0.5
    log.debug(" pressed up new temp is $newTemp")
	quickSetHeat(newTemp)
}

def pressDown() {
	log.debug("pressed Down")
	def currTemp = device.latestValue("heatingSetpoint")
    def newTemp = currTemp - 0.5
	quickSetHeat(newTemp)
}

def quickSetHeat_UNUSED(upDown) {
	log.debug("pressed")
	def change = upDown
    log.debug(change)
    def latest = device.latestValue("heatingSetpoint")
   
   if (change == 1) {
    	def newSetPoint = latest + 1
        log.debug("New Set Point: $newSetPoint")
		state.setPoint = newSetPoint
        log.debug("New State Set Point: $state.setPoint")
    	setHeatingSetpoint(newSetPoint, 1000)
    }
    else if (change == 0) {
    	def newSetPoint = latest - 1
        log.debug("New Set Point: $newSetPoint")
        state.setPoint = newSetPoint
        log.debug("New State Set Point: $state.setPoint")
    	setHeatingSetpoint(newSetPoint, 1000)
    }
	//setHeatingSetpoint(degrees, 1000)
    // log.debug("Degrees at quicksetheat: $degrees")
}

def quickSetHeat(degrees) {	
	setHeatingSetpoint(degrees, 1000)
}

def setHeatingSetpoint(degrees, delay = 30000) {
	setHeatingSetpoint(degrees.toDouble(), delay)
}

def setHeatingSetpoint(Double degrees, Integer delay = 30000) {
	log.trace "setHeatingSetpoint($degrees, $delay)"
	def deviceScale = state.scale ?: 1
	def deviceScaleString = deviceScale == 2 ? "C" : "F"
    def locationScale = getTemperatureScale()
	def p = (state.precision == null) ? 1 : state.precision

    def convertedDegrees
    //if (locationScale == "C" && deviceScaleString == "F") {
    //	convertedDegrees = celsiusToFahrenheit(degrees)
    //} else if (locationScale == "F" && deviceScaleString == "C") {
    	convertedDegrees = fahrenheitToCelsius(degrees)
    //} else {
    	convertedDegrees = degrees
    //}

	delayBetween([
		zwave.thermostatSetpointV1.thermostatSetpointSet(setpointType: 1, scale: deviceScale, precision: p, scaledValue: convertedDegrees).format(),
		zwave.thermostatSetpointV1.thermostatSetpointGet(setpointType: 1).format()
	], delay)
}

def quickSetecoHeat(degrees) {
	
	setecoHeatingSetpoint(degrees, 1000)
}

def setecoHeatingSetpoint(degrees, delay = 30000) {
	setecoHeatingSetpoint(degrees.toDouble(), delay)
}

def setecoHeatingSetpoint(Double degrees, Integer delay = 30000) {
	log.trace "setecoHeatingSetpoint($degrees, $delay)"
	def deviceScale = state.scale ?: 1
	def deviceScaleString = deviceScale == 2 ? "C" : "F"
    def locationScale = getTemperatureScale()
	def p = (state.precision == null) ? 1 : state.precision

    def convertedDegrees
    //if (locationScale == "C" && deviceScaleString == "F") {
    //	convertedDegrees = celsiusToFahrenheit(degrees)
    //} else if (locationScale == "F" && deviceScaleString == "C") {
    	convertedDegrees = fahrenheitToCelsius(degrees)
    //} else {
    	convertedDegrees = degrees
    //}

	delayBetween([
		zwave.thermostatSetpointV1.thermostatSetpointSet(setpointType: 11, scale: deviceScale, precision: p, scaledValue: convertedDegrees).format(),
		zwave.thermostatSetpointV1.thermostatSetpointGet(setpointType: 11).format()
	], delay)
}

/*
def quickSetCool(degrees) {
	setCoolingSetpoint(degrees, 1000)
}
def setCoolingSetpoint(degrees, delay = 30000) {
	setCoolingSetpoint(degrees.toDouble(), delay)
}
def setCoolingSetpoint(Double degrees, Integer delay = 30000) {
    log.trace "setCoolingSetpoint($degrees, $delay)"
	def deviceScale = state.scale ?: 1
	def deviceScaleString = deviceScale == 2 ? "C" : "F"
    def locationScale = getTemperatureScale()
	def p = (state.precision == null) ? 1 : state.precision
    def convertedDegrees
    if (locationScale == "C" && deviceScaleString == "F") {
    	convertedDegrees = celsiusToFahrenheit(degrees)
    } else if (locationScale == "F" && deviceScaleString == "C") {
    	convertedDegrees = fahrenheitToCelsius(degrees)
    } else {
    	convertedDegrees = degrees
    }
	delayBetween([
		zwave.thermostatSetpointV1.thermostatSetpointSet(setpointType: 2, scale: deviceScale, precision: p,  scaledValue: convertedDegrees).format(),
		zwave.thermostatSetpointV1.thermostatSetpointGet(setpointType: 2).format()
	], delay)
}
*/

/*****************************************************************************************************************
 *  SmartThings System Commands:
 *****************************************************************************************************************/

def poll() {
	delayBetween([
		zwave.sensorMultilevelV5.sensorMultilevelGet().format(), // current temperature
		zwave.thermostatSetpointV2.thermostatSetpointGet(setpointType: 1).format(),
		zwave.thermostatSetpointV2.thermostatSetpointGet(setpointType: 2).format(),
        zwave.thermostatModeV2.thermostatModeGet().format(),
        zwave.configurationV2.configurationGet(parameterNumber: 1).format(),
        zwave.configurationV2.configurationGet(parameterNumber: 2).format(),
        zwave.configurationV2.configurationGet(parameterNumber: 3).format(),
        zwave.configurationV2.configurationGet(parameterNumber: 4).format(),
        zwave.configurationV2.configurationGet(parameterNumber: 5).format(),
        zwave.configurationV2.configurationGet(parameterNumber: 6).format(),
        zwave.configurationV2.configurationGet(parameterNumber: 7).format(),
        zwave.configurationV2.configurationGet(parameterNumber: 8).format(),
        zwave.configurationV2.configurationGet(parameterNumber: 9).format(),
        zwave.configurationV2.configurationGet(parameterNumber: 10).format(),
        zwave.configurationV2.configurationGet(parameterNumber: 11).format(),
        zwave.configurationV2.configurationGet(parameterNumber: 12).format()
        
        // zwave.basicV1.basicGet().format(),
		// zwave.thermostatOperatingStateV2.thermostatOperatingStateGet().format()
	], 650)
}

def configure() {
    
    def floorMinY = 0
    def floorMinX = 0
    def floorMin = 0
    if (FLo){
    	floorMin = FLo
        if (floorMin <= 25.5){
        	floorMinX = 0
        	floorMinY = floorMin*10
        }
        else if (floorMin > 25.5){
            floorMinX = 1
        	floorMinY = floorMin*10 - 256
        }
    }
    def floorMax = 0
    def floorMaxY = 0
    def floorMaxX = 0
    if (FHi){
    		floorMax = FHi
    	    if (floorMax <= 25.5){
        	floorMaxX = 0
        	floorMaxY = floorMax*10
        }
        else if (floorMax > 25.5){
            floorMaxX = 1
        	floorMaxY = floorMax*10 - 256
        }
    }
    def AirMin = 0
    def AirMinY = 0
    def AirMinX = 0
    if (ALo){
    		AirMin = ALo
    	    if (AirMin <= 25.5){
        	AirMinX = 0
        	AirMinY = AirMin*10
        }
        else if (AirMin > 25.5){
            AirMinX = 1
        	AirMinY = AirMin*10 - 256
        }
    }
    def AirMax = 0
    def AirMaxY = 0
    def AirMaxX = 0
    if (AHi){
    		AirMax = AHi
    	    if (AirMax <= 25.5){
        	AirMaxX = 0
        	AirMaxY = AirMax*10
        }
        else if (AirMax > 25.5){
            AirMaxX = 1
        	AirMaxY = AirMax*10 - 256
        }
    }
    def tempSensorMode = ""
    def tempModeParam = 1
    if (tempSen){
    tempSensorMode = tempSen
        if (tempSensorMode == "F - Floor temperature mode"){
        tempModeParam = 0
        }
        if (tempSensorMode == "A - Room temperature mode"){
        tempModeParam = 1
        }
        if (tempSensorMode == "AF - Room mode w/floor limitations"){
        tempModeParam = 2
        }
        if (tempSensorMode == "A2 - Room temperature mode (external)"){
        tempModeParam = 3
        }
       	if (tempSensorMode == "P - Power regulator mode"){
        tempModeParam = 4
        }
        if (tempSensorMode == "FP - Floor mode with minimum power limitation"){
        tempModeParam = 5
        }
    }
    def floorSensor = ""
    def floorSensParam = 0
    	if (sensorType){
        floorSensor = sensorType
            if (floorSensor == "10k ntc (Default)"){
            floorSensParam = 0
            }
            if (floorSensor == "12k ntc"){
            floorSensParam = 1
            }
            if (floorSensor == "15k ntc"){
            floorSensParam = 2
            }
            if (floorSensor == "22k ntc"){
            floorSensParam = 3
            }
            if (floorSensor == "33k ntc"){
            floorSensParam = 4
            }
            if (floorSensor == "47k ntc"){
            floorSensParam = 5
            }
        }
   	def powerLo = 0
    	if (PLo){
        	powerLo = PLo  
        }
    def powerSet = 0
    def powerSetPer = ""
    	if (PSet){
        powerSetPer = PSet
            if (powerSetPer == "0%"){
        	powerSet = 0
       		}
            if (powerSetPer == "10%"){
        	powerSet = 1
       		}
            if (powerSetPer == "20%"){
        	powerSet = 2
       		}
            if (powerSetPer == "30%"){
        	powerSet = 3
            log.debug("powerset 3")
       		}
            if (powerSetPer == "40%"){
        	powerSet = 4
       		}
            if (powerSetPer == "50%"){
        	powerSet = 5
       		}
            if (powerSetPer == "60%"){
        	powerSet = 6
       		}
            if (powerSetPer == "70%"){
        	powerSet = 7
       		}
            if (powerSetPer == "80%"){
        	powerSet = 8
       		}
            if (powerSetPer == "90%"){
        	powerSet = 9
       		}
            if (powerSetPer == "100%"){
        	powerSet = 10
       		}
        // DO LIKE LIST INSTEAD 0, 10, 20, 30 etc so no issues powerSetRound = Math.floor(powerSet)
        // log.debug("floor: $powerSetRound")
        }

    
	delayBetween([
		zwave.associationV1.associationSet(groupingIdentifier:1, nodeId:[zwaveHubNodeId]).format(),
        zwave.configurationV2.configurationSet(configurationValue: [0, tempModeParam], parameterNumber: 2, size: 2).format(),
      	zwave.configurationV2.configurationSet(configurationValue: [0, floorSensParam], parameterNumber: 3, size: 2).format(),
       // zwave.configurationV2.configurationSet(configurationValue: [0, 0], parameterNumber: 3, size: 2).format(),
       	zwave.configurationV2.configurationSet(configurationValue: [floorMinX, floorMinY], parameterNumber: 5, size: 2).format(),
        //zwave.configurationV2.configurationSet(configurationValue: [0, 50], parameterNumber: 5, size: 2).format(),
       	zwave.configurationV2.configurationSet(configurationValue: [floorMaxX, floorMaxY], parameterNumber: 6, size: 2).format(),
        zwave.configurationV2.configurationSet(configurationValue: [AirMinX, AirMinY], parameterNumber: 7, size: 2).format(),
        zwave.configurationV2.configurationSet(configurationValue: [AirMaxX, AirMaxY], parameterNumber: 8, size: 2).format(),
        zwave.configurationV2.configurationSet(configurationValue: [powerLo], parameterNumber: 9, size: 1).format(),
        zwave.configurationV2.configurationSet(configurationValue: [powerSet], parameterNumber: 12, size: 1).format(),//zwave.configurationV2.configurationSet(configurationValue: [1, 144], parameterNumber: 6, size: 2).format(),
        //zwave.configurationV2.configurationSet(configurationValue: [1, 144], parameterNumber: 6, size: 2).format(),
        zwave.thermostatModeV2.thermostatModeSupportedGet().format(),
        poll()
	], 650)
    
}

def modes() {
	["off", "heat", "energySaveHeat"]
}

def binaryToDegrees(x, y) {
	def degrees = 0
    def preDegrees = 0
    if (x == 0){
    	degrees = y / 10
    }
    else if (x == 1){
    	preDegrees = y + 256
        degrees = preDegrees / 10
    }
    
    return degrees
}

def switchMode() {
	
    def currentMode = device.currentState("thermostatMode")?.value
  /* 	def lastTriedMode = state.lastTriedMode ?: currentMode ?: "off"
	def supportedModes = getDataByName("supportedModes")
	def modeOrder = modes()
	def next = { modeOrder[modeOrder.indexOf(it) + 1] ?: modeOrder[0] }
	def nextMode = next(lastTriedMode)
	if (supportedModes?.contains(currentMode)) {
		while (!supportedModes.contains(nextMode) && nextMode != "off") {
			nextMode = next(nextMode)
		}
    }
   */ 
   // log.debug("currentMode is $currentMode")
    if (currentMode == "off"){
    	def nextMode = "heat"
        sendEvent(name: "thermostatMode", value: "heat")
        sendEvent(name: "thermostatOperatingState", value: "heating")
        delayBetween([
		zwave.thermostatModeV2.thermostatModeSet(mode: 1).format(),
		zwave.thermostatModeV2.thermostatModeGet().format(),
        poll()
	], 650)
    }
    else if (currentMode == "heat"){
    	def nextMode = "energySaveHeat"
        sendEvent(name: "thermostatMode", value: "energySaveHeat")
        sendEvent(name: "thermostatOperatingState", value: "energySaveHeat")
        delayBetween([
		zwave.thermostatModeV2.thermostatModeSet(mode: 11).format(),
		zwave.thermostatModeV2.thermostatModeGet().format(),
        poll()
	], 650)
    }
    else if (currentMode == "energySaveHeat"){
    	def nextMode = "off"
        sendEvent(name: "thermostatMode", value: "off")
        sendEvent(name: "thermostatOperatingState", value: "idle")
        delayBetween([
		zwave.thermostatModeV2.thermostatModeSet(mode: 0).format(),
		zwave.thermostatModeV2.thermostatModeGet().format(),
        poll()
	], 650)
    }
    


	
	//state.lastTriedMode = nextMode

/*    
    delayBetween([
		zwave.thermostatModeV2.thermostatModeSet(mode: modeMap[nextMode]).format(),
		zwave.thermostatModeV2.thermostatModeGet().format()
	], 1000)*/
}

def switchToMode(nextMode) {
	def supportedModes = getDataByName("supportedModes")
	if(supportedModes && !supportedModes.contains(nextMode)) log.warn "thermostat mode '$nextMode' is not supported"
	if (nextMode in modes()) {
		state.lastTriedMode = nextMode
		"$nextMode"()
	} else {
		log.debug("no mode method '$nextMode'")
	}
}
/*
def switchFanMode() {
	def currentMode = device.currentState("thermostatFanMode")?.value
	def lastTriedMode = state.lastTriedFanMode ?: currentMode ?: "off"
	def supportedModes = getDataByName("supportedFanModes") ?: "fanAuto fanOn"
	def modeOrder = ["fanAuto", "fanCirculate", "fanOn"]
	def next = { modeOrder[modeOrder.indexOf(it) + 1] ?: modeOrder[0] }
	def nextMode = next(lastTriedMode)
	while (!supportedModes?.contains(nextMode) && nextMode != "fanAuto") {
		nextMode = next(nextMode)
	}
	switchToFanMode(nextMode)
}
def switchToFanMode(nextMode) {
	def supportedFanModes = getDataByName("supportedFanModes")
	if(supportedFanModes && !supportedFanModes.contains(nextMode)) log.warn "thermostat mode '$nextMode' is not supported"
	def returnCommand
	if (nextMode == "fanAuto") {
		returnCommand = fanAuto()
	} else if (nextMode == "fanOn") {
		returnCommand = fanOn()
	} else if (nextMode == "fanCirculate") {
		returnCommand = fanCirculate()
	} else {
		log.debug("no fan mode '$nextMode'")
	}
	if(returnCommand) state.lastTriedFanMode = nextMode
	returnCommand
}
*/
def getDataByName(String name) {
	state[name] ?: device.getDataValue(name)
}

def getModeMap() { [
	"off": 0,
	"heat": 1,
	"energySaveHeat": 11
]}

def setThermostatMode(String value) {
	delayBetween([
		zwave.thermostatModeV2.thermostatModeSet(mode: modeMap[value]).format(),
		zwave.thermostatModeV2.thermostatModeGet().format()
	], standardDelay)
}
/*
def getFanModeMap() { [
	"auto": 0,
	"on": 1,
	"circulate": 6
]}
def setThermostatFanMode(String value) {
	delayBetween([
		zwave.thermostatFanModeV3.thermostatFanModeSet(fanMode: fanModeMap[value]).format(),
		zwave.thermostatFanModeV3.thermostatFanModeGet().format()
	], standardDelay)
}
*/
def off() {
        delayBetween([
		zwave.thermostatModeV2.thermostatModeSet(mode: 0).format(),
		zwave.thermostatModeV2.thermostatModeGet().format(),
        sendEvent(name: "thermostatMode", value: "off"),
        sendEvent(name: "thermostatOperatingState", value: "idle"),
        poll()
	], 650)
    	
}

def heat() {
        delayBetween([
		zwave.thermostatModeV2.thermostatModeSet(mode: 1).format(),
		zwave.thermostatModeV2.thermostatModeGet().format(),
        sendEvent(name: "thermostatMode", value: "heat"),
        sendEvent(name: "thermostatOperatingState", value: "heating"),
        poll()
	], 650)
}

def energySaveHeat() {
        delayBetween([
		zwave.thermostatModeV2.thermostatModeSet(mode: 11).format(),
		zwave.thermostatModeV2.thermostatModeGet().format(),
        sendEvent(name: "thermostatMode", value: "energySaveHeat"),
        sendEvent(name: "thermostatOperatingState", value: "energySaveHeat"),
        poll()
	], 650)
}
/*
def cool() {
	delayBetween([
		zwave.thermostatModeV2.thermostatModeSet(mode: 2).format(),
		zwave.thermostatModeV2.thermostatModeGet().format()
	], standardDelay)
}
*/
def auto() {
	delayBetween([
		zwave.thermostatModeV2.thermostatModeSet(mode: 3).format(),
		zwave.thermostatModeV2.thermostatModeGet().format()
	], standardDelay)
}
/*
def fanOn() {
	delayBetween([
		zwave.thermostatFanModeV3.thermostatFanModeSet(fanMode: 1).format(),
		zwave.thermostatFanModeV3.thermostatFanModeGet().format()
	], standardDelay)
}
def fanAuto() {
	delayBetween([
		zwave.thermostatFanModeV3.thermostatFanModeSet(fanMode: 0).format(),
		zwave.thermostatFanModeV3.thermostatFanModeGet().format()
	], standardDelay)
}
def fanCirculate() {
	delayBetween([
		zwave.thermostatFanModeV3.thermostatFanModeSet(fanMode: 6).format(),
		zwave.thermostatFanModeV3.thermostatFanModeGet().format()
	], standardDelay)
}
*/

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
    state.energyLastReset = new Date().format("YYYY/MM/dd \n HH:mm:ss", location.timeZone)
    state.loggingLevelIDE     = 3
    state.loggingLevelDevice  = 2

    sendEvent(name: "fault", value: "clear", descriptionText: "Fault cleared", displayed: false)

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

        // Manage Schedules:
        manageSchedules()

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
        sync(state.syncAll)

        // Set target for parameter #13 [Force Auto-calibration] back to 0 [Readout].
        // Sync will now only complete when auto-calibration has completed:
        state.paramTarget13 = 0

        // Request device medadata (this just seems the best place to do it):
        cmds << zwave.firmwareUpdateMdV2.firmwareMdGet()
        cmds << zwave.manufacturerSpecificV2.manufacturerSpecificGet()
        cmds << zwave.versionV1.versionGet()

        return response(secureSequence(cmds))
    }
    else {
        logger("updated(): Ran within last 2 seconds so aborting.","debug")
    }
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

    getAssocGroupsMd().each( { it.id != 1} ) {
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

    sendEvent(name: "syncPending", value: syncPending, displayed: false)
    sendSecureSequence(cmds,1000) // Need a delay of at least 1000ms.
}

private updateSyncPending() {
    /**
     *  Updates syncPending attribute, which advertises remaining number of sync operations.
     **/
    def syncPending = 0

    getParamsMd().findAll( {!it.readonly} ).each { // Exclude readonly parameters.
        if ( (state."paramTarget${it.id}" != null) & (state."paramCache${it.id}" != state."paramTarget${it.id}") ) {
            syncPending++
        }
    }

    getAssocGroupsMd().each( { it.id != 1} )  {
        def cachedNodes = state."assocGroupCache${it.id}"
        def targetNodes = state."assocGroupTarget${it.id}"

        if ( cachedNodes != targetNodes ) {
            syncPending++
        }
    }

    logger("updateSyncPending(): syncPending: ${syncPending}", "debug")
    if ((syncPending == 0) & (device.latestValue("syncPending") > 0)) logger("Sync Complete.", "info")
    sendEvent(name: "syncPending", value: syncPending, displayed: false)
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
//                defaultValue: "", // iPhone users can uncomment these lines!
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
     *
     *  Reference: http://products.z-wavealliance.org/products/1729/classes
     **/
    return [0x20: 1, // Basic V1
            0x22: 1, // Application Status V1
            0x26: 3, // Switch Multilevel V3
            0x27: 1, // Switch All V1
            0x2B: 1, // Scene Activation V1
            0x31: 4, // Sensor Multilevel V4
            0x32: 3, // Meter V3
            0x56: 1, // CRC16 Encapsulation V1
            //0x59: 1, // Association Group Information V1 (Not handled, as no need)
            0x5A: 1, // Device Reset Locally V1
            //0x5E: 2, // Z-Wave Plus Info V2 (Not supported by SmartThings)
            0x60: 3, // Multi Channel V4 (Device supports V4, but SmartThings only supports V3)
            0x70: 1, // Configuration V1
            0x71: 3, // Notification V5 ((Device supports V5, but SmartThings only supports V3)
            0x72: 2, // Manufacturer Specific V2
            0x73: 1, // Powerlevel V1
            0x75: 2, // Protection V2
            0x7A: 2, // Firmware Update MD V3 (Device supports V3, but SmartThings only supports V2)
            0x85: 1, // Association V2
            0x86: 1, // Version V2 (Device supports V2, but SmartThings only supports V1)
            //0x8E: 2, // Multi Channel Association V3 (Device supports V3, but SmartThings only supports V2)
            //0x98: 1  // Security V1
           ]
}

private getParamsMd() {
    /**
     *  Returns device parameters metadata. Used by sync(), updateSyncPending(),  and generatePrefsParams().
     *
     *  Reference: http://products.z-wavealliance.org/products/1729/configs
     **/
    return [
    
        [id: 2, size: 1, type: "enum", defaultValue: "1", required: false, readonly: false,
         name: "Sensor mode",
         description : "Select sensor(s) or regulator mode",
         options: ["0" : "0: F - Floor temperature mode",
                   "1" : "1: A - Room temperature mode (default)",
                   "2" : "2: AF - Room mode w/floor limitations",
                   "3" : "3: A2 - Room temperature mode (external)", 
                   "4" : "4: P - Power regulator mode", 
                   "5" : "5: FP - Floor mode with minimum power limitation"] ],
        [id: 3, size: 1, type: "enum", defaultValue: "0", required: false, readonly: false,
         name: "Floor sensor type",
         description : "Select floor sensor resistance",
         options: ["0" : "0: 10k ntc (Default)",
                   "1" : "1: 12k ntc",
                   "2" : "2: 15k ntc",
                   "3" : "3: 22k ntc", 
                   "4" : "4: 33k ntc", 
                   "5" : "5: 47k ntc"] ],
        [id:  4, size: 1, type: "number", range: "3..30", defaultValue : 5, required: false, readonly: false,
         name: "DIFF l. Temperature control Hysteresis",
         description : "The percentage value of a dimming step during automatic control. Values: 3..30 = Hysteresis 0.3°C .. 3.0°C"],
        [id:  5, size: 2, type: "number", range: "50..400", defaultValue : 50, required: false, readonly: false,
         name: "FLo, Floor min limit",
         description : "Minimum Limit for floor sensor (5°-40°)"],
        [id:  6, size: 2, type: "number", range: "50..400", defaultValue : 400, required: false, readonly: false,
         name: "FHi: Floor max limit",
         description : "Maximum Limit for floor sensor (5°-40°)"],
        [id:  7, size: 2, type: "number", range: "50..400", defaultValue : 50, required: false, readonly: false,
         name: "ALo: Air min limit",
         description : "Minimum Limit for Air sensor (5°-40°)"],
        [id:  8, size: 2, type: "number", range: "50..400", defaultValue : 400, required: false, readonly: false,
         name: "AHi: Air max limit",
         description : "Maximum Limit for Air sensor (5°-40°)"],
        [id:  9, size: 1, type: "number", range: "0..9", defaultValue : 0, required: false, readonly: false,
         name: "PLo: FP-mode P setting",
         description : "FP-mode P setting (0 - 9)"],
        [id:  10, size: 2, type: "number", range: "50..400", defaultValue : 210, required: false, readonly: false,
         name: "CO mode setpoint",
         description : "COmfort mode temperature setpoint (5°-40°)"],
        [id:  11, size: 2, type: "number", range: "50..400", defaultValue : 180, required: false, readonly: false,
         name: "ECO mode setpoint",
         description : "Economy mode temperature setpoint (5°-40°)"],
        [id:  12, size: 2, type: "number", range: "50..400", defaultValue : 2, required: false, readonly: false,
         name: "P setting",
         description : "Power Regulator setting (0 - 10%)"],
    ]
}

private getAssocGroupsMd() {
    /**
     *  Returns association groups metadata. Used by sync(), updateSyncPending(), and generatePrefsAssocGroups().
     *
     *  Reference: http://products.z-wavealliance.org/products/1729/assoc
     **/
    return [
        [id:  1, maxNodes: 1, name: "Lifeline",
         description : "Reports device state. Main Z-Wave controller should be added to this group."],
        [id:  2, maxNodes: 8, name: "On/Off control",
         description : "Enabling all switching units to be used as auxiliary heating controllers."],
    ]
}