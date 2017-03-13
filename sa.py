#!/usr/bin/env python
import sys
t = float(sys.argv[1])
c = float(sys.argv[2])
print "Temperature is: %s"%t
print "Cooling factor is: %s"%c
ct = 0

while (t > 1):
    t=t*c
    ct = ct + 1

print "Temperature reached 1 after %s iterations"%ct

