package thelm.jeidrawables.gui.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

public class GuiRenderUtil {

	public static void blit(PoseStack poseStack, ResourceLocation atlasLocation, float x, float y, float uOffset, float vOffset, float width, float height, int textureWidth, int textureHeight) {
		blit(poseStack, atlasLocation, x, y, uOffset, vOffset, width, height, textureWidth, textureHeight, -1, BlendFunction.TRANSLUCENT);
	}

	public static void blitSprite(PoseStack poseStack, TextureAtlasSprite sprite, float x, float y, float uOffset, float vOffset, float width, float height, int textureWidth, int textureHeight) {
		blitSprite(poseStack, sprite, x, y, uOffset, vOffset, width, height, textureHeight, textureHeight, -1, BlendFunction.TRANSLUCENT);
	}

	public static void blit(PoseStack poseStack, ResourceLocation atlasLocation, float x, float y, float uOffset, float vOffset, float width, float height, int textureWidth, int textureHeight, BlendFunction blendFunc) {
		blit(poseStack, atlasLocation, x, y, uOffset, vOffset, width, height, textureWidth, textureHeight, -1, blendFunc);
	}

	public static void blitSprite(PoseStack poseStack, TextureAtlasSprite sprite, float x, float y, float uOffset, float vOffset, float width, float height, int textureWidth, int textureHeight, BlendFunction blendFunc) {
		blitSprite(poseStack, sprite, x, y, uOffset, vOffset, width, height, textureHeight, textureHeight, -1, blendFunc);
	}

	public static void blit(PoseStack poseStack, ResourceLocation atlasLocation, float x, float y, float uOffset, float vOffset, float width, float height, int textureWidth, int textureHeight, int color) {
		blit(poseStack, atlasLocation, x, y, uOffset, vOffset, width, height, textureWidth, textureHeight, color, BlendFunction.TRANSLUCENT);
	}

	public static void blitSprite(PoseStack poseStack, TextureAtlasSprite sprite, float x, float y, float uOffset, float vOffset, float width, float height, int textureWidth, int textureHeight, int color) {
		blitSprite(poseStack, sprite, x, y, uOffset, vOffset, width, height, textureHeight, textureHeight, color, BlendFunction.TRANSLUCENT);
	}

	public static void blit(PoseStack poseStack, ResourceLocation atlasLocation, float x, float y, float uOffset, float vOffset, float width, float height, int textureWidth, int textureHeight, int color, BlendFunction blendFunc) {
		float uMin = uOffset / textureWidth;
		float uMax = (uOffset + width) / textureWidth;
		float vMin = vOffset / textureHeight;
		float vMax = (vOffset + height) / textureHeight;
		blit(poseStack, atlasLocation, x, x + width, y, y + height, uMin, uMax, vMin, vMax, color, blendFunc);
	}

	public static void blitSprite(PoseStack poseStack, TextureAtlasSprite sprite, float x, float y, float uOffset, float vOffset, float width, float height, int textureWidth, int textureHeight, int color, BlendFunction blendFunc) {
		float spriteWidth = sprite.getU1() - sprite.getU0();
		float spriteHeight = sprite.getV1() - sprite.getV0();
		float uMin = sprite.getU0() + uOffset / textureWidth * spriteWidth;
		float uMax = sprite.getU0() + (uOffset + width) / textureWidth * spriteWidth;
		float vMin = sprite.getV0() + vOffset / textureHeight * spriteHeight;
		float vMax = sprite.getV0() + (vOffset + height) / textureHeight * spriteHeight;
		blit(poseStack, sprite.atlas().location(), x, x + width, y, y + height, uMin, uMax, vMin, vMax, color, blendFunc);
	}

	static void blit(PoseStack poseStack, ResourceLocation atlasLocation, float xMin, float xMax, float yMin, float yMax, float uMin, float uMax, float vMin, float vMax, int color, BlendFunction blendFunc) {
		RenderSystem.setShaderTexture(0, atlasLocation);
		RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
		if(blendFunc != null) {
			RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(blendFunc.sourceColor(), blendFunc.destColor(), blendFunc.sourceColor(), blendFunc.destColor());
		}
		else {
			RenderSystem.disableBlend();
		}
		Matrix4f matrix = poseStack.last().pose();
		BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		bufferBuilder.vertex(matrix, xMin, yMin, 0).uv(uMin, vMin).color(color).endVertex();
		bufferBuilder.vertex(matrix, xMin, yMax, 0).uv(uMin, vMax).color(color).endVertex();
		bufferBuilder.vertex(matrix, xMax, yMax, 0).uv(uMax, vMax).color(color).endVertex();
		bufferBuilder.vertex(matrix, xMax, yMin, 0).uv(uMax, vMin).color(color).endVertex();
		BufferUploader.drawWithShader(bufferBuilder.end());
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableBlend();
	}
}
