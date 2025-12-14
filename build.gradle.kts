plugins {
	kotlin("jvm") version "2.2.21"
	kotlin("plugin.serialization") version "2.2.21"

	id("com.gradleup.shadow") version "8.3.0"
}

group = "site.remlit"
version = "0.1.0-SNAPSHOT"

repositories {
	mavenCentral()
	maven {
		url = uri("https://repo.remlit.site/releases")
	}
	maven {
		url = uri("https://repo.remlit.site/snapshots")
	}
}

dependencies {
	compileOnly("site.remlit:aster:2025.12.2.1-SNAPSHOT")
	compileOnly("site.remlit.aster:common:2025.12.2.1-SNAPSHOT")

	// ktor server
	compileOnly("io.ktor:ktor-server-core-jvm:3.3.3")

	// database
	compileOnly("org.jetbrains.exposed:exposed-core:1.0.0-rc-4")

	// misc
	compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:1.9.0")
	compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.9.0")
	compileOnly("ch.qos.logback:logback-classic:1.5.20")
	compileOnly("org.slf4j:slf4j-api:2.0.17")
}

kotlin {
	jvmToolchain(21)
}

tasks.build {
	dependsOn("shadowJar")
}

tasks.processResources {
	val version = project.provider { project.version.toString() }.get()

	filesMatching("plugin.json") {
		filter { line ->
			line.replace("%version%", version)
		}
	}
}