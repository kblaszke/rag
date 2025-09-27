package pl.blaszak.ai.rag

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class RagApplication

fun main(args: Array<String>) {
	val applicationBuilder = SpringApplicationBuilder(RagApplication::class.java)
    applicationBuilder.headless(false)
    applicationBuilder.bannerMode(Banner.Mode.OFF)
    applicationBuilder.run(*args)
}
