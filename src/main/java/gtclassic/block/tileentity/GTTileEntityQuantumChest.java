package gtclassic.block.tileentity;

import gtclassic.block.container.GTContainerQuantumChest;
import gtclassic.util.GTLang;
import ic2.core.RotationList;
import ic2.core.block.base.tile.TileEntityMachine;
import ic2.core.inventory.base.IHasGui;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.gui.GuiComponentContainer;
import ic2.core.inventory.management.AccessRule;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.inventory.management.SlotType;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.util.misc.StackUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTTileEntityQuantumChest extends TileEntityMachine implements IHasGui, ITickable {

	int slotInput = 0;
	int slotOutput = 1;
	int slotDisplay = 2;

	int maxSize = Integer.MAX_VALUE - 128;
	int quantumCount;

	public GTTileEntityQuantumChest() {
		super(3);
		this.quantumCount = 0;
		this.addGuiFields(new String[] { "quantumCount" });
	}

	@Override
	public int getMaxStackSize(int slot) {
		if (slot == 2) {
			return 1;
		} else {
			return 64;
		}
	}

	public void initQuantum(int slot) {
		this.quantumCount = (this.inventory.get(slot).getCount());
	}

	public void copySlotToQuantum(int slot) {
		this.quantumCount = this.quantumCount + inventory.get(slot).getCount();
	}

	public void removeQuantumToSlot(int size) {
		this.quantumCount = this.quantumCount - size;
		if (quantumCount < 0) {
			quantumCount = 0;
		}
	}

	@Override
	public void update() {
		if (!inventory.get(slotInput).isEmpty()) {
			if (inventory.get(slotDisplay).isEmpty()) {
				inventory.set(slotDisplay, inventory.get(slotInput).copy());
				initQuantum(slotDisplay);
				inventory.set(slotInput, ItemStack.EMPTY);
			} else if (!inventory.get(slotDisplay).isEmpty()
					&& StackUtil.isStackEqual(inventory.get(slotInput), inventory.get(slotDisplay), true, true)) {
				copySlotToQuantum(slotInput);
				inventory.get(slotDisplay).grow(inventory.get(slotInput).getCount());
				inventory.set(slotInput, ItemStack.EMPTY);
			}
		}

		if (!inventory.get(slotDisplay).isEmpty()) {
			int size = inventory.get(slotDisplay).getMaxStackSize();
			if (inventory.get(slotOutput) == ItemStack.EMPTY || inventory.get(slotOutput).getCount() == 0) {
				if (this.quantumCount > 0) {
					inventory.set(slotOutput, inventory.get(slotDisplay).copy());
					inventory.get(slotOutput).grow(size);
					removeQuantumToSlot(size);
					// inventory.get(slotDisplay).shrink(size);
				} else {
					inventory.set(slotOutput, inventory.get(slotDisplay));
					inventory.set(slotDisplay, ItemStack.EMPTY);
				}
			}
			if (inventory.get(slotDisplay).getCount() != 0
					&& StackUtil.isStackEqual(inventory.get(slotDisplay), inventory.get(slotOutput), true, true)
					&& inventory.get(slotOutput).getCount() <= size - 1) {
				// inventory.get(slotOutput).grow(1);
				inventory.get(slotDisplay).shrink(1);

			}

		}
	}

	public int getDisplayCount() {
		return inventory.get(slotDisplay).getCount();
	}

	public int getQuantumCount() {
		return this.quantumCount;
	}

	@Override
	public LocaleComp getBlockName() {
		return GTLang.quantumchest;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.quantumCount = nbt.getInteger("quantumCount");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("quantumCount", this.quantumCount);
		return nbt;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
		return GuiComponentContainer.class;
	}

	@Override
	public ContainerIC2 getGuiContainer(EntityPlayer player) {
		return new GTContainerQuantumChest(player.inventory, this);
	}

	@Override
	public boolean supportsRotation() {
		return false;
	}

	@Override
	public boolean canSetFacing(EntityPlayer player, EnumFacing facing) {
		return false;
	}

	@Override
	public boolean canRemoveBlock(EntityPlayer player) {
		return true;
	}

	@Override
	protected void addSlots(InventoryHandler handler) {
		handler.registerDefaultSideAccess(AccessRule.Both, RotationList.ALL);
		handler.registerDefaultSlotAccess(AccessRule.Import, 0);
		handler.registerDefaultSlotAccess(AccessRule.Export, 1);
		handler.registerDefaultSlotsForSide(RotationList.ALL, 0);
		handler.registerSlotType(SlotType.Input, 0);
		handler.registerSlotType(SlotType.Output, 1);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return !this.isInvalid();
	}

	@Override
	public void onGuiClosed(EntityPlayer entityPlayer) {
		// needed for construction
	}

	@Override
	public boolean hasGui(EntityPlayer player) {
		return true;
	}

}
