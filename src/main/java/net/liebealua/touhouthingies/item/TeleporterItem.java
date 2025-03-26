package net.liebealua.touhouthingies.item;

import net.liebealua.touhouthingies.registries.SoundRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TeleporterItem extends Item {
    public TeleporterItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        if(!pLevel.isClientSide()) {
            int MIN_DISTANCE = 4;
            int MAX_DISTANCE = 8;
            int UPWARP_DISTANCE = 3;

            int tpDistance = ThreadLocalRandom.current().nextInt(MIN_DISTANCE, MAX_DISTANCE + 1);

            Direction dirLooking = getDirection(pPlayer);

            pPlayer.setInvulnerable(true);

            switch (dirLooking) {
                case UP:
                    pPlayer.teleportTo(
                            Math.floor(pPlayer.position().x) + 0.5f,
                            Math.floor(pPlayer.position().y) + tpDistance,
                            Math.floor(pPlayer.position().z) + 0.5f
                    );
                    break;
                case DOWN:
                    pPlayer.teleportTo(
                            Math.floor(pPlayer.position().x) + 0.5f,
                            Math.floor(pPlayer.position().y) - tpDistance,
                            Math.floor(pPlayer.position().z) + 0.5f
                    );
                    break;
                case NORTH:
                    pPlayer.teleportTo(
                            Math.floor(pPlayer.position().x) + 0.5f,
                            Math.floor(pPlayer.position().y),
                            Math.floor(pPlayer.position().z) - tpDistance + 0.5f
                    );
                    break;
                case EAST:
                    pPlayer.teleportTo(
                            Math.floor(pPlayer.position().x) + 0.5f + tpDistance,
                            Math.floor(pPlayer.position().y),
                            Math.floor(pPlayer.position().z) + 0.5f
                    );
                    break;
                case SOUTH:
                    pPlayer.teleportTo(
                            Math.floor(pPlayer.position().x) + 0.5f,
                            Math.floor(pPlayer.position().y),
                            Math.floor(pPlayer.position().z) + tpDistance + 0.5f
                    );
                    break;
                case WEST:
                    pPlayer.teleportTo(
                            Math.floor(pPlayer.position().x) - tpDistance + 0.5f ,
                            Math.floor(pPlayer.position().y),
                            Math.floor(pPlayer.position().z) + 0.5f
                    );
                    break;
                default:
                    break;
            }

            for (int i = 0; i < UPWARP_DISTANCE; i++) {
                if (pPlayer.getFeetBlockState().isSolid())
                    pPlayer.teleportRelative(0, 1, 0);
                else
                    break;
            }

            pLevel.playSeededSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
                    SoundRegistry.TELEPORTER_TELEPORT.get(), SoundSource.MASTER, 1f, 1f, 0);

//            if (pLevel.isClientSide) {
//                        pLevel.addParticle(ParticleTypes.END_ROD, pPlayer.getRandomX(1.0), pPlayer.getRandomY() + 0.5, pPlayer.getRandomZ(1.0), 0.0, 0.0, 0.0);
//            }

            if(pPlayer.isInWall())
                pPlayer.setForcedPose(Pose.SWIMMING);

            pPlayer.setInvulnerable(false);

            if (pPlayer.getFeetBlockState().isSolid())
                pPlayer.hurt(pLevel.damageSources().inWall(), Float.MAX_VALUE);

            pPlayer.getCooldowns().addCooldown(this, 20);
        }


        return InteractionResultHolder.sidedSuccess(pPlayer.getItemInHand(pUsedHand), pLevel.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.touhouthingies.teleporter_item.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    enum Direction {UP, DOWN, NORTH, EAST, SOUTH, WEST}

    private Direction getDirection(Player pPlayer) {


        float dirX = pPlayer.getXRot();
        float dirY = pPlayer.getYRot();

        if (dirX < -45)
            return Direction.UP;
        else if (dirX > 45)
            return Direction.DOWN;
        else if (dirY > 135 || dirY <= -135)
            return Direction.NORTH;
        else if (dirY > -135 && dirY <= -45)
            return Direction.EAST;
        else if (dirY > -45 && dirY <= 45)
            return Direction.SOUTH;
        else if (dirY > 45 && dirY <= 135)
            return Direction.WEST;
        else
            return null;
    }

}

