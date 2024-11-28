package com.rushhour;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class CarTest {
  @Test
  void defaultConstructorAndGettersTest() {
    Car c = new Car();
    assertEquals(1, c.xPosition());
    assertEquals(0, c.yPosition());
    assertEquals(2, c.length());
    assertEquals(true, c.isHorizontal());
  }

  @Test
  void explicitValueConstructorTest() {
    Car c = new Car(1, 2, 3, false);
    assertEquals(1, c.xPosition());
    assertEquals(2, c.yPosition());
    assertEquals(3, c.length());
    assertEquals(false, c.isHorizontal());
  }

  @Test
  void differentCarDifferentHashCodeTest() {
    Car a = new Car();
    Car b = new Car(1, 1, 2, true);
    assertFalse(a.hashCode() == b.hashCode());
  }

  @Test
  void twoDefaultCarsSameHashCodeTest() {
    Car a = new Car();
    Car b = new Car();
    assertTrue(a.hashCode() == b.hashCode());
  }

  @Test
  void twoExplicitIdenticalCarsSameHashCodeTest() {
    Car a = new Car(2, 2, 3, false);
    Car b = new Car(2, 2, 3, false);
    assertTrue(a.hashCode() == b.hashCode());
  }

  @Test
  void carNotEqualToDifferentObjectTest() {
    Car a = new Car();
    Board b = new Board();
    assertFalse(a.equals(b));
  }

  @Test
  void carNotEqualToCarInDifferentPositionTest() {
    Car a = new Car();
    Car b = new Car(1, 1, 2, true);
    assertFalse(a.equals(b));
    assertFalse(b.equals(a));
  }

  @Test
  void carEqualsIdenticalCarTest() {
    Car a = new Car();
    Car b = new Car();
    assertTrue(a.equals(b));
    assertTrue(b.equals(a));
  }

  @Test
  void identicalDefaultCarsIntersectTest() {
    Car a = new Car();
    Car b = new Car();
    assertTrue(a.intersects(b));
    assertTrue(b.intersects(a));
  }

  @Test
  void verticalCarsIntersectTest() {
    Car a = new Car(0, 0, 3, false);
    Car b = new Car(0, 0, 2, false);
    assertTrue(a.intersects(b));
    assertTrue(b.intersects(a));
  }

  @Test
  void horizontalCarsDontIntersectTest() {
    Car a = new Car();
    Car b = new Car(4, 0, 3, true);
    assertFalse(a.intersects(b));
    assertFalse(b.intersects(a));
  }

  @Test
  void verticalCarsDontIntersectTest() {
    Car a = new Car(0, 1, 2, false);
    Car b = new Car(0, 4, 3, false);
    assertFalse(a.intersects(b));
    assertFalse(b.intersects(a));
  }

  @Test
  void horizontalAndVerticalCarsIntersectTest() {
    Car a = new Car(2, 1, 3, true);
    Car b = new Car(2, 2, 3, false);
    assertTrue(a.intersects(b));
    assertTrue(b.intersects(a));
  }

  @Test
  void horizontalAndVerticalCarsDontIntersectTest() {
    Car a = new Car(2, 1, 2, true);
    Car b = new Car(2, 2, 3, false);
    assertTrue(a.intersects(b));
    assertTrue(b.intersects(a));
  }

  @Test
  void defaultCarMoveForwardTest() {
    Car c = new Car();
    c.moveForward();
    assertEquals(2, c.xPosition());
    assertEquals(0, c.yPosition());
    assertEquals(2, c.length());
    assertEquals(true, c.isHorizontal());
  }

  @Test
  void verticalCarMoveBackwardTest() {
    Car c = new Car(0, 2, 2, false);
    c.moveBackward();
    assertEquals(0, c.xPosition());
    assertEquals(1, c.yPosition());
    assertEquals(2, c.length());
    assertEquals(false, c.isHorizontal());
  }
}