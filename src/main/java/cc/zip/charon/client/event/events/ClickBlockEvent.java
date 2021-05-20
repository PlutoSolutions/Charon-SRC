/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 */
package cc.zip.charon.client.event.events;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import tcb.bces.event.EventCancellable;

public class ClickBlockEvent
extends EventCancellable {
    final BlockPos pos;
    final EnumFacing facing;

    public ClickBlockEvent(BlockPos pos, EnumFacing facing) {
        this.pos = pos;
        this.facing = facing;
    }

    public final BlockPos getPos() {
        return this.pos;
    }

    public final EnumFacing getFacing() {
        return this.facing;
    }
}

