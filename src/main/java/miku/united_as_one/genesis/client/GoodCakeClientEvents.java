package miku.united_as_one.genesis.client;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.contents.items.GoodCake;
import miku.united_as_one.genesis.packets.GoodCakeDamageAdjustPacket;
import miku.united_as_one.genesis.packets.GoodCakeLocateOrePacket;
import miku.united_as_one.genesis.packets.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Genesis.MODID, value = Dist.CLIENT)
public final class GoodCakeClientEvents {
    private GoodCakeClientEvents() {
    }

    @SubscribeEvent
    public static void onInteractionKey(InputEvent.InteractionKeyMappingTriggered event) {
        if (!event.isAttack()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen != null || minecraft.player == null || !minecraft.player.isCrouching()) {
            return;
        }

        ItemStack stack = minecraft.player.getMainHandItem();
        if (!(stack.getItem() instanceof GoodCake)) {
            return;
        }

        NetworkHandler.INSTANCE.sendToServer(new GoodCakeLocateOrePacket());
        event.setSwingHand(true);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen != null || minecraft.player == null || !minecraft.player.isCrouching()) {
            return;
        }

        ItemStack stack = minecraft.player.getMainHandItem();
        if (!(stack.getItem() instanceof GoodCake)) {
            return;
        }
        if (event.getScrollDelta() == 0) {
            return;
        }

        int steps = event.getScrollDelta() > 0 ? 1 : -1;
        GoodCake.adjustTrueDamage(stack, steps);
        NetworkHandler.INSTANCE.sendToServer(new GoodCakeDamageAdjustPacket(steps));
        event.setCanceled(true);
    }
}
