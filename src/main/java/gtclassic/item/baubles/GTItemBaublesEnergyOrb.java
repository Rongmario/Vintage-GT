package gtclassic.item.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import gtclassic.item.GTItemEnergyOrb;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles", striprefs = true)
public class GTItemBaublesEnergyOrb extends GTItemEnergyOrb implements IBauble {

	@Override
	@Optional.Method(modid = "baubles")
	public BaubleType getBaubleType(ItemStack itemStack) {
		return BaubleType.BELT;
	}

	@Override
	@Optional.Method(modid = "baubles")
	public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}
}
