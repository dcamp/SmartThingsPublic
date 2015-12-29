/**
 *  On At
 */
preferences {
	section ("Turn On What?") {
		input "devicesToTurnOn", "capability.switch", title: "Turn on what?", required: false, multiple: true
	}
	section("Turn On When?") {
		input "time1", "time", title: "Turn On when?"
	}
}

def installed()
{
	schedule(time1, "scheduleOnAt")
}

def updated()
{
	unschedule()
	schedule(time1, "scheduleOnAt")
}

def scheduleOnAt()
{
	log.trace "scheduleOnAt"
	devicesToTurnOn?.on()
}

private getLabel() {
	app.label ?: "SmartThings"
}


