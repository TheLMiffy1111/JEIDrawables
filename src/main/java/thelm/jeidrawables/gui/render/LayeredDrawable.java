package thelm.jeidrawables.gui.render;

import java.util.List;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphics;

public record LayeredDrawable(List<? extends IDrawable> drawables) implements IDrawable {

	public LayeredDrawable(IDrawable... drawables) {
		this(List.of(drawables));
	}

	@Override
	public int getWidth() {
		return drawables.stream().mapToInt(IDrawable::getWidth).max().orElse(0);
	}

	@Override
	public int getHeight() {
		return drawables.stream().mapToInt(IDrawable::getHeight).max().orElse(0);
	}

	@Override
	public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
		if(!drawables.isEmpty()) {
			for(IDrawable drawable : drawables) {
				drawable.draw(guiGraphics, xOffset, yOffset);
			}
		}
	}
}
