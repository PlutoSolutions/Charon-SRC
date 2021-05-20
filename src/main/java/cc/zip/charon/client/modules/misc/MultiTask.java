/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.item.EntityExpBottle
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.projectile.EntityArrow
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.RayTraceResult$Type
 */
package cc.zip.charon.client.modules.misc;

import cc.zip.charon.api.property.Setting;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;

@ModuleManifest(label="MultiTask", category=Module.Category.MISC)
public class MultiTask
extends Module {
    private final Setting<Boolean> entityHit = this.register(new Setting<Boolean>("Entities", true));
    private static MultiTask INSTANCE = new MultiTask();

    public MultiTask() {
        INSTANCE = this;
    }

    public static MultiTask getInstance() {
        return INSTANCE;
    }

    public void onMouse(int button) {
        if (button == 0 && this.entityHit.getValue().booleanValue() && this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY && !(this.mc.objectMouseOver.entityHit instanceof EntityItem) && !(this.mc.objectMouseOver.entityHit instanceof EntityExpBottle) && !(this.mc.objectMouseOver.entityHit instanceof EntityArrow) && this.mc.gameSettings.keyBindUseItem.isKeyDown() && this.mc.player.isHandActive() && this.mc.gameSettings.keyBindAttack.isKeyDown()) {
            this.mc.getConnection().sendPacket((Packet)new CPacketUseEntity(this.mc.objectMouseOver.entityHit));
            this.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }
}

