# Farming for Blockheads
Minecraft Mod. Adds farming utilities, such as a seed market, fertilizer and feeding troughs.

See [the license](LICENSE) for modpack permissions etc.

This mod is available for both Forge and Fabric (starting Minecraft 1.17).

#### Forge

[![Versions](http://cf.way2muchnoise.eu/versions/261924_latest.svg)](https://www.curseforge.com/minecraft/mc-mods/farming-for-blockheads)
[![Downloads](http://cf.way2muchnoise.eu/full_261924_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/farming-for-blockheads)

#### Fabric

[![Versions](http://cf.way2muchnoise.eu/versions/554586_latest.svg)](https://www.curseforge.com/minecraft/mc-mods/farming-for-blockheads-fabric)
[![Downloads](http://cf.way2muchnoise.eu/full_554586_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/farming-for-blockheads-fabric)

## Contributing

If you're interested in contributing to the mod, you can check
out [issues labelled as "help wanted"](https://github.com/ModdingForBlockheads/FarmingForBlockheads/issues?q=is%3Aopen+is%3Aissue+label%3A%22help+wanted%22)
. These should be ready to be implemented as they are.

If you need help, feel free to join us on [Discord](https://discord.gg/scGAfXC).

## Custom Market Entries

Data packs are now used to configure the market. You can place JSON files inside a `farmingforblockheads_compat` folder (so the resulting path would be `data/<domain>/farmingforblockheads_compat/{files}.json`) within a datapack which will be loaded. For simple scenarios, you can also use the `MarketRegistry.json` file under `config/farmingforblockheads/MarketRegistry.json`.

See here for an example: https://gist.github.com/blay09/93a48d092d6dc570a64c81e1912dc4d0

Group overrides can be used to enable or disable inbuilt extensions.

Enabled by Default:

* Vanilla
  * Vanilla Seeds
  * Vanilla Saplings
  * Bone Meal
* Biomes O' Plenty
  * BiomesOPlenty Saplings
* Create
  * Create Fertilizer
* Farmer's Delight
  * Farmers Delight Seeds
* Quark
  * Quark Saplings
* Simple Farming
  * SimpleFarming Saplings
  * SimpleFarming Seeds
* Supplementaries
  * Supplementaries Seeds
* Tropicraft
  * Tropicraft Fertilizer
  * Tropicraft Saplings

Optional Groups (many of these [provided by seanimusprime](https://github.com/ModdingForBlockheads/FarmingForBlockheads/issues/125)):

* Vanilla
  * Vanilla Flowers
  * Vanilla Mushrooms
  * Vanilla Crops
  * Vanilla Soil
  * Animal Eggs
* Biomes O' Plenty
  * BiomesOPlenty Flowers
  * BiomesOPlenty Mushroom
  * BiomesOPlenty Soil
* BetterEnd
  * BetterEnd Saplings
  * BetterEnd Seeds
  * BetterEnd Mushrooms 
* BetterNether
  * BetterNether Saplings
  * BetterNether Seeds
  * BetterNether Mushrooms
  * BetterNether Soil
* Buzzier Bees
  * Buzzier Bees Flowers
* Cinderscapes
  * Cinderscapes Berries
  * Cinderscapes Soil
* Endergetic Expansion
  * Endergetic Expansion Soil
* Farmer's Delight
  * Farmers Delight Mushroom Colonies
  * Farmers Delight Soil
  * Farmers Delight Wild Crops
* Farming for Blockheads
  * Farming for Blockheads Fertilizer
* Minecolonies
  * Minecolonies Soil
* Minecraft Earth Mod
  * Minecraft Earth Flowers
* Nether's Delight
  * Nethers Delight Compost
* The Outer End
  * Outer End Berries
  * Outer End Flowers
  * Outer End Soil
* Quark
  * Quark Mushroom
  * Quark Soil
* The Abyss: Chapter II
  * The Abyss Saplings
  * The Abyss Soil
* Tropicraft
  * Tropicraft Flowers
  * Tropicraft Crops
* Twilight Forest
  * Twilight Forest Berries
  * Twilight Forest Saplings
  * Twilight Forest Soil

## IMC API

The below is a list of IMC messages handled by Farming for Blockheads.

* **RegisterMarketCategory** (NBT)
  * RegistryName (String) - Resource location, must be prefixed with your mod id!
  * Tooltip (String) - Should be the language key for the tooltip
  * Texture (String) - Resource location to the texture sheet for your category icon
  * TextureX (Integer) - X coordinate of the icon on the texture sheet (icons must be 20x20)
  * TextureY (Integer) - Y coordinate of the icon on the texture sheet (icons must be 20x20)
* **RegisterMarketEntry** (NBT)
  * OutputItem (ItemStack)
  * CostItem (ItemStack)
  * Category (String) - Resource location, must be a registered category. Default categories are `farmingforblockheads:seeds`, `farmingforblockheads:saplings` and `farmingforblockheads:other`

## Java API

If the IMC API is not enough for you, you can build against Farming for Blockheads' Java API.
The Java API allows everything the IMC API does, and certain tasks can only be achieved via the Java API.
However, if you don't need that extra control, it is recommended to use the IMC API.

### Adding the dependency to your build.gradle
```
repositories {
    maven {
        url "https://minecraft.curseforge.com/api/maven/"
    }
}

dependencies {
    compile "farming-for-blockheads:FarmingForBlockheads_1.12.2:3.1.26"
}
```
The latest version number can be found on [CurseForge](https://minecraft.curseforge.com/projects/farming-for-blockheads/files).
