package com.rushhour;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class BoardTest {
  @Test
  void defaultConstructorAndGettersTest() {
    Board b = new Board();
    assertEquals(6, b.N());
    assertEquals(5, b.exitXPosition());
    assertEquals(2, b.exitYPosition());
    assertTrue(b.cars().isEmpty());
  }

  @Test
  void customEmptyBoardConstructorTest() {
    Board b = new Board(5, 0, 3);
    assertEquals(5, b.N());
    assertEquals(0, b.exitXPosition());
    assertEquals(3, b.exitYPosition());
    assertTrue(b.cars().isEmpty());
  }

  @Test
  void defaultBoardWithProvidedValidTargetCarTest() {
    Board b = new Board(List.of(new Car(1, 2, 2, true)));
    assertEquals(6, b.N());
    assertEquals(5, b.exitXPosition());
    assertEquals(2, b.exitYPosition());
    assertEquals(1, b.cars().size());
    assertFalse(b.solutionFound());
    Car c = b.cars().get(0);
    assertEquals(1, c.xPosition());
    assertEquals(2, c.yPosition());
    assertEquals(2, c.length());
    assertEquals(true, c.isHorizontal());
  }

  @Test
  void defaultBoardWithProvidedEmptyCarCollectionTest() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Board(new ArrayList<Car>());
    });
  }

  @Test
  void defaultBoardWithOutOfBoundsTargetCarTest() {
    assertThrows(RuntimeException.class, () -> {
      new Board(List.of(new Car(0, 2, 2, true)));
    });
  }

  @Test
  void defaultBoardWithCarNotFacingExitTest() {
    assertThrows(RuntimeException.class, () -> {
      new Board(List.of(new Car(0, 1, 2, false)));
    });
  }

  @Test
  void defaultBoardCarNotAlignedWithExitTest() {
    assertThrows(RuntimeException.class, () -> {
      new Board(List.of(new Car(1, 1, 2, true)));
    });
  }

  @Test
  void defaultBoardWithTwoProvidedValidCarsTest() {
    Board b = new Board(List.of(new Car(1, 2, 2, true), new Car(2, 2, 3, false)));
    assertEquals(6, b.N());
    assertEquals(5, b.exitXPosition());
    assertEquals(2, b.exitYPosition());
    assertEquals(2, b.cars().size());
    assertFalse(b.solutionFound());
    assertFalse(b.cars().get(0).intersects(b.cars().get(1)));
    Car c = b.cars().get(0);
    assertEquals(1, c.xPosition());
    assertEquals(2, c.yPosition());
    assertEquals(2, c.length());
    assertEquals(true, c.isHorizontal());
    c = b.cars().get(1);
    assertEquals(2, c.xPosition());
    assertEquals(2, c.yPosition());
    assertEquals(3, c.length());
    assertEquals(false, c.isHorizontal());
  }

  @Test
  void defaultBoardWithTwoCarsOneOffBoardTest() {
    assertThrows(RuntimeException.class, () -> {
      new Board(List.of(new Car(1, 2, 2, true), new Car(1, 1, 3, true)));
    });
  }

  @Test
  void defaultBoardTwoIntersectingCarsTest() {
    assertThrows(RuntimeException.class, () -> {
      new Board(List.of(new Car(1, 2, 2, true), new Car(1, 3, 3, false)));
    });
  }

  @Test
  void customBoardWithProvidedValidTargetCarTest() {
    Board b = new Board(7, 6, 2, List.of(new Car(1, 2, 2, true)));
    assertEquals(7, b.N());
    assertEquals(6, b.exitXPosition());
    assertEquals(2, b.exitYPosition());
    assertEquals(1, b.cars().size());
    assertFalse(b.solutionFound());
    Car c = b.cars().get(0);
    assertEquals(1, c.xPosition());
    assertEquals(2, c.yPosition());
    assertEquals(2, c.length());
    assertEquals(true, c.isHorizontal());
  }

  @Test
  void customBoardWithProvidedEmptyCarCollectionTest() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Board(7, 6, 2, new ArrayList<Car>());
    });
  }

  @Test
  void customBoardWithOutOfBoundsTargetCarTest() {
    assertThrows(RuntimeException.class, () -> {
      new Board(7, 6, 2, List.of(new Car(0, 2, 2, true)));
    });
  }

  @Test
  void customBoardWithCarNotFacingExitTest() {
    assertThrows(RuntimeException.class, () -> {
      new Board(7, 6, 2, List.of(new Car(0, 1, 2, false)));
    });
  }

  @Test
  void customBoardCarNotAlignedWithExitTest() {
    assertThrows(RuntimeException.class, () -> {
      new Board(7, 6, 2, List.of(new Car(1, 1, 2, true)));
    });
  }

  @Test
  void customBoardWithTwoProvidedValidCarsTest() {
    Board b = new Board(7, 6, 2, List.of(new Car(1, 2, 2, true), new Car(2, 2, 3, false)));
    assertEquals(7, b.N());
    assertEquals(6, b.exitXPosition());
    assertEquals(2, b.exitYPosition());
    assertEquals(2, b.cars().size());
    assertFalse(b.solutionFound());
    assertFalse(b.cars().get(0).intersects(b.cars().get(1)));
    Car c = b.cars().get(0);
    assertEquals(1, c.xPosition());
    assertEquals(2, c.yPosition());
    assertEquals(2, c.length());
    assertEquals(true, c.isHorizontal());
    c = b.cars().get(1);
    assertEquals(2, c.xPosition());
    assertEquals(2, c.yPosition());
    assertEquals(3, c.length());
    assertEquals(false, c.isHorizontal());
  }

  @Test
  void customBoardWithTwoCarsOneOffBoardTest() {
    assertThrows(RuntimeException.class, () -> {
      new Board(7, 6, 2, List.of(new Car(1, 2, 2, true), new Car(1, 1, 3, true)));
    });
  }

  @Test
  void customBoardTwoIntersectingCarsTest() {
    assertThrows(RuntimeException.class, () -> {
      new Board(7, 6, 2, List.of(new Car(1, 2, 2, true), new Car(1, 3, 3, false)));
    });
  }

  @Test
  void copyConstructorTest() {
    Board a = new Board(List.of(new Car(1, 2, 2, true)));
    Board b = new Board(a);
    a.cars().get(0).moveForward();
    assertEquals(6, b.N());
    assertEquals(5, b.exitXPosition());
    assertEquals(2, b.exitYPosition());
    assertEquals(1, b.cars().size());
    assertEquals(1, b.cars().get(0).xPosition());
    assertEquals(2, b.cars().get(0).yPosition());
    assertEquals(2, b.cars().get(0).length());
    assertEquals(true, b.cars().get(0).isHorizontal());
  }

  @Test
  void twoDefaultBoardsAreEqualTest() {
    Board a = new Board();
    Board b = new Board();
    assertTrue(a.hashCode() == b.hashCode());
    assertTrue(a.equals(b));
    assertTrue(b.equals(a));
  }

  @Test
  void twoCustomEmptyBoardsAreEqualTest() {
    Board a = new Board(4, 3, 2);
    Board b = new Board(4, 3, 2);
    assertTrue(a.hashCode() == b.hashCode());
    assertTrue(a.equals(b));
    assertTrue(b.equals(a));
  }

  @Test
  void twoIdenticalBoardsAreEqual() {
    Board a = new Board(List.of(new Car(1, 2, 2, true), new Car(4, 3, 2, false)));
    Board b = new Board(List.of(new Car(1, 2, 2, true), new Car(4, 3, 2, false)));
    assertTrue(a.hashCode() == b.hashCode());
    assertTrue(a.equals(b));
    assertTrue(b.equals(a));
  }

  @Test
  void boardsOfDifferentDimensionsAreNotEqual() {
    Board a = new Board();
    Board b = new Board(7, 6, 2);
    assertFalse(a.hashCode() == b.hashCode());
    assertFalse(a.equals(b));
    assertFalse(b.equals(a));
  }

  @Test
  void boardNotEqualToBoardWithCarMovedForward() {
    Board a = new Board(List.of(new Car(1, 2, 2, true)));
    Board b = new Board(a);
    b.cars().get(0).moveForward();
    assertFalse(a.equals(b));
  }

  @Test
  void distanceTest() {
    ArrayList<Car> cars = new ArrayList<>();
    for (int i = 2; i < 6; i++) {
      cars.add(new Car(2, i, 2, true));
    }
    Board a = new Board(cars);
    Board b = new Board(a);
    b.cars().get(0).moveForward();
    b.cars().get(0).moveForward();
    b.cars().get(3).moveBackward();
    assertEquals(3, a.distanceFrom(b));
    assertEquals(3, b.distanceFrom(a));
  }
}