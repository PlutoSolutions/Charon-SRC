package cc.zip.charon.client.modules.combat;

import cc.zip.charon.api.property.Setting;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;

@ModuleManifest(label="CevBreaker", category= Module.Category.COMBAT)
public class CevBreaker
        extends Module {

    public final Setting<Targets> target = this.register(new Setting<Targets>("Target", Targets.Nearest));
    public static enum Targets { Nearest, Looking;}

    public final Setting<BreakMode> breakCrystal = this.register(new Setting<BreakMode>("BreakCrystal", BreakMode.Vanilla));
    public static enum BreakMode { Vanilla, Packet;}

    public final Setting<BreakBlocks> breakBlock = this.register(new Setting<BreakBlocks>("BreakBlocks", BreakBlocks.Normal));
    public static enum BreakBlocks { Normal, Packet;}

    private final Setting<Double> enemyRange = this.register(new Setting<Double>("EnemyRange", 6.0, 0.0, 6.0));
    private final Setting<Integer> preRotationDelay = this.register(new Setting<Integer>("Block/Place", 0, 0, 20));
    private final Setting<Integer> afterRotationDelay = this.register(new Setting<Integer>("Block/Place", 0, 0, 20));
    private final Setting<Integer> supDelay = this.register(new Setting<Integer>("Block/Place", 1, 0, 4));
    private final Setting<Integer> crystalDelay = this.register(new Setting<Integer>("Block/Place", 2, 0, 20));
    private final Setting<Integer> blocksPerTick = this.register(new Setting<Integer>("Block/Place", 4, 2, 6));
    private final Setting<Integer> hitDelay = this.register(new Setting<Integer>("Block/Place", 2, 0, 20));
    private final Setting<Integer> midHitDelay = this.register(new Setting<Integer>("Block/Place", 1, 0, 20));
    private final Setting<Integer> endDelay = this.register(new Setting<Integer>("Block/Place", 1, 0, 20));
    private final Setting<Integer> pickSwitchTick = this.register(new Setting<Integer>("Block/Place", 100, 0, 500));

    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Info", false));
    private final Setting<Boolean> confirmBreak = this.register(new Setting<Boolean>("Info", false));
    private final Setting<Boolean> confirmPlace = this.register(new Setting<Boolean>("Info", false));
    private final Setting<Boolean> antiWeakness = this.register(new Setting<Boolean>("Info", false));
    private final Setting<Boolean> switchSword = this.register(new Setting<Boolean>("Info", false));
    private final Setting<Boolean> fastPlace = this.register(new Setting<Boolean>("Info", false));
    private final Setting<Boolean> fastBreak = this.register(new Setting<Boolean>("Info", false));
    private final Setting<Boolean> trapPlayer = this.register(new Setting<Boolean>("Info", false));
    private final Setting<Boolean> antiStep = this.register(new Setting<Boolean>("Info", false));
    private final Setting<Boolean> placeCrystal = this.register(new Setting<Boolean>("Info", false));
    private final Setting<Boolean> forceRotation = this.register(new Setting<Boolean>("Info", false));
    private final Setting<Boolean> forceBreaker= this.register(new Setting<Boolean>("Info", false));

    public static int cur_item = -1;
    public static boolean isActive = false;
    public static boolean forceBrk = false;
    

}

