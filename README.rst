Sanara
======

Language to produce graphics based on external libraries.

Examples
--------

::

    circle

This will produce a red circle with 50px radius. This is the dafault values. You can change the default values by passing them.

::

    circle[50, "green"]

This will change the defaults to your value.

---------------------------------

::

    vStack[20](circle[50, "green"], circle)

This will stack the two circles vertically with 20px gap.

