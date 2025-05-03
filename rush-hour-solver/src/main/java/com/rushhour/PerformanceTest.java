package com.rushhour;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class PerformanceTest {
  // Method to load puzzles from a file
  public static List<Board> loadPuzzles(String filename) throws IOException {
    List<Board> puzzles = new ArrayList<>();
    List<Car> cars = new ArrayList<>();

    InputStream inputStream = PerformanceTest.class.getClassLoader().getResourceAsStream(filename);
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        // Add a new board when a blank line or comment is encountered
        if (line.isEmpty() || line.startsWith("#")) {
          if (!cars.isEmpty()) {
            puzzles.add(new Board(cars));
            cars.clear();
          }
          continue;
        }
        // Parse car details
        String[] parts = line.split(",");
        int xPosition = Integer.parseInt(parts[0]);
        int yPosition = Integer.parseInt(parts[1]);
        int length = Integer.parseInt(parts[2]);
        boolean isHorizontal = Boolean.parseBoolean(parts[3]);
        cars.add(new Car(xPosition, yPosition, length, isHorizontal));
      }
      // Add the last board if cars are left
      if (!cars.isEmpty()) {
        puzzles.add(new Board(cars));
      }
    }
    return puzzles;
  }

  @SuppressWarnings("UseSpecificCatch")
  static <T extends Solver> long timeSolvePuzzle(Class<T> solverClass, Board initialState) {
    long start;
    long end;
    try {
      Constructor<T> constructor = solverClass.getConstructor(Board.class);
      // Start the timer
      start = System.currentTimeMillis();
      Solver s = constructor.newInstance(initialState);
      s.solveProblem();
      end = System.currentTimeMillis();
      return end - start;
    } catch (Exception e) {
      throw new RuntimeException("Failed to instantiate solver");
    }
  }

  @SuppressWarnings("UseSpecificCatch")
  static <T extends Solver> long solutionLength(Class<T> solverClass, Board initialState) {
    try {
      Constructor<T> constructor = solverClass.getConstructor(Board.class);
      Solver s = constructor.newInstance(initialState);
      List<Board> solution = s.solveProblem();
      return solution.size();
    } catch (Exception e) {
      throw new RuntimeException("Failed to instantiate solver");
    }
  }

  static void chart(XYSeries[] series, String title, String yAxisLabel, String filename) throws Exception {
    XYSeriesCollection ds = new XYSeriesCollection();
    for (XYSeries s : series) {
      ds.addSeries(s);
    }
    // Build the chart
    JFreeChart chart = ChartFactory.createXYLineChart(title, "Puzzle Number", yAxisLabel, ds);
    XYPlot plot = chart.getXYPlot();
    plot.setBackgroundPaint(new Color(220, 220, 220));
    // Configure the chart
    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
    for (int i = 0; i < series.length; i++) {
      renderer.setSeriesShapesVisible(i, true);
      renderer.setSeriesShapesFilled(i, true);
      renderer.setSeriesStroke(i, new BasicStroke(2.5f));
    }
    // Save the result
    int width = 640;
    int height = 480;
    File lineChart = new File(filename);
    ChartUtils.saveChartAsPNG(lineChart, chart, width, height);
  }

  public static void main(String[] args) {
    int numberOfPuzzles;
    List<Board> puzzles;

    try {
      puzzles = PerformanceTest.loadPuzzles("puzzles.txt");
      numberOfPuzzles = puzzles.size();
      System.out.println("Loaded " + numberOfPuzzles + " puzzles");
    } catch (IOException e) {
      System.out.println("IO exception occured");
      return;
    }

    XYSeries s1 = new XYSeries("DepthFirstSolver");
    XYSeries s2 = new XYSeries("BreadthFirstSolver");
    XYSeries s3 = new XYSeries("BidirectionalHeuristicSolver");

    long totalTime = 0;
    Board puzzle;
    for (int i = 0; i < numberOfPuzzles; i++) {
      puzzle = puzzles.get(i);
      for (int j = 0; j < 10; j++) {
        totalTime += timeSolvePuzzle(DepthFirstSolver.class, puzzle);
      }
      s1.add(i + 1, totalTime / 10);
      totalTime = 0;
      for (int j = 0; j < 10; j++) {
        totalTime += timeSolvePuzzle(BreadthFirstSolver.class, puzzle);
      }
      s2.add(i + 1, totalTime / 10);
      totalTime = 0;
      for (int j = 0; j < 10; j++) {
        totalTime += timeSolvePuzzle(BidirectionalHeuristicSolver.class, puzzle);
      }
      s3.add(i + 1, totalTime / 10);
    }
    XYSeries[] series = {s1, s2, s3};
    String title = "Time Performance of Solvers on 40 Puzzles";
    String yAxisLabel = "Average Time (ms)";
    try {
      chart(series, title, yAxisLabel, "target/time_performance.png");
    } catch (Exception e) {
      throw new RuntimeException("Could not print time performance results");
    }

    s1.clear();
    s2.clear();
    s3.clear();

    for (int i = 0; i < numberOfPuzzles; i++) {
      puzzle = puzzles.get(i);
      s1.add(i + 1, solutionLength(DepthFirstSolver.class, puzzle));
      s2.add(i + 1, solutionLength(BreadthFirstSolver.class, puzzle));
      s3.add(i + 1, solutionLength(BidirectionalHeuristicSolver.class, puzzle));
    }
    series[0] = s1;
    series[1] = s2;
    series[2] = s3;
    title = "Solution Length From Solvers on 40 Puzzles";
    yAxisLabel = "Solution Length (total units of car displacement)";
    try {
      chart(series, title, yAxisLabel, "target/solution_lengths.png");
    } catch (Exception e) {
      throw new RuntimeException("Could not print solution length results");
    }
    XYSeries[] reducedSeries = {s2, s3};
    try {
      chart(reducedSeries, title, yAxisLabel, "target/solution_lengths_reduced.png");
    } catch (Exception e) {
      throw new RuntimeException("Could not print solution length results");
    }
  }
}