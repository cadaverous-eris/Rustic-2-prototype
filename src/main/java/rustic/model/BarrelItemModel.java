package rustic.model;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.primitives.Ints;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import rustic.RegistryManager;
import rustic.util.RusticFluidUtil;

public class BarrelItemModel implements IBakedModel {
	
	protected IBakedModel baseModel;
	
	public BarrelItemModel(IBakedModel baseModel) {
		this.baseModel = baseModel;
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		return baseModel.getQuads(state, side, rand);
	}

	@Override
	public boolean isAmbientOcclusion() {
		return baseModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return baseModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return baseModel.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return baseModel.getParticleTexture();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return new BarrelOverrideList();
	}
	
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		Matrix4f matrix4f = baseModel.handlePerspective(cameraTransformType).getRight();
		return Pair.of(this, matrix4f);
	}
	
	protected static class BarrelOverrideList extends ItemOverrideList {

		protected static List<ItemOverride> overrides = new ArrayList<ItemOverride>();
		
		public BarrelOverrideList() {
			super(overrides);
		}
		
		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
			if (stack.getItem() == Item.getItemFromBlock(RegistryManager.barrel) && stack.hasTagCompound()) {
				NBTTagCompound tag = stack.getTagCompound();
				if (tag.hasKey("FluidName", Constants.NBT.TAG_STRING) && tag.hasKey("Amount", Constants.NBT.TAG_INT) && tag.hasKey("Capacity", Constants.NBT.TAG_INT)) {
					int capacity = tag.getInteger("Capacity");
					FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(tag);
					if (fluidStack != null && fluidStack.amount > 0 && capacity > 0) {
						return new FilledBarrelItemModel(originalModel, fluidStack, capacity);
					}
				}
			}
			return originalModel;
		}
		
	}
	
	protected static class FilledBarrelItemModel extends BarrelItemModel {
		
		protected FluidStack fluidStack;
		protected int capacity;
		
		FilledBarrelItemModel(IBakedModel baseModel, FluidStack fluidStack, int capacity) {
			super(baseModel);
			this.fluidStack = fluidStack;
			this.capacity = capacity;
		}
		
		@Override
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
			List<BakedQuad> quads = new ArrayList(baseModel.getQuads(state, side, rand));
			quads.addAll(getFluidQuads(fluidStack.amount, capacity, fluidStack.getFluid()));
			return quads;
		}
		
		private int[] vertexToInts(float x, float y, float z, int color, TextureAtlasSprite texture, float u, float v) {
			return new int[] {
					Float.floatToRawIntBits(x),
					Float.floatToRawIntBits(y),
					Float.floatToRawIntBits(z),
					color,
					Float.floatToRawIntBits(texture.getInterpolatedU(u)),
					Float.floatToRawIntBits(texture.getInterpolatedV(v)),
					0
			};
		}
		
		protected List<BakedQuad> getFluidQuads(int amount, int capacity, Fluid fluid) {
			List<BakedQuad> quads = new ArrayList<BakedQuad>();
			TextureAtlasSprite fluidTexture = RusticFluidUtil.stillTextures.get(fluid);

			if (fluidTexture == null) return quads;
			
			float x1, x2, x3, x4;
			float y1, y2, y3, y4;
			float z1, z2, z3, z4;

			x1 = x2 = 0.8125F;
			x3 = x4 = 0.1875F;
			z1 = z4 = 0.8125F;
			z2 = z3 = 0.1875F;
			y1 = y2 = y3 = y4 = 0.125F + (0.8125F * ((float) amount / (float) capacity));

			int c = 0xFF000000;
			if (fluid != null) {
				c = fluid.getColor();
			}
			
			int[] vertexData = Ints.concat(
					vertexToInts(x1, y1, z1, c, fluidTexture, 16, 16),
					vertexToInts(x2, y2, z2, c, fluidTexture, 16, 0),
					vertexToInts(x3, y3, z3, c, fluidTexture, 0, 0),
					vertexToInts(x4, y4, z4, c, fluidTexture, 0, 16)
			);

			quads.add(new BakedQuad(vertexData, 0, EnumFacing.UP, fluidTexture, false, DefaultVertexFormats.BLOCK));

			return quads;
		}

		protected static List<ItemOverride> overrides = new ArrayList<ItemOverride>();
		@Override
		public ItemOverrideList getOverrides() {
			return new ItemOverrideList(overrides);
		}
		
	}
	
}
