package graphs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
// thread ----- table ----------JSplitPane--- file
public class GraphTable extends JFrame implements ActionListener {

	private JTable table;
	GraphWithWeightedEdgesModel model;
	
	JButton load;
	JButton save;
	JButton go;
	
	private JTextArea text;
	
	private JComboBox<Integer> comboBoxOfCity; // CLASS Elements: Integer
	private JComboBox<Integer> comboBoxOfThreads; // CLASS Elements: Integer
	private Integer[] selectCities = new Integer[31];
	private Integer[] Threads = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
	Roundtrip trip1;
	public GraphTable() throws HeadlessException {
		super();

		for (int i = 0; i <= 30; i++) {
			selectCities[i] = i + 2;
		}

		try {
			model = new GraphWithWeightedEdgesModel(new GraphWithWeightedEdges(2, new int[][] { { 0, 1 }, { 1, 0 } }));
		} catch (WrongEdgeMatrixException e) {
			e.printStackTrace();
		}

		// comboBox--------------------------------------------
		comboBoxOfCity = new JComboBox<Integer>(selectCities);
		comboBoxOfCity.addActionListener(this);
		comboBoxOfThreads = new JComboBox<Integer>(Threads);
		comboBoxOfThreads.addActionListener(this);

		comboBoxOfCity.setSelectedIndex(0);
		comboBoxOfThreads.setSelectedIndex(0);

		// Buttons---------------------------------------------
		load = new JButton("Laden");
		load.addActionListener(this); // subscribe to information
		save = new JButton("Speichern");
		save.addActionListener(this); // subscribe to information
		go = new JButton("Start");
		go.addActionListener(this);

		// Layout
		JPanel panel = new JPanel();
		//JPanel north = new JPanel();
		
		Container cnt = getContentPane();
		cnt.setLayout(new BorderLayout());
		
		//cnt.add(north, BorderLayout.CENTER);
		
		table = new JTable(model);
		

		text = new JTextArea("", 20, 10);
		//cnt.add(new JScrollPane(table), BorderLayout.NORTH);
		//cnt.add(new JScrollPane(text), BorderLayout.CENTER);
		//JScrollPane s=	new JScrollPane(text);
		panel.add(new JLabel("Threads:"));
		panel.add(comboBoxOfThreads);
		panel.add(new JLabel("Städte:"));
		panel.add(comboBoxOfCity);
		panel.add(load);
		panel.add(save);

		panel.add(go);

		cnt.add(panel, BorderLayout.SOUTH);// add buttons and lablels on south of frame
		
		//----------------------JSplitPane-----------------------------------------------------------
		//table.setPreferredSize ( new Dimension (350 ,350));//-----min size of table
		//text.setPreferredSize ( new Dimension (350 ,350));//-----min size of text
		JSplitPane splitVert = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true, new JScrollPane(table),new JScrollPane(text));// new JSplitPane between table and text
		// splitVert  work also without JScrollPane
		splitVert.setDividerLocation(0.90);//50% for table 
		cnt.add(splitVert);
		
		//------------------------------------------------------------------------------------------
		// we want scrolling
		setMinimumSize(new Dimension(1100, 800));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 800);
		// pack();
		setVisible(true);//// frame is Visible
		

	}

	public void actionPerformed(ActionEvent ev) {

		if (ev.getSource() == load) {

			JFileChooser fc = new JFileChooser();
			if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { // user pressed OK
				File loadFrom = fc.getSelectedFile();
				ObjectInputStream in = null;
				try {
					in = new ObjectInputStream(new FileInputStream(loadFrom));
					// read a single graph
					model = new GraphWithWeightedEdgesModel((GraphWithWeightedEdges) in.readObject());
					table.setModel(model); // tell table that we have a new model
					
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} finally { // in all cases close infile
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					System.out.println("hallo");
				}
			}
			
		} else if (ev.getSource() == go) {
			Roundtrip trip = new Roundtrip(model.getGraph(), text);

			// NOTICE : this method should not do any complicated
			// computation ! Hence , we put the computation into a
			// dedicated thread . Consequence : method is finished
			// after few steps

			Thread t = new Thread() { // only one thread
				public void run() {
					trip.compute((int) comboBoxOfThreads.getSelectedItem(), (int) comboBoxOfCity.getSelectedItem(), go);
				}
			};

			t.start();
			}
//-----------save----------------
//		else if (ev.getSource() == save) {
//			
//			JFileChooser fc = new JFileChooser();
//			fc.setFileSelectionMode(JFileChooser.FILES_ONLY); // allow Selection just one file
//			fc.setMultiSelectionEnabled(false);
//			
//			if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { // user pressed OK
//				File saveForm = fc.getSelectedFile();
//				
//				ObjectOutputStream out = null;
//				try {
//					out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(saveForm)));
//				
//					// read a single graph
//					//table.getModel();
//					out.writeObject(table.getModel()); //write the Vector of member in out
//					out.flush();
//					//model = new GraphWithWeightedEdgesModel((GraphWithWeightedEdges) out.readObject());
//					//table.setModel(model); // tell table that we have a new model
//				} catch (IOException e) {
//					e.printStackTrace();
//				} finally { // in all cases close infile
//					if (out != null) {
//						try {
//							out.close();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//			
//			// OutputFile("d:/newfile.txt");
//
//		    }
	}

	
	


//	public void OutputFile(String phad) {
//		
//		// File file = new File("d:/newfile.txt");
//		 File file = new File(phad);
//		 
//		 // get text from text Area 
//		  String content = text.getText();
//	        try (FileOutputStream fop = new FileOutputStream(file)) {
//
//	            // if file doesn't exists, then create it
//	            if (!file.exists()) {
//	                file.createNewFile();
//	            }
//
//	            // get the content in bytes
//	            byte[] contentInBytes = content.getBytes();
//
//	            fop.write(contentInBytes);
//	            fop.flush();
//	            fop.close();
//	            //	to confirm the Save
//	            System.out.println("Daten sind erfolgreich gespeichert");
//	             JOptionPane . showMessageDialog (this , " Daten sind erfolgreich gespeichert ",
//	            		 " Meldung ",JOptionPane . PLAIN_MESSAGE );
//	           
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	            JOptionPane . showMessageDialog (this , " Problem beim Speichern ",
//	            		 " ERROR_MESSAGE ",
//	            		 JOptionPane . ERROR_MESSAGE );
//	        }
//		
//		
//	}
	
	
	// main
	public static void main(String[] args) {
		new GraphTable();
	}
}
