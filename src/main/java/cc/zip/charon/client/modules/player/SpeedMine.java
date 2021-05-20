/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.World
 */
package cc.zip.charon.client.modules.player;

import java.awt.Color;

import cc.zip.charon.Charon;
import me.dev.mexen.api.mixin.accessors.IPlayerControllerMP;
import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.RenderUtil;
import cc.zip.charon.api.util.Timer;
import cc.zip.charon.client.event.events.ClickBlockEvent;
import cc.zip.charon.client.event.events.TickEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tcb.bces.listener.Subscribe;

@ModuleManifest(label="SpeedMine", category= Module.Category.PLAYER)
public class SpeedMine
extends Module {
    public final Setting<Boolean> reset = this.register(new Setting<Boolean>("Reset", true));
    private static SpeedMine INSTANCE = new SpeedMine();
    private BlockPos currentPos = null;
    private final Timer renderTimer = new Timer();

    public SpeedMine() {
        INSTANCE = this;
    }

    public static SpeedMine getInstance() {
        return INSTANCE;
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (this.reset.getValue().booleanValue() && this.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            this.mc.playerController.resetBlockRemoving();
            ((IPlayerControllerMP)this.mc.playerController).setIsHittingBlock(false);
        }
    }

    @Override
    public void onRender3D() {
        if (this.currentPos != null && this.mc.world.getBlockState(this.currentPos).getBlock() == Blocks.AIR) {
            this.currentPos = null;
        }
        if (this.currentPos != null) {
            Color color = new Color(this.renderTimer.hasReached((int)(2000.0f * Charon.INSTANCE.getTpsManager().getTpsFactor())) ? 0 : 255, this.renderTimer.hasReached((int)(2000.0f * Charon.INSTANCE.getTpsManager().getTpsFactor())) ? 255 : 0, 0, 255);
            RenderUtil.drawProperBoxESP(this.currentPos, color, 2.0f, true, true, 40, 1.0f);
        }
    }

    @Subscribe
    public void onClickBlock(ClickBlockEvent event) {
        if (this.canBreak(event.getPos())) {
            if (this.reset.getValue().booleanValue()) {
                ((IPlayerControllerMP)this.mc.playerController).setIsHittingBlock(false);
                this.mc.playerController.resetBlockRemoving();
            }
            if (this.currentPos == null) {
                this.currentPos = event.getPos();
                this.renderTimer.reset();
            }
            this.mc.player.swingArm(EnumHand.MAIN_HAND);
            this.mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFacing()));
            this.mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFacing()));
            this.currentPos = event.getPos();
            event.setCancelled();
        }
    }

    public boolean canBreak(BlockPos pos) {
        Block block = this.mc.world.getBlockState(pos).getBlock();
        return block.getBlockHardness(this.mc.world.getBlockState(pos), (World)this.mc.world, pos) != -1.0f;
    }
}

