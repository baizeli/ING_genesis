package miku.united_as_one.genesis.common.items.armor;

import miku.united_as_one.genesis.Genesis;
import io.redspace.ironsspellbooks.entity.armor.*;
import io.redspace.ironsspellbooks.item.armor.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.*;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class VioletZenithArmor extends ExtendedArmorItem {
    public VioletZenithArmor(IronsExtendedArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties.rarity(Rarity.EPIC));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new VioletZenithArmorRenderer(
            new GeoModel<>() {
                @Override
                public ResourceLocation getModelResource(VioletZenithArmor object) {
                    return ResourceLocation.fromNamespaceAndPath(
                        Genesis.MOD_ID, "geo/armor/violet_zenith_armor.geo.json"
                    );
                }

                @Override
                public ResourceLocation getTextureResource(VioletZenithArmor object) {
                    return ResourceLocation.fromNamespaceAndPath(
                        Genesis.MOD_ID, "textures/models/armor/violet_zenith.png"
                    );
                }

                @Override
                public ResourceLocation getAnimationResource(VioletZenithArmor animatable) {
                    return ResourceLocation.fromNamespaceAndPath(
                        Genesis.MOD_ID, "animations/armor/violet_zenith_armor.animation.json"
                    );
                }
            }
        );
    }

    public static class VioletZenithArmorRenderer extends GenericCustomArmorRenderer<VioletZenithArmor> {
        public VioletZenithArmorRenderer(GeoModel<VioletZenithArmor> model) {
            super(model);
        }
    }
}