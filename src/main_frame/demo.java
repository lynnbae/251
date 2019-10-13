package main_frame;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.DigestException;
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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.odf.OpenDocumentParser;
import org.apache.tika.sax.BodyContentHandler;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.xml.sax.SAXException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import main_frame.time_and_date;

//import textEditor.text_function;

@SuppressWarnings({ "deprecation", "serial" })
public class demo extends JFrame implements ActionListener {
	public void Json(JMenu menu) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		JsonParser parser =new JsonParser();
        JsonObject json=(JsonObject) parser.parse(new FileReader("Json.json"));
        int change = json.get("font").getAsInt();
        menu.setForeground(new Color(change));
	}
	private JPanel contentPane;
	public JTextArea textArea;
	private JMenuItem itemOpen;
	private JMenuItem itemSave;

	// the operations on the file
	int flag = 0;

	// the current name of the file
	static String currentFileName = null;

	//for print part 	
	PrintJob print = null;
	Graphics graph = null;

	// the current path of the file
	static String currentPath = null;

	public UndoManager undoMgr = new UndoManager();

	// the clipboard
	public Clipboard clipboard = new Clipboard("The board");

	private JMenuItem itemNew;
	private JSeparator separator_1;
	private JMenuItem itemPrint;
	private JMenuItem itemExit;
	private JSeparator separator_2;
	public JMenu itemSearch;
	public JMenu itFormat;
	public JMenu itemHelp;
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
					demo frame = new demo();
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
	public demo() throws InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, JsonIOException, JsonSyntaxException, FileNotFoundException {
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
	
		this.Json(itemFile);//apply json
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
		textArea = new RSyntaxTextArea(20, 30);
		((RSyntaxTextArea) textArea).setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		((RSyntaxTextArea) textArea).setCodeFoldingEnabled(true);
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
		timelabel = new JLabel("Time��" + hour + ":" + minute + ":" + second + " ");
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
							demo.this, "Save to NewFile?",
							"Notepad", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.PLAIN_MESSAGE);
					if (result == JOptionPane.OK_OPTION) {
						demo.this.saveAs();
					} else if (result == JOptionPane.NO_OPTION) {
						demo.this.dispose();
						demo.this
								.setDefaultCloseOperation(EXIT_ON_CLOSE);
					}
				} else if (flag == 2 && currentPath != null) {
					//under created file with a path			
					int result = JOptionPane.showConfirmDialog(
							demo.this, "Save to" + currentPath
									+ "?", "Notepad",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.PLAIN_MESSAGE);
					if (result == JOptionPane.OK_OPTION) {
						demo.this.save();
					} else if (result == JOptionPane.NO_OPTION) {
						demo.this.dispose();
						demo.this
								.setDefaultCloseOperation(EXIT_ON_CLOSE);
					}
				} else {
					int result = JOptionPane.showConfirmDialog(
							demo.this, "Exit?", "Alert",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.PLAIN_MESSAGE);
					if (result == JOptionPane.OK_OPTION) {
						demo.this.dispose();
						demo.this
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
			try {
				openFile();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getSource() == itemChangeToPDF) { // save to pdf
			try {
				savePDf();
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		} else if (e.getSource() == itemSave) { // save a file
			save();
		} else if (e.getSource() == itemNew) { // newly create a file
			newFile();
		} else if (e.getSource() == itemExit) { // exit
			exit();
		} else if (e.getSource() == itemPrint) { // print
			// ��ӡ��
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
					"Yao Xinlu"+"\n"+"Shen Jihan", "Information", 1);
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
					.showConfirmDialog(demo.this,
							"Save to NewFile?", "Notepad",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				demo.this.saveAs();
			} else if (result == JOptionPane.NO_OPTION) {
				demo.this.dispose();
				demo.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			}
		} else if (flag == 2 && currentPath != null) {
			int result = JOptionPane
					.showConfirmDialog(demo.this, "Save to"
							+ currentPath + "?", "Notepad",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				demo.this.save();
			} else if (result == JOptionPane.NO_OPTION) {
				demo.this.dispose();
				demo.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			}
		} else {
			int result = JOptionPane.showConfirmDialog(demo.this,
					"Exit��", "Alert!", JOptionPane.YES_NO_OPTION,
					JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				demo.this.dispose();
				demo.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			}
		}
	}
// create a new file
	@SuppressWarnings("static-access")
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
					.showConfirmDialog(demo.this, "Save to"
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
			saveFunction(file,textArea);
			}
		}
		
	
	private void savePDf() throws DocumentException {
        JFileChooser pdfChoose = new JFileChooser();
        pdfChoose.setFileFilter(new FileNameExtensionFilter("pdf",".pdf"));
			pdfChoose.showDialog(this, "save as pdf");
			File file = pdfChoose.getSelectedFile();
			try {
				Document doc = new Document(PageSize.A4,50,50,50,50);
				PdfWriter.getInstance(doc, new FileOutputStream(file.getPath()+".pdf"));
				doc.open();
				doc.add(new Paragraph( textArea.getText()));
				doc.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}
	@SuppressWarnings("static-access")
	private void save() {
		if (this.currentPath == null) {
			this.saveAs();
			if (this.currentPath == null) {
				return;
			}
		}
		saveFunction(new File(currentPath),textArea);
	}
	//save function
	public static void saveFunction(File file,JTextArea textArea){
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			fw.write(textArea.getText());
			@SuppressWarnings("unused")
			String currentFileName = file.getName();
			@SuppressWarnings("unused")
			String currentPath = file.getAbsolutePath();
			fw.flush();
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
	private void openFile() throws FileNotFoundException {
		if (flag == 2 && this.currentPath == null) {
			int result = JOptionPane.showConfirmDialog(demo.this,
							"Save to NewFile?", "Notepad",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				this.saveAs();
			}
			
		} else if (flag == 2 && this.currentPath != null) {
			int result = JOptionPane.showConfirmDialog(demo.this, "Save to "
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
			flag = 3;
			this.setTitle(this.currentPath);
			File file = choose.getSelectedFile();
			if( file.getName().substring(file.getName().lastIndexOf(".") + 1).equals("odt") )
			{	
				FileInputStream file2 = new FileInputStream(file.getPath());;
				ParseContext pcontext = new ParseContext();
	    	    BodyContentHandler handler = new BodyContentHandler();
	    	    Metadata metadata = new Metadata();
	    	      //Open Document Parser
	    	    OpenDocumentParser openofficeparser = new OpenDocumentParser ();
	    	    try {
					openofficeparser.parse(file2, handler, metadata,pcontext);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SAXException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (TikaException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    	      textArea.setText(handler.toString());
			}
			else {
			open(file,textArea);
			}
		}
	}
	public static void open(File file,JTextArea textArea){
	currentFileName = file.getName();
	currentPath = file.getAbsolutePath();
	
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
	//search word and move CaretPosition
	public static void searchFunction(JTextArea textArea,String search,boolean judge, boolean up,boolean down) {
	int a = 0, b = 0;
	int FindStartPos = textArea.getCaretPosition();
	String contain, lower, search_lower, strA, strB;
	contain = textArea.getText();
	lower = contain.toLowerCase();
	search_lower = search.toLowerCase();
	if (judge) {
		strA = contain;
		strB = search;
	} else {
		strA = lower;
		strB = search_lower;
	}
	if (up) {
		if (textArea.getSelectedText() == null) {
			a = strA.lastIndexOf(strB, FindStartPos - 1);
		} else {
			a = strA.lastIndexOf(strB, FindStartPos- search.length() - 1);
		}
	} else if (down) {
		if (textArea.getSelectedText() == null) {
			a = strA.indexOf(strB, FindStartPos);
		} else {
			a = strA.indexOf(strB, FindStartPos- search.length() + 1);
		}
	}
	
	if (a > -1) {
		if (up) {
			textArea.setCaretPosition(a);
			b = search.length();
			textArea.select(a, a + b);
		} else if (down) {
			textArea.setCaretPosition(a);
			b = search.length();
			textArea.select(a, a + b);
		}
	} else {
		JOptionPane.showMessageDialog(null, "not find",
				"Notepad", JOptionPane.INFORMATION_MESSAGE);
	}
	}
	// total search part
	public void mySearch() {
		final JDialog findDialog = new JDialog(this, "Find/Replace", true);
		Container con = findDialog.getContentPane();

		con.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel searchContentLabel = new JLabel("Find(N) :");
		JLabel replaceContentLabel = new JLabel("ReplaceWith(P)�� :");
		final JTextField findText = new JTextField(22);
		final JTextField replaceText = new JTextField(22);
		final JCheckBox matchcase = new JCheckBox("CaseSensitive");
		ButtonGroup bGroup = new ButtonGroup();
		final JRadioButton up = new JRadioButton("Up(U)");
		final JRadioButton down = new JRadioButton("Down(D)");
		down.setSelected(true);
		bGroup.add(up);
		bGroup.add(down);
		JButton searchNext = new JButton("FindNext(F)");
		JButton replace = new JButton("Replace(R)");
		final JButton replaceAll = new JButton("ReplaceALL(A)");
		searchNext.setPreferredSize(new Dimension(110, 22));
		replace.setPreferredSize(new Dimension(110, 22));
		replaceAll.setPreferredSize(new Dimension(110, 22));
		textArea.setCaretPosition(0); 
		// change button 
		replace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (replaceText.getText().length() == 0
						&& textArea.getSelectedText() != null)
					textArea.replaceSelection("");
				if (replaceText.getText().length() > 0
						&& textArea.getSelectedText() != null)
					textArea.replaceSelection(replaceText.getText());
			}
		});
		// all change button
		replaceAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a = 0, b = 0, replaceCount = 0;
				if (findText.getText().length() == 0) {
					JOptionPane.showMessageDialog(findDialog, "Please Input!",
							"Alert", JOptionPane.WARNING_MESSAGE);
					findText.requestFocus(true);
					return;
				}
				while (a > -1) {
					int FindStartPos = textArea.getCaretPosition();
					String string1, string2, string3, string4, stringA, stringB;
					string1 = textArea.getText();
					string2 = string1.toLowerCase();
					string3 = findText.getText();
					string4 = string3.toLowerCase();

					if (matchcase.isSelected()) {
						stringA = string1;
						stringB = string3;
					} else {
						stringA = string2;
						stringB = string4;
					}

					if (up.isSelected()) {
						if (textArea.getSelectedText() == null) {
							a = stringA.lastIndexOf(stringB, FindStartPos - 1);
						} else {
							a = stringA.lastIndexOf(stringB, FindStartPos
									- findText.getText().length() - 1);
						}
					} else if (down.isSelected()) {
						if (textArea.getSelectedText() == null) {
							a = stringA.indexOf(stringB, FindStartPos);
						} else {
							a = stringA.indexOf(stringB, FindStartPos
									- findText.getText().length() + 1);
						}

					}

					if (a > -1) {
						if (up.isSelected()) {
							textArea.setCaretPosition(a);
							b = findText.getText().length();
							textArea.select(a, a + b);
						} else if (down.isSelected()) {
							textArea.setCaretPosition(a);
							b = findText.getText().length();
							textArea.select(a, a + b);
						}
					} else {
						if (replaceCount == 0) {
							JOptionPane.showMessageDialog(findDialog,
									"Cannot find!", "Notepad",
									JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(findDialog,
									"Successfully Replaced: " + replaceCount
											+ "changed!", "Replace",
									JOptionPane.INFORMATION_MESSAGE);
						}
					}
					if (replaceText.getText().length() == 0
							&& textArea.getSelectedText() != null) {
						textArea.replaceSelection("");
						replaceCount++;
					}
					if (replaceText.getText().length() > 0
							&& textArea.getSelectedText() != null) {
						textArea.replaceSelection(replaceText.getText());
						replaceCount++;
					}
				}
			}
		}); 
		
		searchNext.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String search = findText.getText();
				boolean judge=matchcase.isSelected();
				boolean judgeUP=up.isSelected();
				boolean judgeDown=down.isSelected();
				System.out.print(judgeDown);
				searchFunction(textArea,search,judge,judgeUP,judgeDown);

			}
		});
		JButton cancel = new JButton("cancel");
		cancel.setPreferredSize(new Dimension(111, 21));
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				findDialog.dispose();
			}
		});

		JPanel bottomPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		centerPanel.setSize(400, 400);
		JPanel topPanel = new JPanel();

		JPanel direction = new JPanel();
		direction.setBorder(BorderFactory.createTitledBorder("Direction "));
		direction.add(up);
		direction.add(down);
		direction.setPreferredSize(new Dimension(160, 50));
		JPanel replacePanel = new JPanel();
		replacePanel.setLayout(new GridLayout(2, 1));
		replacePanel.add(replace);
		replacePanel.add(replaceAll);

		topPanel.add(searchContentLabel);
		topPanel.add(findText);
		topPanel.add(searchNext);
		centerPanel.add(replaceContentLabel);
		centerPanel.add(replaceText);
		centerPanel.add(replacePanel);
		bottomPanel.add(matchcase);
		bottomPanel.add(direction);
		bottomPanel.add(cancel);

		con.add(topPanel);
		con.add(centerPanel);
		con.add(bottomPanel);

		findDialog.setSize(450, 200);
		findDialog.setResizable(false);
		findDialog.setLocation(225, 270);
		findDialog.setVisible(true);
	}
	
}