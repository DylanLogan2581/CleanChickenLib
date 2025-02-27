package cleanchicken.lib.vec;

import cleanchicken.lib.util.Copyable;
import net.minecraft.util.AxisAlignedBB;

import java.util.Iterator;

public class CuboidCoord implements Iterable<BlockCoord>, Copyable<CuboidCoord>
{
    public BlockCoord min;
    public BlockCoord max;

    public CuboidCoord() {
        min = new BlockCoord();
        max = new BlockCoord();
    }

    public CuboidCoord(BlockCoord min, BlockCoord max) {
        this.min = min;
        this.max = max;
    }

    public CuboidCoord(BlockCoord coord) {
        this(coord, coord.copy());
    }

    public CuboidCoord(int[] ia) {
        this(ia[0], ia[1], ia[2], ia[3], ia[4], ia[5]);
    }

    public CuboidCoord(int x1, int y1, int z1, int x2, int y2, int z2) {
        this(new BlockCoord(x1, y1, z1), new BlockCoord(x2, y2, z2));
    }

    public CuboidCoord expand(int amount) {
        return expand(amount, amount, amount);
    }

    public CuboidCoord expand(int x, int y, int z) {
        max.add(x, y, z);
        min.sub(x, y, z);
        return this;
    }

    public CuboidCoord expand(int side, int amount) {
        if (side % 2 == 0)//negative side
            min = min.offset(side, amount);
        else
            max = max.offset(side, amount);
        return this;
    }

    public CuboidCoord offset(BlockCoord b) {
        min.add(b);
        max.add(b);
        return this;
    }

    public CuboidCoord offset(int x, int y, int z) {
        min.add(x, y, z);
        max.add(x, y, z);
        return this;
    }
    
    public int size(int s) {
        switch(s) {
            case 0:
            case 1:
                return max.y - min.y+1;
            case 2:
            case 3:
                return max.z - min.z+1;
            case 4:
            case 5:
                return max.x - min.x+1;
            default:
                return 0;
        }
    }

    public int getSide(int s) {
        switch(s) {
            case 0: return min.y;
            case 1: return max.y;
            case 2: return min.z;
            case 3: return max.z;
            case 4: return min.x;
            case 5: return max.x;
        }
        throw new IndexOutOfBoundsException("Switch Falloff");
    }

    public CuboidCoord setSide(int s, int v) {
        switch(s) {
            case 0: min.y = v; break;
            case 1: max.y = v; break;
            case 2: min.z = v; break;
            case 3: max.z = v; break;
            case 4: min.x = v; break;
            case 5: max.x = v; break;
            default: throw new IndexOutOfBoundsException("Switch Falloff");
        }
        return this;
    }

    public int getVolume() {
        return (max.x-min.x+1)*(max.y-min.y+1)*(max.z-min.z+1);
    }

    public Vector3 getCenterVec() {
        return new Vector3(min.x+(max.x-min.x+1)/2D, min.y+(max.y-min.y+1)/2D, min.z+(max.z-min.z+1)/2D);
    }

    public BlockCoord getCenter(BlockCoord store) {
        store.set(min.x+(max.x-min.x)/2, min.y+(max.y-min.y)/2, min.z+(max.z-min.z)/2);
        return store;
    }

    public boolean contains(BlockCoord coord) {
        return contains(coord.x, coord.y, coord.z);
    }

    public boolean contains(int x, int y, int z) {
        return x >= min.x && x <= max.x
                && y >= min.y && y <= max.y
                && z >= min.z && z <= max.z;
    }

    public int[] intArray() {
        return new int[]{min.x, min.y, min.z, max.x, max.y, max.z};
    }

    public CuboidCoord copy() {
        return new CuboidCoord(min.copy(), max.copy());
    }

    public Cuboid6 bounds() {
        return new Cuboid6(min.x, min.y, min.z, max.x + 1, max.y + 1, max.z + 1);
    }

    public AxisAlignedBB toAABB() {
        return bounds().toAABB();
    }

    public CuboidCoord set(CuboidCoord c) {
        return set(c.min, c.max);
    }

    public CuboidCoord set(BlockCoord min, BlockCoord max) {
        this.min.set(min);
        this.max.set(max);
        return this;
    }

    public CuboidCoord set(int x1, int y1, int z1, int x2, int y2, int z2) {
        min.set(x1, y1, z1);
        max.set(x2, y2, z2);
        return this;
    }

    public CuboidCoord set(BlockCoord coord) {
        return set(coord, coord);
    }

    public CuboidCoord set(int[] ia) {
        return set(ia[0], ia[1], ia[2], ia[3], ia[4], ia[5]);
    }

    public CuboidCoord include(BlockCoord coord) {
        return include(coord.x, coord.y, coord.z);
    }

    public CuboidCoord include(int x, int y, int z) {
        if(x < min.x) min.x = x;
        else if(x > max.x) max.x = x;
        if(y < min.y) min.y = y;
        else if(y > max.y) max.y = y;
        if(z < min.z) min.z = z;
        else if(z > max.z) max.z = z;
        return this;
    }

    public Iterator<BlockCoord> iterator() {
        return new Iterator<BlockCoord>() {
            BlockCoord b = null;

            public boolean hasNext() {
                return b == null || !b.equals(max);
            }

            public BlockCoord next() {
                if(b == null)
                    b = min.copy();
                else {
                    if(b.z != max.z)
                        b.z++;
                    else {
                        b.z = min.z;
                        if(b.y != max.y)
                            b.y++;
                        else {
                            b.y = min.y;
                            b.x++;
                        }
                    }
                }
                return b.copy();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
