package com.rushhour;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Rush Hour Board, with default and explicit value constructor.
 */
public class Board {
  int N;
  int exitXPosition;
  int exitYPosition;
  ArrayList<Car> cars;

  public Board() {
    this.N = 6;
    this.exitXPosition = 5;
    this.exitYPosition = 2;
    this.cars = new ArrayList<>();
  }

  public Board(int N, int exitXPosition, int exitYPosition) {
    this.N = N;
    this.exitXPosition = exitXPosition;
    this.exitYPosition = exitYPosition;
    this.cars = new ArrayList<>();
  }

  public Board(Collection<Car> cars) {
    this();
    this.cars = new ArrayList<>(cars);
  }

  public Board(int N, int exitXPosition, int exitYPosition, Collection<Car> cars) {
    this(N, exitXPosition, exitYPosition);
    this.cars = new ArrayList<>(cars);
  }
}