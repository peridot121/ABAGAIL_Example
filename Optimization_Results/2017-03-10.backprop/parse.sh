#!/bin/bash
file="$1"
algorithms='GA RHC SA Backprop'

if [ $# -ne 1 ] ; then
  echo "Must provide filename"
  exit 1
fi

for algorithm in $algorithms ; do
    echo "percent-correct,iterations" > ${algorithm}.csv
    grep $algorithm $file | awk '{print $4","$7}'>>${algorithm}.csv
done
