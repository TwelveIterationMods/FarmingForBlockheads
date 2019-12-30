# Farming for Blockheads
Minecraft Mod. Adds farming utilities, such as a seed market, fertilizer and feeding troughs.

[![Versions](http://cf.way2muchnoise.eu/versions/farming-for-blockheads.svg)](https://minecraft.curseforge.com/projects/farming-for-blockheads) [![Downloads](http://cf.way2muchnoise.eu/full_farming-for-blockheads_downloads.svg)](https://minecraft.curseforge.com/projects/farming-for-blockheads)

## Custom Market Entries

Data packs are now used to configure the market. You can place JSON files inside a `farmingforblockheads_compat` folder (so the resulting path would be `data/<domain>/farmingforblockheads_data/{files}.json`) within a datapack which will be loaded. For simple scenarios, you can also use the `MarketRegistry.json` file under `config/farmingforblockheads/MarketRegistry.json`.

See here for an example: https://gist.github.com/blay09/93a48d092d6dc570a64c81e1912dc4d0

Group overrides can be used to enable or disable inbuilt extensions.

Current inbuilt group names are (bold: enabled by default):

* Vanilla
  * **Vanilla Seeds**
  * **Vanilla Saplings**
  * **Bone Meal**
  * Vanilla Flowers
  * Vanilla Mushrooms
  * Animal Eggs
* Biomes O' Plenty
  * **BiomesOPlenty Saplings**
  * BiomesOPlenty Flowers
* Simple Farming
  * **SimpleFarming Saplings**
  * **SimpleFarming Seeds**

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
