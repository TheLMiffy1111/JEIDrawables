package thelm.jeidrawables.gui.render;

import com.mojang.blaze3d.vertex.PoseStack;

import mezz.jei.api.gui.drawable.IDrawableStatic;

public interface IMaskableDrawable extends IDrawableStatic {

	@Override
	default void draw(PoseStack poseStack, int xOffset, int yOffset) {
		draw(poseStack, xOffset, yOffset, 0, 0, 0, 0);
	}

	default void draw(PoseStack poseStack, float xOffset, float yOffset) {
		draw(poseStack, xOffset, yOffset, 0, 0, 0, 0);
	}

	@Override
	default void draw(PoseStack poseStack, int xOffset, int yOffset, int maskTop, int maskBottom, int maskLeft, int maskRight) {
		draw(poseStack, (float)xOffset, (float)yOffset, maskTop, maskBottom, maskLeft, maskRight);
	}

	void draw(PoseStack poseStack, float xOffset, float yOffset, float maskTop, float maskBottom, float maskLeft, float maskRight);

	IMaskableDrawable trim(int trimTop, int trimBottom, int trimLeft, int trimRight);
}
