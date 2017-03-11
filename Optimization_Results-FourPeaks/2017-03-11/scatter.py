#!/usr/bin/env python
import sys
import pandas as pd
import os
import matplotlib.pyplot as plt


if __name__=="__main__":
    pd.set_option('display.max_row', 1000)
    pd.set_option('display.max_columns', 50)
    df = pd.read_csv("./results.csv")
    plt.figure(figsize=(10, 8))
    algos = { 'RHC': { 'marker': 'x', 'color': 'b'},
              'GA': { 'marker': 'o', 'color': 'r'},
              'SA': { 'marker': '^', 'color': 'g'},
            }
    for algorithm in ['RHC', 'GA', 'SA']:
        plt.scatter(df['N'][df['algorithm'] == algorithm],
                    df['optimal'][df['algorithm'] == algorithm],
                    marker=algos[algorithm]['marker'],
                    color=algos[algorithm]['color'],
                    alpha=0.7,
                    s=200,
                    label='RHC')
    plt.title('Four Peaks')
    plt.ylabel('optimal')
    plt.xlabel('N')
    plt.legend(loc='upper right')
    plt.xlim([min(df['optimal'])-200, max(df['optimal'])+200])
    plt.xlim([min(df['N'])-200, max(df['N'])+200])
    plt.show()
