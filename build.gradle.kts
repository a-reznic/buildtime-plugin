import com.vanniktech.maven.publish.GradlePublishPlugin
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    application
    kotlin("jvm").version("2.0.0")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish").version("1.2.1")
    kotlin("plugin.serialization").version("2.0.0")
    id("com.vanniktech.maven.publish").version("0.29.0")
}

repositories {
    mavenCentral()
    mavenLocal()
    google()
}

group = "com.reznicsoftware.buildtime"
version = "0.2.0"

gradlePlugin {
    plugins {
        create("buildTimeTest") {
            id = group.toString()
            implementationClass = "com.reznicsoftware.buildtime.TestPlugin"
            version = version
        }
    }
}
tasks.test {
    useJUnitPlatform()
}
dependencies {
    implementation("com.github.oshi:oshi-core:6.6.1")

    val ktorVersion = "2.3.12"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    mavenPublishing {
        coordinates(project.group.toString(), "test-plugin", project.version.toString())

        pom {
            name.set("TestPlugin")
            description.set("Test build gradle time")
            inceptionYear.set("2024")
            url.set("https://github.com/a-reznic/buildtime-test-plugin.git")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("a-reznic")
                    name.set("Alexandru Reznic")
                    url.set("https://github.com/a-reznic/")
                }
            }
            scm {
                url.set("https://github.com/a-reznic/buildtime-test-plugin")
                connection.set("scm:git:https://github.com/a-reznic/buildtime-test-plugin.git")
            }
        }
    }
    configure(GradlePublishPlugin())
}
