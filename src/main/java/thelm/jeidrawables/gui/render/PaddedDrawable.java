package thelm.jeidrawables.gui.render;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphics;

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
	public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
		drawable.draw(guiGraphics, padLeft + xOffset, padTop + yOffset);
	}
}
