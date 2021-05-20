/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.event.events;

import tcb.bces.event.Event;

public class MoveEvent
extends Event {
    private double motionX;
    private double motionY;
    private double motionZ;

    public MoveEvent(double motionX, double motionY, double motionZ) {
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
    }

    public final double getMotionX() {
        return this.motionX;
    }

    public final double getMotionY() {
        return this.motionY;
    }

    public final double getMotionZ() {
        return this.motionZ;
    }

    public void setMotionX(double x) {
        this.motionX = x;
    }

    public void setMotionY(double y) {
        this.motionY = y;
    }

    public void setMotionZ(double z) {
        this.motionZ = z;
    }
}

