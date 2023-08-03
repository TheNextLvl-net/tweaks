# Tweaks

A lightweight alternative to EssentialsX without the hustle of saving user specific data

## Features _(Configurable)_

- You heal animals by feeding them
- Unless an animal is at full health they can't breed
- Cooldown for milking cows/mushroom-cows (with bowls)
- Cooldown for Sheep wool growth

## Environmental commands

- /day (world) - *set the time to day*
- /night (world) - *set the time to night*
- /rain (world) - *let it rain*
- /sun (world) - *let the sun shine*
- /thunder (world) - *let it thunder*

## Item commands

- /enchant [enchantment] (level) - *enchant your tools*
- /unenchant [enchantment...] - *unenchant your tools*
- /item [item] (amount) - *gives you an item of your choice*
- /head [value|player|url] (value) - *get heads or information about them*
- /lore [set|append] [lore...] - *change the lore of your items*
- /rename [name...] - *change the name of your items*
- /repair (all) - *repair your tools*

## Player commands

- /back - *go back to your last position*
- /heal (player) - *heal yourself or someone else*
- /feed (player) - *satisfy your own or someone else's hunger*
- /gamemode [gamemode] (player) - *change your own or someone else's gamemode*
- /fly (player) - *toggle your own or someone else's fly state*
- /speed [speed] (player) - *change your own or someone else's walk or fly speed*
- /ping (player) - *see your own or someone else's latency*
- /seen [player] - *gives you information about a player*
- /hat - *equip your items as hats*
- /enderchest (player) - *open your own or someone else's enderchest*
- /inventory (player) - *open your own or someone else's inventory*

## Server commands

- /broadcast [message] - *broadcast a message*

## Workstation commands

- /anvil
- /cartography-table
- /enchanting-table
- /grindstone
- /loom
- /smithing-table
- /stonecutter
- /workbench

## Command permissions

### Environmental commands

| Command | Permission             |
|---------|------------------------|
| day     | tweaks.command.day     |
| night   | tweaks.command.night   |
| rain    | tweaks.command.rain    |
| sun     | tweaks.command.sun     |
| thunder | tweaks.command.thunder |

### Item commands

| Command   | Alias | Permission               |
|-----------|-------|--------------------------|
| enchant   |       | tweaks.command.enchant   |
| head      | skull | tweaks.command.head      |
| item      | i     | tweaks.command.item      |
| lore      |       | tweaks.command.lore      |
| rename    |       | tweaks.command.rename    |
| repair    |       | tweaks.command.repair    |
| unenchant |       | tweaks.command.unenchant |

### Player commands

| Command    | Alias          | Permission                                                                                                                                                                                                                                                                                         | Argument | Permission |
|------------|----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------|------------|
| back       |                | tweaks.command.back                                                                                                                                                                                                                                                                                |          |            |
| enderchest | ec             | tweaks.command.enderchest<br/>permits to use the command /enderchest<br/>to allow changes inside of an enderchest, grant<ul><li>tweaks.command.enderchest.edit</ul>                                                                                                                                | player   | *.others   |
| feed       |                | tweaks.command.feed                                                                                                                                                                                                                                                                                | player   | *.others   |
| fly        | flight         | tweaks.command.fly                                                                                                                                                                                                                                                                                 | player   | *.others   |
| gamemode   | gm             | tweaks.command.gamemode<br/>permits to use the command /gamemode<ul><li>tweaks.command.gamemode.survival<li>tweaks.command.gamemode.creative<li>tweaks.command.gamemode.adventure<li>tweaks.command.gamemode.spectator</ul>to allow every game mode, grant<ul><li>tweaks.command.gamemode.all</ul> | player   | *.others   |
| god        |                | tweaks.command.god                                                                                                                                                                                                                                                                                 | player   | *.others   |
| hat        |                | tweaks.command.hat                                                                                                                                                                                                                                                                                 |          |            |
| heal       |                | tweaks.command.heal                                                                                                                                                                                                                                                                                | player   | *.others   |
| inventory  | inv<br/>invsee | tweaks.command.inventory<br/>permits to use the command /inventory<br/>to allow changes inside of an inventory, grant<ul><li>tweaks.command.inventory.edit</ul>                                                                                                                                    |          |            |
| ping       | latency        | tweaks.command.ping                                                                                                                                                                                                                                                                                | player   | *.others   |
| seen       | find           | tweaks.command.seen                                                                                                                                                                                                                                                                                |          |            |
| speed      |                | tweaks.command.speed                                                                                                                                                                                                                                                                               | player   | *.others   |

### Workstation commands

| Command           | Alias       | Permission                       |
|-------------------|-------------|----------------------------------|
| anvil             |             | tweaks.command.anvil             |
| cartography-table | cartography | tweaks.command.cartography-table |
| enchanting-table  | enchanting  | tweaks.command.enchanting-table  |
| grindstone        |             | tweaks.command.grindstone        |
| loom              |             | tweaks.command.loom              |
| smithing-table    | smithing    | tweaks.command.smithing-table    |
| stonecutter       |             | tweaks.command.stonecutter       |
| workbench         | wb          | tweaks.command.workbench         |
