rootProject.name = "carminite"

include("carminite-blockproperties-api-v1")

pluginManagement {
    repositories {
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        mavenCentral()
        gradlePluginPortal()
    }
}