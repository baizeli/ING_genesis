package miku.united_as_one.genesis.init.config.menu;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.init.config.Configuration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConfigMenu extends Screen
{
    public static final int PADDING = 8;
    public static final int HEADER_HEIGHT = 24;
    private final Screen parent;

    public final GroupValue GROUP_ETERNIS_APPLE;
    public final EffectArrayValue ETERNIS_APPLE_EFFECTS;

    public final GroupValue GROUP_COMBAT;
    public final DoubleValue WHISPER_OF_THE_PAST_DAMAGE;

    private EditBox searchBox;
    private String searchQuery = "";
    private ConfigArray configArray;

    public ConfigMenu(Screen parent)
    {
        super(Component.translatable(Genesis.MOD_ID + ".config.title"));
        this.parent = parent;
        Minecraft mc = parent.getMinecraft();
        Configuration.setup();

        this.GROUP_ETERNIS_APPLE = new GroupValue(this, Genesis.MOD_ID + ".config.eternis_apple", mc.font);
        this.GROUP_ETERNIS_APPLE.setExpanded(true);

        this.ETERNIS_APPLE_EFFECTS = new EffectArrayValue(this, Genesis.MOD_ID + ".config.eternis_apple.effects", mc.font);
        this.ETERNIS_APPLE_EFFECTS.value(Configuration.ETERNIS_APPLE_EFFECTS.get());
        this.ETERNIS_APPLE_EFFECTS.defaultValue = Configuration.DEFAULT_ETERNIS_APPLE_EFFECTS;
        this.ETERNIS_APPLE_EFFECTS.tooltip = Component.translatable(Genesis.MOD_ID + ".config.eternis_apple.effects.tooltip");
        this.GROUP_ETERNIS_APPLE.group.add(this.ETERNIS_APPLE_EFFECTS);

        this.GROUP_COMBAT = new GroupValue(this, Genesis.MOD_ID + ".config.combat", mc.font);
        this.GROUP_COMBAT.setExpanded(false);

        this.WHISPER_OF_THE_PAST_DAMAGE = new DoubleValue(this, Genesis.MOD_ID + ".config.whisper_of_the_past.damage", mc.font);
        this.WHISPER_OF_THE_PAST_DAMAGE.min = 0.0;
        this.WHISPER_OF_THE_PAST_DAMAGE.max = Double.MAX_VALUE;
        this.WHISPER_OF_THE_PAST_DAMAGE.value(Configuration.WHISPER_OF_THE_PAST_DAMAGE.get());
        this.WHISPER_OF_THE_PAST_DAMAGE.resetValue = (val) -> ((DoubleValue) val).value(Configuration.WHISPER_OF_THE_PAST_DAMAGE.getDefault());
        this.WHISPER_OF_THE_PAST_DAMAGE.description = Component.translatable(Genesis.MOD_ID + ".config.whisper_of_the_past.damage.desc");
        this.GROUP_COMBAT.group.add(this.WHISPER_OF_THE_PAST_DAMAGE);
    }

    @Override
    public void onClose()
    {
        if (this.minecraft == null)
            super.onClose();
        else
            this.minecraft.setScreen(this.parent);
    }

    @Override
    public void init()
    {
        this.clearWidgets();

        int buttonWidth = 150;
        int buttonHeight = 20;
        int buttonY = this.height - PADDING - buttonHeight;

        Button saveButton = Button.builder(
                Component.translatable(Genesis.MOD_ID + ".config.save"),
                b -> this.save()
        ).bounds(PADDING, buttonY, buttonWidth, buttonHeight).build();

        Button doneButton = Button.builder(
                Component.translatable(Genesis.MOD_ID + ".config.done"),
                b -> this.close()
        ).bounds(this.width - PADDING - buttonWidth, buttonY, buttonWidth, buttonHeight).build();

        Button resetAllButton = Button.builder(
                Component.translatable(Genesis.MOD_ID + ".config.reset_all"),
                b -> this.resetAll()
        ).bounds((this.width - buttonWidth) / 2, buttonY, buttonWidth, buttonHeight).build();

        this.addRenderableWidget(saveButton);
        this.addRenderableWidget(doneButton);
        this.addRenderableWidget(resetAllButton);

        int arrayTop = HEADER_HEIGHT + PADDING * 2;
        int arrayHeight = this.height - arrayTop - buttonHeight - PADDING * 2;
        int arrayWidth = this.width - PADDING * 2;

        this.configArray = new ConfigArray(this.getMinecraft(), arrayWidth, arrayHeight, arrayTop, PADDING);
        this.configArray.setSearchQuery(this.searchQuery);
        this.configArray.push(this.GROUP_ETERNIS_APPLE);
        this.configArray.push(this.GROUP_COMBAT);
        this.addRenderableWidget(this.configArray);

        int searchBoxWidth = 200;
        this.searchBox = new EditBox(
                this.font,
                (this.width - searchBoxWidth) / 2,
                PADDING + 2,
                searchBoxWidth,
                18,
                Component.translatable(Genesis.MOD_ID + ".config.search")
        );
        this.searchBox.setValue(this.searchQuery);
        this.searchBox.setResponder(query -> {
            this.searchQuery = query;
            if (this.configArray != null) {
                this.configArray.setSearchQuery(query);
            }
        });
        this.addWidget(this.searchBox);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
    {
        this.renderBackground(graphics);

        graphics.drawCenteredString(
                this.font,
                this.title,
                this.width / 2,
                PADDING,
                0xFFFFFF
        );

        super.render(graphics, mouseX, mouseY, partialTick);

        if (this.searchBox.isHovered()) {
            graphics.renderTooltip(
                    this.font,
                    Component.translatable(Genesis.MOD_ID + ".config.search.tooltip"),
                    mouseX,
                    mouseY
            );
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if (this.searchBox.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public void save()
    {
        Configuration.ETERNIS_APPLE_EFFECTS.set(List.copyOf(this.ETERNIS_APPLE_EFFECTS.value()));
        Configuration.saveEternisApple();
        Configuration.WHISPER_OF_THE_PAST_DAMAGE.set(this.WHISPER_OF_THE_PAST_DAMAGE.value.doubleValue());
        Configuration.WHISPER_OF_THE_PAST_DAMAGE.save();
    }

    public void resetAll()
    {
        this.ETERNIS_APPLE_EFFECTS.reset(null);
        this.WHISPER_OF_THE_PAST_DAMAGE.reset(null);
        this.save();
    }

    public void close()
    {
        this.save();
        this.onClose();
    }

    public String getSearchQuery() {
        return this.searchQuery;
    }
}

