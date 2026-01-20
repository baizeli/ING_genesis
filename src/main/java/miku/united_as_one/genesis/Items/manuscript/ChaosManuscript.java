package miku.united_as_one.genesis.Items.manuscript;

import miku.united_as_one.genesis.EternisStarrySky;
import miku.united_as_one.genesis.Items.ModItems;
import miku.united_as_one.genesis.client.gui.manuscript.ChaosSpellLearningScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.*;

import java.util.List;

public class ChaosManuscript extends ModItems.ChaosBaseItem {
    public ChaosManuscript() {
        super(new Properties().rarity(Rarity.EPIC));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, @Nullable Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("item." + EternisStarrySky.MOD_ID + ".chaos_manuscript.hover"));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack itemInHand = player.getItemInHand(usedHand);
        
        if (level.isClientSide) {
            Minecraft.getInstance().setScreen(new ChaosSpellLearningScreen(Component.translatable("item.irons_spellbooks.eldritch_manuscript"), usedHand));
        }

        return InteractionResultHolder.success(itemInHand);
    }
}