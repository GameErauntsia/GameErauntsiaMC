
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

    /**
     * Estatistika parametro bat kargatu edo sortu
     * @param s Kargatu nahi den estatistikaren izena
     */
    public static void kargatuStat(String s){
        statsAPI.addStat(new PointStat(s));
    }

    /**
     * Estatistika bat eguneratu
     * @param s Estatistika parametroaren izena
     * @param in Zenbat gehitu nahi zaion
     * @param p Jokalaria
     */
    public static void gehituStat(String s , Integer in,Player p){
        Stat stat = statsAPI.getStatManager().getStat(s);
        StatsHolder holder = statsAPI.getPlayer(p.getUniqueId());
        holder.addEntry(stat, new DefaultStatEntry(1,new MetadataPair("world", p.getLocation().getWorld().getName())));
    }
    /**
     * Estatistika bat eguneratu
     * @param s Estatistika parametroaren izena
     * @param in Zenbat gehitu nahi zaion
     * @param p Jokalaria
     */
    public static void gehituStat2(String s , Integer in,Player p){
        Stat stat = statsAPI.getStatManager().getStat(s);
        StatsHolder holder = statsAPI.getPlayer(p.getUniqueId());
        holder.addEntry(stat, new DefaultStatEntry(1,new MetadataPair("world", "Gamer_Erauntsia")));
    }

    /**
     * Jokalari batek estatistika batean duen balioa eskuratu
     * @param s Estatistika parametroaren izena
     * @param p Jokalaria
     * @return Balioa String moduan
     */
    public static String infoStat(String s,Player p){
        Stat stat = statsAPI.getStatManager().getStat(s);
        for (StatEntry e : statsAPI.getPlayer(p.getUniqueId()).getStats(stat)){
            DecimalFormat df = new DecimalFormat("###");
            return df.format(e.getValue());
        }
        return "";
    }
    /**
     * Jokalari batek estatistika batean duen balioa eskuratu integer moduan
     * @param s Estatistika parametroaren izena
     * @param p Jokalaria
     * @return Balioa Integer moduan
     */
    public static Double infoStatInt(String s,Player p){
        Stat stat = statsAPI.getStatManager().getStat(s);
        for (StatEntry e : statsAPI.getPlayer(p.getUniqueId()).getStats(stat)){
            return e.getValue();
        }
        return 0.0;
    }
    public String checkok(){
        return "OK!";
    }
}
