/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.MobEffects
 */
package cc.zip.charon.client.modules.movement;

import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.EntityUtil;
import cc.zip.charon.api.util.MathUtil;
import cc.zip.charon.api.util.MotionUtil;
import cc.zip.charon.client.event.events.MoveEvent;
import cc.zip.charon.client.event.events.UpdateEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import cc.zip.charon.client.modules.combat.AutoCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import tcb.bces.listener.Subscribe;

@ModuleManifest(label="Speed", category= Module.Category.MOVEMENT)
public class Speed
extends Module {
    private final Setting<Integer> speed = this.register(new Setting<Integer>("Speed", 10, 1, 30, v -> this.page.getValue() == Page.Vanila));
    private final Setting<Page> page = this.register(new Setting<Page>("Page", Page.Strafe));
    public final Setting<Float> yspeed = this.register(new Setting<Float>("Y-Speed", Float.valueOf(-1.0f), Float.valueOf(0.1f), Float.valueOf(10.0f), v -> this.page.getValue() == Page.Yport));
    private final Setting<Boolean> placeHolder = this.register(new Setting<Boolean>("PlaceHolder", true));
    private int stage = 1;
    private double moveSpeed;
    private double lastDist;
    public static enum Page {
        Strafe,
        Vanila,
        Yport;

    }
    @Override
    public void onEnable() {
        if (this.mc.player == null) {
            return;
        }
        if (page.getValue() == Page.Strafe) {
            this.setSuffix("Strafe");
        }
        if (page.getValue() == Page.Yport) {
            this.setSuffix("Yport");
        }
        if (page.getValue() == Page.Vanila) {
            this.setSuffix("Vanila");
        }
        this.lastDist = 0.0;
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (this.placeHolder.getValue().booleanValue()) {
            return;
        }
        if (page.getValue() == Page.Strafe){
            this.lastDist = Math.sqrt((this.mc.player.posX - this.mc.player.prevPosX) * (this.mc.player.posX - this.mc.player.prevPosX) + (this.mc.player.posZ - this.mc.player.prevPosZ) * (this.mc.player.posZ - this.mc.player.prevPosZ));
            if (this.lastDist > 5.0) {
                this.lastDist = 0.0;
            }
        }
        if (page.getValue() == Page.Vanila){
            double[] calc = MathUtil.directionSpeed(this.speed.getValue() / 10.0); mc.player.motionX = calc[0]; mc.player.motionZ = calc[1];

        }
        if (mc.player == null || mc.world == null) { disable(); return; }
        if (page.getValue() == Page.Yport) {
            handleYPortSpeed();
        }
    }

    @Subscribe
    public void onMotion(MoveEvent event) {
        if (this.placeHolder.getValue().booleanValue()) {
            return;
        }
        if (page.getValue() == Page.Strafe) {
            double forward = this.mc.player.movementInput.moveForward;
            double strafe = this.mc.player.movementInput.moveStrafe;
            double yaw = this.mc.player.rotationYaw;
            switch (this.stage) {
                case 0: {
                    ++this.stage;
                    this.lastDist = 0.0;
                    break;
                }
                case 2: {
                    double motionY = 0.4;
                    if (this.mc.player.moveForward == 0.0f && this.mc.player.moveStrafing == 0.0f || !this.mc.player.onGround)
                        break;
                    if (this.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                        motionY += (double) ((float) (this.mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1f);
                    }
                    this.mc.player.motionY = motionY;
                    event.setMotionY(this.mc.player.motionY);
                    this.moveSpeed *= this.mc.player.isPotionActive(MobEffects.SPEED) ? 2.1499 : 2.1499;
                    break;
                }
                case 3: {
                    this.moveSpeed = this.lastDist - (this.mc.player.isPotionActive(MobEffects.SPEED) ? 0.658 : 0.71) * (this.lastDist - this.getBaseMoveSpeed());
                    break;
                }
                default: {
                    if ((this.mc.world.getCollisionBoxes((Entity) this.mc.player, this.mc.player.getEntityBoundingBox().offset(0.0, this.mc.player.motionY, 0.0)).size() > 0 || this.mc.player.collidedVertically) && this.stage > 0) {
                        this.stage = this.mc.player.moveForward == 0.0f && this.mc.player.moveStrafing == 0.0f ? 0 : 1;
                    }
                    this.moveSpeed = this.lastDist - this.lastDist / 159.0;
                }
            }
            this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
            if (forward == 0.0 && strafe == 0.0) {
                event.setMotionX(0.0);
                event.setMotionZ(0.0);
            }
            if (forward != 0.0 && strafe != 0.0) {
                forward *= Math.sin(0.7853981633974483);
                strafe *= Math.cos(0.7853981633974483);
            }
            event.setMotionX((forward * this.moveSpeed * -Math.sin(Math.toRadians(yaw)) + strafe * this.moveSpeed * Math.cos(Math.toRadians(yaw))) * 0.99);
            event.setMotionZ((forward * this.moveSpeed * Math.cos(Math.toRadians(yaw)) - strafe * this.moveSpeed * -Math.sin(Math.toRadians(yaw))) * 0.99);
            ++this.stage;

        }
    }
    private void handleYPortSpeed() {
        if (!MotionUtil.isMoving(mc.player) || mc.player.isInWater() && mc.player.isInLava() || mc.player.collidedHorizontally) {
            return;
        }

        if (mc.player.onGround) {
            EntityUtil.setTimer(1.15f);
            mc.player.jump();
            MotionUtil.setSpeed(mc.player, MotionUtil.getBaseMoveSpeed() + yspeed.getValue());
        } else {
            mc.player.motionY = -1;
            EntityUtil.resetTimer();
        }
    }


    private double getBaseMoveSpeed() {
        double baseSpeed = 0.272;
        if (this.mc.player.isPotionActive(MobEffects.SPEED)) {
            int amplifier = this.mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double) amplifier;
        }
        return baseSpeed;
    }
}

