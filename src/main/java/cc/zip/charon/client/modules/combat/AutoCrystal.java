/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraft.network.play.server.SPacketSpawnObject
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.SoundCategory
 *  net.minecraft.util.math.BlockPos
 */
package cc.zip.charon.client.modules.combat;

import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cc.zip.charon.Charon;
import cc.zip.charon.api.util.CombatUtil;
import cc.zip.charon.api.util.EntityUtil;
import cc.zip.charon.api.util.ItemUtil;
import cc.zip.charon.client.event.events.TickEvent;
import me.dev.mexen.api.mixin.mixins.network.ICPacketUseEntity;
import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.BlockUtil;
import cc.zip.charon.api.util.RenderUtil;
import cc.zip.charon.api.util.Timer;
import cc.zip.charon.client.event.events.PacketEvent;
import cc.zip.charon.client.event.events.UpdateEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import tcb.bces.listener.Subscribe;

@ModuleManifest(label="AutoCrystal", category=Module.Category.COMBAT)
public class    AutoCrystal
extends Module {
    private final Setting<Page> page = this.register(new Setting<Page>("Page", Page.BREAK));
    public final Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(9.0f), Float.valueOf(1.0f), Float.valueOf(300.0f), v -> this.page.getValue() == Page.TARGET));
    private final Setting<Boolean> soundRemove = this.register(new Setting<Boolean>("Sound Remove", Boolean.valueOf(true), v -> this.page.getValue() == Page.MISC));
    public final Setting<Float> breakRange = this.register(new Setting<Float>("Break Range", Float.valueOf(5.0f), Float.valueOf(1.0f), Float.valueOf(6.0f), v -> this.page.getValue() == Page.BREAK));
    private final Setting<Float> breakWallRange = this.register(new Setting<Float>("Break Wall Range", Float.valueOf(5.0f), Float.valueOf(1.0f), Float.valueOf(6.0f), v -> this.page.getValue() == Page.BREAK));
    private final Setting<Integer> breakDelay = this.register(new Setting<Integer>("Break Delay", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(150), v -> this.page.getValue() == Page.BREAK));
    private final Setting<Boolean> instant = this.register(new Setting<Boolean>("Instant", Boolean.valueOf(true), v -> this.page.getValue() == Page.BREAK));
    private final Setting<Integer> instantDelay = this.register(new Setting<Integer>("Instant Delay", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(50), v -> this.instant.getValue() != false && this.page.getValue() == Page.BREAK));
    private final Setting<Boolean> place = this.register(new Setting<Boolean>("Place", Boolean.valueOf(true), v -> this.page.getValue() == Page.PLACE));
    private final Setting<Boolean> autoSwitch = this.register(new Setting<Boolean>("Auto Switch", Boolean.valueOf(false), v -> this.page.getValue() == Page.PLACE && this.place.getValue() != false));
    public final Setting<Float> placeRange = this.register(new Setting<Float>("Place Range", Float.valueOf(5.0f), Float.valueOf(1.0f), Float.valueOf(6.0f), v -> this.page.getValue() == Page.PLACE && this.place.getValue() != false));
    private final Setting<Integer> armorScale = this.register(new Setting<Integer>("Armor Scale", Integer.valueOf(10), Integer.valueOf(0), Integer.valueOf(100), v -> this.page.getValue() == Page.PLACE && this.place.getValue() != false));
    private final Setting<Float> facePlaceHp = this.register(new Setting<Float>("Faceplace HP", Float.valueOf(8.0f), Float.valueOf(0.0f), Float.valueOf(36.0f), v -> this.page.getValue() == Page.PLACE && this.place.getValue() != false));
    public final Setting<Float> minDamage = this.register(new Setting<Float>("Min. Damage", Float.valueOf(4.0f), Float.valueOf(1.0f), Float.valueOf(36.0f), v -> this.page.getValue() == Page.PLACE && this.place.getValue() != false));
    private final Setting<Float> maxSelfDamage = this.register(new Setting<Float>("Max Self Damage", Float.valueOf(8.0f), Float.valueOf(1.0f), Float.valueOf(36.0f), v -> this.page.getValue() == Page.PLACE && this.place.getValue() != false));
    private final Setting<Boolean> secondCheck = this.register(new Setting<Boolean>("Second Check", Boolean.valueOf(true), v -> this.page.getValue() == Page.PLACE));
    private final Setting<Boolean> onePointThirteen = this.register(new Setting<Boolean>("1.13", Boolean.valueOf(false), v -> this.page.getValue() == Page.PLACE));
    private final Setting<Boolean> sync = this.register(new Setting<Boolean>("Sync", Boolean.valueOf(true), v -> this.page.getValue() == Page.RENDER));
    private final Setting<Integer> red = this.register(new Setting<Integer>("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.sync.getValue() == false && this.page.getValue() == Page.RENDER));
    private final Setting<Integer> green = this.register(new Setting<Integer>("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.sync.getValue() == false && this.page.getValue() == Page.RENDER));
    private final Setting<Integer> blue = this.register(new Setting<Integer>("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.sync.getValue() == false && this.page.getValue() == Page.RENDER));
    private final Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", Integer.valueOf(40), Integer.valueOf(0), Integer.valueOf(255), v -> this.page.getValue() == Page.RENDER));
    private final Set<BlockPos> placeSet = new HashSet<BlockPos>();
    private final Timer clearTimer = new Timer();
    private final Timer breakTimer = new Timer();
    private int predictedId = -1;
    private BlockPos renderPos = null;
    private EntityPlayer currentTarget = null;
    private boolean offhand;
    private boolean mainhand;
    private boolean coolDown = false;
    private static AutoCrystal INSTANCE = new AutoCrystal();

    public AutoCrystal() {
        INSTANCE = this;
    }

    public static AutoCrystal getInstance() {
        return INSTANCE;
    }

    public void onTick(TickEvent event) {
        if (this.currentTarget != null && this.currentTarget.isDead && this.mc.player.getDistanceSq((Entity)this.currentTarget) < 10.0f) {
            this.mc.player.sendChatMessage("wtfff, cHaron owns you?");
            this.currentTarget = null;
        }
    }

    public void setTarget(EntityPlayer target) {
        this.currentTarget = target;
    }

    private boolean update() {
        if (this.isNull()) {
            return false;
        }
        if (this.clearTimer.hasReached(500L)) {
            this.placeSet.clear();
            this.predictedId = -1;
            this.coolDown = false;
            this.renderPos = null;
            this.clearSuffix();
            this.clearTimer.reset();
        }
        this.offhand = this.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
        this.mainhand = this.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL;
        return true;
    }

    @Override
    public void onToggle() {
        this.placeSet.clear();
        this.predictedId = -1;
        this.renderPos = null;
        this.clearSuffix();
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (!this.update()) {
            return;
        }
        this.currentTarget = CombatUtil.getTarget(this.range.getValue().floatValue());
        if (this.currentTarget == null) {
            return;
        }
        this.setSuffix(this.currentTarget.getName());
        this.doPlace();
        this.doBreak();
    }

    private void doPlace() {
        BlockPos placePos = null;
        float maxDamage = 0.5f;
        List<BlockPos> sphere = BlockUtil.getSphere(this.placeRange.getValue().floatValue(), true);
        int size = sphere.size();
        for (int i = 0; i < size; ++i) {
            float damage;
            BlockPos pos = sphere.get(i);
            if (!BlockUtil.canPlaceCrystal(pos, this.secondCheck.getValue(), this.onePointThirteen.getValue())) continue;
            float self = this.calculate(pos, (EntityPlayer)this.mc.player);
            if (!(EntityUtil.getHealth((EntityPlayer)this.mc.player) > self + 0.5f) || !(this.maxSelfDamage.getValue().floatValue() > self) || !((damage = this.calculate(pos, this.currentTarget)) > maxDamage) || !(damage > self) || !(damage > this.minDamage.getValue().floatValue()) && (!(this.facePlaceHp.getValue().floatValue() > EntityUtil.getHealth(this.currentTarget)) && !ItemUtil.isArmorLow(this.currentTarget, this.armorScale.getValue()) || !(damage > 2.0f))) continue;
            maxDamage = damage;
            placePos = pos;
        }
        if (!(this.offhand || this.mainhand || this.autoSwitch.getValue().booleanValue())) {
            this.renderPos = null;
            this.clearSuffix();
            return;
        }
        if (placePos != null) {
            if (this.autoSwitch.getValue().booleanValue()) {
                this.doSwitch();
            }
            this.mc.getConnection().sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(placePos, EnumFacing.UP, this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.5f, 0.5f, 0.5f));
            this.placeSet.add(placePos);
            this.renderPos = placePos;
            this.setSuffix(this.currentTarget.getName());
        } else {
            this.renderPos = null;
        }
    }

    private void doSwitch() {
        int slot = ItemUtil.getItemFromHotbar(Items.END_CRYSTAL);
        if (slot == -1 || this.coolDown) {
            return;
        }
        if (this.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE || this.mc.player.getHeldItemMainhand().getItem() == Items.GOLDEN_APPLE && this.mc.player.isHandActive()) {
            return;
        }
        this.mc.player.inventory.currentItem = slot;
        this.coolDown = true;
    }

    private void doBreak() {
        Entity entity = null;
        int size = this.mc.world.loadedEntityList.size();
        for (int i = 0; i < size; ++i) {
            float damage;
            Entity crystal = (Entity)this.mc.world.loadedEntityList.get(i);
            if (crystal.getClass() != EntityEnderCrystal.class || !this.isValid(crystal) || crystal.getEntityId() == this.predictedId) continue;
            float self = this.calculate(crystal, (EntityPlayer)this.mc.player);
            if (!(EntityUtil.getHealth((EntityPlayer)this.mc.player) > self + 0.5f) || !((damage = this.calculate(crystal, this.currentTarget)) > self) || !(damage > this.minDamage.getValue().floatValue()) && (!(this.facePlaceHp.getValue().floatValue() > EntityUtil.getHealth(this.currentTarget)) && !ItemUtil.isArmorLow(this.currentTarget, this.armorScale.getValue()) || !(damage > 2.0f))) continue;
            entity = crystal;
        }
        if (entity != null && this.breakTimer.hasReached(this.breakDelay.getValue().intValue())) {
            this.mc.getConnection().sendPacket((Packet)new CPacketUseEntity(entity));
            this.mc.player.swingArm(this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            this.breakTimer.reset();
        }
    }

    private boolean isValid(Entity crystal) {
        return (double)(this.mc.player.canEntityBeSeen(crystal) ? this.breakRange.getValue().floatValue() * this.breakRange.getValue().floatValue() : this.breakWallRange.getValue().floatValue() * this.breakWallRange.getValue().floatValue()) > this.mc.player.getDistanceSq(crystal);
    }

    private float calculate(Entity crystal, EntityPlayer target) {
        return EntityUtil.calculate(crystal.posX, crystal.posY, crystal.posZ, (EntityLivingBase)target);
    }

    private float calculate(BlockPos pos, EntityPlayer entity) {
        return EntityUtil.calculate((float)pos.getX() + 0.5f, pos.getY() + 1, (float)pos.getZ() + 0.5f, (EntityLivingBase)entity);
    }

    @Override
    public void onRender3D() {
        if (this.renderPos != null) {
            RenderUtil.drawBoxESP(this.renderPos, this.sync.getValue() != false ? Charon.INSTANCE.getColorManager().getColor() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()), 1.0f, true, true, this.alpha.getValue(), 1.0f);
        }
    }

    public void instantHit(int id) {
        ICPacketUseEntity hitPacket = (ICPacketUseEntity)new CPacketUseEntity();
        hitPacket.setEntityId(id);
        hitPacket.setAction(CPacketUseEntity.Action.ATTACK);
        this.mc.getConnection().sendPacket((Packet)hitPacket);
        this.predictedId = id;
        this.mc.player.swingArm(this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
    }

    @Subscribe
    public void onPacketReceive(PacketEvent.Receive event) {
        SPacketSpawnObject packet;
        if (event.getPacket() instanceof SPacketSpawnObject && this.instant.getValue().booleanValue()) {
            packet = (SPacketSpawnObject)event.getPacket();
            BlockPos pos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
            if (packet.getType() == 51 && this.placeSet.contains((Object)pos.down())) {
                if (this.mc.player.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()) > (double)this.breakRange.getValue().floatValue()) {
                    return;
                }
                if (this.instantDelay.getValue() != 0) {
                    try {
                        Thread.sleep(this.instantDelay.getValue().intValue());
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                this.instantHit(packet.getEntityID());
            }
        }
    }

    public static enum Page {
        PLACE,
        BREAK,
        TARGET,
        MISC,
        RENDER;

    }
}

