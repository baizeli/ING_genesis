package miku.united_as_one.genesis.contents.spell.chaos;

import miku.united_as_one.genesis.registries.spell.SpellSchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;

public abstract class ChaosBaseSpell extends AbstractSpell {
    @Override
    public SchoolType getSchoolType() {
        return SpellSchoolRegistry.CHAOS.get();
    }
}
