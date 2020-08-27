package gtclassic.common.item;

import ic2.api.classic.reactor.IReactorProduct;
import ic2.api.reactor.IReactor;
import net.minecraft.item.ItemStack;

public class GTItemDepletedRod extends GTItemComponent implements IReactorProduct {
    /**
     * Constructor for making a simple item with no action.
     *
     * @param type - String type of depleted rod
     * @param title - String name for the item
     * @param x    - int column
     * @param y    - int row
     */
    public GTItemDepletedRod(String type, String title, int x, int y) {
        super(type + "_rod_" + title, x, y);
    }

    @Override
    public boolean isProduct(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean canBePlacedIn(ItemStack itemStack, IReactor iReactor) {
        return false;
    }
}
