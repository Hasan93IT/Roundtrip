package graphs;

/**
 * Exceptions for illegal construction of graphs.
 * 
 * @author Henning Dierks
 * @version 1.0
 */
public class WrongEdgeMatrixException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * No Mumbo-Jumbo here. Just an exception carrying a text.
   * @param description of the problem
   */
  public WrongEdgeMatrixException(String description) {
    super(description);
  }

}
