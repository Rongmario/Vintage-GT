package gtclassic.container;

import gtclassic.GTClassic;
import gtclassic.tileentity.GTTileEntityFusionReactor;
import ic2.core.inventory.container.ContainerTileComponent;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.gui.components.base.MachineProgressComp;
import ic2.core.inventory.slots.SlotCustom;
import ic2.core.inventory.slots.SlotOutput;
import ic2.core.util.math.Box2D;
import ic2.core.util.math.Vec2i;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTContainerFusionReactor extends ContainerTileComponent<GTTileEntityFusionReactor> {
    
    
	public static ResourceLocation TEXTURE = new ResourceLocation(GTClassic.MODID, "textures/gui/fusionreactor.png");
	
	public static Box2D machineProgressBoxUp = new Box2D(83, 23, 10, 10);
    public static Vec2i machineProgressPosUp = new Vec2i(193, 23);
   

    public GTContainerFusionReactor(InventoryPlayer player, GTTileEntityFusionReactor tile)
    {
        super(tile);
        this.addSlotToContainer(new SlotCustom(tile, 0, 88, 17, null));//main slot
        this.addSlotToContainer(new SlotCustom(tile, 1, 88, 53, null));//second slot
        this.addSlotToContainer(new SlotOutput(player.player, tile, 2, 148, 35)); //output

        this.addPlayerInventory(player);
        this.addComponent(new MachineProgressComp(tile, GTContainerFusionReactor.machineProgressBoxUp, GTContainerFusionReactor.machineProgressPosUp));
    }
    
    @Override
    @SideOnly(Side.CLIENT)
	public void onGuiLoaded(GuiIC2 gui) 
    {
    	gui.disableName();
	}

    @Override
    public ResourceLocation getTexture() {
    	return TEXTURE;
    }

    @Override
    public int guiInventorySize() {
        return this.getGuiHolder().slotCount;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.getGuiHolder().canInteractWith(playerIn);
    }

}