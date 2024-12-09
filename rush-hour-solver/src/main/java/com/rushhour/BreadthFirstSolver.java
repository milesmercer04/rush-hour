package com.rushhour;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class BreadthFirstSolver implements Solver {
  Board initialState;
  HashSet<Board> visited;
  Deque<Board> queue;
  HashMap<Board, Board> priorState;

  public BreadthFirstSolver(Board initialState) {
    if (initialState == null) {
      throw new IllegalArgumentException("Initial state cannot be null");
    }
    if (!initialState.validateCars()) {
      throw new IllegalArgumentException("Initial state contains a positional conflict");
    }
    this.initialState = initialState;
    this.visited = new HashSet<>();
    visited.add(initialState);
    this.queue = new LinkedList<>();
    queue.add(initialState);
    this.priorState = new HashMap<>();
    priorState.put(initialState, null);
  }

  @Override
  public List<Board> solveProblem() {
    int numberOfCars = initialState.numberOfCars();

    // Perform bfs until the stack is empty or until a win state is found
    Board u = null;
    Board v;
    boolean solutionFound = false;
    while (queue.peekFirst() != null && !solutionFound) {
      u = queue.removeFirst();
      if (u.solutionFound()) {
        solutionFound = true;
      } else {
        for (int i = 0; i < numberOfCars; i++) {
          v = u.tryMove(i, true);
          if (v != null && !visited.contains(v)) {
            visited.add(v);
            queue.addLast(v);
            priorState.put(v, u);
          }
          v = u.tryMove(i, false);
          if (v != null && !visited.contains(v)) {
            visited.add(v);
            queue.addLast(v);
            priorState.put(v, u);
          }
        }
      }
    }

    // If a solution wasn't found, return an empty list
    if (!solutionFound) {
      return new LinkedList<>();
    }

    // Solution was found, find the certificate from the search tree priorState and the Board
    // variable u
    LinkedList<Board> solution = new LinkedList<>();
    solution.addFirst(u);
    while ((u = priorState.get(solution.getFirst())) != null) {
      solution.addFirst(u);
    }
    return solution;
  }
}