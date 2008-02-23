/*
 *  ********************************************************************   **
 *  Copyright notice                                                       **
 *  **																	   **
 *  (c) 2003 Entagged Developpement Team				                   **
 *  http://www.sourceforge.net/projects/entagged                           **
 *  **																	   **
 *  All rights reserved                                                    **
 *  **																	   **
 *  This script is part of the Entagged project. The Entagged 			   **
 *  project is free software; you can redistribute it and/or modify        **
 *  it under the terms of the GNU General Public License as published by   **
 *  the Free Software Foundation; either version 2 of the License, or      **
 *  (at your option) any later version.                                    **
 *  **																	   **
 *  The GNU General Public License can be found at                         **
 *  http://www.gnu.org/copyleft/gpl.html.                                  **
 *  **																	   **
 *  This copyright notice MUST APPEAR in all copies of the file!           **
 *  ********************************************************************
 */
package entagged.tageditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import miage.Atome;
import miage.ListeFichiers;
import miage.ihm.JButton_Playlist;
import miage.ihm.JDialog_Doublons;
import miage.ihm.JDialog_MauvaisesFrappes;
import miage.ihm.JTextField_Recherche;
import miage.sgbd.DataProvider;


import entagged.tageditor.actions.BrowseBackwardAction;
import entagged.tageditor.actions.BrowseIntoAction;
import entagged.tageditor.actions.BrowseUpAction;
import entagged.tageditor.actions.CtrlTableSelectionAction;
import entagged.tageditor.actions.ReloadAction;
import entagged.tageditor.actions.TableEnterAction;
import entagged.tageditor.actions.FocusRequestAction;
import entagged.tageditor.listeners.DialogWindowListener;
import entagged.tageditor.listeners.NavigatorListener;
import entagged.tageditor.listeners.TableReselector;
import entagged.tageditor.models.FileTreeModel;
import entagged.tageditor.models.Navigator;
import entagged.tageditor.models.TableSorter;
import entagged.tageditor.models.TagEditorTableModel;
import entagged.tageditor.renderers.TagEditorTableCellRenderer;
import entagged.tageditor.resources.Initialization;
import entagged.tageditor.resources.InitializationMonitor;
import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.resources.PreferencesManager;
import entagged.tageditor.resources.ResourcesRepository;
import entagged.tageditor.util.SelectionRecord;
import entagged.tageditor.util.Utils;

/**
 * Main Class. Entry point for the GUI setup the Components like tree,
 * splitpanes etc $Id: TagEditorFrame.java,v 1.58 2005/01/04 20:55:10 kikidonk
 * Exp $
 * 
 * @author Raphael Slinckx (KiKiDonK) ; Nicolas Velin
 * @version v0.03
 */
public class TagEditorFrame extends JFrame {

	private class AlbumTableSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			//Ignore extra messages.
			if (e.getValueIsAdjusting())
				return;

			TagEditorFrame.this.getControlPanel().clear();

			int[] rows = table.getSelectedRows();
			for (int i = 0; i < rows.length; i++) {
				File f = tableModel.getFileAt(tableSorter.modelIndex(rows[i]));
				TagEditorFrame.this.getControlPanel().add(f);
			}
			TagEditorFrame.this.getControlPanel().update();
			TagEditorFrame.this.getControlPanel().processFileDifference();
		}
	}
	
	private static ArrayList<String> checkList;
	
	public static ArrayList<String> getCheckList() {
		return checkList;
	}

	private class ExpandSelectedRowMouseAdapter extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			/* FIXME:
			 * Not a good code
			 */
			String realColumnName = (String)table.getColumnModel().getColumn(table.getSelectedColumn()).getHeaderValue();
			if(realColumnName.equals(tableModel.getColumnName(8))) { // Click on Playlist
				int clickedRow = tableSorter.modelIndex(table.getSelectedRow());
				File f = tableModel.getFileAt(clickedRow);
				if(f.isFile()) {
					Boolean past = (Boolean)tableModel.getValueAt(clickedRow, 8);
					if(!past)
						checkList.add(tableModel.getFileAt(clickedRow).getAbsolutePath());
					else
						checkList.remove(tableModel.getFileAt(clickedRow).getAbsolutePath());
				}
			}
			//Open the directory in the table
			if (e.getClickCount() == 2) {
				if(table.getSelectedColumn() != 8) {
					int clickedRow = tableSorter.modelIndex(table.getSelectedRow());
					File f = tableModel.getFileAt(clickedRow);
					if(f.isDirectory()) {
						search.setText("");
						navigator.browseInto(f);
					}
					else if(f.isFile())
						ListeFichiers.play(TagEditorFrame.this, f.getAbsolutePath());
				}
			}
		}
	}

	/**
	 * Listens for a selection change in the Available roots combobox
	 * 
	 * @author Raphael Slinckx (KiKiDonK)
	 * @version v0.03
	 */
	private class RootSelectionListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			File selectedRoot = (File) e.getItem();

			if (selectedRoot.canRead()) {
				TagEditorFrame.this.dirChooser.setDirectory(selectedRoot);

				PreferencesManager.putInt("tageditor.tageditorframe.roots",roots.getSelectedIndex());

				TagEditorFrame.this.navigator.setDirectory(selectedRoot);

				TagEditorFrame.this.getControlPanel().clear();
				TagEditorFrame.this.getControlPanel().update();
			}
			else {
				String msg = LangageManager.getProperty("tageditorframe.drivecouldnotberead").replaceAll("%1",
						selectedRoot.toString().substring(0, 1));

				JOptionPane.showMessageDialog(TagEditorFrame.this, msg);
			}
		}
	}

	public static void main(String[] args) {
		if (!Initialization.isInitialized)
			Initialization.init(new InitializationMonitor() {
				public void setBeginning(String text) {}
				public void setBounds(int min, int max) {}
				public void setFinishing(String text) {}
				public void setStatus(String status, int val) {}
			});

		TagEditorFrame f = new TagEditorFrame();

		f.setVisible(true);
	}

	/**
	 * The Panel that holds the id3v1 and id3v2 panels and also the freedb and
	 * file rename panels
	 */
	protected ControlPanel controlPanel;

	protected DirectoryChooser dirChooser;
	
	protected JTextField_Recherche search;

	/** The Model used for the JTree (this is a view of the filesystem) */
	protected FileTreeModel fileTreeModel;

	/** Holds the MenuBar */
	protected TagEditorMenuBar menuBar;

	/** Stores the fiel history and notifies on changes */
	protected Navigator navigator;

	/** Roots selection combobox */
	protected JComboBox roots;

	/**
	 * The settings of the editor.
	 */
	protected EditorSettings settings;

	protected JTable table;

	//Needed for GUI SAVE
	protected JSplitPane tableInfoSplitPane;

	/** Splits the table and the infopanel */
	protected TagEditorTableModel tableModel;

	protected JScrollPane tableScrollPane;

	/** The selection model for the mp3album table */
	protected ListSelectionModel tableSelectionModel;  //  @jve:decl-index=0:

	protected TableSorter tableSorter;

	private JPanel jContentPane = null;
	private JPanel rootAndTree = null;
	private JPanel tablePanel;

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setSize(new Dimension(800, 600));
		
		checkList = new ArrayList<String>();

		// Sets the icon for the window manager
		ImageIcon icon = ResourcesRepository.getImageIcon("entagged-icon.png");
		this.setIconImage(icon.getImage());

		// Create the menu bar
		menuBar = new TagEditorMenuBar(this);
		this.setJMenuBar(menuBar);

		this.setContentPane(getJContentPane());

		// Sets some default things, closing behavior, location, size...
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.addWindowListener(new DialogWindowListener() {
			public void windowClosing(WindowEvent e) {
				TagEditorFrame.this.saveGUIPreferences();
				Initialization.exit();
			}
		});
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getTableInfoSplitPane(), "Center");
			jContentPane.add(getRootAndTree(), "North");
		}
		return jContentPane;
	}

	/**
	 * This method initializes rootAndTree
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getRootAndTree() {
		if (rootAndTree == null) {
			// Create container Jpanel for the tree and root selection
			
			rootAndTree = new JPanel();
			rootAndTree.setLayout(new GridBagLayout());

			// Display the root selection combobox + root detection routine, and
			// adds the selection listener
			roots = new JComboBox(File.listRoots());
			buildRootComboBox();
			roots.addItemListener(new RootSelectionListener());
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(2, 1, 2, 1);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.anchor = GridBagConstraints.CENTER;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			rootAndTree.add(roots, gridBagConstraints);
			
			dirChooser = new DirectoryChooser(this);
			gridBagConstraints.insets = new Insets(2, 9, 2, 9);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.weightx = 3;
			rootAndTree.add(dirChooser, gridBagConstraints);
			
			search = new JTextField_Recherche(dirChooser,tableModel);
			gridBagConstraints.insets = new Insets(2, 1, 2, 1);
			gridBagConstraints.gridx = 2;
			gridBagConstraints.weightx = 1;
			rootAndTree.add(search, gridBagConstraints);
			
			JButton_Playlist playList = new JButton_Playlist(this);
			gridBagConstraints.insets = new Insets(2, 1, 2, 1);
			gridBagConstraints.gridx = 3;
			gridBagConstraints.weightx = 0;
			rootAndTree.add(playList, gridBagConstraints);
			
			rootAndTree.setBorder(new EmptyBorder(5, 5, 3, 3));
		}
		return rootAndTree;
	}

	/**
	 * This method initializes tableInfoSplitPane
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getTableInfoSplitPane() {
		if (tableInfoSplitPane == null) {
			//Setup the split Panes
			tableInfoSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false);
			tableInfoSplitPane.setLeftComponent(getTablePanel());
			tableInfoSplitPane.setRightComponent(getControlPanel());
			tableInfoSplitPane.setOneTouchExpandable(true);

			restoreSplitPaneLocations();

			//BORDER TWEAKING
			tableInfoSplitPane.setBorder(new EmptyBorder(5, 0, 0, 0));
		}
		return tableInfoSplitPane;
	}

	/**
	 * This method initializes tablePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getTablePanel() {
		if (tablePanel == null) {
			// Create global settings
			settings = new EditorSettings();

			//Setup right treetable model with multiple selection allowed
			tableModel = new TagEditorTableModel(this);

			//Creates the right treetable, with the above model, turn off
			// autoresize and sets Table selection listeners
			tableSorter = new TableSorter(tableModel); //Provides sorting capabilities
			table = new JTable();
			table.setModel(tableSorter);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
			tableSelectionModel = table.getSelectionModel();
			tableSelectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			table.getSelectionModel().addListSelectionListener(new AlbumTableSelectionListener());
			tableSorter.setTableHeader(table.getTableHeader());
			// Register the TableReselector
			TableReselector reselector = new TableReselector(table);
			navigator.addNavigatorListener(reselector);
			
			TagEditorTableCellRenderer tetcr = new TagEditorTableCellRenderer(tableModel, tableSorter);
			//The renderer does not apply on the images icon
			for (int i = 1 ; i < tableModel.getColumnCount() - 1 ; i++)
				table.getColumnModel().getColumn(i).setCellRenderer(tetcr);
			
			table.setRowHeight(22);
			//MAYBE THIS NEEDS TO BE CHANGED TO FIT BETTER ICONs

			table.getTableHeader().setReorderingAllowed(true);
			table.addMouseListener(new ExpandSelectedRowMouseAdapter());
			restoreTableColumnWidth();

			//Remove the lines between the cells
			table.setGridColor(new Color(240,240,240));
			table.setShowHorizontalLines(true);
			table.setShowVerticalLines(false); 
			table.setIntercellSpacing(new Dimension(0,0));
			
			// Move the last visible column so it becomes the first visible column
			int vSrcColIndex = table.getColumnCount()-1;
			int vDstColIndex = 0;
			table.moveColumn(vSrcColIndex, vDstColIndex);

			tableScrollPane = new JScrollPane();
			tableScrollPane.setViewportView(table);
			tableScrollPane.setMinimumSize(new Dimension(600, 200));

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(0, 1, 1, 1);
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1;
			gbc.weighty = 1;
			
			// Create container Jpanel for the table
			tablePanel = new JPanel();
			tablePanel.setLayout(new GridBagLayout());
			tablePanel.add(tableScrollPane, gbc);
			tablePanel.setBorder(new EmptyBorder(3, 5, 5, 3));
		}
		return tablePanel;
	}
	
	/**
	 * This method initializes controlPanel
	 * 
	 * @return javax.swing.ControlPanel
	 */
	public ControlPanel getControlPanel() {
		if (controlPanel == null) {
			// Setup the bottom Tag Info Panel
			controlPanel = new ControlPanel(this);
			controlPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		}
		return controlPanel;
	}

	/** Build the GUI Components for the Main Class */
	public TagEditorFrame() {
		//Create Main Frame
		super(LangageManager.getProperty("tageditorframe.title"));
		initialize();
		restoreFrameLocation();
		restoreFrameSize();
		restoreWorkingDir();
		registerActions();
	}

	//Try to set as good as possible, to best root disk (especially under
	// windows)
	private void buildRootComboBox() {
		//Old (stored) selected root
		int selectedIndex = PreferencesManager.getInt("tageditor.tageditorframe.roots");

		//If it is the first launch and the root appears to be windows-like, we
		// try to get the index of the "c:\" drive
		if (selectedIndex == 0 && File.listRoots()[0].toString().matches("[a-zA-Z]{1}\\:\\\\"))
			for (int i = 0; i < File.listRoots().length; i++)
				if (File.listRoots()[i].toString().matches("[cC]{1}\\:\\\\"))
					selectedIndex = i;
		/*
		 * Think of a mapped network drive that has been deleted. In this case
		 * we could get a ArrayIndexOutOfBound exception
		 */
		if (selectedIndex >= File.listRoots().length) {
			selectedIndex = 1;
		}
		//We test if we can read this disk (not an empty cdrom..)
		if (File.listRoots()[selectedIndex].canRead()) {
			//Set the combobox, create the model with the given root, and sets
			// it to the left tree
			roots.setSelectedIndex(selectedIndex);
		}
		//We could not read the difk, so it is probably an empty tray, display
		// a message, and don't show a left tree
		else {
			String msg = LangageManager.getProperty(
			"tageditorframe.drivecouldnotberead").replaceAll("%1",
					File.listRoots()[selectedIndex].toString().substring(0, 1));

			JOptionPane.showMessageDialog(this, msg);
			roots.setSelectedIndex(selectedIndex);
			/*
			 * folderTree.setModel( null );
			 */
		}
		/*
		 * folderTree.setSelectionPath(folderTree.getPathForRow(0));
		 */
	}

	public void currentDirDeleted() {
		navigator.fireDirectoryChange(NavigatorListener.EVENT_RELOAD);
	}

	/**
	 * Returns the global settings of the current editor.
	 * @return Settings.
	 */
	public EditorSettings getEditorSettings () {
		return this.settings;
	}

	/**
	 * Returns the navigator of entagged.
	 * 
	 * @return Navigator.
	 */
	public Navigator getNavigator() {
		if (navigator == null) {
			navigator = new Navigator();
		}
		return this.navigator;
	}

	public TagEditorTableModel getTagEditorTableModel() {
		return this.tableModel;
	}

	public void refreshCurrentTableView() {
		getControlPanel().clear();
		SelectionRecord record = new SelectionRecord(this.table, tableSorter, tableModel); 
		
		String str = search.getText();
		if(!str.equals(""))
			tableModel.directoryChanged(new File("c:\\"),DataProvider.getFichiers(str),NavigatorListener.EVENT_JUMPED);
		else
			navigator.reload();
		
		record.applyChange();
		getControlPanel().update();
	}

	/**
	 * This method will create and register the actions.
	 */
	private void registerActions() {
		String browseBack = "bbwk";
		String browseUp = "buk";
		String browseInto = "bik";
		String reload = "reload";
		String enter = "enter";
		String transfer ="transferfocus";

		this.table.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_KP_LEFT, 0), browseBack);
		this.table.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), browseBack);
		this.table.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_KP_RIGHT, 0), browseInto);
		this.table.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), browseInto);
		this.table.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), enter);
		this.table.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), browseBack);
		this.table.getInputMap()
		.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_UP,
						InputEvent.ALT_DOWN_MASK), browseUp);
		this.table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,0), transfer);
		/*
		 * Since the table is always visible, the f5 operation is assigned to
		 * tageditorframe when focused.
		 */
		this.table.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), reload);

		this.table.getActionMap().put(browseBack,
				new BrowseBackwardAction(navigator));
		this.table.getActionMap().put(browseUp, new BrowseUpAction(navigator));
		this.table.getActionMap().put(browseInto,
				new BrowseIntoAction(navigator, table, tableModel));
		this.table.getActionMap().put(reload, new ReloadAction(navigator));
		this.table.getActionMap().put(enter,
				new TableEnterAction(navigator, controlPanel));
		this.table.getActionMap().put(transfer, new FocusRequestAction(controlPanel));
		/*
		 * Automatic registering
		 */
		CtrlTableSelectionAction.registerCombinations(table);
	}

	private void restoreFrameLocation() {
		this.setLocation(PreferencesManager.getInt("tageditor.tageditorframe.framelocationx",
			(int) (PreferencesManager.getInt("entagged.screen.width") * 0.1)),
				PreferencesManager.getInt("tageditor.tageditorframe.framelocationy",
					(int) (PreferencesManager.getInt("entagged.screen.height") * 0.1)));
	}

	private void restoreFrameSize() {
		this.setSize(PreferencesManager.getInt("tageditor.tageditorframe.framewidth",
			(int) (PreferencesManager.getInt("entagged.screen.width") * 0.8)),
				PreferencesManager.getInt("tageditor.tageditorframe.frameheight",
					(int) (PreferencesManager.getInt("entagged.screen.height") * 0.8)));
	}

	private void restoreSplitPaneLocations() {
		tableInfoSplitPane.setDividerLocation(PreferencesManager.getInt(
				"tageditor.tageditorframe.tableinfosplitpane.dividerlocation",
				500));
		/*
		 * tableSplitPane.setDividerLocation(
		 * PreferencesManager.getInt("tageditor.tageditorframe.treetablesplitpane.dividerlocation",
		 * 300));
		 */
		//System.out.println(tableInfoSplitPane.getDividerLocation());
	}

	private void restoreTableColumnWidth() {
		Vector v = Utils.getColumnsInModelOrder(table);

		//Set the icon to fit the width
		((TableColumn) v.elementAt(0)).setWidth(22);
		((TableColumn) v.elementAt(0)).setPreferredWidth(22);
		((TableColumn) v.elementAt(0)).setMaxWidth(22);

		for (int i = 1; i < v.size(); i++) {
			int width = PreferencesManager
			.getInt("tageditor.tageditorframe.treetable.column." + i
					+ ".width");
			((TableColumn) v.elementAt(i)).setPreferredWidth(width);
			//System.out.println( width );
		}
	}

	private void restoreWorkingDir() {
		this.navigator.fireDirectoryChange(NavigatorListener.EVENT_RELOAD);
	}

	protected void saveGUIPreferences() {
		//-------TABLE PREFERENCES---------
		System.out.print("Exiting: Saving table columns widths ");
		Vector v = Utils.getColumnsInModelOrder(table);
		for (int i = 1; i < v.size(); i++) {
			int width = ((TableColumn) v.elementAt(i)).getPreferredWidth();
			System.out.print("|" + width);
			PreferencesManager
			.putInt("tageditor.tageditorframe.treetable.column." + i
					+ ".width", width);
		}
		System.out.println("| ...");
		//-------------------------------------

		//--------Frame Size-------------------
		System.out.println("Exiting: Saving frame size (" + this.getWidth()
				+ "," + this.getHeight() + ") ...");
		PreferencesManager.putInt("tageditor.tageditorframe.framewidth", this
				.getWidth());
		PreferencesManager.putInt("tageditor.tageditorframe.frameheight", this
				.getHeight());
		//-------------------------------------

		//--------Frame Location-------------------
		System.out.println("Exiting: Saving frame location (" + this.getX()
				+ "," + this.getY() + ") ...");
		PreferencesManager.putInt("tageditor.tageditorframe.framelocationx",
				this.getX());
		PreferencesManager.putInt("tageditor.tageditorframe.framelocationy",
				this.getY());
		//-------------------------------------

		//--------Split Panes divider loc.---------
		//System.out.println("Exiting: Saving split panes locations
		// ("+tableInfoSplitPane.getDividerLocation()+" |
		// "+tableSplitPane.getDividerLocation()+") ...");
		System.out.println("Exiting: Saving split panes locations ("
				+ tableInfoSplitPane.getDividerLocation() + ") ...");
		PreferencesManager.putInt(
				"tageditor.tageditorframe.tableinfosplitpane.dividerlocation",
				tableInfoSplitPane.getDividerLocation());
		/*
		 * PreferencesManager.putInt("tageditor.tageditorframe.treetablesplitpane.dividerlocation",
		 * tableSplitPane.getDividerLocation());
		 */
		//-----------------------------------------
		//--------Control Panel prefs -----------
		this.controlPanel.saveGUIPreferences();
		//-----------------------------------------

		//--------Current Working directory--------
		System.out.println("Exiting: Saving current working directory ("
				+ navigator.getCurrentFolder() + ") ...");
		navigator.storeSettings();
	}

	public void openDouble() {
		ArrayList<ArrayList<Atome>> metaListe = DataProvider.getDoublesInit("fichier");
		new JDialog_Doublons(this,metaListe);
		DataProvider.resetCache();
	}
	
	public void openWrongInput() {
		ArrayList<ArrayList<Atome>> metaListe = DataProvider.getDoublesInit("artiste");
		new JDialog_MauvaisesFrappes(this,metaListe, tableModel);
		DataProvider.resetCache();
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"