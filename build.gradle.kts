plugins {
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    // Tests (TDD)
    testImplementation(platform("org.junit:junit-bom:5.10.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.26.3")
    testImplementation("org.mockito:mockito-core:5.14.2")
}

tasks.test {
    useJUnitPlatform()
}

application {
    // Point d’entrée de ton CLI
    mainClass.set("com.samoukh.mousekeeper.adapters.cli.Main")
}

tasks.withType<JavaCompile>().configureEach {
  options.release = 17 // bytecode compatible Java 17
}

