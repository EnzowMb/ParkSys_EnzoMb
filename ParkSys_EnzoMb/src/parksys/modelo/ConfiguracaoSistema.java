package parksys.modelo;

public class ConfiguracaoSistema {
	
	private boolean bloco;
	private double tarifa;
	private int desconto;
	private int qtd_hora_min;
	
	//Bloco
	
	public void setBloco(boolean bloco) {
		this.bloco = bloco;
	}
	
	public boolean getBloco() {
		return bloco;
	}
	
	//
	
	//Tarifa
	
	public double getTarifa() {
		return tarifa;
	}
	
	public void setTarifa(double tarifa) {
		this.tarifa = tarifa;
	}
	
	//
	
	//Desconto
	
	public int getDesconto() {
		return desconto;
	}
	
	public void setDesconto(int desconto) {
		this.desconto = desconto;
	}
	
	//
	
	//Quantidade de Horas minimas para desconto
	
	public int getQtd_hora_min() {
		return qtd_hora_min;
	}
	
	public void setQtd_hora_min(int qtd_hora_min) {
		this.qtd_hora_min = qtd_hora_min;
	}
	
	//
	
}
