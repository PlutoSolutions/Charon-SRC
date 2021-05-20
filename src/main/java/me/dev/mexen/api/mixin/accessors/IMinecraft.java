/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Timer
 */
package me.dev.mexen.api.mixin.accessors;

import net.minecraft.util.Timer;

public interface IMinecraft {
    public void setRightClickDelayTimer(int var1);

    public Timer getTimer();
}

