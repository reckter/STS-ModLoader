#!/bin/bash
java -jar ./tools/cfr_0_124.jar \
    --caseinsensitivefs true \
    --outputdir decompiled \
    --jarfilter com.megacrit.cardcrawl.* \
    compiled/desktop-1.0.jar

mv ./decompiled/com/megacrit/cardcrawl/* decompiled/


#rm -rf compiled/unpacked
#rm -rf compiled/package

#unzip -f compiled/desktop-1.0.jar -d compiled/unpacked
#mkdir -p compiled/package/com/megacrit/
#mv compiled/unpacked/com/megacrit/cardcrawl/ compiled/package/com/megacrit/cardcrawl/

#java -jar tools/fernflower.jar compiled/package/com/megacrit/cardcrawl decompiled
