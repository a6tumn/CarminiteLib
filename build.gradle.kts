plugins {
    id("net.fabricmc.fabric-loom") version "1.15-SNAPSHOT" apply false
    id("org.jetbrains.kotlin.jvm") version "2.3.10" apply false
}

allprojects {
    group = property("maven_group")!!
    version = property("mod_version")!!
    repositories {
        mavenCentral()
        maven("https://maven.fabricmc.net/")
    }
}
