package net.nonopaddle.bow_expansion.item;

import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;

public class ModRangedWeaponItem extends RangedWeaponItem{
    public static Predicate<ItemStack> MACHINE_GUN = (stack) -> {
        return stack.isOf(ModItems.BOLT_ITEM);
    };

    public ModRangedWeaponItem(Settings settings) {
        super(settings);
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        throw new UnsupportedOperationException("Unimplemented method 'getProjectiles'");
    }

    @Override
    public int getRange() {
        throw new UnsupportedOperationException("Unimplemented method 'getRange'");
    }
    
}
