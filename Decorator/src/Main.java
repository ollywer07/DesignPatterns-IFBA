
import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JTextArea;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.ScrollPane;
import java.awt.Scrollbar;

import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.JSeparator;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.ListModel;
import javax.swing.JTable;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.event.ListDataListener;

import interfaces.PizzaComponent;
import interfaces.PizzaDecorator;


import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.AbstractButton;
import javax.swing.AbstractListModel;
import java.awt.Font;

public class Main {
	private JFrame frame;
	private static DefaultListModel listModel = new DefaultListModel<>();

	public static void main(String[] args) throws Exception {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	public static List getPluginsToListSabores() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		List listaDePlugins = new ArrayList<>();
		File currentDir = new File("./plugins");
	     String []plugins = currentDir.list();
	      
	      for (int i = 0; i < plugins.length; i++){
	    	  listaDePlugins.add(plugins[i].split("\\.")[0]);
	      }
	      return listaDePlugins;
	}
	
	public static  URL[] getListURL() throws MalformedURLException {

		File currentDir = new File("./plugins");
	     String []plugins = currentDir.list();
	      URL[] jars = new URL[plugins.length];
	      
	      for (int i = 0; i < plugins.length; i++){
	    	  jars[i] = (new File("./plugins/" + plugins[i])).toURL(); 
	      }
		return jars;
	}
	
	public static List getListEscolhidos() {
		List<String> lista = new ArrayList<String>();
		if(!listModel.isEmpty()) {
			for(int i = 0; i< listModel.size(); i++) {
				lista.add((String) listModel.get(i).toString());
			}
			return lista;
		}
		return null;
	}	
	
	public static List<PizzaDecorator> getInstances() throws Exception {
		List<PizzaDecorator> listDecorator = new ArrayList<PizzaDecorator>();
		List lista = getListEscolhidos();
		URL[] jars = getListURL();
		URLClassLoader ulc = new URLClassLoader(jars);
		 for(int i=0;i<lista.size();i++) {
	    	  String factoryName = ((String) lista.get(i)).split("\\.")[0];
	    		PizzaDecorator factory = (PizzaDecorator) Class.forName(factoryName, true, ulc).newInstance();
	    		listDecorator.add(factory); 
	      }
		 return listDecorator;
	}
	/* Método preparar incompleto */
	public static void preparar() throws Exception {
		PizzaSimples pizza = new PizzaSimples();
		List<PizzaDecorator> listDecorator = getInstances();
	    
		if(!getListEscolhidos().isEmpty()) {
			int i = 0;
		
			for(i=0;i<listDecorator.size()-1;i++) {
				listDecorator.get(i).setDecorated(listDecorator.get(i+1));
			}
			listDecorator.get(listDecorator.size()-1).setDecorated(pizza);
			listDecorator.get(0).preparar();
		}
	}

	public Main() throws Exception {
		initialize();
	}

	private void initialize() throws Exception {
		frame = new JFrame();
		frame.setBounds(100, 100, 580, 402);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel labelDecorator = new JLabel("DECORATOR");
		labelDecorator.setFont(new Font("Tahoma", Font.PLAIN, 18));
		labelDecorator.setHorizontalAlignment(SwingConstants.CENTER);
		labelDecorator.setBounds(162, 24, 194, 14);
		frame.getContentPane().add(labelDecorator);
		
		DefaultListModel listModel = new DefaultListModel();
		listModel.addAll(getPluginsToListSabores());
		
		JList list = new JList(listModel);
		
		list.setBounds(102, 100, 135, 143);
		frame.getContentPane().add(list);
		JScrollPane lst2ContScrollPane = new JScrollPane(list);
		lst2ContScrollPane.setBounds(102, 100, 135, 143);
		frame.getContentPane().add(lst2ContScrollPane);
		
		list.setSelectedIndex(0);
		
		JList list_1 = new JList();
		list_1.setBounds(303, 100, 135, 143);
		frame.getContentPane().add(list_1);
		ScrollPane sc = new ScrollPane();
		JScrollPane lstContScrollPane = new JScrollPane(list_1);
					lstContScrollPane.setBounds(303, 100, 135, 143);
					frame.getContentPane().add(lstContScrollPane);
	
		DefaultListModel lista = new DefaultListModel();
		
		JButton buttonInserir = new JButton(">");
		buttonInserir.setBounds(247, 138, 46, 23);
		frame.getContentPane().add(buttonInserir);

		buttonInserir.addActionListener(

				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					lista.addElement(list.getSelectedValue() );
					list_1.setModel(lista);
					Main.listModel = lista;
					list_1.setSelectedIndex(lista.getSize()-1);
				}
		});
		JButton buttonRetirar = new JButton("<");
		buttonRetirar.setBounds(247, 172, 46, 23);
		frame.getContentPane().add(buttonRetirar);
		
		buttonRetirar.addActionListener(			
				new ActionListener() {
					int index = list_1.getSelectedIndex();
				public void actionPerformed(ActionEvent e) {
					if (lista.getSize() == 0) {
						JOptionPane.getDesktopPaneForComponent(buttonRetirar);
						JOptionPane.showMessageDialog(frame, "Vazio");
					}
					if (! list_1.isSelectionEmpty()) {
						list_1.setSelectedIndex(lista.getSize());
					}
					if(list_1.isSelectionEmpty() && lista.getSize() > 0) {
						lista.remove(list_1.getSelectedIndex());
						list_1.setSelectedIndex(lista.getSize()-1);
					}
					if(lista.getSize() > 0 ) {
						lista.remove(list_1.getSelectedIndex());
						list_1.setSelectedIndex(lista.getSize()-1);
					}
				}
		});

		JLabel labelSabores = new JLabel("SABORES DA PIZZA");
		labelSabores.setHorizontalAlignment(SwingConstants.CENTER);
		labelSabores.setBounds(71, 75, 184, 14);
		frame.getContentPane().add(labelSabores);
		
		JButton buttonUp = new JButton("Up");
		buttonUp.setBounds(465, 116, 76, 23);
		frame.getContentPane().add(buttonUp);
		buttonUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String itemAnterior= "";
				String selected = "";
				int index;
				if(list_1.getSelectedIndex() > 0) {
					//System.out.println(list_1.getSelectedIndex());
					itemAnterior = (String) lista.getElementAt(list_1.getSelectedIndex() -1);
					 selected = (String) lista.getElementAt(list_1.getSelectedIndex());
					 index = list_1.getSelectedIndex();
					 lista.setElementAt(list_1.getSelectedValue(), list_1.getSelectedIndex() -1);
					 lista.setElementAt(itemAnterior, list_1.getSelectedIndex());
					 list_1.setSelectedIndex(list_1.getSelectedIndex() -1);
					}
			}
		});
		JButton buttonDown = new JButton("Down");
		buttonDown.setBounds(465, 150, 76, 23);
		frame.getContentPane().add(buttonDown);
		buttonDown.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e) {
				String itemPosterior= "";
				String selected = "";
				int index;
				
				if(list_1.getSelectedIndex() < list_1.getLastVisibleIndex()) {
					itemPosterior = (String) lista.getElementAt(list_1.getSelectedIndex() +1);
					 selected = (String) lista.getElementAt(list_1.getSelectedIndex());
					 index = list_1.getSelectedIndex();
					 lista.setElementAt(list_1.getSelectedValue(), list_1.getSelectedIndex() +1);
					 lista.setElementAt(itemPosterior, list_1.getSelectedIndex());
					 list_1.setSelectedIndex(list_1.getSelectedIndex() +1);
					}
			}
		});
		
		JButton buttonPreparar = new JButton("Preparar");
		buttonPreparar.setBounds(224, 284, 89, 23);
		frame.getContentPane().add(buttonPreparar);
		
		JLabel labelEscolhidos = new JLabel("ESCOLHIDOS");
		labelEscolhidos.setHorizontalAlignment(SwingConstants.CENTER);
		labelEscolhidos.setBounds(274, 75, 184, 14);
		frame.getContentPane().add(labelEscolhidos);
		
		buttonPreparar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(Main.listModel.isEmpty()) {
						return;
					}
					preparar();
				} catch (Exception e1) {
					e1.printStackTrace();
				}		
			}
		});
		frame.setResizable(false);
	}
}
