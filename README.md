# Tweaks
A must-have command collection for your minecraft server

## Environmental commands

- [x] /day (world) - *set the time to day*
- [x] /night (world) - *set the time to night*
- [x] /rain (world) - *let it rain*
- [x] /sun (world) - *let the sun shine*
- [x] /thunder (world) - *let it thunder*

## Item commands

- [x] /enchant [enchantment] (level) - *enchant your tools*
- [x] /unenchant [enchantment...] - *unenchant your tools*
- [x] /item [item] (amount) - *gives you an item of your choice*
- [x] /lore [set|append] [lore...] - *change the lore of your items*
- [x] /rename [name...] - *change the name of your items*
- [x] /repair (all) - *repair your tools*

## Player commands

- [x] /back - *go back to your last position*
- [x] /heal (player) - *heal yourself or someone else*
- [x] /feed (player) - *satisfy your own or someone else's hunger*
- [x] /gamemode [gamemode] (player) - *change your own or someone else's gamemode*
- [x] /fly (player) - *toggle your own or someone else's fly state*
- [x] /speed [speed] (player) - *change your own or someone else's walk or fly speed*
- [x] /ping (player) - *see your own or someone else's latency*
- [x] /seen [player] - *gives you information about a player*
- [x] /hat - *equip your items as hats*
- [x] /enderchest (player) - *open your own or someone else's enderchest*
- [x] /inventory (player) - *open your own or someone else's inventory*
## Server commands

- [x] /broadcast [message] - *broadcast a message*

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

| Command   | Permission               |
|-----------|--------------------------|
| enchant   | tweaks.command.enchant   |
| unenchant | tweaks.command.unenchant |
| item      | tweaks.command.item      |
| lore      | tweaks.command.lore      |
| rename    | tweaks.command.rename    |
| repair    | tweaks.command.repair    |

### Player commands

| Command    | Alias | Permission                                                                                                                                                                                                                                                                                                                    | Argument | Permission |
|------------|-------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------|------------|
| back       |       | tweaks.command.back                                                                                                                                                                                                                                                                                                           |          |            |
| heal       |       | tweaks.command.heal                                                                                                                                                                                                                                                                                                           | player   | *.others   |
| feed       |       | tweaks.command.feed                                                                                                                                                                                                                                                                                                           | player   | *.others   |
| gamemode   | gm    | tweaks.command.gamemode<br/>permits to use the command /gamemode<ul><li>tweaks.command.gamemode.survival<li>tweaks.command.gamemode.creative</li><li>tweaks.command.gamemode.adventure<li>tweaks.command.gamemode.spectator</ul> or you can use <ul><li>tweaks.command.gamemode.all</ul>to add permissions for all game modes | player   | *.others   |
| fly        |       | tweaks.command.fly                                                                                                                                                                                                                                                                                                            | player   | *.others   |
| speed      |       | tweaks.command.speed                                                                                                                                                                                                                                                                                                          | player   | *.others   |
| ping       |       | tweaks.command.ping                                                                                                                                                                                                                                                                                                           | player   | *.others   |
| seen       | find  | tweaks.command.seen                                                                                                                                                                                                                                                                                                           |          |            |
| hat        |       | tweaks.command.hat                                                                                                                                                                                                                                                                                                            |          |            |
| enderchest | ec    | tweaks.command.enderchest                                                                                                                                                                                                                                                                                                     | player   | *.others   |
| inventory  | inv   | tweaks.command.inventory                                                                                                                                                                                                                                                                                                      |          |            |
