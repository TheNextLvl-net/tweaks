# Tweaks

A modern alternative to EssentialsX<br/>
bStat Metrics can be found [here](https://bstats.org/plugin/bukkit/TheNextLvl%20Tweaks/19651)

## Download

[Modrinth](https://modrinth.com/plugin/tweaks-1)<br>
[Hangar](https://hangar.papermc.io/TheNextLvl/Tweaks)

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

# Chat Tags

These tags can be used within the `chat-format` of your translation files. To insert a tag, use `<tag>`.

| Tag             | Description                                                                  |
|-----------------|------------------------------------------------------------------------------|
| delete          | A clickable component to delete the message.                                 |
| display_name    | The display name of the sender.                                              |
| message         | The original message sent by the player.                                     |
| message_content | The text content of the sent message. Usable within click and hover actions. |
| player          | The username of the sender.                                                  |
| world           | The world where the sender is located.                                       |

## Group and Display-Related Tags

> These tags require a [ServiceIO](https://github.com/TheNextLvl-net/service-io)-compatible group/chat plugin,
> such as [LuckPerms](https://github.com/LuckPerms/LuckPerms)

| Tag               | Description                                                          |
|-------------------|----------------------------------------------------------------------|
| chat_display_name | Displays the player's display name. (not the same as `display_name`) |
| chat_prefix       | The prefix of the sender.                                            |
| chat_suffix       | The suffix of the sender.                                            |
| player_group      | The group of the sender.                                             |
| group_prefix      | The prefix of the sender's group.                                    |
| group_suffix      | The suffix of the sender's group.                                    |

## Economy-Related Tags

> These tags require a [ServiceIO](https://github.com/TheNextLvl-net/service-io)-compatible economy plugin,
> such as [Economist](https://github.com/TheNextLvl-net/economist).

| Tag                      | Description                                                                                        |
|--------------------------|----------------------------------------------------------------------------------------------------|
| balance                  | Displays the player's current balance, formatted according to the server's economy settings.       |
| balance_unformatted      | Displays the player's balance without any formatting (raw number).                                 |
| bank_balance             | Displays the player's bank account balance (if applicable), formatted as per the economy settings. |
| bank_balance_unformatted | Displays the player's bank account balance without formatting (raw number).                        |
| currency_name            | Displays the singular form of the server's currency name (e.g., "Dollar", "Coin").                 |
| currency_name_plural     | Displays the plural form of the currency name (e.g., "Dollars", "Coins").                          |
| currency_symbol          | Displays the symbol of the currency (e.g., "$", "€").                                              |

For more information about MiniMessage, visit their [Documentation](https://docs.advntr.dev/minimessage/format.html)

# Chat Message Deletion

The `delete-tag-format` option allows you to customize the appearance of the message deletion component.
For security purposes, only users with the permission `tweaks.chat.delete` will be able to see the delete tag.
To allow users to delete their own messages, grant the permission: `tweaks.chat.delete.own`.

---

## Hierarchy

> This feature requires [ServiceIO](https://github.com/TheNextLvl-net/service-io)
> and a compatible permission plugin such as [LuckPerms](https://github.com/LuckPerms/LuckPerms).

The chat hierarchy system controls who can delete whose messages based on a user's weight.
A user's weight determines their deletion privileges.
Users can only delete messages from others with equal or lower weights.

### Example

- **Admin:** deletion weight 99
  Admins can delete messages from Admins, Moderators, and Players.

- **Moderator:** deletion weight 50
  Moderators can delete messages from Moderators and Players but not Admins.

- **Player:** deletion weight undefined
  Players can't delete messages from anyone.

## Setting Weights in LuckPerms:

To assign a hierarchy weight in LuckPerms, use the following permission:
`meta.chat-delete-weight.<weight>`

Alternatively, you can use the command:
`/lp user <player> meta set chat-delete-weight <weight>`

# Configuration

## General Configuration

| Option                     | Description                                            |
|----------------------------|--------------------------------------------------------|
| `message-deletion-timeout` | Time in milliseconds chat messages may be deleted for. |
| `back-buffer-stack-size`   | How many back-locations should be stored per user.     |
| `default-permission-level` | _Clientside_ permission level assigned to users. (1-4) |
| `enchantment-overflow`     | Enables or disables enchantment overflow.              |
| `override-join-message`    | Whether join messages should be overridden.            |
| `override-quit-message`    | Whether quit messages should be overridden.            |
| `override-chat`            | Whether tweaks should override the chat.               |
| `log-chat`                 | Whether the chat should be logged to console.          |
| `lobby-server-name`        | Name of the lobby server (for `/lobby`).               |
| `motd`                     | Message of the day for the server.                     |

## Features Configuration

| Option  | Description                                                            |
|---------|------------------------------------------------------------------------|
| `homes` | Enables or disables the homes feature.                                 |
| `msg`   | Enables or disables the private message feature.                       |
| `spawn` | Enables or disables the spawn feature.                                 |
| `tpa`   | Enables or disables the teleport request feature.                      |
| `warps` | Enables or disables the warps feature.                                 |
| `lobby` | Enables or disables the lobby feature. (Right mode should be detected) |

## Features Social Configuration

| Option              | Description                                          |
|---------------------|------------------------------------------------------|
| `add-link-commands` | Enable link commands like `/discord`, `/reddit` etc. |
| `add-server-links`  | Whether to add the links to the `Server Links` tab.  |
| `announcements`     | Enables the announcements feature.                   |
| `community`         | Enables the community feature.                       |
| `feedback`          | Enables the feedback feature.                        |
| `forum`             | Enables the forum feature.                           |
| `guidelines`        | Enables the guidelines feature.                      |
| `issues`            | Enables the issues feature.                          |
| `news`              | Enables the news feature.                            |
| `status`            | Enables the status feature.                          |
| `support`           | Enables the support feature.                         |
| `website`           | Enables the website feature.                         |
| `discord`           | Enables the Discord link command.                    |
| `reddit`            | Enables the Reddit link command.                     |
| `teamspeak`         | Enables the TeamSpeak link command.                  |
| `twitch`            | Enables the Twitch link command.                     |
| `x`                 | Enables the X (Twitter) link command.                |
| `youtube`           | Enables the YouTube link command.                    |

## Home Configuration

| Option         | Description                                                           |
|----------------|-----------------------------------------------------------------------|
| `limit`        | Maximum number of homes allowed (negative value indicates unlimited). |
| `unnamed-name` | Default name for unnamed homes.                                       |

## Spawn Configuration

| Option                    | Description                                              |
|---------------------------|----------------------------------------------------------|
| `teleport-on-first-join`  | Teleports player to spawn on first join.                 |
| `teleport-on-join`        | Teleports player to spawn on join.                       |
| `teleport-on-respawn`     | Teleports player to spawn on respawn.                    |
| `ignore-respawn-position` | Ignores the exact respawn position.                      |
| `location`                | Spawn location (in `world, x, y, z, yaw, pitch` format). |

## GUI Configuration

### Inventory GUI

| Option        | Description                                           |
|---------------|-------------------------------------------------------|
| `helmet`      | Material for the helmet placeholder slot in GUI.      |
| `chestplate`  | Material for the chestplate placeholder slot in GUI.  |
| `leggings`    | Material for the leggings placeholder slot in GUI.    |
| `boots`       | Material for the boots placeholder slot in GUI.       |
| `off-hand`    | Material for the off-hand placeholder slot in GUI.    |
| `cursor`      | Material for the cursor placeholder slot in GUI.      |
| `placeholder` | Material for placeholder slots in GUI.                |
| `update-time` | Update time for the inventory GUI in ticks. (1s = 20) |

### Homes GUI

| Option                 | Description                                                 |
|------------------------|-------------------------------------------------------------|
| `enabled`              | Enables or disables the homes GUI. (chat based if disabled) |
| `rows`                 | Number of rows in the homes GUI.                            |
| `action-slots`         | Slots in the homes GUI that may contain homes.              |
| `button-slot-next`     | Slot for the next button in the homes GUI.                  |
| `button-slot-previous` | Slot for the previous button in the homes GUI.              |

### Warps GUI

| Option                 | Description                                                |
|------------------------|------------------------------------------------------------|
| `enabled`              | Enables or disables the warps GUI. (chat base if disabled) |
| `rows`                 | Number of rows in the warps GUI.                           |
| `action-slots`         | Slots in the warps GUI that may contain warps.             |
| `button-slot-next`     | Slot for the next button in the warps GUI.                 |
| `button-slot-previous` | Slot for the previous button in the warps GUI.             |

### Name Icons

| Option       | Description                |
|--------------|----------------------------|
| `name-icons` | Icons for named locations. |

#### Example

```json
{
  "name-icons": {
    "End Ship": "minecraft:elytra",
    "Bastion": "minecraft:polished_blackstone_bricks",
    "Mine": "minecraft:diamond",
    "Casino": "minecraft:gold_ingot"
  }
}
```

## Teleport Configuration

| Option                    | Description                                             |
|---------------------------|---------------------------------------------------------|
| `cooldown`                | Cooldown time in milliseconds for teleportation.        |
| `cooldown-allow-movement` | Allows movement during teleport cooldown.               |
| `tpa-timeout`             | Timeout duration in milliseconds for teleport requests. |

## Animals Configuration

| Option                       | Description                                                               |
|------------------------------|---------------------------------------------------------------------------|
| `cow-milking-cooldown`       | Cooldown time in milliseconds for cow milking.                            |
| `mushroom-stew-cooldown`     | Cooldown time in milliseconds for getting mushroom stew from a mooshroom. |
| `sheep-wool-growth-cooldown` | Cooldown time in milliseconds before sheep can grow back their wool.      |
| `animal-heal-by-feeding`     | Enables or disables animal healing by feeding them.                       |

## Links Configuration

| Option          | Description                            |
|-----------------|----------------------------------------|
| `announcements` | URL for announcements.                 |
| `community`     | URL for community.                     |
| `feedback`      | URL for feedback.                      |
| `forum`         | URL for the forum.                     |
| `guidelines`    | URL for guidelines.                    |
| `issues`        | URL for issues tracking.               |
| `news`          | URL for the news page.                 |
| `status`        | URL for the status page.               |
| `support`       | URL for the support page.              |
| `website`       | URL for the main website.              |
| `discord`       | URL for the Discord server invitation. |
| `reddit`        | URL for the subreddit.                 |
| `teamspeak`     | URL for the TeamSpeak server.          |
| `twitch`        | URL for the Twitch channel.            |
| `x`             | URL for the X (Twitter) profile.       |
| `youtube`       | URL for the YouTube channel.           |

# Commands file

```json5
{
  "hello": {
    // the key can't be changed
    "command": "test",
    // can be named whatever you want
    "aliases": [
      "test-1",
      // can contain as many aliases you want
      ""
      // can even be empty to work on '/' 
    ],
  },
  "nope": {
    "command": "nope",
    "aliases": []
    // can be empty, to not register any aliases
  }
}
```