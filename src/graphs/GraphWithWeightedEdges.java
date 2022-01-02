package graphs;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * This class models a graph consisting of nodes and edges where the edges carry weights.
 * 
 * @author Hasan Abdullah
 * @version 1.0
 */
public class GraphWithWeightedEdges extends Graph 
implements Serializable {


  private static final long serialVersionUID = 1L;
  private int weights[][]; // the (additional) weights of the edges

  /**
   * Constructs a graph with weights as given.
   * 
   * Edges are computes from the weights.
   *  
   * @param N the number of nodes/vertices
   * @param weights the weights for the edges
   * @throws WrongEdgeMatrixException is thrown if weights do not fit the number of nodes
   */
  public GraphWithWeightedEdges(int N, int[][] weights) throws WrongEdgeMatrixException {
    super(N); // construct a graph first

    // compute the edges from the weights
    boolean[][] edges = new boolean[N][N];
    int i=0,j=0;
    try {
      for (i=0; i<N; i++) {
        for (j=0; j<N; j++) {
          edges[i][j] = (weights[i][j]>0); // edge iff the given weight is positive
        }
      }
    } catch (ArrayIndexOutOfBoundsException outOfBounds) { // happens iff matrix of weights does not fit
      throw new WrongEdgeMatrixException("Index out of bounds: " + i + " or "+j+" with N="+N);
    }
    if (!isSymmetric(weights)) { // check symmetry
      throw new WrongEdgeMatrixException("Matrix of edges is not symmetric");
    }
    setEdges(edges); // may throw exception iff edges are still illegal (self-loops)
    this.weights=weights;
  }

  /**
   * Constructs a graph with N nodes without any edges or weights. No names!
   * 
   * @param N is the number of nodes
   */
  public GraphWithWeightedEdges(int N) {
    super(N); // graph with no edges
    this.weights=new int[N][N]; // hence all weights are empty!
  }

  /**
   * Checks a matrix of weights for symmetry.
   * 
   * @param w the matrix of weights
   * @return true iff matrix is symmetric
   */
  private boolean isSymmetric(int[][] w) {
    for (int i=0; i<w.length; i++) {
      for (int j=0; j<i; j++) {
        if (w[i][j]!=w[j][i]) return false;  // Ooops! Not symmetric!
      }
      if (w[i][i]!=0) return false; // no self-loops allowed!
    }
    // OK. Everything is fine...
    return true;
  }


  // GETTER AND SETTER


  /**
   * Get the weight of the edge between two nodes.
   * 
   * @param i index of first node
   * @param j index of second node (order does not matter!)
   * @return weight of the edge between the nodes. Weight 0 means that there is no edge.
   */
  public int getWeight(int i, int j) {
    return weights[i][j];
  }

  /**
   * Set the weight of the edge between two nodes.
   * 
   * @param i index of first node
   * @param j index of second node (order does not matter!)
   * @param w weight of the edge between the nodes. Weight 0 means that there is no edge.
   * @throws WrongEdgeMatrixException 
   */
  public void setWeight(int i, int j, int w) throws WrongEdgeMatrixException {
    // if w>0 the nodes are connected. 
    // If w==0 they are not connected.
    setEdge(i,j,w>0); // may throw exceptions, all checks are done here
    weights[i][j] = w;
    weights[j][i] = w;
  }

  /**
   * We want to have "nice" number if nodes don't have a name...
   */
  private static final DecimalFormat df = new DecimalFormat("###0");


  /**
   * Produces a textual matrix containing the names and weights.
   */
  @Override
  public String toString() {
    String result ="";
    for (int i=0; i<getN();i++) {
      // We start with the name
      result += getName(i) + " : ";
      for (int j=0; j<getN(); j++) { // followed by all weights
        result +=df.format(weights[i][j]) + " ";
      }
      result +="\r\n"; // newline
    }
    return result;
  }


  /**
   * A minimalistic main function for testing purposes.
   * 
   * @param args are not used here
   */
  public static void main(String[] args) {
    try {

      GraphWithWeightedEdges 
      g1 = new GraphWithWeightedEdges(2, new int[][] {{0,1},{1,0}});
      System.out.println(g1);

      GraphWithWeightedEdges g2 = 
          new GraphWithWeightedEdges(3, new int[][] {{0,1,3},{1,0,2},{3,2,0}});
      System.out.println(g2);



    } catch (WrongEdgeMatrixException e) {
      e.printStackTrace();
    }
  }


}
