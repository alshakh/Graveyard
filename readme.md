# Conway's Game of Life

My attempt to make a 2D game of life with OpenGL and C/C++. A really basic (and not tested) functions to read the format of [RLE files](http://www.conwaylife.com/wiki/Run_Length_Encoded).

When there are cells at the edge, they wrap around the grid.

# Instructions
```
- Click on cell ---> toggle life/death
- 'n'   -----------> next generation
- 'c'   -----------> clear life
- 'g'   -----------> hide/show grid
- 'j'   -----------> jump 100 generations
- 'p'   -----------> populate the grid randomly (about 1/10 of the grid will be live)
- '1'-'5' ---------> draw well known patterns (currently only 1 works)
			1 : glider gun and fish hook eating the gliders
```

**To change size of the grid, change** `#define LIMIT 50` **to whatever number you like** 

# License

[GPLv3](http://www.gnu.org/licenses/gpl-3.0.html)
