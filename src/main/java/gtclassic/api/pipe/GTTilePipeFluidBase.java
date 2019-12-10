package gtclassic.api.pipe;

import gtclassic.api.helpers.GTHelperFluid;
import gtclassic.common.GTLang;
import ic2.core.fluid.IC2Tank;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.item.misc.ItemDisplayIcon;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.util.obj.ITankListener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public abstract class GTTilePipeFluidBase extends GTTilePipeBase implements ITankListener {

	private IC2Tank tank;

	public GTTilePipeFluidBase(int capacity) {
		super(1);
		this.tank = new IC2Tank(capacity);
		this.tank.addListener(this);
		this.addGuiFields("tank");
	}

	@Override
	public LocaleComp getBlockName() {
		return GTLang.FLUID_PIPE_LANG;
	}

	@Override
	public boolean canConnect(TileEntity tile, EnumFacing dir) {
		if (tile == null) {
			return false;
		}
		if (tile instanceof GTTilePipeFluidBase) {
			return true;
		}
		return tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, dir);
	}

	public void onTankChanged(IFluidTank tank) {
		this.getNetwork().updateTileGuiField(this, "tank");
		this.inventory.set(0, ItemDisplayIcon.createWithFluidStack(this.tank.getFluid()));
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.tank.readFromNBT(nbt.getCompoundTag("tank"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		this.tank.writeToNBT(this.getTag(nbt, "tank"));
		return nbt;
	}

	@Override
	public ContainerIC2 getGuiContainer(EntityPlayer player) {
		return new GTContainerPipeFluid(player.inventory, this);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? true
				: super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
				? CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.tank)
				: super.getCapability(capability, facing);
	}

	public IC2Tank getTankInstance() {
		return this.tank;
	}

	@Override
	public boolean isEmpty() {
		return this.tank.getFluid() == null;
	}

	@Override
	public void onPipeTick() {
		for (EnumFacing side : this.connection.getRandomIterator()) {
			BlockPos sidePos = this.pos.offset(side);
			if (world.isBlockLoaded(sidePos) && !isLastRecievedFrom(side)) {
				TileEntity tile = world.getTileEntity(sidePos);
				if (this.onlyPipes && !(tile instanceof GTTilePipeFluidBase)) {
					continue;
				}
				IFluidHandler fluidTile = GTHelperFluid.getFluidHandler(world, sidePos, side);
				boolean canExport = fluidTile != null && this.tank.getFluid() != null;
				if (canExport && FluidUtil.tryFluidTransfer(fluidTile, this.tank, this.tank.getCapacity()
						/ 2, true) != null) {
					this.idle = 0;
					if (tile instanceof GTTilePipeFluidBase) {
						((GTTilePipeFluidBase) tile).lastRecievedFrom = side.getOpposite();
					}
				}
			}
		}
	}

	@Override
	public String[] cheapInfo() {
		FluidStack fluid = this.tank.getFluid();
		String fluidName = fluid != null ? fluid.amount + "mB of " + fluid.getLocalizedName() : "Empty";
		return new String[] { fluidName, "Restricted only to pipes: " + this.onlyPipes };
	}

	@Override
	public String[] scannerInfo() {
		String in = this.lastRecievedFrom != null ? this.lastRecievedFrom.toString().toUpperCase() : "None";
		return new String[] { "Time Idle: " + this.idle / 20 + "/5 Seconds", "Last Recieved From: " + in };
	}
}