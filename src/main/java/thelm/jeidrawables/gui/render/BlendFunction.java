package thelm.jeidrawables.gui.render;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;

public record BlendFunction(SourceFactor sourceColor, DestFactor destColor, SourceFactor sourceAlpha, DestFactor destAlpha) {

	public static final BlendFunction TRANSLUCENT = new BlendFunction(
			SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA,
			SourceFactor.ONE, DestFactor.ONE_MINUS_SRC_ALPHA);

	public BlendFunction(SourceFactor sourceFactor, DestFactor destFactor) {
		this(sourceFactor, destFactor, sourceFactor, destFactor);
	}
}
