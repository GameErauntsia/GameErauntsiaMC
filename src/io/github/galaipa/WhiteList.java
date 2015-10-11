package io.github.galaipa;

import static io.github.galaipa.GameErauntsiaMC.perms;
import static io.github.galaipa.Json.irakurriJSON;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;


public class WhiteList implements Listener {
    static Location spawn;
    static Location errorea;
    static Boolean telegram;
    public static ArrayList<Player> rg = new ArrayList();
    public GameErauntsiaMC plugin;
    public WhiteList(GameErauntsiaMC instance) {
            plugin = instance;
        }
    @EventHandler//(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerJoin(event.getPlayer()); // Jokalaria sartzen denean exekutatu
    }
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerMove(PlayerMoveEvent event) {
        if(rg.contains(event.getPlayer())){
            if (!event.getFrom().getBlock().equals(event.getTo().getBlock())){
                event.setTo(event.getFrom());
                if(irakurriJSON("Erabiltzailea",event.getPlayer().getName().toLowerCase()).get("Pasahitza") != null){
                        sendTitle(event.getPlayer(),20,40,20,"",ChatColor.GREEN + "Sartzeko erabili /login pasahitza ");
                        event.getPlayer().sendMessage(" ");
                        event.getPlayer().sendMessage(" ");
                        event.getPlayer().sendMessage(" ");
                        event.getPlayer().sendMessage(ChatColor.GREEN + "==============================================");
                        event.getPlayer().sendMessage(ChatColor.YELLOW + "Sartzeko erabili /login pasahitza ");
                        event.getPlayer().sendMessage(ChatColor.AQUA + "Admin baten laguntza behar baduzu erabili /laguntza <zergatia>");
                        event.getPlayer().sendMessage(ChatColor.GREEN + "==============================================");
                        event.getPlayer().sendMessage(" ");
                        event.getPlayer().sendMessage(" ");
                }else{
                    sendTitle(event.getPlayer(),20,40,20,"",ChatColor.GREEN + "Erregistratzeko erabili /register pasahitza errepikatuPasahitza");
                    event.getPlayer().sendMessage(" ");
                    event.getPlayer().sendMessage(" ");
                    event.getPlayer().sendMessage(" ");
                    event.getPlayer().sendMessage(ChatColor.GREEN + "===================================");
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Erregistratzeko erabili /register pasahitza errepikatuPasahitza");
                    event.getPlayer().sendMessage(ChatColor.AQUA + "Admin baten laguntza behar baduzu erabili /laguntza <zergatia>");
                    event.getPlayer().sendMessage(ChatColor.GREEN + "===================================");
                    event.getPlayer().sendMessage(" ");
                    event.getPlayer().sendMessage(" ");
                }
            }
            
        }
    }
    @EventHandler
     public void PlayerCommand(PlayerCommandPreprocessEvent event) {
          Player p = event.getPlayer();
          if(rg.contains(event.getPlayer())){
              if(!event.getMessage().toLowerCase().startsWith("/register")){
                 if(!event.getMessage().toLowerCase().startsWith("/login")){
                     if(!event.getMessage().toLowerCase().startsWith("/laguntza")){
                      event.getPlayer().sendMessage(ChatColor.RED + "Ezin duzu hori egin!");
                      event.setCancelled(true);
          }
              }}}}
    @EventHandler
     public void PlayerLeave(PlayerQuitEvent event) {
        Iterator<Player> iterator = rg.iterator();
        while(iterator.hasNext()){
            Player value = iterator.next();
            if (event.getPlayer().equals(value)){
                iterator.remove();
                //rg.remove(event.getPlayer());
                break;
            }
}
     }
    public void PlayerJoin(Player player){
        String izena = player.getName().toLowerCase();
        if(Json.irakurriJSON("Erabiltzailea",izena) != null){ //Zerrendan al dago ?
            System.out.println(player + " erabiltzailea zerrenda txurian dago");
           // registerMezua(Bukkit.getServer().getPlayer(player));
            if(irakurriJSON("Erabiltzailea",izena).get("Uuid").toString().equalsIgnoreCase("null")){
                rg.add(player);
            }
            return;
        }else if(WebAPI.web(izena, "mc_user") != null){ //Ez dago zerrendan baina bai erregistratuta
            //Jokalaria zerrendan gorde
                JSONObject s = irakurriJSON("GameErauntsia",WebAPI.web(izena, "user"));
                if(s!=null){ //Erabitzaileak izen aldaketa bat egin du
                    System.out.println(player + " erabiltzaileak izen aldaketa egin du. Izen zaharra: " + s.get("Erabiltzailea").toString());
                    Json.ezabatuJSON(s.get("Erabiltzailea").toString());
                    s.remove("Erabiltzailea");
                    s.put("Erabiltzailea", izena);
                }else{
               // JSONArray Jokalariak = new JSONArray();
                s = new JSONObject();
                s.put("Erabiltzailea", izena);
                s.put("Uuid", WebAPI.web(izena, "uuid"));
                s.put("GameErauntsia", WebAPI.web(izena, "user"));
                System.out.println("Erabiltzaile berri bat gehitu da zerrendara: " + player);
                //Jokalariak.add(s);
                }
                Json.idatziJSON(s,izena,false);
                //registerMezua(Bukkit.getServer().getPlayer(player));
            // Pantaitik mezua kendu
                sendTitle(player,2,2,2,"","");
            //Baimenak eman
                perms.playerAddGroup(null,player, taldea(izena));

              /*   PermissionUser user = PermissionsEx.getUser(player);
                 user.addGroup(taldea(izena));*/
            // Jokalaria telegarraiatu
                player.teleport(spawn);
        }else{ // Erabiltzailea ez dago zerrendan eta ez dago erregistratuta
            System.out.println(player + " erabiltzailea ez dago webgunean erregistratuta edo errorea gertatu da");
            if(telegram){
                WebAPI.telegramBidali("Erregistratu gabeko jokalaria",izena);
            }
            // Errore zerrendara gehitu
            //Laguntza mezuak erakutsi
            laguntza(player);
            // Jokalaria telegarraiatu
            player.teleport(errorea);
            System.out.println(player + " erabiltzailea ez dago webgunean erregistratuta edo errorea gertatu da");
        }
    }
    
public String taldea(String player){ // Zer taldetan dago jokalaria?
        String rol = WebAPI.web(player,"rol");
        String taldea = null;
        switch (rol) {
            case "Normal": taldea = "Herritarra";
                    break;
            case "VIP": taldea = "Vip";
                    break;
            case "Administrator": taldea = "Admin";
                    break;
        }
        return taldea;
    }


   public void laguntza(final Player p){
       new BukkitRunnable(){
           int i = 0;
           @Override
           public void run(){
               if(p.isOnline()){
               p.sendMessage(ChatColor.GREEN + "");
               p.sendMessage(ChatColor.GREEN + "");
               p.sendMessage(ChatColor.GREEN + "");
               p.sendMessage(ChatColor.GREEN + "");
               p.sendMessage(ChatColor.GREEN + "");
               p.sendMessage(ChatColor.GREEN + "");
               p.sendMessage(ChatColor.GREEN + "");
               p.sendMessage(ChatColor.GREEN + "");
               sendTitle(p,2,10000,2,"",ChatColor.RED + "Jolasteko jarraitu txateko pausoak");
               p.sendMessage(ChatColor.GREEN + "====================================================");
               p.sendMessage(ChatColor.AQUA + "             ZERBITZARIAN JOLASTEKO");
               p.sendMessage(ChatColor.YELLOW + "Bi aukera dituzu:");
               p.sendMessage(ChatColor.YELLOW + "1- /tutoriala komandoa idatzi, eta agertuko zaizkizun pausoak jarraitu");
               p.sendMessage(ChatColor.YELLOW + "2- Youtubeko tutorial hau ikusi: Oraindik ez dago erabilgarri D: ");
               p.sendMessage(ChatColor.GREEN + "");
               p.sendMessage(ChatColor.AQUA + "Admin baten laguntza behar izanez gero idatzi /laguntza <zergatia>");
               p.sendMessage(ChatColor.GREEN + "====================================================");
               i++;
               if(i == 3){
                   cancel();
               }
               }else{
                  cancel();
               }
           }
       }.runTaskTimer(plugin, 20,20);
   }public static void tutoriala(Player p){
       p.sendMessage(ChatColor.GREEN + "");
       p.sendMessage(ChatColor.GREEN + "");
       p.sendMessage(ChatColor.GREEN + "");
       p.sendMessage(ChatColor.GREEN + "");
       p.sendMessage(ChatColor.GREEN + "");
       p.sendMessage(ChatColor.GREEN + "");
       p.sendMessage(ChatColor.GREEN + "");
       p.sendMessage(ChatColor.GREEN + "");
       p.sendMessage(ChatColor.GREEN + "====================================================");
       p.sendMessage(ChatColor.AQUA + "          Zerbitzarian jolasteko jarraitu beharreko pausoak");
       p.sendMessage(ChatColor.YELLOW + "1- gamerauntsia.eus orrialdean erregistratu");
       p.sendMessage(ChatColor.YELLOW + "2- Kontua e-postan aktibatu "+ChatColor.RED +" (Ez ahaztu spam karpeta!)");
       p.sendMessage(ChatColor.YELLOW + "3- Profila editatu atalean, plataformetan MC kontua gehitu");
       p.sendMessage(ChatColor.YELLOW + "4- gamerauntsia.eus/zerbitzariak/minecraft orrialdean sartu, arauak irakurri, eta izena emateko botoia sakatu! ");
       p.sendMessage(ChatColor.GREEN + "");
       p.sendMessage(ChatColor.AQUA + "Admin baten laguntza behar izanez gero idatzi /laguntza <zergatia>");
       p.sendMessage(ChatColor.GREEN + "====================================================");
   } /*public void registerMezua(final Player player){
       new BukkitRunnable(){
           Player p = player;
           @Override
           public void run(){
            if(!player.isOnline()){
                cancel();
            }
            if(!NewAPI.getInstance().isAuthenticated(p)){
                if(!NewAPI.getInstance().isRegistered(p.getName())){
                    sendTitle(p,20,20,20,"",ChatColor.GREEN + "Kontua babesteko idatzi /register pasahitza pasahitza");
                }else{
                    sendTitle(p,20,20,20,"",ChatColor.GREEN + "Sartzeko erabili /login pasahitza ");
                }
            }else{
                cancel();
                return;
            }
           }
       }.runTaskTimer(this, 20,40);
            
   }*/
   
    public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

        PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut);
        connection.sendPacket(packetPlayOutTimes);

        if (subtitle != null) {
            subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
            subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
            IChatBaseComponent titleSub = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
            PacketPlayOutTitle packetPlayOutSubTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleSub);
            connection.sendPacket(packetPlayOutSubTitle);
        }

        if (title != null) {
            title = title.replaceAll("%player%", player.getDisplayName());
            title = ChatColor.translateAlternateColorCodes('&', title);
            IChatBaseComponent titleMain = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
            PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleMain);
            connection.sendPacket(packetPlayOutTitle);
        }
    } 
}
