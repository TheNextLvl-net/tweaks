plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.0.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "net.thenextlvl"
version = "1.2.0"

repositories {
    mavenCentral()
    maven("https://repo.thenextlvl.net/releases")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.26")
    compileOnly("net.thenextlvl.core:annotations:1.0.0")
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")

    implementation("net.thenextlvl.core:api:3.1.10")

    annotationProcessor("org.projectlombok:lombok:1.18.26")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

tasks {
    test {
        useJUnitPlatform()
    }
    assemble {
        dependsOn(shadowJar)
    }
    shadowJar {
        minimize()
    }
    runServer {
        minecraftVersion("1.19.4")
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

bukkit {
    name = "Tweaks"
    main = "net.thenextlvl.tweaks.TweaksPlugin"
    apiVersion = "1.19"
    website = "https://thenextlvl.net"
    authors = listOf("CyntrixAlgorithm", "NonSwag")

    permissions {
        register("tweaks.command.enderchest.others")
        register("tweaks.command.feed.others")
        register("tweaks.command.fly.others")
        register("tweaks.command.god.others")
        register("tweaks.command.heal.others")
        register("tweaks.command.ping.others")
        register("tweaks.command.speed.others")
        register("tweaks.command.gamemode.others")
        register("tweaks.command.gamemode.all") {
            this.children = listOf(
                "tweaks.command.gamemode",
                "tweaks.command.gamemode.survival",
                "tweaks.command.gamemode.creative",
                "tweaks.command.gamemode.adventure",
                "tweaks.command.gamemode.spectator"
            )
        }
    }
}
