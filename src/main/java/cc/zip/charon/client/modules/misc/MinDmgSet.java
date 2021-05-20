/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 */
package cc.zip.charon.client.modules.misc;

import cc.zip.charon.api.property.Setting;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import cc.zip.charon.client.modules.combat.AutoCrystal;
import com.google.common.eventbus.Subscribe;

@ModuleManifest(label="MinDmgSet", category=Module.Category.MISC)
public class MinDmgSet
        extends Module {
    private static MinDmgSet INSTANCE = new MinDmgSet();

    public MinDmgSet() {
        INSTANCE = this;
    }
    private final Setting<Float> enaminDamage = this.register(new Setting<Float>("Enable MG", Float.valueOf(4.0f), Float.valueOf(1.0f), Float.valueOf(36.0f)));
    private final Setting<Float> disnaminDamage = this.register(new Setting<Float>("Disable MG", Float.valueOf(4.0f), Float.valueOf(1.0f), Float.valueOf(36.0f)));

    public static MinDmgSet getInstance() {
        return INSTANCE;
    }

    @Subscribe
    public void onEnable(){
        AutoCrystal.getInstance().minDamage.setValue(enaminDamage.getValue());
    }
    @Subscribe
    public void onDisable(){
        AutoCrystal.getInstance().minDamage.setValue(disnaminDamage.getValue());
    }

}
