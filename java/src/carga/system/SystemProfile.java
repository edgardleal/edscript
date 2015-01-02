package carga.system;

/**
 * 
 * @author Edgard Leal
 */
public enum SystemProfile {

  /**
     *
     */
  DESE("dese"), HOMOL("homol"), PROD("prod");
  private String description;

  private SystemProfile(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return getDescription();
  }

  public boolean equal(Object obj) {
    if (obj == null) {
      return false;
    }
    return obj.toString().equals(description);
  }

  public String getDescription() {
    return description;
  }

  public static SystemProfile getByDescription(final String description) {

    if (description != null) {
      if (description.equalsIgnoreCase(DESE.getDescription())) {
        return DESE;
      } else if (description.equalsIgnoreCase(HOMOL.getDescription())) {
        return HOMOL;
      }
    }

    return PROD;
  }

}
