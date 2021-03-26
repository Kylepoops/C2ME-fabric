package org.yatopiamc.c2me.mixin.fixes.biome_source;

import net.minecraft.util.math.noise.SimplexNoiseSampler;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.biome.source.TheEndBiomeSourceConfig;
import net.minecraft.world.gen.ChunkRandom;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TheEndBiomeSource.class)
public class MixinTheEndBiomeSource {

    private ThreadLocal<SimplexNoiseSampler> noiseSamplerThreadLocal = new ThreadLocal<>();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(TheEndBiomeSourceConfig config, CallbackInfo ci) {
        noiseSamplerThreadLocal = ThreadLocal.withInitial(() -> {
            // TODO [VanillaCopy]
            final ChunkRandom chunkRandom = new ChunkRandom(config.getSeed());
            chunkRandom.consume(17292);
            return new SimplexNoiseSampler(chunkRandom);
        });
    }

    @Redirect(method = "getNoiseRange", at = @At(value = "FIELD", target = "Lnet/minecraft/world/biome/source/TheEndBiomeSource;noise:Lnet/minecraft/util/math/noise/SimplexNoiseSampler;", opcode = Opcodes.GETFIELD))
    private SimplexNoiseSampler redirectGetNoiseRangeNoise(TheEndBiomeSource theEndBiomeSource) {
        return noiseSamplerThreadLocal.get();
    }

}
