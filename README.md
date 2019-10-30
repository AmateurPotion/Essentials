# Essentials
Add more commands to the server.  

I'm getting a lot of suggestions.  
Please submit your idea to this repository issues or Mindustry official discord!

## Installation

Put this plugin in the ``<server folder location>/config/mods`` folder.

## 6.0 version plans
- [x] Make RPG using player DB
  - [x] Lock blocks
- [ ] Monitoring resource consumption
  - [ ] Alarm when resource consumption is fast
  - [ ] Alarm when resource bank is full
- [ ] AI
  - [ ] Pathfinding
  - [ ] OreFlow
  - [ ] Player
- [ ] Improved detection of griefing
- [ ] Make lobby features
  - [ ] Show all server players
    - [ ] Message block
    - [ ] Server information
  - [ ] Show client server players
    - [ ] Message block
    - [ ] Block drawing
- [x] monitoring message block
  - [ ] Core block
  - [x] Power node
- [ ] PvP mode rule
  - [ ] Anti coal in mech
- [ ] Chat feature upgrade
  - [ ] Blocking bad words
  - [ ] Nickname prefix
  - [ ] Remove translate before message
  - [ ] PvP Team chat without command

## Client commands

| Command | Parameter | Description |
|:---|:---|:--- |
| ch | &lt;message&gt; | Send chat to another server () <br> You must modify the settings in ``config/plugins/Essentials/config.txt`` |
| color |  | Enable animated rainbow nickname. <br> Must enable 'realname' and can use admin. |
| difficulty | &lt;difficulty&gt; | Set server difficulty |
| getpos |  | Show your current position position |
| info |  | Show player information |
| kickall |  | Kick all players without you. |
| kill | &lt;name&gt; | Kill other players |
| login | &lt;account id&gt; &lt;password&gt; | Login to account. |
| me | &lt;msg&gt; | Show special chat format |
| motd |  | Show server motd <br> Can modify from ``config/plugins/Essentials/motd.txt`` |
| register | &lt;account id&gt; &lt;new password&gt; &lt;new password repeat&gt; | Register accoun<br>Example - ``/register test test123 test123`` |
| save |  | Save current map |
| spawn | &lt;mob name&gt; &lt;count&gt; &lt;team name&gt; [name] | Spawn mob in player location |
| status |  | Show currently server status (TPS, RAM, Players/ban count) |
| suicide |  | Kill yourself |
| tempban | &lt;name&gt; &lt;time&gt; | Temporarily ban player. time unit: 1 hours |
| time |  | Show server local time |
| tpp | &lt;name&gt; &lt;another player name&gt; | Teleport player to other players |
| tp | &lt;name&gt; | Teleport to players |
| tr |  | Enable/disable auto translate <br> Currently only support Korean to English. |
| vote | &lt;gameover/skipwave/kick/rollback&gt; [name] | Enable animated rainbow nickname. <br> Must enable 'realname' and can use admin. |

## Console commands

| Command | Parameter | Description |
|:---|:---|:---|
| admin | &lt;name&gt; | Set admin status to player |
| allinfo | &lt;name&gt; | Show player information |
| ban | &lt;uuid/name/ip&gt; &lt;username/ip/uuid&gt; | Ban a person. |
| bansync |  | Ban list synchronization from main server |
| blacklist | &lt;add/remove&gt; &lt;name&gt; | Block special nickname. |
| jumpreset |  | Clear a server-to-server jumping zone data. |
| kickall |  | Kick all players |
| kill | &lt;name&gt; | Kill target player |
| nick | &lt;name&gt; &lt;new_name&gt; | Show player information |
| pvp | &lt;anticoal/timer&gt; [time...] | Set gamerule with PvP mode |
| sync | &lt;name&gt; | Force sync request from the target player |
| team | &lt;name&gt; | Change target player team |
| tempban | &lt;uuid/name/ip&gt; &lt;username/ip/uuid&gt; | Temporarily ban player. time unit: 1 hours |