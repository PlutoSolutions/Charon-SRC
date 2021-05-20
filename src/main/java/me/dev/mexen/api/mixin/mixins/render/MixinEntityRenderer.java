/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.renderer.EntityRenderer
 */
package me.dev.mexen.api.mixin.mixins.render;

import cc.zip.charon.Charon;
import cc.zip.charon.client.modules.player.CameraClip;
import cc.zip.charon.client.modules.visual.Nametags;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={EntityRenderer.class})
public abstract class MixinEntityRenderer {
    @Inject(method={"updateCameraAndRender"}, at={@At(value="INVOKE", target="net/minecraft/client/gui/GuiIngame.renderGameOverlay(F)V")})
    public void onRender2D(float partialTicks, long nanoTime, CallbackInfo ci) {
        Charon.INSTANCE.getModuleManager().onRender2D();
    }

    @Inject(method={"renderWorldPass"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/renderer/EntityRenderer;renderHand(FI)V")})
    public void onRender3D(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
        Charon.INSTANCE.getModuleManager().onRender3D();
    }

    @Inject(method={"drawNameplate"}, at={@At(value="HEAD")}, cancellable=true)
    private static void renderName(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking, CallbackInfo ci) {
        if (Nametags.getInstance().isEnabled()) {
            ci.cancel();
        }
    }
}

