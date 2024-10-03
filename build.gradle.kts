import io.papermc.hangarpublishplugin.model.Platforms
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("io.github.goooler.shadow") version "8.1.8"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
    id("io.papermc.hangar-publish-plugin") version "0.1.2"
    id("com.modrinth.minotaur") version "2.+"
}

group = "net.thenextlvl.tweaks"
version = "2.1.5"

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks.compileJava {
    options.release.set(21)
}

repositories {
    mavenCentral()
    maven("https://repo.thenextlvl.net/releases")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.34")
    compileOnly("net.thenextlvl.core:annotations:2.0.1")
    compileOnly("net.thenextlvl.services:service-io:2.0.0")
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")

    implementation("net.thenextlvl.core:adapters:1.0.9")
    implementation("net.thenextlvl.core:files:1.0.5")
    implementation("net.thenextlvl.core:i18n:1.0.19")
    implementation("net.thenextlvl.core:nbt:1.4.2")
    implementation("net.thenextlvl.core:paper:1.5.2")
    implementation("org.bstats:bstats-bukkit:3.1.0")

    annotationProcessor("org.projectlombok:lombok:1.18.34")
}

tasks.shadowJar {
    relocate("org.bstats", "net.thenextlvl.tweaks.bstats")
    minimize()
}

tasks.runServer {
    minecraftVersion("1.21")
}

paper {
    name = "Tweaks"
    main = "net.thenextlvl.tweaks.TweaksPlugin"
    description = "A useful command collection"
    apiVersion = "1.21"
    website = "https://thenextlvl.net"
    authors = listOf("CyntrixAlgorithm", "NonSwag")
    load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD

    foliaSupported = true

    serverDependencies {
        register("ServiceIO") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            required = false
        }
    }

    permissions {
        register("tweaks.command.spawn") {
            default = BukkitPluginDescription.Permission.Default.TRUE
            description = "Allows players to use the spawn command"
        }
        register("tweaks.command.setspawn") {
            default = BukkitPluginDescription.Permission.Default.OP
            description = "Allows players to use the setspawn command"
            children = listOf("tweaks.command.spawn")
        }

        register("tweaks.command.home") {
            default = BukkitPluginDescription.Permission.Default.TRUE
            description = "Allows players to teleport to their homes"
        }
        register("tweaks.command.home.limit.bypass") {
            default = BukkitPluginDescription.Permission.Default.OP
            description = "Allows players to bypass the home limit"
        }
        register("tweaks.command.home.delete") {
            default = BukkitPluginDescription.Permission.Default.TRUE
            description = "Allows players to delete their homes"
            children = listOf("tweaks.command.home.set")
        }
        register("tweaks.command.home.set") {
            default = BukkitPluginDescription.Permission.Default.TRUE
            description = "Allows players to set homes"
            children = listOf("tweaks.command.home")
        }
        register("tweaks.command.home.set.named") {
            default = BukkitPluginDescription.Permission.Default.TRUE
            description = "Allows players to set named homes"
            children = listOf("tweaks.command.home.set")
        }

        register("tweaks.command.warp") {
            default = BukkitPluginDescription.Permission.Default.TRUE
            description = "Allows players to warp themselves"
        }
        register("tweaks.command.warp.delete") {
            default = BukkitPluginDescription.Permission.Default.OP
            description = "Allows players to delete warps"
            children = listOf("tweaks.command.warp.set")
        }
        register("tweaks.command.warp.set") {
            default = BukkitPluginDescription.Permission.Default.OP
            description = "Allows players to set warps"
            children = listOf("tweaks.command.warp")
        }


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
                "tweaks.command.speed",
                "tweaks.command.suicide"
            )
        }
        register("tweaks.commands.server") {
            this.children = listOf(
                "tweaks.command.broadcast",
                "tweaks.command.lobby",
                "tweaks.command.motd"
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
        register("tweaks.command.lobby") {
            default = BukkitPluginDescription.Permission.Default.TRUE
        }
        register("tweaks.command.reply") {
            default = BukkitPluginDescription.Permission.Default.TRUE
        }
        register("tweaks.command.msg") {
            default = BukkitPluginDescription.Permission.Default.TRUE
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

val versions: List<String> = (property("gameVersions") as String)
    .split(",")
    .map { it.trim() }

hangarPublish { // docs - https://docs.papermc.io/misc/hangar-publishing
    publications.register("plugin") {
        id.set("Tweaks")
        version.set(versionString)
        channel.set(if (isRelease) "Release" else "Snapshot")
        apiKey.set(System.getenv("HANGAR_API_TOKEN"))
        platforms.register(Platforms.PAPER) {
            jar.set(tasks.shadowJar.flatMap { it.archiveFile })
            platformVersions.set(versions)
            dependencies {
                hangar("ServiceIO") {
                    required.set(false)
                }
            }
        }
    }
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("HLkJsjy0")
    versionType = if (isRelease) "release" else "beta"
    uploadFile.set(tasks.shadowJar)
    gameVersions.set(versions)
    loaders.add("paper")
    loaders.add("folia")
    dependencies {
        optional.project("ServiceIO")
    }
}