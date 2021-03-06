/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 */
package cc.zip.charon.client.modules.visual;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.BlockUtil;
import cc.zip.charon.api.util.RenderUtil;
import cc.zip.charon.client.event.events.TickEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import tcb.bces.listener.Subscribe;

@ModuleManifest(label="HoleESP", category= Module.Category.VISUAL)
public class HoleESP
extends Module {
    public final Setting<Page> page = this.register(new Setting<Page>("Page", Page.MISC));
    public final Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(5.0f), Float.valueOf(1.0f), Float.valueOf(16.0f), v -> this.page.getValue() == Page.MISC));
    public final Setting<Boolean> flat = this.register(new Setting<Boolean>("Flat", true));
    public final Setting<Boolean> wireframe = this.register(new Setting<Boolean>("Wireframe", Boolean.valueOf(true), v -> this.page.getValue() == Page.MISC && this.flat.getValue() != false));
    private final Setting<Integer> obsidianRed = this.register(new Setting<Integer>("O-Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.page.getValue() == Page.COLOR));
    private final Setting<Integer> obsidianGreen = this.register(new Setting<Integer>("O-Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.page.getValue() == Page.COLOR));
    private final Setting<Integer> obsidianBlue = this.register(new Setting<Integer>("O-Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.page.getValue() == Page.COLOR));
    private final Setting<Integer> obsidianAlpha = this.register(new Setting<Integer>("O-Alpha", Integer.valueOf(40), Integer.valueOf(0), Integer.valueOf(255), v -> this.page.getValue() == Page.COLOR));
    public final Setting<Integer> bedRockRed = this.register(new Setting<Integer>("B-Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.page.getValue() == Page.COLOR));
    public final Setting<Integer> bedRockGreen = this.register(new Setting<Integer>("B-Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.page.getValue() == Page.COLOR));
    public final Setting<Integer> bedRockBlue = this.register(new Setting<Integer>("B-Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.page.getValue() == Page.COLOR));
    public final Setting<Integer> bedRockAlpha = this.register(new Setting<Integer>("B-Alpha", Integer.valueOf(40), Integer.valueOf(0), Integer.valueOf(255), v -> this.page.getValue() == Page.COLOR));
    public List<BlockPos> holes = new ArrayList<BlockPos>();
    private final BlockPos[] surroundOffset = BlockUtil.toBlockPos(BlockUtil.getOffsets(0, true));

    @Subscribe
    public void onTick(TickEvent event) {
        if (event.getStage() == 0 || this.mc.player == null || this.mc.world == null) {
            return;
        }
        this.holes = this.calcHoles();
    }

    @Override
    public void onRender3D() {
        int size = this.holes.size();
        for (int i = 0; i < size; ++i) {
            BlockPos pos = this.holes.get(i);
            Color color = this.isSafe(pos) ? new Color(this.bedRockRed.getValue(), this.bedRockGreen.getValue(), this.bedRockBlue.getValue()) : new Color(this.obsidianRed.getValue(), this.obsidianGreen.getValue(), this.obsidianBlue.getValue());
            RenderUtil.drawBoxESP(pos, color, 1.0f, true, true, this.isSafe(pos) ? this.bedRockAlpha.getValue().intValue() : this.obsidianAlpha.getValue().intValue(), this.flat.getValue() != false ? 0.0f : 1.0f);
            if (!this.wireframe.getValue().booleanValue()) continue;
            RenderUtil.renderCrosses(pos, color, 1.0f);
        }
    }

    public List<BlockPos> calcHoles() {
        ArrayList<BlockPos> safeSpots = new ArrayList<BlockPos>();
        List<BlockPos> positions = BlockUtil.getSphere(this.range.getValue().floatValue(), false);
        int size = positions.size();
        for (int i = 0; i < size; ++i) {
            BlockPos pos = positions.get(i);
            if (!this.mc.world.getBlockState(pos).getBlock().equals((Object)Blocks.AIR) || !this.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals((Object)Blocks.AIR) || !this.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals((Object)Blocks.AIR)) continue;
            boolean isSafe = true;
            for (BlockPos offset : this.surroundOffset) {
                Block block = this.mc.world.getBlockState(pos.add((Vec3i)offset)).getBlock();
                if (block == Blocks.BEDROCK || block == Blocks.OBSIDIAN) continue;
                isSafe = false;
            }
            if (!isSafe) continue;
            safeSpots.add(pos);
        }
        return safeSpots;
    }

    private boolean isSafe(BlockPos pos) {
        boolean isSafe = true;
        for (BlockPos offset : this.surroundOffset) {
            if (this.mc.world.getBlockState(pos.add((Vec3i)offset)).getBlock() == Blocks.BEDROCK) continue;
            isSafe = false;
            break;
        }
        return isSafe;
    }

    public static enum Page {
        COLOR,
        MISC;

    }
}

