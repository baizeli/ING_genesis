package miku.united_as_one.genesis.items.sword;

import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.*;
import net.minecraft.world.item.Tier;

import java.util.Map;

@SuppressWarnings("removal")
public class MithrilSword extends MagicSwordItem {
    public MithrilSword(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, SpellDataRegistryHolder.of(new SpellDataRegistryHolder(SpellRegistry.RAY_OF_FROST_SPELL, 5)), Map.of(), properties);
    }
}