package thelm.jeidrawables.gui.render;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public record ResourceDrawable(ResourceLocation atlasLocation, int u, int v, int width, int height, int textureWidth, int textureHeight, int color, BlendFunction blendFunc) implements ITextureDrawable {

	public ResourceDrawable(ResourceLocation atlasLocation, int u, int v, int width, int height, int textureWidth, int textureHeight, int color) {
		this(atlasLocation, u, v, width, height, textureWidth, textureHeight, color, BlendFunction.TRANSLUCENT);
	}

	public ResourceDrawable(ResourceLocation atlasLocation, int u, int v, int width, int height, int textureWidth, int textureHeight, BlendFunction blendFunc) {
		this(atlasLocation, u, v, width, height, textureWidth, textureHeight, -1, blendFunc);
	}

	public ResourceDrawable(ResourceLocation atlasLocation, int u, int v, int width, int height, int textureWidth, int textureHeight) {
		this(atlasLocation, u, v, width, height, textureWidth, textureHeight, -1, BlendFunction.TRANSLUCENT);
	}

	public ResourceDrawable(ResourceLocation atlasLocation, int u, int v, int width, int height, int color, BlendFunction blendFunc) {
		this(atlasLocation, u, v, width, height, 256, 256, color, blendFunc);
	}

	public ResourceDrawable(ResourceLocation atlasLocation, int u, int v, int width, int height, int color) {
		this(atlasLocation, u, v, width, height, 256, 256, color, BlendFunction.TRANSLUCENT);
	}

	public ResourceDrawable(ResourceLocation atlasLocation, int u, int v, int width, int height, BlendFunction blendFunc) {
		this(atlasLocation, u, v, width, height, 256, 256, -1, blendFunc);
	}

	public ResourceDrawable(ResourceLocation atlasLocation, int u, int v, int width, int height) {
		this(atlasLocation, u, v, width, height, 256, 256, -1, BlendFunction.TRANSLUCENT);
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void draw(GuiGraphics guiGraphics, float xOffset, float yOffset, float maskTop, float maskBottom, float maskLeft, float maskRight) {
		if(maskLeft + maskRight < width && maskTop + maskBottom < height) {
			GuiRenderUtil.blit(guiGraphics, atlasLocation, xOffset + maskLeft, yOffset + maskTop, u + maskLeft, v + maskTop, width - maskLeft - maskRight, height - maskTop - maskBottom, textureWidth, textureHeight, color, blendFunc);
		}
	}

	@Override
	public ITextureDrawable trim(int trimTop, int trimBottom, int trimLeft, int trimRight) {
		int newWidth = Math.max(width - trimLeft - trimRight, 0);
		int newHeight = Math.max(height - trimTop - trimBottom, 0);
		if(newWidth == 0 || newHeight == 0) {
			return new BlankDrawable(newWidth, newHeight);
		}
		return new ResourceDrawable(atlasLocation, u + trimLeft, v + trimTop, newWidth, newHeight, textureWidth, textureHeight, color, blendFunc);
	}

	@Override
	public ITextureDrawable withColor(int color) {
		return new ResourceDrawable(atlasLocation, u, v, width, height, textureWidth, textureHeight, 0xFF000000 & this.color | color, blendFunc);
	}

	@Override
	public ITextureDrawable withAlpha(int alpha) {
		return new ResourceDrawable(atlasLocation, u, v, width, height, textureWidth, textureHeight, 0xFFFFFF & color | alpha << 24, blendFunc);
	}

	@Override
	public ITextureDrawable withAlphaColor(int color) {
		return new ResourceDrawable(atlasLocation, u, v, width, height, textureWidth, textureHeight, color, blendFunc);
	}
}
