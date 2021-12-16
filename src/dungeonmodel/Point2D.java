package dungeonmodel;

import java.util.Objects;

/**
 * This class represents a 2D point. This point is denoted in Cartesian
 * coordinates as (x, y). This is used to represent a location in dungeon.
 */
public class Point2D {
  private final int x;
  private final int y;

  /**
   * Construct a 2d point with the given coordinates.
   *
   * @param x the x-coordinate of this point
   * @param y the y-coordinate of this point
   */
  public Point2D(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Return the x-coordinate of this point.
   *
   * @return x-coordinate of this point
   */
  public int getX() {
    return this.x;
  }

  /**
   * Return the y-coordinate of this point.
   *
   * @return y-coordinate of this point
   */
  public int getY() {
    return this.y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Point2D)) {
      return false;
    }

    Point2D other = (Point2D) o;
    return this.getX() == other.getX() && this.getY() == other.getY() ;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.x, this.y);
  }

  @Override
  public String toString() {
    return String.format("(%d, %d)", this.x, this.y);
  }
}

