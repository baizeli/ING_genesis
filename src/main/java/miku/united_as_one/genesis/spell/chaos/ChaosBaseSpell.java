package miku.united_as_one.genesis.spell.chaos;

import miku.united_as_one.genesis.spell.SpellSchool;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;

public abstract class ChaosBaseSpell extends AbstractSpell {
    @Override
    public SchoolType getSchoolType() {
        return SpellSchool.CHAOS.get();
    }
}
