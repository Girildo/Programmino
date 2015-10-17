package com.girildo.programminoAPI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.girildo.programminoAPI.Messaggio.FlagMessaggio;

import net.miginfocom.swing.MigLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;

public class MainWindow
{

	private JFrame frmProgramminoSoniagallery;
	private JTextField textFieldLink;
	private JLabel lblLink;
	private LogicaProgramma logica;
	private JPopupMenu popupMenu;
	private JMenuItem mntmIncolla;
	private JPanel panel_1;
	private Box verticalBox;
	private Box verticalBox_1;
	private Box verticalBox_2;
	private JScrollPane scrollPane;
	private JTextArea textAreaFoto;
	private Box verticalBox_3;
	private JScrollPane scrollPane_2;
	private JTextArea textAreaErrori;
	private Box horizontalBox;
	private JLabel lblNewLabel;
	private JSlider slider;
	private JButton btnGeneraClassifica;
	private JTextArea textAreaClassifica;
	private JScrollPane scrollPaneClassifica;
	private JPopupMenu popupMenu_1;
	private JMenuItem menuItemCopia;
	private JPopupMenu popupMenu_2;
	private JMenuItem mntmCopiaPerErrori;
	/**
	 * Launch the application.
	 */
	
	private static String VERSIONE = "2.0.2 (17.10.2015)";
	private JPopupMenu popupMenu_3;
	private JMenuItem menuItemVersione;
	private JMenuBar menuBar;
	private JMenu mnImpostazioni;
	private JRadioButtonMenuItem rdbtnmntmSoniaGallery;
	private JRadioButtonMenuItem rdbtnmntmCampionato;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try {
					MainWindow window = new MainWindow();
					window.frmProgramminoSoniagallery.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frmProgramminoSoniagallery = new JFrame();
		frmProgramminoSoniagallery.setTitle("Programmino SoniaGallery");
		frmProgramminoSoniagallery.setMinimumSize(new Dimension(800, 500));
		//BorderLayout borderLayout = (BorderLayout) frame.getContentPane().getLayout();
		frmProgramminoSoniagallery.setBounds(100, 100, 806, 522);
		frmProgramminoSoniagallery.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(6, 6, 6, 6));
		frmProgramminoSoniagallery.getContentPane().add(panel, BorderLayout.NORTH);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {0, 689, 117, 117, 0};
		gbl_panel.rowHeights = new int[] {29};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0};
		panel.setLayout(gbl_panel);

		lblLink = new JLabel("Link:");
		lblLink.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblLink = new GridBagConstraints();
		gbc_lblLink.insets = new Insets(0, 0, 0, 5);
		gbc_lblLink.anchor = GridBagConstraints.EAST;
		gbc_lblLink.gridx = 0;
		gbc_lblLink.gridy = 0;
		panel.add(lblLink, gbc_lblLink);

		textFieldLink = new JTextField();
		
		popupMenu = new JPopupMenu();
		addPopup(textFieldLink, popupMenu);
		
		mntmIncolla = new JMenuItem("Incolla");
		mntmIncolla.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				incolla();
			}
		});
		popupMenu.add(mntmIncolla);
		GridBagConstraints gbc_textFieldLink = new GridBagConstraints();
		gbc_textFieldLink.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldLink.anchor = GridBagConstraints.NORTH;
		gbc_textFieldLink.insets = new Insets(0, 0, 0, 5);
		gbc_textFieldLink.gridx = 1;
		gbc_textFieldLink.gridy = 0;
		panel.add(textFieldLink, gbc_textFieldLink);

		JButton btnOttieniCommenti = new JButton("Ottieni commenti");
		btnOttieniCommenti.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				ottieniCommentiOnClick(textFieldLink.getText());
			}
		});
		btnOttieniCommenti.setHorizontalAlignment(SwingConstants.LEADING);
		GridBagConstraints gbc_btnOttieniCommenti = new GridBagConstraints();
		gbc_btnOttieniCommenti.insets = new Insets(0, 0, 0, 5);
		gbc_btnOttieniCommenti.anchor = GridBagConstraints.NORTHEAST;
		gbc_btnOttieniCommenti.gridx = 2;
		gbc_btnOttieniCommenti.gridy = 0;
		panel.add(btnOttieniCommenti, gbc_btnOttieniCommenti);
		
		btnGeneraClassifica = new JButton("Genera classifica");
		
		btnGeneraClassifica.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				generaClassificaOnClick();
			}
		});
		btnGeneraClassifica.setEnabled(false);
		GridBagConstraints gbc_btnGeneraClassifica = new GridBagConstraints();
		gbc_btnGeneraClassifica.gridx = 3;
		gbc_btnGeneraClassifica.gridy = 0;
		panel.add(btnGeneraClassifica, gbc_btnGeneraClassifica);
		
		panel_1 = new JPanel();
		frmProgramminoSoniagallery.getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new MigLayout("", "[grow,fill][grow,fill][grow,fill]", "[grow,fill]"));
		
		verticalBox = Box.createVerticalBox();
		verticalBox.setBorder(new TitledBorder(null, "Foto trovate", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.add(verticalBox, "cell 0 0,width 33%,grow");
		
		scrollPane = new JScrollPane();
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		verticalBox.add(scrollPane);
		
		textAreaFoto = new JTextArea();
		textAreaFoto.setEditable(false);
		scrollPane.setViewportView(textAreaFoto);
		
		verticalBox_1 = Box.createVerticalBox();
		verticalBox_1.setBorder(new TitledBorder(null, "Classifica", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.add(verticalBox_1, "cell 1 0,width 33%,grow");
		
		scrollPaneClassifica = new JScrollPane();
		scrollPaneClassifica.setAlignmentX(Component.LEFT_ALIGNMENT);
		verticalBox_1.add(scrollPaneClassifica);
		
		textAreaClassifica = new JTextArea();
		
		textAreaClassifica.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) 
			{
//				textAreaClassifica.setSelectionStart(0);
//				textAreaClassifica.setSelectionEnd(textAreaClassifica.getText().length());
				textAreaClassifica.selectAll();
			}
			
		});
		
		
		textAreaClassifica.setEditable(false);
		scrollPaneClassifica.setViewportView(textAreaClassifica);
		
		popupMenu_1 = new JPopupMenu();
		addPopup(textAreaClassifica, popupMenu_1);
		
		menuItemCopia = new JMenuItem("Copia");
		menuItemCopia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				copia(textAreaClassifica.getSelectedText());
			}
		});
		menuItemCopia.setEnabled(false);
		popupMenu_1.add(menuItemCopia);
		
		verticalBox_2 = Box.createVerticalBox();
		verticalBox_2.setBorder(null);
		panel_1.add(verticalBox_2, "cell 2 0,width 33%,grow");
		
		horizontalBox = Box.createHorizontalBox();
		horizontalBox.setBorder(new TitledBorder(null, "Impostazioni", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		verticalBox_2.add(horizontalBox);
		
		lblNewLabel = new JLabel("# Voti");
		horizontalBox.add(lblNewLabel);
		
		slider = new JSlider();
		slider.setMinimum(1);
		slider.setSnapToTicks(true);
		slider.setPaintTicks(true);
		slider.setMaximum(10);
		slider.setValue(3);
		slider.setPaintLabels(true);
		slider.setMajorTickSpacing(1);
		horizontalBox.add(slider);
		
		verticalBox_3 = Box.createVerticalBox();
		verticalBox_3.setBorder(new TitledBorder(null, "Errori", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		verticalBox_2.add(verticalBox_3);
		
		scrollPane_2 = new JScrollPane();
		verticalBox_3.add(scrollPane_2);
		
		textAreaErrori = new JTextArea();
		textAreaErrori.setForeground(new Color(204, 0, 0));
		textAreaErrori.setWrapStyleWord(true);
		textAreaErrori.setLineWrap(true);
		textAreaErrori.setEditable(false);
		scrollPane_2.setViewportView(textAreaErrori);
		
		popupMenu_2 = new JPopupMenu();
		addPopup(textAreaErrori, popupMenu_2);
		
		mntmCopiaPerErrori = new JMenuItem("Copia");
		mntmCopiaPerErrori.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				copia(textAreaErrori.getText());
			}
		});
		popupMenu_2.add(mntmCopiaPerErrori);
		
		menuBar = new JMenuBar();
		frmProgramminoSoniagallery.setJMenuBar(menuBar);
		
		mnImpostazioni = new JMenu("Impostazioni");
		menuBar.add(mnImpostazioni);
		
		rdbtnmntmSoniaGallery = new JRadioButtonMenuItem("Sonia Gallery");
		rdbtnmntmSoniaGallery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				cambiaTipoClassifica();
			}
		});
		buttonGroup.add(rdbtnmntmSoniaGallery);
		mnImpostazioni.add(rdbtnmntmSoniaGallery);
		
		rdbtnmntmCampionato = new JRadioButtonMenuItem("Campionato");
		rdbtnmntmCampionato.setSelected(true);
		rdbtnmntmCampionato.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				cambiaTipoClassifica();
			}
		});
		buttonGroup.add(rdbtnmntmCampionato);
		mnImpostazioni.add(rdbtnmntmCampionato);
		
		popupMenu_3 = new JPopupMenu();
		addPopup(frmProgramminoSoniagallery, popupMenu_3);
		
		menuItemVersione = new JMenuItem("New menu item");
		menuItemVersione.setEnabled(false);
		menuItemVersione.setText("Versione: "+VERSIONE);
		popupMenu_3.add(menuItemVersione);
		cambiaTipoClassifica();
	}

	protected void cambiaTipoClassifica() 
	{
		//slider.setEnabled(rdbtnmntmSoniaGallery.isSelected());
		slider.setEnabled(true);
		this.reset();
	}

	protected void generaClassificaOnClick()
	{
		Messaggio mess = logica.GeneraClassifica(this.slider.getValue());
		if(mess.getFlag() == FlagMessaggio.NESSUN_ERRORE)
		{
			textAreaClassifica.setText(mess.getTestoNessunErrore());
			scrollPaneClassifica.scrollRectToVisible(new Rectangle());
		}
		else if(mess.getFlag() == FlagMessaggio.ERRORE_PARZIALE)
		{
			textAreaClassifica.setText(mess.getTestoNessunErrore());
			textAreaErrori.setText(mess.getTestoErroreParziale());
			scrollPaneClassifica.scrollRectToVisible(new Rectangle());
		}
		else
		{
			textAreaErrori.setText(mess.getTestoNessunErrore());
			textAreaClassifica.setText("");
		}
		this.menuItemCopia.setEnabled(this.textAreaClassifica.getText().length() != 0);
	}

	protected void ottieniCommentiOnClick(final String link)
	{
		logica = null;
		if(rdbtnmntmSoniaGallery.isSelected())
			logica = new LogicaProgrammaSG();
		else if(rdbtnmntmCampionato.isSelected())
			logica = new LogicaProgrammaCM();
		else
			return;
		this.reset();
		
		final DialogWait dial = new DialogWait(this.frmProgramminoSoniagallery);
		dial.setLocationRelativeTo(this.frmProgramminoSoniagallery);
		dial.setVisible(true);
		
		SwingWorker<Void, Void> task = new SwingWorker<Void,Void>()
		{
			@Override
			protected Void doInBackground() throws Exception
			{
				Messaggio mess = logica.OttieniCommentiPulitiDaUrl(link);
				if(mess.getFlag() == FlagMessaggio.NESSUN_ERRORE)
				{
					textAreaFoto.setText(mess.getTestoNessunErrore());
					btnGeneraClassifica.setEnabled(true);
				}
				else if(mess.getFlag() == FlagMessaggio.ERRORE)
					textAreaErrori.setText(mess.getTestoNessunErrore());
				return null;
			}
			@Override
			protected void done()
			{
				dial.setVisible(false);
				dial.dispose();
			}

		};
		task.execute();
	}

	private void reset()
	{
		this.textAreaClassifica.setText("");
		this.textAreaErrori.setText("");
		this.textAreaFoto.setText("");
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
	
	private void incolla()
	{
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		try 
		{
			textFieldLink.setText((String)clipboard.getContents(null).getTransferData(DataFlavor.stringFlavor));
		} 
		catch (UnsupportedFlavorException e1) 
		{
			textFieldLink.setText("Non riesco ad incollare il testo dagli appunti");
		} 
		catch (IOException e1) 
		{
			textFieldLink.setText("Non riesco ad incollare il testo dagli appunti");
		}
	}
	protected void copia(String selectedText)
	{
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection s = new StringSelection(selectedText);
		clipboard.setContents(s, null);
		textAreaClassifica.setSelectionStart(0);
		textAreaClassifica.setSelectionEnd(0);
	}

}