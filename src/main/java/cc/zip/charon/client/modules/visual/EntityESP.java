/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityExpBottle
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.Vec3d
 */
package cc.zip.charon.client.modules.visual;

import java.awt.Color;

import cc.zip.charon.Charon;
import me.dev.mexen.api.mixin.mixins.render.IRenderManager;
import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.RenderUtil;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

@ModuleManifest(label="EntityESP", category= Module.Category.VISUAL, listen=false)
public class EntityESP
extends Module {
    public final Setting<Boolean> bottles = this.register(new Setting<Boolean>("XPBottles", true));
    public final Setting<Boolean> sync = this.register(new Setting<Boolean>("Sync", true));
    public final Setting<Integer> red = this.register(new Setting<Object>("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.sync.getValue() == false));
    public final Setting<Integer> green = this.register(new Setting<Object>("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.sync.getValue() == false));
    public final Setting<Integer> blue = this.register(new Setting<Object>("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.sync.getValue() == false));
    private static EntityESP INSTANCE = new EntityESP();

    public EntityESP() {
        INSTANCE = this;
    }

    public static EntityESP getInstance() {
        return INSTANCE;
    }

    public int getColor() {
        return this.sync.getValue() != false ? Charon.INSTANCE.getColorManager().getColorAsInt() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()).getRGB();
    }

    @Override
    public void onRender3D() {
        int size = this.mc.world.loadedEntityList.size();
        for (int i = 0; i < size; ++i) {
            Entity entity = (Entity)this.mc.world.loadedEntityList.get(i);
            if (!(entity instanceof EntityExpBottle) || !this.bottles.getValue().booleanValue()) continue;
            Vec3d vec = this.interpolateEntity(entity, this.mc.getRenderPartialTicks());
            double posX = vec.x - ((IRenderManager)this.mc.getRenderManager()).getRenderPosX();
            double posY = vec.y - ((IRenderManager)this.mc.getRenderManager()).getRenderPosY();
            double posZ = vec.z - ((IRenderManager)this.mc.getRenderManager()).getRenderPosZ();
            AxisAlignedBB bb = new AxisAlignedBB(0.0, 0.0, 0.0, (double)entity.width, (double)entity.height, (double)entity.width).offset(posX - (double)(entity.width / 2.0f), posY, posZ - (double)(entity.width / 2.0f)).grow(0.125);
            Color color = this.sync.getValue() != false ? Charon.INSTANCE.getColorManager().getColor() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue());
            RenderUtil.drawBoundingBox(bb, color, 2.0f);
        }
    }

    private Vec3d interpolateEntity(Entity entity, float time) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)time, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)time, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)time);
    }
}

