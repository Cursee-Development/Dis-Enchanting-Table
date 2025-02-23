package com.cursee.disenchanting_table.client.block.entity.renderer;

import com.cursee.disenchanting_table.core.world.block.HopperDisEnchantingTableBlock;
import com.cursee.disenchanting_table.core.world.block.entity.HopperDisEnchantingTableBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class HopperDisEnchantingTableBER implements BlockEntityRenderer<HopperDisEnchantingTableBlockEntity> {

    public HopperDisEnchantingTableBER(BlockEntityRendererProvider.Context context) {}

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }

    @Override
    public void render(HopperDisEnchantingTableBlockEntity hopperDisEnchantingTable, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverly) {

        if (hopperDisEnchantingTable.getLevel() == null) return;

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStack = hopperDisEnchantingTable.getRenderStack();

        poseStack.pushPose();
        poseStack.translate(0.5f, 0.75f, 0.5f);
        poseStack.scale(0.5f, 0.5f, 0.5f);

        // if block has facing value east or west, the rendered item appears mirrored
        Direction facing = hopperDisEnchantingTable.getBlockState().getValue(HopperDisEnchantingTableBlock.FACING);
        poseStack.mulPose(Axis.YP.rotationDegrees((facing != Direction.EAST && facing != Direction.WEST) ? facing.getOpposite().toYRot() : facing.toYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(90));

        itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, getLightLevel(hopperDisEnchantingTable.getLevel(), hopperDisEnchantingTable.getBlockPos()), OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, hopperDisEnchantingTable.getLevel(), 1);
        poseStack.popPose();
    }
}
