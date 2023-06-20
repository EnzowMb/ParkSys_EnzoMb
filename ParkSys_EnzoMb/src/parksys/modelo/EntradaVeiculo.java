package parksys.modelo;

import java.sql.Date;
import java.time.LocalDateTime;

public class EntradaVeiculo {
	
	private String placa;
	private String desVeiculo;
	private String horarioEntrada;
	private String duracaoBloco;
	private double precoBloco;
	
	public String getPlaca() {
		return placa;
	}
	
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	
	public String getDesVeiculo() {
		return desVeiculo;
	}
	
	public void setDesVeiculo(String desVeiculo) {
		this.desVeiculo = desVeiculo;
	}

	public String getHorarioEntrada() {
		return horarioEntrada;
	}

	public void setHorarioEntrada(String horarioEntrada) {
		this.horarioEntrada = horarioEntrada;
	}

	public String getDuracaoBloco() {
		return duracaoBloco;
	}
	
	public void setDuracaoBloco(String duracaoBloco) {
		this.duracaoBloco = duracaoBloco;
	}
	
	public double getPrecoBloco() {
		return precoBloco;
	}
	
	public void setPrecoBloco(double precoBloco) {
		this.precoBloco = precoBloco;
	}

}
