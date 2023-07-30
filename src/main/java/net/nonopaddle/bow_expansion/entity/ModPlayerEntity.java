package net.nonopaddle.bow_expansion.entity;

import java.util.function.Predicate;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.nonopaddle.bow_expansion.item.ModRangedWeaponItem;

public class ModPlayerEntity extends PlayerEntity{

    public ModPlayerEntity(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Override
    public boolean isCreative() {
        throw new UnsupportedOperationException("Unimplemented method 'isCreative'");
    }

    @Override
    public boolean isSpectator() {
        throw new UnsupportedOperationException("Unimplemented method 'isSpectator'");
    }

    @Override
    public ItemStack getProjectileType(ItemStack stack) {
      if (!(stack.getItem() instanceof ModRangedWeaponItem)) {
         return ItemStack.EMPTY;
      } else {
         Predicate<ItemStack> predicate = ((ModRangedWeaponItem)stack.getItem()).getHeldProjectiles();
         ItemStack itemStack = ModRangedWeaponItem.getHeldProjectile(this, predicate);
         if (!itemStack.isEmpty()) {
            return itemStack;
         } else {
            predicate = ((ModRangedWeaponItem)stack.getItem()).getProjectiles();

            for(int i = 0; i < this.getInventory().size(); ++i) {
               ItemStack itemStack2 = this.getInventory().getStack(i);
               if (predicate.test(itemStack2)) {
                  return itemStack2;
               }
            }

            return this.getAbilities().creativeMode ? new ItemStack(Items.ARROW) : ItemStack.EMPTY;
         }
      }
   }
    
}
