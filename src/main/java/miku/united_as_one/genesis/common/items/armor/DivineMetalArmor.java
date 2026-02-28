package miku.united_as_one.genesis.common.items.armor;

import miku.united_as_one.genesis.Genesis;
import io.redspace.ironsspellbooks.entity.armor.*;
import io.redspace.ironsspellbooks.item.armor.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.*;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class DivineMetalArmor extends ExtendedArmorItem {
    public DivineMetalArmor(IronsExtendedArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties.rarity(Rarity.EPIC));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new DivineMetalArmorRenderer(
            new GeoModel<>() {
                @Override
                public ResourceLocation getModelResource(DivineMetalArmor object) {
                    return ResourceLocation.fromNamespaceAndPath(
                        Genesis.MOD_ID, "geo/armor/divine_metal_armor.geo.json"
                    );
                }

                @Override
                public ResourceLocation getTextureResource(DivineMetalArmor object) {
                    return ResourceLocation.fromNamespaceAndPath(
                        Genesis.MOD_ID, "textures/models/armor/divine_metal.png"
                    );
                }

                @Override
                public ResourceLocation getAnimationResource(DivineMetalArmor animatable) {
                    return ResourceLocation.fromNamespaceAndPath(
                        Genesis.MOD_ID, "animations/armor/divine_metal_armor.animation.json"
                    );
                }
            }
        );
    }

    public static class DivineMetalArmorRenderer extends GenericCustomArmorRenderer<DivineMetalArmor> {
        public DivineMetalArmorRenderer(GeoModel<DivineMetalArmor> model) {
            super(model);
        }
    }
}