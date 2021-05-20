/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.MoverType
 */
package me.dev.mexen.api.mixin.mixins.entity;

import cc.zip.charon.api.interfaces.Minecraftable;
import cc.zip.charon.client.event.events.PushEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={Entity.class})
public class MixinEntity
implements Minecraftable {
    @Shadow
    public void func_70091_d(MoverType type, double x, double y, double z) {
    }

}

