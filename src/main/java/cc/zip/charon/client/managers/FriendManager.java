/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.reflect.TypeToken
 *  com.google.gson.GsonBuilder
 *  net.minecraft.entity.player.EntityPlayer
 */
package cc.zip.charon.client.managers;

import cc.zip.charon.api.util.MessageUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

public class FriendManager {
    private List<Friend> friends = new ArrayList<Friend>();
    private File directory;

    public void init() {
        if (!this.directory.exists()) {
            try {
                this.directory.createNewFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.loadFriends();
    }

    public void unload() {
        this.saveFriends();
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public void saveFriends() {
        if (this.directory.exists()) {
            try (FileWriter writer = new FileWriter(this.directory);){
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(this.friends));
            }
            catch (IOException e) {
                this.directory.delete();
            }
        }
    }

    public void loadFriends() {
        if (!this.directory.exists()) {
            return;
        }
        try (FileReader inFile = new FileReader(this.directory);){
            this.friends = new ArrayList<Friend>((Collection)new GsonBuilder().setPrettyPrinting().create().fromJson((Reader)inFile, new TypeToken<ArrayList<Friend>>(){}.getType()));
            if (this.friends == null) {
                this.friends = new ArrayList<Friend>();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void addFriend(String name) {
        MessageUtil.sendClientMessage("Added " + name + " as a friend ", false);
        this.friends.add(new Friend(name));
    }

    public Friend getFriend(String ign) {
        for (Friend friend : this.friends) {
            if (!friend.getName().equalsIgnoreCase(ign)) continue;
            return friend;
        }
        return null;
    }

    public boolean isFriend(String ign) {
        return this.getFriend(ign) != null;
    }

    public boolean isFriend(EntityPlayer ign) {
        return this.getFriend(ign.getName()) != null;
    }

    public void clearFriends() {
        this.friends.clear();
    }

    public void removeFriend(String name) {
        Friend f = this.getFriend(name);
        if (f != null) {
            this.friends.remove(f);
        }
    }

    public static class Friend {
        final String name;

        public Friend(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

