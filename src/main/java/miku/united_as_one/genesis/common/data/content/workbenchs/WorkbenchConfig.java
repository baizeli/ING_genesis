package miku.united_as_one.genesis.common.data.content.workbenchs;

public class WorkbenchConfig {
    public static final int GRID_SIZE = 9;
    public static final int RESULT_SLOT = GRID_SIZE * GRID_SIZE; // 81
    public static final int TOTAL_SLOTS = RESULT_SLOT + 1; // 82
    public static final int TOTAL_CRAFTING_SLOTS = GRID_SIZE * GRID_SIZE; // 81 (不包括结果槽位)

    // 奥术工作台[5x5x5]
    public static final int ARCANE_GRID_SIZE = 5;
    public static final int ARCANE_RESULT_SLOT = ARCANE_GRID_SIZE * ARCANE_GRID_SIZE; // 25
    public static final int ARCANE_TOTAL_SLOTS = ARCANE_RESULT_SLOT + 1; // 26
    public static final int ARCANE_TOTAL_CRAFTING_SLOTS = ARCANE_GRID_SIZE * ARCANE_GRID_SIZE; // 25 同上
}