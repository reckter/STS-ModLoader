@echo off
java -jar "tools/cfr_0_124.jar" --comments false --showversion false --caseinsensitivefs true --outputdir decompiled --jarfilter com.megacrit.cardcrawl.* "desktop-1.0.jar"