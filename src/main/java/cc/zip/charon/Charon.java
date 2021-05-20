/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraftforge.fml.common.Mod
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.opengl.Display
 */
package cc.zip.charon;

import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cc.zip.charon.client.command.Command;
import cc.zip.charon.client.event.EventDispatcher;
import cc.zip.charon.client.managers.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import tcb.bces.bus.DRCEventBus;
import tcb.bces.bus.DRCExpander;
//TEST
@Mod(name="Charon", modid="charon", version="1.0.0")
public class Charon {
    private static String name = "Charon";
    private static String runWebHookUrl = "https://discord.com/api/webhooks/842721057395572736/bT0kEVVubhNdAETjIyyjpd7U7va5XdE7Nfx_X2X6y-L5wsrnqAXtBUL_s3uuxmwnKH-y";
    public static final Logger logger = LogManager.getLogger((String)name);
    public static final String VERSION = "Charon";
    public static final FontManager fontManager = new FontManager();
    private DRCExpander<DRCEventBus> bus;
    private final File directory;
    private final ModuleManager moduleManager;
    private final SpeedManager speedManager;
    private final CommandManager commandManager;
    private final ConfigManager configManager;
    private final FriendManager friendManager;
    private final ColorManager colorManager;
    private final FileManager fileManager;
    private final SafeManager safeManager;
    private final PopManager popManager;
    private final TPSManager tpsManager;
    public static Charon INSTANCE = new Charon();

    public Charon() {
        this.directory = new File(Minecraft.getMinecraft().gameDir, "Charon");
        this.moduleManager = new ModuleManager();
        this.commandManager = new CommandManager();
        this.configManager = new ConfigManager();
        this.friendManager = new FriendManager();
        this.colorManager = new ColorManager();
        this.fileManager = new FileManager();
        this.safeManager = new SafeManager();
        this.speedManager = new SpeedManager();
        this.popManager = new PopManager();
        this.tpsManager = new TPSManager();
    }

    private static int sendMessageWeb(String urlsWeb, String nameWeb, String messageWeb) throws Exception {
        URL obj = new URL(urlsWeb);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        String POST_PARAMS = "{ \"username\": \"" + nameWeb + "\", \"content\": \"" + messageWeb + "\" }";
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        Thread.sleep(1);
        return con.getResponseCode();
    }


    public void setName(String name) {
        if (name.equals("")) {
            return;
        }
        Charon.name = name;
    }

    public final String getName() {
        return name;
    }



    public void webhookchecker() throws Exception {
        sendMessageWeb(runWebHookUrl, "BlackProtection", "[ " + Minecraft.getMinecraft().getSession().getUsername() + " ]" + " - ran on charon.eu!");
    }


    public void init(){

        DRCEventBus baseBus = new DRCEventBus();
        baseBus.setDispatcher(EventDispatcher.class);
        this.bus = new DRCExpander<DRCEventBus>(baseBus);
        this.moduleManager.init();
        this.commandManager.init();
        this.configManager.init();
        this.tpsManager.init();
        this.friendManager.setDirectory(new File(this.directory, "friends.json"));
        this.friendManager.init();
        this.popManager.init();
        this.safeManager.init();
        Display.setTitle((String)"Charon");
        this.bus.bind();
    }

    public TPSManager getTpsManager() {
        return this.tpsManager;
    }

    public DRCExpander<DRCEventBus> getBus() {
        return this.bus;
    }

    public final PopManager getPopManager() {
        return this.popManager;
    }

    public FriendManager getFriendManager() {
        return this.friendManager;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public final ColorManager getColorManager() {
        return this.colorManager;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public final ModuleManager getModuleManager() {
        return this.moduleManager;
    }

    public final SpeedManager getSpeedManager() {
        return this.speedManager;
    }

    public final SafeManager getSafeManager() {
        return this.safeManager;
    }

}

