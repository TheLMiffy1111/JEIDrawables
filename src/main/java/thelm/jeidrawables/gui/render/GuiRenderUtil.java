package thelm.jeidrawables.gui.render;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import thelm.jeidrawables.mixin.GuiGraphicsAccessor;

public class GuiRenderUtil {

	public static void blit(GuiGraphics guiGraphics, ResourceLocation atlasLocation, float x, float y, float uOffset, float vOffset, float width, float height, int textureWidth, int textureHeight) {
		float uMin = uOffset / textureWidth;
		float uMax = (uOffset + width) / textureWidth;
		float vMin = vOffset / textureHeight;
		float vMax = (vOffset + height) / textureHeight;
		blit(guiGraphics, atlasLocation, x, x + width, y, y + height, uMin, uMax, vMin, vMax);
	}

	public static void blitSprite(GuiGraphics guiGraphics, TextureAtlasSprite sprite, float x, float y, float uOffset, float vOffset, float width, float height, int textureWidth, int textureHeight) {
		float spriteWidth = sprite.getU1() - sprite.getU0();
		float spriteHeight = sprite.getV1() - sprite.getV0();
		float uMin = sprite.getU0() + uOffset / textureWidth * spriteWidth;
		float uMax = sprite.getU0() + (uOffset + width) / textureWidth * spriteWidth;
		float vMin = sprite.getV0() + vOffset / textureHeight * spriteHeight;
		float vMax = sprite.getV0() + (vOffset + height) / textureHeight * spriteHeight;
		blit(guiGraphics, sprite.atlasLocation(), x, x + width, y, y + height, uMin, uMax, vMin, vMax);
	}

	static void blit(GuiGraphics guiGraphics, ResourceLocation atlasLocation, float xMin, float xMax, float yMin, float yMax, float uMin, float uMax, float vMin, float vMax) {
		RenderType renderType = RenderType.guiTextured(atlasLocation);
		Matrix4f matrix = guiGraphics.pose().last().pose();
		BufferSource bufferSource = ((GuiGraphicsAccessor)guiGraphics).jeidas$bufferSource();
		VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
		vertexConsumer.addVertex(matrix, xMin, yMin, 0).setUv(uMin, vMin);
		vertexConsumer.addVertex(matrix, xMin, yMax, 0).setUv(uMin, vMax);
		vertexConsumer.addVertex(matrix, xMax, yMax, 0).setUv(uMax, vMax);
		vertexConsumer.addVertex(matrix, xMax, yMin, 0).setUv(uMax, vMin);
	}
}
