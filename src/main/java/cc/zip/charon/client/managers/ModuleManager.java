/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.managers;

import java.util.ArrayList;
import java.util.List;

import cc.zip.charon.Charon;
import cc.zip.charon.api.interfaces.Minecraftable;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.event.events.KeyEvent;
import cc.zip.charon.client.modules.client.*;
import cc.zip.charon.client.modules.combat.*;
import cc.zip.charon.client.modules.misc.*;
import cc.zip.charon.client.modules.movement.*;
import cc.zip.charon.client.modules.oyvey.NewAutoTrap;
import cc.zip.charon.client.modules.oyvey.NewHoleFiller;
import cc.zip.charon.client.modules.oyvey.NewOffhand;
import cc.zip.charon.client.modules.oyvey.NewSurround;
import cc.zip.charon.client.modules.player.*;
import cc.zip.charon.client.modules.visual.*;
import tcb.bces.listener.IListener;
import tcb.bces.listener.Subscribe;

public class ModuleManager
implements Minecraftable,
IListener {
    private final List<Module> modules = new ArrayList<Module>();

    public void init() {
        this.register(new CameraClip());
        this.register(new Anchor());
        this.register(new NewOffhand());
        this.register(new NewHoleFiller());
        this.register(new NewSurround());
        this.register(new NewAutoTrap());
        this.register(new BotNarrator());
        this.register(new NoFallBypass());
        this.register(new DiscordRPCinit());
        this.register(new ArmorWarning());
        this.register(new AutoJosifek());
        this.register(new HitMarkers());
        this.register(new CSGO());
        this.register(new GuiGradient());
        this.register(new Refill());
        this.register(new ClickGui());
        this.register(new Colors());
        this.register(new FontModule());
        this.register(new PopCounter());
        this.register(new MiddleClick());
        this.register(new Manage());
        this.register(new AntiChainPop());
        this.register(new AutoCrystal());
        this.register(new AutoTrap());
        this.register(new Burrow());
        this.register(new Criticals());
        this.register(new HoleFiller());
        this.register(new Offhand());
        this.register(new Feetplace());
        this.register(new FakePlayer());
        this.register(new PacketCanceller());
        this.register(new MultiTask());
        this.register(new NoRotate());
        this.register(new AutoRespawn());
        this.register(new ReverseStep());
        this.register(new Speed());
        this.register(new Step());
        this.register(new WebFall());
        this.register(new Interact());
        this.register(new SpeedMine());

        this.register(new BlockHighlight());
        this.register(new EnchantColor());
        this.register(new EntityESP());
        this.register(new HoleESP());
        this.register(new Nametags());
        this.register(new HUD());
        this.register(new SkyColor());
        this.register(new ViewmodelChanger());
        this.register(new ShulkerPreview());
        this.register(new VoidESP());
        this.register(new NoRender());
        this.register(new Skeleton());
        this.register(new LogOutSpots());
        this.register(new Chams());
        this.register(new MinDmgSet());
        this.register(new ElytraFly());
        Charon.INSTANCE.getBus().register(this);
        this.modules.forEach(Module::onLoad);
    }

    @Subscribe
    public void onKey(KeyEvent event) {
        int size = this.modules.size();
        for (int i = 0; i < size; ++i) {
            Module m = this.modules.get(i);
            if (m.getKey() != event.getKey()) continue;
            m.toggle();
        }
    }

    public void onRender2D() {
        if (Minecraftable.mc.player == null || Minecraftable.mc.world == null) {
            return;
        }
        int size = this.modules.size();
        for (int i = 0; i < size; ++i) {
            Module m = this.modules.get(i);
            if (!m.isEnabled()) continue;
            m.onRender2D();
        }
    }

    public void onRender3D() {
        if (Minecraftable.mc.player == null || Minecraftable.mc.world == null) {
            return;
        }
        int size = this.modules.size();
        for (int i = 0; i < size; ++i) {
            Module m = this.modules.get(i);
            if (!m.isEnabled()) continue;
            m.onRender3D();
        }
    }

    private void register(Module module) {
        this.modules.add(module);
    }

    public final List<Module> getModules() {
        return this.modules;
    }

    public final Module getModuleByClass(Class<?> clazz) {
        Module module = null;
        int size = this.modules.size();
        for (int i = 0; i < size; ++i) {
            Module m = this.modules.get(i);
            if (m.getClass() != clazz) continue;
            module = m;
        }
        return module;
    }

    public final List<Module> getModulesByCategory(Module.Category category) {
        ArrayList<Module> list = new ArrayList<Module>();
        int size = this.modules.size();
        for (int i = 0; i < size; ++i) {
            Module module = this.modules.get(i);
            if (!module.getCategory().equals((Object)category)) continue;
            list.add(module);
        }
        return list;
    }

    public final Module getModuleByLabel(String label) {
        Module module = null;
        int size = this.modules.size();
        for (int i = 0; i < size; ++i) {
            Module m = this.modules.get(i);
            if (!m.getLabel().equalsIgnoreCase(label)) continue;
            module = m;
        }
        return module;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

