#!/usr/bin/env python
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

pd.set_option('display.max_row', 1000)
pd.set_option('display.max_columns', 50)
df = pd.read_csv('https://raw.githubusercontent.com/chrisalbon/war_of_the_five_kings_dataset/master/5kings_battles_v1.csv')
df.head()
plt.figure(figsize=(10,8))

# Create a scatterplot of,
plt.scatter(df['attacker_size'][df['year'] == 298],
            # attacker size in year 298 as the y axis
            df['defender_size'][df['year'] == 298],
            # the marker as
            marker='x',
            # the color
            color='b',
            # the alpha
            alpha=0.7,
            # with size
            s = 124,
            # labelled this
            label='Year 298')

plt.scatter(df['attacker_size'][df['year'] == 299],
            # defender size in year 299 as the y axis
            df['defender_size'][df['year'] == 299],
            # the marker as
            marker='o',
            # the color
            color='r',
            # the alpha
            alpha=0.7,
            # with size
            s = 124,
            # labelled this
            label='Year 299')

plt.scatter(df['attacker_size'][df['year'] == 300],
            # defender size in year 300 as the y axis
            df['defender_size'][df['year'] == 300],
            # the marker as
            marker='^',
            # the color
            color='g',
            # the alpha
            alpha=0.7,
            # with size
            s = 124,
            # labelled this
            label='Year 300')

# Chart title
plt.title('Battles Of The War Of The Five Kings')

# y label
plt.ylabel('Defender Size')

# x label
plt.xlabel('Attacker Size')

# and a legend
plt.legend(loc='upper right')

# set the figure boundaries
plt.xlim([min(df['attacker_size'])-1000, max(df['attacker_size'])+1000])
plt.ylim([min(df['defender_size'])-1000, max(df['defender_size'])+1000])

plt.show()
