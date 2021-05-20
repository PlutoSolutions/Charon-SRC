/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 */
package cc.zip.charon.client.managers;

import cc.zip.charon.Charon;
import cc.zip.charon.api.interfaces.Minecraftable;
import cc.zip.charon.api.util.EntityUtil;
import cc.zip.charon.client.event.events.UpdateEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import tcb.bces.listener.IListener;
import tcb.bces.listener.Subscribe;

public class SafeManager
implements Minecraftable,
IListener {
    private boolean safe;

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (Minecraftable.mc.player == null || Minecraftable.mc.world == null) {
            return;
        }
        this.safe = true;
        float maxDamage = 0.5f;
        int size = Minecraftable.mc.world.loadedEntityList.size();
        for (int i = 0; i < size; ++i) {
            float damage;
            Entity entity = (Entity)Minecraftable.mc.world.loadedEntityList.get(i);
            if (!(entity instanceof EntityEnderCrystal) || Minecraftable.mc.player.getDistanceSq(entity) > 12.0f || (damage = EntityUtil.calculate(entity.posX, entity.posY, entity.posZ, (EntityLivingBase)Minecraftable.mc.player)) < maxDamage) continue;
            maxDamage = damage;
            if (damage + 0.5f < EntityUtil.getHealth((EntityPlayer)Minecraftable.mc.player)) continue;
            this.safe = false;
        }
    }

    public boolean isSafe() {
        return this.safe;
    }

    public void init() {
        Charon.INSTANCE.getBus().register(this);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

