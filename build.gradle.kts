plugins {
    id("net.fabricmc.fabric-loom") version "1.15-SNAPSHOT" apply false
    id("org.jetbrains.kotlin.jvm") version "2.3.10" apply false
}

allprojects {
    group = "io.autumn"
    version = "1.1.0"

    repositories {
        mavenCentral()
        maven("https://maven.fabricmc.net/")
    }
}

subprojects {
    plugins.withId("java") {
        apply(plugin = "maven-publish")

        extensions.configure<PublishingExtension> {
            publications {
                create<MavenPublication>("mavenJava") {
                    from(components["java"])
                    groupId = project.group.toString()
                    artifactId = project.name
                    version = project.version.toString()
                }
            }

            repositories {
                mavenLocal()

                maven {
                    name = "GitHubPackages"
                    url = uri("https://maven.pkg.github.com/shinigami7x/Carminite")
                    credentials {
                        username = System.getenv("GITHUB_ACTOR")
                        password = System.getenv("GITHUB_TOKEN")
                    }
                }
            }
        }
    }
}

