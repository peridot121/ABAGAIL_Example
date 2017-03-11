#!/bin/bash
# problem,algorithm,N,iterations,run,function-count,optimal,time-elapsed

file="$1"
algorithms='GA RHC SA'

if [ $# -ne 1 ] ; then
  echo "Must provide filename"
  exit 1
fi

for algorithm in $algorithms ; do
    echo "value,iterations" > ${algorithm}-N.csv
    echo "value,iterations" > ${algorithm}-Optimal.csv
    grep $algorithm $file | awk -F ',' '{print $3","$4}'>>${algorithm}-N.csv
    grep $algorithm $file | awk -F ',' '{print $7","$4}'>>${algorithm}-Optimal.csv
done

#for algorithm in $algorithms ; do
#    echo "optimal,iterations" > ${algorithm}-optimal.csv
#    grep $algorithm $file | awk -F ',' '{print $7","$4}'>>${algorithm}-optimal.csv
#done
