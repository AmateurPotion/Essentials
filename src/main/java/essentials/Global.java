package essentials;

import arc.Core;
import arc.util.Log;
import essentials.utils.Bundle;
import essentials.utils.Config;
import essentials.utils.Permission;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.entities.type.Player;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.world.Tile;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.jsoup.Jsoup;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ThreadFactory;

import static essentials.core.Log.writelog;
import static essentials.core.PlayerDB.getData;
import static mindustry.Vars.playerGroup;
import static mindustry.Vars.world;

public class Global {
    public static Config config = new Config();

    // 일반 기록
    public static void log(String value){
        Log.info("[Essential] "+nbundle(value));
    }
    
    public static void log(String value, Object... parameter){
        Log.info("[Essential] "+nbundle(value, parameter));
    }

    public static void nlog(Object... value){
        Log.info("[Essential] "+ Arrays.toString(value).replace("[", "").replace("]", ""));
    }

    // 경고
    public static void warn(String value){
        Log.warn("[Essential] "+nbundle(value));
    }

    public static void nwarn(String value){
        Log.warn("[Essential] "+value);
    }

    // 오류
    public static void err(String value){
        Log.err("[Essential] "+nbundle(value));
    }

    public static void err(String value, Object... parameter){
        Log.err("[Essential] "+nbundle(value, parameter));
    }

    public static void nerr(String value){
        Log.err("[Essential] "+value);
    }

    // 디버그
    public static void debug(String value){
        Log.debug(value);
    }

    // 서버
    public static void server(String value){
        Log.info("[EssentialServer] "+nbundle(value));
    }

    public static void server(String value, Object... parameter){
        Log.info("[EssentialServer] "+nbundle(value, parameter));
    }

    // 클라이언트
    public static void client(String value){
        Log.info("[EssentialClient] "+nbundle(value));
    }

    public static void client(String value, Object... parameter){
        Log.info("[EssentialClient] "+nbundle(value, parameter));
    }

    public static void nclient(Object... parameter){
        Log.info("[EssentialClient] "+Arrays.toString(parameter));
    }

    // 설정
    public static void config(String value){
        Log.info("[EssentialConfig] "+nbundle(value));
    }

    // PlayerDB
    public static void playernormal(String value){
        Log.info("[EssentialPlayer] "+nbundle(value));
    }

    public static void playernormal(String value, Object... parameter){
        Log.info("[EssentialPlayer] "+nbundle(value, parameter));
    }

    public static void playerlog(String plain){
        Log.info("[EssentialPlayer] "+plain);
    }

    // PlayerDB 경고
    public static void playerwarn(String value){
        Log.warn("[EssentialPlayer] "+nbundle(value));
    }

    // PlayerDB 오류
    public static void playererror(String value, boolean... bool){
        if(bool[0]){
            Log.err("[EssentialPlayer] " + value);
        } else {
            Log.err("[EssentialPlayer] " + nbundle(value));
        }
    }

    // 코어가 없는 팀 찾기
    public static Team getTeamNoCore(Player player){
        int index = player.getTeam().id+1;
        while (index != player.getTeam().id){
            if (index >= Team.all().length){
                index = 0;
            }
            if (Vars.state.teams.get(Team.all()[index]).cores.isEmpty()){
                return Team.all()[index]; //return a team without a core
            }
            index++;
        }
        return player.getTeam();
    }

    public static void setTeam(Player player){
        if (Vars.state.rules.pvp) {
            int index = player.getTeam().id + 1;
            while (index != player.getTeam().id) {
                if (index >= Team.all().length) {
                    index = 0;
                }
                if (!Vars.state.teams.get(Team.all()[index]).cores.isEmpty()) {
                    player.setTeam(Team.all()[index]);
                    break;
                }
                index++;
            }
        } else {
            player.setTeam(Team.sharded);
        }
    }

    // 오류 메세지를 파일로 복사하거나 즉시 출력
    public static void printStackTrace(Throwable e) {
        if(!config.isDebug()){
            StringBuilder sb = new StringBuilder();
            try {
                sb.append(e.toString());
                sb.append("\n");
                StackTraceElement[] element = e.getStackTrace();
                for (StackTraceElement stackTraceElement : element) {
                    sb.append("\tat ");
                    sb.append(stackTraceElement.toString());
                    sb.append("\n");
                }
                sb.append("=================================================\n");
                String text = sb.toString();

                writelog("error", text);
                Global.nlog("Internal error! - "+e.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            e.printStackTrace();
        }
    }

    // 현재 시간출력
    public static String getTime(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm.ss", Locale.ENGLISH);
        return "[" + now.format(dateTimeFormatter) + "] ";
    }

    public static String getnTime(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm.ss", Locale.ENGLISH);
        return now.format(dateTimeFormatter);
    }

    // Bundle 파일에서 Essentials 문구를 포함시켜 출력
    public static String bundle(Player player, String value, Object... parameter) {
        if(isLogin(player)){
            JSONObject db = getData(player.uuid);
            Locale locale = new Locale(db.getString("language"));
            Bundle bundle = new Bundle(locale);
            return bundle.getBundle(value, parameter);
        } else {
            return "";
        }
    }

    public static String bundle(Player player, String value) {
        if(isLogin(player)){
            JSONObject db = getData(player.uuid);
            Locale locale = new Locale(db.getString("language"));
            Bundle bundle = new Bundle(locale);
            return bundle.getBundle(value);
        } else {
            return "";
        }
    }

    public static String bundle(String value, Object... paramter){
        Locale locale = new Locale(config.getLanguage());
        Bundle bundle = new Bundle(locale);
        return bundle.getBundle(value, paramter);
    }

    public static String bundle(String value){
        Locale locale = new Locale(config.getLanguage());
        Bundle bundle = new Bundle(locale);
        return bundle.getBundle(value);
    }

    // Bundle 파일에서 Essentials 문구 없이 출력
    public static String nbundle(Player player, String value, Object... paramter) {
        JSONObject db = getData(player.uuid);
        if(isLogin(player)){
            Locale locale = new Locale(db.getString("language"));
            Bundle bundle = new Bundle(locale);
            return bundle.getNormal(value, paramter);
        } else {
            return "";
        }
    }

    public static String nbundle(Player player, String value) {
        if(isLogin(player)){
            JSONObject db = getData(player.uuid);
            Locale locale = new Locale(db.getString("language"));
            Bundle bundle = new Bundle(locale);
            return bundle.getNormal(value);
        } else {
            return "";
        }
    }

    public static String nbundle(String language, String value) {
        Locale locale = new Locale(language);
        Bundle bundle = new Bundle(locale);
        return bundle.getNormal(value);
    }

    public static String nbundle(String value, Object... paramter){
        Locale locale = new Locale(config.getLanguage());
        Bundle bundle = new Bundle(locale);
        return bundle.getNormal(value, paramter);
    }

    public static String nbundle(String value){
        Locale locale = new Locale(config.getLanguage());
        Bundle bundle = new Bundle(locale);
        return bundle.getNormal(value);
    }

    // 숫자 카운트
    public static void setcount(Tile tile, int count){
        String[] pos = {"0,4","1,4","2,4","0,3","1,3","2,3","0,2","1,2","2,2","0,1","1,1","2,1","0,0","1,0","2,0"};
        int[] zero = {1,1,1,1,0,1,1,0,1,1,0,1,1,1,1};
        int[] one = {0,1,0,1,1,0,0,1,0,0,1,0,1,1,1};
        int[] two = {1,1,1,0,0,1,1,1,1,1,0,0,1,1,1};
        int[] three = {1,1,1,0,0,1,1,1,1,0,0,1,1,1,1};
        int[] four = {1,0,1,1,0,1,1,1,1,0,0,1,0,0,1};
        int[] five = {1,1,1,1,0,0,1,1,1,0,0,1,1,1,1};
        int[] six = {1,1,1,1,0,0,1,1,1,1,0,1,1,1,1};
        int[] seven = {1,1,1,1,0,1,0,0,1,0,0,1,0,0,1};
        int[] eight = {1,1,1,1,0,1,1,1,1,1,0,1,1,1,1};
        int[] nine = {1,1,1,1,0,1,1,1,1,0,0,1,1,1,1};

        switch(count) {
            case 0:
                for(int a=0;a<15;a++){
                    String position = pos[a];
                    String[] data = position.split(",");
                    int x = Integer.parseInt(data[0]);
                    int y = Integer.parseInt(data[1]);
                    Tile target = world.tile(tile.x, tile.y);
                    if(zero[a] == 1) {
                        if(world.tile(target.x+x, target.y+y).block() != Blocks.plastaniumWall){
                            Call.onConstructFinish(world.tile(target.x+x, target.y+y), Blocks.plastaniumWall, 0, (byte) 0, Team.sharded, true);
                        }
                    } else if(zero[a] == 0){
                        if(world.tile(target.x+x, target.y+y).block() == Blocks.plastaniumWall){
                            Call.onDeconstructFinish(world.tile(target.x+x,target.y+y), Blocks.air, 0);
                        }
                    }
                }
                break;
            case 1:
                for(int a=0;a<15;a++){
                    String position = pos[a];
                    String[] data = position.split(",");
                    int x = Integer.parseInt(data[0]);
                    int y = Integer.parseInt(data[1]);
                    Tile target = world.tile(tile.x, tile.y);
                    if(one[a] == 1){
                        if(world.tile(target.x+x, target.y+y).block() != Blocks.plastaniumWall){
                            Call.onConstructFinish(world.tile(target.x+x, target.y+y), Blocks.plastaniumWall, 0, (byte) 0, Team.sharded, true);
                        }
                    } else if(one[a] == 0){
                        if(world.tile(target.x+x, target.y+y).block() == Blocks.plastaniumWall){
                            Call.onDeconstructFinish(world.tile(target.x+x,target.y+y), Blocks.air, 0);
                        }
                    }
                }
                break;
            case 2:
                for(int a=0;a<15;a++){
                    String position = pos[a];
                    String[] data = position.split(",");
                    int x = Integer.parseInt(data[0]);
                    int y = Integer.parseInt(data[1]);
                    Tile target = world.tile(tile.x, tile.y);
                    if(two[a] == 1){
                        if(world.tile(target.x+x, target.y+y).block() != Blocks.plastaniumWall){
                            Call.onConstructFinish(world.tile(target.x+x, target.y+y), Blocks.plastaniumWall, 0, (byte) 0, Team.sharded, true);
                        }
                    } else if(two[a] == 0){
                        if(world.tile(target.x+x, target.y+y).block() == Blocks.plastaniumWall){
                            Call.onDeconstructFinish(world.tile(target.x+x,target.y+y), Blocks.air, 0);
                        }
                    }
                }
                break;
            case 3:
                for(int a=0;a<15;a++){
                    String position = pos[a];
                    String[] data = position.split(",");
                    int x = Integer.parseInt(data[0]);
                    int y = Integer.parseInt(data[1]);
                    Tile target = world.tile(tile.x, tile.y);
                    if(three[a] == 1){
                        if(world.tile(target.x+x, target.y+y).block() != Blocks.plastaniumWall){
                            Call.onConstructFinish(world.tile(target.x+x, target.y+y), Blocks.plastaniumWall, 0, (byte) 0, Team.sharded, true);
                        }
                    } else if(three[a] == 0){
                        if(world.tile(target.x+x, target.y+y).block() == Blocks.plastaniumWall){
                            Call.onDeconstructFinish(world.tile(target.x+x,target.y+y), Blocks.air, 0);
                        }
                    }
                }
                break;
            case 4:
                for(int a=0;a<15;a++){
                    String position = pos[a];
                    String[] data = position.split(",");
                    int x = Integer.parseInt(data[0]);
                    int y = Integer.parseInt(data[1]);
                    Tile target = world.tile(tile.x, tile.y);
                    if(four[a] == 1){
                        if(world.tile(target.x+x, target.y+y).block() != Blocks.plastaniumWall){
                            Call.onConstructFinish(world.tile(target.x+x, target.y+y), Blocks.plastaniumWall, 0, (byte) 0, Team.sharded, true);
                        }
                    } else if(four[a] == 0){
                        if(world.tile(target.x+x, target.y+y).block() == Blocks.plastaniumWall){
                            Call.onDeconstructFinish(world.tile(target.x+x,target.y+y), Blocks.air, 0);
                        }
                    }
                }
                break;
            case 5:
                for(int a=0;a<15;a++){
                    String position = pos[a];
                    String[] data = position.split(",");
                    int x = Integer.parseInt(data[0]);
                    int y = Integer.parseInt(data[1]);
                    Tile target = world.tile(tile.x, tile.y);
                    if(five[a] == 1){
                        if(world.tile(target.x+x, target.y+y).block() != Blocks.plastaniumWall){
                            Call.onConstructFinish(world.tile(target.x+x, target.y+y), Blocks.plastaniumWall, 0, (byte) 0, Team.sharded, true);
                        }
                    } else if(five[a] == 0){
                        if(world.tile(target.x+x, target.y+y).block() == Blocks.plastaniumWall){
                            Call.onDeconstructFinish(world.tile(target.x+x,target.y+y), Blocks.air, 0);
                        }
                    }
                }
                break;
            case 6:
                for(int a=0;a<15;a++){
                    String position = pos[a];
                    String[] data = position.split(",");
                    int x = Integer.parseInt(data[0]);
                    int y = Integer.parseInt(data[1]);
                    Tile target = world.tile(tile.x, tile.y);
                    if(six[a] == 1){
                        if(world.tile(target.x+x, target.y+y).block() != Blocks.plastaniumWall){
                            Call.onConstructFinish(world.tile(target.x+x, target.y+y), Blocks.plastaniumWall, 0, (byte) 0, Team.sharded, true);
                        }
                    } else if(six[a] == 0){
                        if(world.tile(target.x+x, target.y+y).block() == Blocks.plastaniumWall){
                            Call.onDeconstructFinish(world.tile(target.x+x,target.y+y), Blocks.air, 0);
                        }
                    }
                }
                break;
            case 7:
                for(int a=0;a<15;a++){
                    String position = pos[a];
                    String[] data = position.split(",");
                    int x = Integer.parseInt(data[0]);
                    int y = Integer.parseInt(data[1]);
                    Tile target = world.tile(tile.x, tile.y);
                    if(seven[a] == 1){
                        if(world.tile(target.x+x, target.y+y).block() != Blocks.plastaniumWall){
                            Call.onConstructFinish(world.tile(target.x+x, target.y+y), Blocks.plastaniumWall, 0, (byte) 0, Team.sharded, true);
                        }
                    } else if(seven[a] == 0){
                        if(world.tile(target.x+x, target.y+y).block() == Blocks.plastaniumWall){
                            Call.onDeconstructFinish(world.tile(target.x+x,target.y+y), Blocks.air, 0);
                        }
                    }
                }
                break;
            case 8:
                for(int a=0;a<15;a++){
                    String position = pos[a];
                    String[] data = position.split(",");
                    int x = Integer.parseInt(data[0]);
                    int y = Integer.parseInt(data[1]);
                    Tile target = world.tile(tile.x, tile.y);
                    if(eight[a] == 1){
                        if(world.tile(target.x+x, target.y+y).block() != Blocks.plastaniumWall){
                            Call.onConstructFinish(world.tile(target.x+x, target.y+y), Blocks.plastaniumWall, 0, (byte) 0, Team.sharded, true);
                        }
                    } else if(eight[a] == 0){
                        if(world.tile(target.x+x, target.y+y).block() == Blocks.plastaniumWall){
                            Call.onDeconstructFinish(world.tile(target.x+x,target.y+y), Blocks.air, 0);
                        }
                    }
                }
                break;
            case 9:
                for(int a=0;a<15;a++){
                    String position = pos[a];
                    String[] data = position.split(",");
                    int x = Integer.parseInt(data[0]);
                    int y = Integer.parseInt(data[1]);
                    Tile target = world.tile(tile.x, tile.y);
                    if(nine[a] == 1){
                        if(world.tile(target.x+x, target.y+y).block() != Blocks.plastaniumWall){
                            Call.onConstructFinish(world.tile(target.x+x, target.y+y), Blocks.plastaniumWall, 0, (byte) 0, Team.sharded, true);
                        }
                    } else if(nine[a] == 0){
                        if(world.tile(target.x+x, target.y+y).block() == Blocks.plastaniumWall){
                            Call.onDeconstructFinish(world.tile(target.x+x,target.y+y), Blocks.air, 0);
                        }
                    }
                }
                break;
        }
    }

    // 각 언어별 motd
    public static String getmotd(Player player){
        JSONObject db = getData(player.uuid);
        if(Core.settings.getDataDirectory().child("mods/Essentials/motd/motd_"+db.getString("language")+".txt").exists()){
            return Core.settings.getDataDirectory().child("mods/Essentials/motd/motd_"+db.getString("language")+".txt").readString();
        } else {
            return Core.settings.getDataDirectory().child("mods/Essentials/motd/motd_en.txt").readString();
        }
    }

    // No 글자 표시
    public static void setno(Tile tile){
        String[] pos = {"0,4","1,4","2,4","0,3","1,3","2,3","0,2","1,2","2,2","0,1","1,1","2,1","0,0","1,0","2,0"};
        int[] n = {1,1,1,1,0,1,1,0,1,1,0,1,1,0,1};
        int[] o = {1,1,1,1,0,1,1,0,1,1,0,1,1,1,1};

        for(int a=0;a<15;a++) {
            String position = pos[a];
            String[] data = position.split(",");
            int x = Integer.parseInt(data[0]);
            int y = Integer.parseInt(data[1]);
            Tile target = world.tile(tile.x, tile.y);
            if(n[a] == 1) {
                if (world.tile(target.x + x, target.y + y).block() != Blocks.titaniumWall) {
                    Call.onConstructFinish(world.tile(target.x + x, target.y + y), Blocks.scrapWall, 0, (byte) 0, Team.sharded, true);
                }
            } else if(n[a] == 0){
                if(world.tile(target.x+x, target.y+y).block().solid){
                    Call.onDeconstructFinish(world.tile(target.x+x,target.y+y), Blocks.air, 0);
                }
            }
        }

        for(int a=0;a<15;a++) {
            String position = pos[a];
            String[] data = position.split(",");
            int x = Integer.parseInt(data[0]);
            int y = Integer.parseInt(data[1]);
            Tile target = world.tile(tile.x, tile.y);
            if(o[a] == 1) {
                if (world.tile(target.x + x, target.y + y).block() != Blocks.titaniumWall) {
                    Call.onConstructFinish(world.tile(target.x+4+x, target.y+y), Blocks.scrapWall, 0, (byte) 0, Team.sharded, true);
                }
            } else if(o[a] == 0){
                if(world.tile(target.x+x, target.y+y).block().solid){
                    Call.onDeconstructFinish(world.tile(target.x+4+x,target.y+y), Blocks.air, 0);
                }
            }
        }
    }

    // 모든 플레이어에게 메세지 표시
    public static void allsendMessage(String name){
        Thread t = new Thread(() -> {
            for (int i = 0; i < playerGroup.size(); i++) {
                Player other = playerGroup.all().get(i);
                other.sendMessage(bundle(other, name));
            }
        });
        t.start();
    }

    public static void allsendMessage(String name, Object... parameter){
        Thread t = new Thread(() -> {
            for (int i = 0; i < playerGroup.size(); i++) {
                Player other = playerGroup.all().get(i);
                if(other == null) return;
                other.sendMessage(bundle(other, name, parameter));
            }
        });
        t.start();
    }

    // 본인의 코어가 있는지 없는지 확인
    public static boolean isNocore(Player player){
        return Vars.state.teams.get(player.getTeam()).cores.isEmpty();
    }

    // 플레이어 지역 위치 확인
    public static JSONObject geolocation(Player player) {
        String ip = Vars.netServer.admins.getInfo(player.uuid).lastIP;
        JSONObject list = new JSONObject();

        try {
            String json = Jsoup.connect("http://ipapi.co/"+ip+"/json").ignoreContentType(true).execute().body();
            JSONObject result = new JSONObject(new JSONTokener(json));

            if (result.has("reserved")) {
                list.put("country", "Local IP");
                list.put("country_code", "LC");
                list.put("languages", "en");
            } else {
                String[] das = result.getString("languages").split(",");
                list.put("country", result.getString("country_name"));
                list.put("country_code", result.getString("country"));
                list.put("languages", das[0]);
            }
        } catch (IOException e) {
            printStackTrace(e);
            list.put("country", "invalid");
            list.put("country_code", "invalid");
            list.put("languages", "en");
        }

        return list;
    }

    // 로그인 유무 확인 (DB)
    public static boolean isLogin(Player player){
        JSONObject db = getData(player.uuid);
        if(db.toString().equals("{}") || player.uuid == null) return false;
        return db.getBoolean("connected");
    }

    // 비 로그인 유저 확인 (코어)
    public static boolean checklogin(Player player){
        if (Vars.state.teams.get(player.getTeam()).cores.isEmpty()) {
            player.sendMessage(bundle("not-login"));
            return false;
        } else {
            return true;
        }
    }

    // 권한 확인
    public static boolean checkperm(Player player, String command){
        if(isLogin(player) && checklogin(player)){
            JSONObject db = getData(player.uuid);
            String perm = db.getString("permission");
            int size = Permission.permission.getJSONObject(perm).getJSONArray("permission").length();
            for(int a=0;a<size;a++){
                String permlevel = Permission.permission.getJSONObject(perm).getJSONArray("permission").getString(a);
                if(permlevel.equals(command) || permlevel.equals("ALL")){
                    return true;
                }
            }
        }
        return false;
    }

    // 로그인 시간 확인
    public static boolean isLoginold(String date){
        try {
            // 플레이어 시간
            SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm.ss", Locale.ENGLISH);
            Calendar cal1 = Calendar.getInstance();
            Date d = format.parse(String.valueOf(date));
            cal1.setTime(d);
            // 로그인 만료시간 설정 (3시간)
            cal1.add(Calendar.HOUR, 3);

            // 서버 시간
            LocalDateTime now = LocalDateTime.now();
            Calendar cal2 = Calendar.getInstance();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm.ss", Locale.ENGLISH);
            Date d1 = format.parse(now.format(dateTimeFormatter));
            cal2.setTime(d1);

            return cal1.after(cal2);
        } catch (ParseException e) {
            printStackTrace(e);
            return true;
        }
    }

    // Thread name
    public static class threadname implements ThreadFactory {
        String name;
        int count = 0;
        public threadname(String name){
            this.name = name;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, name+"-" + ++count);
        }
    }

    public static byte[] encrypt(String data, SecretKeySpec spec, Cipher cipher) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, spec);
        return cipher.doFinal(data.getBytes());
    }

    public static byte[] decrypt(byte[] data, SecretKeySpec spec, Cipher cipher) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, spec);
        return cipher.doFinal(data);
    }
}
