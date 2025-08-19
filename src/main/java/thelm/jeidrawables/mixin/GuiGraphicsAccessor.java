package thelm.jeidrawables.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;

@Mixin(GuiGraphics.class)
public interface GuiGraphicsAccessor {

	@Accessor("bufferSource")
	BufferSource jeidas$bufferSource();
}
