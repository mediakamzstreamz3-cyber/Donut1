# DonutShop

DonutSMP-style shop for Paper 1.21.11 (Java 21).

## Build
    mvn package
The jar is `target/DonutShop.jar`. Put it in `plugins/`. You also need **Vault** and an economy plugin (e.g. EssentialsX) for the money shops.

## Commands
- `/shop` - main menu, `/shop <menu>` - open one directly, `/shop reload`
- `/shardshop` - shard shop
- `/shards` - your own balance (everyone)
- `/shards <player>`, `/shards give|take|set <player> <amount>`, `/shop reload` - operators only (donutshop.admin)

## Shop controls
Left click = buy, Shift+Left = buy a stack of 64, Right click = sell, Shift+Right = sell all. Item tooltips show just the name and green price.

## Editing
Everything lives in `plugins/DonutShop/menus/*.yml` (one file per menu, file name = menu id). Prices are in each file. Run `/shop reload` after editing.
Item format: `- {material: STONE, amount: 1, buy: 10, sell: 2}`
Enchants: `enchants: {sharpness: 5, mending: 1}`. Potions: `potion: LEAPING`.
`commands: ["..."]` runs console commands on purchase instead of giving an item (`%player%` = buyer). Use this for SmartSpawners and crate keys in the shard shop.

## Shards and placeholders
Shards are a built-in currency stored in `shards.yml`. There is no built-in scoreboard. Install **PlaceholderAPI** and use these in your own scoreboard/tab plugin:
- `%donutshop_shards%` - raw number (12500)
- `%donutshop_shards_commas%` - 12,500
- `%donutshop_shards_formatted%` - 12.5K

## GitHub
Pushing to GitHub builds the jar automatically (`.github/workflows/build.yml`). Open the repo's **Actions** tab, click the latest run, and download the **DonutShop** artifact at the bottom. Unzip it to get `DonutShop.jar`.
