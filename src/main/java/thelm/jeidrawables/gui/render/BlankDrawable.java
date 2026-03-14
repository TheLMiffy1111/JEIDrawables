package thelm.jeidrawables.gui.render;

import com.mojang.blaze3d.vertex.PoseStack;

public record BlankDrawable(int width, int height) implements ITextureDrawable {

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void draw(PoseStack poseStack, float xOffset, float yOffset, float maskTop, float maskBottom, float maskLeft, float maskRight) {}

	@Override
	public ITextureDrawable trim(int trimTop, int trimBottom, int trimLeft, int trimRight) {
		int newWidth = Math.max(width - trimLeft - trimRight, 0);
		int newHeight = Math.max(height - trimTop - trimBottom, 0);
		return new BlankDrawable(newWidth, newHeight);
	}

	@Override
	public ITextureDrawable withColor(int color) {
		return this;
	}

	@Override
	public ITextureDrawable withAlpha(int alpha) {
		return this;
	}

	@Override
	public ITextureDrawable withAlphaColor(int color) {
		return this;
	}
}
