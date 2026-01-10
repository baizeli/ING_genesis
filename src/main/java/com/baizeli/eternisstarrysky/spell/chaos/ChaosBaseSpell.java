package com.baizeli.eternisstarrysky.spell.chaos;

import com.baizeli.eternisstarrysky.spell.SpellSchool;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;

public abstract class ChaosBaseSpell extends AbstractSpell {
    @Override
    public SchoolType getSchoolType() {
        return SpellSchool.CHAOS.get();
    }
}
