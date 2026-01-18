package com.baizeli.eternisstarrysky.Mixin.ironsspellbooks.spells.holy;

import com.baizeli.eternisstarrysky.Items.curios.rune_plus.HolyRunePlus;
import com.baizeli.eternisstarrysky.util.ModCurios;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.spells.holy.WispSpell;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = WispSpell.class, remap = false)
public abstract class MixinWispSpell extends AbstractSpell {
    @Override
    public float getSpellPower(int spellLevel, @Nullable Entity sourceEntity) {
        float power = super.getSpellPower(spellLevel, sourceEntity);
        if(sourceEntity instanceof LivingEntity entity) {
            if(ModCurios.hasCurios(entity, HolyRunePlus::test)) {
                power *= 1.5f;
            }
        }
        return power;
    }
}
