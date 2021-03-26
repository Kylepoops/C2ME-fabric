package org.yatopiamc.c2me.mixin.fixes.biome_source;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(BiomeSource.class)
public class MixinBiomeSource {

    @Shadow
    @Final
    protected Set<BlockState> topMaterials;

    @Shadow
    @Final
    protected Set<Biome> biomes;

    @Mutable
    @Shadow
    @Final
    protected Map<StructureFeature<?>, Boolean> structureFeatures;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo info) {
        this.structureFeatures = new ConcurrentHashMap<>();
    }

    /**
     * @author ishland
     * @reason synchronization
     */
    @Overwrite
    public Set<BlockState> getTopMaterials() {
        if (this.topMaterials.isEmpty())
            synchronized (this) {
                // TODO [VanillaCopy]
                if (this.topMaterials.isEmpty()) {
                    Iterator var1 = this.biomes.iterator();

                    while (var1.hasNext()) {
                        Biome biome = (Biome) var1.next();
                        this.topMaterials.add(biome.getSurfaceConfig().getTopMaterial());
                    }
                }

                return this.topMaterials;
            }
        else return this.topMaterials;
    }

}
