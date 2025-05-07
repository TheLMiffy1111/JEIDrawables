package thelm.jeidrawables.gui.render;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import mezz.jei.api.gui.drawable.IDrawable;

public record CyclingDrawable(List<IDrawable> drawables, int millisPerDrawable) implements IDrawable {

	public CyclingDrawable(int millisPerDrawable, IDrawable... drawables) {
		this(List.of(drawables), millisPerDrawable);
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
			IDrawable drawable = drawables.get((int)(System.currentTimeMillis() / millisPerDrawable % drawables.size()));
			drawable.draw(poseStack, xOffset, yOffset);
		}
	}
}
