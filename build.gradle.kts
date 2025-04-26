plugins {
	id("fabric-loom") version "1.7-SNAPSHOT"
	id("maven-publish")
}

version = (project.property("mod_version") as String) + "+${stonecutter.current.project}"
group = project.property("maven_group") as String

base {
	archivesName.set(project.property("archives_base_name") as String)
}

repositories {
	// Add repositories to retrieve artifacts from in here.
	// You should only use this when depending on other mods because
	// Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
	// See https://docs.gradle.org/current/userguide/declaring_repositories.html
	// for more information about repositories.
}

fabricApi {
	configureDataGeneration()
}

val loader = "0.16.13"
// Fabric Properties - old sinds we now are using stonecutter
// check these on https://fabricmc.net/develop
val OLD_minecraft_version = "1.20.4"
val OLD_yarn_mappings = "1.20.4+build.3"
val OLD_fabric_api = "0.97.2+1.20.4"

dependencies {
//	minecraft("com.mojang:minecraft:$minecraftVersion")
	minecraft("com.mojang:minecraft:${stonecutter.current.project}")
//	mappings("net.fabricmc:yarn:$yarnMappings:v2")
	mappings("net.fabricmc:yarn:${property("deps.yarn_mappings")}:v2")
//	modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
	modImplementation("net.fabricmc:fabric-loader:${loader}")
}

tasks.processResources {
	inputs.property("version", project.version)
	inputs.property("minecraft", stonecutter.current.version)
	inputs.property("loader", loader)

	filesMatching("fabric.mod.json") {
		expand(mapOf(
			"version" to project.version,
			"minecraft" to stonecutter.current.version,
			"loader" to loader
		))
	}
}

loom {
	runConfigs.all {
		ideConfigGenerated(true) // Run configurations are not created for subprojects by default
		runDir = "../../run" // Use a shared run folder and create separate worlds
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release.set(17)
}

java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
	// if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

tasks.jar {
	inputs.property("archivesName", project.base.archivesName)

	from("LICENSE") {
		rename { "${it}_${inputs.properties["archivesName"]}" }
	}
}

// configure the maven publication
publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			artifactId = project.property("archives_base_name") as String
			from(components["java"])
		}
	}

	// See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
	repositories {
		// Add repositories to publish to here.
		// Notice: This block does NOT have the same function as the block in the top level.
		// The repositories here will be used for publishing your artifact, not for
		// retrieving dependencies.
	}
}