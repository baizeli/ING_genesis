package com.baizeli.eternisstarrysky.Items;

import io.redspace.ironsspellbooks.entity.armor.GenericArmorModel;
import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.ExtendedArmorItem;
import io.redspace.ironsspellbooks.item.armor.IronsExtendedArmorMaterial;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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