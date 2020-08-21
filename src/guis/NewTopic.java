package guis;

import java.awt.EventQueue;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import components.TopicTableModel;
import controller.MySQLCategoryDAO;
import entities.Category;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;

public class NewTopic extends JFrame implements ActionListener, ItemListener {

	TopicTableModel tblModel;
	MySQLCategoryDAO msCatDAO;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm a");
	
	private JPanel contentPane;
	private JTextField txtTopicName;
	private JTextField txtSearchCategory;
	private JTable tblCategories;
	private JTable tblExercises;
	private JButton btnAdd;
	private JComboBox<String> cboCategoryGroup;
	private JComboBox<String> cboExerciseGroup;
	private JButton btnNewCategory;
	private JButton btnNewExercise;
	private JTextField txtSearchExercise;
	private JScrollPane spSelectedCategories;
	private JTextArea txtSelectedCategories;
	private JScrollPane spSelectedExercises;
	private JTextArea txtSelectedExercises;
	private int categoryGroupID;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewTopic frame = new NewTopic();
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
	public NewTopic() {
		setTitle("New topic");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 620, 696);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		/*** Topic name text box ***/
		txtTopicName = new JTextField();
		txtTopicName.setBounds(84, 30, 96, 20);
		contentPane.add(txtTopicName);
		
		/*** Search category text box ***/
		txtSearchCategory = new JTextField();
		txtSearchCategory.setBounds(483, 83, 96, 20);
		contentPane.add(txtSearchCategory);
		
		/*** Category table ***/
		JScrollPane spCategories = new JScrollPane();
		spCategories.setBounds(28, 119, 552, 115);
		contentPane.add(spCategories);
		
			tblModel = new TopicTableModel();
			
			// column headings
			tblModel.addColumn("");//0 // ID // hidden column
			tblModel.addColumn("Selection");//1
			tblModel.addColumn("Name");//2
			tblModel.addColumn("Creation date");//3
			tblModel.addColumn("Favorite");//4
			tblModel.addColumn("");//5 // offline or online // hidden column
			tblModel.addColumn("");//6
			tblModel.addColumn("");//7
		
			tblCategories = new JTable();
			
			tblCategories.setModel(tblModel);
			
			tblCategories.getColumnModel().getColumn(0).setMinWidth(0);
			tblCategories.getColumnModel().getColumn(0).setMaxWidth(0);
			tblCategories.getColumnModel().getColumn(0).setWidth(0);
			tblCategories.getColumnModel().getColumn(5).setMinWidth(0);
			tblCategories.getColumnModel().getColumn(5).setMaxWidth(0);
			tblCategories.getColumnModel().getColumn(5).setWidth(0);
			
			showCategories();
			
			filterCategories("true", 4);
			
			spCategories.setViewportView(tblCategories);
		
		/*** Exercise table ***/
		JScrollPane spExercises = new JScrollPane();
		spExercises.setBounds(27, 405, 552, 100);
		contentPane.add(spExercises);
		
			tblExercises = new JTable();
			spExercises.setViewportView(tblExercises);
			
			//filter
		
		/*** Add topic button ***/
		btnAdd = new JButton("Add");
		btnAdd.addActionListener(this);
		btnAdd.setBounds(490, 30, 89, 23);
		contentPane.add(btnAdd);
		
		/*** Groups of categories in combo box ***/
		
		cboCategoryGroup = new JComboBox<String>();
		cboCategoryGroup.setModel(new DefaultComboBoxModel<String>(new String[] {"Favorites", "My categories", "Online categories"}));
		cboCategoryGroup.addItemListener(this);
		cboCategoryGroup.setBounds(84, 82, 96, 22);
		contentPane.add(cboCategoryGroup);
		
		cboExerciseGroup = new JComboBox<String>();
		cboExerciseGroup.addItemListener(this);
		cboExerciseGroup.setModel(new DefaultComboBoxModel<String>(new String[] {"Favorites", "My exercises", "Online exercises"}));
		cboExerciseGroup.setBounds(84, 361, 96, 22);
		contentPane.add(cboExerciseGroup);
		
		/*** New category button ***/
		btnNewCategory = new JButton("New category");
		btnNewCategory.addActionListener(this);
		btnNewCategory.setBounds(425, 233, 154, 23);
		contentPane.add(btnNewCategory);
		
		/*** New exercise button ***/
		btnNewExercise = new JButton("New exercise");
		btnNewExercise.addActionListener(this);
		btnNewExercise.setBounds(425, 503, 154, 23);
		contentPane.add(btnNewExercise);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(28, 33, 48, 14);
		contentPane.add(lblName);
		
		JLabel lblCategory = new JLabel("Category");
		lblCategory.setBounds(28, 85, 89, 14);
		contentPane.add(lblCategory);
		
		JLabel lblSearch = new JLabel("Search");
		lblSearch.setBounds(425, 86, 48, 14);
		contentPane.add(lblSearch);
		
		lblSearch = new JLabel("Search");
		lblSearch.setBounds(425, 365, 48, 14);
		contentPane.add(lblSearch);
		
		JLabel lblExercises = new JLabel("Exercises");
		lblExercises.setBounds(28, 365, 48, 14);
		contentPane.add(lblExercises);
		
		/*** Search exercise text box ***/
		txtSearchExercise = new JTextField();
		txtSearchExercise.setBounds(483, 362, 96, 20);
		contentPane.add(txtSearchExercise);
		txtSearchExercise.setColumns(10);
		
		/*** Selected categories text box ***/
		spSelectedCategories = new JScrollPane();
		spSelectedCategories.setBounds(28, 267, 550, 77);
		contentPane.add(spSelectedCategories);
		
			txtSelectedCategories = new JTextArea();
			txtSelectedCategories.setWrapStyleWord(true);
			txtSelectedCategories.setLineWrap(true);
			txtSelectedCategories.setTabSize(2);
			spSelectedCategories.setViewportView(txtSelectedCategories);
			txtSelectedCategories.setEditable(false);
		
		/*** Selected exercises text box ***/
		spSelectedExercises = new JScrollPane();
		spSelectedExercises.setBounds(28, 544, 550, 77);
		contentPane.add(spSelectedExercises);
		
			txtSelectedExercises = new JTextArea();
			spSelectedExercises.setViewportView(txtSelectedExercises);
			txtSelectedExercises.setEditable(false);
	}
	
	private void filterCategories(String input, int columnIndex) {
		TableRowSorter<DefaultTableModel> trs = new TableRowSorter<DefaultTableModel>(tblModel);
		tblCategories.setRowSorter(trs);
		
		trs.setRowFilter(RowFilter.regexFilter(input, columnIndex));
	}
	
	
	private void showCategories() {
		// clear table content
		tblModel.setRowCount(0);
		
		msCatDAO = new MySQLCategoryDAO();
		
		for(Category bean:msCatDAO.listAllCategories()) {
			Object row[] = {
					bean.getCategoryID(),
					bean.isCategorySelected(),
					bean.getCategoryName(),
					sdf.format(bean.getCategoryDatetime()),
					bean.isCategoryFavorite(),
					"offline"
			};
			tblModel.addRow(row);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnNewExercise) {
			actionPerformedBtnNewExercise(e);
		}
		if (e.getSource() == btnNewCategory) {
			actionPerformedBtnNewCategory(e);
		}
		if (e.getSource() == btnAdd) {
			actionPerformedBtnAdd(e);
		}
	}
	protected void actionPerformedBtnAdd(ActionEvent e) {
	}

	protected void actionPerformedBtnNewCategory(ActionEvent e) {
//		Create gui = new Create();
		
	}

	protected void actionPerformedBtnNewExercise(ActionEvent e) {
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == cboExerciseGroup) {
			itemStateChangedCboExerciseGroup(e);
		}
		if (e.getSource() == cboCategoryGroup) {
			itemStateChangedCboCategoryGroup(e);
		}
	}
	
	protected void itemStateChangedCboCategoryGroup(ItemEvent e) {
		categoryGroupID = cboCategoryGroup.getSelectedIndex();
		if(categoryGroupID == 1)
			filterCategories("offline", 5);// column index == 5 is hidden
		else if(categoryGroupID == 2)
			filterCategories("online", 5);// column index == 5 is hidden
		else
			filterCategories("true", 4);// favorite column
	}

	protected void itemStateChangedCboExerciseGroup(ItemEvent e) {
	}
}
