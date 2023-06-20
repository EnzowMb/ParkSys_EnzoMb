package parksys.modelo;

public class Veiculo {
	
	private String placa;
	private String obsVeiculo;
	private String cpf_dono;
	private String telefone_dono;
	
	//Placa Veiculo
	
	public String getPlaca() {
		return placa;
	}
	
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	
	//
	
	//Descrição do Veiculo
	
	public String getObsVeiculo() {
		return obsVeiculo;
	}
	
	public void setObsVeiculo(String obsVeiculo) {
		this.obsVeiculo = obsVeiculo;
	}
	
	//
	
	//Cpf do cliente mensalista(se houver)
	
	public String getCpf_dono() {
		return cpf_dono;
	}
	
	public void setCpf_dono(String cpf_dono) {
		this.cpf_dono = cpf_dono;
	}
	
	//
	
	//Telefone do cliente mensalista(se houver)
	
	public String getTelefone_dono() {
		return telefone_dono;
	}
	
	public void setTelefone_dono(String telefone_dono) {
		this.telefone_dono = telefone_dono;
	}
	
	//
	
}
