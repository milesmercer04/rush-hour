package com.rushhour;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

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
    // Find the set of feasible winning states
    HashSet<Board> winStates = feasibleWinStates();

    // Check if the initial state is a win state
    if (winStates.contains(initialState)) {
      return List.of(initialState);
    }

    // Set of states visited from the initial state of the problem
    HashSet<Board> visitedFromStart = new HashSet<>();
    visitedFromStart.add(initialState);

    // Set of states visited from a winning state of the game
    HashSet<Board> visitedFromWinState = new HashSet<>();
    visitedFromWinState.addAll(winStates);

    // Map from board states to distances from initial state
    HashMap<Board, Integer> distanceFromStart = new HashMap<>();
    distanceFromStart.put(initialState, 0);

    // Map from board states to distances from nearest win state
    HashMap<Board, Integer> distanceFromWinState = new HashMap<>();
    for (Board w : winStates) {
      distanceFromWinState.put(w, 0);
    }

    // Map from board states to forward heuristic values
    HashMap<Board, Integer> forwardHeuristic = new HashMap<>();
    forwardHeuristic.put(initialState, initialState.minimumDistanceFrom(winStates));

    // Map from board states to backward heuristic values
    HashMap<Board, Integer> backwardHeuristic = new HashMap<>();
    for (Board w : winStates) {
      backwardHeuristic.put(w, w.distanceFrom(initialState));
    }

    // Map from states to their parents from initial state
    HashMap<Board, Board> parentFromStart = new HashMap<>();
    parentFromStart.put(initialState, null);

    // Map from states to their parents from win state
    HashMap<Board, Board> parentFromWinState = new HashMap<>();
    for (Board w : winStates) {
      parentFromWinState.put(w, null);
    }

    // Set of states reachable from initial state
    HashSet<Board> reachableFromStart = new HashSet<>();
    int numberOfCars = initialState.numberOfCars();
    Board newState = null;
    for (int i = 0; i < numberOfCars; i++) {
      newState = initialState.tryMove(i, true);
      if (newState != null && !visitedFromStart.contains(newState)) {
        reachableFromStart.add(newState);
        distanceFromStart.put(newState, 1);
        forwardHeuristic.put(newState, newState.minimumDistanceFrom(winStates));
        parentFromStart.put(newState, initialState);
      }
      newState = initialState.tryMove(i, false);
      if (newState != null && !visitedFromStart.contains(newState)) {
        reachableFromStart.add(newState);
        distanceFromStart.put(newState, 1);
        forwardHeuristic.put(newState, newState.minimumDistanceFrom(winStates));
        parentFromStart.put(newState, initialState);
      }
    }

    // Set of states reachable from a win state
    HashSet<Board> reachableFromWinState = new HashSet<>();
    for (Board w : winStates) {
      for (int i = 0; i < numberOfCars; i++) {
        newState = w.tryMove(i, true);
        if (newState != null && !visitedFromWinState.contains(newState)) {
          reachableFromWinState.add(newState);
          distanceFromWinState.put(newState, 1);
          backwardHeuristic.put(newState, newState.distanceFrom(initialState));
          parentFromWinState.put(newState, w);
        }
        newState = w.tryMove(i, false);
        if (newState != null && !visitedFromWinState.contains(newState)) {
          reachableFromWinState.add(newState);
          distanceFromWinState.put(newState, 1);
          backwardHeuristic.put(newState, newState.distanceFrom(initialState));
          parentFromWinState.put(newState, w);
        }
      }
    }

    // Comparator of reachable board states in forward direction
    Comparator<Board> forwardBoardComparator = (Board a, Board b) -> {
      if (a.equals(b)) {
        return 0;
      }
      Integer distanceA = distanceFromStart.get(a);
      Integer distanceB = distanceFromStart.get(b);
      Integer heuristicA = forwardHeuristic.get(a);
      if (heuristicA == null) {
        heuristicA = a.minimumDistanceFrom(winStates);
        forwardHeuristic.put(a, heuristicA);
      }
      Integer heuristicB = forwardHeuristic.get(b);
      if (heuristicB == null) {
        heuristicB = b.minimumDistanceFrom(winStates);
        forwardHeuristic.put(b, heuristicB);
      }
      if (distanceA == null) {
        if (distanceB == null) {
          return 0;
        } else {
          return 1;
        }
      } else {
        if (distanceB == null) {
          return -1;
        }
      }
      return Integer.compare(distanceA + heuristicA, distanceB + heuristicB);
    };

    // Comparator of reachable board states in backward direction
    Comparator<Board> backwardBoardComparator = (Board a, Board b) -> {
      if (a.equals(b)) {
        return 0;
      }
      Integer distanceA = distanceFromWinState.get(a);
      Integer distanceB = distanceFromWinState.get(b);
      Integer heuristicA = backwardHeuristic.get(a);
      if (heuristicA == null) {
        heuristicA = a.distanceFrom(initialState);
      }
      Integer heuristicB = backwardHeuristic.get(b);
      if (heuristicB == null) {
        heuristicB = b.distanceFrom(initialState);
      }
      if (distanceA == null) {
        if (distanceB == null) {
          return 0;
        } else {
          return 1;
        }
      } else {
        if (distanceB == null) {
          return -1;
        }
      }
      return Integer.compare(distanceA + heuristicA, distanceB + heuristicB);
    };

    // Put all states reachable from initial state into a priority queue based on forward comparator
    PriorityQueue<Board> forwardReachableQueue = new PriorityQueue<>(forwardBoardComparator);
    for (Board b : reachableFromStart) {
      forwardReachableQueue.add(b);
    }

    // Put all states reachable from a win state into a priority queue based on backward comparator
    PriorityQueue<Board> backwardReachableQueue = new PriorityQueue<>(backwardBoardComparator);
    for (Board b : reachableFromWinState) {
      backwardReachableQueue.add(b);
    }

    // Explore new board states until a state has been visited in both directions or one side runs
    // out of options
    boolean bridgeFound = false;
    Board reachableFromNewState;
    while (forwardReachableQueue.peek() != null && backwardReachableQueue.peek() != null) {
      // Work from side with less reachable states
      if (reachableFromStart.size() < reachableFromWinState.size()) {
        // Searching in forward direction
        newState = forwardReachableQueue.poll();
        visitedFromStart.add(newState);
        reachableFromStart.remove(newState);
        // Check if the new state has also been visited from a win state
        if (visitedFromWinState.contains(newState)) {
          bridgeFound = true;
          break;
        }
        // Find all new states reachable from the added state
        for (int i = 0; i < numberOfCars; i++) {
          reachableFromNewState = newState.tryMove(i, true);
          if (reachableFromNewState != null && !visitedFromStart.contains(reachableFromNewState) &&
              !reachableFromStart.contains(reachableFromNewState)) {
            reachableFromStart.add(reachableFromNewState);
            distanceFromStart.put(reachableFromNewState, distanceFromStart.get(newState) + 1);
            forwardHeuristic.put(reachableFromNewState, reachableFromNewState.minimumDistanceFrom(winStates));
            parentFromStart.put(reachableFromNewState, newState);
            forwardReachableQueue.add(reachableFromNewState);
          }
          reachableFromNewState = newState.tryMove(i, false);
          if (reachableFromNewState != null && !visitedFromStart.contains(reachableFromNewState) &&
              !reachableFromStart.contains(reachableFromNewState)) {
            reachableFromStart.add(reachableFromNewState);
            distanceFromStart.put(reachableFromNewState, distanceFromStart.get(newState) + 1);
            forwardHeuristic.put(reachableFromNewState, reachableFromNewState.minimumDistanceFrom(winStates));
            parentFromStart.put(reachableFromNewState, newState);
            forwardReachableQueue.add(reachableFromNewState);
          }
        }
      } else {
        // Searching in backward direction
        newState = backwardReachableQueue.poll();
        visitedFromWinState.add(newState);
        reachableFromWinState.remove(newState);
        // Check if the new state has also been visited from the initial state
        if (visitedFromStart.contains(newState)) {
          bridgeFound = true;
          break;
        }
        // Find all new states reachable from the added state
        for (int i = 0; i < numberOfCars; i++) {
          reachableFromNewState = newState.tryMove(i, true);
          if (reachableFromNewState != null && !visitedFromWinState.contains(reachableFromNewState) &&
              !reachableFromWinState.contains(reachableFromNewState)) {
            reachableFromWinState.add(reachableFromNewState);
            distanceFromWinState.put(reachableFromNewState, distanceFromWinState.get(newState) + 1);
            backwardHeuristic.put(reachableFromNewState, reachableFromNewState.distanceFrom(initialState));
            parentFromWinState.put(reachableFromNewState, newState);
            backwardReachableQueue.add(reachableFromNewState);
          }
          reachableFromNewState = newState.tryMove(i, false);
          if (reachableFromNewState != null && !visitedFromWinState.contains(reachableFromNewState) &&
              !reachableFromWinState.contains(reachableFromNewState)) {
            reachableFromWinState.add(reachableFromNewState);
            distanceFromWinState.put(reachableFromNewState, distanceFromWinState.get(newState) + 1);
            backwardHeuristic.put(reachableFromNewState, reachableFromNewState.distanceFrom(initialState));
            parentFromWinState.put(reachableFromNewState, newState);
            backwardReachableQueue.add(reachableFromNewState);
          }
        }
      }
    }

    // If no bridge was found between the forward and backward searches, no solution exists
    if (!bridgeFound) {
      return new LinkedList<>();
    }

    // Build the solution from the two search trees
    LinkedList<Board> solution = new LinkedList<>();
    Board currentState;
    solution.add(newState);
    while ((currentState = parentFromStart.get(solution.getFirst())) != null) {
      solution.addFirst(currentState);
    }
    while ((currentState = parentFromWinState.get(solution.getLast())) != null) {
      solution.addLast(currentState);
    }
    return solution;
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
      boolean isHorizontal = newCar.isHorizontal();
      // Iterate through all other cars to calculate front and back padding of new car
      int frontPadding = 0;
      int backPadding = 0;
      for (Car c : initialState.cars()) {
        if (c.equals(newCar)) {
          continue;
        }
        if (isHorizontal && c.isHorizontal() && newCar.yPosition() == c.yPosition()) {
          if (c.xPosition() > newCar.xPosition()) {
            frontPadding += c.length();
          } else {
            backPadding += c.length();
          }
        } else if (!isHorizontal && !c.isHorizontal() && newCar.xPosition() == c.xPosition()) {
          if (c.yPosition() > newCar.yPosition()) {
            frontPadding += c.length();
          } else {
            backPadding += c.length();
          }
        }
      }
      // Calculate wiggle room based on car's length, front padding, and back padding
      int wiggleRoom = N - length - frontPadding - backPadding;
      if (newCar.isHorizontal()) {
        newCar.changeXPosition(backPadding + length - 1);
      } else {
        newCar.changeYPosition(backPadding + length - 1);
      }
      for (int d = 0; d <= wiggleRoom; d++) {
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