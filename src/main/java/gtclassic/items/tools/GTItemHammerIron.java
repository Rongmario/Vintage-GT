package gtclassic.items.tools;

import java.util.Arrays;
import java.util.List;

import gtclassic.GTClassic;
import gtclassic.GTMaterials;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.obj.IStaticTexturedItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTItemHammerIron extends ItemPickaxe implements IStaticTexturedItem {
	
	public GTItemHammerIron() {
		super(GTMaterials.IRON);
		this.setMaxDamage(500);
		setRegistryName("iron_hammer");
        setUnlocalizedName(GTClassic.MODID + ".hammerIron");
        setCreativeTab(GTClassic.creativeTabGT);
    }
	
	@Override
    public ItemStack getContainerItem(ItemStack itemStack){
         ItemStack returnItem = new ItemStack(itemStack.getItem(), 1, itemStack.getItemDamage()+1);
         if (itemStack.isItemEnchanted()){
              NBTTagCompound nbtcompound = itemStack.getTagCompound();
              returnItem.setTagCompound(nbtcompound);
         }        
         return returnItem;
    }
	
	@Override
    public List<Integer> getValidVariants() {
        return Arrays.asList(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getTexture(int meta) {
        return Ic2Icons.getTextures("gtclassic_items")[29];
    }

}