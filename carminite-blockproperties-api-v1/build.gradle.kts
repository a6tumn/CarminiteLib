import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("net.fabricmc.fabric-loom")
    id("org.jetbrains.kotlin.jvm")
    id("maven-publish")
}

dependencies {
    minecraft(libs.minecraft)
    implementation(libs.fabricLoader)
    implementation(libs.fabricApi)
    implementation(libs.fabricKotlin)
}

loom {
    mods {
        create("carminite-blockproperties-api-v1") {
            sourceSet(sourceSets.main.get())
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
    archivesName.set("carminite-blockproperties-api-v1")
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
    compilerOptions.jvmTarget.set(JvmTarget.JVM_25)
}

tasks.named<Jar>("jar") {
    inputs.property("archivesName", "carminite-blockproperties-api-v1")

    from("LICENSE") {
        rename { fileName ->
            "${fileName}_${"carminite-blockproperties-api-v1"}"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "carminite-blockproperties-api-v1"
            from(components["java"])
        }
    }

    repositories {
        // Define module-specific publishing repositories if needed
    }
}
