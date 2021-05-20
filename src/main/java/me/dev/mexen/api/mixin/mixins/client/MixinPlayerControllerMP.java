/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 */
package me.dev.mexen.api.mixin.mixins.client;

import cc.zip.charon.Charon;
import me.dev.mexen.api.mixin.accessors.IPlayerControllerMP;
import cc.zip.charon.client.event.events.ClickBlockEvent;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={PlayerControllerMP.class})
public abstract class MixinPlayerControllerMP
implements IPlayerControllerMP {
    @Override
    @Accessor(value="isHittingBlock")
    public abstract void setIsHittingBlock(boolean var1);

    @Inject(method={"clickBlock"}, at={@At(value="HEAD")}, cancellable=true)
    public void clickBlock(BlockPos loc, EnumFacing face, CallbackInfoReturnable<Boolean> cir) {
        ClickBlockEvent event = new ClickBlockEvent(loc, face);
        Charon.INSTANCE.getBus().post(event);
        if (event.isCancelled()) {
            cir.cancel();
        }
    }
}

