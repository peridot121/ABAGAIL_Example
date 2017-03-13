#!/bin/bash

algorithms='GAHighPop GALowPop RHC SA55 SA75 SA95 MIMIC50 MIMIC200'
targets='time optimal functions'

# problem, algorithm, N, iterations, run, function, optimal, time
for target in $targets ; do
    echo "algorithm,iterations,${target}" > ${target}.csv
    for algorithm in $algorithms ; do
        # problem,algorithm,N,iterations,run,function-count,optimal,time-elapsed
        if [ "$target" == "time" ] ; then
            grep $algorithm final_results.csv| awk -F ',' '{print $2,$4,$8}'|sort -n -k2,2|tr " " ",">>${target}.csv
        elif [ "$target" == "functions" ] ; then
            grep $algorithm final_results.csv| awk -F ',' '{print $2,$4,$6}'|sort -n -k2,2|tr " " ",">>${target}.csv
        else
            grep $algorithm final_results.csv| awk -F ',' '{print $2,$4,$7}'|sort -n -k2,2|tr " " ",">>${target}.csv
        fi
    done
done
