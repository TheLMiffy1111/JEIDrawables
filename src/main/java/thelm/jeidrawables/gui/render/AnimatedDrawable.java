package thelm.jeidrawables.gui.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public record AnimatedDrawable(IMaskableDrawable drawable, Type type, int millisPerCycle) implements IMaskableDrawable {

	@Override
	public int getWidth() {
		return drawable.getWidth();
	}

	@Override
	public int getHeight() {
		return drawable.getHeight();
	}

	@Override
	public void draw(GuiGraphics guiGraphics, float xOffset, float yOffset, float maskTop, float maskBottom, float maskLeft, float maskRight) {
		Minecraft minecraft = Minecraft.getInstance();
		int guiScale = minecraft.getWindow().calculateScale(minecraft.options.guiScale().get(), minecraft.isEnforceUnicode());
		float xMask = drawable.getWidth() - Math.round(System.currentTimeMillis() % millisPerCycle * guiScale * drawable.getWidth() / (float)millisPerCycle) / (float)guiScale;
		float yMask = drawable.getHeight() - Math.round(System.currentTimeMillis() % millisPerCycle * guiScale * drawable.getHeight() / (float)millisPerCycle) / (float)guiScale;
		switch(type) {
		case LEFT_FILL -> {
			maskRight = Math.max(xMask, maskRight);
		}
		case LEFT_EMPTY -> {
			maskRight = Math.max(drawable.getWidth() - xMask, maskRight);
		}
		case LEFT_FILL_MOVING -> {
			xOffset -= xMask;
			maskLeft += xMask;
			maskRight = Math.max(maskRight - xMask, 0);
		}
		case LEFT_EMPTY_MOVING -> {
			float mask = drawable.getWidth() - xMask;
			xOffset -= mask;
			maskLeft += mask;
			maskRight = Math.max(maskRight - mask, 0);
		}
		case RIGHT_FILL -> {
			maskLeft = Math.max(xMask, maskLeft);
		}
		case RIGHT_EMPTY -> {
			maskLeft = Math.max(drawable.getWidth() - xMask, maskLeft);
		}
		case RIGHT_FILL_MOVING -> {
			xOffset += xMask;
			maskRight += xMask;
			maskLeft = Math.max(maskLeft - xMask, 0);
		}
		case RIGHT_EMPTY_MOVING -> {
			float mask = drawable.getWidth() - xMask;
			xOffset += mask;
			maskRight += mask;
			maskLeft = Math.max(maskLeft - mask, 0);
		}
		case TOP_FILL -> {
			maskBottom = Math.max(yMask, maskBottom);
		}
		case TOP_EMPTY -> {
			maskBottom = Math.max(drawable.getHeight() - yMask, maskBottom);
		}
		case TOP_FILL_MOVING -> {
			yOffset -= yMask;
			maskTop += yMask;
			maskBottom = Math.max(maskBottom - yMask, 0);
		}
		case TOP_EMPTY_MOVING -> {
			float mask = drawable.getHeight() - yMask;
			yOffset -= mask;
			maskTop += mask;
			maskBottom = Math.max(maskBottom - mask, 0);
		}
		case BOTTOM_FILL -> {
			maskTop = Math.max(yMask, maskTop);
		}
		case BOTTOM_EMPTY -> {
			maskTop = Math.max(drawable.getHeight() - yMask, maskTop);
		}
		case BOTTOM_FILL_MOVING -> {
			yOffset += yMask;
			maskBottom += yMask;
			maskTop = Math.max(maskTop - yMask, 0);
		}
		case BOTTOM_EMPTY_MOVING -> {
			float mask = drawable.getHeight() - yMask;
			yOffset += mask;
			maskBottom += mask;
			maskTop = Math.max(maskTop - mask, 0);
		}
		}
		drawable.draw(guiGraphics, xOffset, yOffset, maskTop, maskBottom, maskLeft, maskRight);
	}

	@Override
	public IMaskableDrawable trim(int trimTop, int trimBottom, int trimLeft, int trimRight) {
		IMaskableDrawable newDrawable = drawable.trim(trimTop, trimBottom, trimLeft, trimRight);
		return new AnimatedDrawable(newDrawable, type, millisPerCycle);
	}

	public enum Type {
		LEFT_FILL, RIGHT_FILL, TOP_FILL, BOTTOM_FILL,
		LEFT_EMPTY, RIGHT_EMPTY, TOP_EMPTY, BOTTOM_EMPTY,
		LEFT_FILL_MOVING, RIGHT_FILL_MOVING, TOP_FILL_MOVING, BOTTOM_FILL_MOVING,
		LEFT_EMPTY_MOVING, RIGHT_EMPTY_MOVING, TOP_EMPTY_MOVING, BOTTOM_EMPTY_MOVING;
	}
}
