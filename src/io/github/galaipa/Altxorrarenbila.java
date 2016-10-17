package io.github.galaipa;
/// Durangoko Azokarako prestatutako altxorraren bila jolasa. Jokalariek 10 buru aurkitu beharko dituzte zerbitzari osoan zehar.
/// Martxan jartzeko:
// 1- getServer().getPluginManager().registerEvents(new Altxorrarenbila(this), this); eta Altxorrarenbila.hasi(); gehitu onEnablen
// 2- Komandoa Main-en jarri:
        /*else if(cmd.getName().equalsIgnoreCase("altxorra")){
                if((args.length < 1)){
                    Altxorrarenbila.openGui((Player) sender);
                    return true;
                }
                else if(args[0].equalsIgnoreCase("eman")){
                    Altxorrarenbila.eman((Player) sender,Integer.parseInt(args[1]));
                    return true;
                }else{
                    Altxorrarenbila.openGui((Player) sender);
                    return true; 
                }
            }*/
        /*
// 3- plugin.yml fitxategian komandoa gehitu:
     altxorra:
        description: Altxorraren bila
        usage: /altxorra
        aliases: []


import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;



public class Altxorrarenbila implements Listener{
    public GameErauntsiaMC plugin;
    public Altxorrarenbila(GameErauntsiaMC instance) {
            plugin = instance;
        }
    public static Map<Integer, String> map = new HashMap<>();
    public static void hasi(){
        buruak();
        GEAPI.kargatuStat("altxorra");
    }
    public static void buruak(){
        map.put(1,"Metroidling");
        map.put(2,"PC");
        map.put(3,"GameNilo");
        map.put(4,"Mrben130Dev");
        map.put(5,"Laserpanda");
        map.put(6,"EladYat");
        map.put(7,"VerizonHD720p");
        map.put(8,"Ferocious_Ben");
        map.put(9,"Cheapshot");
        map.put(10,"Hack");
    }
    public static void eman(Player p,Integer s){
        p.getInventory().addItem(burua(s));
    }
    public static ItemStack burua(Integer i){
        ItemStack skull = new ItemStack(Material.SKULL_ITEM,1);
        skull.setDurability((short)3);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(map.get(i));
        meta.setDisplayName(ChatColor.GREEN + "Altxorraren bila: "+ i);
        skull.setItemMeta(meta);
        return skull;
    }
    public int burua(String s){
        for (Map.Entry<Integer, String> entry : map.entrySet()){
            if(entry.getValue().equalsIgnoreCase(s)){
                return entry.getKey();
            }
    }
        return 0;
    }
    public void pista(Player p){
        int pista = (int) (GEAPI.infoStatInt("altxorra", p)+ 1);
        String mezua = "";
        String mezua2 = "";
        switch (pista) {
            case 0: //DURANGOKO AZOKA
                mezua = ChatColor.YELLOW + "Zerbitzari osoan zehar ordenagailuak aurkitu beharko dituzu";
                mezua2 = ChatColor.YELLOW + "Esate baterako Durangoko Azokako Kabia gunean aurkituko duzu lehenenengoa.";
                break;
            case 1: //IRUA: Plazan
                mezua = ChatColor.YELLOW + "Minik ez baduzu hartu nahi hobe duzu korrika egin, zezenak harrapatuko zaitu bestela";
                mezua2 = ChatColor.YELLOW + "Harresiz inguratutako hiria";
                break;
            case 2: //BILBO: Iberdrola dorrean
                mezua = ChatColor.YELLOW + "Etxea uharte batean izateko aukera ematen dizun hiria";
                mezua2 = ChatColor.YELLOW +"Ordenagailua dagoen lekutik hiri osoa ikus dezakezu. Hiriko eraikin altuenetik.";
                break;
            case 3: //DONOSTI: Anoetan
                mezua = ChatColor.YELLOW + "Eguzkia hartzeko hiri paregabea";
                mezua2 = ChatColor.YELLOW +"Ordenagailua Gipuzkuarrek gehien sufritzen duten tokian aurkituko duzu";
                break; 
            case 4: // DONIBANE: Eraikinean
                mezua = ChatColor.YELLOW + "Zerbitzarian jende gutxien bizi den hiria";
                mezua2 = ChatColor.YELLOW +"Estolda laberinto ederra dauka";
                break;
            case 5:// BAIONA: Gazteluan
                mezua = ChatColor.YELLOW + "Arrantzalea bazara, hiri hontan daukazu zure tokia";
                mezua2 = ChatColor.YELLOW +"Arrantzan asmatzen ez baduzu, tranpolin ederra duzu igerilekuan patxadaz egoteko";
                break;
            case 6: // BALIABIDEEN MUNDUA
                mezua = ChatColor.YELLOW + "Material bila bazabiltza hona jo beharko duzu";
                mezua2 = ChatColor.YELLOW + "Hori bai, kontuz mob-ekin!";
                break;
            case 7: // SPAWNEKO PARKOURRA
                mezua = ChatColor.YELLOW +  "Zerbitzariko leku guztiak lotzen dituen puntua";
                mezua2 = ChatColor.YELLOW + "Parkurrean ona bazara erraz asko aurkituko duzu";
                break;
            case 8: //NETHER
                mezua = ChatColor.YELLOW +  "Bero handia egiten du hemen";
                mezua2 = ChatColor.YELLOW +  "Kontuz hemengo txerriekin! Oso umore txarra dute eta!";
                break;
            case 9:
                mezua = ChatColor.YELLOW +  "Partzela gehigarri bat baduzu hona jo behar duzu";
                mezua2 = ChatColor.YELLOW +  "Eraikin ikusgarriak aurkituko dituzu bertan!";
                break;  
            case 10:
                mezua = ChatColor.YELLOW + "Urtean behin ospatzen den disko eta liburu azoka";
                 mezua2 = ChatColor.YELLOW +  "Game Erauntsiko kideak dauden lekuan aurkituko duzu azken ordenagailua!";
                break;
        }
        p.sendMessage(ChatColor.RED + "=====================================================");
        p.sendMessage(ChatColor.GREEN + "                          Altxorraren bila(" + pista + "/10)");
        p.sendMessage(ChatColor.GREEN + "1.pista: "+ ChatColor.YELLOW +  mezua);
        p.sendMessage(ChatColor.GREEN + "2.pista: " + ChatColor.YELLOW + mezua2);
        p.sendMessage("   ");
        p.sendMessage(ChatColor.RED + "=====================================================");
    }
    public static void openGui(Player p){
        Inventory altxorra = Bukkit.createInventory(null, 18, ChatColor.RED +"Altxorraren bila");
        altxorra.setItem(0,item(Material.STAINED_GLASS_PANE,9,1,ChatColor.GREEN + "Altxorraren bila"));
        altxorra.setItem(1,item(Material.STAINED_GLASS_PANE,4,1,ChatColor.GREEN + "Altxorraren bila"));
        altxorra.setItem(7,item(Material.STAINED_GLASS_PANE,6,1,ChatColor.GREEN + "Altxorraren bila"));
        altxorra.setItem(8,item(Material.STAINED_GLASS_PANE,2,1,ChatColor.GREEN + "Altxorraren bila"));
        altxorra.setItem(9,item(Material.STAINED_GLASS_PANE,1,1,ChatColor.GREEN + "Altxorraren bila"));
        altxorra.setItem(10,item(Material.STAINED_GLASS_PANE,5,1,ChatColor.GREEN + "Altxorraren bila"));
        altxorra.setItem(16,item(Material.STAINED_GLASS_PANE,7,1,ChatColor.GREEN + "Altxorraren bila"));
        altxorra.setItem(17,item(Material.SIGN,9,1,ChatColor.GREEN + "Hurrengo pista"));
        for(int i= 1;i <= 10;i++){
            if(i <= GEAPI.infoStatInt("altxorra", p)){
                altxorra.addItem(burua(i));
            }else{
                ItemStack a = new ItemStack(Material.BARRIER,1);
                ItemMeta metaB = a.getItemMeta();                          
                metaB.setDisplayName(ChatColor.RED +"Lortzeke: " + i);
                a.setItemMeta(metaB);
                altxorra.addItem(a);
            }
        }
        p.openInventory(altxorra);
    }
    public static ItemStack item(Material material, int id, int amount,String name){
            ItemStack b = new ItemStack(material, amount, (short) id);
            ItemMeta metaB = b.getItemMeta();                          
            metaB.setDisplayName(name);
            b.setItemMeta(metaB);
            return b;
    } 
@EventHandler  
public void onPlayerClick(PlayerInteractEvent event){
if (event.getAction() == Action.RIGHT_CLICK_BLOCK ||event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Player p = event.getPlayer();
            BlockState block = event.getClickedBlock().getState();
            if (block instanceof Skull) {
                Skull skull = (Skull) block;
                String owner = skull.getOwner();
                Double izan = GEAPI.infoStatInt("altxorra", p);
                Integer aurkitu = burua(owner);
                if(aurkitu == 10){
                    GEAPI.gehituStat2("altxorra", 1, p);
                    p.playSound(p.getLocation(),Sound.CAT_MEOW, 10, 1);
                    p.sendMessage(ChatColor.GREEN + "Zorionak!"+ ChatColor.RED + " azken burua aurkitu duzu!");
                }
                else if(izan+1 == aurkitu){
                    //Buru berria aurkitu
                    GEAPI.gehituStat2("altxorra", 1, p);
                    p.playSound(p.getLocation(),Sound.CAT_MEOW, 10, 1);
                    p.sendMessage(ChatColor.GREEN + "Zorionak!"+ ChatColor.RED + " buru berri bat aurkitu duzu!");
                    pista(p);
                }else if(izan >= aurkitu){
                    //Dagoeneko badauka
                    p.sendMessage(ChatColor.RED +"Dagoeneko badaukazu!");
                }else{
                    //Ordenean joan beharko da
                    p.sendMessage(ChatColor.RED +"Ordenean joan beharko zara!");
                }
            }
  }
 
}
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getName().equals(ChatColor.RED +"Altxorraren bila")) {
            event.setCancelled(true);
            if(event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null && event.getCurrentItem().getItemMeta().getDisplayName() != null){
                if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Hurrengo pista")){
                    pista((Player) event.getWhoClicked());
                    event.getWhoClicked().closeInventory();
                }
            }
        }
    }
}*/
