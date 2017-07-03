package planocontas;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class ContaModel extends AbstractTableModel {

	private static final long serialVersionUID = -1933215932545866793L;
	private List<Contas> contas;
	
	public void preencherLista(List<Contas> contas) {
		this.contas = contas;
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public int getRowCount() {
		return this.contas.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		
		Contas c =  this.contas.get(row);
		
		switch (column) {
		case 0:
			return c.getId();
		case 1:
			return c.getCodigo();
		case 2:
			return c.getConta();
		case 3:
			return c.getTipoConta().getNome();
		case 4:
			return c.getValor();
		}
		
		return null;
	}
	
	public Contas getOne(int row) {
		return this.contas.get(row);
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "Chave";
		case 1:
			return "Código";
		case 2:
			return "Conta";
		case 3:
			return "Tipo";
		case 4:
			return "Valor";
		}
		
		return null;
	}

}
