package com.Meli4.lumos.common.tileentity;

import com.Meli4.lumos.common.event.BlueGeodeSet;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MutableBoundingBox;
import org.apache.logging.log4j.LogManager;

import java.util.List;

public class BlockLightSourceTile extends TileEntity implements ITickableTileEntity {
    public BlockLightSourceTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public BlockLightSourceTile() {
        this(TileEntityInit.BLOCKLIGHTSOURCE.get());
    }


    @Override
    public void tick() {
        boolean remove=false;
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        int dist = 6;
        List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, AxisAlignedBB.toImmutable(MutableBoundingBox.createProper(x - dist, y - dist, z - dist, x + dist, y + dist, z + dist)));
        if (players.isEmpty())
        {
            world.removeBlock(pos, false);
        }
        else {
            boolean has = false;
            for (PlayerEntity player : players){
                if (BlueGeodeSet.INSTANCE.hasArmor(player)){
                    has = true;
                    break;
                }
            }


            if (!has)
            {
                world.removeBlock(pos, false);
            }
        }

    }
}
