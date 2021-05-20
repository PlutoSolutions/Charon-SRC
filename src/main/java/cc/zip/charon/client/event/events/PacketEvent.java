package cc.zip.charon.client.event.events;

import net.minecraft.network.Packet;
import tcb.bces.event.EventCancellable;

public class PacketEvent
        extends EventCancellable {
    final Packet packet;

    public PacketEvent(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public static class Send
            extends PacketEvent {
        public Send(Packet packet) {
            super(packet);
        }
    }

    public static class Receive
            extends PacketEvent {
        public Receive(Packet packet) {
            super(packet);
        }
    }
}