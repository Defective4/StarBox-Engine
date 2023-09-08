package net.defekt.minecraft.starbox.data;

import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.tags.collection.CompoundTag;
import net.defekt.minecraft.starbox.inventory.ItemStack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

public class DataTypes {

    private DataTypes() {}

    public static void writeSlotData(DataOutputStream out, ItemStack item) throws IOException {
        out.writeBoolean(item != null);
        if (item != null) {
            writeVarInt(out, item.getType().getId());
            out.writeByte(item.getCount());
            CompoundTag tag = item.getNbt();
            if (tag != null) new Nbt().toStream(tag, out);
            else out.writeByte(0);
        }
    }

    public static ItemStack readSlotData(DataInputStream in) throws IOException {
        boolean present = in.readBoolean();
        if (!present) return null;
        int id = readVarInt(in);
        int count = in.readByte();
        CompoundTag nbt = null;
        try {
            nbt = new Nbt().fromStream(in);
        } catch (Exception ignored) {}
        Material type = Material.getItemForID(id);
        if (type == null) return null;
        return new ItemStack(type, nbt, count);
    }

    public static void writeUUID(DataOutputStream out, UUID uid) throws IOException {
        out.writeLong(uid.getMostSignificantBits());
        out.writeLong(uid.getLeastSignificantBits());
    }

    public static byte[] createVarInt(int value) {
        byte[] array = new byte[0];
        do {
            byte temp = (byte) (value & 0b01111111);
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            array = Arrays.copyOf(array, array.length + 1);
            array[array.length - 1] = temp;
        } while (value != 0);
        return array;
    }

    public static void writeVarString(OutputStream outputStream, String value) throws IOException {
        byte[] data = value.getBytes(StandardCharsets.UTF_8);
        writeVarInt(outputStream, data.length);
        outputStream.write(data);
    }

    public static String readVarString(DataInputStream inputStream) throws IOException {
        byte[] data = new byte[readVarInt(inputStream)];
        inputStream.readFully(data);
        return new String(data, StandardCharsets.UTF_8);
    }

    public static void writeVarInt(OutputStream outputStream, int value) throws IOException {
        do {
            byte temp = (byte) (value & 0b01111111);
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            outputStream.write(temp);
        } while (value != 0);
    }

    public static void writeVarLong(OutputStream outputStream, long value) throws IOException {
        do {
            byte temp = (byte) (value & 0b01111111);
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            outputStream.write(temp);
        } while (value != 0);
    }

    public static int readVarInt(DataInputStream inputStream) throws IOException {
        int numRead = 0;
        int result = 0;
        byte read;
        do {
            read = inputStream.readByte();
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));
            numRead++;
            if (numRead > 5) {
                throw new IOException("VarInt is too big");
            }
        } while ((read & 0b10000000) != 0);
        return result;
    }
}
