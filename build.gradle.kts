plugins {
    kotlin("jvm") version "1.9.21"
}

group = "me.lucifer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/soramitsukhmer-lab/iroha-java")
        credentials {
            username = System.getenv("GIT_PUB_USER")
            password = System.getenv("GIT_TOKEN")
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("io.github.soramitsukhmer-lab.iroha-java:client:7.0.1.0")
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