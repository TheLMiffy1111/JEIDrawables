package thelm.jeidrawables.gui.render;

import mezz.jei.api.gui.drawable.IDrawable;

public interface IColorableDrawable extends IDrawable {

	IColorableDrawable withColor(int color);

	IColorableDrawable withAlpha(int alpha);

	IColorableDrawable withAlphaColor(int color);
}
