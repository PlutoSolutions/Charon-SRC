/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.util.Timer
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.Display
 */
package me.dev.mexen.api.mixin.mixins.client;

import cc.zip.charon.Charon;
import me.dev.mexen.api.mixin.accessors.IMinecraft;
import cc.zip.charon.client.event.events.KeyEvent;
import cc.zip.charon.client.event.events.TickEvent;
import cc.zip.charon.client.modules.client.Manage;
import cc.zip.charon.client.modules.client.MiddleClick;
import cc.zip.charon.client.modules.misc.MultiTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.Timer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Minecraft.class}, priority=1001)
public abstract class MixinMinecraft
implements IMinecraft {
    @Override
    @Accessor(value="timer")
    public abstract Timer getTimer();

    @Override
    @Accessor
    public abstract void setRightClickDelayTimer(int var1);

    public boolean isHittingBlock(PlayerControllerMP playerControllerMP) {
        if (MultiTask.getInstance().isEnabled()) {
            return false;
        }
        return playerControllerMP.getIsHittingBlock();
    }

    public boolean isHandActive(EntityPlayerSP entityPlayerSP) {
        if (MultiTask.getInstance().isEnabled()) {
            return false;
        }
        return entityPlayerSP.isHandActive();
    }

    @Inject(method={"runTickKeyboard"}, at={@At(value="INVOKE", target="Lorg/lwjgl/input/Keyboard;getEventKey()I", ordinal=0, shift=At.Shift.BEFORE)})
    private void onKeyboard(CallbackInfo callbackInfo) {
        if (Keyboard.getEventKeyState()) {
            Charon.INSTANCE.getBus().post(new KeyEvent(Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey()));
        }
    }

    @Inject(method={"runTickMouse"}, at={@At(value="INVOKE", target="Lorg/lwjgl/input/Mouse;getEventButton()I", ordinal=0, shift=At.Shift.BEFORE)})
    private void mouseClick(CallbackInfo ci) {
        if (Mouse.getEventButtonState()) {
            MiddleClick.getInstance().run(Mouse.getEventButton());
            MultiTask.getInstance().onMouse(Mouse.getEventButton());
        }
    }

    @Inject(method={"getLimitFramerate"}, at={@At(value="HEAD")}, cancellable=true)
    public void limitFps(CallbackInfoReturnable<Integer> cir) {
        if (Manage.getInstance().unfocusedLimit.getValue().booleanValue() && !Display.isActive()) {
            try {
                cir.setReturnValue(Manage.getInstance().unfocusedFPS.getValue());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Inject(method={"init"}, at={@At(value="RETURN")})
    public void init(CallbackInfo ci) throws Exception {
        Charon.INSTANCE.init();
        Charon.INSTANCE.webhookchecker();
    }

    @Inject(method={"shutdown"}, at={@At(value="HEAD")})
    public void shutdown(CallbackInfo ci) {
        Charon.INSTANCE.getConfigManager().saveConfig(Charon.INSTANCE.getConfigManager().config.replaceFirst("Charon/", ""));
        Charon.INSTANCE.getFriendManager().unload();
    }

    @Inject(method={"runTick"}, at={@At(value="HEAD")})
    public void onTick(CallbackInfo ci) {
        Charon.INSTANCE.getBus().post(new TickEvent(0));
    }

    @Inject(method={"runTick"}, at={@At(value="RETURN")})
    public void onTickPost(CallbackInfo ci) {
        Charon.INSTANCE.getBus().post(new TickEvent(1));
    }
}

