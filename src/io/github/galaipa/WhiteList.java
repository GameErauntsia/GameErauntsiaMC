package io.github.galaipa;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class WhiteList implements Listener {
    static Location spawn;
    static Boolean telegram;
    public static ArrayList<Player> autentifikatuGabe = new ArrayList();
    public GameErauntsiaMC plugin;
    public WhiteList(GameErauntsiaMC instance) {
            plugin = instance;
        }
    @EventHandler//(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String izena = player.getName().toLowerCase();
        if(zerrendanDago(izena)){ // ZERRENDAN BAI
            System.out.println(izena + " erabiltzailea zerrenda txurian dago");
          //  if(pirataDa(izena)) autentifikatuGabe.add(player);
        }
        else{ 
            JSONObject webInfo = WebAPI.web(izena); // GE webgunearekin konexioa
            if(webInfo != null){
                if(berriaDa(webInfo.get("user").toString())){
                    System.out.println("Erabiltzaile berri bat gehitu da zerrendara: " + izena);
                    Json.idatzi(webInfo,izena,false);
                }else{
                    System.out.println("Erabiltzaileak izen aldaketa egin du: " + izena);
                    izenAldaketa(webInfo.get("user").toString(),izena,webInfo.get("uuid").toString());
                }
               // perms.playerAddGroup(null,player, taldea(webInfo));
                player.teleport(spawn);
            }
            else{
                System.out.println(player + " erabiltzailea ez dago webgunean erregistratuta edo errorea gertatu da");
                if(telegram) WebAPI.telegramBidali("Erregistratu gabeko jokalaria",izena);
            }
        }
    }
   /* @EventHandler(priority = EventPriority.LOW)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if(autentifikatuGabe.contains(p)){
            if (!event.getFrom().getBlock().equals(event.getTo().getBlock())){
                event.setTo(event.getFrom());
                if(babestutaDago(p)){  
                    //    sendTitle(event.getPlayer(),20,40,20,"",ChatColor.GREEN + "Sartzeko erabili /login pasahitza ");
                    p.sendMessage(" ");
                    p.sendMessage(" ");
                    p.sendMessage(" ");
                    p.sendMessage(ChatColor.GREEN + "==============================================");
                    p.sendMessage(ChatColor.YELLOW + "Sartzeko erabili /login pasahitza ");
                    p.sendMessage(ChatColor.AQUA + "Admin baten laguntza behar baduzu erabili /laguntza <zergatia>");
                    p.sendMessage(ChatColor.GREEN + "==============================================");
                    p.sendMessage(" ");
                    p.sendMessage(" ");
                }else{
                 //   sendTitle(event.getPlayer(),20,40,20,"",ChatColor.GREEN + "Erregistratzeko erabili /register pasahitza errepikatuPasahitza");
                    p.sendMessage(" ");
                    p.sendMessage(" ");
                    p.sendMessage(" ");
                    p.sendMessage(ChatColor.GREEN + "===================================");
                    p.sendMessage(ChatColor.GREEN + "Erregistratzeko erabili /register pasahitza errepikatuPasahitza");
                    p.sendMessage(ChatColor.AQUA + "Admin baten laguntza behar baduzu erabili /laguntza <zergatia>");
                    p.sendMessage(ChatColor.GREEN + "===================================");
                    p.sendMessage(" ");
                    p.sendMessage(" ");
                }
            }
            
        }
    }
    @EventHandler(priority = EventPriority.LOW)
    public void blockInteract(PlayerInteractEvent event) {
        if(autentifikatuGabe.contains(event.getPlayer())){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player && autentifikatuGabe.contains((Player)event.getEntity())) {
            event.setCancelled(true);
        }
    }
    @EventHandler
     public void PlayerCommand(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();
        if(autentifikatuGabe.contains(event.getPlayer())){
          if(!event.getMessage().toLowerCase().startsWith("/register") ||
             !event.getMessage().toLowerCase().startsWith("/login") ||
             !event.getMessage().toLowerCase().startsWith("/laguntza"))
          {
                  event.setCancelled(true);
          }
        }
     }
              
    @EventHandler
     public void PlayerLeave(PlayerQuitEvent event) {
        Iterator<Player> iterator = autentifikatuGabe.iterator();
        while(iterator.hasNext()){
            Player value = iterator.next();
            if (event.getPlayer().equals(value)){
                iterator.remove();
                break;
            }
}
     }*/

    public void izenAldaketa(String erabiltzailea,String izenBerria, String uuidBerria){
        JSONObject jokalaria = Json.bilatu("user",erabiltzailea); 
        Json.ezabatu(jokalaria.get("user").toString());
        jokalaria.remove("mc_user");
        jokalaria.remove("uuid");
        jokalaria.put("mc_user", izenBerria);
        jokalaria.put("uuid", uuidBerria);
        Json.idatzi(jokalaria,izenBerria,false); 
    }
    public boolean zerrendanDago(String izena){
        return (Json.bilatu("mc_user",izena) != null);
    }
    public static boolean pirataDa(String izena){
        return (Json.bilatu("mc_user",izena).get("uuid") == null);
    }
    public boolean berriaDa(String erabiltzailea){
        return (Json.bilatu("user",erabiltzailea) == null);
    }
    public String taldea(JSONObject t){ // Zer taldetan dago jokalaria?
            String rol = t.get("rol").toString();
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
    public static boolean babestutaDago(Player player){
        return (Json.bilatu("mc_user",player.getName().toLowerCase()).get("Pasahitza") != null);
    }
    public static void pasahitzaGehitu(String izena,String pasahitza){
        JSONObject jokalaria = Json.bilatu("mc_user",izena.toLowerCase());
        Json.ezabatu(izena.toLowerCase());
        jokalaria.put("Pasahitza", pasahitza);
        Json.idatzi(jokalaria,izena.toLowerCase(),false);
    }
    public static String pasahitza(String izena){
        return Json.bilatu("mc_user",izena).get("Pasahitza").toString();
    }
   
   public static void erregistratu(Player sender,String[] args){
       String izena = sender.getName().toLowerCase();
       if(babestutaDago(sender)) sender.sendMessage(ChatColor.RED + "Dagoeneko bazaude erregistratuta");
       else if(!pirataDa(izena)) sender.sendMessage(ChatColor.RED + "Zure kasuan ez da beharrezkoa");
       else if((args.length < 2)) sender.sendMessage(ChatColor.RED + "Erabilera egokia: /register Pasahitza ErrepikatuPasahitza");
       else{
           if(args[0].equalsIgnoreCase(args[1])){
               pasahitzaGehitu(sender.getName(),args[1]);
               sender.sendMessage(ChatColor.GREEN + "Mila esker erregistratzeagatik");
               autentifikatuGabe.remove(sender);
           }else{
               sender.sendMessage(ChatColor.RED + "Pasahitzak ez datoz bat, saiatu berriro meseedez.");
           }
       }
   }
   
   public static void login(Player sender, String[] args){
       String izena = sender.getName().toLowerCase();
       if(!pirataDa(izena)) sender.sendMessage(ChatColor.RED + "Zure kasuan ez da beharrezkoa");
       else if((args.length == 0))sender.sendMessage(ChatColor.RED + "Erabilera egokia: /login Pasahitza");
       else if(!babestutaDago(sender)) sender.sendMessage(ChatColor.RED + "Ez zaude erregistratuta. Erabili /register Pasahitza errepikatuPasahitza");
       else{
           if(args[0].equalsIgnoreCase(pasahitza(izena))){
               sender.sendMessage(ChatColor.GREEN + "Pasahitz zuzena");
               autentifikatuGabe.remove(sender);
           }
           else{
                sender.sendMessage(" ");
                sender.sendMessage(" ");
                sender.sendMessage(" ");
                sender.sendMessage(ChatColor.GREEN + "===================================");
                sender.sendMessage(ChatColor.RED + "Pasahitz okerra, saiatu berriro");
                sender.sendMessage(ChatColor.AQUA + "Admin baten laguntza behar baduzu erabili /laguntza <zergatia>");
                sender.sendMessage(ChatColor.GREEN + "===================================");
                sender.sendMessage(" ");
                sender.sendMessage(" ");
           }
       }

   }
   
   public static void bistaratuJokalaria(Player sender, String[] args){
        if(args.length < 2) sender.sendMessage(ChatColor.RED + "Erabilera egokia: /wl info <JokalariarenIzena>");
        else{
            String izena = args[1].toLowerCase();
            JSONObject jokalaria = Json.bilatu("mc_user",izena);
            if(jokalaria != null){
                sender.sendMessage(ChatColor.YELLOW + "Jokalaria: " + ChatColor.BLUE+ jokalaria.get("mc_user"));
                sender.sendMessage(ChatColor.YELLOW + "GE kontua: " + ChatColor.BLUE+ jokalaria.get("user"));
                sender.sendMessage(ChatColor.YELLOW + "Uuid: " + ChatColor.BLUE+ jokalaria.get("uuid"));
                sender.sendMessage(ChatColor.YELLOW + "Rol: " + ChatColor.BLUE+ jokalaria.get("rol"));
                sender.sendMessage(ChatColor.YELLOW + "Erregistratze data: " + ChatColor.BLUE+ jokalaria.get("created"));
                if(jokalaria.get("Pasahitza") != null){
                    sender.sendMessage(ChatColor.YELLOW + "Jokalaria erregistratuta dago");
                }
            }else{
                sender.sendMessage(ChatColor.RED + "Jokalaria ez da aurkitu");
            }
        }
   }
   public static void bistaratuDenak(Player sender){
        JSONArray jokalariak = Json.zerrendaJSON();
        String s = "";
        for (Object jokalariak1 : jokalariak ) {
          JSONObject o = (JSONObject) jokalariak1;   
          if(o.get("uuid") != null){
           s= s + ", "+ ChatColor.GREEN + o.get("mc_user");
          }else{
           s= s + ", "+ ChatColor.RED + o.get("mc_user");
          }
        }
        sender.sendMessage(ChatColor.AQUA + "Jokalari zerrenda("+ jokalariak.size()+ "): "+  s);
   }
   public static void ezabatuJokalaria(Player sender, String[] args){
       if(args.length < 2) sender.sendMessage(ChatColor.RED + "Erabilera egokia: /wl ezabatu <JokalariarenIzena>");
       else{
            String jokalaria = args[1].toLowerCase();
            Json.ezabatu(jokalaria);
            sender.sendMessage(ChatColor.RED + jokalaria + " jokalaria zerrendatik ezabatu duzu");
       }
   }
   public static void gehituJokalaria(Player sender, String[] args){
        String jokalaria = args[1].toLowerCase();
       /* Player player = Bukkit.getPlayer(args[1]);
        if(player == null || !player.isOnline()){
            sender.sendMessage(ChatColor.RED+ jokalaria + " ez dago online");
            return true;
        }*/
        JSONObject jo = Json.bilatu("mc_user","galaipa");
        jo.remove("mc_user");jo.put("mc_user", jokalaria);
        jo.remove("uuid");jo.put("uuid",null);
        jo.remove("created");jo.put("created", "Unknown");
        jo.remove("user");jo.put("user", "Unknown");
        jo.remove("rol");jo.put("rol", "Normal");
        Json.idatzi(jo,jokalaria,false);
        sender.sendMessage(ChatColor.GREEN + jokalaria + " jokalaria zerrendan sartu duzu ");
   }
   
    public static void tutoriala(Player p){
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
   } 
    
    public static void wlKomandoa(Player sender, String[] args){
        if((args.length < 1)) sender.sendMessage("Game Erauntsia MC");
        else{
            switch(args[0]){
                case "gehitu":
                    WhiteList.gehituJokalaria((Player)sender, args);
                    break;
                case "ezabatu":
                    WhiteList.ezabatuJokalaria((Player)sender, args);
                    break;
                case "zerrenda":
                    WhiteList.bistaratuDenak((Player)sender);
                    break;
                case "info":
                    WhiteList.bistaratuJokalaria((Player)sender, args);
                    break;
                default:
                    sender.sendMessage("Game Erauntsia MC");
                    break;

            }
        }
}
}
