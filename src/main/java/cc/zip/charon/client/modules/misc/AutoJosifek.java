package cc.zip.charon.client.modules.misc;

import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.MessageUtil;
import cc.zip.charon.client.event.events.UpdateEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import tcb.bces.listener.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ModuleManifest(label="AutoJosifek", category=Module.Category.MISC)
public class AutoJosifek
        extends Module {

    private final Setting<Integer> delay = this.register(new Setting<Integer>("Extend", 1, 1, 30));
    private final Setting<Boolean> suffix = this.register(new Setting<Boolean>("Suffix", false));

    List<String> chants = new ArrayList<String>();
    Random r = new Random();
    int tick_delay;
    public static String Suffix2 = "";


    @Override
    public void onEnable(){
        this.tick_delay = 0;
        this.chants.add("https://josbin.org/upload/<player>" + Suffix2);
    }


    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (this.mc.player == null) {
            return;
        }
        ++this.tick_delay;
        if (this.tick_delay < this.delay.getValue() * 10) { return;
        }
        String  s = this.chants.get(this.r.nextInt(this.chants.size()));
        String name = this.get_random_name();
        if (name.equals(mc.player.getName())) {
        }
        if (suffix.getValue()){
            Suffix2 = "_CharonTeam";
        } else {
            Suffix2 = "";
        }
        mc.player.sendChatMessage(s.replace("<player>", name));
        this.tick_delay = 0;

    }
    public String get_random_name() {
        List players = mc.world.playerEntities;
        return ((EntityPlayer)players.get(this.r.nextInt(players.size()))).getName();
    }

    public String random_string(String[] list) {
        return list[this.r.nextInt(list.length)];
    }



}

