package miku.united_as_one.genesis.util.mixinutil;

public interface LivingEventEC {
    boolean revelationfix$isHackedUnCancelable();

    void revelationfix$hackedUnCancelable(boolean target);

    //仅适配部分事件
    boolean revelationfix$isHackedOnlyAmountUp();

    void revelationfix$hackedOnlyAmountUp(boolean target);
}
