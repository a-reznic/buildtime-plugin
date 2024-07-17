
plugins {
    application
    kotlin("jvm").version ("2.0.0")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish").version ("1.2.1")
    kotlin("plugin.serialization").version("2.0.0")
//    id("com.reznicsoftware.buildtime").version ("0.0.3")
}
repositories {
    mavenCentral()
    mavenLocal()
    google()
}

group = "com.reznicsoftware.buildtime"
version = "0.0.3"

gradlePlugin {
    plugins {
        create("buildTimeTest") {
            id = group.toString()
            implementationClass = "com.reznicsoftware.buildtime.TestPlugin"
            version = version

        }
    }
}
dependencies {
    implementation("com.github.oshi:oshi-core:6.6.1")

    val ktorVersion = "2.3.12"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}