/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.client.renderer.entity.RenderLivingBase
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package me.dev.mexen.api.mixin.mixins.render;

import java.awt.Color;
import javax.annotation.Nullable;

import cc.zip.charon.Charon;
import cc.zip.charon.api.util.ColorUtil;
import cc.zip.charon.client.modules.visual.Chams;
import cc.zip.charon.client.modules.visual.Skeleton;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderLivingBase.class})
public class MixinRenderLivingBase<T extends EntityLivingBase>
extends Render<T> {
    @Shadow
    protected ModelBase field_77045_g;

    @Inject(method={"renderModel"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, CallbackInfo ci) {
        Chams chams = Chams.INSTANCE;
        if (entitylivingbaseIn instanceof EntityPlayer && chams.isEnabled() && chams.chams.getValue().booleanValue()) {
            GL11.glPushAttrib((int)1048575);
            GL11.glDisable((int)3008);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2896);
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glLineWidth((float)1.5f);
            GL11.glEnable((int)2960);
            Color color = chams.rainbow.getValue() != false ? new Color(ColorUtil.getRainbow(chams.speed.getValue() * 100, 0, (float)chams.saturation.getValue().intValue() / 100.0f)) : (chams.colorSync.getValue() != false ? Charon.INSTANCE.getColorManager().getColor() : new Color(chams.red.getValue(), chams.green.getValue(), chams.blue.getValue()));
            GL11.glEnable((int)10754);
            if (Charon.INSTANCE.getFriendManager().isFriend(entitylivingbaseIn.getName())) {
                color = new Color(-11157267);
            }
            GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)chams.playerAlpha.getValue().intValue() / 255.0f));
            this.field_77045_g.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            GL11.glEnable((int)3042);
            GL11.glEnable((int)2896);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)3008);
            GL11.glPopAttrib();
            ci.cancel();
        }
    }

    @Inject(method={"renderLayers"}, at={@At(value="RETURN")})
    public void renderLayers(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scaleIn, CallbackInfo ci) {
        if (Skeleton.getInstance().isEnabled()) {
            Skeleton.getInstance().onRenderModel(this.field_77045_g, (Entity)entitylivingbaseIn);
        }
        Chams chams = Chams.INSTANCE;
        if (entitylivingbaseIn instanceof EntityPlayer && chams.isEnabled() && chams.wireframe.getValue().booleanValue()) {
            chams.onRenderModel(this.field_77045_g, (Entity)entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleIn);
        }
    }

    protected MixinRenderLivingBase(RenderManager renderManager) {
        super(renderManager);
    }

    @Nullable
    protected ResourceLocation getEntityTexture(T entity) {
        return null;
    }
}

