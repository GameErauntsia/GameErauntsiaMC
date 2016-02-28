
package io.github.galaipa;

import static io.github.galaipa.Json.irakurriJSON;
import static io.github.galaipa.WhiteList.sendTitle;
import static io.github.galaipa.WhiteList.spawn;
import net.milkbowl.vault.permission.Permission;
import nl.lolmewn.stats.api.StatsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class GameErauntsiaMC extends JavaPlugin {
    @Override
    public void onEnable() {
        Json.pluginFolder = getDataFolder().getAbsolutePath();
        getServer().getPluginManager().registerEvents(new WhiteList(this), this);
        getConfig().options().copyDefaults(true);
        saveConfig();
        WhiteList.telegram = getConfig().getBoolean("Telegram");
        WhiteList.errorea = new Location(getServer().getWorld("Jokoak"),970,4,96);
        WhiteList.spawn = getServer().getWorld("Jokoak").getSpawnLocation();
        setupPermissions();
        setupStatsAPI();
        Rss.SchedulerOn(this);
        System.out.println("GameErauntsiaMC piztu da!");;
    }   
    @Override
    public void onDisable() {
        
    }   
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (cmd.getName().equalsIgnoreCase("wl")) {
            if((args.length < 1)){
                sender.sendMessage(ChatColor.GREEN + "GameErauntsiaMC");
                Player  p = (Player) sender;
                //laguntza(p);
                return true;
            } else if(args[0].equalsIgnoreCase("zerrenda")){
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
                return true;
                    }
            else if(args[0].equalsIgnoreCase("info")){
                if(args.length < 2){
                    sender.sendMessage(ChatColor.RED + "Erabilera egokia: /wl info <JokalariarenIzena>");
                    return true;
                }
                String jokalaria = args[1].toLowerCase();
                JSONObject msg = Json.irakurriJSON("mc_user",jokalaria);
                if(msg != null){
                  //  sender.sendMessage(msg.toString());
                    sender.sendMessage(ChatColor.YELLOW + "Jokalaria: " + ChatColor.BLUE+ msg.get("mc_user"));
                    sender.sendMessage(ChatColor.YELLOW + "GE kontua: " + ChatColor.BLUE+ msg.get("user"));
                    sender.sendMessage(ChatColor.YELLOW + "Uuid: " + ChatColor.BLUE+ msg.get("uuid"));
                    sender.sendMessage(ChatColor.YELLOW + "Rol: " + ChatColor.BLUE+ msg.get("rol"));
                    sender.sendMessage(ChatColor.YELLOW + "Erregistratze data: " + ChatColor.BLUE+ msg.get("created"));
                    if(msg.get("Pasahitza") != null){
                        sender.sendMessage(ChatColor.YELLOW + "Jokalaria erregistratuta dago");
                    }
                }else{
                    sender.sendMessage(ChatColor.RED + "Jokalaria ez da aurkitu");
                }
            return true;
            }
            else if(args[0].equalsIgnoreCase("ezabatu")){
                String jokalaria = args[1].toLowerCase();
                Json.ezabatuJSON(jokalaria);
                sender.sendMessage(ChatColor.RED + jokalaria + " jokalaria zerrendatik ezabatu duzu");
                return true;
            }
            else if(args[0].equalsIgnoreCase("gehitu")){
                String jokalaria = args[1].toLowerCase();
                Player player = Bukkit.getPlayer(args[1]);
                if(player == null || !player.isOnline()){
                    sender.sendMessage(ChatColor.RED+ jokalaria + " ez dago online");
                    return true;
                }
                JSONObject jo = Json.irakurriJSON("mc_user","AmukelaEH");
                    jo.remove("mc_user");jo.put("mc_user", jokalaria);
                    jo.remove("uuid");jo.put("uuid",null);
                    jo.remove("created");jo.put("created", "Unknown");
                    jo.remove("user");jo.put("user", "Unknown");
                    jo.remove("rol");jo.put("rol", "Normal");
                    Json.idatziJSON(jo,jokalaria,false);
                sendTitle(player,2,2,2,"","");
                perms.playerAddGroup(null,player, "Herritarra");
                player.teleport(spawn);
                sender.sendMessage(ChatColor.GREEN + jokalaria + " jokalaria zerrendan sartu duzu ");
                return true;
            }
            else if(args[0].equalsIgnoreCase("telegram")){
                Boolean b = getConfig().getBoolean("Telegram");
                if(b){
                    getConfig().set("Telegram",false);
                    saveConfig();
                    WhiteList.telegram = false;
                    sender.sendMessage(ChatColor.RED + "Telegram jakinarazpenak ezgaitu dituzu");
                    return true;
                }else{
                    getConfig().set("Telegram",true);
                    saveConfig();
                    WhiteList.telegram = true;
                    sender.sendMessage(ChatColor.GREEN + "Telegram jakinarazpenak gaitu dituzu");
                    return true;
                }
            }
            else{
                    sender.sendMessage("WhitelistGE");
                return true;
            }
    }else  if (cmd.getName().equalsIgnoreCase("tutoriala")) {
        WhiteList.tutoriala((Player) sender);
        return true;
    }else if (cmd.getName().equalsIgnoreCase("laguntza")){
        if(args.length < 1){
            sender.sendMessage(ChatColor.YELLOW + "/laguntza <zergatia> "+ ChatColor.GREEN + "erabiliz, zerbitzariko administratzailekin kontaktuan jar zaitezke");
            return true;
        }else{
           if(WebAPI.telegramBidali(Args(0,args),sender.getName())){
               sender.sendMessage(ChatColor.YELLOW + "Laguntza eskaria ondo bidali duzu. Itxaron admin batek eskaera errebisatu arte");
               return true;
           }else{
               sender.sendMessage(ChatColor.RED + "Errorea gertatu da laguntza eskaria bidaltzerakoan");
               return true;
           }
        }
    }else if (cmd.getName().equalsIgnoreCase("arauak")){
        sender.sendMessage(ChatColor.BLUE +"" + ChatColor.BOLD + "=============================================");
        sender.sendMessage(ChatColor.BLUE +"" + ChatColor.BOLD + "             ZERBITZARIKO ARAUAK (Laburbildua)");
        sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Zerbitzariko arautegia 3 hitz hauekin laburbildu daiteke");
        sender.sendMessage(ChatColor.GREEN + "1." + ChatColor.BOLD + "- EUSKARA");
        sender.sendMessage(ChatColor.GREEN + "2." + ChatColor.BOLD + "- ERRESPETUA");
        sender.sendMessage(ChatColor.GREEN + "3." + ChatColor.BOLD + "- ERABILERA EGOKIA");
        sender.sendMessage(ChatColor.BOLD + "Arau guztiak ikusteko joan http://goo.gl/hrm1EO orrialdera");
        sender.sendMessage(ChatColor.BLUE +"" + ChatColor.BOLD + "=============================================");
        return true;
    }
    else if(cmd.getName().equalsIgnoreCase("register")){
        if(irakurriJSON("mc_user",sender.getName().toLowerCase()).get("Pasahitza") != null){
            sender.sendMessage(ChatColor.RED + "Dagoeneko bazaude erregistratuta");
            return true;
        }
        else if(irakurriJSON("mc_user",sender.getName().toLowerCase()).get("uuid") != null){
            sender.sendMessage(ChatColor.RED + "Zure kasuan ez da beharrezkoa");
            return true;
        }
        else if((args.length == 0)){
            sender.sendMessage(ChatColor.RED + "Erabilera egokia: /register Pasahitza ErrepikatuPasahitza");
            return true;
        }else if((args.length == 1)){
            sender.sendMessage(ChatColor.RED + "Pasahitza BI aldiz idatzi beharko duzu: /register Pasahitza ErrepikatuPasahitza");
            return true;
        }else{ 
            if(args[0].equalsIgnoreCase(args[1])){
                JSONObject s = irakurriJSON("mc_user",sender.getName().toLowerCase());
                Json.ezabatuJSON(s.get("mc_user").toString());
                s.put("Pasahitza", args[1]);
                Json.idatziJSON(s,sender.getName().toLowerCase(),false);
                sender.sendMessage(ChatColor.GREEN + "Mila esker erregistratzeagatik");
                WhiteList.rg.remove((Player) sender);
                return true;
            }else{
                sender.sendMessage(ChatColor.RED + "Pasahitzak ez datoz bat, saiatu berriro meseedez.");
                return true;
            }
        }
    }else if(cmd.getName().equalsIgnoreCase("login")){
        if(irakurriJSON("mc_user",sender.getName().toLowerCase()).get("uuid") != null){
            sender.sendMessage(ChatColor.RED + "Zure kasuan ez da beharrezkoa");
            return true;
        }
        else if((args.length == 0)){
            sender.sendMessage(ChatColor.RED + "Erabilera egokia: /login Pasahitza");
            return true;
        }else{
            String pass = irakurriJSON("mc_user",sender.getName().toLowerCase()).get("Pasahitza").toString();
            if(pass != null){
                if(args[0].equalsIgnoreCase(pass)){
                    sender.sendMessage(ChatColor.GREEN + "Pasahitz zuzena");
                    WhiteList.rg.remove((Player) sender);
                    return true;
                }else{
                    sender.sendMessage(" ");
                    sender.sendMessage(" ");
                    sender.sendMessage(" ");
                    sender.sendMessage(ChatColor.GREEN + "===================================");
                    sender.sendMessage(ChatColor.RED + "Pasahitz okerra, saiatu berriro");
                    sender.sendMessage(ChatColor.AQUA + "Admin baten laguntza behar baduzu erabili /laguntza <zergatia>");
                    sender.sendMessage(ChatColor.GREEN + "===================================");
                    sender.sendMessage(" ");
                    sender.sendMessage(" ");
                    return true;
                }
            }else{
                sender.sendMessage(ChatColor.RED + "Ez zaude erregistratuta");
                return true;
            }
        }
    }else  if (cmd.getName().equalsIgnoreCase("berriak")) {
        Rss.broadcast();
        return true;
    }
        return false;
    }
    public static String Args(int nondik, String[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = nondik; i < args.length; i++){
        sb.append(args[i]).append(" ");
        }String allArgs = sb.toString().trim();
        return allArgs;
     }
    public static Permission perms = null;
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
    public static StatsAPI statsAPI;
    private boolean setupStatsAPI(){
        RegisteredServiceProvider<StatsAPI> stats = getServer().getServicesManager().getRegistration(nl.lolmewn.stats.api.StatsAPI.class);
        if (stats!= null) {
            statsAPI = stats.getProvider();
            api = new GEAPI(this);
        }
        return (statsAPI != null);
    }
    private GEAPI api;
    public GEAPI getAPI() {
        return api;
    }
}
