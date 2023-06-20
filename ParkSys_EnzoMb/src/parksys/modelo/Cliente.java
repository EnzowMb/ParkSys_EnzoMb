package parksys.modelo;

public class Cliente {
	
	private String cpf;
	private String nome;
	private String telefone;
	private String obs; //opcional
	private double saldo;
	
	//CPF
	
	public String getCpf() {
		return cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	//
	
	//Nome
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	//
	
	//Telefone
	
	public String getTelefone() {
		return telefone;
	}
	
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	//
	
	//Observação do Veiculo
	
	public String getObs() {
		return obs;
	}
	
	public void setObs(String obs) {
		this.obs = obs;
	}
	
	//
	
	//Saldo
	
	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	
	//
	
}
