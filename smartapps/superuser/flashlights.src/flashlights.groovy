/**
 *  FlashLights
 *
 *  Author: Doug
 *  Date: 2013-12-31
 */

// Automatically generated. Make future change here.
definition(
    name: "FlashLights",
    namespace: "",
    author: "dcamp@acm.org",
    description: "FlashLights",
    category: "Safety & Security",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience%402x.png",
    oauth: true)

preferences {
	section("IF (12PM-6AM)..."){
		input "motion", "capability.motionSensor", title: "Motion Sensor?", required: false, multiple: true
		input "contact", "capability.contactSensor", title: "Contact Sensor?", required: false, multiple: true
		input "acceleration", "capability.accelerationSensor", title: "Acceleration Sensor?", required: false
		input "mySwitch", "capability.switch", title: "Switch?", required: false
		input "myPresence", "capability.presenceSensor", title: "Presence Sensor?", required: false
	}
	section("Then Flash..."){
		input "switches", "capability.switch", title: "These lights", multiple: true
		input "numFlashes", "number", title: "This number of times (default 3)", required: false
	}
	section("Time settings in milliseconds (optional)..."){
		input "onFor", "number", title: "On for (default 1000)", required: false
		input "offFor", "number", title: "Off for (default 1000)", required: false
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"
	subscribe()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	subscribe()
}

def subscribe() {
	if (contact) {
		subscribe(contact, "contact.open", contactOpenHandler)
	}
	if (acceleration) {
		subscribe(acceleration, "acceleration.active", accelerationActiveHandler)
	}
	if (motion) {
		subscribe(motion, "motion.active", motionActiveHandler)
	}
	if (mySwitch) {
		subscribe(mySwitch, "switch.on", switchOnHandler)
	}
	if (myPresence) {
		subscribe(myPresence, "presence", presenceHandler)
	}
}

def motionActiveHandler(evt) {
	log.debug "motion $evt.value"
	flashLights(motion.label)
}

def contactOpenHandler(evt) {
	log.debug "contact $evt.value"
	flashLights(contact.label)
}

def accelerationActiveHandler(evt) {
	log.debug "acceleration $evt.value"
	flashLights(acceleration.label)
}

def switchOnHandler(evt) {
	log.debug "switch $evt.value"
	flashLights(mySwitch.label)
}

def presenceHandler(evt) {
	log.debug "presence $evt.value"
	if (evt.value == "present") {
		flashLights(myPresence.label)
	} else if (evt.value == "not present") {
		flashLights(myPresence.label)
	}
}

// 05-17-15 DAC - Updated to use getHours() - seems to have correct (EST) time,
// not sure where DST fits into this, leaving it alone for the moment.

private flashLights(alarmString) {
	def doFlash = true
	def numFlashes = numFlashes ?: 3
    
//    TimeZone.setDefault(TimeZone.getTimeZone("US/Eastern"))
//    def now = new Date()
//    def hour = now.getHours()

	hour = new Date().format("H", location.timeZone)
	//location.timeZone -> pulls the timezone settings from the hub
    
	log.debug("Alarm string is: " + alarmString)
 	log.debug("Hour is: " + hour.toString())
	if (hour >= 6) {
		log.debug "Did not flash - not between midight and 6AM"
        return
    }

	log.debug "LAST ACTIVATED IS: ${state.lastActivated}"
	if (state.lastActivated) {
		def elapsed = now() - state.lastActivated
		def sequenceTime = (numFlashes + 1) * (onFor + offFor)
		doFlash = elapsed > sequenceTime
		log.debug "DO FLASH: $doFlash, ELAPSED: $elapsed, LAST ACTIVATED: ${state.lastActivated}"
	}

	if (doFlash) {
		sendSms("19192643547", ("Alarm from: " + alarmString))
		log.debug "FLASHING $numFlashes times"
		numFlashes.times {
        	log.trace "Switch on!"
			switches.eachWithIndex {s, i ->
                s.off()
                s.on()
    	}
                
	}
}
}

