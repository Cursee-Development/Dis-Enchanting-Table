package com.cursee.disenchanting_table.client.block.entity.renderer;

import com.cursee.disenchanting_table.client.ClientConfigValues;
import com.cursee.disenchanting_table.core.CommonConfigValues;
import com.cursee.disenchanting_table.core.world.block.DisEnchantingTableBlock;
import com.cursee.disenchanting_table.core.world.block.entity.ForgeDisEnchantingBE;
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

public class ForgeDisEnchantingBER implements BlockEntityRenderer<ForgeDisEnchantingBE> {

    public ForgeDisEnchantingBER(BlockEntityRendererProvider.Context context) {}

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }

    private static final ItemStack MANUAL_STACK = new ItemStack(Items.ENCHANTED_BOOK);

    @Override
    public void render(ForgeDisEnchantingBE disEnchantingBE, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverly) {

        if (disEnchantingBE.getLevel() == null) return;

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStack = disEnchantingBE.getRenderStack();
        if (!CommonConfigValues.automatic_disenchanting) itemStack = MANUAL_STACK;
        if (ClientConfigValues.render_table_item) return;

        poseStack.pushPose();
        poseStack.translate(0.5f, 0.75f, 0.5f);
        poseStack.scale(0.5f, 0.5f, 0.5f);

        // if block has facing value east or west, the rendered item appears mirrored
        Direction facing = disEnchantingBE.getBlockState().getValue(DisEnchantingTableBlock.FACING);
        poseStack.mulPose(Axis.YP.rotationDegrees((facing != Direction.EAST && facing != Direction.WEST) ? facing.getOpposite().toYRot() : facing.toYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(90));

        itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, getLightLevel(disEnchantingBE.getLevel(), disEnchantingBE.getBlockPos()), OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, disEnchantingBE.getLevel(), 1);
        poseStack.popPose();
    }
}
