/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.RenderItem
 *  net.minecraft.client.renderer.block.model.ItemCameraTransforms$TransformType
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.item.ItemStack
 *  org.lwjgl.opengl.GL11
 */
package me.dev.mexen.api.mixin.mixins.render;

import cc.zip.charon.client.modules.visual.EnchantColor;
import cc.zip.charon.client.modules.visual.ViewmodelChanger;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderItem.class})
public class MixinRenderItem {
    @ModifyArg(method={"renderEffect"}, at=@At(value="INVOKE", target="net/minecraft/client/renderer/RenderItem.renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V"))
    private int renderEffect(int glintVal) {
        EnchantColor enchantColor = EnchantColor.getINSTANCE();
        return enchantColor.isEnabled() ? enchantColor.getColor() : glintVal;
    }

    @Inject(method={"renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V"}, at={@At(value="INVOKE")})
    public void renderItem(ItemStack stack, EntityLivingBase entitylivingbaseIn, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo ci) {
        if (ViewmodelChanger.getInstance().isEnabled() && (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND || transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)) {
            ViewmodelChanger changer = ViewmodelChanger.getInstance();
            float size = changer.size.getValue().floatValue() / 10.0f;
            GL11.glScalef((float)size, (float)size, (float)size);
            if (transform.equals((Object)ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND)) {
                GL11.glTranslated((double)(changer.offhandX.getValue().floatValue() / 4.0f), (double)(changer.offhandY.getValue().floatValue() / 4.0f), (double)(changer.offhandZ.getValue().floatValue() / 4.0f));
            } else {
                GL11.glTranslated((double)(changer.offsetX.getValue().floatValue() / 4.0f), (double)(changer.offsetY.getValue().floatValue() / 4.0f), (double)(changer.offsetZ.getValue().floatValue() / 4.0f));
            }
        }
    }
}

