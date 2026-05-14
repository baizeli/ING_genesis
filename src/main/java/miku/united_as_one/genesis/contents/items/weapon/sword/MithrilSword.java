package miku.united_as_one.genesis.contents.items.weapon.sword;

import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.*;
import miku.united_as_one.genesis.client.render.SlashEffectAPI;
import miku.united_as_one.genesis.packets.NetworkHandler;
import miku.united_as_one.genesis.packets.packet.SpawnSlashPacket;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraftforge.network.PacketDistributor;

import java.util.Map;

@SuppressWarnings("removal")
public class MithrilSword extends MagicSwordItem {
    public MithrilSword(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, SpellDataRegistryHolder.of(new SpellDataRegistryHolder(SpellRegistry.RAY_OF_FROST_SPELL, 5)), Map.of(), properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.level().isClientSide()) {
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> attacker),
                    new SpawnSlashPacket(attacker.getId(), target.getId())
            );
        }
        return super.hurtEnemy(stack, target, attacker);
    }
}