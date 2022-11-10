package com.github.channelingmc.quicksand.common;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class QuicksandConfigs {
    
    public static final Client CLIENT;
    public static final Common COMMON;
    public static final Server SERVER;
    
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final ForgeConfigSpec SERVER_SPEC;
    
    public static class Client {
    
        public final ForgeConfigSpec.ConfigValue<Boolean> quicksandRenderFog;
        
        public Client(ForgeConfigSpec.Builder builder) {
            builder.push("render");
            quicksandRenderFog = builder
                .comment("When enabled, renders Powder-Snow-like fog when submerged in Quicksand.")
                .define("renderFog", true);
            builder.pop();
        }
        
    }
    
    public static class Common {
        
        public final ForgeConfigSpec.ConfigValue<Integer> quicksandLakeRarity;
        public final ForgeConfigSpec.ConfigValue<Integer> redQuicksandLakeRarity;
        
        public final ForgeConfigSpec.ConfigValue<Boolean> quicksandRenewable;
        
        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("levelgen");
            quicksandLakeRarity = builder
                .comment("The rarity of the Quicksand Lake to generate (Greater value stands for lower chance)")
                .defineInRange("quicksandLakeRarity", 16, 1, Integer.MAX_VALUE);
            redQuicksandLakeRarity = builder
                .comment("The rarity of the Red Quicksand Lake to generate (Greater value stands for lower chance)")
                .defineInRange("redQuicksandLakeRarity", 16, 1, Integer.MAX_VALUE);
            builder.pop();
            builder.push("gameplay");
            quicksandRenewable = builder
                .comment("When enabled, Quicksand Cauldron can be crafted by adding Sand to Water Cauldron (level 1).")
                .define("quicksandRenewable", true);
            builder.pop();
        }
        
    }
    
    public static class Server {
    
        public final ForgeConfigSpec.ConfigValue<Boolean> quicksandDrownsEntities;
        public final ForgeConfigSpec.ConfigValue<Boolean> quicksandExtinguishesFire;
        public final ForgeConfigSpec.ConfigValue<Boolean> quicksandConvertsZombie;
        public final ForgeConfigSpec.ConfigValue<Boolean> quicksandConvertsDrowned;
    
        public Server(ForgeConfigSpec.Builder builder) {
            builder.push("gameplay");
            quicksandDrownsEntities = builder
                .comment("When enabled, Quicksand makes submerged entities drown.")
                .define("quicksandDrownsEntities", true);
            quicksandExtinguishesFire = builder
                .comment("When enabled, Quicksand and Quicksand Cauldron can extinguish burning entities.")
                .define("quicksandExtinguishesFire", true);
            quicksandConvertsZombie = builder
                .comment("When enabled, Quicksand can convert submerged Zombie into Husk.")
                .define("quicksandConvertsZombie", true);
            quicksandConvertsDrowned = builder
                .comment("When enabled, Quicksand can convert submerged Drowned into Zombie.")
                .define("quicksandConvertsDrowned", true);
            builder.pop();
        }
        
    }
    
    static {
        Pair<Client, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(Client::new);
        Pair<Common, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(Common::new);
        Pair<Server, ForgeConfigSpec> server = new ForgeConfigSpec.Builder().configure(Server::new);
        CLIENT = client.getKey();
        COMMON = common.getKey();
        SERVER = server.getKey();
        CLIENT_SPEC = client.getValue();
        COMMON_SPEC = common.getValue();
        SERVER_SPEC = server.getValue();
    }
    
}
