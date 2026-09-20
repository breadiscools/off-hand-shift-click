# Offhand Shift-Click (Fabric, Minecraft 26.2)

Shift-click an item in any inventory/container to put it in your off hand.
Open Mod Menu -> Offhand Shift-Click -> configure.

## Config
- Enabled: master switch
- List mode:
  - Everything except deny list (default)
  - Only allow list
- Entries (one per line in the list editor):
  - `minecraft:diamond`      a single item
  - `#minecraft:swords`     an item tag
  - `create:*`              every item from one mod

## Play requirements
Java 25, Fabric Loader 0.19.3+, Fabric API, Cloth Config API, Mod Menu.

## Build
1. Install JDK 25.
2. Generate a Gradle wrapper (needs Gradle installed):  gradle wrapper --gradle-version 9.7.0
   (or copy the wrapper files from a template made at https://fabricmc.net/develop)
3. ./gradlew build   ->  build/libs/offhand-shift-click-1.0.0.jar
4. Put the jar in your mods folder.

## 26.x notes
Minecraft 26.1+ is unobfuscated, so this uses the net.fabricmc.fabric-loom plugin, Mojang names,
`implementation` instead of `modImplementation`, and no `mappings` line.
Slot clicks go through MultiPlayerGameMode#handleContainerInput / ContainerInput (formerly ClickType).

## Get the jar without installing anything (GitHub Actions)
1. Create a new GitHub repo and upload everything in this folder (keep the .github folder).
2. Open the repo's Actions tab -> "Build jar" -> the finished run -> download the
   "offhand-shift-click-jar" artifact. Unzip it once to get the .jar.
