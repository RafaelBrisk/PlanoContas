package planocontas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConexaoBanco {
	
	private Connection con;
	private static ConexaoBanco self;

	private ConexaoBanco() {
		try {
			Class.forName("org.postgresql.Driver");
			this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
			
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				try {
					ConexaoBanco.this.con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static synchronized ConexaoBanco getInstace() {
		return self == null ? new ConexaoBanco() : self;
	}
	
	public Connection getConnection() {
		return this.con;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("Singleton");
	}
	
	

}
