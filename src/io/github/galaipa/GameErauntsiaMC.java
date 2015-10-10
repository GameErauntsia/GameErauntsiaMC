
package io.github.galaipa;

import static io.github.galaipa.Json.irakurriJSON;
import net.milkbowl.vault.permission.Permission;
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
        if(getConfig().getString("Spawn.Spawn.World") != null){ //Spawn puntuak kargatuauto uhv
            loadSpawn();
        }
        WhiteList.telegram = getConfig().getBoolean("Telegram");
        setupPermissions();
        System.out.println("GameErauntsiaMC piztu da!");
    }   
    @Override
    public void onDisable() {
        
    }   
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (cmd.getName().equalsIgnoreCase("wl")) {
            if((args.length < 1)){
                sender.sendMessage("WhitelistGE");
                Player  p = (Player) sender;
                //laguntza(p);
                return true;
            } else if(args[0].equalsIgnoreCase("zerrenda")){
                JSONArray jokalariak = Json.zerrendaJSON();
                
                String s = "";
                   for (Object jokalariak1 : jokalariak ) {
                      JSONObject o = (JSONObject) jokalariak1;      
                       s= s + ", "+ o.get("Erabiltzailea");
                      }
                sender.sendMessage(ChatColor.GREEN + "Jokalari zerrenda("+ jokalariak.size()+ "): "+ ChatColor.YELLOW+ s);
                    }
            else if(args[0].equalsIgnoreCase("info")){
                String jokalaria = args[1].toLowerCase();
                String msg = Json.irakurriJSON("Erabiltzailea",jokalaria).toString();
                sender.sendMessage(msg);
            return true;
            }
            else if(args[0].equalsIgnoreCase("ezabatu")){
                String jokalaria = args[1].toLowerCase();
                Json.ezabatuJSON(jokalaria);
                sender.sendMessage(ChatColor.RED + jokalaria + " jokalaria zerrendatik ezabatu duzu");
                return true;
            }
            else if(args[0].equalsIgnoreCase("gehitu")){
                return true;
            }
            else if(args[0].equalsIgnoreCase("point")){
                Player p = (Player) sender;
                SaveSpawn(p.getLocation(),args[1]);
                sender.sendMessage("Spawn puntua gorde duzu");
                return true;
            }else if(args[0].equalsIgnoreCase("telegram")){
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
    }        if (cmd.getName().equalsIgnoreCase("laguntza")){
            if(args.length < 1){
            sender.sendMessage(ChatColor.YELLOW + "/laguntza <zergatia> "+ ChatColor.GREEN + "erabiliz, zerbitzariko administratzailekin kontaktuan jar zaitezke");
            return true;
            }else{
               if(WebAPI.telegramBidali(Args(0,args),sender.getName())){
                   sender.sendMessage(ChatColor.YELLOW + "Laguntza eskaria ondo bidali duzu. Itxaron admin batek eskaera errebisatu arte");
                   return true;
               }
               else{
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
        if(irakurriJSON("Erabiltzailea",sender.getName().toLowerCase()).get("Pasahitza") != null){
            sender.sendMessage(ChatColor.RED + "Dagoeneko bazaude erregistratuta");
            return true;
        }
        else if(!irakurriJSON("Erabiltzailea",sender.getName().toLowerCase()).get("Uuid").toString().equalsIgnoreCase("null")){
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
                JSONObject s = irakurriJSON("Erabiltzailea",sender.getName().toLowerCase());
                Json.ezabatuJSON(s.get("Erabiltzailea").toString());
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
        if(!irakurriJSON("Erabiltzailea",sender.getName().toLowerCase()).get("Uuid").toString().equalsIgnoreCase("null")){
            sender.sendMessage(ChatColor.RED + "Zure kasuan ez da beharrezkoa");
            return true;
        }
        else if((args.length == 0)){
            sender.sendMessage(ChatColor.RED + "Erabilera egokia: /login Pasahitza");
            return true;
        }else{
            String pass = irakurriJSON("Erabiltzailea",sender.getName().toLowerCase()).get("Pasahitza").toString();
            if(pass !=null){
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
    }
        return false;
    }
        
   public  void loadSpawn(){
        String w = getConfig().getString("Spawn.Spawn.World" );
        Double x = getConfig().getDouble("Spawn.Spawn.X");
        Double y= getConfig().getDouble("Spawn.Spawn.Y");
        Double z = getConfig().getDouble("Spawn.Spawn.Z");
        WhiteList.spawn = new Location(Bukkit.getServer().getWorld(w), x, y, z);

        String w2 = getConfig().getString("Spawn.Errorea.World");
        Double x2 = getConfig().getDouble("Spawn.Errorea.X");
        Double y2 = getConfig().getDouble("Spawn.Errorea.Y");
        Double z2 = getConfig().getDouble("Spawn.Errorea.Z");
        WhiteList.errorea = new Location(Bukkit.getServer().getWorld(w2), x2, y2, z2);
   }
   public void SaveSpawn(Location l, String s){
        getConfig().set("Spawn."+ s + ".World", l.getWorld().getName());
        getConfig().set("Spawn."+ s + ".X", l.getX());
        getConfig().set("Spawn."+ s + ".Y", l.getY());
        getConfig().set("Spawn."+ s + ".Z", l.getZ());
        saveConfig();
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
}
