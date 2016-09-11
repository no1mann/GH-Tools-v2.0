package no1mann.ghtools.windows;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import no1mann.ghtools.NPSCalculator;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.Dimension;

public class NPSCalculatorWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtBPM;
	private JTextField txtStep;
	private JTextField txtNPS;
	private NPSCalculatorWindow npsCalculatorWindow = this;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NPSCalculatorWindow frame = new NPSCalculatorWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public NPSCalculatorWindow() {
		setResizable(false);
		setTitle("NPS Calculator");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 342, 188);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblBpm = new JLabel("Beats Per Minute (BPM):");
		lblBpm.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblStepWould = new JLabel("Step (1/32 would enter 32):");
		lblStepWould.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblNotesPerSecond = new JLabel("Notes per Second (NPS):");
		lblNotesPerSecond.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		txtBPM = new JTextField();
		txtBPM.setColumns(10);
		
		txtStep = new JTextField();
		txtStep.setColumns(10);
		
		txtNPS = new JTextField();
		txtNPS.setColumns(10);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				npsCalculatorWindow.dispatchEvent(new WindowEvent(npsCalculatorWindow, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		JButton btnCalculate = new JButton("Calculate");
		btnCalculate.setPreferredSize(new Dimension(65, 23));
		btnCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(txtStep.getText().equals("") && !txtBPM.getText().equals("") && !txtNPS.getText().equals("")){
					double bpm = Double.parseDouble(txtBPM.getText());
					double nps = Double.parseDouble(txtNPS.getText());
					double step = NPSCalculator.getStep(bpm, nps);
					txtStep.setText(step + "");
				}
				else if(!txtStep.getText().equals("") && txtBPM.getText().equals("") && !txtNPS.getText().equals("")){
					double nps = Double.parseDouble(txtNPS.getText());
					double step = Double.parseDouble(txtStep.getText());
					double bpm = NPSCalculator.getBPM(step, nps);
					txtBPM.setText(bpm + "");
				}
				else if(!txtStep.getText().equals("") && !txtBPM.getText().equals("") && txtNPS.getText().equals("")){
					double bpm = Double.parseDouble(txtBPM.getText());
					double step = Double.parseDouble(txtStep.getText());
					double nps = NPSCalculator.getNPS(step, bpm);
					txtNPS.setText(nps + "");
				}
				else{
				}
			}
		});
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtNPS.setText("");
				txtBPM.setText("");
				txtStep.setText("");
			}
		});
		btnClear.setPreferredSize(new Dimension(65, 23));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblStepWould, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(txtStep, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblBpm)
							.addPreferredGap(ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
							.addComponent(txtBPM, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNotesPerSecond, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(btnExit, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(btnClear, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(txtNPS, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnCalculate, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBpm)
						.addComponent(txtBPM, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblStepWould, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtStep, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNotesPerSecond, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtNPS, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(btnExit, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnCalculate, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnClear, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
}
