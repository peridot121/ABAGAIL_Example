#!/bin/bash
testfile="$1"
trainfile="$1"
algorithms='Backprop-test Backprop-train'

if [ $# -ne 2 ] ; then
  echo "Must provide filenames"
  exit 1
fi


for algorithm in $algorithms ; do
    echo "percent-correct,iterations" > ${algorithm}.csv
    grep $algorithm $file | awk '{print $4","$7}'>>${algorithm}.csv
done
