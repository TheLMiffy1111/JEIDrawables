package thelm.jeidrawables.gui.render;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import mezz.jei.api.gui.drawable.IDrawable;

public record LayeredDrawable(List<IDrawable> drawables) implements IDrawable {

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
	public void draw(PoseStack poseStack, int xOffset, int yOffset) {
		if(!drawables.isEmpty()) {
			for(IDrawable drawable : drawables) {
				drawable.draw(poseStack, xOffset, yOffset);
			}
		}
	}
}
