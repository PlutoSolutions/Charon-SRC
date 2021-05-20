/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.MoverType
 *  net.minecraft.util.math.BlockPos
 */
package me.dev.mexen.api.mixin.mixins.entity;

import cc.zip.charon.Charon;
import cc.zip.charon.client.event.events.MoveEvent;
import cc.zip.charon.client.event.events.PushEvent;
import cc.zip.charon.client.event.events.UpdateEvent;
import cc.zip.charon.client.modules.combat.Burrow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={EntityPlayerSP.class})
public abstract class MixinEntityPlayerSP
extends MixinEntityPlayer {
    @Shadow
    protected Minecraft field_71159_c;

    @Override
    public void func_70091_d(MoverType type, double x, double y, double z) {
        MoveEvent event = new MoveEvent(x, y, z);
        Charon.INSTANCE.getBus().post(event);
        super.func_70091_d(type, event.getMotionX(), event.getMotionY(), event.getMotionZ());
    }

    @Inject(method={"onUpdate"}, at={@At(value="HEAD")})
    public void onUpdatePre(CallbackInfo ci) {
        Charon.INSTANCE.getBus().post(new UpdateEvent(0));
    }

    @Inject(method={"onUpdate"}, at={@At(value="RETURN")})
    public void onUpdatePost(CallbackInfo ci) {
        Charon.INSTANCE.getBus().post(new UpdateEvent(1));
    }

    @Inject(method={"pushOutOfBlocks"}, at={@At(value="HEAD")}, cancellable=true)
    public void push(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (new BlockPos(this.field_71159_c.player.getPositionVector()).equals((Object) Burrow.getInstance().startPos)) {
            cir.cancel();
            cir.setReturnValue(false);
        }
    }
    @Inject(method={"pushOutOfBlocks"}, at={@At(value="HEAD")}, cancellable=true)
    private void pushOutOfBlocksHook(double x, double y, double z, CallbackInfoReturnable<Boolean> info) {
        PushEvent event = new PushEvent(1);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCanceled()) {
            info.setReturnValue(false);
        }
    }
}

