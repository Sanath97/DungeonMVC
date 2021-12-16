package dungeonmodel;

/**
 * This interface represents a number generation interface that generates numbers either in random
 * or in predictive fashion.
 */
public interface NumberGenerationInterface {
  /**
   * Returns integer in the given range.
   * @param minRange Minimum range.
   * @param maxRange Maximum range.
   * @return random integer in that range.
   */
  int getIntInRange(int minRange, int maxRange);
}
