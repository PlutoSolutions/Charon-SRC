/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
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
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import tcb.bces.listener.Subscribe;

@ModuleManifest(label="VoidESP", category= Module.Category.VISUAL)
public class VoidESP
extends Module {
    public Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(6.0f), Float.valueOf(3.0f), Float.valueOf(16.0f)));
    public Setting<Boolean> down = this.register(new Setting<Boolean>("Up", false));
    private List<BlockPos> holes = new ArrayList<BlockPos>();

    @Subscribe
    public void onTick(TickEvent event) {
        if (this.mc.player == null || this.mc.world == null || event.getStage() == 0) {
            return;
        }
        this.holes = this.calcHoles();
    }

    @Override
    public void onRender3D() {
        int size = this.holes.size();
        for (int i = 0; i < size; ++i) {
            BlockPos pos = this.holes.get(i);
            RenderUtil.renderCrosses(this.down.getValue() != false ? pos.up() : pos, new Color(255, 255, 255), 2.0f);
        }
    }

    public List<BlockPos> calcHoles() {
        ArrayList<BlockPos> voidHoles = new ArrayList<BlockPos>();
        List<BlockPos> positions = BlockUtil.getSphere(this.range.getValue().floatValue(), false);
        int size = positions.size();
        for (int i = 0; i < size; ++i) {
            BlockPos pos = positions.get(i);
            if (pos.getY() != 0 || this.mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK) continue;
            voidHoles.add(pos);
        }
        return voidHoles;
    }
}

