# Project

The [MySchedule](https://github.com/zemian/myschedule) is a Java based web application for managing 
(Quartz Schedulers)[https://github.com/quartz-scheduler/quartz/].

## How to build this project (generate latest jar and war files etc)

Ensure you have Java8 and Maven3 installed, then run

	mvn install -DskipTests
	cd myschedule-web
	mvn jetty:run
    #open http://localhost:8080/myschedule-web

# Branches

* default => The 4.x development branch (VueJS UI with Quartz 2.3)
* myschedule-3.x => 3.x release series (Vaadin UI with Quartz 2.2).
* myschedule-2.x => 2.x release series (JQuery UI with Quartz 2).
* myschedule-1.x => 1.x release series (JQuery UI with Quartz 1).
