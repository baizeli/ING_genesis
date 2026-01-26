package miku.united_as_one.genesis.client.gui.manuscript;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.registry.ItemRegistry;
import miku.united_as_one.genesis.registry.spell.SpellSchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class CelestialSourceSpellLearningScreen extends AbstractSpellLearningScreen {
    public CelestialSourceSpellLearningScreen(Component pTitle, InteractionHand activeHand) {
        super(pTitle, activeHand);
    }

    @Override
    protected AbstractSpell getSchoolFilter() {
        return new AbstractSpell() {
            @Override
            public SchoolType getSchoolType() {
                return SpellSchoolRegistry.CELESTIAL_SOURCE.get();
            }

            @Override
            public ResourceLocation getSpellResource() {
                return ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "celestial_source_filter");
            }

            @Override
            public DefaultConfig getDefaultConfig() {
                return new DefaultConfig();
            }

            @Override
            public CastType getCastType() {
                return CastType.INSTANT;
            }
        };
    }

    @Override
    protected boolean isCorrectManuscript() {
        return Minecraft.getInstance().player.getItemInHand(activeHand).getItem() == ItemRegistry.CELESTIAL_SOURCE_MANUSCRIPT.get();
    }

    @Override
    protected Component getRequiredItemName() {
        return Component.translatable("item." + Genesis.MOD_ID + ".celestial_source_manuscript");
    }
}