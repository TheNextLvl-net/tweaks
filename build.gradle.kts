import io.papermc.hangarpublishplugin.model.Platforms
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("io.github.goooler.shadow") version "8.1.7"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
    id("io.papermc.hangar-publish-plugin") version "0.1.2"
    id("com.modrinth.minotaur") version "2.+"
}

group = "net.thenextlvl"
version = "2.1.3"

repositories {
    mavenCentral()
    maven("https://repo.thenextlvl.net/releases")
    maven("https://repo.thenextlvl.net/snapshots")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    compileOnly("net.luckperms:api:5.4")
    compileOnly("org.projectlombok:lombok:1.18.34")
    compileOnly("net.thenextlvl.core:annotations:2.0.1")
    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")

    implementation("net.thenextlvl.core:nbt:1.4.2")
    implementation("net.thenextlvl.core:files:1.0.5")
    implementation("net.thenextlvl.core:i18n:1.0.19")
    implementation("net.thenextlvl.core:paper:1.3.5")
    implementation("org.bstats:bstats-bukkit:3.0.2")

    annotationProcessor("org.projectlombok:lombok:1.18.34")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.3")
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
        minecraftVersion("1.21")
    }
}

java {
    targetCompatibility = JavaVersion.VERSION_21
    sourceCompatibility = JavaVersion.VERSION_21
}

paper {
    name = "Tweaks"
    main = "net.thenextlvl.tweaks.TweaksPlugin"
    description = "A useful command collection"
    apiVersion = "1.21"
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
                "tweaks.command.broadcast",
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
                url("LuckPerms", "https://luckperms.net/") {
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
        optional.project("luckperms")
    }
}