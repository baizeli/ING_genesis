package com.baizeli.eternisstarrysky.Items;

import io.redspace.ironsspellbooks.item.armor.*;
import io.redspace.ironsspellbooks.entity.armor.*;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.*;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class DivineMetalArmorItem extends ExtendedArmorItem {
    public DivineMetalArmorItem(IronsExtendedArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties.rarity(Rarity.EPIC));
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer<>(new GenericArmorModel<>("iron_spells_genesis", "divine_metal"));
    }
}