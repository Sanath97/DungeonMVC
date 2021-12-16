package dungeonmodel;

import java.util.Objects;

// This class represents a map of location objects, source and destination locations.
class LocationMap {
  GeneralLocation sourceLocation;
  GeneralLocation destLocation;

  // Constructs location map object.
  public LocationMap(GeneralLocation sourceLocation, GeneralLocation destLocation) {
    this.sourceLocation = sourceLocation;
    this.destLocation = destLocation;
  }

  protected GeneralLocation getSourceLocation() {
    return this.sourceLocation;
  }

  protected GeneralLocation getDestLocation() {
    return this.destLocation;
  }


  @Override
  public boolean equals(Object other)  {
    if (other instanceof LocationMap) {
      return ((this.getSourceLocation().equals(((LocationMap) other).getSourceLocation()))
              && (this.getDestLocation().equals(((LocationMap) other).getDestLocation())))
              || ((this.getSourceLocation().equals(((LocationMap) other).getDestLocation()))
              && (this.getDestLocation().equals(((LocationMap) other).getSourceLocation())));
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getSourceLocation().getLocation().getX(),
            this.getSourceLocation().getLocation().getY(),
            this.getDestLocation().getLocation().getX(),
            this.getDestLocation().getLocation().getY());
  }

  @Override
  public String toString() {
    return "{" + this.sourceLocation.toString() + ", " + this.destLocation.toString() + "}";
  }

}
