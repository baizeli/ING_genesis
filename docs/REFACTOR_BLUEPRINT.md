# EternisStarrySky Refactor Blueprint

This document defines the first refactor boundary for EternisStarrySky. It is intentionally a planning artifact: use it to decide what to keep, move, rewrite, or remove before changing large areas of code.

## Ground Rules

- Treat `ISS/` as read-only reference source for Iron's Spells 'n Spellbooks behavior.
- Treat `genesis_core` as an independent external mechanism. Do not include it in this refactor plan.
- Keep gameplay content in EternisStarrySky.
- Move reusable engine-like code to Genesis Lib.
- Keep client-only code out of common classes.
- Prefer one registration style per domain.
- Keep each migration buildable before moving to the next domain.
- Treat existing custom model JSON files under `src/main/resources` as authored assets. Do not delete or regenerate them unless a specific file is proven obsolete.

## Target Package Shape

```text
miku.united_as_one.genesis
  Genesis.java
  common/
    registry/
    network/
    config/
    datagen/
    compat/
  content/
    block/
    item/
    equipment/
    curio/
    effect/
    spell/
    entity/
    world/
    workbench/
  gameplay/
    combat/
    spell/
    equipment/
    curio/
    save/
  client/
    event/
    model/
    particle/
    render/
    screen/
  integration/
    irons_spellbooks/
    jei/
    curios/
  mixin/
```

Package intent:

- `common.registry`: only registration declarations and registration bootstrap.
- `content`: concrete mod objects, such as items, blocks, effects, entities, spells, menus, recipes.
- `gameplay`: event handlers and runtime systems that connect content to behavior.
- `client`: classes that may reference Minecraft client classes.
- `integration`: optional or external-mod glue.
- `mixin`: mixins grouped by target mod and feature.

## Keep In EternisStarrySky

These are mod-specific and should stay in the main project, though most need cleanup:

- `contents.spell`: actual Genesis spell definitions.
- `contents.items`: Genesis items, weapons, spellbooks, staffs, curios, materials.
- `contents.block`: Genesis blocks and workbench blocks.
- `contents.effect`: Genesis mob effects.
- `contents.entity`: Genesis mobs/projectiles/spell entities.
- `contents.workbench`: arcane workbench and cauldron gameplay.
- `registries.*`: registry declarations, after splitting large registries by domain.
- `handlers.*`: gameplay events, after grouping by feature and side.
- `compat.jei`: JEI integration.
- `data.datagen`: datagen providers, after separating generated vs hand-written resources.
- `data.save`: mod-specific persistent save data.
- `resourcepacks/genesis_old`: keep as optional legacy resource pack unless intentionally dropped.

## Move Or Keep In Genesis Lib

These are reusable or already partially migrated to Genesis Lib:

- shader helpers and shader registrations
- cosmic item/render helpers
- tooltip particle/render API
- trail/slash effect API
- generic particle render types
- damage policy API and damage service
- equipment stat config and manager
- Curios slot constants/helpers
- generic NBT/persistent data helpers
- math/text/color helpers
- core mixin accessors that support the public Genesis Lib API, excluding the independent `genesis_core` mechanism

Main-project imports should eventually point only to stable `miku.bai_ze_li.genesis.api.*` classes for these features.

## Remove Or Quarantine

These should not remain in production registration:

- `contents.entity.test`
- `client.renderer.entity.test`
- debug particles and particle debug events, after confirming their implementations now come from Genesis Lib/API
- registry IDs such as `test`, `testa`, and `testb`
- empty or obsolete networking channels
- `notuse` packages
- placeholder resource locations such as `<null>` and `nop`
- `System.out.println` and broad `printStackTrace` debug paths
- commented-out legacy blocks that no longer describe active behavior

If a debug tool is still valuable, move it behind a dev-only flag or a separate debug source set later.

Do not delete the BloodBoss AI memory/activity entries currently named like test entries. They are required gameplay state and should be renamed to production names during the AI rewrite instead of removed.

## Rewrite Design

This section is the concrete design for rewriting high-risk "mountain" code. The goal is not only to move files, but to replace unclear structures with smaller, named systems.

### Registration Design

Problem:

- Large registries mix categories, item construction, datagen, model rules, tooltip text, and external-mod glue.
- Static field order becomes a hidden dependency graph.
- Test entries are hard to distinguish from production entries.

Design:

- Keep one small bootstrap: `common.registry.GenesisRegistries`.
- Split registries by content category:
  - `GenesisMaterials`
  - `GenesisTools`
  - `GenesisWeapons`
  - `GenesisArmor`
  - `GenesisCurios`
  - `GenesisSpellItems`
  - `GenesisBlocks`
  - `GenesisWorkbenchBlocks`
  - `GenesisEntities`
  - `GenesisEffects`
  - `GenesisSpells`
  - `GenesisParticles`
- Put repetitive construction in small local factories only when it removes real duplication.
- Keep datagen helpers near the category they serve, but do not bury unrelated provider code in registry classes.

Target example:

```text
common/registry/
  GenesisRegistries.java
  GenesisItems.java
  GenesisBlocks.java
  GenesisEntities.java
content/item/
  material/
  weapon/
  armor/
  curio/
  spell/
```

### Item And Equipment Design

Problem:

- Item classes and event classes split one feature across many folders.
- Armor/weapon effects are partly in item classes, partly in global event classes, partly in mixins.
- Tooltips, shaders, stats, and gameplay behavior are interleaved.

Design:

- Each important equipment family gets a feature package:
  - `content/equipment/violet`
  - `content/equipment/divine_metal`
  - `content/equipment/celestial_source`
  - `content/equipment/chaos`
  - `content/equipment/gungnir`
- For each family, keep a predictable structure:
  - item classes
  - behavior/event class
  - client presentation class if needed
  - registry entries
- Keep reusable stat reading/writing in Genesis Lib.
- Keep actual balance numbers and item-specific behavior in EternisStarrySky.

Target rule:

- If a player asks "what does this item do?", the answer should live in one feature package, not five unrelated handlers.

### Spell And Effect Design

Problem:

- Spell classes, mob effects, effect events, projectiles, and mixins are spread across content and handlers.
- Rune interactions duplicate repeated checks like `ModCurios.hasCurios(entity, Rune::test)`.
- Some spell behavior depends on broad mixins without a clear feature owner.

Design:

- Keep each spell school as a feature domain:
  - `content/spell/chaos`
  - `content/spell/celestial_source`
  - `content/spell/fire`
  - `content/spell/ice`
  - `content/spell/thunder`
  - `content/spell/eldritch`
- Add a small `gameplay.spell` layer for cross-cutting spell rules:
  - spell penetration
  - rune modifiers
  - school-specific common hooks
- Create explicit helper services instead of copy-pasted curio checks:
  - `RuneEffectRules`
  - `SpellSchoolRules`
  - `SpellDamageRules`
- Keep mixins only where Iron's Spells has no event or extension hook.

### Event Design

Problem:

- Generic names like `EventHandler`, `ESSLivingEvent`, and scattered subscribers hide ownership.
- Client tooltip/render code appears in common event classes.
- Several event handlers use broad catches or side effects that are hard to test.

Design:

- Replace generic event containers with feature-named subscribers:
  - `gameplay.save.SaveEvents`
  - `gameplay.equipment.EquipmentStatEvents`
  - `gameplay.curio.RuneEvents`
  - `gameplay.spell.SpellCombatEvents`
  - `gameplay.effect.EffectSyncEvents`
  - `client.event.TooltipRenderEvents`
  - `client.event.ClientLivingRenderEvents`
- Each event class should answer three questions at the top of the file:
  - which bus
  - which side
  - which feature
- Avoid event classes that register unrelated behavior only because they share the same event type.

### Network Design

Problem:

- Packet channels and packet packages are inconsistent.
- Some packets are grouped by implementation accident rather than feature.
- Empty channel registration invites confusion.

Design:

- Use one channel unless a protocol boundary is truly needed.
- Group packets under `common.network` by feature:
  - `sync`
  - `spell`
  - `workbench`
  - `client_effect`
- Name packets by direction and intent:
  - `SyncDeadEntitiesS2CPacket`
  - `SyncWireBoxS2CPacket`
  - `TransferArcaneWorkbenchRecipeC2SPacket`
  - `SpawnSlashEffectS2CPacket`
- Centralize registration in `GenesisNetwork`.

### Client Rendering Design

Problem:

- `client.render`, `client.renderer`, and renderer classes inside `contents.entity` overlap.
- Some render APIs have moved to Genesis Lib while old local wrappers remain.
- Debug renderers and production renderers are mixed.

Design:

- Use `client.render` for systems and render helpers.
- Use `client.renderer` only for Minecraft renderer classes if keeping the current convention, or collapse both into `client.render` during rewrite.
- Entity renderers belong under `client.render.entity`.
- Item/block/screen renderers belong under explicit folders.
- Reusable shader/cosmic/tooltip/trail APIs come from Genesis Lib.
- Mod-specific visual choices stay in EternisStarrySky.

### Workbench Design

Problem:

- Arcane workbench and cauldron combine block, menu, recipe, JEI, screen, transfer, and packet code across several packages.

Design:

- Treat each workbench as a complete feature:
  - `content/workbench/arcane`
  - `content/workbench/arcane_cauldron`
- Each feature owns:
  - block
  - block entity
  - menu
  - screen
  - recipe
  - serializer/type registration entry
  - JEI category/transfer integration
  - network packet if needed
- Shared workbench utilities can live in `content/workbench/common`.

### Mixin Design

Problem:

- Mixins are organized mostly by target path, not by reason.
- It is hard to know which gameplay feature breaks if a mixin changes.

Design:

- Keep physical package by target if helpful, but document feature ownership.
- Add a mixin inventory document before editing many mixins:
  - target class
  - injected method
  - feature owner
  - reason event/API is insufficient
  - removal condition
- Prefer fewer, stronger mixins over many tiny unexplained injections.

## Rewrite Order

### Phase 1: Boundary Cleanup

- Finish replacing old main-project API imports with Genesis Lib imports.
- Remove duplicate main-project API classes after all references are gone.
- Split client-only event handling out of common event classes.

Exit criteria:

- Dedicated server can load classes without touching client packages.
- The main mod has no duplicate implementation of APIs already provided by Genesis Lib.
- `genesis_core` is ignored by this refactor and remains externally managed.

### Phase 2: Registry Rewrite

- Split `ItemRegistry` into focused registry classes:
  - materials
  - tools
  - weapons
  - armor
  - curios
  - spellbooks/staffs/manuscripts
  - misc/special items
- Split `BlockRegistry` into:
  - stone/wood block sets
  - plants/crops
  - ores/material blocks
  - workbench blocks
  - portal/dimension blocks
- Keep `ModRegistries` as a small bootstrap only.
- Keep `CreativeTabRegistry` free of hidden spell registration side effects unless that is truly required by Iron's Spells.

Exit criteria:

- Each registry file is small enough to understand in one pass.
- Datagen helpers are not buried inside huge registry classes unless local to one entry.

### Phase 3: Gameplay Events

- Replace scattered subscribers with feature-specific systems:
  - `gameplay.equipment`
  - `gameplay.curio`
  - `gameplay.spell`
  - `gameplay.combat`
  - `gameplay.save`
  - `client.event`
- Move tooltip rendering and client render events under `client`.
- Keep server lifecycle and save sync under `gameplay.save`.
- Prefer explicit event classes with names that describe the feature, not generic `EventHandler`.

Exit criteria:

- Event handlers can be traced from feature to behavior.
- Common handlers have no `Minecraft` client references.

### Phase 4: Network

- Replace multiple packet channels with one explicit channel unless there is a real protocol reason.
- Group packet classes by feature:
  - `network.spell`
  - `network.workbench`
  - `network.debug` if retained
  - `network.sync`
- Centralize packet IDs and registration.
- Make packet direction clear from class name or registration call.

Exit criteria:

- No empty channel registration remains.
- Join sync, workbench transfer, slash effect, and GoodCake packets are all grouped and named by purpose.

### Phase 5: Mixin Inventory

- Group mixins by reason:
  - Iron's Spells rune interactions
  - Iron's Spells UI integration
  - Minecraft tooltip/render effects
  - damage/effect hooks
  - particle suppression
- For each mixin, record why an event/API hook is insufficient.
- Remove mixins made obsolete by Genesis Lib damage/render APIs.

Exit criteria:

- `mixins.iron_spells_genesis.json` is explainable feature by feature.
- Each mixin has a surviving reason.

### Phase 6: Resources And Datagen

- Keep `src/main/resources` for hand-written resources only.
- Keep `src/generated/resources` generated and reproducible.
- Add a small inventory for legacy resource pack contents.
- Remove generated test recipes and placeholder assets once no code references them.
- Preserve custom model JSON files in `src/main/resources` by default.

Exit criteria:

- Running datagen can recreate generated files.
- Legacy assets are intentionally optional, not accidentally duplicated.

## Immediate Work Queue

1. Audit current Genesis Lib migration imports and remove dead main-project API copies.
2. Extract client tooltip/render handling out of `handlers.EventHandler`.
3. Remove or quarantine test entity/particle registrations.
4. Collapse `NetworkHandler` and `ModPacketHandler` into one channel.
5. Split `ItemRegistry` into first two domains: materials and weapons.
6. Split `BlockRegistry` into block sets and special blocks.
7. Rewrite event handlers by feature after registries are stable.

## Validation Checklist

Run these after each phase:

- `gradlew compileJava`
- `gradlew processResources`
- client run starts
- dedicated server run starts
- datagen completes when the phase touches data/resources

Manual checks:

- Items and blocks appear in expected creative tabs.
- Spells load in Iron's Spells.
- Curios and rune effects still trigger.
- Tooltip/shader effects render only on client.
- Join sync packets do not throw.
