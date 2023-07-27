package net.nonopaddle.bow_expansion.item;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.nonopaddle.bow_expansion.rendering.MachineGunRenderer;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.RenderUtils;

public class MachineGun extends BowItem implements /* Runnable, */ GeoItem {

    private final AtomicBoolean running;
    private int interval = 40; // millis

    private World world;
    private LivingEntity user;
    private Hand hand;
    private ItemStack itemStack;
    private int nbLoadedBolts;

    private AnimatableInstanceCache cache;
    private final Supplier<Object> rendererProvider;
    private AnimationController<MachineGun> animationController;

    public MachineGun(Settings settings) {
        super(settings.maxCount(1));
        this.nbLoadedBolts = 0;
        this.cache = new SingletonAnimatableInstanceCache(this);
        this.running = new AtomicBoolean(false);
        this.rendererProvider = GeoItem.makeRenderer(this);

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            PlayerEntity player = (PlayerEntity) user;
            if (this.running.get() && this.nbLoadedBolts > 0) {
                if (server.getTicks() % interval == 0 && interval <= 2) {
                    // System.out.println("interval = " + interval);
                    if (!(player.getStackInHand(this.hand).getItem() instanceof MachineGun))
                        running.set(false);

                    world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT,
                            SoundCategory.PLAYERS, 1.0f, 1.0f / (new Random().nextFloat() * 0.4f + 1.2f) + 0.5f);

                    if (!world.isClient) {
                        BoltItem boltItem = (BoltItem) (itemStack.getItem() instanceof BoltItem
                                ? itemStack.getItem()
                                : ModItems.BOLT_ITEM);

                        PersistentProjectileEntity bolt = boltItem.createBolt(world, itemStack, player);
                        bolt.setPosition(player.getX(), user.getEyeY() - 0.5, user.getZ());
                        bolt.setVelocity(player, player.getPitch(), player.getYaw(), 0.0f, 4f, 7.5f);
                        world.spawnEntity(bolt);
                    }
                    if (!player.getAbilities().creativeMode) {
                        this.nbLoadedBolts--;
                    }
                }
                if (interval > 2)
                    interval--;
            } else {
                if (interval < 40)
                    interval++;
            }
            if (animationController != null) {
                animationController.setAnimationSpeed((38 - (interval - 2) + 0.001) / 4);
            }
        });
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        running.set(false);
        System.out.println("Stopping");
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        this.world = world;
        this.user = user;
        this.hand = hand;
        this.running.set(true);

        if (user.isSneaking()) {
            System.out.println("loading...");
            int index = user.getInventory().indexOf(new ItemStack(ModItems.BOLT_ITEM));
            if(index != -1) this.itemStack = user.getInventory().getStack(index);
            /* int stackPos = 0;
            boolean found = false;
            while (!found && stackPos > user.getInventory().size()) {
                if (user.getInventory().getStack(stackPos).getItem() instanceof BoltItem) {
                    found = true;
                } else {
                    stackPos++;
                }
            } */
            System.out.println("stack found : " + this.itemStack != null);
            if (this.itemStack != null && this.itemStack.getItem() instanceof BoltItem) {
                //this.itemStack.decrement(1);
                this.nbLoadedBolts++;
            }
            System.out.println("nb bolts : " + this.nbLoadedBolts);
        } else {
            System.out.println("Shooting");
            boolean bl;
            ItemStack itemStack = user.getStackInHand(hand);
            bl = !user.getProjectileType(itemStack).isEmpty();
            if (user.getAbilities().creativeMode || bl) {
                user.setCurrentHand(hand);
                return TypedActionResult.consume(itemStack);
            }
        }
        return TypedActionResult.fail(itemStack);
    }

    @Override
    public UseAction getUseAction(ItemStack itemStack) {
        return UseAction.NONE;
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private final MachineGunRenderer renderer = new MachineGunRenderer();

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                return this.renderer;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return rendererProvider;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void registerControllers(ControllerRegistrar controller) {
        this.animationController = new AnimationController<>(this, "controller", 0, this::predicate);
        controller.add(animationController);
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        tAnimationState.getController()
                .setAnimation(RawAnimation.begin().then("machine_gun.rotation", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public double getTick(Object itemStack) {
        return RenderUtils.getCurrentTick();
    }
}
