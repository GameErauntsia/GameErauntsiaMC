
package io.github.galaipa;


import static io.github.galaipa.GameErauntsiaMC.statsAPI;
import java.text.DecimalFormat;
import nl.lolmewn.stats.api.stat.Stat;
import nl.lolmewn.stats.api.stat.StatEntry;
import nl.lolmewn.stats.api.user.StatsHolder;
import nl.lolmewn.stats.stat.DefaultStatEntry;
import nl.lolmewn.stats.stat.MetadataPair;
import org.bukkit.entity.Player;


public class GEAPI {
    private final GameErauntsiaMC plugin;

    public GEAPI(GameErauntsiaMC p) {
        this.plugin = p;
    }
    public void kargatuStat(String s){
        statsAPI.addStat(new PointStat(s));
    }
    public void gehituStat(String s , Integer in,Player p){
        Stat stat = statsAPI.getStatManager().getStat(s);
        StatsHolder holder = statsAPI.getPlayer(p.getUniqueId());
        holder.addEntry(stat, new DefaultStatEntry(1,new MetadataPair("world", p.getLocation().getWorld().getName())));
    }public static String infoStat(String s,Player p){
        Stat stat = statsAPI.getStatManager().getStat(s);
        for (StatEntry e : statsAPI.getPlayer(p.getUniqueId()).getStats(stat)){
            DecimalFormat df = new DecimalFormat("###");
            return df.format(e.getValue());
        }
        return "0";
    }public String checkok(){
        return "OK!";
    }
}
