package net.nonopaddle.bow_expansion.entity.custom;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.object.PlayState;

public class BoltEntity extends ArrowEntity implements GeoEntity {

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private AnimationController<BoltEntity> animationController;

    private boolean onTarget = false;
    private int onTargetTime = 0;
    
    public BoltEntity(EntityType<? extends ArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public BoltEntity(World world, LivingEntity owner) {
        super(world, owner);
    }

    public static BoltEntity newBolt(EntityType<? extends ArrowEntity> entityType, World world) {
        return new BoltEntity(entityType, world);
    }
    
    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        Vec3d vec3d = blockHitResult.getPos().subtract(this.getX(), this.getY(), this.getZ());
        this.setVelocity(vec3d);
        Vec3d vec3d2 = vec3d.normalize().multiply(0.05f);
        this.setPos(this.getX() - vec3d2.x, this.getY() - vec3d2.y, this.getZ() - vec3d2.z);
        this.playSound(this.getSound(), 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));
        this.inGround = true;
        this.shake = 7;
        this.setCritical(false);
        this.setPierceLevel((byte)0);
        this.setSound(SoundEvents.ENTITY_ARROW_HIT);
        this.setShotFromCrossbow(false);
    }
    
    @Override
    public void tick() {
        super.tick();
        if(inGroundTime > 2) {
            //MinecraftClient.getInstance().setCameraEntity(MinecraftClient.getInstance().player);
            this.discard();
        };

        if(this.onTarget) {
            if(this.onTargetTime > 2) {
                this.discard();
            } else {
                this.onTargetTime++;
            }
        }
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        if (this.getWorld().isClient || !this.inGround && !this.isNoClip() || this.shake > 0) {
            return;
        }
    }

    @Override
    public void onHit(LivingEntity target) {
        this.onTarget = true;
        super.onHit(target);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void registerControllers(ControllerRegistrar controllerRegistrar) {
        animationController = new AnimationController<>(this,"controller", 0, this::predicate);
        controllerRegistrar.add(animationController);
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        tAnimationState.getController().setAnimation(RawAnimation.begin().then("bolt.animation.rotation", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }
}
