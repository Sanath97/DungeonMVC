package dungeonmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class generates Random Numbers to be used at various places in the game. This is the actual
 * number generation that is used when player actually plays the game.
 */
public class RandomGeneration implements NumberGenerationInterface {
  private final Random random;
  private List<Integer> randomValues;

  /**
   * Constructs Random generation object.
   */
  public RandomGeneration() {
    random = new Random();
    randomValues = new ArrayList<>();
  }

  @Override
  public int getIntInRange(int minimumBound, int maximumBound) {
    int res = minimumBound + this.random.nextInt(maximumBound - minimumBound + 1);
    this.randomValues.add(res);
    return res;
  }

  protected List<Integer> getRandomValuesList() {
    return List.copyOf(this.randomValues);
  }

}
