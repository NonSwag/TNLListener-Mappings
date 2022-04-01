package net.nonswag.tnl.mappings.v1_16_R3.api.nbt;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.nonswag.tnl.listener.api.nbt.NBTTag;

import javax.annotation.Nonnull;

public class NBT extends NBTTag {

    @Nonnull
    private final NBTTagCompound compound;

    public NBT(@Nonnull NBTTagCompound compound) {
        this.compound = compound;
    }

    @Nonnull
    @Override
    public String get(@Nonnull String s) {
        return versioned().getString(s);
    }

    @Override
    public int getInt(@Nonnull String s) {
        return versioned().getInt(s);
    }

    @Override
    public double getDouble(@Nonnull String s) {
        return versioned().getDouble(s);
    }

    @Override
    public float getFloat(@Nonnull String s) {
        return versioned().getFloat(s);
    }

    @Override
    public long getLong(@Nonnull String s) {
        return versioned().getLong(s);
    }

    @Override
    public short getShort(@Nonnull String s) {
        return versioned().getShort(s);
    }

    @Override
    public byte getByte(@Nonnull String s) {
        return versioned().getByte(s);
    }

    @Override
    public boolean getBoolean(@Nonnull String s) {
        return versioned().getBoolean(s);
    }

    @Override
    public int[] getIntArray(@Nonnull String s) {
        return versioned().getIntArray(s);
    }

    @Override
    public byte[] getByteArray(@Nonnull String s) {
        return versioned().getByteArray(s);
    }

    @Override
    public void set(@Nonnull String s, @Nonnull String s1) {
        versioned().setString(s, s1);
    }

    @Override
    public void set(@Nonnull String s, int i) {
        versioned().setInt(s, i);
    }

    @Override
    public void set(@Nonnull String s, double v) {
        versioned().setDouble(s, v);
    }

    @Override
    public void set(@Nonnull String s, float v) {
        versioned().setFloat(s, v);
    }

    @Override
    public void set(@Nonnull String s, long l) {
        versioned().setLong(s, l);
    }

    @Override
    public void set(@Nonnull String s, short i) {
        versioned().setShort(s, i);
    }

    @Override
    public void set(@Nonnull String s, byte b) {
        versioned().setByte(s, b);
    }

    @Override
    public void set(@Nonnull String s, boolean b) {
        versioned().setBoolean(s, b);
    }

    @Override
    public void set(@Nonnull String s, int[] ints) {
        versioned().setIntArray(s, ints);
    }

    @Override
    public void set(@Nonnull String s, byte[] bytes) {
        versioned().setByteArray(s, bytes);
    }

    @Nonnull
    @Override
    public NBT append(@Nonnull NBTTag tag) {
        if (tag instanceof NBT nbt) versioned().a(nbt.versioned());
        return this;
    }

    @Nonnull
    @Override
    public NBTTagCompound versioned() {
        return compound;
    }
}
