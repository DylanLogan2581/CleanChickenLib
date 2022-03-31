package cleanchicken.lib.inventory;

import cleanchicken.lib.packet.PacketCustom;

public interface IContainerSyncVar
{
    public boolean changed();
    
    public void reset();
    
    public void writeChange(PacketCustom packet);
    
    public void readChange(PacketCustom packet);
}
