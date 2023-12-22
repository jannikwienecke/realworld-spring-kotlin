package de.wienecke.realworld

import org.springframework.boot.Banner
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@SpringBootConfiguration
class RealWorldApplication

fun main(args: Array<String>) {
    runApplication<RealWorldApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)

    }
}
