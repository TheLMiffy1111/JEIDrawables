package thelm.jeidrawables.gui.render;

import org.joml.Matrix3x2f;
import org.joml.Vector2f;

import com.google.common.primitives.Floats;
import com.mojang.blaze3d.pipeline.RenderPipeline;
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
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import thelm.jeidrawables.mixin.GuiGraphicsAccessor;

public class GuiRenderUtil {

	public static void blit(GuiGraphics guiGraphics, Identifier atlasLocation, float x, float y, float uOffset, float vOffset, float width, float height, int textureWidth, int textureHeight, int color) {
		float uMin = uOffset / textureWidth;
		float uMax = (uOffset + width) / textureWidth;
		float vMin = vOffset / textureHeight;
		float vMax = (vOffset + height) / textureHeight;
		blit(guiGraphics, atlasLocation, x, x + width, y, y + height, uMin, uMax, vMin, vMax, color);
	}

	public static void blitSprite(GuiGraphics guiGraphics, TextureAtlasSprite sprite, float x, float y, float uOffset, float vOffset, float width, float height, int textureWidth, int textureHeight, int color) {
		float spriteWidth = sprite.getU1() - sprite.getU0();
		float spriteHeight = sprite.getV1() - sprite.getV0();
		float uMin = sprite.getU0() + uOffset / textureWidth * spriteWidth;
		float uMax = sprite.getU0() + (uOffset + width) / textureWidth * spriteWidth;
		float vMin = sprite.getV0() + vOffset / textureHeight * spriteHeight;
		float vMax = sprite.getV0() + (vOffset + height) / textureHeight * spriteHeight;
		blit(guiGraphics, sprite.atlasLocation(), x, x + width, y, y + height, uMin, uMax, vMin, vMax, color);
	}

	static void blit(GuiGraphics guiGraphics, Identifier atlasLocation, float xMin, float xMax, float yMin, float yMax, float uMin, float uMax, float vMin, float vMax, int color) {
		GuiRenderState renderState = ((GuiGraphicsAccessor)guiGraphics).jeidas$guiRenderState();
		AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(atlasLocation);
		renderState.submitGuiElement(new BlitRenderState(RenderPipelines.GUI_TEXTURED, TextureSetup.singleTexture(texture.getTextureView(), texture.getSampler()), new Matrix3x2f(guiGraphics.pose()), xMin, xMax, yMin, yMax, uMin, uMax, vMin, vMax, color));
	}

	public static record BlitRenderState(RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2f pose, float xMin, float xMax, float yMin, float yMax, float uMin, float uMax, float vMin, float vMax, int color) implements GuiElementRenderState {

		@Override
		public void buildVertices(VertexConsumer consumer) {
			consumer.addVertexWith2DPose(pose, xMin, yMin).setUv(uMin, vMin).setColor(color);
			consumer.addVertexWith2DPose(pose, xMin, yMax).setUv(uMin, vMax).setColor(color);
			consumer.addVertexWith2DPose(pose, xMax, yMax).setUv(uMax, vMax).setColor(color);
			consumer.addVertexWith2DPose(pose, xMax, yMin).setUv(uMax, vMin).setColor(color);
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
