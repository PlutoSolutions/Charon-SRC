/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package cc.zip.charon.api.util;

import java.awt.Color;
import org.lwjgl.opengl.GL11;

public class ColorUtil {
    public static final int FRIEND_COLOR = -11157267;
    public static int toRGBA(int r, int g, int b, int a) {
        return (r << 16) + (g << 8) + b + (a << 24);
    }
    public static int getRainbow(int speed, int offset, float s) {
        float hue = (System.currentTimeMillis() + (long)offset) % (long)speed;
        return Color.getHSBColor(hue / (float)speed, s, 1.0f).getRGB();
    }

    public static void color(int color) {
        GL11.glColor4f((float)((float)(color >> 16 & 0xFF) / 255.0f), (float)((float)(color >> 8 & 0xFF) / 255.0f), (float)((float)(color & 0xFF) / 255.0f), (float)((float)(color >> 24 & 0xFF) / 255.0f));
    }
}

