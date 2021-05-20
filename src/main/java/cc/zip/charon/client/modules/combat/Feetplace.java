/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package cc.zip.charon.client.modules.combat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cc.zip.charon.api.util.*;
import cc.zip.charon.api.property.Setting;
import cc.zip.charon.client.event.events.UpdateEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import tcb.bces.listener.Subscribe;

@ModuleManifest(label="Feetplace", category=Module.Category.COMBAT)
public class Feetplace
extends Module {
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay/Place", 50, 0, 250));
    private final Setting<Integer> blocksPerTick = this.register(new Setting<Integer>("Block/Place", 8, 1, 20));
    private final Setting<Boolean> helpingBlocks = this.register(new Setting<Boolean>("HelpingBlocks", true));
    private final Setting<Boolean> intelligent = this.register(new Setting<Boolean>("Intelligent", Boolean.valueOf(false), v -> this.helpingBlocks.getValue()));
    private final Setting<Boolean> alwaysHelp = this.register(new Setting<Boolean>("Always Help", false));
    private final Setting<Integer> extender = this.register(new Setting<Integer>("Extend", 1, 1, 4));
    private final Setting<Boolean> floor = this.register(new Setting<Boolean>("Floor", false));
    private final Setting<Boolean> echests = this.register(new Setting<Boolean>("Use EChests", false));
    private final Setting<Boolean> info = this.register(new Setting<Boolean>("Info", false));
    private final Setting<Boolean> center = this.register(new Setting<Boolean>("TP-Center", false));
    private final Setting<Integer> retryer = this.register(new Setting<Integer>("Retries", 4, 1, 15));
    public static boolean isPlacing = false;
    private final Timer timer = new Timer();
    private final Timer retryTimer = new Timer();
    private final Map<BlockPos, Integer> retries = new HashMap<BlockPos, Integer>();
    private final Set<Vec3d> extendingBlocks = new HashSet<Vec3d>();
    private boolean didPlace = false;
    private int lastHotbarSlot;
    private int placements = 0;
    private int extenders = 1;
    private double enableYPos = -1.0;
    private BlockPos startPos;

    @Override
    public void onEnable() {
        if (this.isNull()) {
            this.disable();
            return;
        }
        this.lastHotbarSlot = this.mc.player.inventory.currentItem;
        this.enableYPos = (float)this.mc.player.posY;
        this.retries.clear();
        this.retryTimer.reset();
        this.startPos = EntityUtil.getRoundedBlockPos(mc.player);

        if (this.center.getValue().booleanValue()) {
            mc.player.setPosition((double) this.startPos.getX() + 0.5, this.startPos.getY(), (double) this.startPos.getZ() + 0.5);
        }
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
        boolean onEChest = this.mc.world.getBlockState(new BlockPos(this.mc.player.getPositionVector())).getBlock() == Blocks.ENDER_CHEST;
        if (!BlockUtil.isSafe((Entity)this.mc.player, onEChest ? 1 : 0, this.floor.getValue())) {
            this.placeBlocks(this.mc.player.getPositionVector(), BlockUtil.getUnsafeBlockArray(this.mc.player.getPositionVector(), onEChest ? 1 : 0, this.floor.getValue()), this.helpingBlocks.getValue(), false, false);
        } else if (!BlockUtil.isSafe((Entity)this.mc.player, onEChest ? 0 : -1, false) && this.alwaysHelp.getValue().booleanValue()) {
            this.placeBlocks(this.mc.player.getPositionVector(), BlockUtil.getUnsafeBlockArray(this.mc.player.getPositionVector(), onEChest ? 0 : -1, false), false, false, true);
        }
        this.processExtendingBlocks();
        if (this.didPlace) {
            this.timer.reset();
        }
    }

    private void processExtendingBlocks() {
        if (this.extendingBlocks.size() == 2 && this.extenders < this.extender.getValue()) {
            Vec3d[] array = new Vec3d[2];
            int i = 0;
            Iterator<Vec3d> iterator = this.extendingBlocks.iterator();
            while (iterator.hasNext()) {
                Vec3d vec3d;
                array[i] = vec3d = iterator.next();
                ++i;
            }
            int placementsBefore = this.placements;
            if (this.areClose(array) != null) {
                this.placeBlocks(this.areClose(array), BlockUtil.getUnsafeBlockArray(this.areClose(array), 0, this.floor.getValue()), this.helpingBlocks.getValue(), false, true);
            }
            if (placementsBefore < this.placements) {
                this.extendingBlocks.clear();
            }
        } else if (this.extendingBlocks.size() > 2 || this.extenders >= this.extender.getValue()) {
            this.extendingBlocks.clear();
        }
    }

    private Vec3d areClose(Vec3d[] vec3ds) {
        int matches = 0;
        for (Vec3d vec3d : vec3ds) {
            for (Vec3d pos : BlockUtil.getUnsafeBlockArray(this.mc.player.getPositionVector(), 0, this.floor.getValue())) {
                if (!vec3d.equals((Object)pos)) continue;
                ++matches;
            }
        }
        if (matches == 2) {
            return this.mc.player.getPositionVector().add(vec3ds[0].add(vec3ds[1]));
        }
        return null;
    }

    private boolean placeBlocks(Vec3d pos, Vec3d[] vec3ds, boolean hasHelpingBlocks, boolean isHelping, boolean isExtending) {
        int helpings = 0;
        int lastSlot = this.mc.player.inventory.currentItem;
        int obbySlot = ItemUtil.getItemFromHotbar(Item.getItemFromBlock((Block)Blocks.OBSIDIAN));
        this.mc.getConnection().sendPacket((Packet)new CPacketHeldItemChange(obbySlot));
        block6: for (Vec3d vec3d : vec3ds) {
            boolean gotHelp = true;
            if (isHelping && !this.intelligent.getValue().booleanValue() && ++helpings > 1) {
                return false;
            }
            BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
            switch (BlockUtil.isPositionPlaceable(position, true)) {
                case -1: {
                    continue block6;
                }
                case 1: {
                    if (this.retries.get((Object)position) != null && this.retries.get((Object)position) >= this.retryer.getValue()) continue block6;
                    this.placeBlock(position);
                    this.retries.put(position, this.retries.get((Object)position) == null ? 1 : this.retries.get((Object)position) + 1);
                    this.retryTimer.reset();
                    continue block6;
                }
                case 2: {
                    if (!hasHelpingBlocks) continue block6;
                    gotHelp = this.placeBlocks(pos, BlockUtil.getHelpingBlocks(vec3d), false, true, true);
                }
                case 3: {
                    if (gotHelp) {
                        this.placeBlock(position);
                    }
                    if (!isHelping) continue block6;
                    return true;
                }
            }
        }
        this.mc.getConnection().sendPacket((Packet)new CPacketHeldItemChange(lastSlot));
        return false;
    }

    private boolean check() {
        if (this.isNull()) {
            return true;
        }
        isPlacing = false;
        this.didPlace = false;
        this.extenders = 1;
        this.placements = 0;
        int obbySlot = ItemUtil.getItemFromHotbar(Item.getItemFromBlock((Block)Blocks.OBSIDIAN));
        int echestSlot = ItemUtil.getItemFromHotbar(Item.getItemFromBlock((Block)Blocks.ENDER_CHEST));
        if (!this.isEnabled()) {
            return true;
        }
        if (this.mc.player.posY > this.enableYPos) {
            this.disable();
            return false;
        }
        if (this.retryTimer.hasReached(2500L)) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        if (!(obbySlot != -1 || this.echests.getValue().booleanValue() && echestSlot != -1)) {
            if (this.info.getValue().booleanValue()) {
                MessageUtil.sendClientMessage("<Feetplace> No obsidian, toggling.", -1144);
            }
            this.disable();
            return true;
        }
        if (this.mc.player.inventory.currentItem != this.lastHotbarSlot && this.mc.player.inventory.currentItem != obbySlot && this.mc.player.inventory.currentItem != echestSlot) {
            this.lastHotbarSlot = this.mc.player.inventory.currentItem;
        }
        return !this.timer.hasReached(this.delay.getValue().intValue());
    }

    private void placeBlock(BlockPos pos) {
        if (this.placements < this.blocksPerTick.getValue()) {
            isPlacing = true;
            BlockUtil.placeBlock(pos);
            this.didPlace = true;
            ++this.placements;
        }
    }
}

