package planocontas;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class ContaCadastro extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField txtCodigo;
	private JTextField txtConta;
	private JLabel lblContaPai;
	private JTextField txtPai;
	private Box horizontalBox;
	private JButton btnCadastrar;
	private JButton btnDeletar;
	private JPanel panel;
	private JScrollPane scrollPane;
	private JTable table;
	private ContaModel model;
	private Contas selecionado;
	private JButton btnLimpar;
	private JTextField txtValor;
	private JLabel lblValor;

	public ContaCadastro() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblCdigo = new JLabel("Código");
		GridBagConstraints gbc_lblCdigo = new GridBagConstraints();
		gbc_lblCdigo.insets = new Insets(0, 0, 5, 5);
		gbc_lblCdigo.gridx = 0;
		gbc_lblCdigo.gridy = 0;
		add(lblCdigo, gbc_lblCdigo);
		
		txtCodigo = new JTextField();
		txtCodigo.setName("");
		GridBagConstraints gbc_txtCodigo = new GridBagConstraints();
		gbc_txtCodigo.insets = new Insets(0, 0, 5, 0);
		gbc_txtCodigo.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCodigo.gridx = 1;
		gbc_txtCodigo.gridy = 0;
		add(txtCodigo, gbc_txtCodigo);
		txtCodigo.setColumns(10);
		
		JLabel lblConta = new JLabel("Conta");
		GridBagConstraints gbc_lblConta = new GridBagConstraints();
		gbc_lblConta.insets = new Insets(0, 0, 5, 5);
		gbc_lblConta.gridx = 0;
		gbc_lblConta.gridy = 1;
		add(lblConta, gbc_lblConta);
		
		txtConta = new JTextField();
		GridBagConstraints gbc_txtConta = new GridBagConstraints();
		gbc_txtConta.insets = new Insets(0, 0, 5, 0);
		gbc_txtConta.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtConta.gridx = 1;
		gbc_txtConta.gridy = 1;
		add(txtConta, gbc_txtConta);
		txtConta.setColumns(10);
		
		lblContaPai = new JLabel("Conta Pai");
		GridBagConstraints gbc_lblContaPai = new GridBagConstraints();
		gbc_lblContaPai.insets = new Insets(0, 0, 5, 5);
		gbc_lblContaPai.gridx = 0;
		gbc_lblContaPai.gridy = 2;
		add(lblContaPai, gbc_lblContaPai);
		
		txtPai = new JTextField();
		GridBagConstraints gbc_txtPai = new GridBagConstraints();
		gbc_txtPai.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPai.insets = new Insets(0, 0, 5, 0);
		gbc_txtPai.gridx = 1;
		gbc_txtPai.gridy = 2;
		add(txtPai, gbc_txtPai);
		txtPai.setColumns(10);
		
		lblValor = new JLabel("Valor");
		GridBagConstraints gbc_lblValor = new GridBagConstraints();
		gbc_lblValor.insets = new Insets(0, 0, 5, 5);
		gbc_lblValor.gridx = 0;
		gbc_lblValor.gridy = 3;
		add(lblValor, gbc_lblValor);
		
		txtValor = new JTextField();
		GridBagConstraints gbc_txtValor = new GridBagConstraints();
		gbc_txtValor.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtValor.insets = new Insets(0, 0, 5, 0);
		gbc_txtValor.gridx = 1;
		gbc_txtValor.gridy = 3;
		add(txtValor, gbc_txtValor);
		txtValor.setColumns(10);
		
		horizontalBox = Box.createHorizontalBox();
		GridBagConstraints gbc_horizontalBox = new GridBagConstraints();
		gbc_horizontalBox.insets = new Insets(0, 0, 5, 0);
		gbc_horizontalBox.anchor = GridBagConstraints.EAST;
		gbc_horizontalBox.gridx = 1;
		gbc_horizontalBox.gridy = 4;
		add(horizontalBox, gbc_horizontalBox);
		
		btnDeletar = new JButton("Deletar");
		btnDeletar.addActionListener(e -> deletarSelecionado());
		horizontalBox.add(btnDeletar);
		
		btnLimpar = new JButton("Limpar");
		btnLimpar.addActionListener(e -> limparFields());
		horizontalBox.add(btnLimpar);
		
		btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.addActionListener(e -> cadastrarConta());
		horizontalBox.add(btnCadastrar);
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 2;
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 5;
		add(panel, gbc_panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);
				
		table = new JTable();
		scrollPane.setViewportView(table);
		
		//$hide>>$
		ContaDao dao = new ContaDao();
		model = new ContaModel();
		model.preencherLista(dao.getAllContas());
		
		table.setModel(model);
		
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					selecionado = model.getOne(table.getSelectedRow());
					preencherFields();
				}
			}
		});
		//$hide<<$
		
	}
	
	//$hide>>$
	private void cadastrarConta() {
		Contas c = new Contas();
		ContaDao dao = new ContaDao();
		
		c.setCodigo(txtCodigo.getText());
		c.setConta(txtConta.getText());
		
		try {
			String pai = txtPai.getText();
			if (!pai.isEmpty()) {
				c.setPai(dao.getOne(Long.parseLong(pai)));				
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "O pai precisa ser uma chave (número).");
		}
		
		String valor = txtValor.getText();
		valor.replace(",", ".");
		
		try {
			c.setValor(new BigDecimal(valor));			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "O valor necessita ser um número.");
		}
		
		c.setTipoConta(TipoConta.ANALITICA);
		
		dao.cadastrarConta(c);
		
		limparFields();
		research();
	}

	protected void preencherFields() {
		txtCodigo.setText(selecionado.getCodigo());
		txtConta.setText(selecionado.getConta());
		
		if (selecionado.getPai() != null) {
			txtPai.setText(selecionado.getPai().getId().toString());
		}
		txtValor.setText(selecionado.getValor().toString());
	}

	private void deletarSelecionado() {
		if (selecionado != null) {
			ContaDao dao = new ContaDao();
			dao.deleteConta(selecionado.getId());
			research();
			limparFields();
		}
	}

	private void limparFields() {
		txtCodigo.setText("");
		txtConta.setText("");
		txtPai.setText("");
		txtValor.setText("");
	}

	private void research() {
		ContaDao dao = new ContaDao();
		model.preencherLista(dao.getAllContas());
		model.fireTableDataChanged();
	}
	//$hide<<$
}
