/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketClickWindow
 */
package cc.zip.charon.client.modules.combat;

import cc.zip.charon.Charon;
import cc.zip.charon.api.property.Bind;
import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.CombatUtil;
import cc.zip.charon.api.util.ItemUtil;
import cc.zip.charon.api.util.Timer;
import cc.zip.charon.client.event.events.KeyEvent;
import cc.zip.charon.client.event.events.UpdateEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketClickWindow;
import tcb.bces.listener.Subscribe;

@ModuleManifest(label="Offhand", category= Module.Category.COMBAT)
public class Offhand
extends Module {
    private final Setting<Float> health = this.register(new Setting<Float>("C-T-Health", Float.valueOf(15.0f), Float.valueOf(1.0f), Float.valueOf(37.0f)));
    private final Setting<Float> gappleHealth = this.register(new Setting<Float>("G-T-Health", Float.valueOf(8.0f), Float.valueOf(1.0f), Float.valueOf(37.0f)));
    private final Setting<Bind> gappleBind = this.register(new Setting<Bind>("GappleBind", new Bind(-1)));
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 15, 0, 200));
    private boolean gappling;
    private final Timer timer = new Timer();
    private static Offhand INSTANCE = new Offhand();

    public Offhand() {
        INSTANCE = this;
    }

    public static Offhand getInstance() {
        return INSTANCE;
    }

    @Subscribe
    public void onKey(KeyEvent event) {
        if (event.getKey() == this.gappleBind.getValue().getKey()) {
            this.gappling = !this.gappling;
        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (this.isNull()) {
            return;
        }
        if (this.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        if (this.timer.hasReached(this.delay.getValue().intValue())) {
            Item item = this.shouldTotem() ? Items.TOTEM_OF_UNDYING : (this.gappling ? Items.GOLDEN_APPLE : Items.END_CRYSTAL);
            int getSlot = ItemUtil.getItemSlot(item);
            if (item == Items.END_CRYSTAL) {
                this.setSuffix("Crystal");
            } else if (item == Items.TOTEM_OF_UNDYING) {
                this.setSuffix("Totem");
            } else {
                this.setSuffix("Gapple");
            }
            if (this.mc.player.getHeldItemOffhand() == ItemStack.EMPTY || this.mc.player.getHeldItemOffhand().getItem() != item) {
                int slot;
                int n = slot = getSlot < 9 ? getSlot + 36 : getSlot;
                if (getSlot != -1) {
                    this.clickSlot(0, slot, 0);
                    this.clickSlot(0, 45, 0);
                    this.clickSlot(0, slot, 0);
                }
            }
            this.timer.reset();
        }
    }

    private void clickSlot(int windowId, int slotId, int button) {
        this.mc.getConnection().sendPacket((Packet)new CPacketClickWindow(windowId, slotId, button, ClickType.PICKUP, this.mc.player.openContainer.slotClick(slotId, 0, ClickType.PICKUP, (EntityPlayer)this.mc.player), this.mc.player.openContainer.getNextTransactionID(this.mc.player.inventory)));
    }

    private boolean nearPlayers() {
        AutoCrystal ac = (AutoCrystal) Charon.INSTANCE.getModuleManager().getModuleByClass(AutoCrystal.class);
        return CombatUtil.getTarget(ac.range.getValue().floatValue()) != null;
    }

    private boolean shouldTotem() {
        if (ItemUtil.getItemCount(Items.TOTEM_OF_UNDYING) == 0) {
            return false;
        }
        if (ItemUtil.getItemCount(this.gappling ? Items.GOLDEN_APPLE : Items.END_CRYSTAL) == 0) {
            return true;
        }
        return this.mc.player.getHealth() + this.mc.player.getAbsorptionAmount() <= (!this.gappling ? this.health.getValue() : this.gappleHealth.getValue()).floatValue() || !Charon.INSTANCE.getSafeManager().isSafe() || this.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA || !this.gappling && !this.nearPlayers();
    }
}

