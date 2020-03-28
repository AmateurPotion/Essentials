package remake.internal;

import arc.files.Fi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static remake.Main.root;
import static remake.Main.tool;

public class Log {
    final static arc.util.Log.DefaultLogHandler handler = new arc.util.Log.DefaultLogHandler();

    public enum LogType {
        log, warn, error, debug, server, serverwarn, servererr, client, clientwarn, clienterr, config, player, playerwarn, playererr,
        tap, withdraw, block, deposit, chat, griefer, web
    }

    public static void info(String value, Object... parameter) {
        String result = new Bundle().get(value);
        if (result != null) {
            handler.log(arc.util.Log.LogLevel.info, MessageFormat.format(result, parameter));
        } else {
            handler.log(arc.util.Log.LogLevel.info, value);
        }
    }

    public static void err(String value, Object... parameter) {
        String result = new Bundle().get(value);
        if (result != null) {
            handler.log(arc.util.Log.LogLevel.err, MessageFormat.format(result, parameter));
        } else {
            handler.log(arc.util.Log.LogLevel.err, value);
        }
    }

    public static void warn(String value, Object... parameter) {
        String result = new Bundle().get(value);
        if (result != null) {
            handler.log(arc.util.Log.LogLevel.warn, MessageFormat.format(result, parameter));
        } else {
            handler.log(arc.util.Log.LogLevel.warn, value);
        }
    }

    public static void server(String value, Object... parameter) {
        String result = new Bundle().get(value);
        if (result != null) {
            handler.log(arc.util.Log.LogLevel.info, "[EssentialServer] " + MessageFormat.format(result, parameter));
        } else {
            handler.log(arc.util.Log.LogLevel.info, "[EssentialServer] " + value);
        }
    }

    public static void client(String value, Object... parameter) {
        String result = new Bundle().get(value);
        if (result != null) {
            handler.log(arc.util.Log.LogLevel.info, "[EssentialClient] " + MessageFormat.format(result, parameter));
        } else {
            handler.log(arc.util.Log.LogLevel.info, "[EssentialClient] " + value);
        }
    }

    public static void player(String value, Object... parameter) {
        String result = new Bundle().get(value);
        if (result != null) {
            handler.log(arc.util.Log.LogLevel.info, "[EssentialPlayer] " + MessageFormat.format(result, parameter));
        } else {
            handler.log(arc.util.Log.LogLevel.info, "[EssentialPlayer] " + value);
        }
    }

    public static void write(LogType type, String value) {
        String date = DateTimeFormatter.ofPattern("yyyy-MM-dd HH_mm_ss").format(LocalDateTime.now());
        Path newlog = Paths.get(root.child("log/" + type + ".log").path());
        Path oldlog = Paths.get(root.child("log/old/" + type + "/" + date + ".log").path());
        Fi mainlog = root.child("log/" + type + ".log");
        Fi logfolder = root.child("log");

        if (mainlog != null && mainlog.length() > 1024 * 256) {
            mainlog.writeString(new Bundle().get("log-file-end", date), true);
            try {
                Files.move(newlog, oldlog, REPLACE_EXISTING);
            } catch (IOException e) {
                new CrashReport(e);
            }
            mainlog = null;
        }

        if (mainlog == null) mainlog = logfolder.child(type + ".log");
        mainlog.writeString("[" + tool.getTime() + "]" + value + "\n", true);
    }
}
