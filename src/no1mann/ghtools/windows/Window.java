package no1mann.ghtools.windows;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import no1mann.ghtools.ChartInterpreter;
import no1mann.ghtools.DragDropListener;
import no1mann.ghtools.database.Database;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.dnd.DropTarget;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Desktop;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Window extends JFrame {

	private static final long serialVersionUID = 1L;
	private DragDropListener dragDropListener;
	@SuppressWarnings("rawtypes")
	private DefaultListModel listModel = new DefaultListModel();
	@SuppressWarnings("rawtypes")
	private JList fileList;
	private Database database = new Database(this);
	private Window window = this;
	private List<File> files = new ArrayList<File>();
	private ChartInterpreter chart;
	
	private JPanel contentPane;
	private JTextField txtSetDestination;
	@SuppressWarnings("rawtypes")
	private JComboBox saveOption;
	private JButton btnSetDestination;
	private JButton btnSave;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window frame = new Window();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Window() {
		setTitle("GH Tools v2.0 by No1mann");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 622, 583);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel title = new JLabel("GH Tools");
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Verdana", Font.BOLD, 36));
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				database.setOutputFolder(txtSetDestination.getText());
			}
		});
		
		JButton btnAddFiles = new JButton("Add Files");
		btnAddFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addFiles();
			}
		});
		
		JButton btnRemoveFiles = new JButton("Remove Files");
		btnRemoveFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteFiles();
			}
		});
		
		JButton btnTempoChanger = new JButton("Change Tempo");
		btnTempoChanger.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chart.speedUp();
			}
		});
		
		JButton btn3999Creator = new JButton("3999+ Creator");
		btn3999Creator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getFiles().size()==0){
					JOptionPane.showMessageDialog(new JFrame(), "No files detected", "Error", JOptionPane.DEFAULT_OPTION);
					return;
				}
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							SectionRepeaterWindow frame = new SectionRepeaterWindow(chart);
							frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		
		JButton btnForceNotes = new JButton("Force Notes");
		btnForceNotes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getFiles().size()==0){
					JOptionPane.showMessageDialog(new JFrame(), "No files detected", "Error", JOptionPane.DEFAULT_OPTION);
					return;
				}
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							ForceNotesWindow frame = new ForceNotesWindow();
							frame.setVisible(true);
							frame.setChartInterpreter(chart);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				//chart.forceNotes();
			}
		});
		
		JButton btnUnforceNotes = new JButton("Unforce Notes");
		btnUnforceNotes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chart.unforceNotes();
			}
		});
		
		JLabel lblModifications = new JLabel("Modifications");
		lblModifications.setFont(new Font("Verdana", Font.PLAIN, 18));
		lblModifications.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton btnNoteShuffle = new JButton("Note Shuffle");
		btnNoteShuffle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chart.shuffleNotes();
			}
		});
		
		JButton btnMirrorMode = new JButton("Mirror Mode");
		btnMirrorMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chart.mirrorCharts();
			}
		});
		
		JButton btnChartToArray = new JButton("Chart -> Array");
		btnChartToArray.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chart.chatToArray();
			}
		});
		
		JButton btnDBCToChart = new JButton("DBC -> Chart");
		btnDBCToChart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chart.dbcToChart();
			}
		});
		
		JButton btnReverser = new JButton("Reverse Chart");
		btnReverser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chart.reverseChart();
			}
		});
		
		JButton btnAllTapNotes = new JButton("Force All Taps");
		btnAllTapNotes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chart.forceAllTaps();
			}
		});
		
		JButton btnAddSections = new JButton("Add Sections");
		btnAddSections.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chart.addSections();
			}
		});
		
		JButton btnCalculateNPS = new JButton("Calculate NPS");
		btnCalculateNPS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
			}
		});
		
		JLabel lblImportantTools = new JLabel("Creator Tools");
		lblImportantTools.setHorizontalAlignment(SwingConstants.CENTER);
		lblImportantTools.setFont(new Font("Verdana", Font.PLAIN, 18));
		
		fileList = new JList(listModel);
		fileList.addMouseListener(new MouseAdapter() {
			 int clickCounter = 0;
			 @Override
			public void mouseClicked(MouseEvent mouseEvent) {
				JList theList = (JList) mouseEvent.getSource();
				clickCounter++;
				if (clickCounter >= 2 && mouseEvent.getClickCount()!=1) {
					clickCounter=0;
					int index = theList.locationToIndex(mouseEvent.getPoint());
					 if ( SwingUtilities.isRightMouseButton(mouseEvent) ){
						 List<File> files = getFiles();
						 files.remove(index);
						 updateList(files);
					 }
					 else if(SwingUtilities.isLeftMouseButton(mouseEvent)){
						 if (index >= 0) {
								File selectedFile = getFiles().get(index);
								try {
									Desktop.getDesktop().open(selectedFile);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
					 }
				}
			}
		});
		fileList.addKeyListener(new MyKeyListener());
		dragDropListener = new DragDropListener(this);
		new DropTarget(fileList, dragDropListener);
		JScrollPane scroller = new JScrollPane(fileList);
		
		txtSetDestination = new JTextField();
		txtSetDestination.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					database.setOutputFolder(txtSetDestination.getText());
				}
			}
		});
		txtSetDestination.setText(database.getOutputFolder());
		txtSetDestination.setColumns(10);
		txtSetDestination.setHorizontalAlignment(JTextField.LEFT);
		
		btnSetDestination = new JButton("Set Folder");
		buttonGroup.add(btnSetDestination);
		btnSetDestination.setAlignmentX(Component.RIGHT_ALIGNMENT);
		btnSetDestination.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String folder = getOutputFolder();
				if(folder.equals("")){
					return;
				}
				database.setOutputFolder(folder);
				txtSetDestination.setText(database.getOutputFolder());
			}
		});
		
		JButton btnClearList = new JButton("Clear List");
		btnClearList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearList();
			}
		});
		
		JButton btnAddFolder = new JButton("Add Folder");
		btnAddFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addFolder();
			}
		});
		
		saveOption = new JComboBox();
		saveOption.addItem("Output Folder");
		saveOption.addItem("Original File Location");
		saveOption.setSelectedIndex(database.getSaveOption());
		updateSaveOption();
		saveOption.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		        updateSaveOption();
		    }
		});
		
		JButton btnMidToChart = new JButton("Mid -> Chart");
		btnMidToChart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chart.midiToChart();
			}
		});
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(title, GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(scroller, GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(lblModifications, GroupLayout.PREFERRED_SIZE, 252, GroupLayout.PREFERRED_SIZE)
							.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(btnTempoChanger, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
								.addGap(14)
								.addComponent(btn3999Creator, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(btnNoteShuffle, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
								.addGap(14)
								.addComponent(btnMirrorMode, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(btnReverser, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
								.addGap(14)
								.addComponent(btnAllTapNotes, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(btnForceNotes, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
								.addGap(14)
								.addComponent(btnUnforceNotes, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(btnDBCToChart, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
								.addGap(14)
								.addComponent(btnChartToArray, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(btnMidToChart, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnCalculateNPS, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE))
							.addComponent(lblImportantTools, GroupLayout.PREFERRED_SIZE, 252, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnAddSections, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnAddFiles, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnRemoveFiles, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE))
					.addGap(14)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnClearList, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAddFolder, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(saveOption, 0, 186, Short.MAX_VALUE)
						.addComponent(txtSetDestination, GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnSetDestination, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(title, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblModifications, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(btn3999Creator, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnTempoChanger, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(btnNoteShuffle, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnMirrorMode, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(btnReverser, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnAllTapNotes, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addComponent(lblImportantTools, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(btnForceNotes, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnUnforceNotes, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(btnDBCToChart, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnChartToArray, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(btnCalculateNPS, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnMidToChart, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
							.addGap(11)
							.addComponent(btnAddSections, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 7, Short.MAX_VALUE))
						.addComponent(scroller, GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE))
					.addGap(11)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnAddFiles)
								.addComponent(btnAddFolder))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnRemoveFiles)
								.addComponent(btnClearList)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(txtSetDestination, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSave))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnSetDestination)
								.addComponent(saveOption, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
		chart = new ChartInterpreter(window);
	}
	
	@SuppressWarnings("unchecked")
	public void updateList(List<File> files){
		listModel.clear();
		for(File file: files){
			listModel.addElement(file.getName());
		}
	}
	
	public void updateSaveOption(){
		 int newIndex = saveOption.getSelectedIndex();
	        database.setSaveOption(newIndex);
	        if(newIndex==1){
	        	btnSetDestination.setEnabled(false);
	        	btnSave.setEnabled(false);
	        	txtSetDestination.setEditable(false);
	        	txtSetDestination.setText("");
	        }
	        else if(newIndex==0){
	        	btnSetDestination.setEnabled(true);
	        	btnSave.setEnabled(true);
	        	txtSetDestination.setEditable(true);
	        	txtSetDestination.setText(database.getOutputFolder());
	        }
	}
	
	public boolean addFiles() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
		    File[] files = fileChooser.getSelectedFiles();
		    List<File> filesToAdd = new ArrayList<File>();
		    for(File file: files){
		    	filesToAdd.add(file);
		    }
		    dragDropListener.addFiles(filesToAdd);
		    return true;
		}
		else{
			return false;
		}
	}
	
	public void addFolder(){
		String folderName = "";
		JFileChooser chooser = new JFileChooser();
	    chooser.setCurrentDirectory(new java.io.File(System.getProperty("user.home") + "/Desktop"));
	    chooser.setDialogTitle("Select a Chart Folder");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    chooser.setAcceptAllFileFilterUsed(false);
	    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	      //System.out.println("getSelectedFile() : " + chooser.getSelectedFile().getAbsolutePath());
	      folderName = chooser.getSelectedFile().getAbsolutePath();
	    } else {
	      System.out.println("No Selection ");
	    }
	    File folder = new File(folderName);
	    files = new ArrayList<File>();
	    listFilesForFolder(folder);
		dragDropListener.addFiles(files);
	}
	
	public void listFilesForFolder(final File folder) {
		//List<File> files = new ArrayList<File>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				if(hasExtension(fileEntry, "chart") || hasExtension(fileEntry, "dbc")){
		    		//System.out.println("File " + listOfFiles[i].getName());
		    		files.add(fileEntry);
		    	}
			}
		}
		//return files;
	}
	
	public String getOutputFolder() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(database.getOutputFolder()));
	    chooser.setDialogTitle("Select Output Folder");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    chooser.setAcceptAllFileFilterUsed(false);
	    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	      return chooser.getSelectedFile().getAbsolutePath();
	    } else {
	      return "";
	    }
	}
	
	public void clearList(){
		updateList(new ArrayList<File>());
		dragDropListener.clearList();
	}
	
	public void deleteFiles(){
		int[] indeces = fileList.getSelectedIndices();
		int[] filesToRemove = new int[indeces.length];
		for(int i = 0; i < indeces.length; i++){
			filesToRemove[i] = indeces[indeces.length-i-1];
		}
		dragDropListener.removeFiles(filesToRemove);
	}
	
	public List<File> getFiles(){
		return dragDropListener.getFiles();
	}
	
	public boolean hasExtension(File file, String extension){
		return dragDropListener.hasExtension(file, extension);
	}
	
	public String getOutput(){
		return database.getOutputFolder();
	}
	
	public int getSaveOption(){
		return database.getSaveOption();
	}
	
	class MyKeyListener extends KeyAdapter {
		  public void keyPressed(KeyEvent evt) {
		    if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
		      deleteFiles();
		    }
		}
	}
}
