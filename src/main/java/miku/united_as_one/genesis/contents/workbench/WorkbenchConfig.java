package miku.united_as_one.genesis.contents.workbench;

public class WorkbenchConfig {
    // 奥术工作台[5x5x5]
    public static final int ARCANE_GRID_SIZE = 5;
    public static final int ARCANE_RESULT_SLOT = ARCANE_GRID_SIZE * ARCANE_GRID_SIZE; // 25
    public static final int ARCANE_TOTAL_SLOTS = ARCANE_RESULT_SLOT + 1; // 26
    public static final int ARCANE_TOTAL_CRAFTING_SLOTS = ARCANE_GRID_SIZE * ARCANE_GRID_SIZE; // 25 同上
}