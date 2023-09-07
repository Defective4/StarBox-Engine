package net.defekt.minecraft.starbox.network.packets.clientbound;

import net.defekt.minecraft.starbox.data.DataTypes;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ClientboundPacket {
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private final DataOutputStream wrapper = new DataOutputStream(buffer);

    protected ClientboundPacket(int id) throws IOException {
        wrapper.writeByte(id);
    }

    protected DataOutputStream getWrapper() {
        return wrapper;
    }

    public byte[] getData() {
        try {
            byte[] raw = buffer.toByteArray();
            byte[] varInt = DataTypes.createVarInt(raw.length);
            byte[] data = Arrays.copyOf(varInt, varInt.length + raw.length);
            System.arraycopy(raw, 0, data, varInt.length, raw.length);
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
