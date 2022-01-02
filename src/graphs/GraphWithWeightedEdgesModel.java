package graphs;

import java.lang.reflect.Member;


import javax.swing.table.AbstractTableModel;

public class GraphWithWeightedEdgesModel extends AbstractTableModel {
	
	
	private GraphWithWeightedEdges graph;

	public GraphWithWeightedEdgesModel(GraphWithWeightedEdges graph) {
		this.graph = graph;
	}

	//add member from graph
	public GraphWithWeightedEdges getGraph() {
		return graph;
	}
	
	//titel of Column---------------------------------------
	@Override
	public Class<?> getColumnClass(int col) {
		return String.class; 
		
		
	}

	@Override
	public int getColumnCount() {

		return graph.getN() + 1; //for node feld
	}

	@Override
	public int getRowCount() {

		return graph.getN();

	}

	@Override
	public String getColumnName(int col) {

		if (col==0) {
			return "Knoten";
		}else  {
			return graph.getName(col - 1);
			
		}
		
	}

	
	@Override
	public Object getValueAt(int row, int col) {
		// elemnts of Table

		if (col==0) {
			return graph.getName(row);
		}else  {
			return graph.getWeight(row, col - 1);
		}
		
	}

}
