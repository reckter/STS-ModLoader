#!/bin/bash
# Author: github.com/reckter (Hannes Güdelhöfer)

if [ ! -d "decompiled_clean" ]; then
  echo You have to run './tools/unix/prepare_diff.sh' first
  exit
fi

# create a diff, remove all lines with "Only in"
diff -ru decompiled_clean/ decompiled/ \
    | grep -v '^Only in' \
    | sed "s/decompiled_clean/decompiled/g" \
    > diffs/com.megacrit.cardcrawl.diff

