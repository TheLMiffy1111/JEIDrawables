package thelm.jeidrawables.gui.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import mezz.jei.api.gui.drawable.IDrawable;

public record RotatedDrawable(IDrawable drawable, float degrees) implements IDrawable {

	@Override
	public int getWidth() {
		return drawable.getWidth();
	}

	@Override
	public int getHeight() {
		return drawable.getHeight();
	}

	@Override
	public void draw(PoseStack poseStack, int xOffset, int yOffset) {
		poseStack.pushPose();
		poseStack.translate(xOffset, yOffset, 0);
		poseStack.translate(drawable.getWidth() * 0.5, drawable.getHeight() * 0.5, 0);
		poseStack.mulPose(Axis.ZP.rotationDegrees(degrees));
		poseStack.translate(-drawable.getWidth() * 0.5, -drawable.getHeight() * 0.5, 0);
		poseStack.pushPose();
		drawable.draw(poseStack);
		poseStack.popPose();
		poseStack.popPose();
	}
}
