@echo off
diff -Nru "decompiled_clean" "decompiled" > "diffs/com.megacrit.cardcrawl.diff"
sed -i "s/decompiled_clean/decompiled/g" "diffs/com.megacrit.cardcrawl.diff"