package gtclassic.api.pipe;

import java.util.Map;

import gtclassic.common.tile.GTTileTranslocator;
import ic2.core.inventory.transport.IItemTransporter;
import ic2.core.inventory.transport.TransporterManager;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public abstract class GTTilePipeItemBase extends GTTilePipeBase {

	public GTTilePipeItemBase(int slots) {
		super(slots);
	}

	@Override
	public boolean canConnect(TileEntity tile, EnumFacing dir) {
		if (tile == null) {
			return false;
		}
		if (tile instanceof GTTilePipeItemBase) {
			GTTilePipeItemBase pipe = (GTTilePipeItemBase) tile;
			return (!pipe.isColored() || !this.isColored()) || (this.color == pipe.color);
		}
		if (tile instanceof GTTileTranslocator) {
			return true;
		}
		return tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (getHandler() == null) {
				return false;
			}
			return this.handler.hasInventory(facing);
		}
		return super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (getHandler() == null) {
				return null;
			}
			return (T) this.handler.getInventory(facing);
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean isEmpty() {
		int empty = 0;
		for (int j = 0; j < this.inventory.size(); ++j) {
			if (this.inventory.get(j).isEmpty()) {
				empty = empty + 1;
			}
		}
		return empty == this.inventory.size();
	}

	@Override
	public void onPipeTick() {
		for (EnumFacing side : this.connection) {
			BlockPos sidePos = this.pos.offset(side);
			if (world.isBlockLoaded(sidePos) && !isBlacklistSide(side)) {
				TileEntity tile = world.getTileEntity(sidePos);
				boolean onlyPipesAndNotPipe = this.onlyPipes && !(tile instanceof GTTilePipeItemBase);
				boolean upwardHopper = side == EnumFacing.UP && tile instanceof TileEntityHopper;
				if (onlyPipesAndNotPipe || upwardHopper) {
					continue;
				}
				IItemTransporter slave = TransporterManager.manager.getTransporter(tile, false);
				if (slave != null) {
					for (int i = 0; i < this.inventory.size(); ++i) {
						int added = slave.addItem(this.getStackInSlot(i).copy(), side.getOpposite(), true).getCount();
						if (added > 0) {
							this.getStackInSlot(i).shrink(added);
						}
					}
				}
			}
		}
	}

	@Override
	public void getData(Map<String, Boolean> data) {
		boolean empty = true;
		for (int i = 0; i < this.inventory.size(); ++i) {
			ItemStack stack = this.getStackInSlot(i).copy();
			if (!stack.isEmpty()) {
				data.put(stack.getDisplayName() + " x " + stack.getCount(), false);
				empty = false;
			}
		}
		if (empty) {
			data.put("Pipe is empty", false);
		}
		data.put("Restricted only to pipes: " + this.onlyPipes, false);
		String last = this.blacklistSide != null ? this.blacklistSide.toString().toUpperCase() : "None";
		data.put("Blacklisted Side: " + last, true);
	}
}
