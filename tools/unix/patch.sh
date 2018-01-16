#!/bin/bash

patches=$(find ./diffs -name "*.diff")

while read line
do
    cat $line | patch -p0
done <<< "$patches"
