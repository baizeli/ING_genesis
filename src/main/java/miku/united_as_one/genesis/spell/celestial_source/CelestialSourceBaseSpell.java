package miku.united_as_one.genesis.spell.celestial_source;

import miku.united_as_one.genesis.spell.SpellSchool;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;

public abstract class CelestialSourceBaseSpell extends AbstractSpell {
    @Override
    public SchoolType getSchoolType() {
        return SpellSchool.CELESTIAL_SOURCE.get();
    }
}
