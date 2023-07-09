plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.0.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "net.thenextlvl"
version = "1.3.1"

repositories {
    mavenCentral()
    maven("https://repo.thenextlvl.net/releases")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://nexuslite.gcnt.net/repos/other/")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.26")
    compileOnly("net.thenextlvl.core:annotations:1.0.0")
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")

    implementation("com.tcoded:FoliaLib:0.2.0")
    implementation("net.thenextlvl.core:api:3.1.10")

    annotationProcessor("org.projectlombok:lombok:1.18.26")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

tasks {
    test {
        useJUnitPlatform()
    }
    shadowJar {
        minimize()
        relocate("com.tcoded.folialib", "net.thenextlvl.tweaks.folialib")
    }
    runServer {
        minecraftVersion("1.20.1")
    }
}

java {
    targetCompatibility = JavaVersion.VERSION_17
    sourceCompatibility = JavaVersion.VERSION_17
}

bukkit {
    name = "Tweaks"
    main = "net.thenextlvl.tweaks.TweaksPlugin"
    apiVersion = "1.19"
    website = "https://thenextlvl.net"
    authors = listOf("CyntrixAlgorithm", "NonSwag")

    foliaSupported = true

    permissions {
        register("tweaks.command.inventory.edit")
        register("tweaks.command.enderchest.edit")
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
