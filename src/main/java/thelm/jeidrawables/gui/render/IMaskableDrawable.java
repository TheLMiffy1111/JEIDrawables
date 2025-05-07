package thelm.jeidrawables.gui.render;

import mezz.jei.api.gui.drawable.IDrawableStatic;
import net.minecraft.client.gui.GuiGraphics;

public interface IMaskableDrawable extends IDrawableStatic {

	@Override
	default void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
		draw(guiGraphics, xOffset, yOffset, 0, 0, 0, 0);
	}

	default void draw(GuiGraphics guiGraphics, float xOffset, float yOffset) {
		draw(guiGraphics, xOffset, yOffset, 0, 0, 0, 0);
	}

	@Override
	default void draw(GuiGraphics guiGraphics, int xOffset, int yOffset, int maskTop, int maskBottom, int maskLeft, int maskRight) {
		draw(guiGraphics, (float)xOffset, (float)yOffset, maskTop, maskBottom, maskLeft, maskRight);
	}

	void draw(GuiGraphics guiGraphics, float xOffset, float yOffset, float maskTop, float maskBottom, float maskLeft, float maskRight);

	IMaskableDrawable trim(int trimTop, int trimBottom, int trimLeft, int trimRight);
}
