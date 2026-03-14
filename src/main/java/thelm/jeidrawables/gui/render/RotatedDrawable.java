package thelm.jeidrawables.gui.render;

import org.joml.Matrix3x2fStack;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphics;

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
	public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
		Matrix3x2fStack pose = guiGraphics.pose();
		pose.pushMatrix();
		pose.translate(xOffset, yOffset);
		pose.translate(drawable.getWidth() * 0.5F, drawable.getHeight() * 0.5F);
		pose.rotate((float)Math.toRadians(degrees));
		pose.translate(-drawable.getWidth() * 0.5F, -drawable.getHeight() * 0.5F);
		pose.pushMatrix();
		drawable.draw(guiGraphics);
		pose.popMatrix();
		pose.popMatrix();
	}
}
