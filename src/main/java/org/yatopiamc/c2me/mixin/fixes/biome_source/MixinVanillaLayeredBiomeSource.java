package org.yatopiamc.c2me.mixin.fixes.biome_source;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.layer.BiomeLayers;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSourceConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VanillaLayeredBiomeSource.class)
public class MixinVanillaLayeredBiomeSource {

    private ThreadLocal<BiomeLayerSampler> layerSamplerThreadLocal = new ThreadLocal<>();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(VanillaLayeredBiomeSourceConfig config, CallbackInfo ci) {
        layerSamplerThreadLocal = ThreadLocal.withInitial(() -> BiomeLayers.build(config.getSeed(), config.getGeneratorType(), config.getGeneratorSettings()));
    }

    /**
     * @author ishland
     * @reason use threadlocal
     */
    @Overwrite
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.layerSamplerThreadLocal.get().sample(biomeX, biomeZ);
    }

}
