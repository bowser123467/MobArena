import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

import com.garbagemule.MobArena.framework.Arena;
import com.garbagemule.MobArena.waves.MABoss;
import com.garbagemule.MobArena.waves.ability.*;

@AbilityInfo(
    name = "Obsidian Bomb",
    aliases = {"obsidianbomb"}
)
public class ObsidianBomb implements Ability
{
    /**
     * How many ticks before the bomb goes off.
     */
    private final int FUSE = 80;
    
    private final int MAX_DISTANCE = 10;//Max distance a bomb can be placed
    
    @Override
    public void execute(final Arena arena, MABoss boss) {
        // Grab the target, or a random player.
        LivingEntity target = AbilityUtils.getTarget(arena, boss.getEntity(), true);
        
        final World world = arena.getWorld();
        final Location loc = target.getLocation();
        if(loc.distance(boss.getEntity().getLocation()) > MAX_DISTANCE){ 
           /*
            Sometimes when a dead player is targeted the bomb is placed at spawn, exit lobby, eg. This will fix that.
           */
           return;
        }
        Block b = world.getBlockAt(loc); 
        b.setType(Material.OBSIDIAN);
        arena.addBlock(b);
        
        arena.scheduleTask(new Runnable() {
            public void run() {
                if (!arena.isRunning())
                    return;
                
                world.getBlockAt(loc).breakNaturally();
                world.createExplosion(loc, 3F);
            }
        }, FUSE);
    }
}
