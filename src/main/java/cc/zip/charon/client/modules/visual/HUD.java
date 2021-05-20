/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemStack
 *  org.apache.commons.lang3.StringUtils
 */
package cc.zip.charon.client.modules.visual;

import cc.zip.charon.Charon;
import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.*;
import cc.zip.charon.api.util.Timer;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import cc.zip.charon.client.modules.combat.AutoCrystal;
import com.mojang.realmsclient.gui.ChatFormatting;

import java.text.DecimalFormat;
import java.util.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.StringUtils;

@ModuleManifest(label="HUD", category=Module.Category.VISUAL, listen=false)
public class    HUD
        extends Module {
    private final Setting<Boolean> crystalInfo = this.register(new Setting<Boolean>("AutoCrystal Info", true));
    private final Setting<Integer> crystalInfoX = this.register(new Setting<Integer>("Info X", Integer.valueOf(60), Integer.valueOf(0), Integer.valueOf(1000), v -> this.crystalInfo.getValue()));
    private final Setting<Integer> crystalInfoY = this.register(new Setting<Integer>("Info Y", Integer.valueOf(60), Integer.valueOf(0), Integer.valueOf(1000), v -> this.crystalInfo.getValue()));

    private final Setting<Boolean> pvpInfo = this.register(new Setting<Boolean>("PvP Info", true));
    private final Setting<Integer> pvpInfoY = this.register(new Setting<Integer>("PvP Info Y", Integer.valueOf(60), Integer.valueOf(0), Integer.valueOf(500), v -> this.pvpInfo.getValue()));
    private final Setting<Boolean> pvpInfoSync = this.register(new Setting<Boolean>("PvP Info Sync", Boolean.valueOf(false), v -> this.pvpInfo.getValue()));
    private final Setting<Boolean> arrayList = this.register(new Setting<Boolean>("Arraylist", true));
    private final Setting<SortMode> sortModeSetting = this.register(new Setting<SortMode>("Sorting", SortMode.LENGTH, v -> this.arrayList.getValue()));
    private final Setting<Integer> sortDelay = this.register(new Setting<Integer>("Sort Delay", Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(1000), v -> this.arrayList.getValue()));
    private final Setting<Integer> animationSpeed = this.register(new Setting<Integer>("Animation Speed", 60, 1, 500));
    private final Setting<ColorMode> colorSetting = this.register(new Setting<ColorMode>("Mode", ColorMode.SYNC, v -> this.arrayList.getValue()));
    private final Setting<Integer> rainbowFactor = this.register(new Setting<Integer>("Factor", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(200), v -> this.arrayList.getValue() != false && this.colorSetting.getValue() == ColorMode.RAINBOW));
    private final Setting<Integer> rainbowSaturation = this.register(new Setting<Integer>("Saturation", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.arrayList.getValue() != false && this.colorSetting.getValue() == ColorMode.RAINBOW));
    private final Setting<Integer> rainbowSpeed = this.register(new Setting<Integer>("Speed", Integer.valueOf(5000), Integer.valueOf(1), Integer.valueOf(10000), v -> this.arrayList.getValue() != false && this.colorSetting.getValue() == ColorMode.RAINBOW));
    private final Setting<Boolean> armorHud = this.register(new Setting<Boolean>("Armor", true));
    private final Setting<Boolean> watermark = this.register(new Setting<Boolean>("Watermark", true));
    private final Setting<Boolean> offsetWatermark = this.register(new Setting<Boolean>("Offset Watermark", Boolean.valueOf(false), v -> this.watermark.getValue()));
    private final Setting<Boolean> customWatermark = this.register(new Setting<Boolean>("Custom", Boolean.valueOf(false), v -> this.watermark.getValue()));
    private final Setting<String> watermarkString = this.register(new Setting<String>("CustomMark", "JordoHack", v -> this.customWatermark.getValue()));
    private final Setting<Boolean> coords = this.register(new Setting<Boolean>("Coordinates", true));
    private final Setting<Boolean> netherCoords = this.register(new Setting<Boolean>("Nether Coords", Boolean.valueOf(true), v -> this.coords.getValue()));
    private final Setting<Boolean> welcomer = this.register(new Setting<Boolean>("Welcomer", true));
    private final Setting<Boolean> oldnewelcom = this.register(new Setting<Boolean>("Welcomer Old", true));
    private final Setting<String> welcomeMsg = this.register(new Setting<String>("WelcomerMsg", "Welcom to charon.eu / %s", v -> this.welcomer.getValue()));
    private final Setting<Boolean> tpsSetting = this.register(new Setting<Boolean>("TPS Counter", true));
    private final Setting<Boolean> speedPlayer = this.register(new Setting<Boolean>("Speed Counter", true));

   private static HUD INSTANCE = new HUD();
    public Map<Integer, Integer> colorMap = new HashMap<Integer, Integer>();
    public HUD() {
        INSTANCE = this;
    }

    public static HUD getInstance() {
        return INSTANCE;
    }


    private final HashMap<Module, Float> extendedAmount = new HashMap();
    private final Timer sortTimer = new Timer();
    private final List<Module> modules = new ArrayList<Module>(Charon.INSTANCE.getModuleManager().getModules());


    @Override
    public void onRender2D() {



        int i;
        ScaledResolution resolution = new ScaledResolution(this.mc);
        if (this.watermark.getValue().booleanValue()) {
            Charon.fontManager.drawString(this.customWatermark.getValue() != false ? this.watermarkString.getValue() : "charon.eu", 2.0f, this.offsetWatermark.getValue() != false ? 10.0f : 2.0f, Charon.INSTANCE.getColorManager().getColorAsInt());
        }

        if (this.welcomer.getValue().booleanValue()) {
            if (this.oldnewelcom.getValue()) {
                this.drawCenteredString(StringUtils.replace((String) this.welcomeMsg.getValue(), (String) "%s", (String) this.mc.player.getName()), resolution.getScaledWidth() / 2, 2, Charon.INSTANCE.getColorManager().getColorAsInt());
            } else
                Charon.fontManager.drawString(StringUtils.replace((String) this.welcomeMsg.getValue(), (String) "%s", (String) this.mc.player.getName()), 2, 2, Charon.INSTANCE.getColorManager().getColorAsInt());

        }
        if (this.tpsSetting.getValue().booleanValue()) {
            String tps = (Object)ChatFormatting.GRAY + "TPS [" + (Object)ChatFormatting.WHITE + String.format("%.1f", Float.valueOf(Charon.INSTANCE.getTpsManager().getTPS())) + (Object)ChatFormatting.GRAY + "]";
            Charon.fontManager.drawString(tps, resolution.getScaledWidth() - 2 - Charon.fontManager.getStringWidth(tps), resolution.getScaledHeight() - 10, -1);
        }
        if (this.speedPlayer.getValue().booleanValue()) {
            final DecimalFormat df = new DecimalFormat("##.#");

            final double deltaX = Minecraft.getMinecraft().player.posX - Minecraft.getMinecraft().player.prevPosX;
            final double deltaZ = Minecraft.getMinecraft().player.posZ - Minecraft.getMinecraft().player.prevPosZ;
            final float tickRate = (Minecraft.getMinecraft().timer.tickLength / 1000.0f);

            final String BPSText = df.format((MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ) / tickRate)*3.6);
            String speed = (Object)ChatFormatting.GRAY + "Speed" + ChatFormatting.GRAY + " [" + (Object)ChatFormatting.WHITE + "" + BPSText  + "km/h" + (Object)ChatFormatting.GRAY + "]";
            Charon.fontManager.drawString(speed, resolution.getScaledWidth() - 2 - Charon.fontManager.getStringWidth(speed), resolution.getScaledHeight() - 20, -1);

        }
        if (this.crystalInfo.getValue().booleanValue() ){
            String crystalStats;
            if(AutoCrystal.getInstance().isEnabled()){
                crystalStats = ChatFormatting.GREEN + "ENABLED";
            } else {
                crystalStats = ChatFormatting.RED + "DISABLED";
            }
            String crinfo = crystalStats + " | " + ChatFormatting.WHITE + "[target:" +  ChatFormatting.RESET + AutoCrystal.getInstance().getSuffix()  + ChatFormatting.WHITE  + "]" + " | " + "MD:[" +  ChatFormatting.RESET + AutoCrystal.getInstance().minDamage.getValue() +  ChatFormatting.WHITE + "]" ;
            Charon.fontManager.drawString(crinfo, crystalInfoX.getValue(), crystalInfoY.getValue(), this.pvpInfoSync.getValue() != false ? Charon.INSTANCE.getColorManager().getColorAsInt() : ColorUtil.getRainbow(10000, 0, (float)this.rainbowSaturation.getValue().intValue() / 255.0f));
        }

        if (this.armorHud.getValue().booleanValue()) {
            int width = resolution.getScaledWidth();
            int height = resolution.getScaledHeight();
            GlStateManager.enableTexture2D();
            int i2 = width / 2;
            int iteration = 0;
            int y = height - 55 - (this.mc.player.isInWater() && this.mc.playerController.gameIsSurvivalOrAdventure() ? 10 : 0);
            for (ItemStack is : this.mc.player.inventory.armorInventory) {
                ++iteration;
                if (is.isEmpty()) continue;
                int x = i2 - 90 + (9 - iteration) * 20 + 2;
                GlStateManager.enableDepth();
                this.mc.getRenderItem().zLevel = 200.0f;
                this.mc.getRenderItem().renderItemAndEffectIntoGUI(is, x, y);
                this.mc.getRenderItem().renderItemOverlayIntoGUI(this.mc.fontRenderer, is, x, y, "");
                this.mc.getRenderItem().zLevel = 0.0f;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                String s = is.getCount() > 1 ? is.getCount() + "" : "";
                Charon.fontManager.drawString(s, x + 19 - 2 - Charon.fontManager.getStringWidth(s), y + 9, 0xFFFFFF);
                int dmg = (int)ItemUtil.getDamageInPercent(is);
                Charon.fontManager.drawString(dmg + "", (float)(x + 8) - (float) Charon.fontManager.getStringWidth(dmg + "") / 2.0f, y - 8, is.getItem().getRGBDurabilityForDisplay(is));
            }
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
        if (this.coords.getValue().booleanValue()) {
            String netherX = "";
            String netherZ = "";
            if (this.netherCoords.getValue().booleanValue()) {
                if (this.mc.player.dimension == 0) {
                    netherX = String.format("%.1f", this.mc.player.posX / 8.0);
                    netherZ = String.format("%.1f", this.mc.player.posZ / 8.0);
                } else {
                    netherX = String.format("%.1f", this.mc.player.posX * 8.0);
                    netherZ = String.format("%.1f", this.mc.player.posZ * 8.0);
                }
            }
            Charon.fontManager.drawString((Object)ChatFormatting.GRAY + "XYZ " + (Object)ChatFormatting.WHITE + String.format("%.1f", this.mc.player.posX) + ", " + String.format("%.1f", this.mc.player.posY) + ", " + String.format("%.1f", this.mc.player.posZ) + (this.netherCoords.getValue() != false ? (Object)ChatFormatting.GRAY + " [" + (Object)ChatFormatting.WHITE + netherX + ", " + netherZ + (Object)ChatFormatting.GRAY + "]" : ""), 2.0f, resolution.getScaledHeight() - 10, -1);
        }
        if (this.pvpInfo.getValue().booleanValue()) {
            String[] array = new String[]{"Hit", "Pla", String.valueOf(ItemUtil.getItemCount(Items.TOTEM_OF_UNDYING)), "PING " + (this.mc.getConnection() != null && this.mc.getConnection().getPlayerInfo(this.mc.player.getUniqueID()) != null ? Integer.valueOf(this.mc.getConnection().getPlayerInfo(this.mc.player.getUniqueID()).getResponseTime()) : "-1"), "LBY"};
            EntityPlayer player = CombatUtil.getTarget(AutoCrystal.getInstance().range.getValue().floatValue());
            int offset = 0;
            Charon.fontManager.drawString("charon.eu", 1.0f, this.pvpInfoY.getValue() - 10, this.pvpInfoSync.getValue() != false ? Charon.INSTANCE.getColorManager().getColorAsInt() : ColorUtil.getRainbow(10000, 0, (float)this.rainbowSaturation.getValue().intValue() / 255.0f));
            for (i = 0; i < 5; ++i) {
                String s = array[i];
                Charon.fontManager.drawString(s, 1.0f, this.pvpInfoY.getValue() + offset, this.getColor(s, player));
                offset += 9;
            }
        }
        if (this.arrayList.getValue().booleanValue()) {
            if (this.sortTimer.hasReached(this.sortDelay.getValue().intValue())) {
                this.modules.sort(this.sortModeSetting.getValue() == SortMode.LENGTH ? Comparator.comparingDouble(m -> -Charon.fontManager.getStringWidth(this.getFullName((Module)m))) : Comparator.comparing(this::getFullName));
                this.sortTimer.reset();
            }
            int offset = 2;
            float restore = -3.0f;
            int size = this.modules.size();
            for (i = 0; i < size; ++i) {
                Module module = this.modules.get(i);
                if (!module.isEnabled() && (this.extendedAmount.get(module) == null || !(this.extendedAmount.get(module).floatValue() > restore)) || !module.drawn.getValue().booleanValue()) continue;
                String name = this.getFullName(module);
                float openingTarget = Charon.fontManager.getStringWidth(name);
                float target = module.isEnabled() ? openingTarget : restore;
                float newAmount = this.extendedAmount.get(module) != null ? this.extendedAmount.get(module).floatValue() : 0.0f;
                newAmount = (float)((double)newAmount + 1.5 * (double)((float)this.animationSpeed.getValue().intValue() / (float)Minecraft.getDebugFPS()) * (double)(module.isEnabled() ? 1 : -1));
                float f = newAmount = module.isEnabled() ? Math.min(target, newAmount) : Math.max(target, newAmount);
                if (!module.isEnabled() && newAmount < 0.0f) {
                    newAmount = restore;
                }
                if (module.isEnabled() && target - newAmount < 1.0f) {
                    newAmount = target;
                }
                float percent = newAmount / openingTarget;
                this.extendedAmount.put(module, Float.valueOf(newAmount));
                Charon.fontManager.drawString(name, (float)(resolution.getScaledWidth() - 2) - (this.extendedAmount.get(module) != null ? this.extendedAmount.get(module).floatValue() : (float) Charon.fontManager.getStringWidth(name)), offset, this.colorSetting.getValue() == ColorMode.RAINBOW ? ColorUtil.getRainbow(this.rainbowSpeed.getValue(), offset * this.rainbowFactor.getValue(), (float)this.rainbowSaturation.getValue().intValue() / 255.0f) : (this.colorSetting.getValue() == ColorMode.CATEGORY ? module.getCategory().getColor() : Charon.INSTANCE.getColorManager().getColorAsInt()));
                offset = (int)((float)offset + 10.0f * percent);
            }
        }
    }

    private int getColor(String s, EntityPlayer entityPlayer) {
        int green = 65280;
        int red = 0xFF0000;
        switch (s) {
            case "Hit": {
                if (entityPlayer != null && AutoCrystal.getInstance().isEnabled() && this.mc.player.getDistanceSq((Entity)entityPlayer) < AutoCrystal.getInstance().breakRange.getValue().floatValue()) {
                    return 65280;
                }
                return 0xFF0000;
            }
            case "Pla": {
                if (entityPlayer != null && AutoCrystal.getInstance().isEnabled() && this.mc.player.getDistanceSq((Entity)entityPlayer) < AutoCrystal.getInstance().placeRange.getValue().floatValue()) {
                    return 65280;
                }
                return 0xFF0000;
            }
            case "LBY": {
                if (entityPlayer != null) {
                    int holeInfo = CombatUtil.isInHoleInt(entityPlayer);
                    if (holeInfo == 0) {
                        return 0xFF0000;
                    }
                    if (holeInfo == 1) {
                        return 65280;
                    }
                    if (holeInfo == 2) {
                        return 16727040;
                    }
                }
                return 0xFF0000;
            }
        }
        if (s.startsWith("PING")) {
            int num = Integer.parseInt(s.substring(5));
            if (num >= 100) {
                return 0xFF0000;
            }
            return 65280;
        }
        int num = Integer.parseInt(s);
        if (num > 0) {
            return 65280;
        }
        return 0xFF0000;
    }

    public void drawCenteredString(String text, int x, int y, int color) {
        Charon.fontManager.drawString(text, x - Charon.fontManager.getStringWidth(text) / 2, y, color);
    }

    private String getFullName(Module m) {
        if (m.getSuffix().length() == 0) {
            return m.getLabel();
        }
        StringBuilder builder = new StringBuilder();
        builder.append(m.getLabel());
        builder.append((Object)ChatFormatting.WHITE);
        builder.append(" ");
        builder.append(m.getSuffix());
        return builder.toString();
    }

    public static enum SortMode {
        ABC,
        LENGTH;

    }

    public static enum ColorMode {
        CATEGORY,
        SYNC,
        RAINBOW;

    }
}

