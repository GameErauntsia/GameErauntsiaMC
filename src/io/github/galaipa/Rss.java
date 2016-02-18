package io.github.galaipa;

import io.github.galaipa.RssParser.Item;
import io.github.galaipa.RssParser.RssFeed;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class Rss {
    public static GameErauntsiaMC plugin;
    public Rss(GameErauntsiaMC instance) {
            plugin = instance;
        }
    
    public static ArrayList<String> titlesGame;
    public static ArrayList<String> titlesBlog;
    
    
    private static void broadcast(){
            for(Player p : Bukkit.getServer().getOnlinePlayers()){
                if(!p.hasPermission("rss.broadcast")){
                    return;
                }
            p.sendMessage(ChatColor.BLUE + "Game Erauntsia: azken gameplay eta berriak");
                    for(String s : titlesBlog){
                      p.sendMessage(ChatColor.GREEN + "[Blog-a] " + ChatColor.GOLD + s);      
                    }
                    for(String s : titlesGame){
                      p.sendMessage(ChatColor.GREEN + "[GamePlay] " + ChatColor.GOLD + s);      
                    }
                    p.sendMessage(ChatColor.BLUE + "Ikusteko: gamerauntsia.eus");  
            }

    }
    public static void updateGame(){
	titlesGame = new ArrayList<>();
	RssParser rp = new RssParser("http://gamerauntsia.eus/rss/gameplayak");
	rp.parse();
	RssFeed feed = rp.getFeed();
	ArrayList<Item> items = feed.getItems();
        for(int i=0; i<2;i++){ // Izenburua bakarrik nahi dugu
           titlesGame.add(items.get(i).title);
        }
    }
    public static void updateBlog(){
	RssParser rp = new RssParser("http://gamerauntsia.eus/rss/bloga");
	rp.parse();
	RssFeed feed = rp.getFeed();
	ArrayList<Item> items = feed.getItems();
        titlesBlog = new ArrayList<>();
        for(int i=0; i<2;i++){
           titlesBlog.add(items.get(i).title);
        }
    }
    public static void SchedulerOn(){      
        int BroadcastTime = 30*1200;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                updateBlog();
                updateGame();
                broadcast();
            }
        }, 200, BroadcastTime);

}
}
