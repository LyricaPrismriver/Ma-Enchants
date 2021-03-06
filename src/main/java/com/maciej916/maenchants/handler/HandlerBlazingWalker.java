package com.maciej916.maenchants.handler;

import com.google.common.base.Objects;
import com.maciej916.maenchants.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import static com.maciej916.maenchants.init.ModEnchants.BLAZING_WALKER;
import static com.maciej916.maenchants.init.ModEnchants.FLOATING;

public class HandlerBlazingWalker {

    public static void handlerPlayerTick(PlayerEntity player) {
        ItemStack stack = player.getItemStackFromSlot(EquipmentSlotType.FEET);
        int lvl = EnchantmentHelper.getEnchantmentLevel(BLAZING_WALKER, stack);
        if (lvl == 0) return;

        BlockPos prevBlockpos = ObfuscationReflectionHelper.getPrivateValue(LivingEntity.class, player, "field_184620_bC");
        BlockPos blockpos = new BlockPos(player);

        if (!Objects.equal(prevBlockpos, blockpos)) {
            makeFloor(player, player.world, blockpos, lvl);
        }
    }

    private static void makeFloor(LivingEntity living, World worldIn, BlockPos pos, int level) {
        if (living.onGround) {
            BlockState blockstate = ModBlocks.MELTED_COBBLESTONE.getDefaultState();
            float f = (float)Math.min(16, 2 + level);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for(BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add((double)(-f), -1.0D, (double)(-f)), pos.add((double)f, -1.0D, (double)f))) {
                if (blockpos.withinDistance(living.getPositionVec(), (double)f)) {
                    blockpos$mutableblockpos.setPos(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
                    BlockState blockstate1 = worldIn.getBlockState(blockpos$mutableblockpos);
                    if (blockstate1.isAir(worldIn, blockpos$mutableblockpos)) {
                        BlockState blockstate2 = worldIn.getBlockState(blockpos);
                        boolean isFull = blockstate2.getBlock() == Blocks.LAVA && blockstate2.get(FlowingFluidBlock.LEVEL) == 0;
                        if (blockstate2.getMaterial() == Material.LAVA && isFull && blockstate.isValidPosition(worldIn, blockpos) && worldIn.func_217350_a(blockstate, blockpos, ISelectionContext.dummy()) && !net.minecraftforge.event.ForgeEventFactory.onBlockPlace(living, new net.minecraftforge.common.util.BlockSnapshot(worldIn, blockpos, blockstate2), net.minecraft.util.Direction.UP)) {
                            worldIn.setBlockState(blockpos, blockstate);
                            worldIn.getPendingBlockTicks().scheduleTick(blockpos, ModBlocks.MELTED_COBBLESTONE, MathHelper.nextInt(living.getRNG(), 20, 60));
                        }
                    }
                }
            }
        }
    }

}
