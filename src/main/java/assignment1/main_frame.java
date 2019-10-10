package assignment1;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.PrintJob;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.undo.UndoManager;

import assignment1.time_and_date;

//import textEditor.text_function;

@SuppressWarnings({ "deprecation", "serial" })
public class main_frame extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextArea textArea;
	private JMenuItem itemOpen;
	private JMenuItem itemSave;

	// the operations on the file
	int flag = 0;

	// the current name of the file
	String currentFileName = null;

	//for print part 	
	PrintJob print = null;
	Graphics graph = null;

	// the current path of the file
	String currentPath = null;

	public UndoManager undoMgr = new UndoManager();

	// the clipboard
	public Clipboard clipboard = new Clipboard("The board");

	private JMenuItem itemNew;
	private JSeparator separator_1;
	private JMenuItem itemPrint;
	private JMenuItem itemExit;
	private JSeparator separator_2;
	private JMenu itemSearch;
	private JMenu itFormat;
	private JMenu itemHelp;
	private JMenuItem itemSearchForHelp;
	private JMenuItem itemFind;
	private JMenuItem itemChangeToPDF;
	private JMenuItem itemTime;
	private JScrollPane scrollPane;
	private JCheckBoxMenuItem itemStatement;
	private JToolBar tool;
	public static JLabel timelabel;
	private JMenuItem Cut_fun;
	private JMenuItem Copy_fun;
	private JMenuItem Paste_fun;
	private JPopupMenu Menu;

	//the main function of the frame
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					main_frame frame = new main_frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	GregorianCalendar clock = new GregorianCalendar();
	int hour = clock.get(Calendar.HOUR_OF_DAY);
	int minute = clock.get(Calendar.MINUTE);
	int second = clock.get(Calendar.SECOND);

	//to create the frame	
	public main_frame() throws InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} 
		setTitle("Text Editor");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 400);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu itemFile = new JMenu("File (F)");
		itemFile.setMnemonic('F');
		menuBar.add(itemFile);
		
		itemChangeToPDF = new JMenuItem("PDF");
		itemChangeToPDF.addActionListener(this);
		itemFile.add(itemChangeToPDF);
		
		itemNew = new JMenuItem("New (N)", 'N');
		itemNew.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_N, java.awt.Event.CTRL_MASK));
		itemNew.addActionListener(this);
		itemFile.add(itemNew);

		itemOpen = new JMenuItem("Open (O)", 'O');
		itemOpen.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_O, java.awt.Event.CTRL_MASK));
		itemOpen.addActionListener(this);
		itemFile.add(itemOpen);

		itemSave = new JMenuItem("Save (S)");
		itemSave.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_S, java.awt.Event.CTRL_MASK));
		itemSave.addActionListener(this);
		itemFile.add(itemSave);

		separator_1 = new JSeparator();
		itemFile.add(separator_1);

		itemPrint = new JMenuItem("Print (P)", 'P');
		itemPrint.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_P, java.awt.Event.CTRL_MASK));
		itemPrint.addActionListener(this);
		itemFile.add(itemPrint);

		separator_2 = new JSeparator();
		itemFile.add(separator_2);

		itemExit = new JMenuItem("Exit (X)", 'X');
		itemExit.addActionListener(this);
		itemFile.add(itemExit);

		itemSearch = new JMenu("Search (E)");
		itemSearch.setMnemonic('E');
		menuBar.add(itemSearch);

		itemFind = new JMenuItem("Search (F)", 'F');
		itemFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
				Event.CTRL_MASK));
		itemFind.addActionListener(this);
		itemSearch.add(itemFind);

		itFormat = new JMenu("View (O)");
		itFormat.setMnemonic('O');
		menuBar.add(itFormat);
		
		itemTime = new JMenuItem("Date/Time (D)", 'D');
		itemTime.addActionListener(this);
		itFormat.add(itemTime);
		
		itemStatement = new JCheckBoxMenuItem("T&D (S)");
		itemStatement.addActionListener(this);
		itFormat.add(itemStatement);

		itemHelp = new JMenu("Help (H)");
		itemHelp.setMnemonic('H');
		menuBar.add(itemHelp);

		itemSearchForHelp = new JMenuItem("About (A)", 'H');
		itemSearchForHelp.addActionListener(this);
		itemHelp.add(itemSearchForHelp);

		//to create the scrollpane		
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		textArea = new JTextArea();
		scrollPane = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		//Menu part		
		Menu = new JPopupMenu();
		addPopup(textArea, Menu);

		//text functions		
		Cut_fun = new JMenuItem("Cut (T)");
		Cut_fun.addActionListener(this);
		Menu.add(Cut_fun);

		Copy_fun = new JMenuItem("Copy(C)");
		Copy_fun.addActionListener(this);
		Menu.add(Copy_fun);

		Paste_fun = new JMenuItem("Paste (P)");
		Paste_fun.addActionListener(this);
		Menu.add(Paste_fun);
		contentPane.add(scrollPane);

		textArea.getDocument().addUndoableEditListener(undoMgr);

		tool = new JToolBar();
		tool.setSize(textArea.getSize().width, 10);
		timelabel = new JLabel("Time£º" + hour + ":" + minute + ":" + second + " ");
		tool.add(timelabel);

		contentPane.add(tool, BorderLayout.NORTH);
		tool.setVisible(false);
		tool.setFloatable(false);
		time_and_date clock = new time_and_date();
		clock.start();

		// create the menu
		final JPopupMenu jp = new JPopupMenu(); // create the drop-down menu
		textArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3)// Only respond to right mouse click events
				{
					jp.show(e.getComponent(), e.getX(), e.getY());// Displays a pop-up menu in the mouse position
				}
			}
		});

		this.MainFrameWidowListener();
	}

	private void MainFrameWidowListener() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			//under created file but without a path			
			public void windowClosing(WindowEvent e) {
				if (flag == 2 && currentPath == null) {
					int result = JOptionPane.showConfirmDialog(
							main_frame.this, "Save to NewFile?",
							"Notepad", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.PLAIN_MESSAGE);
					if (result == JOptionPane.OK_OPTION) {
						main_frame.this.saveAs();
					} else if (result == JOptionPane.NO_OPTION) {
						main_frame.this.dispose();
						main_frame.this
								.setDefaultCloseOperation(EXIT_ON_CLOSE);
					}
				} else if (flag == 2 && currentPath != null) {
					//under created file with a path			
					int result = JOptionPane.showConfirmDialog(
							main_frame.this, "Save to" + currentPath
									+ "?", "Notepad",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.PLAIN_MESSAGE);
					if (result == JOptionPane.OK_OPTION) {
						main_frame.this.save();
					} else if (result == JOptionPane.NO_OPTION) {
						main_frame.this.dispose();
						main_frame.this
								.setDefaultCloseOperation(EXIT_ON_CLOSE);
					}
				} else {
					int result = JOptionPane.showConfirmDialog(
							main_frame.this, "Exit?", "Alert",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.PLAIN_MESSAGE);
					if (result == JOptionPane.OK_OPTION) {
						main_frame.this.dispose();
						main_frame.this
								.setDefaultCloseOperation(EXIT_ON_CLOSE);
					}
				}
			}
		});
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == itemOpen) { // open a file
			openFile();
		} else if (e.getSource() == itemSave) { // save a file
			save();
		} else if (e.getSource() == itemNew) { // newly create a file
			newFile();
		} else if (e.getSource() == itemExit) { // exit
			exit();
		} else if (e.getSource() == itemPrint) { // print
			// ´òÓ¡»ú
			Print();
		} else if (e.getSource() == itemFind) { // search
			mySearch();
		} else if (e.getSource() == itemTime) { // time and schedule
			textArea.append(clock.get(Calendar.HOUR) + ":"
					+ clock.get(Calendar.MINUTE)+ ":" 
					+ clock.get(Calendar.SECOND) + " " +  
					+ clock.get(Calendar.YEAR) + "/"
					+ (clock.get(Calendar.MONTH) + 1) + "/"
					+ clock.get(Calendar.DAY_OF_MONTH));
		
		} else if (e.getSource() == itemStatement) { // time schedule condition
			if (itemStatement.isSelected()) {
				tool.setVisible(true);				
				return;
			}
			tool.setVisible(false);

		} else if (e.getSource() == itemSearchForHelp) {
			JOptionPane.showMessageDialog(this,
					"Yao Xinlu is the author", "Team member???", 1);
		} else if (e.getSource() == Cut_fun) { // cut function
			cut();
		} else if (e.getSource() == Copy_fun) { // copy function
			copy();
		} else if (e.getSource() == Paste_fun) { // paste function
			paste();
		}
	}
	//exit the function 
	private void exit() {
		if (flag == 2 && currentPath == null) {
			int result = JOptionPane
					.showConfirmDialog(main_frame.this,
							"Save to NewFile?", "Notepad",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				main_frame.this.saveAs();
			} else if (result == JOptionPane.NO_OPTION) {
				main_frame.this.dispose();
				main_frame.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			}
		} else if (flag == 2 && currentPath != null) {
			int result = JOptionPane
					.showConfirmDialog(main_frame.this, "Save to"
							+ currentPath + "?", "Notepad",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				main_frame.this.save();
			} else if (result == JOptionPane.NO_OPTION) {
				main_frame.this.dispose();
				main_frame.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			}
		} else {
			int result = JOptionPane.showConfirmDialog(main_frame.this,
					"Exit£¿", "Alert!", JOptionPane.YES_NO_OPTION,
					JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				main_frame.this.dispose();
				main_frame.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			}
		}
	}
// create a new file
	private void newFile() {
		if (flag == 0 || flag == 1) { 
			return;
		} else if (flag == 2 && this.currentPath == null) { 
			this.textArea.setText("");
			this.setTitle("Text Editor");
			flag = 1;
			return;
		} else if (flag == 2 && this.currentPath != null) {
			int result = JOptionPane
					.showConfirmDialog(main_frame.this, "Save to"
							+ this.currentPath + "?", "Notepad",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				this.save(); 
			} else if (result == JOptionPane.NO_OPTION) {
				this.textArea.setText("");
				this.setTitle("Text Editor");
				flag = 1;
			}
		} else if (flag == 3) {
			this.textArea.setText("");
			flag = 1;
			this.setTitle("Text Editor");
		}
	}
//	set a save source file
	private void saveAs() {
		JFileChooser choose = new JFileChooser();
		int result = choose.showSaveDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = choose.getSelectedFile();
			FileWriter fw = null;
			try {
				fw = new FileWriter(file);
				fw.write(textArea.getText());
				currentFileName = file.getName();
				currentPath = file.getAbsolutePath();
				fw.flush();
				this.flag = 3;
				this.setTitle(currentPath);
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				try {
					if (fw != null)
						fw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private void save() {
		if (this.currentPath == null) {
			this.saveAs();
			if (this.currentPath == null) {
				return;
			}
		}
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(currentPath));
			fw.write(textArea.getText());
			fw.flush();
			flag = 3;
			this.setTitle(this.currentPath);
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (fw != null)
					fw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	// to open a file	
	private void openFile() {
		if (flag == 2 && this.currentPath == null) {
			int result = JOptionPane.showConfirmDialog(main_frame.this,
							"Save to NewFile?", "Notepad",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				this.saveAs();
			}
			
		} else if (flag == 2 && this.currentPath != null) {
			int result = JOptionPane.showConfirmDialog(main_frame.this, "Save to "
							+ this.currentPath + "?", "Notepad",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				this.save();
			}
		}
		
		JFileChooser choose = new JFileChooser();
		int result = choose.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = choose.getSelectedFile();
			currentFileName = file.getName();
			currentPath = file.getAbsolutePath();
			flag = 3;
			this.setTitle(this.currentPath);
			BufferedReader br = null;
			try {
				InputStreamReader isr = new InputStreamReader(
						new FileInputStream(file), "GBK");
				br = new BufferedReader(isr);
				StringBuffer sb = new StringBuffer();
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line + System.getProperty("line.separator"));
				}
				textArea.setText(sb.toString());
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				try {
					if (br != null)
						br.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public void Print() {
		try {
			print = getToolkit().getPrintJob(this,"string",null);// create a print job 
			graph = print.getGraphics();
			this.textArea.printAll(graph);
			print.end();
		} catch (Exception a) {

		}
	}

	public void cut() {
		copy();
		int start = this.textArea.getSelectionStart();
		int end = this.textArea.getSelectionEnd();
		this.textArea.replaceRange("", start, end);

	}

	public void copy() {
		String temp = this.textArea.getSelectedText();
		StringSelection text = new StringSelection(temp);
		this.clipboard.setContents(text, null);
	}

	public void paste() {
		Transferable contents = this.clipboard.getContents(this);
		DataFlavor flavor = DataFlavor.stringFlavor;
		if (contents.isDataFlavorSupported(flavor)) {
			String str;
			try {
				str = (String) contents.getTransferData(flavor);
				if (this.textArea.getSelectedText() != null) {
					int start = this.textArea.getSelectionStart();
					int end = this.textArea.getSelectionEnd();
					this.textArea.replaceRange(str, start, end);
				} else {
					int mouse = this.textArea.getCaretPosition();
					this.textArea.insert(str, mouse);
				}
			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
	}

	public void mySearch() {

			}

}
