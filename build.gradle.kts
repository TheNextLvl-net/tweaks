import io.papermc.hangarpublishplugin.model.Platforms
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.2.3"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.hangar-publish-plugin") version "0.1.2"
}

group = "net.thenextlvl"
version = "2.0.7"

repositories {
    mavenCentral()
    maven("https://repo.thenextlvl.net/releases")
    maven("https://repo.thenextlvl.net/snapshots")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("net.luckperms:api:5.4")
    compileOnly("org.projectlombok:lombok:1.18.30")
    compileOnly("net.thenextlvl.core:annotations:2.0.1")
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")

    implementation("net.thenextlvl.core:nbt:1.3.9")
    implementation("net.thenextlvl.core:files:1.0.3")
    implementation("net.thenextlvl.core:i18n:1.0.13")
    implementation("net.thenextlvl.core:paper:1.2.3")
    implementation("org.bstats:bstats-bukkit:3.0.2")

    annotationProcessor("org.projectlombok:lombok:1.18.30")

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
        minecraftVersion("1.20.4")
    }
}

java {
    targetCompatibility = JavaVersion.VERSION_19
    sourceCompatibility = JavaVersion.VERSION_19
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
                "tweaks.command.noon",
                "tweaks.command.night",
                "tweaks.command.midnight",
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
                "tweaks.command.offline-tp",
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
        register("tweaks.chat.delete") {
            this.children = listOf(
                "tweaks.chat.delete.own"
            )
        }
        register("tweaks.chat.delete.own")

        register("tweaks.command.gamemode.others")
        register("tweaks.command.inventory.edit")
        register("tweaks.command.enderchest.edit")
        register("tweaks.command.enderchest.others")
        register("tweaks.command.feed.others")
        register("tweaks.command.fly.others")
        register("tweaks.command.god.others")
        register("tweaks.command.heal.others")
        register("tweaks.command.offline-tp.others")
        register("tweaks.command.ping.others")
        register("tweaks.command.speed.others")
    }
}

val versionString: String = project.version as String
val isRelease: Boolean = !versionString.contains("-pre")

hangarPublish { // docs - https://docs.papermc.io/misc/hangar-publishing
    publications.register("plugin") {
        id.set("Tweaks")
        version.set(project.version as String)
        channel.set(if (isRelease) "Release" else "Snapshot")
        if (extra.has("HANGAR_API_TOKEN"))
            apiKey.set(extra["HANGAR_API_TOKEN"] as String)
        else apiKey.set(System.getenv("HANGAR_API_TOKEN"))
        platforms {
            register(Platforms.PAPER) {
                jar.set(tasks.shadowJar.flatMap { it.archiveFile })
                val versions: List<String> = (property("paperVersion") as String)
                    .split(",")
                    .map { it.trim() }
                platformVersions.set(versions)
                dependencies {
                    url("LuckPerms", "https://luckperms.net/") {
                        required.set(false)
                    }
                }
            }
        }
    }
}
