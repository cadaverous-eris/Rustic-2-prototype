package rustic.model;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.primitives.Ints;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
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
import rustic.blocks.BlockPot;
import rustic.util.RusticFluidUtil;

public class PotItemModel implements IBakedModel {

	protected IBakedModel baseModel;
	
	public PotItemModel(IBakedModel baseModel) {
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
	};

	@Override
	public ItemOverrideList getOverrides() {
		return new PotOverrideList();
	}
	
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		Matrix4f matrix4f = baseModel.handlePerspective(cameraTransformType).getRight();
		return Pair.of(this, matrix4f);
	}
	
	protected static class PotOverrideList extends ItemOverrideList {
		
		protected static List<ItemOverride> overrides = new ArrayList<ItemOverride>();
		
		public PotOverrideList() {
			super(overrides);
		}
		
		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
			if (stack.getItem() == Item.getItemFromBlock(RegistryManager.pot) && stack.hasTagCompound()) {
				NBTTagCompound tag = stack.getTagCompound();
				if (tag.hasKey("FluidName", Constants.NBT.TAG_STRING) && tag.hasKey("Amount", Constants.NBT.TAG_INT) && tag.hasKey("Capacity", Constants.NBT.TAG_INT)) {
					int capacity = tag.getInteger("Capacity");
					FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(tag);
					if (fluidStack != null && fluidStack.amount > 0 && capacity > 0) {
						int modelStyle = ((BlockPot) RegistryManager.pot).getModelStyle(stack.getMetadata());
						return new FilledPotItemModel(originalModel, modelStyle, fluidStack, capacity);
					}
				}
			}
			return originalModel;
		}
		
	}
	
	protected static class FilledPotItemModel extends PotItemModel {
		
		protected FluidStack fluidStack;
		protected int capacity;
		protected int modelStyle;
		
		FilledPotItemModel(IBakedModel baseModel, int modelStyle, FluidStack fluidStack, int capacity) {
			super(baseModel);
			this.modelStyle = modelStyle;
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
			
			float fluidHeight = 0.125F + (0.8125F * ((float) amount / (float) capacity));
			float fluidRadius = ((BlockPot) RegistryManager.pot).getInnerRadius(modelStyle, (int) (16 * fluidHeight));
			
			float x1, x2, x3, x4;
			float y1, y2, y3, y4;
			float z1, z2, z3, z4;

			x1 = x2 = 0.5F + fluidRadius;
			x3 = x4 = 0.5F - fluidRadius;
			z1 = z4 = 0.5F + fluidRadius;
			z2 = z3 = 0.5F - fluidRadius;
			y1 = y2 = y3 = y4 = fluidHeight;

			int c = 0xFFFFFFFF;
			if (fluid != null) {
				c = fluid.getColor();
			}
			
			int[] vertexData = Ints.concat(
					vertexToInts(x1, y1, z1, c, fluidTexture, (int) (16 * x1), (int) (16 * z1)),
					vertexToInts(x2, y2, z2, c, fluidTexture, (int) (16 * x2), (int) (16 * z2)),
					vertexToInts(x3, y3, z3, c, fluidTexture, (int) (16 * x3), (int) (16 * z3)),
					vertexToInts(x4, y4, z4, c, fluidTexture, (int) (16 * x4), (int) (16 * z4))
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
