package miku.united_as_one.genesis.handlers.armor;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ArcaneCrystalArmorEvent {
    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if (event.phase != TickEvent.Phase.END || player.level().isClientSide) return;
        if (++tickCounter % 20 != 0) return;
        
        ServerPlayer serverPlayer = (ServerPlayer) player;
        MagicData magicData = MagicData.getPlayerMagicData(serverPlayer);

        float currentMana = magicData.getMana();
        float maxMana = (float) serverPlayer.getAttributeValue(AttributeRegistry.MAX_MANA.get());
        
        if ((currentMana < maxMana * 0.8F || currentMana <= 0) && !serverPlayer.isCreative()) return;
        
        boolean repaired = false;
        for (int slot = 3; slot >= 0; slot--) {
            ItemStack stack = player.getInventory().getArmor(slot);
            if (!stack.isEmpty() && stack.getItem() instanceof ArmorItem item 
                && item.getMaterial().getName().equals("arcane_crystal") && stack.getDamageValue() > 0) {
                stack.setDamageValue(Math.max(0, stack.getDamageValue() - 1));
                repaired = true;
            }
        }
        
        if (repaired && !serverPlayer.isCreative()) {
            magicData.setMana(currentMana - 1);
        }
    }
}