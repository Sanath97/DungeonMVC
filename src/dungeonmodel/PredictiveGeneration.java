package dungeonmodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * This class generates Predictive Numbers inorder to be used for testing. This enables predictive
 * testing when random cannot exactly help for any scenarios.
 */
public class PredictiveGeneration implements NumberGenerationInterface {
  private final List<Integer> predictiveValues;
  private int index;
  private boolean isRestart;

  /**
   * Constructs Predictive generation object.
   * @param predictiveValues Values that can be used instead of Randomly generated values.
   */
  public PredictiveGeneration(Integer... predictiveValues) {
    this.predictiveValues = new ArrayList<>(Arrays.asList(predictiveValues));
    this.index = 0;
    this.isRestart = false;
  }

  /**
   * Constructs Predictive generation object from List of Integers.
   * @param predictiveValues List of values that can be used instead of Randomly generated values.
   */
  public PredictiveGeneration(List<Integer> predictiveValues) {
    this.predictiveValues = predictiveValues;
    this.index = 0;
    this.isRestart = true;
  }

  @Override
  public int getIntInRange(int minimumBound, int maximumBound) {
    if (index == this.predictiveValues.size() - 1 && this.isRestart) {
      Random random = new Random();
      return random.nextInt( 2);
    }
    int randomValue = this.predictiveValues.get(index);
    index++;
    return randomValue;
  }
}
