/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  org.lwjgl.input.Mouse
 */
package cc.zip.charon.client.gui.components.items.buttons;

import cc.zip.charon.Charon;
import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.ColorUtil;
import cc.zip.charon.api.util.RenderUtil;
import cc.zip.charon.client.gui.Gui;
import cc.zip.charon.client.gui.components.Component;
import cc.zip.charon.client.managers.ColorManager;
import cc.zip.charon.client.modules.client.GuiGradient;
import com.mojang.realmsclient.gui.ChatFormatting;
import org.lwjgl.input.Mouse;

public class Slider
extends Button {
    public final Setting setting;
    private final Number min;
    private final Number max;
    private final int difference;
    private int startcolor;
    private int endcolor;
    public Slider(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.min = (Number)setting.getMin();
        this.max = (Number)setting.getMax();
        this.difference = this.max.intValue() - this.min.intValue();
        this.width = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.dragSetting(mouseX, mouseY);
        startcolor = ColorUtil.toRGBA(GuiGradient.getInstance().g_red.getValue(), GuiGradient.getInstance().g_green.getValue(), GuiGradient.getInstance().g_blue.getValue(), GuiGradient.getInstance().g_alpha.getValue());
        endcolor =  ColorUtil.toRGBA(GuiGradient.getInstance().g_red1.getValue(), GuiGradient.getInstance().g_green1.getValue(), GuiGradient.getInstance().g_blue1.getValue(), GuiGradient.getInstance().g_alpha1.getValue());;

        RenderUtil.drawRect(this.x, this.y + 2, ((Number)this.setting.getValue()).floatValue() <= this.min.floatValue() ? this.x : this.x + ((float)this.width + 7.4f) * this.partialMultiplier(), this.y + (float)this.height - 1f, this.getColor(true, this.isHovering(mouseX, mouseY)));
        RenderUtil.drawGradientSideways(this.x, this.y + 2, ((Number)this.setting.getValue()).floatValue() <= this.min.floatValue() ? this.x : this.x + ((float)this.width + 7.4f) * this.partialMultiplier(), this.y + (float)this.height - 1f, startcolor, endcolor);
        //RenderUtil.drawRect(this.x, this.y, this.x + (float)this.width + 7.4f, this.y + (float)this.height - 0.5f, !this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515);
        Charon.fontManager.drawString(this.getName() + " " + (Object)ChatFormatting.GRAY + (this.setting.getValue() instanceof Float ? (Number)((Number)this.setting.getValue()) : (Number)((Number)this.setting.getValue()).doubleValue()), this.x + 2.3f, this.y - 2f - (float) Gui.getClickGui().getTextOffset(), -1);

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            this.setSettingFromX(mouseX);
        }
    }

    @Override
    public boolean isHovering(int mouseX, int mouseY) {
        for (Component component : Gui.getClickGui().getComponents()) {
            if (!component.drag) continue;
            return false;
        }
        return (float)mouseX >= this.getX() && (float)mouseX <= this.getX() + (float)this.getWidth() + 8.0f && (float)mouseY >= this.getY() && (float)mouseY <= this.getY() + (float)this.height;
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    private void dragSetting(int mouseX, int mouseY) {
        if (this.isHovering(mouseX, mouseY) && Mouse.isButtonDown((int)0)) {
            this.setSettingFromX(mouseX);
        }
    }

    @Override
    public int getHeight() {
        return 14;
    }

    private void setSettingFromX(int mouseX) {
        float percent = ((float)mouseX - this.x) / ((float)this.width + 7.4f);
        if (this.setting.getValue() instanceof Double) {
            double result = (Double)this.setting.getMin() + (double)((float)this.difference * percent);
            this.setting.setValue((double)Math.round(10.0 * result) / 10.0);
        } else if (this.setting.getValue() instanceof Float) {
            float result = ((Float)this.setting.getMin()).floatValue() + (float)this.difference * percent;
            this.setting.setValue(Float.valueOf((float)Math.round(10.0f * result) / 10.0f));
        } else if (this.setting.getValue() instanceof Integer) {
            this.setting.setValue((Integer)this.setting.getMin() + (int)((float)this.difference * percent));
        }
    }

    private float middle() {
        return this.max.floatValue() - this.min.floatValue();
    }

    private float part() {
        return ((Number)this.setting.getValue()).floatValue() - this.min.floatValue();
    }

    private float partialMultiplier() {
        return this.part() / this.middle();
    }
}

