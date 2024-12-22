import io.papermc.hangarpublishplugin.model.Platforms
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.5"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
    id("io.papermc.hangar-publish-plugin") version "0.1.2"
    id("com.modrinth.minotaur") version "2.+"
}

group = "net.thenextlvl.tweaks"
version = "3.1.3"

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
    compileOnly("org.projectlombok:lombok:1.18.36")
    compileOnly("net.thenextlvl.services:service-io:2.1.0")
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT") {
        exclude("org.jetbrains", "annotations")
    }

    implementation("net.thenextlvl.core:adapters:2.0.1")
    implementation("net.thenextlvl.core:files:2.0.0")
    implementation("net.thenextlvl.core:i18n:1.0.20")
    implementation("net.thenextlvl.core:nbt:2.2.14")
    implementation("net.thenextlvl.core:paper:2.0.2")
    implementation("org.bstats:bstats-bukkit:3.1.0")

    annotationProcessor("org.projectlombok:lombok:1.18.36")
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
            this.default = BukkitPluginDescription.Permission.Default.TRUE
            this.description = "Allows players to use the spawn command"
        }
        register("tweaks.command.setspawn") {
            this.default = BukkitPluginDescription.Permission.Default.OP
            this.description = "Allows players to use the setspawn command"
            this.children = listOf("tweaks.command.spawn")
        }

        register("tweaks.command.home") {
            this.default = BukkitPluginDescription.Permission.Default.TRUE
            this.description = "Allows players to teleport to their homes"
        }
        register("tweaks.command.home.limit.bypass") {
            this.default = BukkitPluginDescription.Permission.Default.OP
            this.description = "Allows players to bypass the home limit"
        }
        register("tweaks.command.home.delete") {
            this.default = BukkitPluginDescription.Permission.Default.TRUE
            this.description = "Allows players to delete their homes"
            this.children = listOf("tweaks.command.home.set")
        }
        register("tweaks.command.home.set") {
            this.default = BukkitPluginDescription.Permission.Default.TRUE
            this.description = "Allows players to set homes"
            this.children = listOf("tweaks.command.home")
        }
        register("tweaks.command.home.set.named") {
            this.default = BukkitPluginDescription.Permission.Default.TRUE
            this.description = "Allows players to set named homes"
            this.children = listOf("tweaks.command.home.set")
        }

        register("tweaks.command.warp") {
            this.default = BukkitPluginDescription.Permission.Default.TRUE
            this.description = "Allows players to warp themselves"
        }
        register("tweaks.command.warp.delete") {
            this.default = BukkitPluginDescription.Permission.Default.OP
            this.description = "Allows players to delete warps"
            this.children = listOf("tweaks.command.warp.set")
        }
        register("tweaks.command.warp.set") {
            this.default = BukkitPluginDescription.Permission.Default.OP
            this.description = "Allows players to set warps"
            this.children = listOf("tweaks.command.warp")
        }

        register("tweaks.command.weather")
        register("tweaks.command.weather.rain") { this.children = listOf("tweaks.command.weather") }
        register("tweaks.command.weather.sun") { this.children = listOf("tweaks.command.weather") }
        register("tweaks.command.weather.thunder") { this.children = listOf("tweaks.command.weather") }

        register("tweaks.command.time")
        register("tweaks.command.time.add") { this.children = listOf("tweaks.command.time") }
        register("tweaks.command.time.afternoon") { this.children = listOf("tweaks.command.time") }
        register("tweaks.command.time.day") { this.children = listOf("tweaks.command.time") }
        register("tweaks.command.time.midnight") { this.children = listOf("tweaks.command.time") }
        register("tweaks.command.time.night") { this.children = listOf("tweaks.command.time") }
        register("tweaks.command.time.noon") { this.children = listOf("tweaks.command.time") }
        register("tweaks.command.time.query") { this.children = listOf("tweaks.command.time") }
        register("tweaks.command.time.set") { this.children = listOf("tweaks.command.time.add") }
        register("tweaks.command.time.sunrise") { this.children = listOf("tweaks.command.time") }
        register("tweaks.command.time.sunset") { this.children = listOf("tweaks.command.time") }

        register("tweaks.command.enderchest.edit") { this.children = listOf("tweaks.command.enderchest.others") }
        register("tweaks.command.enderchest.others") { this.children = listOf("tweaks.command.enderchest") }
        register("tweaks.command.feed.others") { this.children = listOf("tweaks.command.feed") }
        register("tweaks.command.fly.others") { this.children = listOf("tweaks.command.fly") }
        register("tweaks.command.gamemode.others") { this.children = listOf("tweaks.command.gamemode") }
        register("tweaks.command.god.others") { this.children = listOf("tweaks.command.god") }
        register("tweaks.command.heal.others") { this.children = listOf("tweaks.command.heal") }
        register("tweaks.command.inventory.edit") { this.children = listOf("tweaks.command.inventory") }
        register("tweaks.command.ping.others") { this.children = listOf("tweaks.command.ping") }
        register("tweaks.command.speed.others") { this.children = listOf("tweaks.command.speed") }
        register("tweaks.command.vanish.others") { this.children = listOf("tweaks.command.vanish") }

        register("tweaks.command.lobby") { this.default = BukkitPluginDescription.Permission.Default.TRUE }

        register("tweaks.command.msg") { this.default = BukkitPluginDescription.Permission.Default.TRUE }
        register("tweaks.command.msg.toggle") { this.default = BukkitPluginDescription.Permission.Default.TRUE }
        register("tweaks.command.msg.reply") { this.default = BukkitPluginDescription.Permission.Default.TRUE }

        register("tweaks.command.tpa") { this.default = BukkitPluginDescription.Permission.Default.TRUE }
        register("tweaks.command.tpa.accept") { this.default = BukkitPluginDescription.Permission.Default.TRUE }
        register("tweaks.command.tpa.deny") { this.default = BukkitPluginDescription.Permission.Default.TRUE }
        register("tweaks.command.tpa.here") { this.default = BukkitPluginDescription.Permission.Default.TRUE }
        register("tweaks.command.tpa.toggle") { this.default = BukkitPluginDescription.Permission.Default.TRUE }

        register("tweaks.chat.delete") { this.children = listOf("tweaks.chat.delete.own") }


        register("tweaks.commands.environmental") {
            this.description = "Grants access to all environmental commands"
            this.default = BukkitPluginDescription.Permission.Default.OP
            this.children = listOf(
                "tweaks.command.time.query",
                "tweaks.command.time.set",
                "tweaks.command.weather.rain",
                "tweaks.command.weather.sun",
                "tweaks.command.weather.thunder"
            )
        }
        register("tweaks.commands.item") {
            this.description = "Grants access to all item commands"
            this.default = BukkitPluginDescription.Permission.Default.OP
            this.children = listOf(
                "tweaks.command.enchant",
                "tweaks.command.head",
                "tweaks.command.item",
                "tweaks.command.lore",
                "tweaks.command.rename",
                "tweaks.command.repair",
                "tweaks.command.unbreakable",
                "tweaks.command.unenchant"
            )
        }
        register("tweaks.commands.player") {
            this.description = "Grants access to all player commands"
            this.default = BukkitPluginDescription.Permission.Default.OP
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
                "tweaks.command.suicide",
                "tweaks.command.vanish"
            )
        }

        register("tweaks.commands.server") {
            this.description = "Grants access to all server commands"
            this.default = BukkitPluginDescription.Permission.Default.OP
            this.children = listOf(
                "tweaks.command.broadcast",
                "tweaks.command.lobby",
                "tweaks.command.motd"
            )
        }
        register("tweaks.commands.workstation") {
            this.description = "Grants access to all workstation commands"
            this.default = BukkitPluginDescription.Permission.Default.OP
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
