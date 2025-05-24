package com.cursee.disenchanting_table.client.renderer.blockentity;

import com.cursee.disenchanting_table.client.ClientConfig;
import com.cursee.disenchanting_table.core.ServerConfig;
import com.cursee.disenchanting_table.core.world.block.DisenchantingTableBlock;
import com.cursee.disenchanting_table.core.world.block.entity.DisenchantingTableBlockEntity;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class DisenchantingTableRenderer implements BlockEntityRenderer<DisenchantingTableBlockEntity> {

    /** Used when CommonConfigValues.automatic_disenchanting is false */
    private static final ItemStack MANUAL_STACK = new ItemStack(Items.ENCHANTED_BOOK);

    public DisenchantingTableRenderer(BlockEntityRendererProvider.Context context) {}

    private static int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }

    @Override
    public void render(DisenchantingTableBlockEntity table, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        if (!ClientConfig.render_table_item) return;
        if (table.getLevel() == null) return;

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStack = table.getRenderStack();
        if (!ServerConfig.automatic_disenchanting) itemStack = MANUAL_STACK;

        poseStack.pushPose();
        poseStack.translate(0.5f, 0.75f, 0.5f);
        poseStack.scale(0.5f, 0.5f, 0.5f);

        // if block has facing value east or west, the rendered item appears mirrored
        Direction facing = table.getBlockState().getValue(DisenchantingTableBlock.FACING);
        poseStack.mulPose(Axis.YP.rotationDegrees((facing != Direction.EAST && facing != Direction.WEST) ? facing.getOpposite().toYRot() : facing.toYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(90));

        itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, getLightLevel(table.getLevel(), table.getBlockPos()), OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, table.getLevel(), 1);
        poseStack.popPose();
    }
}
