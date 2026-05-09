package miku.united_as_one.genesis.contents.items.armor;

import miku.united_as_one.genesis.Genesis;
import io.redspace.ironsspellbooks.entity.armor.*;
import io.redspace.ironsspellbooks.item.armor.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.*;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ArcaneCrystalArmor extends ExtendedArmorItem {
    public ArcaneCrystalArmor(IronsExtendedArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties.rarity(Rarity.EPIC));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new ArcaneCrystalArmorRenderer(
            new GeoModel<>() {
                @Override
                public ResourceLocation getModelResource(ArcaneCrystalArmor object) {
                    return ResourceLocation.fromNamespaceAndPath(
                        Genesis.MOD_ID, "geo/armor/arcane_crystal_armor.geo.json"
                    );
                }

                @Override
                public ResourceLocation getTextureResource(ArcaneCrystalArmor object) {
                    return ResourceLocation.fromNamespaceAndPath(
                        Genesis.MOD_ID, "textures/models/armor/arcane_crystal.png"
                    );
                }

                @Override
                public ResourceLocation getAnimationResource(ArcaneCrystalArmor animatable) {
                    return ResourceLocation.fromNamespaceAndPath(
                        Genesis.MOD_ID, "animations/armor/arcane_crystal_armor.animation.json"
                    );
                }
            }
        );
    }

    public static class ArcaneCrystalArmorRenderer extends GenericCustomArmorRenderer<ArcaneCrystalArmor> {
        public ArcaneCrystalArmorRenderer(GeoModel<ArcaneCrystalArmor> model) {
            super(model);
        }
    }
}