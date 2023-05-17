package org.test.refugiotestmod.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

public class NebulaEntity extends FlyingEntity implements Monster, GeoEntity {

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    Vec3d targetPosition;
    NebulaMovementType movementType;
    BlockPos circlingCenter;

    public NebulaEntity(EntityType<? extends FlyingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {

        if (tAnimationState.isMoving()){
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nebula.walk", Animation.LoopType.LOOP));
        }

        tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.nebula.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    private static enum NebulaMovementType {
        CIRCLE,
        SWOOP;

        private NebulaMovementType() {
        }
    }

    public static DefaultAttributeContainer.Builder setAttributes(){
        return FlyingEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 24.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.5f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2.5f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.0f);

    }

    protected void initGoals() {
        this.goalSelector.add(3, new CircleMovementGoal());
        this.targetSelector.add(1, new FindTargetGoal());
    }

    class CircleMovementGoal extends MovementGoal {
        private float angle;
        private float radius;
        private float yOffset;
        private float circlingDirection;

        CircleMovementGoal() {
            super();
        }

        public boolean canStart() {
            return NebulaEntity.this.getTarget() == null || NebulaEntity.this.movementType == NebulaMovementType.CIRCLE;
        }

        public void start() {
            this.radius = 5.0F + NebulaEntity.this.random.nextFloat() * 10.0F;
            this.yOffset = -4.0F + NebulaEntity.this.random.nextFloat() * 9.0F;
            this.circlingDirection = NebulaEntity.this.random.nextBoolean() ? 1.0F : -1.0F;
            this.adjustDirection();
        }

        public void tick() {
            if (NebulaEntity.this.random.nextInt(this.getTickCount(350)) == 0) {
                this.yOffset = -4.0F + NebulaEntity.this.random.nextFloat() * 9.0F;
            }

            if (NebulaEntity.this.random.nextInt(this.getTickCount(250)) == 0) {
                ++this.radius;
                if (this.radius > 15.0F) {
                    this.radius = 5.0F;
                    this.circlingDirection = -this.circlingDirection;
                }
            }

            if (NebulaEntity.this.random.nextInt(this.getTickCount(450)) == 0) {
                this.angle = NebulaEntity.this.random.nextFloat() * 2.0F * 3.1415927F;
                this.adjustDirection();
            }

            if (this.isNearTarget()) {
                this.adjustDirection();
            }

            if (NebulaEntity.this.targetPosition.y < NebulaEntity.this.getY() && !NebulaEntity.this.world.isAir(NebulaEntity.this.getBlockPos().down(1))) {
                this.yOffset = Math.max(1.0F, this.yOffset);
                this.adjustDirection();
            }

            if (NebulaEntity.this.targetPosition.y > NebulaEntity.this.getY() && !NebulaEntity.this.world.isAir(NebulaEntity.this.getBlockPos().up(1))) {
                this.yOffset = Math.min(-1.0F, this.yOffset);
                this.adjustDirection();
            }

        }

        private void adjustDirection() {
            if (BlockPos.ORIGIN.equals(NebulaEntity.this.circlingCenter)) {
                NebulaEntity.this.circlingCenter = NebulaEntity.this.getBlockPos();
            }

            this.angle += this.circlingDirection * 15.0F * 0.017453292F;
            NebulaEntity.this.targetPosition = Vec3d.of(NebulaEntity.this.circlingCenter).add((double)(this.radius * MathHelper.cos(this.angle)), (double)(-4.0F + this.yOffset), (double)(this.radius * MathHelper.sin(this.angle)));
        }
    }

    class FindTargetGoal extends Goal {
        private final TargetPredicate PLAYERS_IN_RANGE_PREDICATE = TargetPredicate.createAttackable().setBaseMaxDistance(64.0);
        private int delay = toGoalTicks(20);

        FindTargetGoal() {
        }

        public boolean canStart() {
            if (this.delay > 0) {
                --this.delay;
                return false;
            } else {
                this.delay = toGoalTicks(60);
                List<PlayerEntity> list = NebulaEntity.this.world.getPlayers(this.PLAYERS_IN_RANGE_PREDICATE, NebulaEntity.this, NebulaEntity.this.getBoundingBox().expand(16.0, 64.0, 16.0));
                if (!list.isEmpty()) {
                    list.sort(Comparator.comparing(Entity::getY).reversed());
                    Iterator var2 = list.iterator();

                    while(var2.hasNext()) {
                        PlayerEntity playerEntity = (PlayerEntity)var2.next();
                        if (NebulaEntity.this.isTarget(playerEntity, TargetPredicate.DEFAULT)) {
                            NebulaEntity.this.setTarget(playerEntity);
                            return true;
                        }
                    }
                }

                return false;
            }
        }

        public boolean shouldContinue() {
            LivingEntity livingEntity = NebulaEntity.this.getTarget();
            return livingEntity != null ? NebulaEntity.this.isTarget(livingEntity, TargetPredicate.DEFAULT) : false;
        }
    }

    abstract class MovementGoal extends Goal {
        public MovementGoal() {
            this.setControls(EnumSet.of(Control.MOVE));
        }

        protected boolean isNearTarget() {
            return NebulaEntity.this.targetPosition.squaredDistanceTo(NebulaEntity.this.getX(), NebulaEntity.this.getY(), NebulaEntity.this.getZ()) < 4.0;
        }
    }


}
