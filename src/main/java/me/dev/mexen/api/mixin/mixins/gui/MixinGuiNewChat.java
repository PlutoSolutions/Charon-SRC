/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiNewChat
 */
package me.dev.mexen.api.mixin.mixins.gui;

import cc.zip.charon.Charon;
import cc.zip.charon.client.modules.client.Manage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={GuiNewChat.class})
public class MixinGuiNewChat {
    @Final
    @Shadow
    private Minecraft field_146247_f;

    @Redirect(method={"drawChat"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"))
    public void drawChatHook1(int left, int top, int right, int bottom, int color) {
        if (!Manage.getInstance().chatTweaks.getValue().booleanValue() || !Manage.getInstance().giantBeetleSoundsLikeAJackhammer.getValue().booleanValue()) {
            Gui.drawRect((int)left, (int)top, (int)right, (int)bottom, (int)color);
        }
    }

    @Redirect(method={"drawChat"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    public int drawChatHook2(FontRenderer fontRenderer, String text, float x, float y, int color) {
        if (Manage.getInstance().chatTweaks.getValue().booleanValue() && Manage.getInstance().customFont.getValue().booleanValue()) {
            return Charon.fontManager.drawString(text, x, y, color);
        }
        return this.field_146247_f.fontRenderer.drawStringWithShadow(text, x, y, color);
    }
}

