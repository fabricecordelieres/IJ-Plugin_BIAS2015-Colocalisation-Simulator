
import ij.plugin.PlugIn;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.TitledBorder;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;

import BIAS_Coloc_Simulator.GUI.parameters.parameters;
import BIAS_Coloc_Simulator.simulator.simulator;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * BIAS_Coloc_Simulator_GUI.java v1, 29 mai 2015
    Fabrice P Cordelieres, fabrice.cordelieres at gmail.com
    
    Copyright (C) 2015 Fabrice P. Cordelieres
  
    License:
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * @author Fabrice P Cordelieres
 *
 */
public class BIAS_Coloc_Simulator_GUI extends JDialog implements PlugIn{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable tableChannel1;
	private JTable tableChannel2;
	private JPanel buttonPane;
	private JPanel panel;
	private JPanel panelChannel1;
	private JPanel panelChannel2;
	private JTable tableGlobalParameters;
	private JSpinner spinnerMethod;
	
	public static final String INCLUSION="Inclusion (Channel 1=hollow elements, Channel2=filled elements)";
	public static final String EXCLUSION="Exclusion (not implemented)";
	public static final String COLOC="Pure colocalisation (Channel 1=Channel 2)";
	public static final String RANDOM="Random";
	public static final String[] METHODS=new String[] {INCLUSION, EXCLUSION, COLOC, RANDOM};
	
	/* (non-Javadoc)
	 * @see ij.plugin.PlugIn#run(java.lang.String)
	 */
	@Override
	public void run(String arg) {
		main(null);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			BIAS_Coloc_Simulator_GUI dialog = new BIAS_Coloc_Simulator_GUI();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public BIAS_Coloc_Simulator_GUI() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("BIAS Colocalisation Simulator");
		setBounds(100, 100, 575, 520);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		{
			buttonPane = new JPanel();
			springLayout.putConstraint(SpringLayout.WEST, buttonPane, 0, SpringLayout.WEST, getContentPane());
			springLayout.putConstraint(SpringLayout.SOUTH, buttonPane, 0, SpringLayout.SOUTH, getContentPane());
			springLayout.putConstraint(SpringLayout.EAST, buttonPane, 0, SpringLayout.EAST, getContentPane());
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						generateSimulation();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		{
			panel = new JPanel();
			springLayout.putConstraint(SpringLayout.NORTH, panel, 0, SpringLayout.NORTH, getContentPane());
			springLayout.putConstraint(SpringLayout.SOUTH, panel, -6, SpringLayout.NORTH, buttonPane);
			springLayout.putConstraint(SpringLayout.WEST, panel, 0, SpringLayout.WEST, getContentPane());
			springLayout.putConstraint(SpringLayout.EAST, panel, 0, SpringLayout.EAST, getContentPane());
			getContentPane().add(panel);
			SpringLayout sl_panel = new SpringLayout();
			panel.setLayout(sl_panel);
			
			JPanel panelGlobalParameters = new JPanel();
			sl_panel.putConstraint(SpringLayout.NORTH, panelGlobalParameters, 0, SpringLayout.NORTH, panel);
			sl_panel.putConstraint(SpringLayout.WEST, panelGlobalParameters, 0, SpringLayout.WEST, panel);
			sl_panel.putConstraint(SpringLayout.SOUTH, panelGlobalParameters, 160, SpringLayout.NORTH, panel);
			sl_panel.putConstraint(SpringLayout.EAST, panelGlobalParameters, 575, SpringLayout.WEST, panel);
			panel.add(panelGlobalParameters);
			panelGlobalParameters.setBorder(new TitledBorder(null, "Global parameters", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			SpringLayout sl_panelGlobalParameters = new SpringLayout();
			panelGlobalParameters.setLayout(sl_panelGlobalParameters);
			
			JScrollPane scrollPane = new JScrollPane();
			sl_panelGlobalParameters.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, panelGlobalParameters);
			sl_panelGlobalParameters.putConstraint(SpringLayout.SOUTH, scrollPane, 0, SpringLayout.SOUTH, panelGlobalParameters);
			sl_panelGlobalParameters.putConstraint(SpringLayout.EAST, scrollPane, 0, SpringLayout.EAST, panelGlobalParameters);
			panelGlobalParameters.add(scrollPane);
			
			tableGlobalParameters = new JTable();
			tableGlobalParameters.setFillsViewportHeight(true);
			tableGlobalParameters.setModel(getGlobalParametersModel());
			scrollPane.setViewportView(tableGlobalParameters);
			
			spinnerMethod = new JSpinner();
			sl_panelGlobalParameters.putConstraint(SpringLayout.NORTH, scrollPane, 0, SpringLayout.SOUTH, spinnerMethod);
			sl_panelGlobalParameters.putConstraint(SpringLayout.NORTH, spinnerMethod, 0, SpringLayout.NORTH, panelGlobalParameters);
			sl_panelGlobalParameters.putConstraint(SpringLayout.WEST, spinnerMethod, 59, SpringLayout.WEST, panelGlobalParameters);
			spinnerMethod.setModel(new SpinnerListModel(METHODS));
			panelGlobalParameters.add(spinnerMethod);
			
			panelChannel1 = new JPanel();
			sl_panel.putConstraint(SpringLayout.NORTH, panelChannel1, 6, SpringLayout.SOUTH, panelGlobalParameters);
			sl_panel.putConstraint(SpringLayout.WEST, panelChannel1, 0, SpringLayout.WEST, panelGlobalParameters);
			sl_panel.putConstraint(SpringLayout.SOUTH, panelChannel1, 141, SpringLayout.SOUTH, panelGlobalParameters);
			sl_panel.putConstraint(SpringLayout.EAST, panelChannel1, 0, SpringLayout.EAST, panelGlobalParameters);
			panel.add(panelChannel1);
			panelChannel1.setBorder(new TitledBorder(null, "Channel 1", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			SpringLayout sl_panelChannel1 = new SpringLayout();
			panelChannel1.setLayout(sl_panelChannel1);
			{
				JScrollPane scrollPaneChannel1 = new JScrollPane();
				sl_panelChannel1.putConstraint(SpringLayout.NORTH, scrollPaneChannel1, 0, SpringLayout.NORTH, panelChannel1);
				sl_panelChannel1.putConstraint(SpringLayout.WEST, scrollPaneChannel1, 0, SpringLayout.WEST, panelChannel1);
				sl_panelChannel1.putConstraint(SpringLayout.SOUTH, scrollPaneChannel1, 0, SpringLayout.SOUTH, panelChannel1);
				sl_panelChannel1.putConstraint(SpringLayout.EAST, scrollPaneChannel1, 0, SpringLayout.EAST, panelChannel1);
				panelChannel1.add(scrollPaneChannel1);
				{
					tableChannel1 = new JTable();
					tableChannel1.setFillsViewportHeight(true);
					tableChannel1.setModel(getChannelModel(1));
					scrollPaneChannel1.setViewportView(tableChannel1);
				}
			}
			
			panelChannel2 = new JPanel();
			sl_panel.putConstraint(SpringLayout.NORTH, panelChannel2, 6, SpringLayout.SOUTH, panelChannel1);
			sl_panel.putConstraint(SpringLayout.WEST, panelChannel2, 0, SpringLayout.WEST, panel);
			sl_panel.putConstraint(SpringLayout.SOUTH, panelChannel2, 141, SpringLayout.SOUTH, panelChannel1);
			sl_panel.putConstraint(SpringLayout.EAST, panelChannel2, 0, SpringLayout.EAST, panelGlobalParameters);
			panel.add(panelChannel2);
			panelChannel2.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Channel 2", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			SpringLayout sl_panelChannel2 = new SpringLayout();
			panelChannel2.setLayout(sl_panelChannel2);
			{
				JScrollPane scrollPaneChannel2 = new JScrollPane();
				sl_panelChannel2.putConstraint(SpringLayout.NORTH, scrollPaneChannel2, 0, SpringLayout.NORTH, panelChannel2);
				sl_panelChannel2.putConstraint(SpringLayout.WEST, scrollPaneChannel2, 0, SpringLayout.WEST, panelChannel2);
				sl_panelChannel2.putConstraint(SpringLayout.SOUTH, scrollPaneChannel2, 0, SpringLayout.SOUTH, panelChannel2);
				sl_panelChannel2.putConstraint(SpringLayout.EAST, scrollPaneChannel2, 0, SpringLayout.EAST, panelChannel2);
				panelChannel2.add(scrollPaneChannel2);
				{
					tableChannel2 = new JTable();
					tableChannel2.setFillsViewportHeight(true);
					tableChannel2.setModel(getChannelModel(2));
					scrollPaneChannel2.setViewportView(tableChannel2);
				}
			}
		}
		{	
		}
	}
	
	/**
	 * Model to be used to fill the channel table
	 * @param channel channel number: 1 or 2
	 * @return a model to be used to fill the channel table
	 */
	DefaultTableModel getChannelModel(int channel){
		String[] columnNames=new String[]{"Element", "Draw", "Nb of items", "Width", "Height", "Depth", "R", "r"};
		
		Object[][] data=
				channel==1?
				new Object[][]{
				{"Parallelepiped", Boolean.TRUE, new Integer(4), new Integer(32), new Integer(32), new Integer(32), null, null},
				{"Ellipsoid", Boolean.TRUE, new Integer(4), new Integer(32), new Integer(32), new Integer(32), null, null},
				{"Cylinder", Boolean.TRUE, new Integer(4), new Integer(32), new Integer(32), new Integer(32), null, null},
				{"Cone", Boolean.TRUE, new Integer(4), new Integer(32), new Integer(32), new Integer(32), null, null},
				{"Torus", Boolean.TRUE, new Integer(4), new Integer(32), new Integer(32), new Integer(32), new Integer(32), new Integer(4)}
				}
				:
				new Object[][]{
				{"Parallelepiped", Boolean.TRUE, new Integer(4), new Integer(16), new Integer(16), new Integer(16), null, null},
				{"Ellipsoid", Boolean.TRUE, new Integer(4), new Integer(16), new Integer(16), new Integer(16), null, null},
				{"Cylinder", Boolean.TRUE, new Integer(4), new Integer(16), new Integer(16), new Integer(16), null, null},
				{"Cone", Boolean.TRUE, new Integer(4), new Integer(16), new Integer(16), new Integer(16), null, null},
				{"Torus", Boolean.TRUE, new Integer(4), new Integer(16), new Integer(16), new Integer(16), new Integer(16), new Integer(2)}
				};
		
		DefaultTableModel out=new DefaultTableModel(data, columnNames) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {String.class, Boolean.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class};
			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public Class getColumnClass(int col){
				return columnTypes[col];
			}
			
			@Override
			public boolean isCellEditable(int row, int col){
				return col!=0?true:false;
			}
		}; 
		
		return out;
	}
	
	/**
	 * Model to be used to fill the global parameters table
	 * @return a model to be used to fill the global parameters table
	 */
	DefaultTableModel getGlobalParametersModel(){
		String[] columnNames=new String[]{"Parameter", "Use", "X or %ch1>ch2", "Y or %ch2>ch1", "Z"};
		
		Object[][] data=new Object[][]{
				{"Image dimensions", null, new Integer(128), new Integer(128), new Integer(128)},
				{"Chromatic shift", true, new Integer(1), new Integer(1), new Integer(2)},
				{"Field non-homogeneity", true, new Integer(32), new Integer(64), null},
				{"Bleedthrough", true, new Integer(10), new Integer(20), null},
				};
		
		DefaultTableModel out=new DefaultTableModel(data, columnNames) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {String.class, Boolean.class, Integer.class, Integer.class, Integer.class};
			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public Class getColumnClass(int col){
				return columnTypes[col];
			}
			
			@Override
			public boolean isCellEditable(int row, int col){
				return col!=0?true:false;
			}
		}; 
		
		return out;
	}
	
	/**
	 * Generates the simulation
	 */
	public void generateSimulation(){
		parameters param=new parameters((SpinnerListModel) spinnerMethod.getModel(), (DefaultTableModel) tableGlobalParameters.getModel(), (DefaultTableModel) tableChannel1.getModel(), (DefaultTableModel) tableChannel2.getModel());
		simulator sim=new simulator(param);
		sim.generate();
		sim.showImages();
		//System.out.println(param.toString());
	}
}
