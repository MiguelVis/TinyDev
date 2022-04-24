/**
 * TinyDev - Programming language for kids
 *
 * Copyright (C) 2017-2022 Miguel I. Garc�a L�pez (FloppySoftware).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Read the file `LICENSE.MD` for more details.
 */

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URI;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.ImageIcon;

import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.AbstractListModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.JPopupMenu;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Clase que implementa el IDE / editor de TinyDev.
 * 
 * @author Miguel
 * @version 1.00 - 18 Mar 2017
 * @since
 *
 * Revisiones:
 * 
 * v1.01 - 27 Mar 2017
 * 
 * 31 Mar 2017 : Tomar el directorio actual (en el que est� TinyDev) al ejecutar doNew().
 */
/**
 * @author Miguel
 *
 */
@SuppressWarnings("serial")
public class TinyDevEditor extends JFrame {
	
	// ---------------
	// G L O B A L E S
	// ---------------

	private static final String APP_VERSION = "v1.01 - 24 Apr 2022"; // Versi�n
	private static final String APP_COPYRIGHT = "(c) 2017 - 2022 Miguel I. Garc�a L�pez (FloppySoftware)"; // Copyright
	private static final String APP_WEBSITE = "http://www.floppysoftware.es/tinydev/"; // P�gina web
	
	private static final int DEFAULT_SCREEN_WIDTH = 850;	// Ancho de pantalla en pixels por defecto
	private static final int DEFAULT_SCREEN_HEIGHT = 600;   // Alto  de pantalla en pixels por defecto
	private JPanel contentPane;  // Panel contenedor
	private JTextArea contents;  // �rea de texto del editor
	private DocumentListener docListener; // DocumentLisener para el �rea de edici�n
	private Font contentsFont = new Font("Monospaced", Font.PLAIN, 14);  // Fuente a utilizar por el editor
	private TinyDevLocalizator loc = new TinyDevLocalizator("TinyDevEditor");  // Localizador de mensajes, etc.
	private TinyDevProperties props = new TinyDevProperties("TinyDev"); // Propiedades de la aplicaci�n
	private Image appIcon;       // Icono de la aplicaci�n
	private String fileName;     // Nombre del fichero de c�digo fuente que se est� editando
	private String currentDirectory;         // Directorio en el que se encuentra el fichero de c�digo fuente actual
	private boolean textChanged = false;     // Detecci�n de cambios en el texto que se est� editando
	private String findValue = null;         // Cadena de texto para Find / FindNext
	private FileFilter fileFilter;           // Filtro para los cuadros de di�logo Open / Save As
	private TinyDevLang lang = new TinyDevLang(this);  // Objeto de lenguaje TinyDev
	private TinyDevScreen screen = null;          // Ventana para el objeto de TinyDevLang
	private String[] keywords = lang.getKeywords();  // Lista de palabras reservadas
	private JPanel statusPanel;  // Barra de estado
	private JLabel statusLine;   // N� de l�nea actual en la barra de estado
	private JLabel statusText;   // Texto (mensaje) en la barra de estado 
	private int lineNumber;      // N� de l�nea actualmente en edici�n
	private boolean cfgTip;      // Mostrar consejo al iniciar
	private boolean cfgTemplate; // Utilizar plantilla en nuevos ficheros
	private boolean cfgChanged;  // True si ha habido cambios en la configuraci�n, que hay que grabar
	private String tip = null;   // Consejo a mostrar al inicio

	/**
	 * Ejecutar el IDE de TinyDev.
	 * 
	 * @param args  argumentos de la l�nea de comandos
	 */
	public static void main(String[] args) {
		
		// Ejecutar el IDE
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// Lanzar el IDE
					new TinyDevEditor();
				} catch (Exception e) {
					// Mostrar mensaje de error y abortar
					fatalError(e);
				}
			}
		});
	}

	/**
	 * Constructor del IDE.
	 */
	public TinyDevEditor() {
		
		// -------------------
		// INICIALIZAR VENTANA
		// -------------------
		
		// Listener para el cierre
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			
            @Override
            public void windowClosing(WindowEvent e) {
                	doExit();
                }
        });
		
		// Tama�o y posici�n iniciales
		setBounds(50, 50, DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_HEIGHT);
		
		// Icono de la aplicaci�n
		appIcon = new ImageIcon(getClass().getResource("/AppIcon.png")).getImage();
		setIconImage(appIcon);

		// Area de texto del editor
		contents = new JTextArea();
		
		// Barra de men�
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		// ------------------------------------------
		// ActionListeners para el men� y los botones
		// ------------------------------------------
		
		ActionListener actionListenerNew = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doNew();
			}
		};
		
		ActionListener actionListenerOpen = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doOpen();
			}
		};
		
		ActionListener actionListenerSave = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSave();
			}
		};
		
		ActionListener actionListenerSaveAs = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSaveAs();
			}
		};
		
		ActionListener actionListenerPrint = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doPrint();
			}
		};
		
		ActionListener actionListenerExit = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doExit();
			}
		};
		
		ActionListener actionListenerCut = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCut();
			}
		};
		
		ActionListener actionListenerCopy = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCopy();
			}
		};
		
		ActionListener actionListenerPaste = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doPaste();
			}
		};
		
		ActionListener actionListenerDelete = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doDelete();
			}
		};
		
		ActionListener actionListenerSelectAll = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSelectAll();
			}
		};
		
		ActionListener actionListenerFind = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doFind();
			}
		};
		
		ActionListener actionListenerFindNext = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doFindNext();
			}
		};
	
		ActionListener actionListenerTip = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cfgTip = !cfgTip;  // Invertir estado
				cfgChanged = true; // Hay cambios pdtes. de grabar
			}
		};
		
		ActionListener actionListenerTemplate = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cfgTemplate = !cfgTemplate;  // Invertir estado
				cfgChanged = true;           // Hay cambios pdtes. de grabar
			}
		};
	
		ActionListener actionListenerRun = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doRun();
			}
		};
		
		ActionListener actionListenerStop = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doStop();
			}
		};
		
		ActionListener actionListenerHelp = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doHelp();
			}
		};
		
		ActionListener actionListenerAbout = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAbout();
			}
		};
		
		// ---------
		// MENU File
		// ---------
		
		String k = "File";
		JMenu mnFile = new JMenu(loc.getLabel(k));
		mnFile.setMnemonic(loc.getKey(k));
		menuBar.add(mnFile);
		
		k = "FileNew";
		JMenuItem mntmNew = new JMenuItem(loc.getLabel(k));
		mntmNew.setAccelerator(loc.getAccel(k));
		mntmNew.setMnemonic(loc.getKey(k));
		mntmNew.addActionListener(actionListenerNew);
		mnFile.add(mntmNew);
		
		k = "FileOpen";
		JMenuItem mntmOpen = new JMenuItem(loc.getLabel(k));
		mntmOpen.setAccelerator(loc.getAccel(k));
		mntmOpen.setMnemonic(loc.getKey(k));
		mntmOpen.addActionListener(actionListenerOpen);
		mnFile.add(mntmOpen);
		
		k = "FileSave";
		JMenuItem mntmSave = new JMenuItem(loc.getLabel(k));
		mntmSave.setAccelerator(loc.getAccel(k));
		mntmSave.setMnemonic(loc.getKey(k));
		mntmSave.addActionListener(actionListenerSave);
		mnFile.add(mntmSave);
		
		k = "FileSaveAs";
		JMenuItem mntmSaveAs = new JMenuItem(loc.getLabel(k));
		mntmSaveAs.setMnemonic(loc.getKey(k));
		mntmSaveAs.addActionListener(actionListenerSaveAs);
		mnFile.add(mntmSaveAs);
		
		mnFile.addSeparator();
		
		k = "FilePrint";
		JMenuItem mntmPrint = new JMenuItem(loc.getLabel(k));
		mntmPrint.setAccelerator(loc.getAccel(k));
		mntmPrint.setMnemonic(loc.getKey(k));
		mntmPrint.addActionListener(actionListenerPrint);
		mnFile.add(mntmPrint);
		
		mnFile.addSeparator();
		
		k = "FileExit";
		JMenuItem mntmExit = new JMenuItem(loc.getLabel(k));
		mntmExit.setMnemonic(loc.getKey(k));
		mntmExit.addActionListener(actionListenerExit);
		mnFile.add(mntmExit);
		
		// ---------
		// MENU Edit
		// ---------
		
		k = "Edit";
		JMenu mnEdit = new JMenu(loc.getLabel(k));
		mnEdit.setMnemonic(loc.getKey(k));

		k = "EditCut";
		JMenuItem mntmCut = new JMenuItem(loc.getLabel(k));
		mntmCut.setAccelerator(loc.getAccel(k));
		mntmCut.setMnemonic(loc.getKey(k));
		mntmCut.addActionListener(actionListenerCut);
		mnEdit.add(mntmCut);
		
		k = "EditCopy";
		JMenuItem mntmCopy = new JMenuItem(loc.getLabel(k));
		mntmCopy.setAccelerator(loc.getAccel(k));
		mntmCopy.setMnemonic(loc.getKey(k));
		mntmCopy.addActionListener(actionListenerCopy);
		mnEdit.add(mntmCopy);
		
		k = "EditPaste";
		JMenuItem mntmPaste = new JMenuItem(loc.getLabel(k));
		mntmPaste.setAccelerator(loc.getAccel(k));
		mntmPaste.setMnemonic(loc.getKey(k));
		mntmPaste.addActionListener(actionListenerPaste);
		mnEdit.add(mntmPaste);
		
		k = "EditDelete";
		JMenuItem mntmDelete = new JMenuItem(loc.getLabel(k));
		mntmDelete.setMnemonic(loc.getKey(k));		
		mntmDelete.setAccelerator(loc.getAccel(k));
		mntmDelete.addActionListener(actionListenerDelete);
		mnEdit.add(mntmDelete);
		
		mnEdit.addSeparator();
		
		k = "EditSelectAll";
		JMenuItem mntmSelectAll = new JMenuItem(loc.getLabel(k));
		mntmSelectAll.setMnemonic(loc.getKey(k));
		mntmSelectAll.setAccelerator(loc.getAccel(k));
		mntmSelectAll.addActionListener(actionListenerSelectAll);
		mnEdit.add(mntmSelectAll);
		
		mnEdit.addSeparator();
		
		k = "EditFind";
		JMenuItem mntmFind = new JMenuItem(loc.getLabel(k));
		mntmFind.setMnemonic(loc.getKey(k));
		mntmFind.setAccelerator(loc.getAccel(k));
		mntmFind.addActionListener(actionListenerFind);
		mnEdit.add(mntmFind);
		
		k = "EditFindNext";
		JMenuItem mntmFindNext = new JMenuItem(loc.getLabel(k));
		mntmFindNext.setMnemonic(loc.getKey(k));
		mntmFindNext.setAccelerator(loc.getAccel(k));
		mntmFindNext.addActionListener(actionListenerFindNext);
		mnEdit.add(mntmFindNext);
		
		// -------------------------------------------------------------
		// Listener para activar / desactivar las opciones del men� Edit
		// -------------------------------------------------------------
		
		mnEdit.addMenuListener(new MenuListener() {
			
			@Override
		    public void menuSelected(MenuEvent e) {
				
				// �Hay texto seleccionado?
				boolean selection = contents.getSelectionStart() != contents.getSelectionEnd();

				// Activar si hay texto seleccionado
				mntmCut.setEnabled(selection);
				mntmCopy.setEnabled(selection);
				mntmDelete.setEnabled(selection);
			      
				// Activar si el clipboard tiene datos
				mntmPaste.setEnabled(Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null) != null);
			}
			
		    @Override
		    public void menuDeselected(MenuEvent e) {
		    	// Nada
		    }

		    @Override
		    public void menuCanceled(MenuEvent e) {
		    	// Nada
		    }

		});
		menuBar.add(mnEdit);
		
		// --------
		// MENU Run
		// --------
		
		k = "Run";
		JMenu mnRun = new JMenu(loc.getLabel(k));
		mnRun.setMnemonic(loc.getKey(k));
		menuBar.add(mnRun);
		
		k = "RunRun";
		JMenuItem mntmRun = new JMenuItem(loc.getLabel(k));
		mntmRun.setAccelerator(loc.getAccel(k));
		mntmRun.setMnemonic(loc.getKey(k));
		mntmRun.addActionListener(actionListenerRun);
		mnRun.add(mntmRun);
		
		k = "RunStop";
		JMenuItem mntmStop = new JMenuItem(loc.getLabel(k));
		mntmStop.setAccelerator(loc.getAccel(k));
		mntmStop.setMnemonic(loc.getKey(k));
		mntmStop.addActionListener(actionListenerStop);
		mnRun.add(mntmStop);
		
		// ------------
		// MENU Options
		// ------------
		
		k = "Options";
		JMenu mnOptions = new JMenu(loc.getLabel(k));
		mnOptions.setMnemonic(loc.getKey(k));
		
		k = "OptionsTip";
		JCheckBoxMenuItem mntmTip = new JCheckBoxMenuItem(loc.getLabel(k));
		mntmTip.setMnemonic(loc.getKey(k));
		mntmTip.addActionListener(actionListenerTip);
		mnOptions.add(mntmTip);
		
		k = "OptionsTemplate";
		JCheckBoxMenuItem mntmTemplate = new JCheckBoxMenuItem(loc.getLabel(k));
		mntmTemplate.setMnemonic(loc.getKey(k));
		mntmTemplate.addActionListener(actionListenerTemplate);
		mnOptions.add(mntmTemplate);
		
		// -----------------------------------------------
		// Listener para activar / desactivar las opciones
		// -----------------------------------------------
		
		mnOptions.addMenuListener(new MenuListener() {
			
			@Override
		    public void menuSelected(MenuEvent e) {
				
				mntmTip.setState(cfgTip);
				mntmTemplate.setState(cfgTemplate);
			}
			
		    @Override
		    public void menuDeselected(MenuEvent e) {
		    	//cfgTip = mntmTip.getState();
		    	//cfgTemplate = mntmTemplate.getState();
		    	//System.out.println(cfgTip + ", " + cfgTemplate);
		    }

		    @Override
		    public void menuCanceled(MenuEvent e) {
		    	// Nada
		    }

		});
		
		menuBar.add(mnOptions);
		
		// ---------
		// MENU Help
		// ---------
		
		k = "Help";
		JMenu mnHelp = new JMenu(loc.getLabel(k));
		mnHelp.setMnemonic(loc.getKey(k));
		menuBar.add(mnHelp);
		
		k = "HelpContents";
		JMenuItem mntmShowTheHelp = new JMenuItem(loc.getLabel(k));
		mntmShowTheHelp.setMnemonic(loc.getKey(k));
		mntmShowTheHelp.setAccelerator(loc.getAccel(k));
		mntmShowTheHelp.addActionListener(actionListenerHelp);
		mnHelp.add(mntmShowTheHelp);
		
		mnHelp.addSeparator();
		
		k = "HelpAbout";
		JMenuItem mntmAbout = new JMenuItem(loc.getLabel(k));
		mntmAbout.setMnemonic(loc.getKey(k));
		mntmAbout.addActionListener(actionListenerAbout);
		mnHelp.add(mntmAbout);
		
		// ----------------
		// Panel contenedor
		// ----------------
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		// ----------------
		// Barra de botones
		// ----------------
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		JButton btnNew = new JButton();
		btnNew.setToolTipText(loc.getTip("FileNew"));
		btnNew.addActionListener(actionListenerNew);
		btnNew.setIcon(new ImageIcon(getClass().getResource("/New.png")));
		toolBar.add(btnNew);
		
		JButton btnOpen = new JButton();
		btnOpen.setToolTipText(loc.getTip("FileOpen"));
		btnOpen.addActionListener(actionListenerOpen);
		btnOpen.setIcon(new ImageIcon(getClass().getResource("/Open.png")));
		toolBar.add(btnOpen);
		
		JButton btnSave = new JButton();
		btnSave.setToolTipText(loc.getTip("FileSave"));
		btnSave.addActionListener(actionListenerSave);
		btnSave.setIcon(new ImageIcon(getClass().getResource("/Save.png")));
		toolBar.add(btnSave);
		
		JButton btnPrint = new JButton();
		btnPrint.setToolTipText(loc.getTip("FilePrint"));
		btnPrint.addActionListener(actionListenerPrint);
		btnPrint.setIcon(new ImageIcon(getClass().getResource("/Print.png")));
		toolBar.add(btnPrint);
		
		toolBar.addSeparator();
		
		JButton btnCut = new JButton();
		btnCut.setToolTipText(loc.getTip("EditCut"));
		btnCut.addActionListener(actionListenerCut);
		btnCut.setIcon(new ImageIcon(getClass().getResource("/Cut.png")));
		toolBar.add(btnCut);
		
		JButton btnCopy = new JButton();
		btnCopy.setToolTipText(loc.getTip("EditCopy"));
		btnCopy.addActionListener(actionListenerCopy);
		btnCopy.setIcon(new ImageIcon(getClass().getResource("/Copy.png")));
		toolBar.add(btnCopy);
		
		JButton btnPaste = new JButton();
		btnPaste.setToolTipText(loc.getTip("EditPaste"));
		btnPaste.addActionListener(actionListenerPaste);
		btnPaste.setIcon(new ImageIcon(getClass().getResource("/Paste.png")));
		toolBar.add(btnPaste);
		
		JButton btnDelete = new JButton();
		btnDelete.setToolTipText(loc.getTip("EditDelete"));
		btnDelete.addActionListener(actionListenerDelete);
		btnDelete.setIcon(new ImageIcon(getClass().getResource("/Delete.png")));
		toolBar.add(btnDelete);
		
		toolBar.addSeparator();
		
		JButton btnFind = new JButton();
		btnFind.setToolTipText(loc.getTip("EditFind"));
		btnFind.addActionListener(actionListenerFind);
		btnFind.setIcon(new ImageIcon(getClass().getResource("/Find.png")));
		toolBar.add(btnFind);
		
		toolBar.addSeparator();
		
		JButton btnRun = new JButton();
		btnRun.setToolTipText(loc.getTip("RunRun"));
		btnRun.addActionListener(actionListenerRun);
		btnRun.setIcon(new ImageIcon(getClass().getResource("/Run.png")));
		toolBar.add(btnRun);
		
		JButton btnStop = new JButton();
		btnStop.setToolTipText(loc.getTip("RunStop"));
		btnStop.addActionListener(actionListenerStop);
		btnStop.setIcon(new ImageIcon(getClass().getResource("/Stop.png")));
		toolBar.add(btnStop);
		
		toolBar.addSeparator();
		
		JButton btnHelp = new JButton();
		btnHelp.setToolTipText(loc.getTip("HelpContents"));
		btnHelp.addActionListener(actionListenerHelp);
		btnHelp.setIcon(new ImageIcon(getClass().getResource("/Help.png")));
		toolBar.add(btnHelp);
		
		toolBar.addSeparator();
		
		JButton btnExit = new JButton();
		btnExit.setToolTipText(loc.getTip("FileExit"));
		btnExit.addActionListener(actionListenerExit);
		btnExit.setIcon(new ImageIcon(getClass().getResource("/Exit.png")));
		toolBar.add(btnExit);
		
		// ---------------------------------------
		// Men� contextual para el �rea de edici�n
		// ---------------------------------------
		
		// Nota: No se pueden aprovechar las opciones equivalentes del men� de edici�n,
		//       puesto que genera conflictos en Swing.
		
		JPopupMenu popupMenu = new JPopupMenu();
		
		JMenuItem mntmContextCut = new JMenuItem(loc.getLabel("EditCut"));
		mntmContextCut.addActionListener(actionListenerCut);
		popupMenu.add(mntmContextCut);
		
		JMenuItem mntmContextCopy = new JMenuItem(loc.getLabel("EditCopy"));
		mntmContextCopy.addActionListener(actionListenerCopy);
		popupMenu.add(mntmContextCopy);
		
		JMenuItem mntmContextPaste = new JMenuItem(loc.getLabel("EditPaste"));
		mntmContextPaste.addActionListener(actionListenerPaste);
		popupMenu.add(mntmContextPaste);
		
		JMenuItem mntmContextDelete = new JMenuItem(loc.getLabel("EditDelete"));
		mntmContextDelete.addActionListener(actionListenerDelete);
		popupMenu.add(mntmContextDelete);
		
		popupMenu.addSeparator();
		
		JMenuItem mntmContextSelectAll = new JMenuItem(loc.getLabel("EditSelectAll"));
		mntmContextSelectAll.addActionListener(actionListenerSelectAll);
		popupMenu.add(mntmContextSelectAll);
		
		// ----------------------------------------
		// L�stener para activar el men� contextual
		// ----------------------------------------
		
		contents.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				showPopUp(e);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				showPopUp(e);
			}

			private void showPopUp(MouseEvent e) {
				// Activar si corresponde
				if (e.isPopupTrigger()) {
					
					// �Hay texto seleccionado?
					boolean selection = contents.getSelectionStart() != contents.getSelectionEnd();

					// Activar si hay texto seleccionado
					mntmContextCut.setEnabled(selection);
					mntmContextCopy.setEnabled(selection);
					mntmContextDelete.setEnabled(selection);
					
					// Activar si el clipboard contiene datos
					mntmContextPaste.setEnabled(Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null) != null);
					
					// Mostrar el men� contextual
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});		
		
		// ------------------------------------------
		// Listener para detectar cambios en el texto
		// ------------------------------------------
		
		docListener = new DocumentListener() {
			
	        @Override
	        public void removeUpdate(DocumentEvent e) {
	        	textChanged = true;
	        }

	        @Override
	        public void insertUpdate(DocumentEvent e) {
	        	textChanged = true;
	        }

	        @Override
	        public void changedUpdate(DocumentEvent arg0) {
	        	textChanged = true;
	        }
	    };
	    
	    contents.getDocument().addDocumentListener(docListener);
	    
	    // ---------------------------------------------------------------------
	    // Listener para detectar cambios en el n� de l�nea que se est� editando
	    // ---------------------------------------------------------------------
	    
	    contents.addCaretListener(new CaretListener() {

	    	// Ser� llamado cuando el cursor (caret) sea movido a otra posici�n
	    	public void caretUpdate(CaretEvent e) {
	    		
	    		// N� de l�nea nuevo
	    		int newLineNumber;
	    		
	    		// Calcular el n� de l�nea donde est� el cursor, controlando las
	    		// posibles excepciones
	    		try {
	    			
	    			// Calcular
					newLineNumber = contents.getLineOfOffset(contents.getCaretPosition()) + 1;
					
				} catch (BadLocationException e1) {
					
					// Si hay una excepci�n, asignar un valor por defecto
					newLineNumber = 1;
				}
	    		
                //columnnum = caretpos - editArea.getLineStartOffset(linenum);

	    		// Actualizar la barra de estado, si el n� de l�nea ha cambiado
	    		if(newLineNumber != lineNumber) {
	    			
	    			// N� de l�nea
	    			lineNumber = newLineNumber;
	    			setStatusLine(lineNumber);
	    			
	    			// Texto
	    			if(statusText.getText() != null) {
	    				setStatusText(null);
	    			}
	    		}
            }
        });

		// -------------------------------------------
		// Varias inicializaciones del �rea de edici�n
		// -------------------------------------------
		
		contents.setFont(contentsFont);
		scrollPane.setViewportView(contents);

		// -------------------------------------------------
		// Lista de palabras reservadas del lenguaje TinyDev
		// -------------------------------------------------
	
		// Panel
		JScrollPane scrollPaneKeywords = new JScrollPane();
		scrollPaneKeywords.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scrollPaneKeywords, BorderLayout.EAST);
		
		// Lista de palabras reservadas
		JList<String> list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// Modelo para la lista
		list.setModel(new AbstractListModel<String>() {
			
			// Devolver tama�o de la lista
			public int getSize() {
				
				return keywords.length;
			}
			
			// Devolver la palabra solicitada
			public String getElementAt(int index) {
				
				return keywords[index];
			}
		});
		
		// --------------------------------------------------------------------
		// Listener para insertar en el texto la palabra reservada seleccionada
		// --------------------------------------------------------------------
		
		list.addListSelectionListener(new ListSelectionListener() {
			
			public void valueChanged(ListSelectionEvent e) {
				
				// Proceder, si ya no se est�n haciendo m�s cambios en la lista
				if(e.getValueIsAdjusting() == false) {
					
					// Devolver la palabra seleccionada en la lista
					String item = (String) list.getSelectedValue();
					
					// Insertar la palabra en la posici�n actual del �rea de edici�n
					if(item != null) {
						
						contents.insert(item + " ", contents.getCaretPosition());
						contents.requestFocus();
					}
				}
			}
		});
		
		scrollPaneKeywords.setViewportView(list);
		
		// ----------------------------------------------------------------------
		// Filtro de nombre de fichero para los cuadros de di�logo Open / Save As
		// ----------------------------------------------------------------------
		
		fileFilter = new FileFilter() {

			@Override
			public boolean accept(File f) {
				
				// Mostrar directorios tambi�n
				if (f.isDirectory()) {
			        return true;
			    }

				// Mostrar ficheros que terminen en ".td"
		        String fn = f.getName();      // Nombre del fichero
		        int i = fn.lastIndexOf('.');  // Posici�n de la extensi�n del nombre del fichero (p.ej.: '.td')

		        // Comprobar la extensi�n, si existe
		        if (i > 0 &&  i < fn.length() - 1) {
		        	
		        	// Tomar la extensi�n (p.ej.: 'td')
		            String ext = fn.substring(i+1).toLowerCase();
		            
		            // Mostrar el nombre de fichero, si la extensi�n es 'td'
		            if(ext.equals("td"))
			        	return true;
		        }
		        
		        // No coincide, no mostrar
		        return false;
			}

			@Override
			public String getDescription() {

				// Devolver descripci�n del filtro / tipo de fichero
				return loc.getString("FileFilterDescription");
			}
		};
		
		// ---------------
		// Barra de estado
		// ---------------
		
		statusPanel = new JPanel();  // Panel
		statusLine = new JLabel();   // N� de l�nea actual
		statusText = new JLabel();   // Texto o mensaje
		
		//statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		//statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		// Tama�o del panel
		statusPanel.setPreferredSize(new Dimension(this.getWidth(), 24));
		
		// Layout del panel
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		
		// A�adir n� de l�nea actual
		statusPanel.add(statusLine);
		
		//JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		//statusPanel.add(separator);
		
		// Separador
		statusPanel.add(new JLabel("  |  "));
		
		// Texto o mensaje
		statusPanel.add(statusText);
		
		//statusPanel.add(Box.createHorizontalGlue());

		// A�adir el panel al frame
		this.add(statusPanel, BorderLayout.SOUTH);
		
		// ---------------------
		// Leer la configuraci�n
		// ---------------------
		
		config(false);       // Leer la configuraci�n
		cfgChanged = false;  // No hay cambios en la configuraci�n

		// ---------------------------
		// Finalizar la inicializaci�n
		// ---------------------------
		
		this.setVisible(true);
		contents.requestFocusInWindow();
		
		// --------------------------------------
		// �rea de edici�n en blanco, sin fichero
		// --------------------------------------
		
		doNew();
		changeTitle();
		setStatusLine(1);
		setStatusText(null);
		
		// ---------------------
		// Mostrar tip (consejo)
		// ---------------------
		
		if(cfgTip && tip != null) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						// Mostrar tip
						JOptionPane.showMessageDialog(null, // FIXME
							    tip,
							    loc.getTitle("Tip"),
							    JOptionPane.INFORMATION_MESSAGE,
							    new ImageIcon(appIcon));
					} catch (Exception e) {
						// Mostrar mensaje de error y abortar
						fatalError(e);
					}
				}
			});
		}
	}
	
	/**
	 * Ejecutar la opci�n FILE / NEW
	 */
	private void doNew() {
		
		// Si hay cambios sin grabar, preguntar al usuario
		if(askSave()) {

			if(cfgTemplate) {
				if(!readFile("templates" + File.separator + loc.getString("Template"))) {
					contents.setText(null); // FIXME: No deber�a cambiar nada, sino salir
				}
			} else {
				//
				contents.setText(null);
			}
			//
			fileName = null;
			textChanged = false;
			currentDirectory = System.getProperty("user.dir");
			
			changeTitle();
			setStatusLine(1);
			setStatusText(null);
		}
		
		// Retomar el foco
		contents.requestFocusInWindow();
	}
	
	/**
	 * Ejecutar la opci�n FILE / OPEN
	 */
	private void doOpen() {
		
		// Si hay cambios sin grabar, preguntar al usuario
		if(askSave()) {
			
			// Cuadro de di�logo para seleccionar un fichero
			JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
			
			// Establecer filtro para la extensi�n de los ficheros
			fileChooser.setFileFilter(fileFilter);
	
			// Leer el fichero, si el usuario ha seleccionado uno
			if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				
				// Tomar el nombre del fichero seleccionado
				String choosedFileName = fileChooser.getSelectedFile().getAbsolutePath();
		
				// Leer el fichero
				if(readFile(choosedFileName)) {
		
					// Proceder, si la lectura ha tenido �xito
					fileName = choosedFileName;
					textChanged = false;
					currentDirectory = fileChooser.getCurrentDirectory().toString();
					
					changeTitle();
					setStatusLine(1);
					setStatusText(null);
				}
			}
		}
		
		// Retormar el foco
		contents.requestFocusInWindow();
	}
	
	/**
	 * Ejecutar la opci�n FILE / SAVE
	 */
	private void doSave() {
		
		// Grabar el fichero, si tiene nombre
		if(fileName != null) {
			
				// Grabar
				if(writeFile(fileName)) {
					
					// Proceder, si la escritura ha tenido �xito
					textChanged = false;
					
					// Retomar el foco
					contents.requestFocusInWindow();
					
					return;
			}
		}

		// Grabar el fichero, solicitando el nombre
		doSaveAs();
	}
	
	/**
	 * Ejecutar la opci�n FILE / SAVE AS
	 */
	private void doSaveAs() {
		
		// Cuadro de di�logo, para elecci�n del nombre de fichero
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
		
		// Establecer el filtro para la extensi�n de los ficheros
		fileChooser.setFileFilter(fileFilter);
		
		// Proceder, si el usuario ha seleccionado un fichero
		if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			
			// Nombre del fichero seleccionado
			String choosedFileName = fileChooser.getSelectedFile().getAbsolutePath();
			
			// A�adir la extensi�n del fichero, si no tiene una
			if(choosedFileName.indexOf('.') == -1)
				choosedFileName = choosedFileName.concat(".td");
			
			// Escribir el fichero
			if(writeFile(choosedFileName)) {
				
				// Proceder, si la escritura ha tenido �xito
				fileName = choosedFileName;
				textChanged = false;
				currentDirectory = fileChooser.getCurrentDirectory().toString();
				
				changeTitle();
			}
		}
		
		// Retomar el foco
		contents.requestFocusInWindow();
	}
	
	/**
	 * Ejecutar la opci�n FILE / PRINT
	 */
	private void doPrint() {
		
		// Nota: No se puede cambiar el icono del di�logo de impresi�n,
		//       ya que no podemos acceder al JFrame correspondiente.
		
		try {
			
			// Ejecutar la opci�n de impresi�n, indicando el formato de la cabecera
			// y pi�
			contents.print(new MessageFormat("TinyDev: " + fileName), new MessageFormat("Page {0}"));
			
		} catch (Exception e) {
			
			// Mostrar mensaje de error
			printError(loc.getError("CantPrintFile"));
		}
		
		// Retomar el foco
		contents.requestFocusInWindow();
	}
	
	/**
	 * Ejecutar la opci�n FILE / EXIT
	 */
	private void doExit() {
		
		// Si hay cambios sin grabar, preguntar al usuario
		if(askSave()) {
			
			// Grabar la configuraci�n actual, si hay cambios
			// pendientes de grabar
			if(cfgChanged) {
				config(true);
			}
			
			// Modificar el comportamiento de cerrado
			// de la ventana, para que la aplicaci�n finalice
			// al salir de este m�todo
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			// Cerrar la ventana de ejecuci�n, si est� abierta
			if(screen != null && screen.isClosed() == false) {
				screen.quit();
			}
			
			// Cerrar la ventana del IDE
			dispose();
			
			// En su lugar, se podr�a haber utilizado:
			
			/**************
			System.exit(0);
			***************/
			
			// Pero queda m�s elegante lo anterior (digo yo)
		}
		
		// Retomar el foco
		contents.requestFocusInWindow();
	}
	
	/**
	 * Ejecutar la opci�n EDIT / CUT
	 */
	private void doCut() {
		
		// Operaci�n CUT (no hace nada, si no hay selecci�n)
		contents.cut();
		
		// Retomar el foco
		contents.requestFocusInWindow();
	}
	
	/**
	 * Ejecutar la opci�n EDIT / COPY
	 */
	private void doCopy() {
		
		// Operaci�n COPY (no hace nada, si no hay selecci�n)
		contents.copy();
		
		// Retomar el foco
		contents.requestFocusInWindow();
	}
	
	/**
	 * Ejecutar la opci�n EDIT / PASTE
	 */
	private void doPaste() {
		
		// Operaci�n PASTE (no hace nada, el portapapeles est� vac�o)
		contents.paste();
		
		// Retomar el foco
		contents.requestFocusInWindow();
	}
	
	/**
	 * Ejecutar la opci�n EDIT / DELETE
	 */
	private void doDelete() {
		
		// Operaci�n DELETE (no hace nada, si no hay selecci�n)
		contents.replaceSelection("");
		
		// Retomar el foco
		contents.requestFocusInWindow();
	}
	
	
	/**
	 * Ejecutar la opci�n EDIT / SELECT ALL
	 */
	private void doSelectAll() {
		
		// Operaci�n SELECT ALL (no hace nada, si no hay texto)
		contents.selectAll();
		
		// Retormar el foco
		contents.requestFocusInWindow();
	}
	
	/**
	 * Ejecutar la opci�n EDIT / FIND
	 */
	private void doFind() {
		
		// Cuadro de di�logo de b�squeda de texto
		String whatToFind = (String) JOptionPane.showInputDialog(this, loc.getMsg("Find"), loc.getTitle("Find"),
				JOptionPane.QUESTION_MESSAGE, new ImageIcon(getClass().getResource("/Find.png")),
				null, findValue) ;
		
		// Comprobar la respuesta del usuario
		if(whatToFind != null) {
			
			// El usuario introdujo un texto a buscar

			// Buscar el texto
			findValue = whatToFind;
			doFindNext();
			
		} else {
			// El usuario cancel� la operaci�n de b�squeda
			
			// Retomar el foco
			contents.requestFocusInWindow();
		}
	}
	
	/**
	 * Ejecutar la opci�n EDIT / FIND NEXT
	 */
	private void doFindNext() {
		
		// Buscar el texto, si el contenido es v�lido (no es nulo, y no es la cadena vac�a "")
		if(findValue != null && findValue.length() > 0) {
			
			// Tomar la posici�n actual del cursor en el �rea de edici�n
			int caretPos = contents.getCaretPosition();
			
			// Tomar el texto
			String text = contents.getText();
			
			// Buscar el texto, a partir de dicha posici�n + 1
			int nextPos = text.indexOf(findValue, caretPos + 1);
			
			// Si no se ha encontrado, realizar otra b�squeda, a partir del principio del texto
			if(nextPos == -1) {
				nextPos = text.indexOf(findValue);
			}
			
			// Si se ha encontrado, actualizar la posici�n del cursor
			if(nextPos != -1) {
				contents.setCaretPosition(nextPos);
			}
		}
			
		// Retormar el foco
		contents.requestFocusInWindow();
	}
	
	/**
	 * Ejecutar la opci�n OPTIONS / RUN
	 */
	private void doRun() {
		
		// Si hay cambios sin grabar, preguntar al usuario
		if(askSave()) {
			
			setStatusText(null);

			// Transformar el c�digo fuente del �rea de edici�n,
			// en un array de cadenas de texto
			String str_array[] = contents.getText().split("\\r?\\n");
			
			// Transformar el array de cadenas de texto,
			// en un ArrayList de cadenas de texto
		    ArrayList<String>sourceCode = new ArrayList<>(Arrays.asList(str_array));
		    
		    // Crear un objeto TinyDevScreen, si no existe ya		    
		    if(screen == null)
				screen = new TinyDevScreen();
		    
		    // Ejecutar el int�rprete
		    lang.execute(sourceCode, screen, currentDirectory);
		}
	    
	    // Retomar el foco
	    contents.requestFocusInWindow();
	}
	
	/**
	 * Ejecutar la opci�n OPTIONS / STOP
	 */
	private void doStop() {
		
		// Parar el int�rprete de TinyDev
		lang.stop();
		
		// Retomar el foco
		contents.requestFocusInWindow();
	}
	
	/**
	 * Ejecutar la opci�n HELP / HELP CONTENTS
	 */
	private void doHelp() {
		
		// NOTA: La idea original, era incluir el topic a buscar al lanzar el navegador web,
		//       para que mostrase la secci�n correspondiente.
		//       Desafortunadamente, �sto funciona con URIs de WWW, pero
		//       no con URIs locales:
		//
		//       --quote--
		//       The net based URIs will scroll to the anchor, disk based URIs will not.
		//       Raised a bug report with Oracle
		//       (ID: 7143677 - Desktop.browse() - Anchors are ignored for local URIs).
		//       --unquote--
		//
		//       Como parece ser que hay alg�n m�todo chapucero que s�lo funciona con Windows, y no
		//       siempre, he preferido no implementarlo.
		//
		//       Por lo tanto, este m�todo, lanza el navegador con el archivo HTML de ayuda.
		
		// Lanzar el navegador WEB, para que abra el archivo HTML de ayuda
		try {
			
			// Crear URI a partir del nombre del fichero del archivo de ayuda
			//URI uri = new URI("./help/" + loc.getString("HelpFilename") + ".html");
			URI uri = new File("help" + File.separator + loc.getString("HelpFilename") + ".html").toURI();
			
			// Lanzar el navegador WWW para que abra dicho fichero HTML
			Desktop.getDesktop().browse(uri);
			
		} catch (Exception e) {
			
			// Mostrar mensaje de error
			printError(loc.getError("CantRunHelp"));
		}
		
		// Retomar el foco
		contents.requestFocusInWindow();
	}
	
	/**
	 * Ejecutar la opci�n HELP / ABOUT
	 */
	private void doAbout() {
			
		// Mostrar di�logo
		JOptionPane.showMessageDialog(this,
				loc.getMsg("About_title") + "\n" + APP_VERSION + "\n\n" +
				APP_COPYRIGHT + "\n" + loc.getMsg("About_license") + "\n" +
				loc.getMsg("About_rights") + "\n\n" +
				APP_WEBSITE + "\n\n",
			    loc.getTitle("About"),
			    JOptionPane.INFORMATION_MESSAGE,
			    new ImageIcon(appIcon));
		
		// Retomar el foco
		contents.requestFocusInWindow();
	}
	
	// ------------------
	// METODOS AUXILIARES
	// ------------------
	
	/**
	 * Leer un fichero de c�digo fuente. Muestra un mensaje
	 * de error, si fracasa.
	 * 
	 * @param fn  nombre del fichero
	 * @return    true en caso de �xito, false en caso contrario
	 */
	private boolean readFile(String fn) {
		
		FileReader fr = null;  // FileReader
		boolean ok = true;     // Por defecto: �xito
		
		// Leer el contenido del fichero
		try {
			
			// Crear el FileReader
			fr = new FileReader(fn);
			
			// Leer su contenido, y enviarlo al �rea de texto
			contents.read(fr, "contents");
			
		} catch (Exception e) {

			// Se�alar el error
			ok = false;
		}
		
		// A�adir el DocumentListener, para detectar cambios
		// en el texto
		contents.getDocument().addDocumentListener(docListener);
		
		// NOTA: Aunque en la inicializaci�n ya se ha a�adido el DocumentListener,
		//       se ha de volver a hacer aqu�, puesto que al ejecutar la
		//       instrucci�n ' contents.read(fr, "contents"); ',
		//       se cambia el modelo de Documento, y dicho listener deja
		//       de funcionar.
		
		// Cerrar el FileReader si est� abierto
		if(fr != null) {
			try {
				
				// Cerrar
				fr.close();
				
			} catch (Exception e) {
				
				// Se�alar el error
				ok = false;
			}
		}
		
		// Mostrar un mensaje de error, si procede
		if(!ok)
			printError(loc.getError("CantReadFile"));
		
		// Devolver �xito o fracaso
		return ok;
	}
	
	/**
	 * Escribir un fichero de c�digo fuente. Muestra un mensaje
	 * de error, si fracasa.
	 * 
	 * @param fn  nombre del fichero
	 * @return    true en caso de �xito, false en caso contrario
	 */
	private boolean writeFile(String fn) {
		
		FileWriter fw = null;  // FileWriter
		boolean ok = true;     // Por defecto: �xito
		
		// Escribir el fichero
		try {
			
			// Crear el FileWriter
			fw = new FileWriter(fn);
			
			// Escribir el contenido del �rea de edici�n en el fichero
			contents.write(fw);
			
		} catch (Exception e) {
			
			// Se�alar el error
			ok = false;
		}
		
		// Cerrar el FileWriter si est� abierto
		if(fw != null) {
			try {
				
				// Cerrar
				fw.close();
			} catch (Exception e) {
				
				// Se�alar el error
				ok = false;
			}
		}
		
		// Mostrar un mensaje de error, si procede
		if(!ok)
			printError(loc.getError("CantWriteFile"));
		
		// Devolver �xito o fracaso
		return ok;
	}
	
	/**
	 * Cambiar el t�tulo de la ventana del IDE, indicando
	 * el nombre del archivo que se est� editando, o "untitled"
	 * si todav�a no tiene nombre.
	 */
	private void changeTitle() {
		
		// Cambiar el t�tulo de la ventana del IDE
		setTitle("TinyDev - " + (fileName == null ? loc.getString("Untitled") : fileName));
	}
	
	
	/**
	 * Si el c�digo fuente ha sido modificado y no se ha grabado todav�a,
	 * preguntar al usuario si quiere grabarlo y continuar con
	 * la operaci�n (utilizado antes de las operaciones SAVE, EXIT, etc.):
	 * 
	 * YES:    Se graba fichero. Si tiene �xito, devuelve true. Si fracasa,
	 *         devuelve false.
	 *      
	 * NO:     Devuelve true.
	 * 
	 * CANCEL: Devuelve false.
	 * 
	 * @return true para continuar con la operaci�n, false para abortarla
	 */
	private boolean askSave() {

		// Si hay cambios no grabados, preguntar al usuario
		// qu� hacer
		if(textChanged) {
			
			// Di�logo para preguntar al usuario: YES, NO, CANCEL
			int reply = JOptionPane.showConfirmDialog(this,
					loc.getMsg("Save"),
					loc.getTitle("Save"),
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			
			// Si el usuario selecciona YES, grabar el fichero antes de proceder
			if(reply == JOptionPane.YES_OPTION) {
				
				// Grabar el fichero
				doSave();
				
				// Si finalmente no se ha grabado (debido a un error, o a cancelaci�n
				// por parte del usuario), abortar la operaci�n
				if(textChanged)
					return false;				
			}
			
			// Si el usuario selecciona NO, se proseguir� con la operaci�n (no
			// hace falta incluir c�digo espec�fico para esto)
			
			// Si el usuario selecciona CANCEL, se cancelar� la operaci�n
			if(reply == JOptionPane.CANCEL_OPTION)
				return false;
		}
		
		// Proseguir con la operaci�n		
		return true;
	}
	

	private void config(boolean write) {
		
		if(write) {
			props.set("tips", cfgTip ? "true" : "false");
			props.set("template", cfgTemplate ? "true" : "false");
			
			props.save();
		} else {
			props.load();
			
			cfgTip = props.get("tips", "false").equals("true");
			cfgTemplate = props.get("template", "false").equals("true");
			
			// Tip
			if(cfgTip) {
				try {
					ResourceBundle rb = ResourceBundle.getBundle("TinyDevTips");
					
					int howManyTips = rb.keySet().size();
					int whichTip = new Random().nextInt(howManyTips) + 1; // nextInt == 0 .. howMany - 1
					
					String tipKey = Integer.toString(whichTip);
					
					if(rb.containsKey(tipKey)) {
						tip = rb.getString(tipKey);
					}
					
				} catch(Exception e) {
					e.printStackTrace(); // FIXME controlar mejor las excepciones
				}
			}
		}
		
		/*
		Connection con = null; et // Conexi�n a la BD
		Statement stm = null;   // Statement para la BD
		ResultSet rst = null;   // ResultSet para obtenci�n de datos de la BD
		String configTable = null; // Nombre de la tabla de configuraci�n
		
		// Acceso a la BD de SQLite
		try {
			
			// Acceso a la clase
			Class.forName("org.sqlite.JDBC");
			
			// Obtener una conexi�n con la BD
			con = DriverManager.getConnection("jdbc:sqlite:" + loc.getString("SQLiteFilename") + ".db");
			
			// Crear un statement
			stm = con.createStatement();
			
			configTable = loc.getString("ConfigTable");
			
			if(write) {
				
				stm.executeUpdate("UPDATE " + configTable + " SET value = '" + cfgTip + "' WHERE name = 'ShowTipOnStart'");
				stm.executeUpdate("UPDATE " + configTable + " SET value = '" + cfgTemplate + "' WHERE name = 'UseTemplateInNewFiles'");

				con.commit();

			} else {
				
				// Leer la configuraci�n
				
				rst = stm.executeQuery("SELECT value FROM " + configTable + " WHERE name='ShowTipOnStart'");
				
				cfgTip = rst.getString(1).equals("true") ? true : false;
				
				rst = stm.executeQuery("SELECT value FROM " + configTable + " WHERE name='UseTemplateInNewFiles'");
				
				cfgTemplate = rst.getString(1).equals("true") ? true : false;
				
				// Leer un tip aleatoriamente
				
				if(cfgTip) {
					// Averiguar cu�ntas filas (registros) hay en la tabla de tips
					rst = stm.executeQuery("SELECT COUNT(*) FROM " + loc.getString("TipsTable"));
					
					// La 1� columna es la n� 1
					int howMany = rst.getInt(1);
					
					// Obtener un n� de fila aleatoriamente
					int whichRow = new Random().nextInt(howMany) + 1; // nextInt == 0 .. howMany - 1
				
					// Acceder a ficha fila
					rst = stm.executeQuery("SELECT contents FROM " + loc.getString("TipsTable") + " WHERE id=" + (whichRow));
					
					// Tomar la descripci�n del tip
					tip = rst.getString(1);
				}
			}
			
		// Controlar posibles excepciones
		} catch(Exception e) {
			
			// Nada FIXME: Mostrar mensaje de error, y establecer la config. por defecto
		}
		
		// Cerrar el ResultSet si fue abierto
		if(rst != null) {
			try {
				rst.close();
			} catch(SQLException e) {
				// Nada
			}
		}
		
		// Cerrar el Statement si fue abierto
		if(stm != null) {
			try {
				stm.close();
			} catch(SQLException e) {
				// Nada
			}
		}
		
		// Cerrar la conexi�n si fue abierta
		if(con != null) {
			try {
				con.close();
			} catch(SQLException e) {
				// Nada
			}
		}
		*/
	}
	
	/**
	 * Informar al usuario de un error no fatal.
	 * 
	 * @param msg  mensaje describiendo el error
	 */
	private void printError(String msg) {
		
		// Mostrar di�logo con la informaci�n del error
		JOptionPane.showMessageDialog(this, msg, loc.getTitle("Error"), JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Informar al usuario de un error fatal, y terminar el programa.
	 * 
	 * @param e  excepci�n
	 */
	private static void fatalError(Exception e) {
		
		// Mostrar la informaci�n del error
		e.printStackTrace();
		
		// Finalizar el programa, indicando terminaci�n anormal
		System.exit(-1);
	}
	
	/**
	 * Cambiar el n� de l�nea en la barra de estado.
	 * 
	 * @param lineNumber  n�mero de l�nea
	 */
	private void setStatusLine(int lineNumber) {
		
		// Cambiar
		statusLine.setText(String.format("%05d", lineNumber));
	}
	
	/**
	 * Cambiar el texto de la barra de estado. Si su valor es null,
	 * se mostrar� en blanco.
	 * 
	 * @param text  texto a mostrar
	 */
	private void setStatusText(String text) {
		
		// Cambiar
		statusText.setText(text);
	}
	
	/**
	 * Indicar que durante el parseado o la ejecuci�n, se ha producido un error.
	 * 
	 * Cambiar la posici�n del cursor a la l�nea correspondiente, si �sta sigue
	 * siendo la original (ha podido editarse, o se ha cargado otro fichero).
	 * 
	 * @param lineNumber  n�mero de l�nea (1..?)
	 * @param sourceCode  l�nea de c�digo fuente
	 * @param errorMsg    mensaje de error
	 */
	public void notifyError(int lineNumber, String sourceCode, String errorMsg) {
		
		// Tomar posici�n actual del cursor de edici�n
		int oldCaretPos = contents.getCaretPosition();
		
		// Cambiar la posici�n del cursor a la l�nea que ha originado el error,
		// y mostrar en la barra de estado, el mensaje de error
		try {
			
			// Cambiar la posici�n del cursor a la l�nea que ha originado el
			// error, primera columna
			contents.setCaretPosition(contents.getDocument()
	                .getDefaultRootElement().getElement(lineNumber)
	                .getStartOffset());
			
			// Comprobar que el contenido de la l�nea no ha cambiado, y proceder
			if(contents.getText(contents.getCaretPosition(), sourceCode.length() + 1).equals(sourceCode + "\n")) {
				
				// Mostrar mensaje de error en la barra de estado
				setStatusText(errorMsg);
				
				// Llevar al frente la ventana del editor
				this.toFront();

				// Finalizar
				return;
			}
		
		  // Controlar posibles excepciones
		} catch(IllegalArgumentException | BadLocationException e) {
			
			// Nada
		}
		
		// Se ha producido una excepci�n, o el contenido original de la l�nea ha cambiado,
		// por lo que no procede cambiar la posici�n del cursor, ni mostrar el mensaje de error
		
		// Volver a la posici�n anterior del cursor
		contents.setCaretPosition(oldCaretPos);
	}
}

