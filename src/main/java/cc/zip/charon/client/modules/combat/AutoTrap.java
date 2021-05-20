/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package cc.zip.charon.client.modules.combat;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.zip.charon.api.util.EntityUtil;
import cc.zip.charon.api.util.ItemUtil;
import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.BlockUtil;
import cc.zip.charon.api.util.MessageUtil;
import cc.zip.charon.api.util.Timer;
import cc.zip.charon.client.event.events.UpdateEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import tcb.bces.listener.Subscribe;

@ModuleManifest(label="AutoTrap", category=Module.Category.COMBAT)
public class AutoTrap
extends Module {
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay/Place", 50, 0, 250));
    private final Setting<Integer> blocksPerPlace = this.register(new Setting<Integer>("Block/Place", 8, 1, 30));
    private final Setting<Double> targetRange = this.register(new Setting<Double>("Target Range", 10.0, 0.0, 20.0));
    private final Setting<Double> range = this.register(new Setting<Double>("Place Range", 6.0, 0.0, 10.0));
    private final Setting<TargetMode> targetMode = this.register(new Setting<TargetMode>("Target", TargetMode.CLOSEST));
    private final Setting<Pattern> pattern = this.register(new Setting<Pattern>("Pattern", Pattern.STATIC));
    private final Setting<Integer> extend = this.register(new Setting<Object>("Extend", 4, 1, 4, v -> this.pattern.getValue() != Pattern.STATIC, "Extending the Trap."));
    private final Setting<Boolean> antiScaffold = this.register(new Setting<Boolean>("Anti Scaffold", false));
    private final Setting<Boolean> antiStep = this.register(new Setting<Boolean>("Anti Step", false));
    private final Setting<Boolean> platform = this.register(new Setting<Object>("Platform", Boolean.valueOf(false), v -> this.pattern.getValue() != Pattern.OPEN));
    private final Setting<Boolean> antiDrop = this.register(new Setting<Boolean>("Anti Drop", false));
    private final Setting<Boolean> antiSelf = this.register(new Setting<Boolean>("Anti Self", false));
    private final Setting<Boolean> retry = this.register(new Setting<Boolean>("Retry", false));
    private final Setting<Integer> retryer = this.register(new Setting<Object>("Retries", Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(15), v -> this.retry.getValue()));
    private final Timer timer = new Timer();
    private boolean didPlace = false;
    public EntityPlayer target;
    private int lastHotbarSlot;
    private int placements = 0;
    public static boolean isPlacing = false;
    private final Map<BlockPos, Integer> retries = new HashMap<BlockPos, Integer>();
    private final Timer retryTimer = new Timer();

    @Override
    public void onEnable() {
        if (this.isNull()) {
            this.disable();
            return;
        }
        this.lastHotbarSlot = this.mc.player.inventory.currentItem;
        this.retries.clear();
    }

    @Override
    public void onDisable() {
        isPlacing = false;
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (this.check()) {
            return;
        }
        switch (this.pattern.getValue()) {
            case STATIC: {
                this.placeList(BlockUtil.targets(this.target.getPositionVector(), this.antiScaffold.getValue(), this.antiStep.getValue(), true, this.platform.getValue(), this.antiDrop.getValue(), false));
                break;
            }
            case SMART: 
            case OPEN: {
                this.placeList(BlockUtil.getUntrappedBlocksExtended(this.extend.getValue(), this.target, this.antiScaffold.getValue(), this.antiStep.getValue(), true, this.platform.getValue(), this.antiDrop.getValue(), false));
                break;
            }
        }
        if (this.didPlace) {
            this.timer.reset();
        }
    }

    private void placeList(List<Vec3d> list) {
        list.sort((vec3d, vec3d2) -> Double.compare(this.mc.player.getDistanceSq(vec3d2.x, vec3d2.y, vec3d2.z), this.mc.player.getDistanceSq(vec3d.x, vec3d.y, vec3d.z)));
        list.sort(Comparator.comparingDouble(vec3d -> vec3d.y));
        this.lastHotbarSlot = this.mc.player.inventory.currentItem;
        int obbySlot = ItemUtil.getItemFromHotbar(Item.getItemFromBlock((Block)Blocks.OBSIDIAN));
        this.mc.getConnection().sendPacket((Packet)new CPacketHeldItemChange(obbySlot));
        int size = list.size();
        for (int i = 0; i < size; ++i) {
            Vec3d vec3d3 = list.get(i);
            BlockPos position = new BlockPos(vec3d3);
            int placeability = BlockUtil.isPositionPlaceable(position, true);
            if (this.retry.getValue().booleanValue() && placeability == 1 && (this.retries.get((Object)position) == null || this.retries.get((Object)position) < this.retryer.getValue())) {
                this.placeBlock(position);
                this.retries.put(position, this.retries.get((Object)position) == null ? 1 : this.retries.get((Object)position) + 1);
                this.retryTimer.reset();
                continue;
            }
            if (placeability != 3 || this.antiSelf.getValue().booleanValue() && BlockUtil.areVec3dsAligned(this.mc.player.getPositionVector(), vec3d3)) continue;
            this.placeBlock(position);
        }
        this.mc.getConnection().sendPacket((Packet)new CPacketHeldItemChange(this.lastHotbarSlot));
    }

    private boolean check() {
        isPlacing = false;
        this.didPlace = false;
        this.placements = 0;
        int obbySlot = ItemUtil.getItemFromHotbar(Item.getItemFromBlock((Block)Blocks.OBSIDIAN));
        if (!this.isEnabled()) {
            return true;
        }
        if (this.retryTimer.hasReached(2000L)) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        if (obbySlot == -1) {
            MessageUtil.sendClientMessage("<AutoTrap> No obsidian, toggling.", -3232);
            this.disable();
            return true;
        }
        if (this.mc.player.inventory.currentItem != this.lastHotbarSlot && this.mc.player.inventory.currentItem != obbySlot) {
            this.lastHotbarSlot = this.mc.player.inventory.currentItem;
        }
        this.target = this.getTarget(this.targetRange.getValue(), this.targetMode.getValue() == TargetMode.UNTRAPPED);
        return this.target == null || !this.timer.hasReached(this.delay.getValue().intValue());
    }

    private EntityPlayer getTarget(double range, boolean trapped) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        int size = this.mc.world.playerEntities.size();
        for (int i = 0; i < size; ++i) {
            EntityPlayer player = (EntityPlayer)this.mc.world.playerEntities.get(i);
            if (EntityUtil.isntValid(player, range) || this.pattern.getValue() == Pattern.STATIC && trapped && BlockUtil.isTrapped(player, this.antiScaffold.getValue(), this.antiStep.getValue(), true, this.platform.getValue(), this.antiDrop.getValue()) || this.pattern.getValue() != Pattern.STATIC && trapped && BlockUtil.isTrappedExtended(this.extend.getValue(), player, this.antiScaffold.getValue(), this.antiStep.getValue(), true, this.platform.getValue(), this.antiDrop.getValue(), false) || BlockUtil.getRoundedBlockPos((Entity)this.mc.player).equals((Object)BlockUtil.getRoundedBlockPos((Entity)player)) && this.antiSelf.getValue().booleanValue()) continue;
            if (target == null) {
                target = player;
                distance = this.mc.player.getDistanceSq((Entity)player);
                continue;
            }
            if (!(this.mc.player.getDistanceSq((Entity)player) < distance)) continue;
            target = player;
            distance = this.mc.player.getDistanceSq((Entity)player);
        }
        return target;
    }

    private void placeBlock(BlockPos pos) {
        if (this.placements < this.blocksPerPlace.getValue() && this.mc.player.getDistanceSq(pos) <= this.range.getValue() * this.range.getValue()) {
            isPlacing = true;
            BlockUtil.placeBlock(pos);
            this.didPlace = true;
            ++this.placements;
        }
    }

    public static enum TargetMode {
        CLOSEST,
        UNTRAPPED;

    }

    public static enum Pattern {
        STATIC,
        SMART,
        OPEN;

    }
}

