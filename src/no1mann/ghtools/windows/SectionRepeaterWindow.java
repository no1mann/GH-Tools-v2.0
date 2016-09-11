package no1mann.ghtools.windows;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import no1mann.ghtools.ChartInterpreter;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class SectionRepeaterWindow extends JFrame {

	/**
	 * 
	 */
	public  static final long serialVersionUID = 1L;
	public  JPanel contentPane;
	public JTextField txtNumOfNotes;
	public JTextField txtStartingOffset;
	public JTextField txtEndingOffset;
	public  JTextField txtBaseOffset;
	public JTextField txtNameOfChart;
	public JLabel lblNameOfNew;
	public JLabel lblCurrentSong;
	public JButton btnCancel;
	public SectionRepeaterWindow sectionRepeaterWindow = this;
	public ChartInterpreter chart;
	public int counter = -1;
	private JButton btnSkipSong;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SectionRepeaterWindow frame = new SectionRepeaterWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 * @param window 
	 */
	public SectionRepeaterWindow(final ChartInterpreter chart) {
		this.chart = chart;
		setResizable(false);
		setTitle("3999+ Creator");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 360, 281);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		txtNumOfNotes = new JTextField();
		txtNumOfNotes.setColumns(10);
		
		JLabel lblNumberOfNotes = new JLabel("Number of Notes:");
		lblNumberOfNotes.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		txtStartingOffset = new JTextField();
		txtStartingOffset.setColumns(10);
		
		JLabel lblStartingOffset = new JLabel("Starting Offset:");
		lblStartingOffset.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		txtEndingOffset = new JTextField();
		txtEndingOffset.setColumns(10);
		
		JLabel lblEndingOffset = new JLabel("Ending Offset:");
		lblEndingOffset.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		txtBaseOffset = new JTextField();
		txtBaseOffset.setColumns(10);
		
		JLabel lblBaseOffset = new JLabel("Base Offset:");
		lblBaseOffset.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		txtNameOfChart = new JTextField();
		txtNameOfChart.setColumns(10);
		
		lblNameOfNew = new JLabel("Name of Chart:");
		lblNameOfNew.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//If any text field is empty
				if(txtNumOfNotes.getText().equalsIgnoreCase("") || txtStartingOffset.getText().equalsIgnoreCase("") ||
						txtEndingOffset.getText().equalsIgnoreCase("") || txtBaseOffset.getText().equalsIgnoreCase("") ||
						txtNameOfChart.getText().equalsIgnoreCase("")){
					JOptionPane.showMessageDialog(new JFrame(), "Error: Fill in every Text Box", "Error", JOptionPane.DEFAULT_OPTION);
				}
				else{
					sectionRepeaterWindow.setVisible(false);
					counter++;
					chart.sectionRepeater(sectionRepeaterWindow, counter);
				}
			}
		});
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(counter>0){
					chart.clearList();
					JOptionPane.showMessageDialog(new JFrame(), "Charts have been generated \n\n3999+Tool created by Threevan ", "Completed", JOptionPane.DEFAULT_OPTION);
				}
				sectionRepeaterWindow.dispatchEvent(new WindowEvent(sectionRepeaterWindow, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		chart.getInfo();
		lblCurrentSong = new JLabel("Current Song: " + chart.chartData.get(0).getFile().getName());
		lblCurrentSong.setHorizontalAlignment(SwingConstants.CENTER);
		lblCurrentSong.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		btnSkipSong = new JButton("Skip Song");
		btnSkipSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				counter++;
				if(counter+1>=chart.chartData.size()){
					chart.clearList();
					JOptionPane.showMessageDialog(new JFrame(), "Charts have been generated", "Completed", JOptionPane.DEFAULT_OPTION);
					sectionRepeaterWindow.dispatchEvent(new WindowEvent(sectionRepeaterWindow, WindowEvent.WINDOW_CLOSING));
				}
				else{
					setCurrentSong();
				}
			}
		});
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblEndingOffset, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
							.addGap(32)
							.addComponent(txtEndingOffset, GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblStartingOffset, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNumberOfNotes))
							.addGap(32)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(txtNumOfNotes, GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
								.addComponent(txtStartingOffset, GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblBaseOffset, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
							.addGap(32)
							.addComponent(txtBaseOffset, GroupLayout.PREFERRED_SIZE, 183, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblCurrentSong, GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblNameOfNew, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
							.addGap(32)
							.addComponent(txtNameOfChart, GroupLayout.PREFERRED_SIZE, 183, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnCancel, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnCreate, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnSkipSong, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblCurrentSong, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNumberOfNotes)
						.addComponent(txtNumOfNotes, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblStartingOffset, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtStartingOffset, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblEndingOffset, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtEndingOffset, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblBaseOffset, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtBaseOffset, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNameOfNew, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtNameOfChart, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnSkipSong, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnCreate, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	public void clearTextBoxes(){
		txtNumOfNotes.setText("");
		txtStartingOffset.setText("");
		txtEndingOffset.setText("");
		txtBaseOffset.setText("");
		txtNameOfChart.setText("");
	}
	
	public void setCurrentSong(){
		lblCurrentSong.setText("Current Song: " + chart.chartData.get(counter+1).getFile().getName());
	}
}
