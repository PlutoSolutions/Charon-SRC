/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.world.World
 */
package cc.zip.charon.client.modules.misc;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import cc.zip.charon.api.property.Setting;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

@ModuleManifest(label="FakePlayer", category=Module.Category.MISC)
public class FakePlayer
extends Module {
    private EntityOtherPlayerMP entityOtherPlayerMP = null;
    public final Setting<Boolean> update = this.register(new Setting<Boolean>("Update", true));

    @Override
    public void onEnable() {
        if (this.mc.player == null) {
            return;
        }
        this.entityOtherPlayerMP = new EntityOtherPlayerMP((World)this.mc.world, new GameProfile(UUID.fromString("cc72ff00-a113-48f4-be18-2dda8db52355"), "daily"));
        this.entityOtherPlayerMP.copyLocationAndAnglesFrom((Entity)this.mc.player);
        this.entityOtherPlayerMP.inventory = this.mc.player.inventory;
        this.mc.world.spawnEntity((Entity)this.entityOtherPlayerMP);
    }

    @Override
    public void onDisable() {
        if (this.entityOtherPlayerMP != null) {
            this.mc.world.removeEntity((Entity)this.entityOtherPlayerMP);
        }
    }
}

