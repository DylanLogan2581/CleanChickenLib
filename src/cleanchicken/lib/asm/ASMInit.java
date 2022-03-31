package cleanchicken.lib.asm;

import net.minecraft.launchwrapper.Launch;

/**
 * Initialisation class for using this package. Call this on coremod load
 */
public class ASMInit
{
    private static boolean initialised = false;
    public static void init() {
        if(!initialised) {
            Launch.classLoader.addTransformerExclusion("cleanchicken.lib.asm");
            Launch.classLoader.addTransformerExclusion("cleanchicken.lib.config");
            initialised = true;
        }
    }
}
