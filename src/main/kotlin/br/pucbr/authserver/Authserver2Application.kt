package br.pucbr.authserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class Authserver2Application

fun main(args: Array<String>) {
	runApplication<Authserver2Application>(*args)
}
