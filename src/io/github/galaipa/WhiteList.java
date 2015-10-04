package io.github.galaipa;

import static io.github.galaipa.Json.irakurriJSON;
import java.util.ArrayList;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;




public class WhiteList implements Listener {
    static Location spawn;
    static Location errorea;
    static Boolean telegram;
    public static ArrayList<Player> rg = new ArrayList();
    public GameErauntsiaMC plugin;
    public WhiteList(GameErauntsiaMC instance) {
            plugin = instance;
        }
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerJoin(event.getPlayer().getName()); // Jokalaria sartzen denean exekutatu
    }
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerMove(PlayerMoveEvent event) {
        if(rg.contains(event.getPlayer())){
            if (!event.getFrom().getBlock().equals(event.getTo().getBlock())){
                event.setTo(event.getFrom());
                if(irakurriJSON("Erabiltzailea",event.getPlayer().getName().toLowerCase()).get("Pasahitza") != null){
                        sendTitle(event.getPlayer(),20,20,20,"",ChatColor.GREEN + "Sartzeko erabili /login pasahitza ");
                        event.getPlayer().sendMessage(ChatColor.GREEN + "Sartzeko erabili /login pasahitza ");
                }else{
                    sendTitle(event.getPlayer(),20,20,20,"",ChatColor.GREEN + "Erregistratzeko erabili /register pasahitza errepikatuPasahitza");
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Erregistratzeko erabili /register pasahitza errepikatuPasahitza");
                }
            }
            
        }
    }
    public  void PlayerJoin(String player){
        player = player.toLowerCase();
        if(zerrendan(player)){ //Zerrendan dago
            System.out.println(player + " erabiltzailea zerrenda txurian dago");
           // registerMezua(Bukkit.getServer().getPlayer(player));
            if(irakurriJSON("Erabiltzailea",player.toLowerCase()).get("Uuid").toString().equalsIgnoreCase("null")){
                rg.add(Bukkit.getServer().getPlayer(player));
            }
            return;
        }else if(WebAPI.web(player, "mc_user") != null){ //Ez dago zerrendan baina bai erregistratuta
            //Jokalaria zerrendan gorde
                JSONObject s = irakurriJSON("GameErauntsia",WebAPI.web(player, "user"));
                if(s!=null){ //Erabitzaileak izen aldaketa bat egin du
                    System.out.println(player + " erabiltzaileak izen aldaketa egin du. Izen zaharra: " + s.get("Erabiltzailea").toString());
                    Json.ezabatuJSON(s.get("Erabiltzailea").toString());
                    s.remove("Erabiltzailea");
                    s.put("Erabiltzailea", player.toLowerCase());
                }else{
               // JSONArray Jokalariak = new JSONArray();
                s = new JSONObject();
                s.put("Erabiltzailea", player.toLowerCase());
                s.put("Uuid", WebAPI.web(player, "uuid"));
                s.put("GameErauntsia", WebAPI.web(player, "user"));
                System.out.println("Erabiltzaile berri bat gehitu da zerrendara: " + player);
                //Jokalariak.add(s);
                }
                Json.idatziJSON(s,player,false);
                //registerMezua(Bukkit.getServer().getPlayer(player));
            // Pantaitik mezua kendu
                sendTitle(Bukkit.getServer().getPlayer(player),2,2,2,"","");
            //Baimenak eman
                 PermissionUser user = PermissionsEx.getUser(player);
                 user.addGroup(taldea(player));
            //Errore zerrendatik ezabatu

            // Jokalaria telegarraiatu
                Bukkit.getServer().getPlayer(player).teleport(spawn);
        }
        else{ // Erabiltzailea ez dago zerrendan eta ez dago erregistratuta
            System.out.println(player + " erabiltzailea ez dago webgunean erregistratuta edo errorea gertatu da");
            if(telegram){
                WebAPI.telegramBidali("Erregistratu gabeko jokalaria",player);
            }
            // Errore zerrendara gehitu
            //Laguntza mezuak erakutsi
            laguntza(Bukkit.getServer().getPlayer(player));
            // Jokalaria telegarraiatu
            Bukkit.getServer().getPlayer(player).teleport(errorea);
            System.out.println(player + " erabiltzailea ez dago webgunean erregistratuta edo errorea gertatu da");
        }
    }
    
    public Boolean erregistratuta(String player){ // Jokalaria erregistratuta al dago?
        if(WebAPI.web(player, "mc_user") != null){
            return true;
        }else{
            return false;
        }
    }
    public Boolean zerrendan(String player){ // Jokalaria zerrendan al dago?
        if(Json.irakurriJSON("Erabiltzailea",player) != null){
            return true;
        }
        return false;
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
