package com.rushhour;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Rush Hour Board, specified by size (N X N square grid), exit position (x and y coordinate on the
 * perimeter of the square grid), and the positions of all cars on the board (x and y coordinates
 * of the front of the car, the length of the car, and a boolean specifying whether the car is
 * horizontal and vertical). The board is able to test its equality to and distance from another
 * board, perform moves in-place, and return a new board reflecting a specified move.
 */
public class Board {
  int N;
  int exitXPosition;
  int exitYPosition;
  ArrayList<Car> cars;

  // Default constructor for standard board with no cars
  public Board() {
    this.N = 6;
    this.exitXPosition = 5;
    this.exitYPosition = 2;
    this.cars = new ArrayList<>();
  }

  // Explicit value constructor for custom board with no cars
  public Board(int N, int exitXPosition, int exitYPosition) {
    // Verify that N is positive
    if (N <= 0) {
      throw new IllegalArgumentException("N must be greater than 0");
    }

    this.N = N;
    this.exitXPosition = exitXPosition;
    this.exitYPosition = exitYPosition;
    this.cars = new ArrayList<>();

    // Validate exit position
    if (!this.validateExitPosition()) {
      throw new IllegalArgumentException("Specified exit position is not on the perimeter of the board");
    }
  }

  // Explicit value constructor for default board with a provided collection of cars
  public Board(Collection<Car> cars) {
    this();
    this.cars = new ArrayList<>(cars);

    // Verify that cars collection contains at least the target car
    if (cars == null || this.cars.isEmpty()) {
      throw new IllegalArgumentException("Cars collection must contain at least 1 car");
    }

    // Verify that cars collection doesn't contain any intersecting cars
    if (!this.validateCars()) {
      throw new RuntimeException("Cars collection contains a position conflict");
    }

    // Verify that the target car is in line with the exit and no cars with the same orientation
    // are in its way
    Car targetCar = this.cars.get(0);
    Car otherCar;
    int numberOfCars = this.cars.size();

    if (targetCar.isHorizontal()) {
      // Target car is horizontal
      if (targetCar.yPosition() != this.exitYPosition) {
        throw new RuntimeException("Target car is not in line with board exit");
      }
      for (int i = 1; i < numberOfCars; i++) {
        otherCar = this.cars.get(i);
        if (otherCar.isHorizontal() && targetCar.yPosition() == otherCar.yPosition() &&
          Math.abs(this.exitXPosition - otherCar.xPosition()) < Math.abs(this.exitXPosition - targetCar.xPosition())) {
          throw new RuntimeException("Car collection contains a car between target car and exit with same orientation");
        }
      }
    } else {
      // Target car is vertical
      if (targetCar.xPosition() != this.exitXPosition) {
        throw new RuntimeException("Target car is not in line with board exit");
      }
      for (int i = 1; i < numberOfCars; i++) {
        otherCar = this.cars.get(i);
        if (!otherCar.isHorizontal() && targetCar.xPosition() == otherCar.xPosition() &&
          Math.abs(this.exitYPosition - otherCar.yPosition()) < Math.abs(this.exitYPosition - targetCar.yPosition())) {
          throw new RuntimeException("Car collection contains a car between target car and exit with same orientation");
        }
      }
    }
  }

  // Explicit value constructor for custom board with a provided collection of cars
  public Board(int N, int exitXPosition, int exitYPosition, Collection<Car> cars) {
    this(N, exitXPosition, exitYPosition);
    this.cars = new ArrayList<>(cars);

    // Verify that cars collection contains at least the target car
    if (cars == null || this.cars.isEmpty()) {
      throw new IllegalArgumentException("Cars collection must contain at least 1 car");
    }

    // Verify that cars collection doesn't contain any intersecting cars
    if (!this.validateCars()) {
      throw new RuntimeException("Cars collection contains a position conflict");
    }

    // Verify that the target car is in line with the exit and no cars with the same orientation
    // are in its way
    Car targetCar = this.cars.get(0);
    Car otherCar;
    int numberOfCars = this.cars.size();

    if (targetCar.isHorizontal()) {
      // Target car is horizontal
      if (targetCar.yPosition() != this.exitYPosition) {
        throw new RuntimeException("Target car is not in line with board exit");
      }
      for (int i = 1; i < numberOfCars; i++) {
        otherCar = this.cars.get(i);
        if (otherCar.isHorizontal() && targetCar.yPosition() == otherCar.yPosition() &&
          Math.abs(this.exitXPosition - otherCar.xPosition()) < Math.abs(this.exitXPosition - targetCar.xPosition())) {
          throw new RuntimeException("Car collection contains a car between target car and exit with same orientation");
        }
      }
    } else {
      // Target car is vertical
      if (targetCar.xPosition() != this.exitXPosition) {
        throw new RuntimeException("Target car is not in line with board exit");
      }
      for (int i = 1; i < numberOfCars; i++) {
        otherCar = this.cars.get(i);
        if (!otherCar.isHorizontal() && targetCar.xPosition() == otherCar.xPosition() &&
          Math.abs(this.exitYPosition - otherCar.yPosition()) < Math.abs(this.exitYPosition - targetCar.yPosition())) {
          throw new RuntimeException("Car collection contains a car between target car and exit with same orientation");
        }
      }
    }
  }

  // Copy constructor
  public Board(Board otherBoard) {
    this.N = otherBoard.N();
    this.exitXPosition = otherBoard.exitXPosition();
    this.exitYPosition = otherBoard.exitYPosition();
    this.cars = new ArrayList<>(otherBoard.cars().size());
    for (Car c : otherBoard.cars()) {
      this.cars.add(new Car(c));
    }
  }

  // Hash code generation function
  @Override
  public int hashCode() {
    return Objects.hash(this.N, this.exitXPosition, this.exitYPosition, this.cars);
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

    Board otherBoard = (Board) obj;
    return this.N == otherBoard.N() && this.exitXPosition == otherBoard.exitXPosition() &&
      this.exitYPosition == otherBoard.exitYPosition() && this.cars.equals(otherBoard.cars());
  }

  // Calculate distance between this board and another board (returns null if different number of
  // cars or cars misaligned, dimensions and exit assumed identical)
  public Integer distanceFrom(Board otherBoard) {
    int numberOfCars = this.cars.size();
    if (numberOfCars != otherBoard.cars().size()) {
      return null;
    }
    int totalDistance = 0;
    Integer individualDistance;
    for (int i = 0; i < numberOfCars; i++) {
      individualDistance = this.cars.get(i).distanceFrom(otherBoard.cars().get(i));
      if (individualDistance != null) {
        totalDistance += individualDistance;
      } else {
        return null;
      }
    }
    return totalDistance;
  }

  // Helper function to verify that the exit is on the perimeter of the board
  private boolean validateExitPosition() {
    return this.exitXPosition == 0 || this.exitXPosition == this.N - 1 ||
      this.exitYPosition == 0 || this.exitYPosition == this.N - 1;
  }

  // Helper function to verify that all cars are entirely on the board and no two cars intersect
  // each other
  private boolean validateCars() {
    // Ensure that the board contains at least 1 car
    if (this.cars.size() < 1) {
      return false;
    }

    // Ensure that every car is on the board
    for (Car c : this.cars) {
      if (c.xPosition() - c.length() + 1 < 0 || c.xPosition() >= N) {
        return false;
      }
    }

    // Ensure that no two cars intersect
    int numberOfCars = this.cars.size();
    for (int i = 0; i < numberOfCars - 1; i++) {
      for (int j = i + 1; j < numberOfCars; j++) {
        if (this.cars.get(i).intersects(this.cars.get(j))) {
          return false;
        }
      }
    }

    // No conflicts found
    return true;
  }

  // Function to check if the target car is at the exit
  public boolean solutionFound() {
    // Ensure that the board contains the target car
    if (this.cars.size() < 1) {
      return false;
    }

    // Check if the target car is at the exit
    Car targetCar = this.cars.get(0);
    return targetCar.xPosition() == this.exitXPosition && targetCar.yPosition() == this.exitYPosition;
  }

  // Function to evaluate whether a particular move is possible
  public boolean moveIsPossible(int carIndex, boolean movingForward) {
    if (carIndex < 0 || carIndex >= this.cars.size()) {
      return false;
    }
    Car movingCar = this.cars.get(carIndex);
    if (movingForward) {
      movingCar.moveForward();
      if (this.validateCars()) {
        movingCar.moveBackward();
        return true;
      } else {
        movingCar.moveBackward();
        return false;
      }
    } else {
      movingCar.moveBackward();
      if (this.validateCars()) {
        movingCar.moveForward();
        return true;
      } else {
        movingCar.moveForward();
        return false;
      }
    }
  }

  // Function to try a move, returning a copy of the board reflecting the move if possible and null
  // otherwise
  public Board tryMove(int carIndex, boolean movingForward) {
    if (carIndex < 0 || carIndex >= this.cars.size()) {
      return null;
    }
    Car movingCar = this.cars.get(carIndex);
    Board newBoard;
    if (movingForward) {
      movingCar.moveForward();
      if (this.validateCars()) {
        newBoard = new Board(this);
        movingCar.moveBackward();
        return newBoard;
      } else {
        movingCar.moveBackward();
        return null;
      }
    } else {
      movingCar.moveBackward();
      if (this.validateCars()) {
        newBoard = new Board(this);
        movingCar.moveForward();
        return newBoard;
      } else {
        movingCar.moveForward();
        return null;
      }
    }
  }

  // Function to test if it's possible to add a car to the board without conflict
  public boolean canAddCar(Car newCar) {
    this.cars.add(newCar);
    if (this.validateCars()) {
      this.cars.remove(newCar);
      return true;
    } else {
      this.cars.remove(newCar);
      return false;
    }
  }

  // Function to add a car to the board
  public void addCar(Car newCar) {
    this.cars.add(newCar);
    if (!validateCars()) {
      throw new RuntimeException("Inserted car conflicts with an existing car");
    }
  }

  // Function to try to add a car to the board, returning a new board reflecting the adition if
  // possible and null otherwise
  public Board tryAddCar(Car newCar) {
    this.cars.add(newCar);
    if (!this.validateCars()) {
      this.cars.remove(newCar);
      return null;
    }
    Board newBoard = new Board(this);
    this.cars.remove(newCar);
    return newBoard;
  }

  // Function to remove a car from the board
  public Car removeCar(int carIndex) {
    if (carIndex < 0 || carIndex >= this.cars.size()) {
      return null;
    }
    Car removedCar = this.cars.remove(carIndex);
    if (carIndex == 0) {
      int numberOfCars = this.cars.size();
      if (numberOfCars == 0) {
        throw new RuntimeException("Board must contain a target car");
      }
      Car targetCar = this.cars.get(0);
      Car otherCar;
      if (targetCar.isHorizontal()) {
        // Target car is horizontal
        if (targetCar.yPosition() != this.exitYPosition) {
          throw new RuntimeException("Target car is not in line with board exit");
        }
        for (int i = 1; i < numberOfCars; i++) {
          otherCar = this.cars.get(i);
          if (otherCar.isHorizontal() && targetCar.yPosition() == otherCar.yPosition() &&
            Math.abs(this.exitXPosition - otherCar.xPosition()) < Math.abs(this.exitXPosition - targetCar.xPosition())) {
            throw new RuntimeException("Car collection contains a car between target car and exit with same orientation");
          }
        }
      } else {
        // Target car is vertical
        if (targetCar.xPosition() != this.exitXPosition) {
          throw new RuntimeException("Target car is not in line with board exit");
        }
        for (int i = 1; i < numberOfCars; i++) {
          otherCar = this.cars.get(i);
          if (!otherCar.isHorizontal() && targetCar.xPosition() == otherCar.xPosition() &&
            Math.abs(this.exitYPosition - otherCar.yPosition()) < Math.abs(this.exitYPosition - targetCar.yPosition())) {
            throw new RuntimeException("Car collection contains a car between target car and exit with same orientation");
          }
        }
      }
    }
    return removedCar;
  }

  // Getter function for N
  public int N() {
    return this.N;
  }

  // Getter function for exit x position
  public int exitXPosition() {
    return this.exitXPosition;
  }

  // Getter function for exit y position
  public int exitYPosition() {
    return this.exitYPosition;
  }

  // Getter function for cars list (returned by reference, modify with caution)
  public List<Car> cars() {
    return this.cars;
  }

  // Getter function for the number of cars on the board
  public int numberOfCars() {
    return this.cars.size();
  }
}