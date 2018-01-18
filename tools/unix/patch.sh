#!/bin/bash
# Author: github.com/reckter (Hannes Güdelhöfer)

# patches the diff onto the decompiled folder
patch -p0 < ./diffs/com.megacrit.cardcrawl.diff
