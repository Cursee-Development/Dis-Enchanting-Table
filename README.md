# Dis-Enchanting Table

**Dis-Enchanting Table** is a utility mod that introduces a new workstation designed to remove enchantments from items, giving players more control over their gear! Whether you're trying to salvage enchantments or simply want to reset your gear's anvil repair cost, this table has you covered.

## Features

- **Remove Enchantments**: Strip all enchantments from tools, weapons, and armor—or remove a single enchantment from an enchanted book. The item remains intact. _(core feature)_
- **Reset Repair Cost**: Clears an item's anvil repair cost, making it cheaper to modify again. _(enabled by default)_
- **Hopper-Compatible**: Supports full automation via hoppers—input from any side, output below. _(disabled by default — `enable_automatic_disenchanting`)_
- **Automatic Disenchanting**: Runs automatically if a player with enough experience is nearby. _(disabled by default — `enable_automatic_disenchanting`)_
- **Multi-Loader Support**: One unified `.jar` works on **Fabric**, **Forge**, and **NeoForge**—no need to download separate versions. _(core feature)_
- **Depends on MonoLib**: Built on top of [MonoLib](https://www.curseforge.com/minecraft/mc-mods/monolib), a shared library for cross-platform mod development. _(core feature)_

---

## Usage

### Servers & Modpacks

Configuration files are located in your instance's `config` folder:

- `disenchanting_table-client.toml`
- `disenchanting_table-server.toml`

**Client Config Options:**
- Disable the “Insufficient Experience” message.
- Toggle particles spawned by the Disenchanting Table.

**Server Config Options:**
- Adjust disenchanting cost (points or levels).
- Enable/disable repair cost resetting.
- Toggle automatic disenchanting support.

### Players

The Dis-Enchanting Table works in **manual** or **automatic** mode depending on the config:

- **Manual Mode** (default): Works like an anvil. Insert item → Take result manually.
- **Automatic Mode**: Behaves like a specialized hopper.
  - Accepts items from any side (except below).
  - Outputs disenchanted items only below.
  - Requires a nearby player with enough XP.

---

## Client Configuration Options

- `render_ender_particles` (`boolean: true`) — Should particles spawn around the table?
- `experience_indicator` (`boolean: true`) — Show “Insufficient Experience” if the player can't afford the cost?
- `render_table_item` (`boolean: true`) — Show the current item being disenchanted? (Defaults to an enchanted book in manual mode)

## Server Configuration Options

- `automatic_disenchanting` (`boolean: false`) — Enable hopper-based automatic disenchanting?
- `resets_repair_cost` (`boolean: true`) — Should repair cost be reset when disenchanting?
- `requires_experience` (`boolean: true`) — Should disenchanting consume player experience?
- `uses_points` (`boolean: true`) — Use XP points instead of levels for cost calculations?
- `experience_cost` (`int: 25`) — How much experience is required to disenchant?
