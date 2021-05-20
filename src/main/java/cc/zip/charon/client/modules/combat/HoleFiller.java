/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 */
package cc.zip.charon.client.modules.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.BlockUtil;
import cc.zip.charon.api.util.CombatUtil;
import cc.zip.charon.api.util.ItemUtil;
import cc.zip.charon.api.util.MessageUtil;
import cc.zip.charon.client.command.Command;
import cc.zip.charon.client.event.events.UpdateEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import tcb.bces.listener.Subscribe;

@ModuleManifest(label="HoleFiller", category=Module.Category.COMBAT)
public class HoleFiller
        extends Module {
    private final Setting<Integer> bpt = this.register(new Setting<Integer>("Blocks/Tick", 10, 1, 20));
    private final Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(5.0f), Float.valueOf(1.0f), Float.valueOf(6.0f)));
    private final Setting<Float> distance = this.register(new Setting<Float>("Distance", Float.valueOf(4.0f), Float.valueOf(1.0f), Float.valueOf(7.0f)));
    private static final BlockPos[] surroundOffset = BlockUtil.toBlockPos(BlockUtil.getOffsets(0, true));
    private int placeAmount = 0;
    private int blockSlot = -1;

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (this.check()) {
            EntityPlayer currentTarget = CombatUtil.getTarget(10.0f);
            if (currentTarget == null) {
                return;
            }
            if (CombatUtil.isInHole(currentTarget)) {
                return;
            }
            List<BlockPos> holes = this.calcHoles();
            holes.sort(Comparator.comparingDouble(((EntityPlayer)currentTarget)::getDistanceSq));
            if (holes.size() == 0) {
                this.toggle();
                return;
            }
            int lastSlot = this.mc.player.inventory.currentItem;
            this.blockSlot = ItemUtil.getItemFromHotbar(Item.getItemFromBlock((Block)Blocks.OBSIDIAN));
            if (this.blockSlot == -1) {
                this.toggle();
                return;
            }
            BlockPos hole = null;
            for (BlockPos pos : holes) {
                if (!(currentTarget.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()) < (double)this.distance.getValue().floatValue())) continue;
                hole = pos;
            }
            if (hole != null) {
                this.mc.getConnection().sendPacket((Packet)new CPacketHeldItemChange(this.blockSlot));
                this.placeBlock(hole);
                this.mc.getConnection().sendPacket((Packet)new CPacketHeldItemChange(lastSlot));
            }
        }
    }

    private void placeBlock(BlockPos pos) {
        if (this.bpt.getValue() > this.placeAmount) {
            BlockUtil.placeBlock(pos);
            ++this.placeAmount;
        }
    }

    private boolean check() {
        if (this.mc.player == null) {
            return false;
        }
        this.placeAmount = 0;
        this.blockSlot = ItemUtil.getItemFromHotbar(Item.getItemFromBlock((Block)Blocks.OBSIDIAN));
        if (this.blockSlot == -1) {
            MessageUtil.sendClientMessage("<HoleFiller> No obsidian, toggling!", -22221);
            this.toggle();
        }
        return true;
    }

    public List<BlockPos> calcHoles() {
        ArrayList<BlockPos> safeSpots = new ArrayList<BlockPos>();
        List<BlockPos> positions = BlockUtil.getSphere(this.range.getValue().floatValue(), false);
        for (int i = 0; i < positions.size(); ++i) {
            BlockPos pos = positions.get(i);
            if (BlockUtil.isPositionPlaceable(pos, true) == 1 || !this.mc.world.getBlockState(pos).getBlock().equals((Object)Blocks.AIR) || !this.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals((Object)Blocks.AIR) || !this.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals((Object)Blocks.AIR)) continue;
            boolean isSafe = true;
            for (BlockPos offset : surroundOffset) {
                Block block = this.mc.world.getBlockState(pos.add((Vec3i)offset)).getBlock();
                if (block == Blocks.BEDROCK || block == Blocks.OBSIDIAN) continue;
                isSafe = false;
            }
            if (!isSafe) continue;
            safeSpots.add(pos);
        }
        return safeSpots;
    }
}
