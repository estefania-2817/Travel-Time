package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;
import java.awt.Color;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.BevelBorder;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.Component;

public class StationGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JLabel labelStation;
	private JLabel labelDeparture;
	private JLabel labelArrival;
	private JLabel insertCityLabel;
	private JLabel routeTraced;
	private JTextField insertCityText;
	private JToggleButton SubmitButton;
	

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StationGUI frame = new StationGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * Creates the frame, pane and table of the GUI.
	 */
	public StationGUI() {
		
		setTitle("PracticeSwing");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 600);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 179, 179));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
				
		
		JLabel paneTitle = new JLabel("Welcome to WestSide Station");
		paneTitle.setFont(new Font("Verdana", Font.BOLD, 20));
		paneTitle.setBounds(168, 11, 333, 39);
		contentPane.add(paneTitle);
		
		String[] columnNames = { "Station", "Departure", "Arrival" };
		 
        String[][] data = {
                { "Bugapest", "9:35 am", "11:53 am" }, // 138	x
                { "Dubay", "10:30 am", "2:40 pm" },  // 250		x
                { "Berlint", "8:25 pm", "2:40 am" }, // 375		x
                { "Mosbull", "6:00 pm", "9:50 pm" }, // 230		x
                { "Cayro", "6:40 am", "10:28 am" },  // 228		x
                { "Bostin", "10:25 am", "1:38 pm" }, // 193		x
                { "Los Angelos", "12:30 pm", "5:55 pm" },//325	x
                { "Dome", "1:30 pm", "7:25 pm" }, // 355 		x
                { "Takyo", "3:35 pm", "8:45 pm" }, /// 310		x
                { "Unstanbul", "4:45 pm", "9:50 pm" }, // 305	x
                { "Chicargo", "7:25 am", "1:53 pm" },  // 388	x
                { "Looondun", "12:00 pm", "5:15 pm" } // 315	x

            };
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
		table = new JTable(data, columnNames);
		table.setShowVerticalLines(false);
		table.setBackground(new Color(203, 249, 254));
		table.setShowHorizontalLines(false);
		table.setRowHeight(25);
		table.setFillsViewportHeight(true);
		table.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		table.setFont(new Font("Verdana", Font.ITALIC, 16));
		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		table.setBounds(161, 79, 340, 333);
		contentPane.add(table);
		
		labelStation = new JLabel("Station");
		labelStation.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 18));
		labelStation.setBounds(178, 45, 79, 34);
		contentPane.add(labelStation);
		
		labelDeparture = new JLabel("Departure");
		labelDeparture.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 18));
		labelDeparture.setBounds(278, 45, 110, 34);
		contentPane.add(labelDeparture);
		
		labelArrival = new JLabel("Arrival");
		labelArrival.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 18));
		labelArrival.setBounds(407, 45, 79, 34);
		contentPane.add(labelArrival);
		
		insertCityLabel = new JLabel("Insert city here:");
		insertCityLabel.setFont(new Font("Verdana", Font.BOLD, 16));
		insertCityLabel.setBounds(122, 414, 146, 55);
		contentPane.add(insertCityLabel);
		
		insertCityText = new JTextField();
		insertCityText.setFont(new Font("Verdana", Font.PLAIN, 18));
		insertCityText.setBounds(278, 423, 110, 36);
		contentPane.add(insertCityText);
		insertCityText.setColumns(10);
		
		routeTraced = new JLabel("");
		routeTraced.setAlignmentX(Component.CENTER_ALIGNMENT);
		routeTraced.setFont(new Font("Verdana", Font.BOLD, 16));
		routeTraced.setBounds(30, 480, 582, 39);
		routeTraced.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(routeTraced);
		
		SubmitButton = new JToggleButton("Submit");
		SubmitButton.setFont(new Font("Verdana", Font.BOLD, 16));
		SubmitButton.setActionCommand("rememberToSetFunction");
		SubmitButton.addActionListener(new ActionListener() {
			/**
			 * This method analyzes the input data (city) in the text field (search bar) 
			 * after pressing the submit button.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				String insertedCity = insertCityText.getText();
				if(insertedCity.equals("Los Angelos") || insertedCity.equals("los angelos")) {
					routeTraced.setText("Westside->Bugapest->Cayro->Los Angelos");
				} else
				if(insertedCity.equals("Unstabul") || insertedCity.equals("unstanbul")) {
					routeTraced.setText("Westside->Bugapest->Unstabul");
				} else
				if(insertedCity.equals("Dome") || insertedCity.equals("dome")) {
					routeTraced.setText("Westside->Bugapest->Cayro->Dome");
				} else
				if(insertedCity.equals("Loondun") || insertedCity.equals("loondun")) {
					routeTraced.setText("Westside->Bugapest->Bostin->Loondun");
				} else
				if(insertedCity.equals("Mosbull") || insertedCity.equals("mosbull")) {
					routeTraced.setText("Westside->Mosbull");
				} else
				if(insertedCity.equals("Bugapest") || insertedCity.equals("bugapest")) {
					routeTraced.setText("Westside->Bugapest");
				} else
				if(insertedCity.equals("Bostin") || insertedCity.equals("bostin")) {
					routeTraced.setText("Westside->Bugapest->Bostin");
				} else
				if(insertedCity.equals("Berlint") || insertedCity.equals("berlint")) {
					routeTraced.setText("Westside->Dubay->Berlint");
				} else
				if(insertedCity.equals("Takyo") || insertedCity.equals("takyo")) {
					routeTraced.setText("Westside->Bugapest->Cayro->Takyo");
				} else
				if(insertedCity.equals("Cayro") || insertedCity.equals("cayro")) {
					routeTraced.setText("Westside->Bugapest->Cayro");
				} else
				if(insertedCity.equals("Dubay") || insertedCity.equals("dubay")) {
					routeTraced.setText("Westside->Dubay");
				} else
				if(insertedCity.equals("Chicargo") || insertedCity.equals("chicargo")) {
					routeTraced.setText("Westside->Bugapest->Bostin->Loondun->Chicargo");
				} else 
				{
					routeTraced.setText("City not found");
				}
				
			}
		});
		SubmitButton.setBounds(407, 430, 123, 23);
		contentPane.add(SubmitButton);
		
		


	}
}
