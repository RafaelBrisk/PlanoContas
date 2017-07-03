package planocontas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ContaDao {
	
	private final String SQL_PUT_ONE = "UPDATE contas SET codigo = ?, conta = ?, pai = ?, tipoconta = ?, valor = ? WHERE id = ?;";
	private final String SQL_POST_ONE = "INSERT INTO contas(codigo, conta, pai, tipoconta, valor)VALUES(?, ?, ?, ?, ?);";
	private final String SQL_DELETE_ONE = "DELETE FROM contas c WHERE c.id = ?;";
	private final String SQL_GET_ALL = "SELECT * FROM contas";
	private final String SQL_GET_ALLTREE = "SELECT * FROM getAllTree(?);";
	private final String SQL_GET_ONE = "SELECT * FROM contas c WHERE c.id = ?;";
	private final Connection con;
	
	public ContaDao() {
		con = ConexaoBanco.getInstace().getConnection();
	}
	
	public List<Contas> getAllTree(Long idRoot) {
		List<Contas> retorno = new ArrayList<>();
		retorno.add(getOne(idRoot));
		
		try {
			PreparedStatement ps = con.prepareStatement(SQL_GET_ALLTREE);
			ps.setLong(1, idRoot);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Contas c = new Contas();
				c = populaConta(c, rs);
				retorno.add(c);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return retorno;
	}
	
	public Contas getOne(Long id) {
		Contas c = new Contas();
		
		try {
			PreparedStatement ps = con.prepareStatement(SQL_GET_ONE);
			ps.setLong(1, id);
			
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			c = populaConta(c, rs);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return c;
	}
	
	public List<Contas> getAllContas() {
		List<Contas> contas = new ArrayList<>();
		
		try {
			PreparedStatement ps = con.prepareCall(SQL_GET_ALL);
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Contas c = new Contas();
				c = populaConta(c, rs);
				contas.add(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return contas;
	}
	
	public void deleteConta(Long id) {
		try {
			PreparedStatement ps = con.prepareStatement(SQL_DELETE_ONE);
			ps.setLong(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void cadastrarConta(Contas c) {
		try {
			PreparedStatement ps = con.prepareStatement(SQL_POST_ONE, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, c.getCodigo());
			ps.setString(2, c.getConta());
			
			
			if (c.getPai() != null) {
				ps.setLong(3, c.getPai().getId());				
			} else {
				ps.setObject(3, null);
			}
			
			ps.setString(4, c.getTipoConta().name().toUpperCase());
			ps.setBigDecimal(5, c.getValor());
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			
			refatorarContas(rs.getLong(4));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void atualizarConta(Contas c) {
		try {
			PreparedStatement ps = con.prepareStatement(SQL_PUT_ONE);
			ps.setString(1, c.getCodigo());
			ps.setString(2, c.getConta());
			
			if (c.getPai() != null) {
				ps.setLong(3, c.getPai().getId());				
			} else {
				ps.setObject(3, null);
			}
			
			ps.setString(4, c.getTipoConta().name());
			ps.setBigDecimal(5, c.getValor());
			ps.setLong(6, c.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void refatorarContas(Long idPai) {
		if (idPai != 0L) {
			Contas c = getOne(idPai);
			c.setTipoConta(TipoConta.SINTETICA);
			atualizarConta(c);
			
			if (c.getPai() != null) {
				refatorarContas(c.getPai().getId());				
			}
		}
	}

	private Contas populaConta(Contas c, ResultSet rs) {
		try {
			c.setId(rs.getLong(1));
			c.setCodigo(rs.getString(2));
			c.setConta(rs.getString(3));
			
			Long idPai = rs.getLong(4); 
			
			if (idPai != 0L) {
				c.setPai(getOne(idPai));
			}
			
			c.setTipoConta(TipoConta.valueOf(rs.getString(5)));
			c.setValor(rs.getBigDecimal(6));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return c;
	}

}
