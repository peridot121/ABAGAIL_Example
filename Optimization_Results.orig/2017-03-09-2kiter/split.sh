#!/bin/bash
file="$1"
dir="$2"

for i in "GA" "SA" "RHC" ; do
  for j in "test" "train" ; do
    grep $i $file | grep $j | cut -d ',' -f3- > ${dir}/${j}-${i}.csv
  done
done

sed 's/train,RHC/train-RHC/g' $file | sed 's/train,SA/train-SA/g'|sed 's/train,GA/train-GA/g' |sed 's/test,RHC/test-RHC/g' | sed 's/test,SA/test-SA/g'|sed 's/test,GA/test-GA/g' > altered.csv
