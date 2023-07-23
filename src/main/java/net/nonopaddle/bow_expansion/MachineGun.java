package net.nonopaddle.bow_expansion;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
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

    private final AtomicBoolean running = new AtomicBoolean(false);
    private int interval = 40; // millis

    private World world;
    private LivingEntity user;
    private Hand hand;
    private ItemStack itemStack;

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final Supplier<Object> rendererProvider = GeoItem.makeRenderer(this);
    private AnimationController<MachineGun> animationController;

    public MachineGun(Settings settings) {
        super(settings);

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            PlayerEntity player = (PlayerEntity) user;
            if (this.running.get()) {
                if (server.getTicks() % interval == 0 && interval <=2) {
                    //System.out.println("interval = " + interval);
                    if (!(player.getStackInHand(this.hand).getItem() instanceof MachineGun))
                        running.set(false);

                    world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT,
                            SoundCategory.PLAYERS, 1.0f, 1.0f / (new Random().nextFloat() * 0.4f + 1.2f) + 0.5f);

                    if (!world.isClient) {
                        ArrowItem arrowItem = (ArrowItem) (itemStack.getItem() instanceof ArrowItem
                                ? itemStack.getItem()
                                : Items.ARROW);

                        PersistentProjectileEntity arrow = arrowItem.createArrow(world, itemStack, player);
                        arrow.setPosition(player.getX(), user.getEyeY() - 0.5, user.getZ());
                        arrow.setVelocity(player, player.getPitch(), player.getYaw(), 0.0f, 4f, 7.5f);
                        world.spawnEntity(arrow);
                    }
                    if (!player.getAbilities().creativeMode) {
                        itemStack.decrement(1);
                    }
                }
                if(interval > 2) interval--;
            } else {
                if(interval < 40) interval ++;
            }
            if (animationController != null) {
                animationController.setAnimationSpeed((38-(interval-2)+0.001)/4);
            }
        });
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        running.set(false);
        System.out.println("Stop pulling");
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        this.world = world;
        this.user = user;
        this.hand = hand;
        this.itemStack = user.getStackInHand(hand);
        this.running.set(true);

        System.out.println("Pulling");
        boolean bl;
        ItemStack itemStack = user.getStackInHand(hand);
        bl = !user.getProjectileType(itemStack).isEmpty();
        if (user.getAbilities().creativeMode || bl) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }

    @Override
    public UseAction getUseAction(ItemStack itemStack) {
        return UseAction.NONE;
    }

    /*
     * @Override
     * public void run() {
     * running.set(true);
     * PlayerEntity player = (PlayerEntity) user;
     * while (running.get()) {
     * try {
     * Thread.sleep(interval);
     * } catch (InterruptedException e) {
     * e.printStackTrace();
     * }
     * 
     * animationController.setAnimationSpeed(5);
     * 
     * if (!(player.getStackInHand(this.hand).getItem() instanceof MachineGun))
     * running.set(false);
     * 
     * world.playSound(null, player.getX(), player.getY(), player.getZ(),
     * SoundEvents.ENTITY_ARROW_SHOOT,
     * SoundCategory.PLAYERS, 1.0f, 1.0f / (new Random().nextFloat() * 0.4f + 1.2f)
     * + 0.5f);
     * 
     * if (!world.isClient) {
     * ArrowItem arrowItem = (ArrowItem) (itemStack.getItem() instanceof ArrowItem ?
     * itemStack.getItem()
     * : Items.ARROW);
     * 
     * PersistentProjectileEntity arrow = arrowItem.createArrow(world, itemStack,
     * player);
     * arrow.setPosition(player.getX(), user.getEyeY() - 0.5, user.getZ());
     * arrow.setVelocity(player, player.getPitch(), player.getYaw(), 0.0f, 4f, 0f);
     * world.spawnEntity(arrow);
     * }
     * if (!player.getAbilities().creativeMode) {
     * itemStack.decrement(1);
     * }
     * }
     * }
     */

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
