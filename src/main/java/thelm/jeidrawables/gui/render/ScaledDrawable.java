package thelm.jeidrawables.gui.render;

import org.joml.Matrix3x2fStack;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphics;
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
	public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
		Matrix3x2fStack pose = guiGraphics.pose();
		pose.pushMatrix();
		pose.translate(xOffset, yOffset);
		pose.scale(widthScale, heightScale);
		drawable.draw(guiGraphics);
		pose.popMatrix();
	}
}
