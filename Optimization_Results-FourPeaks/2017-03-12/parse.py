#!/usr/bin/env python
import sys
import pandas as pd
import os
import matplotlib.pyplot as plt

def make_df(attribute=['value'], algorithms=['GA-N','RHC-N','SA-N', 'GA-Optimal', 'RHC-Optimal', 'SA-Optimal'], indexes=[ 10, 25, 50, 100, 200, 500, 1000, 5000 ]):
    frame = pd.DataFrame(index=indexes, columns=algorithms)

    for algorithm in algorithms:
        df_temp = pd.read_csv("./%s.csv"%algorithm, index_col='iterations')
        for it in indexes:
        #    frame.loc[it][algorithm] = df_temp.ix[it]['value'].mean()
            frame = frame.join(df_temp)
    return frame
                  

def plot_data(df, title, xlabel, ylabel):
    ax = df.scatter(title=title, fontsize=12)
    ax.set_xlabel(xlabel)
    ax.set_ylabel(ylabel)
    plt.show()

if __name__=="__main__":
    if len(sys.argv) < 2:
        title = 'Four peaks'
    else:
        title = sys.argv[1]
    frame = make_df()
    plot_data(frame, title, 'Iterations', 'Optimal/N')
