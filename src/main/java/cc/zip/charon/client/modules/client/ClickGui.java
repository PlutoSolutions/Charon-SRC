/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.settings.GameSettings$Options
 */
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

@ModuleManifest(label="ClickGui", category=Module.Category.CLIENT)
public class ClickGui
extends Module {
    public final Setting<String> prefix = this.register(new Setting<String>("Prefix", "."));
    public final Setting<Boolean> blur = this.register(new Setting<Boolean>("Blur", true));
    public final Setting<Boolean> rainbow = this.register(new Setting<Boolean>("rainbow", true));
    public final Setting<Boolean> outline = this.register(new Setting<Boolean>("test", false));

    public final Setting<Boolean> customFov = this.register(new Setting<Boolean>("Custom Fov", false));
    public final Setting<Float> fov = this.register(new Setting<Float>("Fov", Float.valueOf(150.0f), Float.valueOf(-180.0f), Float.valueOf(180.0f)));
    public final Setting<Integer> red = this.register(new Setting<Integer>("Red", 175, 0, 255));
    public final Setting<Integer> green = this.register(new Setting<Integer>("Green", 13, 0, 255));
    public final Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 229, 0, 255));
    public final Setting<Integer> hoverAlpha = this.register(new Setting<Integer>("Hover Alpha", 60, 0, 255));
    public final Setting<Integer> enabledAlpha = this.register(new Setting<Integer>("Enabled Alpha", 60, 0, 255));
   
    private static ClickGui INSTANCE = new ClickGui();
    private int startcolor1;
    private int endcolor1;
    public ClickGui() {
        INSTANCE = this;
    }

    public static ClickGui getInstance() {
        return INSTANCE;
    }

    public final int getColor(boolean hover) {
        return new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), hover ? this.hoverAlpha.getValue().intValue() : this.enabledAlpha.getValue().intValue()).getRGB();
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (event.getStage() == 0 || this.mc.player == null || this.mc.world == null) {
            return;
        }
        if (!(this.mc.currentScreen instanceof Gui)) {
            this.setEnabled(false);
        }
        if (this.customFov.getValue().booleanValue()) {
            this.mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, this.fov.getValue().floatValue());
        }
    }

    @Override
    public void onEnable() {
        if (this.mc.player != null) {
            this.mc.displayGuiScreen((GuiScreen) new Gui());
            if (blur.getValue()) {
                mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
            }
        }
    }

    @Override
    public void onRender2D() {
        if (ClickGui.getInstance().isEnabled()){
            startcolor1 = ColorUtil.toRGBA((GuiGradient.getInstance()).red1.getValue().intValue(), (GuiGradient.getInstance()).green1.getValue().intValue(), (GuiGradient.getInstance()).blue1.getValue().intValue(), (GuiGradient.getInstance()).alpha1.getValue().intValue());
            endcolor1 = ColorUtil.toRGBA((GuiGradient.getInstance()).red2.getValue().intValue(), (GuiGradient.getInstance()).green2.getValue().intValue(), (GuiGradient.getInstance()).blue2.getValue().intValue(), (GuiGradient.getInstance()).alpha2.getValue().intValue());
            RenderUtil.drawGradientRect(0,
                    0,
                    1800,
                    1800,
                    startcolor1,
                    endcolor1);


        }
    }

    @Override
    public void onDisable() {
        if (this.mc.currentScreen instanceof Gui) {
            this.mc.displayGuiScreen(null);
        }
    }
}

