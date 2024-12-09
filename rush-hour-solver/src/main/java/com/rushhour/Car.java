package com.rushhour;

import java.util.Objects;

/**
 * Rush Hour Car, specified by the x and y position of the front of the car, its length, and a
 * boolean value specifying whether the car is horizontal or vertical. The car can evaluate whether
 * it collides with another car, calculate its distance from another car of compatible orientation,
 * and move.
 */
public class Car {
  int xPosition;
  int yPosition;
  int length;
  boolean isHorizontal;

  // Default value constructor
  public Car() {
    this.xPosition = 1;
    this.yPosition = 0;
    this.length = 2;
    this.isHorizontal = true;
  }

  // Explicit value constructor
  public Car(int xPosition, int yPosition, int length, boolean isHorizontal) {
    this.xPosition = xPosition;
    this.yPosition = yPosition;
    this.length = length;
    this.isHorizontal = isHorizontal;
  }

  // Copy constructor
  public Car(Car otherCar) {
    this.xPosition = otherCar.xPosition();
    this.yPosition = otherCar.yPosition();
    this.length = otherCar.length();
    this.isHorizontal = otherCar.isHorizontal();
  }

  // Hash code generation function
  @Override
  public int hashCode() {
    return Objects.hash(this.xPosition, this.yPosition, this.length, this.isHorizontal);
  }

  // Equality operator override
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || this.getClass() != obj.getClass()) {
      return false;
    }

    Car otherCar = (Car) obj;
    return this.xPosition == otherCar.xPosition() && this.yPosition == otherCar.yPosition() &&
      this.length == otherCar.length() && this.isHorizontal == otherCar.isHorizontal();
  }

  // Calculate whether this car intersects some given other car
  public boolean intersects(Car otherCar) {
    if (this.isHorizontal) {
      if (otherCar.isHorizontal()) {
        // Both cars horizontal
        if (this.yPosition != otherCar.yPosition()) {
          return false;
        }
        return (this.xPosition - this.length + 1 <= otherCar.xPosition() && otherCar.xPosition() <= this.xPosition) ||
          (otherCar.xPosition() - otherCar.length() + 1 <= this.xPosition && this.xPosition <= otherCar.xPosition());
      } else {
        // This car horizontal, other car vertical
        return this.xPosition - this.length + 1 <= otherCar.xPosition() && otherCar.xPosition() <= this.xPosition &&
          otherCar.yPosition() - otherCar.length() + 1 <= this.yPosition && this.yPosition <= otherCar.yPosition();
      }
    } else {
      if (otherCar.isHorizontal()) {
        // This car vertical, other car horizontal
        return this.yPosition - this.length + 1 <= otherCar.yPosition() && otherCar.yPosition() <= this.yPosition &&
          otherCar.xPosition() - otherCar.length() + 1 <= this.xPosition && this.xPosition <= otherCar.xPosition();
      } else {
        // Both cars vertical
        if (this.xPosition != otherCar.xPosition()) {
          return false;
        }
        return (this.yPosition - this.length + 1 <= otherCar.yPosition() && otherCar.yPosition() <= this.yPosition) ||
          (otherCar.yPosition() - otherCar.length() + 1 <= this.yPosition && this.yPosition <= otherCar.yPosition());
      }
    }
  }

  // Calculate the distance between this car and another (returns null if the cars are misaligned)
  public Integer distanceFrom(Car otherCar) {
    if (this.isHorizontal) {
      if (!otherCar.isHorizontal() || this.yPosition != otherCar.yPosition()) {
        return null;
      }
      return Math.abs(this.xPosition - otherCar.xPosition());
    } else {
      if (otherCar.isHorizontal() || this.xPosition != otherCar.xPosition()) {
        return null;
      }
      return Math.abs(this.yPosition - otherCar.yPosition());
    }
  }

  // Getter for x position attribute
  public int xPosition() {
    return this.xPosition;
  }

  // Getter for y position attribute
  public int yPosition() {
    return this.yPosition;
  }

  // Getter for length attribute
  public int length() {
    return this.length;
  }

  // Getter for horizontal orientation attribute
  public boolean isHorizontal() {
    return this.isHorizontal;
  }

  // Move car forward one space (dependent on orientation)
  public void moveForward() {
    if (this.isHorizontal) {
      this.xPosition++;
    } else {
      this.yPosition++;
    }
  }

  // Move car backward one space (dependent on orientation)
  public void moveBackward() {
    if (this.isHorizontal) {
      this.xPosition--;
    } else {
      this.yPosition--;
    }
  }

  // Manually reposition car
  public void reposition(int newXPosition, int newYPosition) {
    this.xPosition = newXPosition;
    this.yPosition = newYPosition;
  }

  // Manually change the car's length
  public void changeLength(int newLength) {
    this.length = newLength;
  }

  // Manually change the car's orientaton
  public void changeOrientation(boolean newIsHorizontal) {
    this.isHorizontal = newIsHorizontal;
  }


}