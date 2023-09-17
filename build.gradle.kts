import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.0.1"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "net.thenextlvl"
version = "1.4.7"

repositories {
    mavenCentral()
    maven("https://repo.thenextlvl.net/releases")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("net.luckperms:api:5.4")
    compileOnly("org.projectlombok:lombok:1.18.26")
    compileOnly("net.thenextlvl.core:annotations:1.0.0")
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")

    implementation("net.thenextlvl.core:api:3.1.12")
    implementation("org.bstats:bstats-bukkit:3.0.2")

    annotationProcessor("org.projectlombok:lombok:1.18.26")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

tasks {
    test {
        useJUnitPlatform()
    }
    shadowJar {
        minimize()
        relocate("org.bstats", "net.thenextlvl.tweaks.bstats")
    }
    runServer {
        minecraftVersion("1.20.1")
    }
}

java {
    targetCompatibility = JavaVersion.VERSION_17
    sourceCompatibility = JavaVersion.VERSION_17
}

paper {
    name = "Tweaks"
    main = "net.thenextlvl.tweaks.TweaksPlugin"
    apiVersion = "1.19"
    website = "https://thenextlvl.net"
    authors = listOf("CyntrixAlgorithm", "NonSwag")

    foliaSupported = true

    serverDependencies {
        register("LuckPerms") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            required = false
        }
    }

    permissions {
        register("tweaks.commands.environmental") {
            this.children = listOf(
                "tweaks.command.day",
                "tweaks.command.night",
                "tweaks.command.rain",
                "tweaks.command.sun",
                "tweaks.command.thunder"
            )
        }
        register("tweaks.commands.item") {
            this.children = listOf(
                "tweaks.command.enchant",
                "tweaks.command.head",
                "tweaks.command.item",
                "tweaks.command.lore",
                "tweaks.command.rename",
                "tweaks.command.repair",
                "tweaks.command.unenchant"
            )
        }
        register("tweaks.commands.player") {
            this.children = listOf(
                "tweaks.command.back",
                "tweaks.command.enderchest",
                "tweaks.command.feed",
                "tweaks.command.fly",
                "tweaks.command.gamemode",
                "tweaks.command.god",
                "tweaks.command.hat",
                "tweaks.command.heal",
                "tweaks.command.inventory",
                "tweaks.command.ping",
                "tweaks.command.seen",
                "tweaks.command.speed"
            )
        }
        register("tweaks.commands.server") {
            this.children = listOf(
                "tweaks.command.broadcast"
            )
        }
        register("tweaks.commands.workstation") {
            this.children = listOf(
                "tweaks.command.anvil",
                "tweaks.command.cartography-table",
                "tweaks.command.enchanting-table",
                "tweaks.command.grindstone",
                "tweaks.command.loom",
                "tweaks.command.smithing-table",
                "tweaks.command.stonecutter",
                "tweaks.command.workbench"
            )
        }
        register("tweaks.command.gamemode.all") {
            this.children = listOf(
                "tweaks.command.gamemode",
                "tweaks.command.gamemode.survival",
                "tweaks.command.gamemode.creative",
                "tweaks.command.gamemode.adventure",
                "tweaks.command.gamemode.spectator"
            )
        }
        register("tweaks.command.gamemode.others")
        register("tweaks.command.inventory.edit")
        register("tweaks.command.enderchest.edit")
        register("tweaks.command.enderchest.others")
        register("tweaks.command.feed.others")
        register("tweaks.command.fly.others")
        register("tweaks.command.god.others")
        register("tweaks.command.heal.others")
        register("tweaks.command.ping.others")
        register("tweaks.command.speed.others")
        register("tweaks.chat.delete")
    }
}
