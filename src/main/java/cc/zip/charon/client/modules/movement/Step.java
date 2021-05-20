/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.modules.movement;

import cc.zip.charon.api.property.Setting;
import cc.zip.charon.client.event.events.UpdateEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tcb.bces.listener.Subscribe;

@ModuleManifest(label="Step", category= Module.Category.MOVEMENT)
public class Step
extends Module {
    private final Setting<Boolean> placeHolder = this.register(new Setting<Boolean>("PlaceHolder", true));

    @SubscribeEvent
    public void onEnable(){
        if (!this.placeHolder.getValue()) {
            mc.player.stepHeight = 2.1f;
        }
    }
    @Subscribe
    public void onDisable(){
        if (!this.placeHolder.getValue()) {
            mc.player.stepHeight = 0.6f;
        }
    }
}
