package net.nonopaddle.bow_expansion.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.nonopaddle.bow_expansion.entity.custom.BoltEntity;

public class BoltItem extends Item {
    public BoltItem(Item.Settings settings) {
        super(settings);
    }

    public BoltEntity createBolt(World world, ItemStack stack, LivingEntity shooter) {
        BoltEntity boltEntity = new BoltEntity(world, shooter);
        return boltEntity;
    }
}
