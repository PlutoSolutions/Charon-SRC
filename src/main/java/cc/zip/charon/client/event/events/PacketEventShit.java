package cc.zip.charon.client.event.events;

import cc.zip.charon.client.event.EventStage;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class PacketEventShit
        extends EventStage {
    private final Packet<?> packet;

    public PacketEventShit(int stage, Packet<?> packet) {
        super(stage);
        this.packet = packet;
    }

    public <T extends Packet<?>> T getPacket() {
        return (T) this.packet;
    }

    @Cancelable
    public static class Send
            extends PacketEventShit {
        public Send(int stage, Packet<?> packet) {
            super(stage, packet);
        }
    }

    @Cancelable
    public static class Receive
            extends PacketEventShit {
        public Receive(int stage, Packet<?> packet) {
            super(stage, packet);
        }
    }
}