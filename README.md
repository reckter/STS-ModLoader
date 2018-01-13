# STS ModLoader #
Patch version: `[EARLY_ACCESS_011] (01-11-2018)`

Source version: `[EARLY_ACCESS_011] (01-11-2018)`

## Requirements ##
Java 8+

## Install instructions: ##
1. Clone repo
2. Make a copy of desktop-1.0.jar from your Slay the Spire Steam folder and do one of the following: (MAKE SURE THE VERSION MATCHES PATCH VERSION ABOVE)
  * Apply the JDF patch to it with JojoDiff (http://jojodiff.sourceforge.net/)
  * Extact the classes there are diff files for, decompile (CFR 124), apply diffs, recompile, put back in the JAR
3. Run with "java -jar desktop-1.0.jar" to get logger output

## Mod Package Structure ##
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
    
## JSON Structure ##
#### mod.json ####
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
        "id": "TestSquare",
        "floor": "EXORDIUM/CITY/BEYOND",
        "group": "WEAK/STRONG/ELITE/BOSS",
        "weight": 1.0,
        "monsters": [
            "TestSquare"
        ]
    }]
}
```
Weight should be 0.0 on boss encounters, can be any float on other encounters

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