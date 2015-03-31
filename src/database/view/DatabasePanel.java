package database.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;

import database.controller.DatabaseAppController;

public class DatabasePanel extends JPanel
{
	/**
	 * The controller of the app
	 */
	private DatabaseAppController baseController;

	 //GUI Objects
	private SpringLayout baseLayout;
	private JScrollPane textPane;
	private JButton appButton;
	private JScrollPane displayPane;
	private JTextArea displayArea;
	private JTable tableData;
	private JPasswordField password;
	private TableCellWrapRenderer cellRenderer;

	/**
	 * The constructor of the panel class
	 * @param baseController the app controller
	 */
	public DatabasePanel(DatabaseAppController baseController)
	{
		this.baseController = baseController;

		baseLayout = new SpringLayout();
		cellRenderer = new TableCellWrapRenderer();
		appButton = new JButton("Test the Query");
		baseLayout.putConstraint(SpringLayout.WEST, appButton, 39, SpringLayout.WEST, this);
		baseLayout.putConstraint(SpringLayout.EAST, appButton, -44, SpringLayout.EAST, this);
		displayArea = new JTextArea(10, 30);
		displayPane = new JScrollPane(displayArea);

		setupDisplayPane();
		setupPanel();
		setupLayout();
		setupListeners();
		setupTable();
	}
	
	/**
	 * Sets up the JTable for viewing
	 */
	private void setupTable() {
		//One D array for column
		//2D array for contents)
		tableData = new JTable(new DefaultTableModel(baseController.getDatabase().realInfo(), baseController.getDatabase().getMetaData()));
		displayPane = new JScrollPane(tableData);
		for(int spot = 0; spot < tableData.getColumnCount(); spot++){
			tableData.getColumnModel().getColumn(spot).setCellRenderer(cellRenderer);
		}
	}
	
	/**
	 * Sets up the display pane in the JTable
	 */
	private void setupDisplayPane()
	{
		displayArea.setWrapStyleWord(true);
		displayArea.setLineWrap(true);
		displayArea.setEditable(false);
	}

	/**
	 * Sets up the default stuff for the panel
	 */
	private void setupPanel()
	{
		this.setBackground(Color.BLUE);
		this.setLayout(baseLayout);
		this.add(appButton);
		this.add(displayPane);
		//this.add(password);
		//password.setEchoChar('௵');
		//password.setForeground(Color.GREEN);
		//password.setFont(new Font("Serif", Font.BOLD, 30));

	}

	/**
	 * Base layout positions for the GUI objects
	 */
	private void setupLayout()
	{
		baseLayout.putConstraint(SpringLayout.NORTH, displayPane, 30, SpringLayout.NORTH, this);
		baseLayout.putConstraint(SpringLayout.WEST, displayPane, 39, SpringLayout.WEST, this);
		baseLayout.putConstraint(SpringLayout.EAST, displayPane, -44, SpringLayout.EAST, this);
		baseLayout.putConstraint(SpringLayout.SOUTH, appButton, -29, SpringLayout.SOUTH, this);
		baseLayout.putConstraint(SpringLayout.SOUTH, displayPane, -6, SpringLayout.NORTH, appButton);
	}

	/**
	 * Listeners for the GUI buttons
	 */
	private void setupListeners()
	{
		appButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent click)
			{
				String databaseAnswer = baseController.getDatabase().describeTable();
				displayArea.setText(databaseAnswer);
			}
		});
	}
}
