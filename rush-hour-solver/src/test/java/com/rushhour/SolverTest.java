package com.rushhour;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public abstract class SolverTest {
  // Factory method to be implemented by subclasses for specific solvers
  protected abstract Solver createSolver(Board initialState);

  @Test
  void initialStateIsASolutionTest() {
    ArrayList<Car> cars = new ArrayList<>();
    cars.add(new Car(5, 2, 2, true));
    Board initialState = new Board(cars);
    Solver solver = createSolver(initialState);
    List<Board> solution = solver.solveProblem();
    assertNotNull(solution);
    assertEquals(1, solution.size());
    Board state = solution.get(0);
    assertTrue(state.validateCars());
    assertEquals(initialState, state);
  }

  @Test
  void targetCarOneSpaceAwayFromExitTest() {
    Board initialState = new Board(List.of(new Car(4, 2, 2, true)));
    Solver solver = createSolver(initialState);
    List<Board> solution = solver.solveProblem();
    assertNotNull(solution);
    assertEquals(2, solution.size());
    Board state1 = solution.get(0);
    assertEquals(initialState, state1);
    Board state2 = solution.get(1);
    assertTrue(state2.solutionFound());
    assertEquals(1, state2.distanceFrom(state1));
  }

  @Test
  void boardContainsSimpleObstacleTest() {
    ArrayList<Car> cars = new ArrayList<>();
    cars.add(new Car(4, 2, 2, true));
    cars.add(new Car(5, 3, 2, false));
    Board initialState = new Board(cars);
    Solver solver = createSolver(initialState);
    List<Board> solution = solver.solveProblem();
    assertNotNull(solution);
    assertNotEquals(0, solution.size());
    Board state = solution.get(0);
    assertEquals(initialState, state);
    assertTrue(solution.get(solution.size() - 1).solutionFound());
    for (int i = 1; i < solution.size(); i++) {
      assertTrue(solution.get(i).validateCars());
      assertEquals(1, solution.get(i).distanceFrom(solution.get(i - 1)));
    }
  }

  @Test
  void primaryAndSecondaryObstacleTest() {
    ArrayList<Car> cars = new ArrayList<>();
    cars.add(new Car(4, 2, 2, true));
    cars.add(new Car(5, 4, 3, false));
    cars.add(new Car(5, 5, 2, true));
    Board initialState = new Board(cars);
    Solver solver = createSolver(initialState);
    List<Board> solution = solver.solveProblem();
    assertNotEquals(0, solution.size());
    assertEquals(initialState, solution.get(0));
    assertTrue(solution.get(solution.size() - 1).solutionFound());
    for (int i = 1; i < solution.size(); i++) {
      assertTrue(solution.get(i).validateCars());
      assertEquals(1, solution.get(i).distanceFrom(solution.get(i - 1)));
    }
  }

  @Test
  void twoSecondaryObstaclesTest() {
    ArrayList<Car> cars = new ArrayList<>();
    cars.add(new Car(4, 2, 2, true));
    cars.add(new Car(5, 3, 2, false));
    cars.add(new Car(5, 1, 2, true));
    cars.add(new Car(5, 4, 2, true));
    Board initialState = new Board(cars);
    Solver solver = createSolver(initialState);
    List<Board> solution = solver.solveProblem();
    assertNotEquals(0, solution.size());
    assertEquals(initialState, solution.get(0));
    assertTrue(solution.get(solution.size() - 1).solutionFound());
    for (int i = 1; i < solution.size(); i++) {
      assertTrue(solution.get(i).validateCars());
      assertEquals(1, solution.get(i).distanceFrom(solution.get(i - 1)));
    }
  }

  @Test
  void oneOptionVerticalTruckTest() {
    ArrayList<Car> cars = new ArrayList<>();
    cars.add(new Car(4, 2, 2, true));
    cars.add(new Car(5, 1, 2, false));
    cars.add(new Car(5, 4, 3, false));
    cars.add(new Car(5, 5, 2, true));
    Board initialState = new Board(cars);
    Solver solver = createSolver(initialState);
    List<Board> solution = solver.solveProblem();
    assertNotEquals(0, solution.size());
    assertEquals(initialState, solution.get(0));
    assertTrue(solution.get(solution.size() - 1).solutionFound());
    for (int i = 1; i < solution.size(); i++) {
      assertTrue(solution.get(i).validateCars());
      assertEquals(1, solution.get(i).distanceFrom(solution.get(i - 1)));
    }
  }

  @Test
  void simpleImpossibleProblemTest() {
    ArrayList<Car> cars = new ArrayList<>();
    cars.add(new Car(1, 2, 2, true));
    cars.add(new Car(5, 2, 3, false));
    cars.add(new Car(5, 5, 3, false));
    Board initialState = new Board(cars);
    Solver solver = createSolver(initialState);
    List<Board> solution = solver.solveProblem();
    assertEquals(0, solution.size());
  }

  @Test
  void targetCarIsAnObstacleTest() {
    ArrayList<Car> cars = new ArrayList<>();
    cars.add(new Car(1, 2, 2, true));
    cars.add(new Car(0, 1, 2, false));
    cars.add(new Car(2, 0, 2, true));
    cars.add(new Car(5, 0, 3, true));
    cars.add(new Car(5, 2, 2, false));
    cars.add(new Car(5, 5, 3, false));
    Board initialState = new Board(cars);
    Solver solver = createSolver(initialState);
    List<Board> solution = solver.solveProblem();
    assertNotEquals(0, solution.size());
    assertEquals(initialState, solution.get(0));
    assertTrue(solution.get(solution.size() - 1).solutionFound());
    for (int i = 1; i < solution.size(); i++) {
      assertTrue(solution.get(i).validateCars());
      assertEquals(1, solution.get(i).distanceFrom(solution.get(i - 1)));
    }
  }

  @Test
  void wiggleRoomImpossiblePuzzleTest() {
    ArrayList<Car> cars = new ArrayList<>();
    cars.add(new Car(2, 2, 2, true));
    cars.add(new Car(0, 2, 3, false));
    cars.add(new Car(5, 0, 3, true));
    cars.add(new Car(3, 2, 2, false));
    cars.add(new Car(2, 3, 3, true));
    cars.add(new Car(5, 3, 3, true));
    Board initialState = new Board(cars);
    Solver solver = createSolver(initialState);
    List<Board> solution = solver.solveProblem();
    assertEquals(0, solution.size());
  }

  @Test
  void oneObstacleRemovableImpossiblePuzzleTest() {
    ArrayList<Car> cars = new ArrayList<>();
    cars.add(new Car(1, 2, 2, true));
    cars.add(new Car(2, 2, 3, false));
    cars.add(new Car(4, 2, 3, false));
    cars.add(new Car(1, 5, 2, true));
    cars.add(new Car(4, 5, 3, true));
    Board initialState = new Board(cars);
    Solver solver = createSolver(initialState);
    List<Board> solution = solver.solveProblem();
    assertEquals(0, solution.size());
  }

}