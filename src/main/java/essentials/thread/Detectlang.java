package essentials.thread;

import io.anuke.arc.util.Log;
import io.anuke.mindustry.core.NetClient;
import io.anuke.mindustry.entities.type.Player;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static essentials.EssentialPlayer.getData;

public class Detectlang {
    private static JSONObject lang;

    public static void detectlang(Player player, String message) {
        String clientId1 = "Ujx3Ysdxfg7FY2wQn2ES";
        String clientSecret1 = "iHAb6PF3SK";
        try {
            String query = URLEncoder.encode(message, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/papago/detectLangs";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", clientId1);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret1);

            String postParams = "query=" + query;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            JSONTokener result = new JSONTokener(response.toString());
            lang = new JSONObject(result);
            translate(player, lang, message);
        } catch (Exception e) {
            Log.err(e);
        }
    }
    public static void translate(Player player, JSONObject lang, String message){
        JSONObject db = getData(player.uuid);

        String clientId = "RNOXzFalw7FMFjBe2mbq";
        String clientSecret = "6k0TWLFmPN";
        try {
            String text = URLEncoder.encode(message, "UTF-8");
            Log.info(text);
            String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

            String postParams = "source="+lang.get("langCode")+"&target="+db.get("language")+"&text=" + text;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            JSONTokener parser = new JSONTokener(response.toString());
            JSONObject object = new JSONObject(parser);
            JSONObject v1 = (JSONObject) object.get("message");
            JSONObject v2 = (JSONObject) v1.get("result");
            String v3 = String.valueOf(v2.get("translatedText"));
            Log.info(v3);
            player.sendMessage("["+ NetClient.colorizeName(player.id, player.name)+"[white]: [#F5FF6B]" + v3);
        } catch (Exception f) {
            f.getStackTrace();
        }
    }
}