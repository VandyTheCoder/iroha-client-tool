plugins {
    kotlin("jvm") version "1.9.21"
}

group = "me.lucifer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/soramitsukhmer/iroha-java")
        credentials {
            username = System.getenv("GIT_PUBLISH_USER")
            password = System.getenv("GIT_PUBLISH_PASSWORD")
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("com.soramitsukhmer.iroha-java:client:7.0.1-rc.4")
    implementation("org.slf4j:slf4j-simple:1.7.32")
    testImplementation("com.google.protobuf:protobuf-java-util:3.12.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}