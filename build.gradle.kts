import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val mod_version: String by project
val maven_group: String by project
val archives_base_name: String by project

plugins {
    // REMEMBER TO UPDATE LOOM VERSION HERE
    id("net.fabricmc.fabric-loom") version "1.15-SNAPSHOT"
    id("maven-publish")
    id("org.jetbrains.kotlin.jvm") version "2.3.10"
}

dependencies {
    minecraft(libs.minecraft)
    implementation(libs.fabricLoader)
    implementation(libs.fabricApi)
    implementation(libs.fabricKotlin)
}

fabricApi {
    //DATA GENERATION GOES HERE
}

loom {
    splitEnvironmentSourceSets()

    mods {
        create("carminite") {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets["client"])
        }
    }
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}


base {
    archivesName.set(archives_base_name)
}

repositories {
    // REPOSITORIES DEPENDED ON HERE
}

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(25)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_25)
    }
}

tasks.named<Jar>("jar") {
    inputs.property("archivesName", archives_base_name)

    from("LICENSE") {
        rename { fileName ->
            "${fileName}_${archives_base_name}"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = archives_base_name
            from(components["java"])
        }
    }

    repositories {
        // REPOSITORIES TO PUBLISH HERE
    }
}