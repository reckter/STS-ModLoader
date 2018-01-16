@echo off
diff -ru "decompiled_clean" "decompiled" | grep -v '^Only in' > "diffs/com.megacrit.cardcrawl.diff"
sed -i "s/decompiled_clean/decompiled/g" "diffs/com.megacrit.cardcrawl.diff"