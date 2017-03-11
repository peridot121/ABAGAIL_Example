#!/usr/bin/env python
import sys
import pandas as pd
import os
import matplotlib.pyplot as plt

def make_df(attribute=['percent-correct'], algorithms=['GA','RHC','SA','Backprop'], indexes=[ 5, 10, 20, 30, 40, 50, 100, 200, 500, 1000, 1500]):
    frame = pd.DataFrame(index=indexes, columns=algorithms)

    for algorithm in algorithms:
        df_temp = pd.read_csv("./%s.csv"%algorithm, index_col='iterations')
        for it in indexes:
            frame.loc[it][algorithm] = df_temp.ix[it]['percent-correct'].mean()
    return frame
                  

def plot_data(df, title, xlabel, ylabel):
    ax = df.plot(title=title, fontsize=12)
    ax.set_xlabel(xlabel)
    ax.set_ylabel(ylabel)
    plt.show()

if __name__=="__main__":
    if len(sys.argv) < 2:
        title = 'White Wine NN'
    else:
        title = sys.argv[1]
    frame = make_df()
    plot_data(frame, title, 'Iterations', 'Percent Correct')
