#!/usr/bin/env python
import sys
import pandas as pd
import os
import matplotlib.pyplot as plt

if __name__=="__main__":
    target = sys.argv[1]
    if target == 'time':
        f = './time.csv'
        scatter = False
        lines = True
        title = 'White Wine: SA(1E11/.995), GA(200/100/10), RHC, Backprop Time'
    else:
        f = './optimal.csv'
        scatter = False
        lines = True
        title = 'White Wine: SA(1E11/.995), GA(200/100/10), RHC, Backprop Correct'

    pd.set_option('display.max_row', 1000)
    pd.set_option('display.max_columns', 50)
    df = pd.read_csv(f)
    plt.figure(figsize=(10, 8))
    algos = { 'RHC': { 'marker': 'x', 'color': 'Red'},
              'GA': { 'marker': '1', 'color': 'Orchid'},
              'Backprop': { 'marker': '*', 'color': 'Lime'},
              'SA': { 'marker': '^', 'color': 'Turquoise'},
            }
    xval = 'iterations'
    yval = target
    for algorithm in df['algorithm'].unique():
        for iter in df['iterations'].unique():
            df.ix[(df.algorithm == algorithm) & (df.iterations == iter), target] = df.ix[(df.algorithm == algorithm) & (df.iterations == iter)][target].mean()

        if lines is True:
            plt.plot(df[xval][df['algorithm'] == algorithm],
                     df[yval][df['algorithm'] == algorithm],
                     color=algos[algorithm]['color'],
                     label=algorithm)
        if scatter is True:
            plt.scatter(df[xval][df['algorithm'] == algorithm],
                        df[yval][df['algorithm'] == algorithm],
                        marker=algos[algorithm]['marker'],
                        color=algos[algorithm]['color'],
                        alpha=0.7,
                        s=50,
                        label=algorithm)
    plt.title(title)
    plt.ylabel(yval)
    plt.xlabel(xval)
    plt.legend(loc='auto',prop={'size':10})
    plt.xlim([0, max(df[xval])+10])
    plt.ylim([min(df[yval])-5, max(df[yval])+10])
    plt.show()
