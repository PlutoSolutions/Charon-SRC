package cc.zip.charon.client.modules.player;

import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.MessageUtil;
import cc.zip.charon.client.event.events.UpdateEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import tcb.bces.listener.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ModuleManifest(label="ArmorWarning", category=Module.Category.MISC)
public class ArmorWarning
        extends Module {

    private final Setting<Integer> delay = this.register(new Setting<Integer>("Extend", 1, 1, 30));

    List<String> chants = new ArrayList<String>();
    Random r = new Random();
    int tick_delay;

    @Override
    public void onEnable(){
        this.tick_delay = 0;
        this.chants.add("https://doxbin.org/upload/<player>_CharonTeam");
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (this.mc.player == null) {
            return;
        }
        boolean ArmorDurability = this.getArmorDurability();
        if (ArmorDurability) {
            MessageUtil.sendClientMessage("" + ChatFormatting.RED + ChatFormatting.BOLD + "Armor Durability: 50%", true);
        }
        return;

    }
    private boolean getArmorDurability() {
        boolean TotalDurability = false;
        for (ItemStack itemStack : mc.player.inventory.armorInventory) {
            if (itemStack.getMaxDamage() / 2 >= itemStack.getItemDamage()) continue;
            return true;
        }
        return TotalDurability;
    }

}

