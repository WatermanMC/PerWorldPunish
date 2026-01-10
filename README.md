# PerWorldPunish

**PerWorldPunish** is a simple but powerful per-world punishment plugin designed for **PaperMC 1.21+** servers. It provides server admins with the fundamental ability to ban players per-world quick and easy without the unneccessary clutter or overhead of larger plugins.

---

## Key Features & Why You Should Download It

* **Essential Per-World Punishments**: Server admins can ban/tempban/kick players on specified worlds, perfect for server admins who want to punish players on a world but not on whole server.
* **Robust error logging**: Designed for server owners to understand errors on configs, missing data files and more!
* **Minimal Overhead:** Built to be lightweight and efficient, ensuring minimal impact on your server's performance. Perfect for small to medium-sized survival, semi-vanilla, or even large public servers that just need a per-world punishments.
* **Customizable Messages**: Customizable messages on `messages.yml` file, it also supports MiniMessage so you can use HEX, gradients and more!
* * **Modern Server Support:** Built specifically for the latest **PaperMC 1.21+** versions and its forks, guaranteeing up-to-date performance and stability.
* **Open Source:** Developed under the **GNU General Public License v3.0**, allowing for transparency and community contributions.

---

## Commands & Usage
**(<> is optional, [] is required):**

| Command | Description | Permission |
| :--- | :--- | :--- |
| `/worldban <player> <world> [reason]` | Ban a player in a world. | `perworldpunish.worldban` |
| `/worldtempban <player> <world> <timeInMinutes> [reason]` | Temporarily ban a player in a world. | `perworldpunish.worldtempban` |
| `/worldkick <player> <world> [reason]` | Kick a player in a world. | `perworldpunish.worldkick` |
| `/worldbanlist` | Ban list of players. | `perworldpunish.worldbanlist` |
| `/worldunban <player>` | Unban a player in a world. | `perworldpunish.worldunban` |
| `/perworldpunish <info/reload>` | PerWorldPunish admin command | `perworldpunish.admin` |

---

## Developer API
Integrate PerWorldPunish in your own plugins!

**1. Add the dependency**

**MAVEN:**
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

**GRADLE GROOVY:**
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

**GRADLE KOTLIN:**
```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.WatermanMC:PerWorldPunish:1.0.0:api")
}
```

