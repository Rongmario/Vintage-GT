package gtclassic.blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gtclassic.GTClassic;
import gtclassic.tileentity.GTTileEntityQuantumChest;
import gtclassic.tileentity.GTTileEntitySmallChest;
import gtclassic.util.GTBlocks;
import ic2.core.block.base.BlockMultiID;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.platform.textures.Ic2Icons;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTBlockStorage extends BlockMultiID {
    public enum GTBlockStorageVariants
    {
    	SMALLCHEST,
    	LARGECHEST,
    	QUANTUMCHEST,
    }

    GTBlockStorageVariants variant;
    public GTBlockStorage(GTBlockStorageVariants variant)
    {
        super(Material.IRON);
        this.variant = variant;
        setRegistryName(variant.toString().toLowerCase());
        setUnlocalizedName(GTClassic.MODID + "." + variant.toString().toLowerCase());
        setCreativeTab(GTClassic.creativeTabGT);
        setHardness(4.0F);
        setResistance(20.0F);
        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", 2);
    }
    
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) 
    {
    	if (this == GTBlocks.quantumChest)
    	{
    	tooltip.add(I18n.format("tooltip."+ GTClassic.MODID +".quantumchest"));
    	}
    	
    	else
    	{
    	tooltip.add(I18n.format("tooltip."+ GTClassic.MODID +".chest"));
    	}
    	
    }
    
    @Override
    public List<Integer> getValidMetas() 
	{
        return Arrays.asList(0);
    }

    @Override
    public TileEntityBlock createNewTileEntity(World worldIn, int meta)
    {
    	 if (this == GTBlocks.quantumChest) 
    	 {
    		 return new GTTileEntityQuantumChest();
    	 }
    	 
    	 else if (this == GTBlocks.smallChest)
    	 {
    		 return new GTTileEntitySmallChest();
    	 }

    	 else
    	 {
    		 return new TileEntityBlock();
    	 }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite[] getIconSheet(int meta)
    {
    	
    	if (this == GTBlocks.smallChest) 
   	 	{
    		return Ic2Icons.getTextures("gtclassic_smallchest");
   	 	}
   	 
   	 	else if (this == GTBlocks.largeChest)
   	 	{
   	 		return Ic2Icons.getTextures("gtclassic_largechest");
   	 	}
    	
    	else if (this == GTBlocks.quantumChest)
	 	{
	 		return Ic2Icons.getTextures("gtclassic_quantumchest");
	 	}
    	
   	 	else
	 	{
	 		return Ic2Icons.getTextures("gtclassic_builder");
	 	}
    }
    
    @Override
    public int getMaxSheetSize(int meta)
    {
        return 1;
    }

    @Override
    public List<IBlockState> getValidStateList()
    {
        IBlockState def = getDefaultState();
        List<IBlockState> states = new ArrayList<>();
        for(EnumFacing side : EnumFacing.VALUES)
        {
        	states.add(def.withProperty(getMetadataProperty(), 0).withProperty(allFacings, side).withProperty(active, false));
        }
        return states;
    }

    @Override
    public List<IBlockState> getValidStates()
    {
        return getBlockState().getValidStates();
    }



}