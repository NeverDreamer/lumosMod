package com.Meli4.lumos.common.block;

import com.Meli4.lumos.common.tileentity.TileEntityInit;
import net.minecraft.block.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class BlockLightSource extends AirBlock  {
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(0D, 0.0D, 0D, 0D, 0D, 0D);
    public BlockLightSource(Properties properties) {
        super(properties);
        properties.setOpaque((state, reader, pos) -> {
            return false;
        });
        //this.setDefaultState(new BlockState(IntegerProperty.create("lightProp", 0,1), BooleanProperty.create("lightBooleanProp"), EnumProperty.create()));
        //this.setRegistryName( "blocklight");
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }


    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos){
        return 15;
    }


   public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileEntityInit.BLOCKLIGHTSOURCE.get().create();
    }

}
