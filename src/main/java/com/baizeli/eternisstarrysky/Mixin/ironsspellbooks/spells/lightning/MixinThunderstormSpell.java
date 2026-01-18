package com.baizeli.eternisstarrysky.Mixin.ironsspellbooks.spells.lightning;

import com.baizeli.eternisstarrysky.Items.curios.rune_plus.LightningRunePlus;
import com.baizeli.eternisstarrysky.util.ModCurios;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.spells.lightning.ThunderstormSpell;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ThunderstormSpell.class, remap = false)
public abstract class MixinThunderstormSpell extends AbstractSpell {
    @Override
    public int getEffectiveCastTime(int spellLevel, @Nullable LivingEntity entity) {
        if(ModCurios.hasCurios(entity, LightningRunePlus::test)) return 0;
        return super.getEffectiveCastTime(spellLevel, entity);
    }
}
