# Tweaks

A must-have command collection for your minecraft server

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
| unenchant |       | tweaks.command.unenchant |
| item      | i     | tweaks.command.item      |
| head      | skull | tweaks.command.head      |
| lore      |       | tweaks.command.lore      |
| rename    |       | tweaks.command.rename    |
| repair    |       | tweaks.command.repair    |

### Player commands

| Command    | Alias   | Permission                                                                                                                                                                                                                                                                                                                    | Argument | Permission |
|------------|---------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------|------------|
| back       |         | tweaks.command.back                                                                                                                                                                                                                                                                                                           |          |            |
| heal       |         | tweaks.command.heal                                                                                                                                                                                                                                                                                                           | player   | *.others   |
| feed       |         | tweaks.command.feed                                                                                                                                                                                                                                                                                                           | player   | *.others   |
| gamemode   | gm      | tweaks.command.gamemode<br/>permits to use the command /gamemode<ul><li>tweaks.command.gamemode.survival<li>tweaks.command.gamemode.creative</li><li>tweaks.command.gamemode.adventure<li>tweaks.command.gamemode.spectator</ul> or you can use <ul><li>tweaks.command.gamemode.all</ul>to add permissions for all game modes | player   | *.others   |
| fly        | flight  | tweaks.command.fly                                                                                                                                                                                                                                                                                                            | player   | *.others   |
| speed      |         | tweaks.command.speed                                                                                                                                                                                                                                                                                                          | player   | *.others   |
| ping       | latency | tweaks.command.ping                                                                                                                                                                                                                                                                                                           | player   | *.others   |
| seen       | find    | tweaks.command.seen                                                                                                                                                                                                                                                                                                           |          |            |
| hat        |         | tweaks.command.hat                                                                                                                                                                                                                                                                                                            |          |            |
| enderchest | ec      | tweaks.command.enderchest                                                                                                                                                                                                                                                                                                     | player   | *.others   |
| inventory  | inv     | tweaks.command.inventory                                                                                                                                                                                                                                                                                                      |          |            |
