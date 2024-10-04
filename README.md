# Tweaks

A modern alternative to EssentialsX<br/>
bStat Metrics can be found [here](https://bstats.org/plugin/bukkit/TheNextLvl%20Tweaks/19651)

## Download

[Modrinth](https://modrinth.com/plugin/tweaks-1)<br>
[Hangar](https://hangar.papermc.io/TheNextLvl/Tweaks)

## Features _(Configurable)_

- You heal animals by feeding them
- Unless an animal is at full health, they can't breed
- Cooldown for milking cows/mushroom-cows (with bowls)
- Cooldown for Sheep wool growth
- You can define a default permission level (purely client-side)

## Versions

> [!IMPORTANT]
> Version 3.0.0 introduces several breaking changes: command, permission, and translation incompatibilities.<br>
> It is highly recommended to delete the entire `Tweaks` folder before upgrading to avoid conflicts with outdated
> configurations.

> [!NOTE]
> Tweaks only supports the latest version of Paper (1.21.1)<br>
> The latest version of Tweaks requires Java 21

[Latest version supporting 1.19-1.20.4 (Java 19)](https://github.com/TheNextLvl-net/tweaks/releases/tag/v2.0.10)<br>
[Latest version supporting 1.19-1.20.4 (Java 17)](https://github.com/TheNextLvl-net/tweaks/releases/tag/v2.0.5)<br>

---

## What is New in the Latest Major Update

This release brings a host of exciting new features and enhancements:

- **New Commands & Features:** Added commands for `homes`, `spawn`, `tpa`, `warps`, and a variety of new social
  commands.
- **Advanced Configurations:**
    - **Feature Toggles:** Every feature can now be enabled or disabled individually, giving you full control over your
      server’s functionality.
    - **Custom Links:** Set up server links to your website or social media platforms.
    - **Teleportation Cooldowns:** Teleportation now includes configurable cooldowns to balance gameplay.
    - **Conditional Spawn Teleportation:** Decide when players should be teleported to spawn based on specific
      conditions.

- **GUI Customization:**
    - Icon Overrides: Customize the icons for your homes and warps GUIs to fit your server’s theme.
    - **Chat vs. GUI:** Choose between using interactive GUIs or simple chat messages for warps and homes.
    - **Full GUI Customization:** Modify the appearance and layout of GUIs to your liking.

- **Improved Command Flexibility:**
    - **Command Customization:** A new `commands.json` file allows you to rename commands and set up aliases
      effortlessly.
    - **Enchantment Overflow:** You can now allow enchantment overflow up to level 255 in commands for even more
      control.

- **Performance Enhancements:**
    - **Message Deletion Timeout:** Configure a timeout for message deletion to prevent memory buildup from undeleted
      messages.

---

# Commands

## Environmental commands

### Time commands

| Command                                        | Alternatives          | Description                             | Permission                    |
|------------------------------------------------|-----------------------|-----------------------------------------|-------------------------------|
| `/time add <time> [<world>]`                   |                       | add N amount of ticks to the world time | tweaks.command.time.add       |
| `/time query [day/daytime/gametime] [<world>]` |                       | query the time of a world               | tweaks.command.time.query     |
| `/time set afternoon [<world>]`                |                       | set the time to afternoon (9000 ticks)  | tweaks.command.time.afternoon |
| `/time set day [<world>]`                      | `/day [<world>]`      | set the time to day (1000 ticks)        | tweaks.command.time.day       |
| `/time set midnight [<world>]`                 | `/midnight [<world>]` | set the time to midnight (18000 ticks)  | tweaks.command.time.midnight  |
| `/time set morning [<world>]`                  |                       | set the time to morning (0 ticks)       | tweaks.command.time.morning   |
| `/time set night [<world>]`                    | `/night [<world>]`    | set the time to night (13000 ticks)     | tweaks.command.time.night     |
| `/time set noon [<world>]`                     | `/noon [<world>]`     | set the time to noon (6000 ticks)       | tweaks.command.time.noon      |
| `/time set sunrise [<world>]`                  |                       | set the time to sunrise (23000 ticks)   | tweaks.command.time.sunrise   |
| `/time set sunset [<world>]`                   |                       | set the time to sunset (12000 ticks)    | tweaks.command.time.sunset    |

### Weather commands

| Command            | Alternatives | Description       | Permission                     |
|--------------------|--------------|-------------------|--------------------------------|
| `/weather clear`   | `/sun`       | let the sun shine | tweaks.command.weather.sun     |
| `/weather rain`    | `/rain`      | let it rain       | tweaks.command.weather.rain    |
| `/weather thunder` | `/thunder`   | let it thunder    | tweaks.command.weather.thunder |

The perm-pack to grant all permissions: `tweaks.commands.environmental`

## Home commands

| Command                 | Aliases    | Description                | Permission                 |
|-------------------------|------------|----------------------------|----------------------------|
| `/delete-home [<name>]` | `/delhome` | Delete your homes          | tweaks.command.home.delete |
| `/home [<name>]`        |            | Teleport you to your homes | tweaks.command.home        |
| `/homes`                |            | List all of your homes     | tweaks.command.home        |
| `/set-home [<name>]`    | `/sethome` | Set a home                 | tweaks.command.home.set    |

## Item commands

| Command                              | Aliases  | Description                                        | Permission                 |
|--------------------------------------|----------|----------------------------------------------------|----------------------------|
| `/enchant <enchantment> [<level>]`   |          | enchant your tools                                 | tweaks.command.enchant     |
| `/head [player/url/value] [<value>]` | `/skull` | receive heads or information about them            | tweaks.command.head        |
| `/item [item] (amount)`              | `/i`     | gives you an item of your choice                   | tweaks.command.item        |
| `/lore apped <text>`                 |          | append a line to the lore of your item             | tweaks.command.lore        |
| `/lore clear`                        |          | clear the lore of your item                        | tweaks.command.lore        |
| `/lore prepend <text>`               |          | prepend a line to the lore of your item            | tweaks.command.lore        |
| `/lore replace <text> <replacement>` |          | replace a specific string in the lore of your item | tweaks.command.lore        |
| `/lore set <text>`                   |          | set the lore of your item                          | tweaks.command.lore        |
| `/rename <name>`                     |          | change the displayname of your item                | tweaks.command.rename      |
| `/repair [all]`                      |          | repair your tools                                  | tweaks.command.repair      |
| `/unbreakable`                       |          | makes your item unbreakable                        | tweaks.command.unbreakable |
| `/unenchant <enchantment>`           |          | unenchant your tools                               | tweaks.command.unenchant   |

The perm-pack to grant all permissions: `tweaks.commands.item`

## MSG commands

| Command                   | Aliases                                | Description                                | Permission                |
|---------------------------|----------------------------------------|--------------------------------------------|---------------------------|
| `/msg <player> <message>` | `/tell`<br/>`/write`<br/>`/t`<br/>`/w` | Send a private message to a player         | tweaks.command.msg        |
| `/msgtoggle`              | `/togglemsg`                           | Toggle receiving private messages          | tweaks.command.msg.toggle |
| `/reply <message>`        | `/r`                                   | Reply to the last received private message | tweaks.command.msg.reply  |

## Player commands

| Command                                       | Aliases                  | Description                                        | Permission                                                                                                                                                          | Argument | Permission |
|-----------------------------------------------|--------------------------|----------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------|------------|
| `/back`                                       |                          | go back to your last position                      | tweaks.command.back                                                                                                                                                 |          |            |
| `/enderchest [<player>]`                      | `/ec`                    | open your own or someone else's enderchest         | tweaks.command.enderchest<br/>permits to use the command /enderchest<br/>to allow changes inside of an enderchest, grant<ul><li>tweaks.command.enderchest.edit</ul> | player   | *.others   |
| `/feed [<players>]`                           |                          | satisfy your own or someone else's hunger          | tweaks.command.feed                                                                                                                                                 | player   | *.others   |
| `/fly [<players>]`                            | `/flight`                | toggle your own or someone else's fly state        | tweaks.command.fly                                                                                                                                                  | player   | *.others   |
| `/gamemode [gamemode] [<player>]`             | `/gm`                    | change your own or someone else's gamemode         | tweaks.command.gamemode<br/>permits to use the command /gamemode                                                                                                    | player   | *.others   |
| `/god [<targets>]`                            | `/invincible`            | make you or someone else invulnerable              | tweaks.command.god                                                                                                                                                  | player   | *.others   |
| `/hat`                                        |                          | equip your item as a hat                           | tweaks.command.hat                                                                                                                                                  |          |            |
| `/heal [<targets>]`                           |                          | heal yourself or someone else                      | tweaks.command.heal                                                                                                                                                 | player   | *.others   |
| `/inventory [<player>]`                       | `/inv`<br/>`/invsee`     | open your own or someone else's inventory          | tweaks.command.inventory<br/>permits to use the command /inventory<br/>to allow changes inside of an inventory, grant<ul><li>tweaks.command.inventory.edit</ul>     |          |            |
| `/offline-teleport <player> [<target>]`       | `/offline-tp`<br/>`/tpo` | teleport offline-players to others or you to them  | tweaks.command.offline-tp                                                                                                                                           |          |            |
| `/ping [<player>]`                            | `/latency`<br/>`/ms`     | see your own or someone else's latency             | tweaks.command.ping                                                                                                                                                 | player   | *.others   |
| `/seen <player>`                              | `/find`                  | gives you information about a player               | tweaks.command.seen                                                                                                                                                 |          |            |
| `/speed <speed> [fly/sneak/walk] [<targets>]` |                          | Change your own or others fly, sneak or walk speed | tweaks.command.speed                                                                                                                                                | targets  | *.others   |
| `/speed reset [fly/sneak/walk] [<targets>]`   |                          | Reset your own or others fly, sneak or walk speed  | tweaks.command.speed                                                                                                                                                | targets  | *.others   |
| `/suicide`                                    |                          | take your own life                                 | tweaks.command.suicide                                                                                                                                              |          |            |
| `/vanish (player)`                            | `/v`<br/>`/invisible`    | hide yourself or someone else from others          | tweaks.command.vanish                                                                                                                                               |          |            |

The perm-pack to grant all permissions: `tweaks.commands.player`

## Server commands

| Command                              | Aliases         | Description                                | Permission               |
|--------------------------------------|-----------------|--------------------------------------------|--------------------------|
| `/broadcast <message>`               | `/bc`           | Broadcast a message                        | tweaks.command.broadcast |
| `/lobby`                             | `/hub`<br/>`/l` | Connect to the lobby                       | tweaks.command.lobby     |
| `/motd clear`                        |                 | Reset the motd of the server               | tweaks.command.motd      |
| `/motd get`                          |                 | Get the motd of the server                 | tweaks.command.motd      |
| `/motd replace <text> <replacement>` |                 | Replace a string in the motd of the server | tweaks.command.motd      |
| `/motd set <motd>`                   |                 | Change the motd of the server              | tweaks.command.motd      |

The perm-pack to grant all permissions: `tweaks.commands.server`

## Social commands

| Command      | Aliases                            | Description                               |
|--------------|------------------------------------|-------------------------------------------|
| `/discord`   | `/dc`                              | Receive the server's Discord invite       |
| `/reddit`    |                                    | Receive the server's Subreddit link       |
| `/teamspeak` | `/teamspeak3`<br/>`/ts`<br/>`/ts3` | Receive the server's TeamSpeak link       |
| `/website`   |                                    | Receive the server's Website url          |
| `/x`         | `/twitter`                         | Receive the server's X profile link       |
| `/youtube`   | `/yt`                              | Receive the server's YouTube channel link |

## Spawn commands

| Command                                             | Aliases     | Description            | Permission              |
|-----------------------------------------------------|-------------|------------------------|-------------------------|
| `/set-spawn [<position>] [<yaw>] <pitch> [<world>]` | `/setspawn` | Set the spawn location | tweaks.command.setspawn |
| `/spawn`                                            |             | Teleport you to spawn  | tweaks.command.spawn    |

## TPA commands

| Command             | Aliases      | Description                                     | Permission                |
|---------------------|--------------|-------------------------------------------------|---------------------------|
| `/tpa <player>`     | `/tpask`     | Request to teleport to a player                 | tweaks.command.tpa        |
| `/tpaccept`         |              | Accept a teleport request                       | tweaks.command.tpa.accept |
| `/tpadeny`          | `/tpdeny`    | Deny a teleport request                         | tweaks.command.tpa.deny   |
| `/tpahere <player>` | `/tphere`    | Request a player to teleport to you             | tweaks.command.tpa.here   |
| `/tpatoggle`        | `/toggletpa` | Toggle the ability to receive teleport requests | tweaks.command.tpa.toggle |

## Warp commands

| Command               | Aliases    | Description              | Permission                 |
|-----------------------|------------|--------------------------|----------------------------|
| `/delete-warp <warp>` | `/delwarp` | Delete a warp point      | tweaks.command.warp.delete |
| `/set-warp <warp>`    | `/setwarp` | Set a new warp point     | tweaks.command.warp.set    |
| `/warp <warp>`        |            | Warp to a location       | tweaks.command.warp        |
| `/warps`              |            | List all available warps | tweaks.command.warp        |

## Workstation commands

| Command              | Aliases        | Description                      | Permission                       |
|----------------------|----------------|----------------------------------|----------------------------------|
| `/anvil`             |                | Open a virtual anvil             | tweaks.command.anvil             |
| `/cartography-table` | `/cartography` | Open a virtual cartography table | tweaks.command.cartography-table |
| `/enchanting-table`  | `/enchanting`  | Open a virtual enchanting table  | tweaks.command.enchanting-table  |
| `/grindstone`        |                | Open a virtual grindstone        | tweaks.command.grindstone        |
| `/loom`              |                | Open a virtual loom              | tweaks.command.loom              |
| `/smithing-table`    | `/smithing`    | Open a virtual smithing table    | tweaks.command.smithing-table    |
| `/stonecutter`       |                | Open a virtual stonecutter       | tweaks.command.stonecutter       |
| `/workbench`         | `/wb`          | Open a virtual workbench         | tweaks.command.workbench         |

The perm-pack to grant all permissions: `tweaks.commands.workstation`

---

# Configuration

## General

| Option                   | Description                                                                                                                                                                    | Value           |
|--------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------|
| back-buffer-stack-size   | the amount of locations stored for the /back command<br/>_the larger the value the more ram will be reserved_                                                                  | `Integer` (1-n) |
| default-permission-level | the permission level determines which commands a player can access<br/>_this option is purely-client side therefore has no effect on the server_<br/>(-1 disables this option) | `byte` (-1/0-4) |
| override-join-message    | whether to override the join message<br/>_the message can be changed within the local files_                                                                                   | `true`, `false` |
| override-quit-message    | whether to override the quit message<br/>_the message can be changed within the local files_                                                                                   | `true`, `false` |
| override-chat            | whether to override the chat<br/>_the chat format can be changed within the [`chat-format`](#minimessage-chat-tags) entry_                                                     | `true`, `false` |
| log-chat                 | whether to send the chat to the console<br/>_not related with override-chat_                                                                                                   | `true`, `false` |

For more information about the permission level visit: https://minecraft.fandom.com/wiki/Permission_level

## Server

| Option               | Description                                 | Value           |
|----------------------|---------------------------------------------|-----------------|
| enable-lobby-command | whether the lobby command should be enabled | `true`, `false` |
| lobby-server-name    | the name of the lobby server                | String          |

## MiniMessage Chat Tags

These tags can be used within the `chat-format` entry<br/>
To insert a tag use `<tag>`

| Tag             | Description                                                                                | Requires  |
|-----------------|--------------------------------------------------------------------------------------------|-----------|
| player_prefix   | the prefix of the sender                                                                   | LuckPerms |
| player_suffix   | the suffix of the sender                                                                   | LuckPerms |
| player_group    | the group of the sender                                                                    | LuckPerms |
| signature       | the message signature                                                                      |           |
| delete          | a clickable component to delete a certain message<br/>(requires the signature as argument) |           |
| display_name    | the display name of the sender                                                             |           |
| message_content | the text content of the sent message<br/>_usable within click actions_                     |           |
| message         | the original message sent                                                                  |           |
| player          | the name of the sender                                                                     |           |
| world           | the world of the sender                                                                    |           |

For more information about minimessage visit: https://docs.advntr.dev/minimessage/format.html

## Chat Message Deletion

With the `delete-tag-format` option you can control the look and feel of the component

For security reasons, the entire delete tag will only be visible to players with the
permission `tweaks.chat.delete`<br/>
To allow the deletion of the users own messages, grant: `tweaks.chat.delete.own`

### Hierarchy

_(This feature is based on LuckPerms' weight system, therefore, requires LuckPerms)_

The chat hierarchy makes it possible to control who can delete whose messages.<br/>
A user's weight acts as an upper limit for their deletion privileges.

For example, a user with a weight of 100 can delete messages from users with weights equal to or lower than 100,<br/>
but not from those with weights higher than 100.

To add a hierarchy permission, use: `tweaks.chat.delete.<weight>`

## Inventory

The `update-time` entry, is given in ticks _(20 ticks = 1 second)_.<br/>
The lower the value the faster the `/invsee` inventory updates.<br/>
The minimum value is 1 tick to avoid lag.

## Vanilla Tweaks

| Option                     | Description                                                                                                      | Value           |
|----------------------------|------------------------------------------------------------------------------------------------------------------|-----------------|
| cow-milking-cooldown       | the cooldown until a mooshroom/cow give milk again                                                               | milliseconds    |
| mushroom-stew-cooldown     | the cooldown until a mooshroom can give mushroom stew again                                                      | milliseconds    |
| sheep-wool-growth-cooldown | the minimum cooldown until sheep can grow back their wool<br/>(values below 2 minutes are not really noticeable) | milliseconds    |
| animal-heal-by-feeding     | whether animals should heal by feeding them<br/>_this implies animals can only breed when on full hearts_        | `true`, `false` |

For more information about sheep wool growth visit: https://minecraft.fandom.com/wiki/Tutorials/Wool_farming
