package cc.zip.charon.client.modules.movement;

import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.MathUtil;
import cc.zip.charon.client.event.events.TickEvent;
import cc.zip.charon.client.event.events.UpdateEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import tcb.bces.listener.Subscribe;

@ModuleManifest(label="ElytraFly", category= Module.Category.MOVEMENT)
public class ElytraFly
        extends Module {
    private final Setting<Integer> speed = this.register(new Setting<Integer>("Speed", 10, 1, 30));


    @Override
    public void onEnable(){
        mc.player.capabilities.isFlying = true;
    }
    @Override
    public void onDisable(){
        mc.player.capabilities.isFlying = false;
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (!mc.player.isElytraFlying()) {
            return;
        }
        if (!mc.player.onGround) {
            double[] calc = MathUtil.directionSpeed(this.speed.getValue() / 10.0);
            mc.player.motionX = calc[0];
            mc.player.motionZ = calc[1];
        }
    }
}
