package curso.springboot.enums;

public enum Cargo {
	
	ESTAGIARIO("Estagiário"),
	JUNIOR("Júnior"),
	PLENO("Pleno"),
	SENIOR("Sênior");
	
	private String nome;
	
	Cargo(String nome) {
		this.nome = nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
	
	@Override
	public String toString() {
		return this.name();
	}
	
}
