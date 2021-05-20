package cc.zip.charon.client.modules.movement;

import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.MathUtil;
import cc.zip.charon.client.event.events.TickEvent;
import cc.zip.charon.client.event.events.UpdateEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tcb.bces.listener.Subscribe;

/*

   NoFall
   By Amfero - Razmorozka client
   https://github.com/oyzipfile/razmorozka/blob/main/src/main/java/me/amfero/razmorozka/module/movement/godNoFall.java

*/

@ModuleManifest(label="NoFallBypass", category= Module.Category.MOVEMENT)
public class NoFallBypass
        extends Module {
    private final Setting<Integer> fallDist = this.register(new Setting<Integer>("FallDist", 4, 3, 40, v -> this.page.getValue() == Page.Old));
    private final Setting<Integer> fallDist2 = this.register(new Setting<Integer>("FallDist 2", 15, 10, 40, v -> this.page.getValue() == Page.Predict));
    private final Setting<Page> page = this.register(new Setting<Page>("Page", Page.Predict));
    public static enum Page {
        Predict,
        Old;

    }
    @Subscribe
    public void onTick(TickEvent event) {
        if(mc.world == null) return;
        if(page.getValue() == Page.Predict) {
            Vec3d vec = new Vec3d(mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * mc.getRenderPartialTicks(), mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * mc.getRenderPartialTicks(), mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * mc.getRenderPartialTicks());
            BlockPos pos = new BlockPos(vec.x, vec.y - 2, vec.z);
            BlockPos[] posList = { pos.north(), pos.south(), pos.east(), pos.west(), pos.down(), pos.down() };
            for (BlockPos blockPos : posList)
            {
                Block block = mc.world.getBlockState(blockPos).getBlock();
                if (mc.player.dimension == 1) {
                    if(mc.player.fallDistance > fallDist2.getValue()) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(0, 64, 0, false));
                        mc.player.fallDistance = fallDist.getValue() + 1;
                    }
                    if (mc.player.fallDistance > fallDist.getValue() && block != Blocks.AIR) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(0, 64, 0, false));
                        mc.player.fallDistance = 0;
                    }
                } else {
                    if(mc.player.fallDistance > fallDist2.getValue()) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, 0, mc.player.posZ, false));
                        mc.player.fallDistance = fallDist.getValue() + 1;
                    }
                    if (mc.player.fallDistance > fallDist.getValue() && block != Blocks.AIR) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, 0, mc.player.posZ, false));
                        mc.player.fallDistance = 0;
                    }
                }
            }
        }
        if(page.getValue() == Page.Old) {
            if(mc.player.fallDistance > fallDist.getValue()) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, 0, mc.player.posZ, false));
                mc.player.fallDistance = 0;
            }
        }
    }
}
