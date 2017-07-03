package planocontas;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;

public class TelaPlanoContas extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private JTree tree;
	private JLabel lblChaveDoRoot;
	private JTextField txtChave;
	private JButton btnCriarrvore;
	private JScrollPane scrollPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaPlanoContas frame = new TelaPlanoContas();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public TelaPlanoContas() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel, BorderLayout.NORTH);
		
		JButton btnConta = new JButton("Conta");
		btnConta.addActionListener(e -> addConta());
		panel.add(btnConta);
		
		lblChaveDoRoot = new JLabel("Chave do root da Árvore");
		panel.add(lblChaveDoRoot);
		
		txtChave = new JTextField();
		panel.add(txtChave);
		txtChave.setColumns(10);
		
		btnCriarrvore = new JButton("Criar Árvore");
		btnCriarrvore.addActionListener(e -> criarArvore());
		panel.add(btnCriarrvore);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		scrollPane = new JScrollPane();
		tabbedPane.addTab("Plano de Contas", null, scrollPane, null);
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
		tree = new JTree(root);
		scrollPane.setViewportView(tree);
	}

	private void criarArvore() {
		ContaDao dao = new ContaDao();
		
		try {
			String pai = txtChave.getText();
			if (!pai.isEmpty()) {
				List<Contas> lista = dao.getAllTree(1L);
				Map<Long, DefaultMutableTreeNode> map = new HashMap<>();
				
				DefaultMutableTreeNode root = new DefaultMutableTreeNode(lista.get(0).getConta());
				map.put(lista.get(0).getId(), root);
				lista.remove(0);
				
				for (Contas contas : lista) {
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(contas.getConta());
					map.put(contas.getId(), node);
					
					map.get(contas.getPai().getId()).add(node);
				}
				
				scrollPane.remove(tree);
				tree = new JTree(root);
				scrollPane.setViewportView(tree);
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "O pai precisa ser uma chave (número).");
		}
		
	}

	private void addConta() {
		tabbedPane.addTab("Conta", new ContaCadastro());
	}

}
