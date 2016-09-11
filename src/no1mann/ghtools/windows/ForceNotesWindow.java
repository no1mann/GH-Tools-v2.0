package no1mann.ghtools.windows;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import no1mann.ghtools.ChartInterpreter;

import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

public class ForceNotesWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField fieldTapNote;
	private JTextField fieldForcedNote;
	private ForceNotesWindow forceNotesWindow = this;
	private ChartInterpreter chartInterpreter;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ForceNotesWindow frame = new ForceNotesWindow();
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
	public ForceNotesWindow() {
		setResizable(false);
		setTitle("Force Notes");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 285, 152);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblForcedNoteNotation = new JLabel("Forced Note Notation:");
		lblForcedNoteNotation.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblTapNoteNotation = new JLabel("Tap Note Notation:");
		lblTapNoteNotation.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		fieldTapNote = new JTextField();
		fieldTapNote.setColumns(10);
		
		fieldForcedNote = new JTextField();
		fieldForcedNote.setColumns(10);
		
		JButton btcCancel = new JButton("Cancel");
		btcCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				forceNotesWindow.dispatchEvent(new WindowEvent(forceNotesWindow, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		JButton btnDone = new JButton("Done");
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chartInterpreter.forceNotes(fieldForcedNote.getText(), fieldTapNote.getText());
				forceNotesWindow.dispatchEvent(new WindowEvent(forceNotesWindow, WindowEvent.WINDOW_CLOSING));
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblForcedNoteNotation)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(fieldForcedNote, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblTapNoteNotation, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(fieldTapNote, GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(btcCancel, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
							.addComponent(btnDone, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblForcedNoteNotation)
						.addComponent(fieldForcedNote, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTapNoteNotation, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(fieldTapNote, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(btnDone, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(btcCancel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
					.addGap(22))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	public void setChartInterpreter(ChartInterpreter chartInterpreter){
		this.chartInterpreter = chartInterpreter;
	}
}
