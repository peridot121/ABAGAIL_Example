#!/usr/bin/env python
import sys
import pandas as pd
import os
import matplotlib.pyplot as plt


if __name__=="__main__":
    pd.set_option('display.max_row', 1000)
    pd.set_option('display.max_columns', 50)
    df = pd.read_csv("./results.csv")
    df_temp = (df['N'] - df['optimal']).abs()
    df_temp.rename(columns={"difference"})
    df = df.join(pd.DataFrame(df_temp, columns = {"difference"}))
    plt.figure(figsize=(10, 8))
    algos = { 'RHC': { 'marker': 'x', 'color': 'b'},
              'GA': { 'marker': 'o', 'color': 'r'},
              'SA': { 'marker': '^', 'color': 'g'},
            }
    xval = 'iterations'
    yval = 'difference'
    for algorithm in ['RHC', 'GA', 'SA']:
        #plt.plot(df[xval][df['algorithm'] == algorithm],
        #         df[yval][df['algorithm'] == algorithm],
        #         label=algorithm)
        plt.scatter(df[xval][df['algorithm'] == algorithm],
                    df[yval][df['algorithm'] == algorithm],
                    marker=algos[algorithm]['marker'],
                    color=algos[algorithm]['color'],
                    alpha=0.7,
                    s=50,
                    label=algorithm)
    plt.title('Four Peaks')
    plt.ylabel(yval)
    plt.xlabel(xval)
    plt.legend(loc='upper left')
    plt.xlim([0, max(df[xval])+10])
    plt.ylim([0, max(df[yval])+10])
    plt.show()
