sokoban-searcher
================


TODO
================

Use only hashes to compare states
A collision is very unlikely and there are speed advantages to not 
saving states and not making the explicit "equals" calculations.

Tunnel makros
Check if both squares diagonally in front of the pushed box are
walls/dead. If that is true bot before and after the push, repeat
the push and increase the depth of the node.

True heuristics
Take push dynamics into account when calculating the distance
to push all boxes to all goals. Some goals may be unreachable.