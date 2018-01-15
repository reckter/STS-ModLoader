# STS ModLoader #
Patch version: `[EARLY_ACCESS_011] (01-11-2018)`

Source version: `[EARLY_ACCESS_011] (01-11-2018)`

### Mod Loader Requirements ###
Java 8+

### Install instructions: ###
1. Clone repo
2. Make a copy of desktop-1.0.jar from your Slay the Spire Steam folder and do one of the following:
  * Apply the JDF patch to it with JojoDiff (http://jojodiff.sourceforge.net/)
  * Decompile and manually apply diffs or run _patch.bat to automatically apply them, then compile and put the resulting class files back into the JAR
3. Run with _run.bat to get logger output

## Toolchain Information ##
### Toolchain Requirements ###
Java 8+

Python 3.6+

### Toolchain Setup: ###
1. Run tools/setup.bat as admin to install required Python modules (patch).

### Compiler Instructions: ###
1. Drag and drop a .java file onto _compile.bat

### Decompiler Instructions: ###
1. Get cfr_0_124.jar and put it in the tools folder (http://www.benf.org/other/cfr/)
2. Create a folder named 'compiled' in the root and copy the contents of desktop-1.0.jar/com/megacrit/cardcrawl into it
3. Run _decompile.bat, this will take a few minutes. A decompiled folder should be created.
4. (OPTIONAL) Run _patch.bat to automatically apply diffs to the decompiled files

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