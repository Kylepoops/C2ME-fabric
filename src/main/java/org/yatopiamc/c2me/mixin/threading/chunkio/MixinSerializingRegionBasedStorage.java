package org.yatopiamc.c2me.mixin.threading.chunkio;

import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.datafixer.NbtOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.storage.RegionBasedStorage;
import net.minecraft.world.storage.SerializingRegionBasedStorage;
import net.minecraft.world.storage.StorageIoWorker;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.yatopiamc.c2me.common.threading.chunkio.C2MECachedRegionStorage;
import org.yatopiamc.c2me.common.threading.chunkio.ISerializingRegionBasedStorage;

import java.io.File;

@Mixin(SerializingRegionBasedStorage.class)
public abstract class MixinSerializingRegionBasedStorage implements ISerializingRegionBasedStorage {

    @Shadow protected abstract <T> void method_20368(ChunkPos chunkPos, DynamicOps<T> dynamicOps, @Nullable T object);

    @Override
    public void update(ChunkPos pos, CompoundTag tag) {
        this.method_20368(pos, NbtOps.INSTANCE, tag);
    }

    @Redirect(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/world/storage/StorageIoWorker"))
    private StorageIoWorker onStorageIoInit(RegionBasedStorage storage, String name) {
        return new C2MECachedRegionStorage(storage, name);
    }
}
