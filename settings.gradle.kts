pluginManagement {
	repositories {
		maven {
			name = "Fabric"
			url = uri("https://maven.fabricmc.net/")
		}
		mavenCentral()
		gradlePluginPortal()
		maven { url = uri("https://maven.kikugie.dev/releases") }
		maven { url = uri("https://maven.kikugie.dev/snapshots") }
	}
}

plugins {
	id("dev.kikugie.stonecutter") version "0.6-beta.2"
}

stonecutter {
	kotlinController = true
	centralScript = "build.gradle.kts"

	create(getRootProject()) {
		versions("1.20.1", "1.20.4")
		vcsVersion = "1.20.4"
	}
}