/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.util.math.BlockPos
 */
package cc.zip.charon.client.modules.combat;

import cc.zip.charon.api.util.ItemUtil;
import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.BlockUtil;
import cc.zip.charon.api.util.MessageUtil;
import cc.zip.charon.client.event.events.UpdateEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import tcb.bces.listener.Subscribe;


@ModuleManifest(label="Burrow", category= Module.Category.COMBAT)
public class Burrow
        extends Module {
    private final Setting<Float> height = this.register(new Setting<Float>("Height", Float.valueOf(5.0f), Float.valueOf(-15.0f), Float.valueOf(15.0f)));
    public final Setting<Page> page = this.register(new Setting<Page>("Page", Page.EnderChest));
    private static Burrow INSTANCE = new Burrow();
    public BlockPos startPos = null;

    public Burrow() {
        INSTANCE = this;
    }

    public static Burrow getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        if (this.isNull()) {
            this.disable();
            return;
        }
        this.startPos = new BlockPos(this.mc.player.getPositionVector());
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (this.isNull()) {
            return;
        }
        int startSlot = this.mc.player.inventory.currentItem;
        if(page.getValue() == Page.EnderChest){
            int enderSlot = ItemUtil.getItemFromHotbar(Item.getItemFromBlock((Block)Blocks.ENDER_CHEST));
            ItemUtil.switchToHotbarSlot(enderSlot, false);
            if (enderSlot == -1) {
                MessageUtil.sendClientMessage("<Burrow> No ender chest, toggling.", -413);
                this.disable();
                return;
            }
        }
        if(page.getValue() == Page.Obsdidian){
            int obbySlot = ItemUtil.getItemFromHotbar(Item.getItemFromBlock((Block)Blocks.OBSIDIAN));
            ItemUtil.switchToHotbarSlot(obbySlot, false);
            if (obbySlot == -1) {
                MessageUtil.sendClientMessage("<Burrow> No obsidian, toggling.", -42322);
                this.disable();
                return;
            }
        }
        this.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + 0.4199999, this.mc.player.posZ, true));
        this.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + 0.7531999, this.mc.player.posZ, true));
        this.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + 1.0013359, this.mc.player.posZ, true));
        this.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + 1.1661092, this.mc.player.posZ, true));
        BlockUtil.placeBlock(this.startPos);
        this.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + (double)this.height.getValue().floatValue(), this.mc.player.posZ, false));
        if (startSlot != -1) {
            ItemUtil.switchToHotbarSlot(startSlot, false);
        }
        this.disable();
    }
    public static enum Page {
        EnderChest,
        Obsdidian;

    }
}