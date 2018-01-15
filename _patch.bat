@echo off
python -m patch diffs/com/megacrit/cardcrawl/core/CardCrawlGame.java.diff
python -m patch diffs/com/megacrit/cardcrawl/dungeons/Exordium.java.diff
python -m patch diffs/com/megacrit/cardcrawl/dungeons/TheBeyond.java.diff
python -m patch diffs/com/megacrit/cardcrawl/dungeons/TheCity.java.diff
python -m patch diffs/com/megacrit/cardcrawl/helpers/EventHelper.java.diff
python -m patch diffs/com/megacrit/cardcrawl/helpers/MonsterHelper.java.diff
python -m patch diffs/com/megacrit/cardcrawl/screens/charSelect/CharacterOption.java.diff
pause