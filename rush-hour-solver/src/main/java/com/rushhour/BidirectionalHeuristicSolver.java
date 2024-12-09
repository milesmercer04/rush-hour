package com.rushhour;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class BidirectionalHeuristicSolver implements Solver {
  Board initialState;
  HashSet<Board> visited;

  public BidirectionalHeuristicSolver(Board initialState) {
    if (initialState == null) {
      throw new IllegalArgumentException("Initial state cannot be null");
    }
    if (!initialState.validateCars()) {
      throw new IllegalArgumentException("Initial state contains a positional conflict");
    }
    this.initialState = initialState;
    visited = new HashSet<>();
    visited.add(initialState);
  }

  @Override
  public List<Board> solveProblem() {
    return new ArrayList<>();
  }

  public HashSet<Board> feasibleWinStates() {
    int numberOfCars = initialState.numberOfCars();
    final int N = initialState.N();

    // Create an array of sets of board states to incrementally add new cars
    ArrayList<HashSet<Board>> incrementalWinStates = new ArrayList<>(numberOfCars);
    for (int i = 0; i < numberOfCars; i++) {
      incrementalWinStates.add(new HashSet<>());
    }

    // Add a board containing only the target car at the win state to the first set
    Car luckyTargetCar = new Car(initialState.cars().get(0));
    luckyTargetCar.reposition(initialState.exitXPosition(), initialState.exitYPosition());
    Board simpleWinState = new Board(initialState.N(), initialState.exitXPosition(),
                                     initialState.exitYPosition(), List.of(luckyTargetCar));
    incrementalWinStates.get(0).add(simpleWinState);

    // Incrementally add cars to the board, building on the prior set each iteration
    Car newCar;
    Board newBoard;
    int length;
    for (int i = 1; i < numberOfCars; i++) {
      newCar = new Car(initialState.cars().get(i));
      length = newCar.length();
      if (newCar.isHorizontal()) {
        newCar.changeXPosition(length - 1);
      } else {
        newCar.changeYPosition(length - 1);
      }
      for (int d = 0; d <= N - length; d++) {
        for (Board b : incrementalWinStates.get(i - 1)) {
          newBoard = b.tryAddCar(newCar);
          if (newBoard != null) {
            incrementalWinStates.get(i).add(newBoard);
          }
        }
        newCar.moveForward();
      }
    }

    // Ensure that each win state is reachable from a non-win state
    HashSet<Board> winStates = incrementalWinStates.get(numberOfCars - 1);
    boolean newStateFound;

    // Mark all win states at this stage as visited, even if unfeasible
    for (Board b : winStates) {
      visited.add(b);
    }
    // Must use iterator rather than foreach loop because we delete win states as we're traversing
    // them
    Iterator<Board> iterator = winStates.iterator();
    while (iterator.hasNext()) {
      Board b = iterator.next();
      newStateFound = false;
      for (int i = 0; i < numberOfCars; i++) {
        newBoard = b.tryMove(i, true);
        if (newBoard != null && !visited.contains(newBoard)) {
          newStateFound = true;
          break;
        }
        newBoard = b.tryMove(i, false);
        if (newBoard != null && !visited.contains(newBoard)) {
          newStateFound = true;
          break;
        }
      }
      if (!newStateFound) {
        iterator.remove();
      }
    }
    return winStates;
  }
}