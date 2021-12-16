package dungeonmodel;

// Represents all kinds of monsters that could be present in dungeon.
interface Monster {

  // Gets damage a monster can still make.
  double getPotentialDamage();

  // Reduces damaging capability of monster.
  void reducedDamage();

  // Assigns given damaging capability to monster.
  void assignDamage(double actualMonsterHealth);
}
