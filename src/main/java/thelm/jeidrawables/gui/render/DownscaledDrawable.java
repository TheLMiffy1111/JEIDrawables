package thelm.jeidrawables.gui.render;

import com.mojang.blaze3d.vertex.PoseStack;

import mezz.jei.api.gui.drawable.IDrawable;

public record DownscaledDrawable(IDrawable drawable, int scale) implements IDrawable {

	@Override
	public int getWidth() {
		return ceilDiv(drawable.getWidth(), scale);
	}

	@Override
	public int getHeight() {
		return ceilDiv(drawable.getHeight(), scale);
	}

	@Override
	public void draw(PoseStack poseStack, int xOffset, int yOffset) {
		poseStack.pushPose();
		poseStack.translate(xOffset, yOffset, 0);
		poseStack.scale(1F / scale, 1F / scale, 1);
		drawable.draw(poseStack);
		poseStack.popPose();
	}

	public static int ceilDiv(int x, int y){
		return -Math.floorDiv(-x, y);
	}
}
