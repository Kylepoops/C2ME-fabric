package org.yatopiamc.c2me.mixin.fixes.weighted_list;

import net.minecraft.util.WeightedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Mixin(WeightedList.class)
public abstract class MixinWeightedList<U> {

    @Shadow @Final public List<WeightedList<U>.Entry<? extends U>> entries;

    /**
     * @author ishland
     * @reason use new random
     */
    @Overwrite
    public WeightedList<U> shuffle() {
        return this.shuffle(new Random());
    }

    /**
     * @author ishland
     * @reason use new instance on shuffle
     */
    @Overwrite
    public WeightedList<U> shuffle(Random random) {
        final WeightedList<U> newWeightedList = new WeightedList<>();
        // TODO [semi-VanillaCopy]
        newWeightedList.entries.addAll(this.entries);
        newWeightedList.entries.forEach((entry) -> {
            entry.setShuffledOrder(random.nextFloat());
        });
        newWeightedList.entries.sort(Comparator.comparingDouble((object) -> {
            return ((WeightedList.Entry)object).getShuffledOrder();
        }));
        return newWeightedList;
    }

}
