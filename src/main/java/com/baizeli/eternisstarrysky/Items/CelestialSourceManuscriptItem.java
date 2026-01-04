package com.baizeli.eternisstarrysky.Items;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CelestialSourceManuscriptItem extends ModItems.CelestialSourceBaseItem {
    public CelestialSourceManuscriptItem() {
        super(new Properties().fireResistant().rarity(Rarity.EPIC));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, @Nullable Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable(
                "item." + EternisStarrySky.MOD_ID + ".celestial_source_manuscript.hover"
        ));
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        return super.use(level, player, usedHand);
    }
}
