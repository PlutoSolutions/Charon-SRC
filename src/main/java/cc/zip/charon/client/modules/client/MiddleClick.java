/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 */
package cc.zip.charon.client.modules.client;

import cc.zip.charon.Charon;
import cc.zip.charon.api.property.Setting;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

@ModuleManifest(label="MiddleClick", category=Module.Category.CLIENT)
public class MiddleClick
extends Module {
    private static MiddleClick INSTANCE = new MiddleClick();
    private final Setting<Boolean> friends = this.register(new Setting<Boolean>("Friends", true));

    public MiddleClick() {
        INSTANCE = this;
    }

    public static MiddleClick getInstance() {
        return INSTANCE;
    }

    public void run(int mouse) {
        if (mouse == 2 && this.friends.getValue().booleanValue() && this.mc.objectMouseOver.entityHit != null) {
            Entity entity = this.mc.objectMouseOver.entityHit;
            if (!(entity instanceof EntityPlayer)) {
                return;
            }
            if (Charon.INSTANCE.getFriendManager().isFriend(entity.getName())) {
                Charon.INSTANCE.getFriendManager().removeFriend(entity.getName());
            } else {
                Charon.INSTANCE.getFriendManager().addFriend(entity.getName());
            }
        }
    }
}

