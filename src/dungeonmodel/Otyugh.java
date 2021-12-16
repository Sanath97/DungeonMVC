package dungeonmodel;

// Class represents Otyugh one of the monsters kinds.
class Otyugh implements Monster {
  double potentialDamage;

  // Constructs object for Otyugh.
  public Otyugh() {
    this.potentialDamage = 1.0;
  }

  @Override
  public double getPotentialDamage() {
    return this.potentialDamage;
  }

  @Override
  public void reducedDamage() {
    this.potentialDamage = this.potentialDamage - 0.5;
  }

  @Override
  public void assignDamage(double actualMonsterHealth) {
    this.potentialDamage = actualMonsterHealth;
  }
}
