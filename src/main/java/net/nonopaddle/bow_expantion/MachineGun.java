package net.nonopaddle.bow_expantion;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

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

public class MachineGun extends BowItem implements Runnable {

    private Thread arrowThrower;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private int interval = 200; // millis

    private World world;
    private LivingEntity user;
    private Hand hand;
    private ItemStack itemStack;

    public MachineGun(Settings settings) {
        super(settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        arrowThrower.interrupt();
        running.set(false);
        arrowThrower = null;
        System.out.println("Stop pulling");
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        this.world = world;
        this.user = user;
        this.hand = hand;
        this.itemStack = user.getStackInHand(hand);
        if (!running.get()) {
            arrowThrower = new Thread(this, "arrow_thrower");
            arrowThrower.start();
        }

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
        return UseAction.BOW;
    }

    @Override
    public void run() {
        running.set(true);
        PlayerEntity player = (PlayerEntity) user;
        while (running.get()) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(
                        "Thread was interrupted, Failed to complete operation");
            }

            if (!(player.getStackInHand(this.hand).getItem() instanceof MachineGun))
                running.set(false);

            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT,
                    SoundCategory.PLAYERS, 1.0f, 1.0f / (new Random().nextFloat() * 0.4f + 1.2f) + 0.5f);

            if (!world.isClient) {
                ArrowItem arrowItem = (ArrowItem) (itemStack.getItem() instanceof ArrowItem ? itemStack.getItem()
                        : Items.ARROW);

                PersistentProjectileEntity arrow = arrowItem.createArrow(world, itemStack, player);
                arrow.setPosition(player.getX(), user.getEyeY() - 0.5, user.getZ());
                arrow.setVelocity(player, player.getPitch(), player.getYaw(), 0.0f, 4f, 0f);
                world.spawnEntity(arrow);
            }
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }
        }
    }
}
