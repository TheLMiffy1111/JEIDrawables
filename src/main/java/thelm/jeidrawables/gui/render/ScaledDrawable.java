package thelm.jeidrawables.gui.render;

import com.mojang.blaze3d.vertex.PoseStack;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.util.Mth;

public record ScaledDrawable(IDrawable drawable, float widthScale, float heightScale) implements IDrawable {

	public ScaledDrawable(IDrawable drawable, float scale) {
		this(drawable, scale, scale);	
	}

	@Override
	public int getWidth() {
		return Mth.ceil(drawable.getWidth() * widthScale - Mth.EPSILON);
	}

	@Override
	public int getHeight() {
		return Mth.ceil(drawable.getHeight() * heightScale - Mth.EPSILON);
	}

	@Override
	public void draw(PoseStack poseStack, int xOffset, int yOffset) {
		poseStack.pushPose();
		poseStack.translate(xOffset, yOffset, 0);
		poseStack.scale(widthScale, heightScale, 1);
		drawable.draw(poseStack);
		poseStack.popPose();
	}
}
