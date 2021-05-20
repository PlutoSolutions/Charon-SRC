/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.entity.RenderEnderCrystal
 *  net.minecraft.entity.Entity
 *  org.lwjgl.opengl.GL11
 */
package me.dev.mexen.api.mixin.mixins.render;

import java.awt.Color;

import cc.zip.charon.Charon;
import cc.zip.charon.api.util.ColorUtil;
import cc.zip.charon.client.modules.visual.Chams;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={RenderEnderCrystal.class})
public class MixinRenderEnderCrystal {
    @Redirect(method={"doRender"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void renderModelBaseHook(ModelBase model, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        Chams crystalScale = Chams.INSTANCE;
        if (crystalScale.isEnabled() && (crystalScale.crystalWireframe.getValue().booleanValue() || crystalScale.crystalChams.getValue().booleanValue())) {
            GlStateManager.scale((float)crystalScale.scale.getValue().floatValue(), (float)crystalScale.scale.getValue().floatValue(), (float)crystalScale.scale.getValue().floatValue());
            if (crystalScale.crystalWireframe.getValue().booleanValue()) {
                crystalScale.onRenderModel(model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            }
            if (crystalScale.crystalChams.getValue().booleanValue()) {
                GL11.glPushAttrib((int)1048575);
                GL11.glDisable((int)3008);
                GL11.glDisable((int)3553);
                GL11.glDisable((int)2896);
                GL11.glEnable((int)3042);
                GL11.glBlendFunc((int)770, (int)771);
                GL11.glLineWidth((float)1.5f);
                GL11.glEnable((int)2960);
                Color color = crystalScale.rainbow.getValue() != false ? new Color(ColorUtil.getRainbow(crystalScale.speed.getValue() * 100, 0, (float)crystalScale.saturation.getValue().intValue() / 100.0f)) : (crystalScale.colorSync.getValue() != false ? Charon.INSTANCE.getColorManager().getColor() : new Color(crystalScale.red.getValue(), crystalScale.green.getValue(), crystalScale.blue.getValue()));
                GL11.glEnable((int)10754);
                GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)crystalScale.crystalAlpha.getValue().intValue() / 255.0f));
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GL11.glEnable((int)3042);
                GL11.glEnable((int)2896);
                GL11.glEnable((int)3553);
                GL11.glEnable((int)3008);
                GL11.glPopAttrib();
            }
        } else {
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
        if (crystalScale.isEnabled()) {
            GlStateManager.scale((float)(1.0f / crystalScale.scale.getValue().floatValue()), (float)(1.0f / crystalScale.scale.getValue().floatValue()), (float)(1.0f / crystalScale.scale.getValue().floatValue()));
        }
    }

    public Color getColor(Entity entity, int red, int green, int blue, int alpha, boolean colorFriends) {
        Color color = new Color((float)red / 255.0f, (float)green / 255.0f, (float)blue / 255.0f, (float)alpha / 255.0f);
        return color;
    }
}

