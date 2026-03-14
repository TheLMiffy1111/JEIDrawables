package thelm.jeidrawables.gui.render;

public interface ITextureDrawable extends IMaskableDrawable, IColorableDrawable {

	@Override
	ITextureDrawable trim(int trimTop, int trimBottom, int trimLeft, int trimRight);

	@Override
	ITextureDrawable withColor(int color);

	@Override
	ITextureDrawable withAlpha(int alpha);

	@Override
	ITextureDrawable withAlphaColor(int color);
}
