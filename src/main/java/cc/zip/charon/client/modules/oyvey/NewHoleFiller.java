package cc.zip.charon.client.modules.oyvey;

import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.BlockUtil;
import cc.zip.charon.api.util.EntityUtil;
import cc.zip.charon.api.util.ItemUtil;
import cc.zip.charon.api.util.Timer;
import cc.zip.charon.client.event.events.TickEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import tcb.bces.listener.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ModuleManifest(label="HoleFillerOyv", category= Module.Category.OYVEY)
public class NewHoleFiller

        extends Module {

     static {
        surroundOffset = BlockUtil.toBlockPos(EntityUtil.getOffsets(0, true));
    }

    private static final BlockPos[] surroundOffset;
    private final Timer offTimer = new Timer();
    private final Timer timer = new Timer();
    private final Map<BlockPos, Integer> retries = new HashMap<BlockPos, Integer>();;
    private final Timer retryTimers = new Timer();
    private int blocksThisTick;
    private int trie;
    private ArrayList<BlockPos> holes = new ArrayList<BlockPos>();

    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 50, 0, 250));
    private final Setting<Integer> range = this.register(new Setting<Integer>("Range", 5, 0, 8));
    private final Setting<Integer> blocksPerTick = this.register(new Setting<Integer>("BlockTick", 8, 8, 30));

    @Override
    public void onEnable() {
        if (isNull()) {
            this.disable();
        }
        this.offTimer.reset();
        this.trie = 0;
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (this.isOn()) {
            this.doHoleFill();
        }
    }

    @Override
    public void onDisable() {
        this.retries.clear();
    }

    private void doHoleFill() {
        if (this.check()) {
            return;
        }
        this.holes = new ArrayList<BlockPos>();
        final Iterable<BlockPos> blocks = BlockPos.getAllInBox(mc.player.getPosition().add(-this.range.getValue(), -this.range.getValue(), -this.range.getValue()), mc.player.getPosition().add(this.range.getValue(), this.range.getValue(), this.range.getValue()));
        for (final BlockPos pos : blocks) {
            if (!mc.world.getBlockState(pos).getMaterial().blocksMovement() && !mc.world.getBlockState(pos.add(0, 1, 0)).getMaterial().blocksMovement()) {
                final boolean solidNeighbours = (mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK | mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK | mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK | mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK | mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN) && mc.world.getBlockState(pos.add(0, 0, 0)).getMaterial() == Material.AIR && mc.world.getBlockState(pos.add(0, 1, 0)).getMaterial() == Material.AIR && mc.world.getBlockState(pos.add(0, 2, 0)).getMaterial() == Material.AIR;
                if (!solidNeighbours) {
                    continue;
                }
                this.holes.add(pos);
            }
        }
        this.holes.forEach(this::placeBlock);
        this.toggle();
    }

    private void placeBlock(final BlockPos pos) {
        for (final Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos))) {
            if (entity instanceof EntityLivingBase) {
                return;
            }
        }
        if (this.blocksThisTick < this.blocksPerTick.getValue()) {
            final int obbySlot = ItemUtil.findHotbarBlock(BlockObsidian.class);
            final int eChestSot = ItemUtil.findHotbarBlock(BlockEnderChest.class);
            if (obbySlot == -1 && eChestSot == -1) {
                this.toggle();
            }
            final int originalSlot = mc.player.inventory.currentItem;
            mc.player.inventory.currentItem = ((obbySlot == -1) ? eChestSot : obbySlot);
            mc.playerController.updateController();
            BlockUtil.placeBlock(pos);
            if (mc.player.inventory.currentItem != originalSlot) {
                mc.player.inventory.currentItem = originalSlot;
                mc.playerController.updateController();
            }
            this.timer.reset();
            ++this.blocksThisTick;
        }
    }

    private boolean check() {
        if (isNull()) {
            this.disable();
            return true;
        }
        this.blocksThisTick = 0;
        if (this.retryTimers.passedMs(2000L)) {
            this.retries.clear();
            this.retryTimers.reset();
        }
        return !this.timer.passedMs(this.delay.getValue());
    }
}
