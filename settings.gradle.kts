rootProject.name = "carminite"

include("carminite-blockproperties-api-v1")
include("carminite-registry-api-v1")
include("carminite-api-base")

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