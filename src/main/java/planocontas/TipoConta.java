package planocontas;

public enum TipoConta {
	
	SINTETICA("Sintética"), ANALITICA("Analítica");
	
	private String nome;
	
	private TipoConta(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return this.nome;
	}

}
