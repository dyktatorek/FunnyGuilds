package net.dzikoysk.funnyguilds.data.util;

import net.dzikoysk.funnyguilds.FunnyGuilds;
import net.dzikoysk.funnyguilds.basic.guild.Guild;
import net.dzikoysk.funnyguilds.basic.guild.Region;
import net.dzikoysk.funnyguilds.basic.user.User;
import net.dzikoysk.funnyguilds.basic.user.UserBan;
import org.bukkit.Location;

import java.util.Set;
import java.util.UUID;

public final class DeserializationUtils {

    @SuppressWarnings("unchecked")
    public static Guild deserializeGuild(Object[] values) {
        if (values == null) {
            FunnyGuilds.getInstance().getPluginLogger().error("[Deserialize] Cannot deserialize guild! Caused by: null");
            return null;
        }
        
        final Guild guild = Guild.getOrCreate((UUID) values[0]);
        
        guild.setName((String) values[1]);
        guild.setTag(FunnyGuilds.getInstance().getPluginConfiguration().guildTagKeepCase ? (String) values[2] : (FunnyGuilds.getInstance().getPluginConfiguration().guildTagUppercase ? ((String) values[2]).toUpperCase() : ((String) values[2]).toLowerCase()));
        guild.setOwner((User) values[3]);
        guild.setHome((Location) values[4]);
        guild.setRegion((Region) values[5]);
        guild.setMembers((Set<User>) values[6]);
        guild.setAllies((Set<Guild>) values[7]);
        guild.setEnemies((Set<Guild>) values[8]);
        guild.setBorn((long) values[9]);
        guild.setValidity((long) values[10]);
        guild.setAttacked((long) values[11]);
        guild.setLives((int) values[12]);
        guild.setBan((long) values[13]);
        guild.setDeputies((Set<User>) values[14]);
        guild.deserializationUpdate();
        
        return guild;
    }

    public static Region deserializeRegion(Object[] values) {
        if (values == null) {
            FunnyGuilds.getInstance().getPluginLogger().error("Cannot deserialize region! Caused by: null");
            return null;
        }
        
        Region region = Region.get((String) values[0]);

        region.setCenter((Location) values[1]);
        region.setSize((int) values[2]);
        region.setEnlarge((int) values[3]);
        region.update();
        
        return region;
    }

    public static User deserializeUser(Object[] values) {
        User user = User.get(UUID.fromString((String) values[0]), (String) values[1]);
        
        user.getRank().setPoints((int) values[2]);
        user.getRank().setKills((int) values[3]);
        user.getRank().setDeaths((int) values[4]);

        long banTime = (long) values[5];

        if (banTime > 0) {
            user.setBan(new UserBan((String) values[6], banTime));
        }
        
        return user;
    }

    private DeserializationUtils() {}
    
}
