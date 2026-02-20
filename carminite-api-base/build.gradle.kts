dependencies {
    minecraft(libs.minecraft)
    implementation(libs.fabricLoader)
    implementation(libs.fabricApi)
    implementation(libs.fabricKotlin)

    implementation(project(":carminite-blockproperties-api-v1"))
    implementation(project(":carminite-registry-api-v1"))
}

base {
    archivesName.set("carminite-api")
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}