package cc.zip.charon.client.modules.oyvey;

import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.*;
import cc.zip.charon.api.util.Timer;
import cc.zip.charon.client.event.events.TickEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import tcb.bces.listener.Subscribe;

import java.util.*;
@ModuleManifest(label="SurroundOy", category= Module.Category.OYVEY)
public class NewSurround
        extends Module {
    public static boolean isPlacing = false;
    private final Setting<Integer> blocksPerTick = this.register(new Setting<Integer>("BlocksPerTick", 12, 1, 20));
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 0, 0, 250));
    private final Setting<Boolean> noGhost = this.register(new Setting<Boolean>("PacketPlace", false));
    private final Setting<Boolean> center = this.register(new Setting<Boolean>("TPCenter", false));
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    private final Timer timer = new Timer();
    private final Timer retryTimer = new Timer();
    private final Set<Vec3d> extendingBlocks = new HashSet<Vec3d>();
    private final Map<BlockPos, Integer> retries = new HashMap<BlockPos, Integer>();
    private int isSafe;
    private BlockPos startPos;
    private boolean didPlace = false;
    private boolean switchedItem;
    private int lastHotbarSlot;
    private boolean isSneaking;
    private int placements = 0;
    private int extenders = 1;
    private int obbySlot = -1;
    private boolean offHand = false;


    @Override
    public void onEnable() {
        if (isNull()) {
            this.disable();
        }
        this.lastHotbarSlot = mc.player.inventory.currentItem;
        this.startPos = EntityUtilNigger.getRoundedBlockPos(mc.player);
        if (this.center.getValue().booleanValue()) {
            mc.player.setPosition(this.startPos.getX() + 0.5, this.startPos.getY(), this.startPos.getZ() + 0.5);
        }
        this.retries.clear();
        this.retryTimer.reset();
    }

    @Subscribe
    public void onTick(TickEvent event) {
        this.doFeetPlace();
    }

    @Override
    public void onDisable() {
        if (isNull()) {
            return;
        }
        isPlacing = false;
        this.isSneaking = EntityUtilNigger.stopSneaking(this.isSneaking);
    }

    @Subscribe
    public String getDisplayInfo() {
        switch (this.isSafe) {
            case 0: {
                return ChatFormatting.RED + "Unsafe";
            }
            case 1: {
                return ChatFormatting.YELLOW + "Safe";
            }
        }
        return ChatFormatting.GREEN + "Safe";
    }

    private void doFeetPlace() {
        if (this.check()) {
            return;
        }
        if (!EntityUtilNigger.isSafe(mc.player, 0, true)) {
            this.isSafe = 0;
            this.placeBlocks(mc.player.getPositionVector(), EntityUtilNigger.getUnsafeBlockArray(mc.player, 0, true), true, false, false);
        } else if (!EntityUtilNigger.isSafe(mc.player, -1, false)) {
            this.isSafe = 1;
            this.placeBlocks(mc.player.getPositionVector(), EntityUtilNigger.getUnsafeBlockArray(mc.player, -1, false), false, false, true);
        } else {
            this.isSafe = 2;
        }
        this.processExtendingBlocks();
        if (this.didPlace) {
            this.timer.reset();
        }
    }

    private void processExtendingBlocks() {
        if (this.extendingBlocks.size() == 2 && this.extenders < 1) {
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
                this.placeBlocks(this.areClose(array), EntityUtilNigger.getUnsafeBlockArrayFromVec3d(this.areClose(array), 0, true), true, false, true);
            }
            if (placementsBefore < this.placements) {
                this.extendingBlocks.clear();
            }
        } else if (this.extendingBlocks.size() > 2 || this.extenders >= 1) {
            this.extendingBlocks.clear();
        }
    }

    private Vec3d areClose(Vec3d[] vec3ds) {
        int matches = 0;
        for (Vec3d vec3d : vec3ds) {
            for (Vec3d pos : EntityUtilNigger.getUnsafeBlockArray(mc.player, 0, true)) {
                if (!vec3d.equals(pos)) continue;
                ++matches;
            }
        }
        if (matches == 2) {
            return mc.player.getPositionVector().add(vec3ds[0].add(vec3ds[1]));
        }
        return null;
    }

    private boolean placeBlocks(Vec3d pos, Vec3d[] vec3ds, boolean hasHelpingBlocks, boolean isHelping, boolean isExtending) {
        boolean gotHelp = true;
        block5:
        for (Vec3d vec3d : vec3ds) {
            gotHelp = true;
            BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
            switch (BlockUtil.isPositionPlaceable(position, false)) {
                case 1: {
                    if (this.retries.get(position) == null || this.retries.get(position) < 4) {
                        this.placeBlock(position);
                        this.retries.put(position, this.retries.get(position) == null ? 1 : this.retries.get(position) + 1);
                        this.retryTimer.reset();
                        continue block5;
                    }
                    if (isExtending || this.extenders >= 1) continue block5;
                    this.placeBlocks(mc.player.getPositionVector().add(vec3d), EntityUtilNigger.getUnsafeBlockArrayFromVec3d(mc.player.getPositionVector().add(vec3d), 0, true), hasHelpingBlocks, false, true);
                    this.extendingBlocks.add(vec3d);
                    ++this.extenders;
                    continue block5;
                }
                case 2: {
                    if (!hasHelpingBlocks) continue block5;
                    gotHelp = this.placeBlocks(pos, BlockUtil.getHelpingBlocks(vec3d), false, true, true);
                }
                case 3: {
                    if (gotHelp) {
                        this.placeBlock(position);
                    }
                    if (!isHelping) continue block5;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean check() {
        if (isNull()) {
            return true;
        }
        int obbySlot = ItemUtil.findHotbarBlock(BlockObsidian.class);
        int eChestSot = ItemUtil.findHotbarBlock(BlockEnderChest.class);
        if (obbySlot == -1 && eChestSot == -1) {
            this.toggle();
        }
        this.offHand = ItemUtil.isBlock(mc.player.getHeldItemOffhand().getItem(), BlockObsidian.class);
        isPlacing = false;
        this.didPlace = false;
        this.extenders = 1;
        this.placements = 0;
        this.obbySlot = ItemUtil.findHotbarBlock(BlockObsidian.class);
        int echestSlot = ItemUtil.findHotbarBlock(BlockEnderChest.class);
        if (this.isOff()) {
            return true;
        }
        if (this.retryTimer.passedMs(2500L)) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        if (this.obbySlot == -1 && !this.offHand && echestSlot == -1) {
            MessageUtil.sendClientMessage("<" + this.getClass() + "> " + ChatFormatting.RED + "No Obsidian in hotbar disabling...", 9942);
            this.disable();
            return true;
        }
        this.isSneaking = EntityUtilNigger.stopSneaking(this.isSneaking);
        if (mc.player.inventory.currentItem != this.lastHotbarSlot && mc.player.inventory.currentItem != this.obbySlot && mc.player.inventory.currentItem != echestSlot) {
            this.lastHotbarSlot = mc.player.inventory.currentItem;
        }
        if (!this.startPos.equals(EntityUtilNigger.getRoundedBlockPos(mc.player))) {
            this.disable();
            return true;
        }
        return !this.timer.passedMs(this.delay.getValue().intValue());
    }

    private void placeBlock(BlockPos pos) {
        if (this.placements < this.blocksPerTick.getValue()) {
            int originalSlot = mc.player.inventory.currentItem;
            int obbySlot = ItemUtil.findHotbarBlock(BlockObsidian.class);
            int eChestSot = ItemUtil.findHotbarBlock(BlockEnderChest.class);
            if (obbySlot == -1 && eChestSot == -1) {
                this.toggle();
            }
            isPlacing = true;
            mc.player.inventory.currentItem = obbySlot == -1 ? eChestSot : obbySlot;
            mc.playerController.updateController();
            this.isSneaking = BlockUtil.placeBlock1(pos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.rotate.getValue(), this.noGhost.getValue(), this.isSneaking);
            mc.player.inventory.currentItem = originalSlot;
            mc.playerController.updateController();
            this.didPlace = true;
            ++this.placements;
        }
    }
}