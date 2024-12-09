package com.rushhour;

public class BreadthFirstSolverTest extends SolverTest {
  @Override
  public Solver createSolver(Board initialState) {
    return new BreadthFirstSolver(initialState);
  }
}