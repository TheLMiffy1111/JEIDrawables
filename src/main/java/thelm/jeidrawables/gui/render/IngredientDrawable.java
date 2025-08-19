package thelm.jeidrawables.gui.render;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.joml.Matrix3x2fStack;

import com.google.common.base.Suppliers;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.gui.GuiGraphics;
import thelm.jeidrawables.JEIDrawables;

public record IngredientDrawable<T>(T ingredient, Supplier<IIngredientRenderer<T>> renderer) implements IDrawable {

	public IngredientDrawable(T ingredient) {
		this(ingredient, Suppliers.memoizeWithExpiration(() -> JEIDrawables.jeiRuntime.getIngredientManager().getIngredientRenderer(ingredient), 10, TimeUnit.SECONDS));
	}

	@Override
	public int getWidth() {
		try {
			return renderer.get().getWidth();
		}
		catch(Exception e) {
			return 0;
		}
	}

	@Override
	public int getHeight() {
		try {
			return renderer.get().getHeight();
		}
		catch(Exception e) {
			return 0;
		}
	}

	@Override
	public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
		Matrix3x2fStack pose = guiGraphics.pose();
		pose.pushMatrix();
		pose.translate(xOffset, yOffset);
		try {
			renderer.get().render(guiGraphics, ingredient);
		}
		catch(Exception e) {}
		pose.popMatrix();
	}
}
