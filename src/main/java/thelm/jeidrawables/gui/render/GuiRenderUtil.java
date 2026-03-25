package thelm.jeidrawables.gui.render;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.joml.Matrix3x2f;
import org.joml.Vector2f;

import com.google.common.primitives.Floats;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
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

	static final Map<BlendFunction, RenderPipeline> GUI_TEXTURED_PIPELINES = Collections.synchronizedMap(new HashMap<>());

	static {	
		GUI_TEXTURED_PIPELINES.put(BlendFunction.TRANSLUCENT, RenderPipelines.GUI_TEXTURED);
		GUI_TEXTURED_PIPELINES.put(BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA, RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA);
		GUI_TEXTURED_PIPELINES.put(null, RenderPipelines.GUI_OPAQUE_TEXTURED_BACKGROUND);
		GUI_TEXTURED_PIPELINES.put(BlendFunction.ADDITIVE, RenderPipelines.GUI_NAUSEA_OVERLAY);
		GUI_TEXTURED_PIPELINES.put(new BlendFunction(SourceFactor.ZERO, DestFactor.ONE_MINUS_SRC_COLOR), RenderPipelines.VIGNETTE);
		GUI_TEXTURED_PIPELINES.put(BlendFunction.INVERT, RenderPipelines.CROSSHAIR);
		GUI_TEXTURED_PIPELINES.put(new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE), RenderPipelines.MOJANG_LOGO);
	}

	static RenderPipeline buildGuiTexturedPipeline(BlendFunction blendFunc) {
		String desc = "no_blend";
		if(blendFunc != null) {
			desc = String.join("_", blendFunc.sourceColor().name(), blendFunc.destColor().name(), blendFunc.sourceAlpha().name(), blendFunc.destAlpha().name()).toLowerCase(Locale.ROOT);
		}
		RenderPipeline.Builder builder = RenderPipeline.builder().
				withLocation(ResourceLocation.parse("jeidrawables:pipeline/gui_textured_" + desc)).
				withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER).
				withUniform("Projection", UniformType.UNIFORM_BUFFER).
				withVertexShader("core/gui").
				withFragmentShader("core/gui").
				withoutBlend();
		if(blendFunc != null) {
			builder.withBlend(blendFunc);
		}
		return builder.build();
	}

	static void blit(GuiGraphics guiGraphics, ResourceLocation atlasLocation, float xMin, float xMax, float yMin, float yMax, float uMin, float uMax, float vMin, float vMax, int color, BlendFunction blendFunc) {
		GuiRenderState renderState = ((GuiGraphicsAccessor)guiGraphics).jeidas$guiRenderState();
		RenderPipeline pipeline = GUI_TEXTURED_PIPELINES.computeIfAbsent(blendFunc, GuiRenderUtil::buildGuiTexturedPipeline);
		AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(atlasLocation);
		renderState.submitGuiElement(new BlitRenderState(pipeline, TextureSetup.singleTexture(texture.getTextureView()), new Matrix3x2f(guiGraphics.pose()), xMin, xMax, yMin, yMax, uMin, uMax, vMin, vMax, color));
	}

	public static record BlitRenderState(RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2f pose, float xMin, float xMax, float yMin, float yMax, float uMin, float uMax, float vMin, float vMax, int color) implements GuiElementRenderState {

		@Override
		public void buildVertices(VertexConsumer consumer, float z) {
			consumer.addVertexWith2DPose(pose, xMin, yMin, z).setUv(uMin, vMin).setColor(color);
			consumer.addVertexWith2DPose(pose, xMin, yMax, z).setUv(uMin, vMax).setColor(color);
			consumer.addVertexWith2DPose(pose, xMax, yMax, z).setUv(uMax, vMax).setColor(color);
			consumer.addVertexWith2DPose(pose, xMax, yMin, z).setUv(uMax, vMin).setColor(color);
		}

		@Override
		public ScreenRectangle scissorArea() {
			return null;
		}

		@Override
		public ScreenRectangle bounds() {
			Vector2f x0y0 = pose.transformPosition(xMin, yMin, new Vector2f());
			Vector2f x1y0 = pose.transformPosition(xMax, yMin, new Vector2f());
			Vector2f x0y1 = pose.transformPosition(xMin, yMax, new Vector2f());
			Vector2f x1y1 = pose.transformPosition(xMax, yMax, new Vector2f());
			int ixMin = Mth.floor(Floats.min(x0y0.x(), x0y1.x(), x1y0.x(), x1y1.x()) + Mth.EPSILON);
			int ixMax = Mth.ceil(Floats.max(x0y0.x(), x0y1.x(), x1y0.x(), x1y1.x()) - Mth.EPSILON);
			int iyMin = Mth.floor(Floats.min(x0y0.y(), x0y1.y(), x1y0.y(), x1y1.y()) + Mth.EPSILON);
			int iyMax = Mth.ceil(Floats.max(x0y0.y(), x0y1.y(), x1y0.y(), x1y1.y()) - Mth.EPSILON);
			return new ScreenRectangle(ixMin, iyMin, ixMax - ixMin, iyMax - iyMin);
		}
	}
}
