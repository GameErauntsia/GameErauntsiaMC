
package io.github.galaipa;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public class GameErauntsiaMC extends JavaPlugin {
    @Override
    public void onEnable() {
        Json.pluginFolder = getDataFolder().getAbsolutePath();
        getServer().getPluginManager().registerEvents(new WhiteList(this), this);
        getConfig().options().copyDefaults(true);
        saveConfig();
        WhiteList.telegram = getConfig().getBoolean("Telegram");
        WhiteList.spawn = getServer().getWorld("Gamer_Erauntsia").getSpawnLocation();
        Rss.piztuta = getConfig().getBoolean("RSS");
        setupPermissions();
        WebAPI.httpsOn();
        Rss.SchedulerOn(this);
        System.out.println("GameErauntsiaMC piztu da!");;
    }   
    @Override
    public void onDisable() {
        System.out.println("GameErauntsiaMC itzali da!");
    }   
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        Player player = (Player) sender;
        switch(cmd.getName()){
            case "wl":
                WhiteList.wlKomandoa(player,args);
                return true;
            case "register":
            case "erregistratu":
                WhiteList.erregistratu(player,args);
                return true;
            case "login":
            case "sartu":
                WhiteList.login(player,args);
                return true;
            case "berriak":
                Rss.broadcast();
                return true;
            case "arauak":
                arauak(player);
                return true;
            case "tutoriala":
                WhiteList.tutoriala(player);
                return true;
            case "telegram":
                TelegramSwitch(player);
                return true;
            case "rss":
                RSSSwitch(player);
                return true;
            default:
                sender.sendMessage(ChatColor.GREEN +"Game Erauntsia Minecraft zerbitzaria");
                return true;
        }
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
    
    public void arauak(Player sender){
        sender.sendMessage(ChatColor.BLUE +"" + ChatColor.BOLD + "=============================================");
        sender.sendMessage(ChatColor.BLUE +"" + ChatColor.BOLD + "             ZERBITZARIKO ARAUAK (Laburbildua)");
        sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Zerbitzariko arautegia 3 hitz hauekin laburbildu daiteke");
        sender.sendMessage(ChatColor.GREEN + "1." + ChatColor.BOLD + "- EUSKARA");
        sender.sendMessage(ChatColor.GREEN + "2." + ChatColor.BOLD + "- ERRESPETUA");
        sender.sendMessage(ChatColor.GREEN + "3." + ChatColor.BOLD + "- ERABILERA EGOKIA");
        sender.sendMessage(ChatColor.BOLD + "Arau guztiak ikusteko joan http://goo.gl/hrm1EO orrialdera");
        sender.sendMessage(ChatColor.BLUE +"" + ChatColor.BOLD + "=============================================");
    }
    
    public void TelegramSwitch(Player sender){
        Boolean b = getConfig().getBoolean("Telegram");
        if(b){
            getConfig().set("Telegram",false);
            saveConfig();
            WhiteList.telegram = false;
            sender.sendMessage(ChatColor.RED + "Telegram jakinarazpenak ezgaitu dituzu");
        }else{
            getConfig().set("Telegram",true);
            saveConfig();
            WhiteList.telegram = true;
            sender.sendMessage(ChatColor.GREEN + "Telegram jakinarazpenak gaitu dituzu");
        }
    }
    public void RSSSwitch(Player sender){
        Boolean b = getConfig().getBoolean("RSS");
        if(b){
            getConfig().set("RSS",false);
            saveConfig();
            Rss.piztuta = false;
            sender.sendMessage(ChatColor.RED + "RSS jakinarazpenak ezgaitu dituzu");
        }else{
            getConfig().set("RSS",true);
            saveConfig();
            Rss.piztuta = true;
            sender.sendMessage(ChatColor.GREEN + "RSS jakinarazpenak gaitu dituzu");
        }
    }
    
    public void laguntza(Player sender, String[] args){
        if(args.length < 1){
            sender.sendMessage(ChatColor.YELLOW + "/laguntza <zergatia> "+ ChatColor.GREEN + "erabiliz, zerbitzariko administratzailekin kontaktuan jar zaitezke");
        }else{
           if(WebAPI.telegramBidali(Args(0,args),sender.getName())){
               sender.sendMessage(ChatColor.YELLOW + "Laguntza eskaria ondo bidali duzu. Itxaron admin batek eskaera errebisatu arte");
           }else{
               sender.sendMessage(ChatColor.RED + "Errorea gertatu da laguntza eskaria bidaltzerakoan");
           }
        }
    }
    


}
