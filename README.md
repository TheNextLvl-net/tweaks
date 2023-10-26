# Tweaks

A lightweight alternative to EssentialsX without the hustle of saving user specific data<br/>
bStat Metrics can be found [here](https://bstats.org/plugin/bukkit/TheNextLvl%20Tweaks/19651)

## Features _(Configurable)_

- You heal animals by feeding them
- Unless an animal is at full health they can't breed
- Cooldown for milking cows/mushroom-cows (with bowls)
- Cooldown for Sheep wool growth
- You can define a default permission level (purely client-side)

## Commands

## Environmental commands

| Command          | Description           | Permission             |
|------------------|-----------------------|------------------------|
| /day (world)     | set the time to day   | tweaks.command.day     |
| /night (world)   | set the time to night | tweaks.command.night   |
| /rain (world)    | let it rain           | tweaks.command.rain    |
| /sun (world)     | let the sun shine     | tweaks.command.sun     |
| /thunder (world) | let it thunder        | tweaks.command.thunder |

The perm-pack to grant all permissions: `tweaks.commands.environmental`

### Item commands

| Command                            | Description                         | Alias | Permission               |
|------------------------------------|-------------------------------------|-------|--------------------------|
| /enchant [enchantment] (level)     | enchant your tools                  |       | tweaks.command.enchant   |
| /head [value/player/url] (value)   | get heads or information about them | skull | tweaks.command.head      |
| /item [item] (amount)              | gives you an item of your choice    | i     | tweaks.command.item      |
| /lore [set/append/unset] (lore...) | change the lore of your items       |       | tweaks.command.lore      |
| /rename [name...]                  | change the name of your items       |       | tweaks.command.rename    |
| /repair (all)                      | repair your tools                   |       | tweaks.command.repair    |
| /unenchant [enchantment...]        | unenchant your tools                |       | tweaks.command.unenchant |

The perm-pack to grant all permissions: `tweaks.commands.item`

### Player commands

| Command                       | Description                                         | Alias          | Permission                                                                                                                                                                                                                                                                                         | Argument | Permission |
|-------------------------------|-----------------------------------------------------|----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------|------------|
| /back                         | go back to your last position                       |                | tweaks.command.back                                                                                                                                                                                                                                                                                |          |            |
| /enderchest (player)          | open your own or someone else's enderchest          | ec             | tweaks.command.enderchest<br/>permits to use the command /enderchest<br/>to allow changes inside of an enderchest, grant<ul><li>tweaks.command.enderchest.edit</ul>                                                                                                                                | player   | *.others   |
| /feed (player)                | satisfy your own or someone else's hunger           |                | tweaks.command.feed                                                                                                                                                                                                                                                                                | player   | *.others   |
| /fly (player)                 | toggle your own or someone else's fly state         | flight         | tweaks.command.fly                                                                                                                                                                                                                                                                                 | player   | *.others   |
| /gamemode [gamemode] (player) | change your own or someone else's gamemode          | gm             | tweaks.command.gamemode<br/>permits to use the command /gamemode<ul><li>tweaks.command.gamemode.survival<li>tweaks.command.gamemode.creative<li>tweaks.command.gamemode.adventure<li>tweaks.command.gamemode.spectator</ul>to allow every game mode, grant<ul><li>tweaks.command.gamemode.all</ul> | player   | *.others   |
| /god (player)                 | make you or someone else invulnerable               |                | tweaks.command.god                                                                                                                                                                                                                                                                                 | player   | *.others   |
| /hat                          | equip your item as a hat                            |                | tweaks.command.hat                                                                                                                                                                                                                                                                                 |          |            |
| /heal (player)                | heal yourself or someone else                       |                | tweaks.command.heal                                                                                                                                                                                                                                                                                | player   | *.others   |
| /inventory (player)           | open your own or someone else's inventory           | inv<br/>invsee | tweaks.command.inventory<br/>permits to use the command /inventory<br/>to allow changes inside of an inventory, grant<ul><li>tweaks.command.inventory.edit</ul>                                                                                                                                    |          |            |
| /ping (player)                | see your own or someone else's latency              | latency        | tweaks.command.ping                                                                                                                                                                                                                                                                                | player   | *.others   |
| /seen [player]                | gives you information about a player                | find           | tweaks.command.seen                                                                                                                                                                                                                                                                                |          |            |
| /speed [speed] (player)       | change your own or someone else's walk or fly speed |                | tweaks.command.speed                                                                                                                                                                                                                                                                               | player   | *.others   |
| /tpo [player] (player)        | teleport offline-players to others or you to them   |                | tweaks.command.offline-tp                                                                                                                                                                                                                                                                          |          |            |

The perm-pack to grant all permissions: `tweaks.commands.player`

### Server commands

| Command              | Description          | Permission               |
|----------------------|----------------------|--------------------------|
| /broadcast [message] | broadcast a message  | tweaks.command.broadcast |
| /lobby               | connect to the lobby |                          |

The perm-pack to grant all permissions: `tweaks.commands.server`

### Workstation commands

| Command            | Alias       | Permission                       |
|--------------------|-------------|----------------------------------|
| /anvil             |             | tweaks.command.anvil             |
| /cartography-table | cartography | tweaks.command.cartography-table |
| /enchanting-table  | enchanting  | tweaks.command.enchanting-table  |
| /grindstone        |             | tweaks.command.grindstone        |
| /loom              |             | tweaks.command.loom              |
| /smithing-table    | smithing    | tweaks.command.smithing-table    |
| /stonecutter       |             | tweaks.command.stonecutter       |
| /workbench         | wb          | tweaks.command.workbench         |

The perm-pack to grant all permissions: `tweaks.commands.workstation`

## Configuration

### General

| Option                   | Description                                                                                                                                                                    | Value           |
|--------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------|
| back-buffer-stack-size   | the amount of locations stored for the /back command<br/>_the larger the value the more ram will be reserved_                                                                  | `Integer` (1-n) |
| default-permission-level | the permission level determines which commands a player can access<br/>_this option is purely-client side therefore has no effect on the server_<br/>(-1 disables this option) | `byte` (-1/0-4) |
| override-join-message    | whether to override the join message<br/>_the message can be changed within the local files_                                                                                   | `true`, `false` |
| override-quit-message    | whether to override the quit message<br/>_the message can be changed within the local files_                                                                                   | `true`, `false` |
| override-chat            | whether to override the chat<br/>_the chat format can be changed within the [`chat-format`](#minimessage-chat-tags) entry_                                                     | `true`, `false` |
| log-chat                 | whether to send the chat to the console<br/>_not related with override-chat_                                                                                                   | `true`, `false` |

For more information about the permission level visit: https://minecraft.fandom.com/wiki/Permission_level

### Server

| Option               | Description                                 | Value           |
|----------------------|---------------------------------------------|-----------------|
| enable-lobby-command | whether the lobby command should be enabled | `true`, `false` |
| lobby-server-name    | the name of the lobby server                | String          |

### MiniMessage Chat Tags

These tags can be used within the `chat-format` entry<br/>
To insert a tag just use `<tag>`

| Tag             | Description                                                                                | Requires  |
|-----------------|--------------------------------------------------------------------------------------------|-----------|
| prefix          | the prefix of the sender                                                                   | LuckPerms |
| suffix          | the suffix of the sender                                                                   | LuckPerms |
| group           | the group of the sender                                                                    | LuckPerms |
| signature       | the message signature                                                                      |           |
| delete          | a clickable component to delete a certain message<br/>(requires the signature as argument) |           |
| display_name    | the display name of the sender                                                             |           |
| message_content | the text content of the sent message<br/>_usable within click actions_                     |           |
| message         | the original message sent                                                                  |           |
| player          | the name of the sender                                                                     |           |
| world           | the world of the sender                                                                    |           |

For more information about minimessage visit: https://docs.advntr.dev/minimessage/format.html

### Chat Message Deletion

With the `delete-tag-format` option you can control the look and feel of the component

For security reasons, the entire delete tag will only be visible to players with the
permission `tweaks.chat.delete`<br/>
To allow the deletion of the users own messages, grant: `tweaks.chat.delete.own`

#### Hierarchy

_(This feature is based on LuckPerms' weight system, therefore requires LuckPerms)_

The chat hierarchy makes it possible to control who can delete who's messages.<br/>
A users weight acts as an upper limits for their deletion privileges.

For example, a user with a weight of 100 can delete messages from users with weights equal to or lower than 100,<br/>
but not from those with weights higher than 100.

To add a hierarchy permission use: `tweaks.chat.delete.<weight>`

### Inventory

The `update-time` entry, is given in ticks _(20 ticks = 1 second)_.<br/>
The lower the value the faster the `/invsee` inventory updates.<br/>
The minimum value is 1 tick to avoid lag.

### Vanilla Tweaks

| Option                     | Description                                                                                                      | Value           |
|----------------------------|------------------------------------------------------------------------------------------------------------------|-----------------|
| cow-milking-cooldown       | the cooldown until a (mooshroom) cow give milk again                                                             | milliseconds    |
| mushroom-stew-cooldown     | the cooldown until a mooshroom can give mushroom stew again                                                      | milliseconds    |
| sheep-wool-growth-cooldown | the minimum cooldown until sheep can grow back their wool<br/>(values below 2 minutes are not really noticeable) | milliseconds    |
| animal-heal-by-feeding     | whether animals should heal by feeding them<br/>_this implies animals can only breed when on full hearts_        | `true`, `false` |

For more information about sheep wool growth visit: https://minecraft.fandom.com/wiki/Tutorials/Wool_farming