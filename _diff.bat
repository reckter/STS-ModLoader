@echo off

diff -u "decompiled_clean/core/CardCrawlGame.java" "decompiled/core/CardCrawlGame.java" > "diffs/com/megacrit/cardcrawl/core/CardCrawlGame.java.diff"
diff -u "decompiled_clean/dungeons/Exordium.java" "decompiled/dungeons/Exordium.java" > "diffs/com/megacrit/cardcrawl/dungeons/Exordium.java.diff"
diff -u "decompiled_clean/dungeons/TheBeyond.java" "decompiled/dungeons/TheBeyond.java" > "diffs/com/megacrit/cardcrawl/dungeons/TheBeyond.java.diff"
diff -u "decompiled_clean/dungeons/TheCity.java" "decompiled/dungeons/TheCity.java" > "diffs/com/megacrit/cardcrawl/dungeons/TheCity.java.diff"
diff -u "decompiled_clean/helpers/EventHelper.java" "decompiled/helpers/EventHelper.java" > "diffs/com/megacrit/cardcrawl/helpers/EventHelper.java.diff"
diff -u "decompiled_clean/helpers/MonsterHelper.java" "decompiled/helpers/MonsterHelper.java" > "diffs/com/megacrit/cardcrawl/helpers/MonsterHelper.java.diff"
diff -u "decompiled_clean/screens/charSelect/CharacterOption.java" "decompiled/screens/charSelect/CharacterOption.java" > "diffs/com/megacrit/cardcrawl/screens/charSelect/CharacterOption.java.diff"

for /R %%i in (*.diff) do sed -i "s/decompiled_clean/decompiled/g" %%i