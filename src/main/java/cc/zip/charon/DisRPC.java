package cc.zip.charon;

import cc.zip.charon.client.modules.combat.AutoCrystal;
import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;

public class DisRPC {
    private static String discordID = "818218548588838962";
    private static DiscordRichPresence discordRichPresence = new DiscordRichPresence();
    private static DiscordRPC discordRPC = DiscordRPC.INSTANCE;
    private static Thread thread;
    public static void startRPC() {
        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
        eventHandlers.disconnected = (var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + var1 + "var2: " + var2);
        discordRPC.Discord_Initialize(discordID, eventHandlers, true, discordID);
        DisRPC.discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        DisRPC.discordRichPresence.largeImageKey = "charon2";
        DisRPC.discordRichPresence.details = "charon members: ll1l1lll1lll1";
        DisRPC.discordRichPresence.largeImageText = " Bv 1.0.2-edition ";
        DisRPC.discordRichPresence.state = null;
        discordRPC.Discord_UpdatePresence(discordRichPresence);

    }

    public static void stopRPC() {
        discordRPC.Discord_Shutdown();
        discordRPC.Discord_ClearPresence();
    }
}