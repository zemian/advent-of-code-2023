Java Project to solve https://adventofcode.com/ puzzles

## How to Compile and Run

These code are tested with JDK 21 (though JDK17 should work as well.).

There is no build script for this project. Each puzzle is runnable from command line.
First compile all the source files, then simply run it like this:

    javac -d out src/aoc2023/*.java
    java -cp src:out aoc2023.Day1
    java -cp src:out aoc2023.Day1b
