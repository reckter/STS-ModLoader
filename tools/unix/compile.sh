#!/bin/bash
# Author: github.com/reckter (Hannes Güdelhöfer)

# so we exit on any failure
set -o errexit

# which filed to compile. Fix with a diff somehow? (todo)
coreFiles=$(cat << EOM
    ./decompiled/com/megacrit/cardcrawl/core/CardCrawlGame.java
    ./decompiled/com/megacrit/cardcrawl/dungeons/Exordium.java
    ./decompiled/com/megacrit/cardcrawl/dungeons/TheBeyond.java
    ./decompiled/com/megacrit/cardcrawl/dungeons/TheCity.java
    ./decompiled/com/megacrit/cardcrawl/helpers/EventHelper.java
    ./decompiled/com/megacrit/cardcrawl/helpers/MonsterHelper.java
    ./decompiled/com/megacrit/cardcrawl/screens/charSelect/CharacterOption.java
EOM)

# all the files in the modloader directory
modFiles=$(find ./modloader -name "*.java")

# remove old patched files
rm -rf compiled/patched

# redo the directory
mkdir compiled/patched

# compille all the changed files and all modloader files
javac -d compiled/patched -classpath "compiled/desktop-1.0.jar:mods" $coreFiles $modFiles

# unziped the patched game
unzip -u compiled/desktop-1.0.jar -d compiled/unpacked

# injecting our compiled files
rsync -a  compiled/patched/ compiled/unpacked/

# zipping the folder with the injected class files
pushd compiled/unpacked/
zip -r -0 -Du ../desktop-1.0-modded.jar *
popd
