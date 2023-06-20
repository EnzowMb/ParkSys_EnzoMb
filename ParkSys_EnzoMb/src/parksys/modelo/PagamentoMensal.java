package parksys.modelo;

import java.util.Date;

public class PagamentoMensal {
	private String cpf;
	private String data;
	private double valor;
	private int saldoHoras;
	
	public String getCpf() {
		return cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public double getValor() {
		return valor;
	}
	
	public void setValor(double valor) {
		this.valor = valor;
	}
	
	public int getSaldoHoras() {
		return saldoHoras;
	}
	
	public void setSaldoHoras(int saldoHoras) {
		this.saldoHoras = saldoHoras;
	}

}
