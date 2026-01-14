# PerWorldPunish

**PerWorldPunish** is a simple but powerful per-world punishment plugin designed for **PaperMC 1.21+** servers. It provides server admins with the fundamental ability to ban players per-world quick and easy without the unnecessary clutter or overhead of larger plugins.

---

## Links
* [Source Code](https://github.com/WatermanMC/PerWorldPunish)
* [Discord Support](https://discord.gg/Scgqfm5EU4)


## Key Features & Why You Should Download It

* **Essential Per-World Punishments**: Server admins can ban/tempban/kick players on specified worlds, perfect for server admins who want to punish players on a world but not on whole server.
* **Robust error logging**: Designed for server owners to understand errors on configs, missing data files and more!
* **Minimal Overhead:** Built to be lightweight and efficient, ensuring minimal impact on your server's performance. Perfect for small to medium-sized survival, semi-vanilla, or even large public servers that just need a per-world punishments.
* **Customizable Messages**: Customizable messages on `messages.yml` file, it also supports MiniMessage so you can use HEX, gradients and more!
* **Modern Server Support:** Built specifically for the latest **PaperMC 1.21+** versions and its forks, guaranteeing up-to-date performance and stability.
* **Open Source:** Developed under the **GNU General Public License v3.0**, allowing for transparency and community contributions.

---

## Commands & Usage
**(<> is optional, [] is required):**

| Command                                                   | Description | Permission |
|:----------------------------------------------------------| :--- | :--- |
| `/worldban <player> <world> [reason]`                     | Ban a player in a world. | `perworldpunish.worldban` |
| `/worldtempban <player> <world> <timeInMinutes> [reason]` | Temporarily ban a player in a world. | `perworldpunish.worldtempban` |
| `/worldkick <player> <world> [reason]`                    | Kick a player in a world. | `perworldpunish.worldkick` |
| `/worldbanlist`                                           | Ban list of players. | `perworldpunish.worldbanlist` |
| `/worldunban <player>`                                    | Unban a player in a world. | `perworldpunish.worldunban` |
| `/perworldpunish <info/reload/forcesavedata>`             | PerWorldPunish admin command | `perworldpunish.admin` |

---
## Default Config Files

<details>
<summary>config.yml</summary>

```yaml
# PerWorldPunish - Allows per-world punishments such as ban, kick and tempban!
# Support: https://discord.gg/Scgqfm5EU4
# GitHub Source Code: https://github.com/WatermanMC/PerWorldPunish

# This will show if there is no reason provided for ban/kick/tempban
default-reason: "<red>No reason provided."

# The player will get teleported in this world after getting banned/kicked
fallback-world: 'world'
```
</details>

<details>
<summary>messages.yml</summary>

```yaml
# All messages used in plugin
# Only supports MiniMesages
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

## Developer API
Integrate PerWorldPunish in your own projects!


### Add the dependency

MAVEN `pom.xml`:
```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>

	<dependency>
           <groupId>com.github.WatermanMC</groupId>
           <artifactId>PerWorldPunish</artifactId>
           <version>1.0.0</version>
           <classifier>api</classifier>
           <scope>provided</scope>
	</dependency>
```

GRADLE `build.gradle`:
```groovy
repositories {
    maven {
      url 'https://jitpack.io'
    }
}

dependencies {
    compileOnly 'com.github.WatermanMC:PerWorldPunish:1.0.0:api'
}
```

`settings.gradle`
**Only for Gradle 7.0+** Add this in your settings.gradle at the end of repositories:
```groovy
	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```

GRADLE `build.gradle.kts`:
```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.WatermanMC:PerWorldPunish:1.0.0:api")
}
```

`settings.gradle.kts`
**Only for Gradle 7.0+** Add it in your settings.gradle.kts at the end of repositories:
```kotlin
	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url = uri("https://jitpack.io") }
		}
	}
```

### Using the API
The whole API code can be seen [here](https://github.com/WatermanMC/PerWorldPunish/tree/main/src/main/java/com/github/WatermanMC/PerWorldPunish/api).

**Accessing the API**
Access the API through Services Manager.
```java
RegisteredServiceProvider<PerWorldPunishAPI> rsp = Bukkit.getServicesManager().getRegistration(PerWorldPunishAPI.class);
if (rsp != null) {
    PerWorldPunishAPI api = rsp.getProvider();
    // Use the API here
}
```
Or, use static getter (Not recommended)
```java
// Direct access via your main class
PerWorldPunishAPI api = PerWorldPunish.getApi();
```


**API Methods**
Once you have the api, you can use these methods:

* Ban: `api.banPlayer(uuid, worldName, reason);`
* Temporary ban: `api.tempBanPlayer(uuid, worldName, minutes, reason);`
* Kick: `api.kickPlayer(uuid, worldName, reason);`
* Check: `api.isBanned(uuid, worldName);`
* Unban: `api.unbanPlayer(uuid, worldName);`

**Custom Events**
Listen to these events in your own listener:

* `PerWorldPunishEvent`
* `PlayerWorldBanEvent`
* `PlayerWorldTempBanEvent`
* `PlayerWorldKickEvent`
* `PlayerWorldUnbanEvent`

---

This plugin is also owned by **VOXELWARE Studios**

---