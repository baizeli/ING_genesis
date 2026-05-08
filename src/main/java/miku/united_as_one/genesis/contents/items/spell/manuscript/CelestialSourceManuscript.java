package miku.united_as_one.genesis.contents.items.spell.manuscript;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.gui.manuscript.CelestialSourceSpellLearningScreen;
import miku.united_as_one.genesis.contents.items.CelestialSourceBase;
import net.minecraft.network.chat.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.*;

import java.util.*;

public class CelestialSourceManuscript extends CelestialSourceBase {
    public CelestialSourceManuscript() {
        super(new Properties().fireResistant().rarity(Rarity.EPIC));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, @Nullable Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("item." + Genesis.MOD_ID + ".celestial_source_manuscript.hover"));
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack itemInHand = player.getItemInHand(usedHand);
        
        if (level.isClientSide) {
           Minecraft.getInstance().setScreen(new CelestialSourceSpellLearningScreen(Component.translatable("item.irons_spellbooks.eldritch_manuscript"), usedHand));
        }

        return InteractionResultHolder.success(itemInHand);
    }
}