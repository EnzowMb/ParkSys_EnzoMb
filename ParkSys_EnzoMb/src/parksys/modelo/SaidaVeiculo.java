package parksys.modelo;

public class SaidaVeiculo {
	
	private String placa;
	private String horarioSaida;
	private int qtdBlocoDev;
	private double totalPagar;
	
	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getHorarioSaida() {
		return horarioSaida;
	}
	
	public void setHorarioSaida(String horarioSaida) {
		this.horarioSaida = horarioSaida;
	}
	
	public int getQtdBlocoDev() {
		return qtdBlocoDev;
	}
	
	public void setQtdBlocoDev(int qtdBlocoDev) {
		this.qtdBlocoDev = qtdBlocoDev;
	}
	
	public double getTotalPagar() {
		return totalPagar;
	}
	
	public void setTotalPagar(double totalPagar) {
		this.totalPagar = totalPagar;
	}
	
}
