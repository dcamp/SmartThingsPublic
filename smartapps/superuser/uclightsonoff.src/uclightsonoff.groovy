/**
 *  PhysicalMasterSwitch
 *
 *  Author: dcamp@acm.org
 *  Date: 2013-12-08
 *  Update: 2014-09-29 -- .isPhysical() -> .physical
 */


// Automatically generated. Make future change here.
definition(
    name: "UcLightsOnOff",
    namespace: "",
    author: "dcamp@acm.org",
    description: "UcLightsOnOff",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience%402x.png",
    oauth: true
)

preferences {
	section("When I turn on/off...") {
		input "masterSwitch", "capability.switch", multiple: false
	}
	section("Then turn on/off...") {
		input "slaveSwitches", "capability.switch", multiple: true
	}
}

def installed()
{
//	subscribe(masterSwitch, "switch", switchHandler, [filterEvents: false])
	subscribe(masterSwitch, "switch", switchHandler)
}

def updated()
{
	unsubscribe()
//	subscribe(masterSwitch, "switch", switchHandler, [filterEvents: false])
	subscribe(masterSwitch, "switch", switchHandler)
}

def switchHandler(evt) {

	if (evt.physical) {
		if (evt.value == "on") {
			log.debug "turn on other switche(s)"
			slaveSwitches?.on()
		} else if (evt.value == "off") {
			log.debug "turn off other switche(s)"
			slaveSwitches?.off()
		}
	}
	else {
		log.trace "Skipping digital on/off event"
	}
}
