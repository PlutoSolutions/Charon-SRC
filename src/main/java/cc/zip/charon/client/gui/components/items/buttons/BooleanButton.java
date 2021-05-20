/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.util.SoundEvent
 */
package cc.zip.charon.client.gui.components.items.buttons;

import cc.zip.charon.Charon;
import cc.zip.charon.api.interfaces.Minecraftable;
import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.RenderUtil;
import cc.zip.charon.client.gui.Gui;
import cc.zip.charon.client.managers.ColorManager;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

public class BooleanButton
extends Button {
    private final Setting setting;

    public BooleanButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Charon.fontManager.drawString(this.getName(), this.x + 2.3f, this.y - 1.7f - (float) Gui.getClickGui().getTextOffset(), this.getState() ? -1 : -1);
        //RenderUtil.drawGradientSideways(this.x - 1, (float)this.y + 13.2f, this.x + this.width + 1, this.y + (float)this.height - 4f, ColorManager.toRGBA(194,81,125,255), ColorManager.toRGBA(75,91,186,255));
        RenderUtil.drawRect(this.x + 75, this.y + 5, this.x + (float)this.width + 3.5f, this.y + (float)this.height - 2, ColorManager.toRGBA(102,102,102,255));

        //true test
        //RenderUtil.drawRect(this.x + 76, this.y + 4, this.x + (float)this.width - 4.7f, this.y + (float)this.height - 5f, ColorManager.toRGBA(153,255,105,255));
        //false test
        //RenderUtil.drawRect(this.x + 83, this.y + 4, this.x + (float)this.width + 2.5f, this.y + (float)this.height - 5f, ColorManager.toRGBA(255,255,105,255));

        //RenderUtil.drawRect(this.x + 85, this.y + 3, this.x + (float)this.width, this.y + (float)this.height - 4f, ColorManager.toRGBA(51,255,51,255));

        if (this.getState()) {
            //true
            RenderUtil.drawRect(this.x + 83, this.y + 6, this.x + (float)this.width + 2.5f, this.y + (float)this.height - 3f, ColorManager.toRGBA(102   ,255,51,200));
        }
        else {
            //false
            RenderUtil.drawRect(this.x + 76, this.y + 6, this.x + (float)this.width - 4.7f, this.y + (float)this.height - 3f, ColorManager.toRGBA(51,51,51,255));
        }
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            Minecraftable.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord((SoundEvent)SoundEvents.UI_BUTTON_CLICK, (float)1.0f));
        }
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
       this.setting.setValue((Boolean)this.setting.getValue() == false);
    }

    @Override
    public boolean getState() {
        return (Boolean)this.setting.getValue();
    }
}

