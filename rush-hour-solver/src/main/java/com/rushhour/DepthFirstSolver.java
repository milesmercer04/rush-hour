package com.rushhour;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DepthFirstSolver implements Solver {
  Board initialState;
  HashSet<Board> visited;
  HashMap<Board, Board> priorState;
  Deque<Board> stack;
  
  public DepthFirstSolver(Board initialState) {
    this.initialState = initialState;
    this.visited = new HashSet<>();
    this.visited.add(initialState);
    this.priorState.put(initialState, null);
    this.stack.addFirst(initialState);
  }

  @Override
  public List<Board> solveProblem() {
    int numberOfCars = initialState.numberOfCars();

    // Repeat until the stack is empty or until a win state is found
    Board u;
    Board v;
    boolean solutionFound = false;
    while (!solutionFound && stack.peek() != null) {
      u = stack.removeFirst();
      if (u.solutionFound()) {
        solutionFound = true;
        stack.addFirst(u);
      } else {
        // Try to move each car in the cars list one unit
        for (int i = 0; i < numberOfCars; i++) {
          v = u.tryMove(i, true); // Try to move the ith car forward, then backward
          if (v != null && !visited.contains(v)) {
            visited.add(v);
            priorState.put(v, u);
            stack.addFirst(u);
            stack.addFirst(v);
            break;
          }
          v = u.tryMove(i, false); // Movind forward didn't work, try moving backward
          if (v != null && !visited.contains(v)) {
            visited.add(v);
            priorState.put(v, u);
            stack.addFirst(u);
            stack.addFirst(v);
            break;
          }
        }
      }
    }

    // If a solution wasn't found, return an empty list
    if (!solutionFound) {
      return new ArrayList<>();
    }

    // Solution was found, find the certificate from the search tree priorState and return as a
    // list from the initial state to the end state
    int solutionLength = stack.size();
    ArrayList<Board> solution = new ArrayList<>(solutionLength);
    for (int i = 0; i < solutionLength; i++) {
      solution.add(null);
    }
    for (int i = solutionLength - 1; i >= 0; i--) {
      solution.set(i, stack.removeFirst());
    }
    return solution;
  }
}