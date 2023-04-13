plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper") version "2.0.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "net.thenextlvl"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")

    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.runServer {
    minecraftVersion("1.19.4")
}

bukkit {
    name = "Tweaks"
    main = "net.thenextlvl.tweaks.TweaksPlugin"
    apiVersion = "1.19"
    authors = listOf("CyntrixAlgorithm")
    commands {}
}