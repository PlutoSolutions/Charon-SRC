/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package cc.zip.charon.api.interfaces;

import cc.zip.charon.Charon;
import cc.zip.charon.client.managers.FontManager;
import net.minecraft.client.Minecraft;

public interface Minecraftable {
    public static final Minecraft mc = Minecraft.getMinecraft();

    public FontManager renderer = Charon.fontManager;
}

