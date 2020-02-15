package essentials.core;

import arc.Events;
import mindustry.content.Blocks;
import mindustry.entities.type.Player;
import mindustry.game.EventType.BlockBuildEndEvent;
import mindustry.game.EventType.BuildSelectEvent;
import mindustry.game.EventType.Trigger;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.world.Block;
import mindustry.world.Tile;

import java.util.ArrayList;
import java.util.TimerTask;

import static essentials.Global.nlog;
import static essentials.Main.timer;
import static essentials.core.PlayerDB.PlayerData;
import static essentials.core.PlayerDB.Players;
import static mindustry.Vars.world;

public class Anti {
    ArrayList<Tile> conveyor = new ArrayList<>();

    public Anti(){
        Events.on(BlockBuildEndEvent.class, e->{
            PlayerData data = PlayerData(e.player.uuid);
            data.grief_build_count++;
            data.grief_tilelist.add(e.tile);

            // 필터 아트 감지
            int sorter_count = 0;
            for(Tile t : data.grief_tilelist) if(t.block() == Blocks.sorter || t.block() == Blocks.invertedSorter) sorter_count++;
            if(sorter_count > 10){
                for(Tile t : data.grief_tilelist){
                    if(t.block() == Blocks.sorter || t.block() == Blocks.invertedSorter){
                        Call.onDeconstructFinish(t,Blocks.air,e.player.id);
                    }
                }
            }
            if(data.grief_build_count > 30) nlog("log", data.name+" 가 블럭을 빛의 속도로 건설하고 있습니다.");
        });

        Events.on(BuildSelectEvent.class, e->{
            if (e.builder instanceof Player && e.builder.buildRequest() != null && !e.builder.buildRequest().block.name.matches(".*build.*") && e.tile.block() != Blocks.air) {
                PlayerData data = PlayerData(((Player) e.builder).uuid);
                data.grief_destory_count++;
                if (data.grief_destory_count > 30) nlog("log", data.name + " 가 블럭을 빛의 속도로 파괴하고 있습니다.");
            }
        });

        Events.on(Trigger.class, e->{
            for(int x=0;x<world.width();x++){
                for(int y=0;y<world.height();y++){
                    Block block = world.tile(x,y).block();
                    if(block == Blocks.conveyor || block == Blocks.armoredConveyor || block == Blocks.titaniumConveyor){
                        boolean already = false;
                        for(Tile tile : conveyor){
                            if(tile == world.tile(x,y)){
                                already = true;
                                break;
                            }
                        }
                        if(!already) conveyor.add(world.tile(x, y));
                        for(Tile tile : conveyor){
                            if(tile.block().rotate != block.rotate){
                                Call.onConstructFinish(tile,block,0,tile.rotation(), Team.sharded,false);
                            }
                        }

                    }
                }
            }
        });

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                for(PlayerData p : Players){
                    if(p.grief_destory_count > 0) p.grief_destory_count--;
                    if(p.grief_build_count > 0) p.grief_build_count--;
                }
            }
        };
        timer.scheduleAtFixedRate(task,1000,1000);
    }
}
