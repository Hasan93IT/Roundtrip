package graphs;

import java.io.Serializable;

/**
 * Graph models a graph consisting of nodes and edges.
 * 
 * 
 * @author Henning Dierks
 * @version 1.0
 */
public class Graph 
implements Serializable {

  private static final long serialVersionUID = 1L;
  private int N;             // number of nodes/vertices
  private boolean[][] edges; // edges between nodes, adjacency matrix
  private String[] names;    // nodes _may_ have names

  /**
   * Constructs a graph with N nodes and the edges as given. No names!
   * 
   * @param N is the number of nodes
   * @param edges describes whether two nodes are connected or not 
   * @throws WrongEdgeMatrixException is thrown if edges do not fit the number of nodes
   */
  public Graph(int N, boolean[][] edges) throws WrongEdgeMatrixException {
    this.N = N;
    names = new String[N]; // empty names!   
    setEdges(edges);
  }

  /**
   * Constructs a graph with N nodes without any edges. No names!
   * 
   * @param N is the number of nodes
   */
  public Graph(int N) {
    this.N = N;
    names = new String[N];     // empty names!   
    edges = new boolean[N][N]; // empty graph, no edges at all
  }

  // GETTER AND SETTER

  /**
   * Get the number of nodes.
   *  
   * @return number of nodes
   */
  public int getN() { 
    return N; 
  }

  
  /**
   * Get the name of a node. 
   * 
   * If no name was defined for the index-th node the name is given as "index". 
   * @param index is the index of the node
   * @return name of the index-th node
   */
  public String getName(int index) {
    if (names[index]!=null) {
      return names[index];
    }
    else {
      return ""+index;
    }
  }

  /**
   * Set the name of a node.
   * 
   * @param index is the index of the node
   * @param name is the new name of the index-th node
   */
  public void setName(int index, String name) { 
    names[index] = name;
  }

  /**
   * Get the matrix of edges.
   * 
   * @return matrix of edges as two-dimensional array
   */
  public boolean[][] getEdges() {
    return edges;
  }

  /**
   * Change the connection between two distinct nodes. 
   * 
   * @param i index of first node
   * @param j index of second node (order does not matter!)
   * @param connected whether nodes are connected or not connected
   * @throws WrongEdgeMatrixException is thrown iff the indices are ill-formed or not distinct.
   */
  public void setEdge(int i, int j, boolean connected) throws WrongEdgeMatrixException  {
    if (i<0 || j<0 || i>=N || j>=N) { // indices are not valid
      throw new WrongEdgeMatrixException("Index out of bounds: " + i + " or "+j+" with N="+N);
    }
    if (i==j && connected) {          // indices are equal -> illegal
        throw new WrongEdgeMatrixException("Graph shall not have self-loops");
    }
    // OK, everything is fine...
    edges[i][j]=connected;
    edges[j][i]=connected;
  }

  /**
   * Set all edges for the graph at once. 
   * @param edges the two-dimensional array containing information about edges
   * @throws WrongEdgeMatrixException is thrown iff edges are illegal
   */
  public void setEdges(boolean[][] edges) throws WrongEdgeMatrixException  {
    if (edges.length!=N) { // does not fit!
      throw new WrongEdgeMatrixException("Matrix of edges does not fit the number of edges");
    } else {
      for (int i=0; i<N; i++) {
        if (edges[i].length!=N) {
          throw new WrongEdgeMatrixException("Matrix of edges does not fit the number of edges");
        }
        // check symmetry
        for (int j=0; j<i; j++) {
          if (edges[i][j]!=edges[j][i]) {
            throw new WrongEdgeMatrixException("Matrix of edges is not symmetric");
          }
        }
        // check absence of self-loops
        if (edges[i][i]) {
          throw new WrongEdgeMatrixException("Graph shall not have self-loops");
        }
      }
    }
    // OK, everything is fine...
    this.edges = edges;
  }

  /**
   * Get the information whether two nodes are connected or not.
   * 
   * @param i first index
   * @param j second index (order does not matter!)
   * @return true iff nodes i and j are connected
   */
  public boolean isConnected(int i, int j) {
    return edges[i][j];
  }

  /**
   * Computes whether the *whole* graph is connected. 
   * 
   * Refer to your favourite book on graph theory to find
   * out what a connected graph is.
   * @return true iff graph is connected
   */
  public boolean isConnectedGraph() {
    boolean[] reachable = new boolean[N]; // will be true iff node is reachable from first node

    int hits=1; // number of hits/reachable nodes
    reachable[0]=true; // first node is of course reachable from first node

    boolean change=false; // repeat check for more reachable nodes (breadth-first-search)
    do {
      change=false; // nothing found in this round yet
      for (int i=0; i<N; i++) { 
        if (reachable[i]) { // if node i is reachable
           for (int j=0; j<N; j++) { // then check which nodes are connected to i
            if (isConnected(i,j) && !reachable[j]) { // j is connected and not found yet
              // Gotcha!
              reachable[j]=true;
              hits++; 
              change=true;
            }
          }
        }
      }
    } while(change); // repeat until no new reachable node is found

    // if number of hits is equal to N then _all_ nodes are reachable from
    // first node => graph is connected
    return hits==N;
  }
}
