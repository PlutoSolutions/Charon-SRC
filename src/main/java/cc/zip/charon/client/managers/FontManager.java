/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.managers;

import java.awt.Font;

import cc.zip.charon.api.interfaces.Minecraftable;
import cc.zip.charon.api.util.font.CFontRenderer;
import net.minecraft.util.math.MathHelper;

public class FontManager
implements Minecraftable {
    public CFontRenderer fontRenderer = new CFontRenderer(new Font("Verdana", 0, 18), true, true);
    private boolean customFont = false;
    public int scaledWidth;
    public int scaledHeight;
    public int scaleFactor;
    public void setCustomFont(boolean bool) {
        this.customFont = bool;
    }

    public int drawString(String text, float x, float y, int color) {
        if (this.customFont) {
            return (int)this.fontRenderer.drawStringWithShadow(text, x, y, color);
        }
        return Minecraftable.mc.fontRenderer.drawStringWithShadow(text, x, y, color);
    }

    public int drawCenteredString(String text, float x, float y, int color, boolean shadow) {
        if (this.customFont) {
            return (int)this.fontRenderer.drawStringWithShadow(text, x - (float)this.getStringWidth(text) / 2.0f, y, color);
        }
        return Minecraftable.mc.fontRenderer.drawString(text, x - (float)this.getStringWidth(text) / 2.0f, y, color, shadow);
    }

    public int drawStringNoShadow(String text, float x, float y, int color) {
        if (this.customFont) {
            return (int)this.fontRenderer.drawString(text, x, y, color, false);
        }
        return Minecraftable.mc.fontRenderer.drawString(text, x, y, color, false);
    }

    public int getStringHeight() {
        if (this.customFont) {
            return this.fontRenderer.getStringHeight("A");
        }
        return 9;
    }

    public int getStringWidth(String text) {
        if (this.customFont) {
            return this.fontRenderer.getStringWidth(text);
        }
        return Minecraftable.mc.fontRenderer.getStringWidth(text);
    }
    public void updateResolution() {
        this.scaledWidth = mc.displayWidth;
        this.scaledHeight = mc.displayHeight;
        this.scaleFactor = 1;
        boolean flag = mc.isUnicode();
        int i = mc.gameSettings.guiScale;
        if (i == 0) {
            i = 1000;
        }
        while (this.scaleFactor < i && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }
        if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }
        double scaledWidthD = (double) this.scaledWidth / (double) this.scaleFactor;
        double scaledHeightD = (double) this.scaledHeight / (double) this.scaleFactor;
        this.scaledWidth = MathHelper.ceil(scaledWidthD);
        this.scaledHeight = MathHelper.ceil(scaledHeightD);
    }

}

