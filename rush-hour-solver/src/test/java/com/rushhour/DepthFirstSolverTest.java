package com.rushhour;

public class DepthFirstSolverTest extends SolverTest {
  @Override
  protected Solver createSolver(Board initialState) {
    return new DepthFirstSolver(initialState);
  }
}