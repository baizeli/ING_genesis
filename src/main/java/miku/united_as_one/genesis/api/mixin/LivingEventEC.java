package miku.united_as_one.genesis.api.mixin;

public interface LivingEventEC {
    //源代码来自revelationfix，原作者mega32k
    boolean ironSpellGenesis$isHackedUnCancelable();

    void ironSpellGenesis$hackedUnCancelable(boolean target);

    //仅适配部分事件
    boolean ironSpellGenesis$isHackedOnlyAmountUp();

    void ironSpellGenesis$hackedOnlyAmountUp(boolean target);
}
