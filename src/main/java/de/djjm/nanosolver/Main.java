package de.djjm.nanosolver;

import de.djjm.nanosolver.matrix.NanoLine;
import de.djjm.nanosolver.matrix.Nonogram;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //Nonogram ng = new Nonogram(new Integer[][]{{1},{3},{2}},new Integer[][]{{1},{3},{2}});
        Nonogram ng = new Nonogram(
                new Integer[][]{
                        {1, 1, 1, 1},
                        {4},
                        {1, 4},
                        {2, 4},
                        {2, 1},
                        {2},
                        {3},
                        {1, 4, 3},
                        {6, 3},
                        {3, 1, 1, 1}
                }, new Integer[][]{
                {1, 3},
                {1, 1, 2},
                {1, 2, 3},
                {1, 2},
                {2, 2},
                {3, 3},
                {3},
                {4, 4},
                {7},
                {5}
        }
        );
        /*Nonogram ng = new Nonogram(
            new Integer[][]{
                {1,1},
                {3,1},
                {2,1},
                {3},
                {1}
            }, new Integer[][]{
                {2},
                {4},
                {1, 2},
                {1,1},
                {2}
            }
        );*/
        /*Nonogram ng = new Nonogram(
                new Integer[][]{
                        {0},
                        {3,3},
                        {10},
                        {7,2},
                        {7,2},
                        {6,3},
                        {8},
                        {6},
                        {4},
                        {2}
                }, new Integer[][]{
                {4},
                {6},
                {7},
                {8},
                {8},
                {8},
                {4,3},
                {2,3},
                {6},
                {4}
        }
        );*/
        Nonogram frankSternfeld = new Nonogram(
                new Integer[][]{
                        {10},
                        {2, 7},
                        {3, 6},
                        {2, 2, 2},
                        {1, 2, 2},
                        {2, 1, 1},
                        {3, 10, 2, 2},
                        {2, 11, 1, 2},
                        {1, 4, 2, 9, 1},
                        {1, 2, 2, 7, 2},
                        {1, 2, 1, 6, 1},
                        {1, 2, 5, 1},
                        {1, 3, 5, 1},
                        {1, 3, 5, 1},
                        {1, 1, 4, 1},
                        {1, 2, 4, 1},
                        {1, 5, 5, 2, 2},
                        {10, 8, 1, 2},
                        {4, 6, 3, 8},
                        {2, 16, 5, 3},
                        {1, 1, 2, 2, 3, 3, 2, 2},
                        {1, 4, 1, 5, 1, 1},
                        {1, 1, 2, 2},
                        {1, 1, 1, 2, 2},
                        {1, 1, 2, 4, 1},
                        {1, 1, 2, 6},
                        {2, 4, 4, 7},
                        {3, 2, 5, 12},
                        {2, 3, 4},
                        {1, 13, 2, 4},
                        {1, 1, 2, 2, 3},
                        {1, 9, 1, 4},
                        {2, 4, 1, 4},
                        {1, 2, 5},
                        {2, 2, 6},
                        {2, 2, 8},
                        {2, 2, 3, 5},
                        {2, 3, 6, 4},
                        {5, 4, 5, 5},
                        {6, 8, 5},
                        {6, 6, 6},
                        {6, 2, 6},
                        {6, 4, 2, 7},
                        {6, 6, 3, 6},
                        {6, 7, 3, 5}
                }, new Integer[][]{
                {11, 6},
                {2, 3, 7},
                {1, 11, 7},
                {2, 4, 7, 8},
                {2, 9, 2, 3, 8},
                {1, 6, 2, 1, 1, 2, 7},
                {2, 7, 4, 1, 1, 3},
                {1, 3, 4, 1, 2, 1, 3, 1},
                {1, 3, 5, 1, 2, 4, 2},
                {1, 2, 2, 4, 2, 1, 1, 3, 3},
                {2, 4, 2, 2, 2, 1, 1, 3, 3},
                {1, 3, 2, 1, 1, 2, 2, 3},
                {1, 2, 1, 1, 1, 2, 2, 3},
                {1, 2, 2, 1, 1, 2, 3, 2},
                {1, 3, 6, 2, 1, 2, 1, 2},
                {1, 3, 2, 3, 1, 1, 2, 2},
                {1, 3, 4, 2, 1, 1, 2, 2},
                {1, 1, 5, 2, 3, 3, 2},
                {2, 4, 2, 3, 1, 2, 5, 1},
                {2, 4, 2, 2, 1, 1, 1, 1, 3, 2},
                {4, 3, 2, 2, 2, 4, 2, 2},
                {2, 6, 2, 3, 4, 3, 3},
                {2, 13, 4, 4, 4},
                {2, 7, 3, 4, 5, 5},
                {2, 8, 2, 14, 6},
                {2, 27, 6},
                {2, 1, 1, 19},
                {4, 2, 5, 9},
                {4, 5, 6, 6},
                {15, 3}
        }
        );
        NanoLine frankSternfeldProblemLine = new NanoLine("I?#??###############??#####?##?I 2 16 5 3");
        NanoLine borderTestLine = new NanoLine("I   ?I 1");
        NanoLine anotherProblemline = new NanoLine("I??????????###########################?###### I 2 27 6");

        boolean calcNonogram = true;
        Nonogram nonogramToSolve = frankSternfeld;
        boolean calcNonoline = false;
        NanoLine nonolineToSolve = anotherProblemline;
        if (calcNonogram) {
            System.out.println("Nonogram");
            while (nonogramToSolve.solveStep()) {
                System.out.println(nonogramToSolve); //Problem in 9th iteration
            }
            System.out.println("----------------------------------");
            System.out.println(nonogramToSolve);
        }

        if (calcNonoline) {
            System.out.println("NonoLine");
            System.out.println(nonolineToSolve);
            while (nonolineToSolve.solveStep()) {
                System.out.println(nonolineToSolve);
            }
            System.out.println("----------------------------------");
            System.out.println(nonolineToSolve);
        }
    }
}