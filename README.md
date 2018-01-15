# STS ModLoader #
Patch version: `[EARLY_ACCESS_011] (01-11-2018)`

Source version: `[EARLY_ACCESS_011] (01-11-2018)`

### Mod Loader Requirements ###
Java 8 (Java 9 DOES NOT WORK)

### Install instructions: ###
#### Basic (General Use): ####
1. Clone repo
2. Make a copy of desktop-1.0.jar from your Slay the Spire Steam folder in the root of the repo
3. Apply the JDF patch to it with JojoDiff (http://jojodiff.sourceforge.net/)
4. Run with _run.bat to get logger output

#### Advanced (Development): ####
1. Clone repo
2. Make a copy of 'desktop-1.0.jar' from your Slay the Spire Steam folder in the root of the repo
3. Get 'cfr_0_124.jar' and put it in the tools folder (http://www.benf.org/other/cfr/)
4. Run '_decompile.bat'. A folder named 'decompiled' should be created
5. Make a copy of the 'decompiled' folder created in step 5, name it 'decompiled_clean'
6. Run 'modloader/_compile_modloader.bat'
7. Copy the entire 'modloader' folder into the root of 'desktop-1.0.jar'
8. Run '_patch.bat' to automatically apply the diff files to the contents of 'decompiled'
9. Compile the following, plus whatever other files you modify:
    * decompiled/core/CardCrawlGame.java
    * decompiled/dungeons/Exordium.java
    * decompiled/dungeons/TheBeyond.java
    * decompiled/dungeons/TheCity.java
    * decompiled/helpers/EventHelper.java
    * decompiled/helpers/MonsterHelper.java
    * decompiled/screens/charSelect/CharacterOption.java
10. Copy the compiled files from step 10 into the appropriate locations in 'desktop-1.0.jar'
11. Run with _run.bat to get logger output

## Toolchain Information ##
### Toolchain Requirements ###
Java 8 (Java 9 DOES NOT WORK)

### Compiler Instructions: ###
1. (If needed) Build the modloader package by running modloader/_compile_modloader.bat
2. Drag and drop a .java file onto _compile.bat

### Decompiler Instructions: ###
1. Get cfr_0_124.jar and put it in the tools folder (http://www.benf.org/other/cfr/)
2. Run _decompile.bat. A folder named 'decompiled' should be created
3. (OPTIONAL) Run _patch.bat to automatically apply diffs to the decompiled files

## Mod Package Structure ##
```
mods
|
+-- modpackage
|   |
|   +-- cards
|   |   |
|   |   +-- CardId.class   : Compiled card
|   |   +-- CardId.java    : Card source
|   |   \-- CardId.png     : Card image
|   |
|   +-- events
|   |   |
|   |   +-- EventId.class   : Compiled event
|   |   +-- EventId.java    : Event source
|   |   \-- EventId.png     : Event image
|   |   
|   +-- localization
|   |   |
|   |   +-- events.json     : Event strings
|   |   +-- keywords.json   : Keywords
|   |   \-- relics.json     : Relic strings
|   |   
|   +-- monsters
|   |   |
|   |   +-- MonsterId.class : Compiled monster
|   |   +-- MonsterId.java  : Monster source
|   |   \-- MonsterId.png   : Monster image
|   |
|   +-- relics
|   |   |
|   |   +-- RelicId.class   : Compiled relic
|   |   +-- RelicId.java    : Relic source
|   |   \-- RelicId.png     : Relic image
|   |
|   +-- ironclad.json       : Ironclad start modifications
|   +-- mod.json            : Mod information, custom IDs, event/encounter setup
|   \-- silent.json         : Silent start modifications
|
+-- modpackage2
|   ...
\
```
    
## JSON Structure ##
#### mod.json ####
Weight should be 0.0 on boss encounters, can be any float on other encounters
```json
{
    "modName": "",
    "modPackage": "",
    "modDescription": "",
    "modAuthor": "",
    "modVersion": "",
    "customCardIds": [
        "",
        ""
    ],
    "customEventIds": [
        "",
        ""
    ],
    "customMonsterIds": [
        "",
        ""
    ],
    "customRelicIds": [
        "",
        ""
    ],
    "customEvents": [{
        "id": "",
        "eventType": "EVENT/SHRINE",
        "floor": "EXORDIUM/CITY/BEYOND"
    }],
    "customEncounters": [{
        "id": "",
        "floor": "EXORDIUM/CITY/BEYOND",
        "group": "WEAK/STRONG/ELITE/BOSS",
        "weight": 1.0,
        "monsters": [
            "",
            ""
        ]
    }]
}
```

#### ironclad.json & silent.json ####
```json
{
    "addCards": [
        "",
        ""
    ],
    "removeCards": [
        "",
        ""
    ],
    "addRelics": [
        "",
        ""
    ],
    "removeRelics": [
        "",
        ""
    ]
}
```

#### localization/events.json ####
```json
{
    "EventID": {
        "NAME": "",
        "DESCRIPTIONS": [
            "",
            ""
        ],
        "OPTIONS": [
            "",
            ""
        ]
    }
}
```

#### localization/keywords.json ####
```json
{
    "": "",
    "": ""
}
```

#### localization/relics.json
```json
{
    "RelicID": {
        "NAME": "",
        "FLAVOR": "",
        "DESCRIPTIONS": [
            "",
            ""
        ]
    }
}
```