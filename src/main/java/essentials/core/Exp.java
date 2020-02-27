package essentials.core;

import mindustry.entities.type.Player;
import mindustry.gen.Call;

import static essentials.Global.nbundle;
import static essentials.Main.config;
import static essentials.core.PlayerDB.PlayerData;

public class Exp {
    private static double BASE_XP = config.getBasexp();
    private static double EXPONENT = config.getExponent();

    public void setExp(Player player) {
        PlayerData target = PlayerData(player.uuid);

        int currentlevel = target.level;
        int max = (int) calculateFullTargetXp(currentlevel);

        int xp =  target.exp;
        int levelXp = max - xp;
        int level = calculateLevel(xp);
        int reqexp = (int)Math.floor(max);
        String reqtotalexp = xp+"("+(int) Math.floor(levelXp)+") / "+(int) Math.floor(max);

        target.reqexp = reqexp;
        target.level = level;
        target.reqtotalexp = reqtotalexp;

        if(currentlevel < level && currentlevel > config.getAlarmlevel() && config.isLevelupalarm()){
            Call.onInfoToast(nbundle("player-levelup", player.name,level),600);
        }
    }

    double calcXpForLevel(int level) {
        return BASE_XP+(BASE_XP * Math.pow(level, EXPONENT));
    }

    double calculateFullTargetXp(int level) {
        double requiredXP = 0;
        for (int i = 0; i <= level; i++) {
            requiredXP += calcXpForLevel(i);
        }
        return requiredXP;
    }

    int calculateLevel(double xp) {
        int level = 0;
        double maxXp = calcXpForLevel(0);
        do {
            maxXp += calcXpForLevel(++level);
        } while (maxXp < xp);
        return level;
    }
}

