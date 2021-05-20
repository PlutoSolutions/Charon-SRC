/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.EntityRenderer
 */
package me.dev.mexen.api.mixin.mixins.render;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={EntityRenderer.class})
public interface IEntityRenderer {
    @Accessor(value="drawBlockOutline")
    public void setDrawBlockOutline(boolean var1);

    @Invoker(value="orientCamera")
    public void invokeOrientCamera(float var1);
}

