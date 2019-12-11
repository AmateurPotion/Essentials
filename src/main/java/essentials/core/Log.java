package essentials.core;

import essentials.Global;
import io.anuke.arc.Core;
import io.anuke.arc.Events;
import io.anuke.arc.files.FileHandle;
import io.anuke.mindustry.Vars;
import io.anuke.mindustry.entities.type.Player;
import io.anuke.mindustry.game.EventType.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static essentials.Global.getTime;
import static essentials.Global.nbundle;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Log{
    public static ExecutorService ex = Executors.newSingleThreadExecutor(new Global.threadname("EssentialLog"));

    public void main() {
        // No error, griefer, non-block, withdraw event
        Events.on(PlayerChatEvent.class, e -> {
            writelog("chat", e.player.name + ": " + e.message);
        });

        Events.on(TapEvent.class, e-> {
            if (e.tile.entity != null && e.tile.entity.block != null && e.player != null && e.player.name != null) {
                writelog("tap", nbundle("log-tap", e.player.name, e.tile.entity.block.name));
            }
        });

        Events.on(TapConfigEvent.class, e-> {
            if (e.tile.entity != null && e.tile.entity.block != null && e.player != null && e.player.name != null) {
                writelog("tap", nbundle("log-tap-config", e.player.name, e.tile.entity.block.name));
            }
        });

        Events.on(BlockBuildEndEvent.class, e -> {
            if(!e.breaking && e.player != null && e.tile.entity.block != null && e.player.name != null) {
                writelog("block", nbundle("log-block-place", e.player.name, e.tile.entity.block.name));
            }
        });

        Events.on(BuildSelectEvent.class, e -> {
            if(e.breaking && e.builder instanceof Player && e.builder.buildRequest() != null && !e.builder.buildRequest().block.name.matches(".*build.*")) {
                writelog("block", nbundle("log-block-remove", ((Player) e.builder).name, e.builder.buildRequest().block.name));
            }
        });

        Events.on(MechChangeEvent.class, e -> writelog("player", nbundle("log-mech-change", e.player.name, e.mech.name)));
        Events.on(PlayerJoin.class, e -> writelog("player", nbundle("log-player-join", e.player.name, e.player.uuid, Vars.netServer.admins.getInfo(e.player.uuid).lastIP)));
        Events.on(PlayerConnect.class, e -> writelog("player", nbundle("log-player-connect", e.player.name, e.player.uuid, Vars.netServer.admins.getInfo(e.player.uuid).lastIP)));
        Events.on(PlayerLeave.class, e -> writelog("player", nbundle("log-player-leave", e.player.name, e.player.uuid, Vars.netServer.admins.getInfo(e.player.uuid).lastIP)));
        Events.on(DepositEvent.class, e -> writelog("deposit", nbundle("log-deposit", e.player.name, e.player.item().item.name, e.tile.block().name)));
    }

    public static void writelog(String type, String text){
        Thread t = new Thread(() -> {
            String date = DateTimeFormatter.ofPattern("yyyy-MM-dd HH_mm_ss").format(LocalDateTime.now());
            Path newlog = Paths.get(Core.settings.getDataDirectory().child("mods/Essentials/log/" + type + ".log").path());
            Path oldlog = Paths.get(Core.settings.getDataDirectory().child("mods/Essentials/log/old/" + type + "/" + date + ".log").path());
            FileHandle mainlog = Core.settings.getDataDirectory().child("mods/Essentials/log/" + type + ".log");
            FileHandle logfolder = Core.settings.getDataDirectory().child("mods/Essentials/log");

            if (mainlog != null && mainlog.length() > 1024 * 512) {
                mainlog.writeString(nbundle("log-file-end", date), true);
                try {
                    Files.move(newlog, oldlog, REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mainlog = null;
            }

            if (mainlog == null) {
                mainlog = logfolder.child(type + ".log");
            }

            mainlog.writeString(getTime() + text + "\n", true);
        });
        ex.submit(t);
    }
}
