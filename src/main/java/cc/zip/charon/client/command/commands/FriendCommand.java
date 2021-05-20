/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.command.commands;

import cc.zip.charon.Charon;
import cc.zip.charon.client.command.Command;
import cc.zip.charon.client.command.CommandManifest;

@CommandManifest(label="Friend", aliases={"friends", "friend"})
public class FriendCommand
extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            return;
        }
        try {
            String name = args[2];
            switch (args[1].toUpperCase()) {
                case "ADD": {
                    Charon.INSTANCE.getFriendManager().addFriend(name);
                    break;
                }
                case "DEL": {
                    Charon.INSTANCE.getFriendManager().removeFriend(name);
                    break;
                }
                case "DELETE": {
                    Charon.INSTANCE.getFriendManager().removeFriend(name);
                    break;
                }
                case "CLEAR": {
                    Charon.INSTANCE.getFriendManager().clearFriends();
                    break;
                }
                case "INSIDE": {
                    Charon.INSTANCE.getFriendManager().clearFriends();
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

