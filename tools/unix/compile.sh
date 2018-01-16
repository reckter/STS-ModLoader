#!/bin/bash

set -o errexit

coreFiles=$(cat << EOM
    ./decompiled/core/CardCrawlGame.java
    ./decompiled/dungeons/Exordium.java
    ./decompiled/dungeons/TheBeyond.java
    ./decompiled/dungeons/TheCity.java
    ./decompiled/helpers/EventHelper.java
    ./decompiled/helpers/MonsterHelper.java
    ./decompiled/screens/charSelect/CharacterOption.java
EOM)

modFiles=$(find ./modloader -name "*.java")

rm -rf compiled/patched
mkdir compiled/patched
javac -d compiled/patched -classpath "compiled/desktop-1.0.jar:mods" $coreFiles $modFiles

unzip -u compiled/desktop-1.0.jar -d compiled/unpacked

rsync -a  compiled/patched/ compiled/unpacked/

pushd compiled/unpacked/
zip -r -0 -Du ../desktop-1.0-modded.jar *
popd
