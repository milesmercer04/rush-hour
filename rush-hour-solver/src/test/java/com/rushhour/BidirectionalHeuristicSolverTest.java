package com.rushhour;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class BidirectionalHeuristicSolverTest extends SolverTest {
  @Override
  public Solver createSolver(Board initialState) {
    return new BidirectionalHeuristicSolver(initialState);
  }

  @Test
  void targetCarAtExitFeasibleWinStatesTest() {
    ArrayList<Car> cars = new ArrayList<>();
    cars.add(new Car(5, 2, 2, true));
    Board initialState = new Board(cars);
    BidirectionalHeuristicSolver solver = new BidirectionalHeuristicSolver(initialState);
    HashSet<Board> s = solver.feasibleWinStates();
    assertEquals(1, s.size());
    assertTrue(s.contains(initialState));
  }

  @Test
  void onlyTargetCarFeasibleWinStatesTest() {
    ArrayList<Car> cars = new ArrayList<>();
    cars.add(new Car(1, 2, 2, true));
    Board initialState = new Board(cars);
    BidirectionalHeuristicSolver solver = new BidirectionalHeuristicSolver(initialState);
    HashSet<Board> s = solver.feasibleWinStates();
    assertEquals(1, s.size());
    cars.get(0).changeXPosition(5);
    assertTrue(s.contains(new Board(cars)));
  }

  @Test
  void targetCarWithOneObstacleFeasibleWinStatesTest() {
    ArrayList<Car> cars = new ArrayList<>();
    cars.add(new Car(4, 2, 2, true));
    cars.add(new Car(5, 3, 2, false));
    Board initialState = new Board(cars);
    BidirectionalHeuristicSolver solver = new BidirectionalHeuristicSolver(initialState);
    HashSet<Board> s = solver.feasibleWinStates();
    assertEquals(3, s.size());
    cars.get(0).changeXPosition(5);
    cars.get(1).changeYPosition(1);
    assertTrue(s.contains(new Board(cars)));
    cars.get(1).changeYPosition(4);
    assertTrue(s.contains(new Board(cars)));
    cars.get(1).moveForward();
    assertTrue(s.contains(new Board(cars)));
  }

  @Test
  void multipleWinningStatesButOnlyOneFeasibleWinStateTest() {
    ArrayList<Car> cars = new ArrayList<>();
    cars.add(new Car(4, 2, 2, true));
    cars.add(new Car(0, 1, 2, false));
    cars.add(new Car(5, 1, 2, false));
    cars.add(new Car(2, 2, 3, true));
    cars.add(new Car(5, 4, 3, false));
    cars.add(new Car(0, 5, 3, false));
    cars.add(new Car(3, 5, 2, true));
    cars.add(new Car(5, 5, 2, true));
    Board initialState = new Board(cars);
    BidirectionalHeuristicSolver solver = new BidirectionalHeuristicSolver(initialState);
    HashSet<Board> s = solver.feasibleWinStates();
    assertEquals(1, s.size());
    cars.get(6).moveBackward();
    cars.get(7).moveBackward();
    cars.get(4).moveForward();
    cars.get(0).moveForward();
    for (Board b : s) {
      for (Car c : b.cars()) {
        System.err.println("(" + String.valueOf(c.xPosition()) + ", " + String.valueOf(c.yPosition()) + ")");
      }
    }
    assertTrue(s.contains(new Board(cars)));
  }

}