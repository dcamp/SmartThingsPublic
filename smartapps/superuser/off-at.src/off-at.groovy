/**
 *  Off At
 */
preferences {
	section ("Turn Off What?") {
		input "devicesToTurnOff", "capability.switch", title: "Turn off what?", required: false, multiple: true
	}
	section("Turn Off When?") {
		input "time1", "time", title: "Turn off when?"
	}
}

def installed()
{
	schedule(time1, "scheduleOffAt")
}

def updated()
{
	unschedule()
	schedule(time1, "scheduleOffAt")
}

def scheduleOffAt()
{
	log.trace "scheduleOffAt"
	devicesToTurnOff?.off()
}

private getLabel() {
	app.label ?: "SmartThings"
}


