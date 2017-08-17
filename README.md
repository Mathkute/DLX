# DLX
Implementation in Java of the DLX algorithm as described by Donald Knuth. 
Included copy of the article that describes it, and two test files.

- usage: cat test-file.txt | java DLX
- Shapes: represented choosing a number of contiguous squares of a nx.ny board.

- test file: first line: <nshapes> <nrows> <nx> <ny>
             secong line: <nshapes <symbol> letters> <nx.ny <figures>> 
             nrows lines:
                 each line nshapes+nx*ny 0/1 entries:
                     one of first nshapes entries is 1 for the shape the others 0,
                     then a number of ones to represent the position of the squares on the board
