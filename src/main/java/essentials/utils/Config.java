package essentials.utils;

import arc.Core;
import arc.files.Fi;
import essentials.Global;
import org.hjson.JsonObject;
import org.hjson.JsonValue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static essentials.Global.*;
import static essentials.PluginData.loadall;
import static mindustry.Vars.net;

public class Config {
    private JsonObject obj = new JsonObject();
    private Fi path = Core.settings.getDataDirectory().child("mods/Essentials/data/data.json");
    public static JsonObject PluginConfig = new JsonObject();

    public static ExecutorService executorService = Executors.newFixedThreadPool(6, new Global.threadname("Essentials Thread"));
    public static ExecutorService singleService = Executors.newSingleThreadExecutor(new Global.threadname("Essentials single thread"));
    static int version = 9;

    public void main(){
        validfile();
        try{
            JsonObject data = new JsonObject();

            if(!path.exists()){
                data.add("servername", Core.settings.getString("servername"));
                data.add("unexception",false);
                data.add("sqlite",true);
                Core.settings.getDataDirectory().child("mods/Essentials/data/data.json").writeString(data.toString());
            } else {
                data = JsonValue.readJSON(Core.settings.getDataDirectory().child("mods/Essentials/data/data.json").reader()).asObject();
            }

            PluginConfig = data;

            if(Core.settings.getDataDirectory().child("mods/Essentials/config.hjson").exists()) obj = JsonValue.readHjson(Core.settings.getDataDirectory().child("mods/Essentials/config.hjson").reader()).asObject();
            update();

            loadall();
        } catch (IOException e){
            printError(e);
        }
    }

    void update(){
        String antirushtime = obj.getString("antirushtime","10:00");
        if(obj.getInt("version",0) < version) log("config","config-updated");

        String text = "{\n"+
                "  # "+nbundle("config-version-description")+"\n" +
                "  version: "+version+"\n" +
                "\n" +
                "  # "+nbundle("config-language-description")+"\n" +
                "  language: "+getLanguage()+"\n" +
                "\n" +
                "  # "+nbundle("config-server/client-description")+"\n" +
                "  # "+nbundle("config-server/client-child-description")+"\n" +
                "  server-enable: " + isServerenable() + "\n" +
                "  server-port: " + getServerport() + "\n" +
                "\n" +
                "  client-enable: " + isClientenable() + "\n" +
                "  client-port: " + getClientport() + "\n" +
                "  client-host: " + getClienthost() + "\n" +
                "\n" +
                "  # "+nbundle("config-realname-description")+"\n" +
                "  # "+nbundle("config-realname-child-description")+"\n" +
                "  # "+nbundle("config-realname-strict-description")+"\n" +
                "  realname: " + isRealname() + "\n" +
                "  strict-name: " + isStrictname() + "\n" +
                "\n" +
                "  # "+nbundle("config-colornick-description")+"\n" +
                "  cupdatei: " + getCupdatei() + "\n" +
                "\n" +
                "  # "+nbundle("config-detectreactor-description")+"\n" +
                "  detectreactor: " + isDetectreactor() + "\n" +
                "\n" +
                "  # "+nbundle("config-scanresource-description")+"\n" +
                "  scanresource: " + isScanresource() + "\n" +
                "\n" +
                "  # "+nbundle("config-antigrief-description")+"\n" +
                "  # "+nbundle("config-blockdetect-description")+"\n" +
                "  # "+nbundle("config-alertdeposit-description")+"\n" +
                "  antigrief: "+ isAntigrief() + "\n" +
                "  alert-blockdetect: "+ isBlockdetect() + "\n" +
                "  alert-deposit: " + isAlertdeposit() + "\n" +
                "\n" +
                "  # "+nbundle("config-exp-description")+"\n" +
                "  # "+nbundle("config-exp-explimit-description")+"\n" +
                "  # "+nbundle("config-exp-basexp-description")+"\n" +
                "  # "+nbundle("config-exp-exponent-description")+"\n" +
                "  # "+nbundle("config-exp-levelupalarm-description")+"\n" +
                "  # "+nbundle("config-exp-minimal-level-description")+"\n" +
                "  explimit: " + isExplimit() + "\n" +
                "  basexp: " + getBasexp() + "\n" +
                "  exponent: " + getExponent() + "\n" +
                "  levelupalarm: " + isLevelupalarm() + "\n" +
                "  alarm-minimal-level: " + getAlarmlevel() + "\n" +
                "\n" +
                "  # "+nbundle("config-banshare-description")+"\n" +
                "  # "+nbundle("config-banshare-child-description")+"\n" +
                "  banshare: " + isBanshare() + "\n" +
                "\n" +
                "  # "+nbundle("config-bantrust-description")+"\n" +
                "  # "+nbundle("config-bantrust-child-description")+"\n" +
                "  bantrust: " + Arrays.toString(getBantrust()).replace("[","").replace("]","") + "\n" +
                "\n" +
                "  # "+nbundle("config-query-description")+"\n" +
                "  # "+nbundle("config-query-child-description")+"\n" +
                "  query: " + isQuery() + "\n" +
                "\n" +
                "  # "+nbundle("config-antivpn-description")+"\n" +
                "  antivpn: " + isAntivpn() + "\n" +
                "\n" +
                "  # "+nbundle("config-enableantirush-description")+"\n" +
                "  # "+nbundle("config-antirushtime-description")+"\n" +
                "  enable-antirush: " + isEnableantirush() + "\n" +
                "  antirushtime: " + antirushtime + "\n" +
                "\n" +
                "  # "+nbundle("config-logging-description")+"\n" +
                "  logging: " + isLogging() + "\n" +
                "\n" +
                "  # "+nbundle("config-update-description")+"\n" +
                "  update: " + isUpdate() + "\n" +
                "\n" +
                "  # "+nbundle("config-database-description")+"\n" +
                "  # "+nbundle("config-database-child1-description")+"\n" +
                "  # "+nbundle("config-database-child2-description")+"\n" +
                "  sqlite: " + isSqlite() + "\n" +
                "  dburl: " + getDBurl() + "\n" +
                "  dbid: " + getDBid() + "\n" +
                "  dbpw: " + getDBpw() + "\n" +
                "\n" +
                "  # "+nbundle("config-login-description")+"\n" +
                "  # "+nbundle("config-loginmethod-description")+"\n" +
                "  # "+nbundle("config-validconnect-description")+"\n" +
                "  loginenable: " + isLoginenable() + "\n" +
                "  loginmethod: " + getPasswordmethod() + "\n" +
                "  validconnect: " + isValidconnect() + "\n" +
                "\n" +
                "  # "+nbundle("config-discord-description")+"\n" +
                "  # "+nbundle("config-discord-role-description")+"\n" +
                "  discord-token: " + getDiscordToken() + "\n" +
                "  discord-guild: " + getDiscordGuild() + "\n" +
                "  discord-room: " + getDiscordRoom() + "\n" +
                "  discord-link: " + getDiscordLink() + "\n" +
                "  discord-register-role: " + getDiscordRole() +"\n" +
                "  discord-command-prefix: " + getDiscordPrefix() + "\n" +
                "\n" +
                "  # "+nbundle("config-papago-description")+"\n" +
                "  # "+nbundle("config-papago-child-description")+"\n" +
                "  enable-translate: " + isEnableTranslate() + "\n" +
                "  clientId: " + getClientId() + "\n" +
                "  clientSecret: " + getClientSecret() + "\n" +
                "\n" +
                "  # "+nbundle("config-debug-description")+"\n" +
                "  debug: " + isDebug() + "\n" +
                "\n" +
                "  # "+nbundle("config-savetime-description")+"\n" +
                "  savetime: " + getSavetime() + "\n" +
                "\n" +
                "  # "+nbundle("config-slotnumber-description")+"\n" +
                "  enable-rollback: " + isEnableRollback() + "\n" +
                "  slotnumber: " + getSlotnumber() + "\n" +
                "\n" +
                "  # "+nbundle("config-auto-difficulty-description")+"\n" +
                "  auto-difficulty: "+isAutodifficulty()+"\n" +
                "  easy: "+getEasy()+"\n" +
                "  normal: "+getNormal()+"\n" +
                "  hard: "+getHard()+"\n" +
                "  insane: "+getInsane()+"\n" +
                "\n" +
                "  # "+nbundle("config-spawnlimit-description")+"\n" +
                "  spawnlimit: "+getSpawnlimit() +"\n" +
                "\n" +
                "  # "+nbundle("config-prefix-description")+"\n"+
                "  prefix: \""+getPrefix()+"\"\n" +
                "\n" +
                "  # "+nbundle("config-event-port-description")+"\n"+
                "  event-port: "+getEventport()+"\n"+
                "}";
        Core.settings.getDataDirectory().child("mods/Essentials/config.hjson").writeString(text);
        log("config","config-loaded");
    }

    public String getClienthost(){
        return obj.getString("client-host","mindustry.kr");
    }

    public int getClientport(){
        return obj.getInt("client-port",25000);
    }

    public int getServerport(){
        return obj.getInt("server-port",25000);
    }

    public boolean isRealname(){
        return obj.getBoolean("realname",false);
    }

    public boolean isStrictname() {
        return obj.getBoolean("strict-name",false);
    }

    public boolean isDetectreactor(){
        return obj.getBoolean("detectreactor",false);
    }

    public boolean isScanresource(){
        return obj.getBoolean("scanresource",false);
    }

    public boolean isServerenable(){
        return obj.getBoolean("server-enable",false);
    }

    public boolean isClientenable(){
        return obj.getBoolean("client-enable",false);
    }

    public double getBasexp(){
        return obj.getDouble("basexp",500);
    }

    public double getExponent(){
        return obj.getDouble("exponent",1.12f);
    }

    public int getCupdatei(){
        return obj.getInt("cupdatei",1000);
    }

    public boolean isBanshare(){
        return obj.getBoolean("banshare",false);
    }

    public String[] getBantrust(){
        String ban = obj.getString("bantrust","");
        return ban.split(",");
    }

    public boolean isAntivpn(){
        return obj.getBoolean("antivpn",false);
    }

    public boolean isQuery(){
        return obj.getBoolean("query",false);
    }

    public String getLanguage(){
        return obj.getString("language","en");
    }

    public boolean isEnableantirush(){
        return obj.getBoolean("enable-antirush",false);
    }

    public LocalTime getAntirushtime() {
        return LocalTime.parse(obj.getString("antirushtime","10:00"));
    }

    public boolean isAntigrief(){
        return obj.getBoolean("antigrief",false);
    }

    public boolean isBlockdetect(){
        return obj.getBoolean("alert-blockdetect",false);
    }

    public boolean isAlertdeposit(){
        return obj.getBoolean("alertdeposit",false);
    }

    public boolean isExplimit(){
        return obj.getBoolean("explimit",false);
    }

    public boolean isLogging(){
        return obj.getBoolean("logging",false);
    }

    public boolean isUpdate(){
        return obj.getBoolean("update",true);
    }

    public boolean isLevelupalarm(){
        return obj.getBoolean("levelupalarm",false);
    }

    public int getAlarmlevel(){
        return obj.getInt("alarm-minimal-level",20);
    }

    public boolean isSqlite(){
        return obj.getBoolean("sqlite",true);
    }

    public boolean isLoginenable(){
        return obj.getBoolean("loginenable",false);
    }

    public String getPasswordmethod(){
        return obj.getString("loginmethod","password");
    }

    public boolean isValidconnect(){
        return obj.getBoolean("validconnect",false);
    }

    public String getDiscordToken(){
        return obj.getString("discord-token","Put your discord bot token here");
    }

    public Long getDiscordGuild(){
        return obj.getLong("discord-guild",0L);
    }

    public Long getDiscordRoom(){
        return obj.getLong("discord-room",0L);
    }

    public String getDiscordLink(){
        return obj.getString("discord-link","Put your discord invite link here");
    }

    public String getDiscordRole(){
        return obj.getString("discord-register-role","none");
    }

    public String getDiscordPrefix(){
        return obj.getString("discord-command-prefix","!");
    }

    public String getDBurl(){
        if(isSqlite()){
            return "jdbc:sqlite:"+Core.settings.getDataDirectory().child("mods/Essentials/data/player.sqlite3");
        } else {
            return obj.getString("dburl","none");
        }
    }

    public String getDBid(){
        return obj.getString("dbid","none");
    }

    public String getDBpw(){
        return obj.getString("dbpw","none");
    }

    public boolean isEnableTranslate(){
        return obj.getBoolean("enable-translate",false);
    }

    public String getClientId(){
        return obj.getString("clientId","none");
    }

    public String getClientSecret(){
        return obj.getString("clientSecret","none");
    }

    public boolean isDebug(){
        return obj.getBoolean("debug",false);
    }

    public int getSavetime(){
        return obj.getInt("savetime",10);
    }

    public boolean isEnableRollback(){
        return obj.getBoolean("enable-rollback",false);
    }

    public int getSlotnumber(){
        return obj.getInt("slotnumber",1000);
    }

    public boolean isAutodifficulty(){
        return obj.getBoolean("auto-difficulty",false);
    }

    public int getEasy(){
        return obj.getInt("easy",2);
    }

    public int getNormal(){
        return obj.getInt("normal",4);
    }

    public int getHard(){
        return obj.getInt("hard",6);
    }

    public int getInsane(){
        return obj.getInt("insane",10);
    }

    public int getSpawnlimit(){
        return obj.getInt("spawnlimit",30);
    }

    public String getPrefix(){
        return obj.getString("prefix","[green][Essentials] []");
    }

    public String getServername(){
        return PluginConfig.getString("servername","Essentials server");
    }

    public String getEventport(){
        return obj.getString("event-port","8000-8050");
    }

    private void validfile(){
        final String path = "configs";
        File jarFile;
        try {
            jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            nlog("warn","The plugin file or folder path is invalid. Please check your server path!");
            net.dispose();
            Core.app.exit();
            return;
        }

        if (jarFile.isFile()) {
            try {
                final JarFile jar = new JarFile(jarFile);
                final Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    String name = entries.nextElement().getName();
                    if (name.startsWith(path + "/")) {
                        if (!name.equals(path + "/")) {
                            if(!Core.settings.getDataDirectory().child("mods/Essentials/" + name.replaceFirst("configs/", "")).exists()) {
                                if (!name.contains(".")) {
                                    Core.settings.getDataDirectory().child("mods/Essentials/" + name.replaceFirst("configs/", "")).mkdirs();
                                    continue;
                                }
                                InputStream reader = getClass().getResourceAsStream("/" + name);
                                Core.settings.getDataDirectory().child("mods/Essentials/" + name.replaceFirst("configs/", "")).write(reader, false);
                            }
                        }
                    }
                }
                jar.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}