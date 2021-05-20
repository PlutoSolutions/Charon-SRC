package cc.zip.charon.client.modules.client;

import java.awt.Color;

import cc.zip.charon.Charon;
import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.ColorUtil;
import cc.zip.charon.api.util.RenderUtil;
import cc.zip.charon.client.event.events.TickEvent;
import cc.zip.charon.client.gui.Gui;
import cc.zip.charon.client.managers.ModuleManager;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import tcb.bces.listener.Subscribe;

@ModuleManifest(label="GuiGradient", category=Module.Category.CLIENT)
public class  GuiGradient
        extends Module {
    public final Setting<Boolean> background = this.register(new Setting<Boolean>("Background", true));
    public final Setting<Integer> red1 = this.register(new Setting<Integer>("Red", 255, 0, 255, v -> this.background.getValue()));
    public final Setting<Integer> green1 = this.register(new Setting<Integer>("Green", 255, 0, 255, v -> this.background.getValue()));
    public final Setting<Integer> blue1 = this.register(new Setting<Integer>("Blue", 255, 0, 255, v -> this.background.getValue()));
    public final Setting<Integer> red2 = this.register(new Setting<Integer>("Red 2", 255, 0, 255, v -> this.background.getValue()));
    public final Setting<Integer> green2 = this.register(new Setting<Integer>("Green 2", 255, 0, 255, v -> this.background.getValue()));
    public final Setting<Integer> blue2 = this.register(new Setting<Integer>("Blue 2", 255, 0, 255, v -> this.background.getValue()));
    public final Setting<Integer> alpha1 = this.register(new Setting<Integer>("Alpha 1", 0, 0, 255, v -> this.background.getValue()));
    public final Setting<Integer> alpha2 = this.register(new Setting<Integer>("Alpha 2", 255, 0, 255, v -> this.background.getValue()));
    public final Setting<Boolean> guiComponent = this.register(new Setting<Boolean>("Gui Component", true));
    public final Setting<Integer> g_red = this.register(new Setting<Integer>("Red", 240, 0, 255, v -> this.guiComponent.getValue()));
    public final Setting<Integer> g_green = this.register(new Setting<Integer>("Green", 126, 0, 255, v -> this.guiComponent.getValue()));
    public final Setting<Integer> g_blue = this.register(new Setting<Integer>("Blue", 255, 0, 255, v -> this.guiComponent.getValue()));
    public final Setting<Integer> g_red1 = this.register(new Setting<Integer>("Red 2", 145, 0, 255, v -> this.guiComponent.getValue()));
    public final Setting<Integer> g_green1 = this.register(new Setting<Integer>("Green 2", 40, 0, 255, v -> this.guiComponent.getValue()));
    public final Setting<Integer> g_blue1 = this.register(new Setting<Integer>("Blue 2", 255, 0, 255, v -> this.guiComponent.getValue()));
    public final Setting<Integer> g_alpha = this.register(new Setting<Integer>("Alpha 1", 255, 0, 255, v -> this.guiComponent.getValue()));
    public final Setting<Integer> g_alpha1 = this.register(new Setting<Integer>("Alpha 2", 255, 0, 255, v -> this.guiComponent.getValue()));


    private static GuiGradient INSTANCE = new GuiGradient();

    public GuiGradient() {
        INSTANCE = this;
    }

    public static GuiGradient getInstance() {
        return INSTANCE;
    }
}

