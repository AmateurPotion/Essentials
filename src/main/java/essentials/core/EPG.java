package essentials.core;

import essentials.Global;
import essentials.utils.Config;
import io.anuke.arc.Core;
import io.anuke.arc.Events;
import io.anuke.mindustry.entities.type.Player;
import io.anuke.mindustry.game.EventType;
import io.anuke.mindustry.gen.Call;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

import static essentials.Global.nbundle;
import static essentials.core.PlayerDB.getData;

public class EPG {
    public Config config = new Config();
    
    public void main(){
        if(config.isExplimit()){
            Events.on(EventType.BuildSelectEvent.class, e -> {
                if(!e.breaking){
                    JSONObject db = getData(((Player)e.builder).uuid);
                    String name = e.tile.block().name;
                    int level = (int) db.get("level");
                    Yaml yaml = new Yaml();
                    Map<String, Object> obj = yaml.load(String.valueOf(Core.settings.getDataDirectory().child("mods/Essentials/BlockReqExp.txt").readString()));
                    int blockreqlevel = 100;
                    if(String.valueOf(obj.get(name)) != null) {
                        blockreqlevel = Integer.parseInt(String.valueOf(obj.get(name)));
                    } else if(e.tile.block().name.equals("air")){
                        Global.loge(nbundle("epg-block-not-valid", name));
                    } else {
                        return;
                    }

                    if(level < blockreqlevel){
                        Call.onDeconstructFinish(e.tile, e.tile.block(), ((Player)e.builder).id);
                        ((Player)e.builder).sendMessage(nbundle(((Player)e.builder), "epg-block-require", ((Player)e.builder).name, blockreqlevel));
                    }
                }
            });
        }
    }
}
