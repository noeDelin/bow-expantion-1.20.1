package net.nonopaddle.bow_expansion.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.nonopaddle.bow_expansion.entity.custom.BoltEntity;

public class BoltItem extends Item{
    public BoltItem(Item.Settings settings) {
        super(settings);
    }

    public PersistentProjectileEntity createBolt(World world, ItemStack stack, LivingEntity shooter) {
        BoltEntity boltEntity = new BoltEntity(world, shooter);
        boltEntity.initFromStack(stack);
        return boltEntity;
    }
}
