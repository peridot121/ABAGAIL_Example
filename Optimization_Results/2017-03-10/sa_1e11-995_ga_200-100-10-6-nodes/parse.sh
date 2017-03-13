#!/bin/bash

algorithms='GA RHC SA Backprop'
targets='time optimal'

for target in $targets ; do
    echo "algorithm,iterations,${target}" > ${target}.csv
    for algorithm in $algorithms ; do
        if [ "$target" == "time" ] ; then
            grep $algorithm final_results.csv| awk '{print $1,$7,$5}'|sort -n -k2,2|tr " " ",">>${target}.csv
        else #optimal
            grep $algorithm final_results.csv| awk '{print $1,$7,$4}'|sort -n -k2,2|tr " " ",">>${target}.csv
        fi
    done
done
