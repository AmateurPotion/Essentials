package essentials.core.plugin;

import essentials.internal.Bundle;
import essentials.internal.Log;
import org.hjson.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static essentials.Main.*;
import static essentials.PluginVars.config_version;

public class Config {
    public int version;
    public Locale language;
    public boolean serverenable;
    public int serverport;
    public boolean clientenable;
    public int clientport;
    public String clienthost;
    public boolean realname;
    public boolean strictname;
    public int cupdatei;
    public boolean scanresource;
    public boolean antigrief;
    public boolean alertaction;
    public boolean explimit;
    public double basexp;
    public double exponent;
    public boolean levelupalarm;
    public int alarmlevel;
    public boolean banshare;
    public JsonArray bantrust;
    public boolean query;
    public boolean antivpn;
    public boolean antirush;
    public LocalTime antirushtime;
    public boolean vote;
    public boolean logging;
    public boolean update;
    public boolean internalDB;
    public boolean DBServer;
    public String DBurl;
    public boolean OldDBMigration;
    public String OldDBurl;
    public String OldDBID;
    public String OldDBPW;
    public String dataserverurl;
    public String dataserverid;
    public String dataserverpw;
    public boolean loginenable;
    public String passwordmethod;
    public boolean validconnect;
    public String emailserver;
    public int emailport;
    public String emailAccountID;
    public String emailUsername;
    public String emailPassword;
    public String discordtoken;
    public Long discordguild;
    public String discordroom;
    public String discordlink;
    public String discordrole;
    public String discordprefix;
    public boolean translate;
    public String translateid;
    public String translatepw;
    public boolean debug;
    public String debugcode;
    public boolean crashreport;
    public LocalTime savetime;
    public boolean rollback;
    public int slotnumber;
    public boolean autodifficulty;
    public int difficultyEasy;
    public int difficultyNormal;
    public int difficultyHard;
    public int difficultyInsane;
    public boolean border;
    public int spawnlimit;
    public String prefix;
    public String eventport;

    JsonObject obj;

    public Config() {
        JsonObject settings;
        JsonObject database;
        JsonObject network;
        JsonObject anti;
        JsonObject features;
        JsonObject difficulty;
        JsonObject tr;
        JsonObject auth;
        JsonObject discord;
        try {
            obj = JsonValue.readHjson(root.child("config.hjson").readString()).asObject();
            JsonObject as = new JsonObject();
            as.add("test", "testas", "this comment");
            System.out.println(as.toString());
        } catch (RuntimeException e) {
            JsonObject empty = new JsonObject();
            obj = new JsonObject();
            obj.add("settings", new JsonObject().add("database", empty));
            obj.add("network", empty);
            obj.add("antigrief", empty);
            obj.add("features", new JsonObject().add("difficulty", empty).add("translate", empty));
            obj.add("auth", new JsonObject().add("discord", empty));
        }

        settings = obj.get("settings").asObject();
        version = settings.getInt("version", config_version);
        language = new Locale(settings.getString("language", System.getProperty("user.language") + "_" + System.getProperty("user.country")));
        logging = settings.getBoolean("logging", true);
        update = settings.getBoolean("update", true);
        debug = settings.getBoolean("debug", false);
        debugcode = settings.getString("debugcode", "none");
        crashreport = settings.getBoolean("crashreport", true);
        prefix = settings.getString("prefix", "[green][Essentials] []");

        database = settings.get("database").asObject();
        internalDB = database.getBoolean("internalDB", true);
        DBServer = database.getBoolean("DBServer", false);
        DBurl = database.getString("DBurl", "jdbc:h2:file:./config/mods/Essentials/data/player");
        OldDBMigration = database.getBoolean("OldDBMigration", false);
        OldDBurl = database.getString("OldDBurl", "jdbc:sqlite:config/mods/Essentials/data/player.sqlite3");
        OldDBID = database.getString("OldDBID", "none");
        OldDBPW = database.getString("OldDBPW", "none");
        dataserverurl = database.getString("dataserverurl", "none");
        dataserverid = database.getString("dataserverid", "none");
        dataserverpw = database.getString("dataserverpw", "none");

        network = obj.get("network").asObject();
        serverenable = network.getBoolean("serverenable", false);
        serverport = network.getInt("serverport", 25000);
        clientenable = network.getBoolean("clientenable", false);
        clientport = network.getInt("clientport", 25000);
        clienthost = network.getString("clienthost", "mindustry.kr");
        banshare = network.getBoolean("banshare", false);
        bantrust = network.get("bantrust") == null ? JsonArray.readJSON("[\"127.0.0.1\",\"localhost\"]").asArray() : network.get("bantrust").asArray();
        query = network.getBoolean("query", false);

        anti = obj.get("antigrief").asObject();
        antigrief = anti.getBoolean("antigrief", false);
        antivpn = anti.getBoolean("antivpn", false);
        antirush = anti.getBoolean("antirush", false);
        antirushtime = LocalTime.parse(anti.getString("antirushtime", "00:10:00"), DateTimeFormatter.ofPattern("HH:mm:ss"));
        alertaction = anti.getBoolean("alertaction", false);
        realname = anti.getBoolean("realname", false);
        strictname = anti.getBoolean("strictname", false);
        scanresource = anti.getBoolean("scanresource", false);

        features = obj.get("features").asObject();
        explimit = features.getBoolean("explimit", false);
        basexp = features.getDouble("basexp", 500.0);
        exponent = features.getDouble("exponent", 1.12);
        levelupalarm = features.getBoolean("levelupalarm", false);
        alarmlevel = features.getInt("alarmlevel", 20);
        vote = features.getBoolean("vote", true);
        savetime = LocalTime.parse(features.getString("savetime", "00:10:00"), DateTimeFormatter.ofPattern("HH:mm:ss"));
        rollback = features.getBoolean("rollback", false);
        slotnumber = features.getInt("slotnumber", 1000);
        border = features.getBoolean("border", false);
        spawnlimit = features.getInt("spawnlimit", 500);
        eventport = features.getString("eventport", "8000-8050");
        cupdatei = features.getInt("cupdatei", 1000);

        difficulty = features.get("difficulty").asObject();
        autodifficulty = difficulty.getBoolean("autodifficulty", false);
        difficultyEasy = difficulty.getInt("difficultyEasy", 2);
        difficultyNormal = difficulty.getInt("difficultyNormal", 4);
        difficultyHard = difficulty.getInt("difficultyHard", 6);
        difficultyInsane = difficulty.getInt("difficultyInsane", 10);

        tr = features.get("translate").asObject();
        translate = tr.getBoolean("translate", false);
        translateid = tr.getString("translateid", "none");
        translatepw = tr.getString("translatepw", "none");

        auth = obj.get("auth").asObject();
        loginenable = obj.getBoolean("loginenable", false);
        passwordmethod = auth.getString("passwordmethod", "password");
        validconnect = auth.getBoolean("validconnect", false);
        emailserver = auth.getString("emailserver", "smtp.gmail.com");
        emailport = auth.getInt("emailport", 587);
        emailAccountID = auth.getString("emailAccountID", "none");
        emailUsername = auth.getString("emailUsername", "none");
        emailPassword = auth.getString("emailPassword", "none");

        discord = auth.get("discord").asObject();
        discordtoken = discord.getString("discordtoken", "none");
        discordguild = discord.getLong("discordguild", 0L);
        discordroom = discord.getString("discordroom", "none");
        discordlink = discord.getString("discordlink", "none");
        discordrole = discord.getString("discordrole", "none");
        discordprefix = discord.getString("discordprefix", "none");

        update();
    }

    public void version(int version) {
        this.version = version;
    }

    public void language(Locale language) {
        this.language = language;
    }

    public void serverenable(boolean serverenable) {
        this.serverenable = serverenable;
    }

    public void serverport(int serverport) {
        this.serverport = serverport;
    }

    public void clientenable(boolean clientenable) {
        this.clientenable = clientenable;
    }

    public void clientport(int clientport) {
        this.clientport = clientport;
    }

    public void clienthost(String clienthost) {
        this.clienthost = clienthost;
    }

    public void realname(boolean realname) {
        this.realname = realname;
    }

    public void strictname(boolean strictname) {
        this.strictname = strictname;
    }

    public void cupdatei(int cupdatei) {
        this.cupdatei = cupdatei;
    }

    public void scanresource(boolean scanresource) {
        this.scanresource = scanresource;
    }

    public void antigrief(boolean antigrief) {
        this.antigrief = antigrief;
    }

    public void alertaction(boolean alertaction) {
        this.alertaction = alertaction;
    }

    public void explimit(boolean explimit) {
        this.explimit = explimit;
    }

    public void basexp(double basexp) {
        this.basexp = basexp;
    }

    public void exponent(double exponent) {
        this.exponent = exponent;
    }

    public void levelupalarm(boolean levelupalarm) {
        this.levelupalarm = levelupalarm;
    }

    public void alarmlevel(int alarmlevel) {
        this.alarmlevel = alarmlevel;
    }

    public void banshare(boolean banshare) {
        this.banshare = banshare;
    }

    public void bantrust(JsonArray bantrust) {
        this.bantrust = bantrust;
    }

    public void query(boolean query) {
        this.query = query;
    }

    public void antivpn(boolean antivpn) {
        this.antivpn = antivpn;
    }

    public void antirush(boolean antirush) {
        this.antirush = antirush;
    }

    public void antirushtime(LocalTime antirushtime) {
        this.antirushtime = antirushtime;
    }

    public void voteenable(boolean voteenable) {
        this.vote = voteenable;
    }

    public void logging(boolean logging) {
        this.logging = logging;
    }

    public void update(boolean update) {
        this.update = update;
    }

    public void internalDB(boolean internalDB) {
        this.internalDB = internalDB;
    }

    public void DBServer(boolean DBServer) {
        this.DBServer = DBServer;
    }

    public void DBurl(String DBurl) {
        this.DBurl = DBurl;
    }

    public void oldDBMigration(boolean oldDBMigration) {
        OldDBMigration = oldDBMigration;
    }

    public void oldDBurl(String oldDBurl) {
        OldDBurl = oldDBurl;
    }

    public void oldDBID(String oldDBID) {
        OldDBID = oldDBID;
    }

    public void oldDBPW(String oldDBPW) {
        OldDBPW = oldDBPW;
    }

    public void dataserverurl(String dataserverurl) {
        this.dataserverurl = dataserverurl;
    }

    public void dataserverid(String dataserverid) {
        this.dataserverid = dataserverid;
    }

    public void dataserverpw(String dataserverpw) {
        this.dataserverpw = dataserverpw;
    }

    public void loginenable(boolean loginenable) {
        this.loginenable = loginenable;
    }

    public void passwordmethod(String passwordmethod) {
        this.passwordmethod = passwordmethod;
    }

    public void validconnect(boolean validconnect) {
        this.validconnect = validconnect;
    }

    public void emailserver(String emailserver) {
        this.emailserver = emailserver;
    }

    public void emailport(int emailport) {
        this.emailport = emailport;
    }

    public void emailAccountID(String emailAccountID) {
        this.emailAccountID = emailAccountID;
    }

    public void emailUsername(String emailUsername) {
        this.emailUsername = emailUsername;
    }

    public void emailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public void discordtoken(String discordtoken) {
        this.discordtoken = discordtoken;
    }

    public void discordguild(Long discordguild) {
        this.discordguild = discordguild;
    }

    public void discordroom(String discordroom) {
        this.discordroom = discordroom;
    }

    public void discordlink(String discordlink) {
        this.discordlink = discordlink;
    }

    public void discordrole(String discordrole) {
        this.discordrole = discordrole;
    }

    public void discordprefix(String discordprefix) {
        this.discordprefix = discordprefix;
    }

    public void translate(boolean translate) {
        this.translate = translate;
    }

    public void translateid(String translateid) {
        this.translateid = translateid;
    }

    public void translatepw(String translatepw) {
        this.translatepw = translatepw;
    }

    public void debug(boolean debug) {
        this.debug = debug;
    }

    public void debugcode(String debugcode) {
        this.debugcode = debugcode;
    }

    public void crashreport(boolean crashreport) {
        this.crashreport = crashreport;
    }

    public void savetime(LocalTime savetime) {
        this.savetime = savetime;
    }

    public void rollback(boolean rollback) {
        this.rollback = rollback;
    }

    public void slotnumber(int slotnumber) {
        this.slotnumber = slotnumber;
    }

    public void autodifficulty(boolean autodifficulty) {
        this.autodifficulty = autodifficulty;
    }

    public void difficultyEasy(int difficultyEasy) {
        this.difficultyEasy = difficultyEasy;
    }

    public void difficultyNormal(int difficultyNormal) {
        this.difficultyNormal = difficultyNormal;
    }

    public void difficultyHard(int difficultyHard) {
        this.difficultyHard = difficultyHard;
    }

    public void difficultyInsane(int difficultyInsane) {
        this.difficultyInsane = difficultyInsane;
    }

    public void border(boolean border) {
        this.border = border;
    }

    public void spawnlimit(int spawnlimit) {
        this.spawnlimit = spawnlimit;
    }

    public void prefix(String prefix) {
        this.prefix = prefix;
    }

    public void eventport(String eventport) {
        this.eventport = eventport;
    }

    public void update() {
        locale = tool.TextToLocale(obj.getString("language", locale.toString()));
        Bundle bundle = new Bundle(locale);

        if (obj.getInt("version", 0) < config_version) Log.info("config-updated");

        JsonObject config = new JsonObject();
        JsonObject settings = new JsonObject();
        JsonObject db = new JsonObject();
        JsonObject network = new JsonObject();
        JsonObject anti = new JsonObject();
        JsonObject features = new JsonObject();
        JsonObject difficulty = new JsonObject();
        JsonObject auth = new JsonObject();
        JsonObject discord = new JsonObject();
        JsonObject tr = new JsonObject();

        config.setFullComment(CommentType.BOL, bundle.get("config-description"));
        config.add("settings", settings);
        config.add("network", network);
        config.add("antigrief", anti);
        config.add("features", features);
        config.add("auth", auth);

        // 플러그인 설정
        settings.add("version", version, bundle.get("config-version-description"));
        settings.add("language", language.toString(), bundle.get("config-language-description"));
        settings.add("logging", logging, bundle.get("config-logging-description"));
        settings.add("update", update, bundle.get("config-update-description"));
        //settings.add(CommentType.BOL, CommentStyle.BLOCK,"\n\nasdkfjlkfkjdaslkfjdaslkfjdsalkfjdsalkfjadsflkajdsflkasjflkdasjflks");
        settings.add("debug", debug, bundle.get("config-debug-description"));
        settings.add("debugcode", debugcode);
        settings.add("crash-report", crashreport);
        //settings.setLineLength(1);
        settings.add("prefix", prefix, bundle.get("config-prefix-description"));
        //settings.setLineLength(1);

        // DB 설정 (settings 상속)
        settings.add("database", db);
        db.add("internalDB", internalDB, bundle.get("config-database-description"));
        db.add("DBServer", DBServer);
        db.add("DBurl", DBurl);
        //db.setLineLength(1);
        db.add("old-db-migration", OldDBMigration, bundle.get("config-old-database-migration-description"));
        db.add("old-db-url", OldDBurl);
        db.add("old-db-id", OldDBID);
        db.add("old-db-pw", OldDBPW);
        //db.setLineLength(1);
        db.add("data-server-url", dataserverurl, bundle.get("config-data-share-description"));
        db.add("data-server-id", dataserverid);
        db.add("data-server-pw", dataserverpw);

        // 네트워크 설정
        network.add("server-enable", serverenable, bundle.get("config-network-description"));
        network.add("server-port", serverport);
        network.add("client-enable", clientenable);
        network.add("client-port", clientport);
        network.add("client-host", clienthost);
        //network.setLineLength(1);
        network.add("banshare", banshare, bundle.get("config-banshare-description"));
        network.add("bantrust", bantrust, bundle.get("config-bantrust-description"));
        //network.setLineLength(1);
        network.add("query", query, bundle.get("config-query-description"));

        // 테러방지 설정
        anti.add("antigrief", antigrief, bundle.get("config-antigrief-description"));
        anti.add("antivpn", antivpn, bundle.get("config-antivpn-description"));
        anti.add("antirush", antirush, bundle.get("config-antirush-description"));
        anti.add("antirushtime", antirushtime.format(DateTimeFormatter.ofPattern("HH:mm:ss")), bundle.get("config-antirushtime-description"));
        anti.add("alert-action", alertaction, bundle.get("config-alert-action-description"));
        anti.add("realname", realname, bundle.get("config-realname-description"));
        anti.add("strict-name", strictname, bundle.get("config-strict-name-description"));
        anti.add("scanresource", scanresource, bundle.get("config-scanresource-description"));

        // 특별한 기능 설정
        features.add("explimit", explimit, bundle.get("config-exp-explimit-description"));
        features.add("basexp", basexp, bundle.get("config-exp-basexp-description"));
        features.add("exponent", exponent, bundle.get("config-exp-exponent-description"));
        features.add("levelupalarm", levelupalarm, bundle.get("config-exp-levelupalarm-description"));
        features.add("alarm-minimal-level", alarmlevel, bundle.get("config-exp-minimal-level-description"));
        features.add("vote", vote, bundle.get("config-vote-description"));
        features.add("savetime", savetime.format(DateTimeFormatter.ofPattern("HH:mm:ss")), bundle.get("config-savetime-description"));
        features.add("rollback", rollback, bundle.get("config-slotnumber-description"));
        features.add("slotnumber", slotnumber);
        features.add("border", border, bundle.get("config-border-description"));
        features.add("spawnlimit", spawnlimit, bundle.get("config-spawnlimit-description"));
        features.add("eventport", eventport, bundle.get("config-event-port-description"));
        features.add("cupdatei", cupdatei, bundle.get("config-colornick-description"));

        // 난이도 설정 (features 상속)
        features.add("difficulty", difficulty, bundle.get("config-auto-difficulty-description"));
        difficulty.add("auto-difficulty", autodifficulty);
        difficulty.add("easy", difficultyEasy);
        difficulty.add("normal", difficultyNormal);
        difficulty.add("hard", difficultyHard);
        difficulty.add("insane", difficultyInsane);

        // 번역 설정 (features 상속)
        features.add("translate", tr, bundle.get("config-papago-description"));
        tr.add("translate", translate);
        tr.add("translateid", translateid);
        tr.add("translatepw", translatepw);

        // 로그인 설정
        auth.add("loginenable", loginenable, bundle.get("config-login-description"));
        auth.add("loginmethod", passwordmethod, bundle.get("config-loginmethod-description"));
        auth.add("validconnect", validconnect, bundle.get("config-validconnect-description"));
        //auth.setLineLength(1);
        auth.add("email-smtp-server", emailserver, bundle.get("config-email-description"));
        auth.add("email-smtp-port", emailport);
        auth.add("email-smtp-accountid", emailAccountID);
        auth.add("email-smtp-username", emailUsername);
        auth.add("email-smtp-password", emailPassword);

        // Discord 설정 (auth 상속)
        //auth.setLineLength(1);
        auth.add("discord", discord, bundle.get("config-discord-description"));
        discord.add("discord-token", discordtoken);
        discord.add("discord-guild", discordguild);
        discord.add("discord-room", discordroom);
        discord.add("discord-link", discordlink);
        discord.add("discord-register-role", discordrole);
        discord.add("discord-command-prefix", discordprefix);

        root.child("config.hjson").writeString(config.toString(Stringify.HJSON_COMMENTS));
    }
}
