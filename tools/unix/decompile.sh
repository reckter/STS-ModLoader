#!/bin/bash

rm -rf compiled/unpacked
rm -rf compiled/package

unzip compiled/desktop-1.0.jar -d compiled/unpacked
mkdir -p compiled/package/com/megacrit/
mv compiled/unpacked/com/megacrit/cardcrawl/ compiled/package/com/megacrit/cardcrawl/

java -jar tools/fernflower.jar -bto=0 compiled/package/com/megacrit/cardcrawl decompiled
