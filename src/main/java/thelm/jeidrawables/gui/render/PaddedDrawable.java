package thelm.jeidrawables.gui.render;

import com.mojang.blaze3d.vertex.PoseStack;

import mezz.jei.api.gui.drawable.IDrawable;

public record PaddedDrawable(IDrawable drawable, int padTop, int padBottom, int padLeft, int padRight) implements IDrawable {

	@Override
	public int getWidth() {
		return drawable.getWidth() + padLeft + padRight;
	}

	@Override
	public int getHeight() {
		return drawable.getHeight() + padTop + padBottom;
	}

	@Override
	public void draw(PoseStack poseStack, int xOffset, int yOffset) {
		drawable.draw(poseStack, padLeft + xOffset, padTop + yOffset);
	}
}
