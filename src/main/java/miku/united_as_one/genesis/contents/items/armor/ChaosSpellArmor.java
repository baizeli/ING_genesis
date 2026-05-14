package miku.united_as_one.genesis.contents.items.armor;

import miku.united_as_one.genesis.Genesis;
import io.redspace.ironsspellbooks.entity.armor.*;
import io.redspace.ironsspellbooks.item.armor.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.*;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ChaosSpellArmor extends ExtendedArmorItem {
    public ChaosSpellArmor(IronsExtendedArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties.rarity(Rarity.EPIC));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new ChaosSpellArmorRenderer(
            new GeoModel<>() {
                @Override
                public ResourceLocation getModelResource(ChaosSpellArmor object) {
                    return ResourceLocation.fromNamespaceAndPath(
                        Genesis.MOD_ID, "geo/armor/chaos_spell_armor.geo.json"
                    );
                }

                @Override
                public ResourceLocation getTextureResource(ChaosSpellArmor object) {
                    return ResourceLocation.fromNamespaceAndPath(
                        Genesis.MOD_ID, "textures/models/armor/chaos_spell.png"
                    );
                }

                @Override
                public ResourceLocation getAnimationResource(ChaosSpellArmor animatable) {
                    return ResourceLocation.fromNamespaceAndPath(
                        Genesis.MOD_ID, "animations/armor/chaos_spell_armor.animation.json"
                    );
                }
            }
        );
    }

    public static class ChaosSpellArmorRenderer extends GenericCustomArmorRenderer<ChaosSpellArmor> {
        public ChaosSpellArmorRenderer(GeoModel<ChaosSpellArmor> model) {
            super(model);
        }
    }
}