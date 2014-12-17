Sanara
======

simple languages to produce graphics

Examples
--------

    circle

will produce a red circle with 50px radius. This is the dafault values. You can change the default values by passing them.

    circle[50, "green"]

This will change the defaults to your value.

---------------------------------

    VStack[20](circle[50, "green"], circle)

This wil stack the two circles vertically with 20px gap.

