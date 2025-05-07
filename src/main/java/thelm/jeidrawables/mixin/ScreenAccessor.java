package thelm.jeidrawables.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;

@Mixin(Screen.class)
public interface ScreenAccessor {

	@Invoker("addRenderableOnly")
	<T extends Renderable> T jeidas$addRenderable(T renderable);
}
