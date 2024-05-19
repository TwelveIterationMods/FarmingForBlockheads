Updated to Minecraft 1.20.6

- Added new ways of defining Market Presets, Market Categories and Market Items
    - See [here](https://mods.twelveiterations.com/mc/farming-for-blockheads/customization/) for documentation
      and [here](https://mods.twelveiterations.com/mc/farming-for-blockheads/migration-to-1204/) for an automatic
      migration tool.
- Payments can now be defined as tags, optionally with a custom label
- Markets are now two-blocks high (instead of a single block occupying the space of two), which should resolve lighting
  issues with shaders and puts it more in line with Minecraft in general
- Fixed JEI not populating until joining a singleplayer world
- Removed old ways of defining Market Groups, Market Categories and Market Items
- Removed IMC API
- Removed some API methods as market items are now defined using Vanilla's recipe system
- Updated to Minecraft 1.20.4