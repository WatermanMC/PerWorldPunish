# PerWorldPunish

**PerWorldPunish** is a lightweight per-world punishment plugin designed for PaperMC 1.21+ servers. It provides server admins with the fundamental ability to ban players only in specified worlds without the unnecessary clutter or overhead of larger plugins.

---

## Key Features & Why You Should Download It

* **Essential Per-world Punishments:** Easily ban/kick/tempban your players per-world! Perfect for server admins who don't want to ban their players on whole server.
* **Minimal Overhead:** Built to be lightweight and efficient, ensuring minimal impact on your server's performance. Perfect for small to medium-sized survival, semi-vanilla, or even large public servers that just need per-world punishments!
* **Customize Plugin Messages:** Customize all PerWorldPunish messages completely on `messages.yml`! Uses MiniMessage so you can have gradients, hex, clickable text and more!
* **Intuitive Commands:** Everything the server admin needs is covered by small, easy to remember commands set.
* **Modern Server Support:** Built specifically for the latest **PaperMC 1.21+** versions and its forks, guaranteeing up-to-date performance and stability.
* **Open Source:** Developed under the **GNU General Public License v3.0**, allowing for transparency and community contributions.

---

## Commands & Usage
**(<> is required, [] is optional):**
*NOTE: It shows command usage if you typed the command without any arguments.*

| Command                                           | Description                          | Permission                    |
|:--------------------------------------------------|:-------------------------------------|:------------------------------|
| `/perworldpunish [info/reload]`                   | PerWorldPunish admin command.        | `perworldpunish.admin`        |
| `/worldban <player> [reason]`                     | Ban a player in a world.             | `perworldpunish.worldban`     |
| `/worldtempban <player> <timeInMinutes> [reason]` | Temporarily ban a player in a world. | `perworldpunish.worldtempban` |
| `/worldkick <player> [reason]`                    | Kick a player in a world.            | `perworldpunish.worldkick`    |
| `/worldunban <player>`                            | Unban a player in a world.           | `perworldpunish.worldunban`   |
| `/worldbanlist`                                   | Ban list of players.                 | `perworldpunish.worldbanlist` |

---

## Default Configs
Default configs used on the plugin.

<details>

<summary>config.yml</summary>

```yaml
# PerWorldPunish - Allows per-world punishments such as ban, kick and tempban!
# Support: https://discord.gg/Scgqfm5EU4
# GitHub Source Code: https://github.com/WatermanMC/PerWorldPunish

# This will show if there is no reason provided for ban/kick/tempban
# Only supports MiniMessage!
# MiniMessage formatter: https://webui.advntr.dev/
default-reason: "<red>No reason provided."

# The player will get teleported in this world after getting banned/kicked
# If not set or world is invalid, the plugin will choose your main world, where your datapacks etc. is stored
fallback-world: 'world'
```
</details>

<details>

<summary>messages.yml</summary>

```yaml
# All messages used in plugin
# Only supports MiniMessage!
# MiniMessage formatter: https://webui.advntr.dev/

nopermission: "<red>You don't have permission to do that!"
pluginReloaded: "<green>Plugin reloaded!"
playerPunishImmune: "<red>You can't punish <reset>{player}"
pluginReloadFail: "<red>Plugin reload failed. Check console for errors."
invalidPlayer: "<red>Player not found."
invalidWorld: "<red>World not found."
invalidTimeFormat: "<red>Invalid time format."
timeNotPositive: "<red>Time must be positive."
playerNotBanned: "<red>That player is not banned in <gray>{world}<red>."
banListPrefix: "<gray>Banned players:"
banListFormat: "<reset>{player} <gray>is banned on <yellow>{world}"
banListFormat-tempban: "<reset>{player} <gray>is banned on <yellow>{world} <gray>for <gray>{time} <gray>minutes"
noBannedPlayers: "<gray>There's no banned players."
banSuccess: "<green>Successfully banned <reset>{player} <green>on <yellow>{world}<gray>. Reason <reset>: {reason}"
tempBanSuccess: "<green>Successfully banned <reset>{player} <green>on <yellow>{world} <green>for <yellow>{time} <green>minutes<gray>. Reason <reset>: {reason}"
kickSuccess: "<green>Successfully kicked <reset>{player} <green>from <yellow>{world}<gray>. Reason <reset>: {reason}"
unBanSuccess: "<green>Successfully unbanned <reset>{player} <green>from <yellow>{world}"
playerBanned: "<red>You are banned on <yellow>{world}<red>! <gray>Reason: <reset>{reason}"
playerTempBanned: "<red>You are temporarily banned on <yellow>{world}<red> for <yellow>{time} <red>minutes! <gray>Reason: <reset>{reason}"
playerKicked: "<gray>You got kicked from <yellow>{world}<gray>. Reason: <reset>{reason}"
playerNotInWorld: "<gray>That player is not in <yellow>{world}<gray>."
```
</details>

---

## Links & Additional Information

**Author:** WatermanMC

* **GitHub Repository** (Source Code & Full Documentation): [https://github.com/WatermanMC/PerWorldPunish](https://github.com/WatermanMC/PerWorldPunish)
* **Documentation:** [https://github.com/WatermanMC/PerWorldPunish/wiki](https://github.com/WatermanMC/PerWorldPunish/wiki)
* **Discord Support:** [https://discord.gg/Scgqfm5EU4](https://discord.gg/Scgqfm5EU4)

---

This plugin is also owned by **VOXELWARE STUDIOS**.

---
