package net.nonopaddle.bow_expansion.item;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity.PickupPermission;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.nonopaddle.bow_expansion.item.Longbow;
import net.nonopaddle.bow_expansion.LongbowRenderer;
import net.nonopaddle.bow_expansion.entity.custom.BoltEntity;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.RenderUtils;

public class Longbow extends BowItem implements GeoItem {

   private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
   private final Supplier<Object> rendererProvider = GeoItem.makeRenderer(this);
   private AnimationController<Longbow> animationController;

   private AtomicBoolean pulling = new AtomicBoolean(false);

   private PlayerEntity user;

   public Longbow(Settings settings) {
      super(settings);
   }

   @Override
   public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
      this.pulling.set(false);

      ServerTickEvents.END_SERVER_TICK.register(register -> {
         System.out.println();
      });

      if (user instanceof PlayerEntity playerEntity) {
         boolean bl = playerEntity.getAbilities().creativeMode
               || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
         ItemStack itemStack = playerEntity.getProjectileType(stack);
         if (!itemStack.isEmpty() || bl) {
            if (itemStack.isEmpty()) {
               itemStack = new ItemStack(Items.ARROW);
            }

            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            float f = getPullProgress(i);
            if (!((double) f < 0.2)) {
               boolean bl2 = bl && itemStack.isOf(Items.ARROW);
               if (!world.isClient) {
                  BoltItem arrowItem = (BoltItem) (itemStack.getItem() instanceof BoltItem ? itemStack.getItem()
                        : ModItems.BOLT_ITEM);
                  PersistentProjectileEntity persistentProjectileEntity = arrowItem.createBolt(world, itemStack,
                        playerEntity);
                  
                  MinecraftClient.getInstance().setCameraEntity(persistentProjectileEntity);
                  persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(),
                        0.0F, f * 3.0F, 1.0F);
                  if (f == 2.0F) {
                     persistentProjectileEntity.setCritical(true);
                  }

                  int j = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
                  if (j > 0) {
                     persistentProjectileEntity
                           .setDamage(persistentProjectileEntity.getDamage() + (double) j * 0.5 + 0.5);
                  }

                  int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
                  if (k > 0) {
                     persistentProjectileEntity.setPunch(k);
                  }

                  if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
                     persistentProjectileEntity.setOnFireFor(100);
                  }

                  stack.damage(1, playerEntity, (p) -> {
                     p.sendToolBreakStatus(playerEntity.getActiveHand());
                  });
                  if (bl2 || playerEntity.getAbilities().creativeMode
                        && (itemStack.isOf(Items.SPECTRAL_ARROW) || itemStack.isOf(Items.TIPPED_ARROW))) {
                     persistentProjectileEntity.pickupType = PickupPermission.CREATIVE_ONLY;
                  }

                  world.spawnEntity(persistentProjectileEntity);
               }

               world.playSound((PlayerEntity) null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                     SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F,
                     1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
               if (!bl2 && !playerEntity.getAbilities().creativeMode) {
                  itemStack.decrement(1);
                  if (itemStack.isEmpty()) {
                     playerEntity.getInventory().removeOne(itemStack);
                  }
               }

               playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
            }
         }
      }
   }

   @Override
   public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
      this.user = user;
      this.pulling.set(true);
      return super.use(world, user, hand);
   }

   public static float getPullProgress(int useTicks) {
      float f = (float) useTicks / 20.0F;
      f = (f * f + f * 2.0F) / 3.0F;
      if (f > 2.0F) {
         f = 2.0F;
      }

      return f;
   }

   @Override
   public UseAction getUseAction(ItemStack itemStack) {
      return UseAction.BOW;
   }

   @Override
   public void createRenderer(Consumer<Object> consumer) {
      consumer.accept(new RenderProvider() {
         private final LongbowRenderer renderer = new LongbowRenderer();

         @Override
         public BuiltinModelItemRenderer getCustomRenderer() {
            return this.renderer;
         }
      });
   }

   @Override
   public Supplier<Object> getRenderProvider() {
      return this.rendererProvider;
   }

   @Override
   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return this.cache;
   }

   @Override
   public void registerControllers(ControllerRegistrar controller) {
      this.animationController = new AnimationController<Longbow>(this, "controller", 0,
            this::predicate);
      controller.add(animationController);
   }

   private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
      if (this.pulling.get()) {
         if(this.user != null && this.user.getActiveItem().getItem() instanceof Longbow) {
            tAnimationState.getController()
               .setAnimation(RawAnimation.begin().then("longbow.animation.draw", Animation.LoopType.HOLD_ON_LAST_FRAME));
            return PlayState.CONTINUE;
         }
      }
      tAnimationState.getController()
            .setAnimation(RawAnimation.begin().then("longbow.animation.shoot", Animation.LoopType.HOLD_ON_LAST_FRAME));
      return PlayState.CONTINUE;
   }

   @Override
   public double getTick(Object itemStack) {
      return RenderUtils.getCurrentTick();
   }
}
