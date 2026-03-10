package pl.twojserwer.guilditems;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class GuildItemsPlugin extends JavaPlugin implements Listener {

    private final HashMap<UUID, Map<String, Long>> cooldowns = new HashMap<>();
    private final NamespacedKey itemKey = new NamespacedKey(this, "guild_item_id");

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        registerAllItems();
        getLogger().info("Załadowano 20 przedmiotów gildyjnych!");
    }

    private void registerAllItems() {
        // --- MIECZE I BROŃ ---
        createRecipe("vampire_sword", Material.NETHERITE_SWORD, "§4§lOstrze Wampira", "RNR", "RSR", " R ", 'R', Material.REDSTONE_BLOCK, 'N', Material.NETHERITE_INGOT, 'S', Material.NETHERITE_SWORD);
        createRecipe("shield_breaker", Material.NETHERITE_AXE, "§6§lTopór Rozpruwacz", "NNN", "NAN", " G ", 'N', Material.NETHERITE_INGOT, 'A', Material.NETHERITE_AXE, 'G', Material.GOLD_BLOCK);
        createRecipe("frost_bow", Material.BOW, "§b§lŁuk Mroźnego Wichru", " IS", "I S", " IS", 'I', Material.ICE, 'S', Material.STRING);
        createRecipe("shadow_dagger", Material.NETHERITE_SWORD, "§8§lSztylet Cienia", " O ", " O ", " S ", 'O', Material.OBSIDIAN, 'S', Material.STICK);
        createRecipe("thor_hammer", Material.NETHERITE_AXE, "§e§lMłot Thora", "III", "ISI", " S ", 'I', Material.IRON_BLOCK, 'S', Material.BLAZE_ROD);

        // --- NARZĘDZIA ---
        createRecipe("void_pickaxe", Material.NETHERITE_PICKAXE, "§d§lKilof Próżni", "EEE", " P ", " P ", 'E', Material.END_CRYSTAL, 'P', Material.NETHERITE_PICKAXE);
        createRecipe("fortune_pick", Material.DIAMOND_PICKAXE, "§a§lKilof Geologa", "DDD", " S ", " S ", 'D', Material.DIAMOND_BLOCK, 'S', Material.STICK);
        createRecipe("hermes_boots", Material.NETHERITE_BOOTS, "§f§lButy Hermesa", "F F", "N N", 'F', Material.FEATHER, 'N', Material.NETHERITE_INGOT);

        // --- FLAGI I TOTEMY (BANNERY/ITEMY) ---
        createRecipe("strength_flag", Material.RED_BANNER, "§c§lSztandar Siły", "SSS", "S B", " P ", 'S', Material.BLAZE_POWDER, 'B', Material.RED_BANNER, 'P', Material.BLAZE_ROD);
        createRecipe("defense_flag", Material.BLUE_BANNER, "§9§lSztandar Obrony", "III", "I B", " P ", 'I', Material.IRON_BLOCK, 'B', Material.BLUE_BANNER, 'P', Material.BLAZE_ROD);
        createRecipe("escape_totem", Material.CHORUS_FRUIT, "§5§lTotem Ucieczki", "CCC", "CTC", "CCC", 'C', Material.CHORUS_FRUIT, 'T', Material.TOTEM_OF_UNDYING);
        createRecipe("horn_plenty", Material.GOAT_HORN, "§6§lRóg Obfitości", "GGG", "G H", "GGG", 'G', Material.GOLDEN_APPLE, 'H', Material.GOAT_HORN);

        // --- SPECJALNE ---
        createRecipe("anti_pearl", Material.ENDER_EYE, "§0§lAnty-Perła", "DOD", "OEO", "DOD", 'D', Material.DIAMOND, 'O', Material.OBSIDIAN, 'E', Material.ENDER_EYE);
        createRecipe("web_ball", Material.SNOWBALL, "§f§lKula Pajęczyny", "WWW", "WSW", "WWW", 'W', Material.COBWEB, 'S', Material.SNOWBALL);
        createRecipe("magma_chest", Material.NETHERITE_CHESTPLATE, "§c§lZbroja Magmowa", "M M", "MNM", "MMM", 'M', Material.MAGMA_BLOCK, 'N', Material.NETHERITE_CHESTPLATE);
        createRecipe("hook_rod", Material.FISHING_ROD, "§3§lWędka Przyciągacz", "  I", " I ", "S  ", 'I', Material.IRON_INGOT, 'S', Material.STICK);
        createRecipe("berserk_pot", Material.POTION, "§4§lWywar Berserkera", "RGR", "GPG", "RGR", 'R', Material.REDSTONE_BLOCK, 'G', Material.GLOWSTONE_BLOCK, 'P', Material.POTION);
        createRecipe("ocean_heart", Material.HEART_OF_THE_SEA, "§b§lAmulet Oceanu", "CCC", "CHC", "CCC", 'C', Material.PRISMARINE_CRYSTALS, 'H', Material.HEART_OF_THE_SEA);
        createRecipe("vault_key", Material.TRIPWIRE_HOOK, "§e§lKlucz do Skarbca", "GGG", "GKG", "GGG", 'G', Material.GOLD_BLOCK, 'K', Material.TRIPWIRE_HOOK);
        createRecipe("scout_compass", Material.COMPASS, "§2§lKompas Zwiadowcy", "EEE", "ECE", "EEE", 'E', Material.EMERALD, 'C', Material.COMPASS);
    }

    private void createRecipe(String id, Material mat, String name, String s1, String s2, String s3, Object... ing) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.getPersistentDataContainer().set(itemKey, PersistentDataType.STRING, id);
        item.setItemMeta(meta);
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, id), item);
        recipe.shape(s1, s2, s3);
        for (int i = 0; i < ing.length; i += 2) recipe.setIngredient((char) ing[i], (Material) ing[i + 1]);
        Bukkit.addRecipe(recipe);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        if (item == null || !item.hasItemMeta()) return;
        String id = item.getItemMeta().getPersistentDataContainer().get(itemKey, PersistentDataType.STRING);
        if (id == null) return;

        if (e.getAction().name().contains("RIGHT")) {
            if (!checkCooldown(p, id, getCooldownSec(id))) return;

            switch (id) {
                case "vampire_sword":
                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
                    p.sendMessage("§4Poczuj krew!");
                    break;
                case "shadow_dagger":
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 0));
                    p.sendMessage("§8Znikasz w cieniu...");
                    break;
                case "escape_totem":
                    p.teleport(p.getLocation().add(new Vector(Math.random() * 50 - 25, 0, Math.random() * 50 - 25)));
                    p.sendMessage("§5Teleportacja!");
                    break;
                case "horn_plenty":
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 200, 1));
                    p.getNearbyEntities(10, 10, 10).forEach(ent -> {
                        if (ent instanceof Player) ((Player) ent).addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 200, 1));
                    });
                    break;
                case "void_pickaxe":
                    p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 600, 2));
                    break;
            }
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player p)) return;
        ItemStack item = p.getInventory().getItemInMainHand();
        String id = item.getItemMeta() != null ? item.getItemMeta().getPersistentDataContainer().get(itemKey, PersistentDataType.STRING) : null;

        if (id == null) return;

        if (id.equals("shield_breaker") && e.getEntity() instanceof Player target) {
            if (target.isBlocking()) {
                target.setCooldown(Material.SHIELD, 100);
                target.sendMessage("§cTwoja tarcza została rozbita!");
            }
        }
        if (id.equals("thor_hammer")) {
            e.getEntity().getWorld().strikeLightning(e.getEntity().getLocation());
        }
    }

    private int getCooldownSec(String id) {
        return switch (id) {
            case "thor_hammer" -> 60;
            case "vampire_sword" -> 20;
            case "escape_totem" -> 120;
            case "shadow_dagger" -> 45;
            default -> 10;
        };
    }

    private boolean checkCooldown(Player p, String item, int sec) {
        cooldowns.putIfAbsent(p.getUniqueId(), new HashMap<>());
        long now = System.currentTimeMillis();
        long last = cooldowns.get(p.getUniqueId()).getOrDefault(item, 0L);
        if (now - last < sec * 1000L) {
            p.sendMessage("§cZaczekaj " + (sec - (now - last) / 1000) + "s!");
            return false;
        }
        cooldowns.get(p.getUniqueId()).put(item, now);
        return true;
    }
}
