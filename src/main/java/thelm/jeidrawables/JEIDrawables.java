package thelm.jeidrawables;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import thelm.jeidrawables.gui.render.AnimatedDrawable;
import thelm.jeidrawables.gui.render.DownscaledDrawable;
import thelm.jeidrawables.gui.render.LayeredDrawable;
import thelm.jeidrawables.gui.render.ResourceDrawable;

@JeiPlugin
public class JEIDrawables implements IModPlugin {

	public static final ResourceLocation UID = ResourceLocation.tryParse("jeidrawables:plugin");

	public static IIngredientManager ingredientManager;
	public static IJeiRuntime jeiRuntime;

	public static final ResourceLocation ELEMENTS = ResourceLocation.tryParse("jeidrawables:textures/gui/elements.png");

	public static final ResourceDrawable SLOT = new ResourceDrawable(ELEMENTS, 0, 0, 18, 18);
	public static final ResourceDrawable OUTPUT_SLOT = new ResourceDrawable(ELEMENTS, 18, 0, 26, 26);
	public static final ResourceDrawable RECIPE_ARROW = new ResourceDrawable(ELEMENTS, 44, 0, 22, 16);
	public static final ResourceDrawable RECIPE_ARROW_FILLED = new ResourceDrawable(ELEMENTS, 66, 0, 22, 16);
	public static final ResourceDrawable RECIPE_PLUS_SIGN = new ResourceDrawable(ELEMENTS, 66, 0, 13, 13);
	public static final IDrawable SHAPELESS_ICON = new DownscaledDrawable(new ResourceDrawable(ELEMENTS, 101, 0, 36, 36), 4);
	public static final ResourceDrawable ARROW_NEXT = new ResourceDrawable(ELEMENTS, 137, 0, 9, 9);
	public static final ResourceDrawable ARROW_PREVIOUS = new ResourceDrawable(ELEMENTS, 146, 0, 9, 9);
	public static final ResourceDrawable INFO = new ResourceDrawable(ELEMENTS, 155, 0, 16, 16);
	public static final ResourceDrawable FLAME = new ResourceDrawable(ELEMENTS, 171, 0, 14, 14);
	public static final ResourceDrawable FLAME_EMPTY = new ResourceDrawable(ELEMENTS, 185, 0, 14, 14);

	@Override
	public ResourceLocation getPluginUid() {
		return UID;
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		JEIDrawables.jeiRuntime = jeiRuntime;
	}

	public static IDrawable recipeArrow(int millisPerCycle) {
		if(millisPerCycle > 0) {
			return new LayeredDrawable(
					RECIPE_ARROW,
					new AnimatedDrawable(RECIPE_ARROW_FILLED, AnimatedDrawable.Type.LEFT_FILL, millisPerCycle));
		}
		else {
			return RECIPE_ARROW;
		}
	}

	public static IDrawable flame(int millisPerCycle) {
		if(millisPerCycle > 0) {
			return new LayeredDrawable(
					FLAME_EMPTY,
					new AnimatedDrawable(FLAME, AnimatedDrawable.Type.BOTTOM_EMPTY, millisPerCycle));
		}
		else {
			return FLAME;
		}
	}
}
