package com.rushhour;

import java.util.List;

public interface Solver {
    /**
     * Given a board representing the initial state of a rush hour puzzle, solve the puzzle,
     * returning a list of board states from the initial state to the end state as a certificate
     * @param initialState The starting state of the rush hour puzzle
     * @return A valid solution to the rush hour puzzle, as a list of Board objects from the
     * initial state to a win state, or an empty list if the puzzle isn't solvable
     */
    List<Board> solveProblem();
}