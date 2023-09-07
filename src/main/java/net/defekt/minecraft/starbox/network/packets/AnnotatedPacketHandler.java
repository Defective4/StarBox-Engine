package net.defekt.minecraft.starbox.network.packets;

import net.defekt.minecraft.starbox.network.packets.serverbound.ServerboundPacket;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AnnotatedPacketHandler implements PacketHandler {

    @Override
    public void packetReceiving(ServerboundPacket packet) {
        for (Method method : getClass().getMethods()) {
            if (method.isAnnotationPresent(PacketHandlerMethod.class)) {
                Class<?>[] params = method.getParameterTypes();
                if (params.length == 1 && params[0] == packet.getClass()) {
                    try {
                        method.invoke(this, packet);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
