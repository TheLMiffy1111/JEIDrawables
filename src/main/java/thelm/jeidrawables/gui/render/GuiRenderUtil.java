package thelm.jeidrawables.gui.render;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import org.joml.Matrix4f;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TriState;
import thelm.jeidrawables.mixin.GuiGraphicsAccessor;

public class GuiRenderUtil {

	public static void blit(GuiGraphics guiGraphics, ResourceLocation atlasLocation, float x, float y, float uOffset, float vOffset, float width, float height, int textureWidth, int textureHeight) {
		blit(guiGraphics, atlasLocation, x, y, uOffset, vOffset, width, height, textureWidth, textureHeight, -1, BlendFunction.TRANSLUCENT);
	}

	public static void blitSprite(GuiGraphics guiGraphics, TextureAtlasSprite sprite, float x, float y, float uOffset, float vOffset, float width, float height, int textureWidth, int textureHeight) {
		blitSprite(guiGraphics, sprite, x, y, uOffset, vOffset, width, height, textureHeight, textureHeight, -1, BlendFunction.TRANSLUCENT);
	}

	public static void blit(GuiGraphics guiGraphics, ResourceLocation atlasLocation, float x, float y, float uOffset, float vOffset, float width, float height, int textureWidth, int textureHeight, BlendFunction blendFunc) {
		blit(guiGraphics, atlasLocation, x, y, uOffset, vOffset, width, height, textureWidth, textureHeight, -1, blendFunc);
	}

	public static void blitSprite(GuiGraphics guiGraphics, TextureAtlasSprite sprite, float x, float y, float uOffset, float vOffset, float width, float height, int textureWidth, int textureHeight, BlendFunction blendFunc) {
		blitSprite(guiGraphics, sprite, x, y, uOffset, vOffset, width, height, textureHeight, textureHeight, -1, blendFunc);
	}

	public static void blit(GuiGraphics guiGraphics, ResourceLocation atlasLocation, float x, float y, float uOffset, float vOffset, float width, float height, int textureWidth, int textureHeight, int color) {
		blit(guiGraphics, atlasLocation, x, y, uOffset, vOffset, width, height, textureWidth, textureHeight, color, BlendFunction.TRANSLUCENT);
	}

	public static void blitSprite(GuiGraphics guiGraphics, TextureAtlasSprite sprite, float x, float y, float uOffset, float vOffset, float width, float height, int textureWidth, int textureHeight, int color) {
		blitSprite(guiGraphics, sprite, x, y, uOffset, vOffset, width, height, textureHeight, textureHeight, color, BlendFunction.TRANSLUCENT);
	}

	public static void blit(GuiGraphics guiGraphics, ResourceLocation atlasLocation, float x, float y, float uOffset, float vOffset, float width, float height, int textureWidth, int textureHeight, int color, BlendFunction blendFunc) {
		float uMin = uOffset / textureWidth;
		float uMax = (uOffset + width) / textureWidth;
		float vMin = vOffset / textureHeight;
		float vMax = (vOffset + height) / textureHeight;
		blit(guiGraphics, atlasLocation, x, x + width, y, y + height, uMin, uMax, vMin, vMax, color, blendFunc);
	}

	public static void blitSprite(GuiGraphics guiGraphics, TextureAtlasSprite sprite, float x, float y, float uOffset, float vOffset, float width, float height, int textureWidth, int textureHeight, int color, BlendFunction blendFunc) {
		float spriteWidth = sprite.getU1() - sprite.getU0();
		float spriteHeight = sprite.getV1() - sprite.getV0();
		float uMin = sprite.getU0() + uOffset / textureWidth * spriteWidth;
		float uMax = sprite.getU0() + (uOffset + width) / textureWidth * spriteWidth;
		float vMin = sprite.getV0() + vOffset / textureHeight * spriteHeight;
		float vMax = sprite.getV0() + (vOffset + height) / textureHeight * spriteHeight;
		blit(guiGraphics, sprite.atlasLocation(), x, x + width, y, y + height, uMin, uMax, vMin, vMax, color, blendFunc);
	}

	static final Map<BlendFunction, Function<ResourceLocation, RenderType>> GUI_TEXTURED_RENDER_TYPES = Collections.synchronizedMap(new HashMap<>());

	static {
		GUI_TEXTURED_RENDER_TYPES.put(BlendFunction.TRANSLUCENT, RenderType::guiTextured);
		GUI_TEXTURED_RENDER_TYPES.put(null, RenderType::guiOpaqueTexturedBackground);
		GUI_TEXTURED_RENDER_TYPES.put(new BlendFunction(SourceFactor.ONE_MINUS_DST_COLOR, DestFactor.ONE_MINUS_SRC_COLOR, SourceFactor.ONE, DestFactor.ZERO), RenderType::crosshair);
	}

	static Function<ResourceLocation, RenderType> buildGuiTexturedRenderType(BlendFunction blendFunc) {
		String desc;
		if(blendFunc != null) {
			desc = String.join("_", blendFunc.sourceColor().name(), blendFunc.destColor().name(), blendFunc.sourceAlpha().name(), blendFunc.destAlpha().name()).toLowerCase(Locale.ROOT);
		}
		else {
			desc = "no_blend";
		}
		RenderPipeline.Builder builder = RenderPipeline.builder().
				withLocation(ResourceLocation.parse("jeidrawables:pipeline/gui_textured_" + desc)).
				withUniform("ModelViewMat", UniformType.MATRIX4X4).
				withUniform("ProjMat", UniformType.MATRIX4X4).
				withUniform("ColorModulator", UniformType.VEC4).
				withVertexShader("core/position_tex_color").
				withFragmentShader("core/position_tex_color").
				withoutBlend().
				withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS);
		if(blendFunc != null) {
			builder.withBlend(blendFunc);
		}
		RenderPipeline pipeline = builder.build();
		return Util.memoize(location -> RenderType.create(
				"jeidrawables:" + desc, 1536, pipeline,
				RenderType.CompositeState.builder().
				setTextureState(new RenderStateShard.TextureStateShard(location, TriState.DEFAULT, false)).
				createCompositeState(false)));
	}

	static void blit(GuiGraphics guiGraphics, ResourceLocation atlasLocation, float xMin, float xMax, float yMin, float yMax, float uMin, float uMax, float vMin, float vMax, int color, BlendFunction blendFunc) {
		RenderType renderType = GUI_TEXTURED_RENDER_TYPES.computeIfAbsent(blendFunc, GuiRenderUtil::buildGuiTexturedRenderType).apply(atlasLocation);
		Matrix4f matrix = guiGraphics.pose().last().pose();
		BufferSource bufferSource = ((GuiGraphicsAccessor)guiGraphics).jeidas$bufferSource();
		VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
		vertexConsumer.addVertex(matrix, xMin, yMin, 0).setUv(uMin, vMin).setColor(color);
		vertexConsumer.addVertex(matrix, xMin, yMax, 0).setUv(uMin, vMax).setColor(color);
		vertexConsumer.addVertex(matrix, xMax, yMax, 0).setUv(uMax, vMax).setColor(color);
		vertexConsumer.addVertex(matrix, xMax, yMin, 0).setUv(uMax, vMin).setColor(color);
	}
}
