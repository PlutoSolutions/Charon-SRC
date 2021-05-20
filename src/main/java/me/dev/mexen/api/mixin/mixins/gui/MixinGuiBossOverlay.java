/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiBossOverlay
 */
package me.dev.mexen.api.mixin.mixins.gui;

import cc.zip.charon.client.modules.visual.NoRender;
import net.minecraft.client.gui.GuiBossOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiBossOverlay.class})
public class MixinGuiBossOverlay {
    @Inject(method={"renderBossHealth"}, at={@At(value="HEAD")}, cancellable=true)
    public void render(CallbackInfo ci) {
        if (NoRender.getInstance().isEnabled() && NoRender.getInstance().noBossOverlay.getValue().booleanValue()) {
            ci.cancel();
        }
    }
}

