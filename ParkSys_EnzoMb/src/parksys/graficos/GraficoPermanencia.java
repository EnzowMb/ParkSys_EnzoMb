package parksys.graficos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import parksys.dao.ConsultaPermanenciaDAO;
import parksys.dao.DataAccessException;
import parksys.dao.ParksysDataSource;
import parksys.modelo.ConsultaPermanencia;
import parksys.modelo.EntradaVeiculo;
import parksys.modelo.Estacionamento;

public class GraficoPermanencia {
	
	private double perSegunda;
	private double perTerca;
	private double perQuarta;
	private double perQuinta;
	private double perSexta;
	private double perSabado;
	
	private void execute() {
		XYChart chart = buildChart();
		new SwingWrapper<>(chart).displayChart();
	}
	
	private double permanenciaEstacionamento(String dataSaida, String dataEntrada) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		LocalDateTime entradaData = LocalDateTime.parse(dataEntrada, formatter);
		LocalDateTime saidaData = LocalDateTime.parse(dataSaida, formatter);
		
		long diffInMinutes = java.time.Duration.between(entradaData, saidaData).toMinutes();
		
		return diffInMinutes;
	}
	
	/*
	 * Implementação baseada no exemplo disponível em:
	 * https://github.com/knowm/XChart/blob/36977a3dc5be67085a086528b84dae67856ba38b/xchart-demo/src/main/java/org/knowm/xchart/demo/charts/date/DateChart06.java#L34
	 */
	private XYChart buildChart() {
		
		System.out.println("Exemplo de data : 01/12/21");
		Scanner teclado = new Scanner(System.in);
		System.out.println("Informe a data inicial dia/mes/ano : ");
		String dataInicial = teclado.next();

		DateFormat df = new SimpleDateFormat ("dd/MM/yy");
		df.setLenient (false);
		try {
		    df.parse(dataInicial);
		} catch (ParseException ex) {
		   System.out.println("Informe uma data Correta!!!");
		   System.out.println(ex);
		   System.exit(0);
		}
		
		System.out.println("Informe a data final dia/mes/ano : ");
		String dataFinal = teclado.next();
		
		try {
		    df.parse(dataFinal);
		} catch (ParseException ex) {
		   System.out.println("Informe uma data Correta!!!");
		   System.out.println(ex);
		   System.exit(0);
		}
		
		XYChart chart = new XYChartBuilder()
				.width(600)
				.height(400)
				.title("Permanência Média por Dia da Semana - " + dataInicial + " - " + dataFinal)
				.yAxisTitle("Horas")
				.xAxisTitle("Dia da Semana")
				.build();
		
		// Series
		
		ConsultaPermanenciaDAO dao = new ConsultaPermanenciaDAO();
		List<ConsultaPermanencia> permanencia = new ArrayList<>();
		
		try {
			permanencia = dao.Consultar(dataInicial, dataFinal);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		
		permanencia.forEach(new Consumer<ConsultaPermanencia>() {

			public void accept(ConsultaPermanencia lista) {
				System.out.println(lista.getDia());
				System.out.println(lista.getHorarioEntrada());
				System.out.println(lista.getHorarioSaida());
				
				if(lista.getDia().equalsIgnoreCase("Monday")) {
					perSegunda = perSegunda + (permanenciaEstacionamento(lista.getHorarioSaida(), lista.getHorarioEntrada()) / 60);
				}
				
				if(lista.getDia().equalsIgnoreCase("Tuesday")) {
					perTerca = perTerca + (permanenciaEstacionamento(lista.getHorarioSaida(), lista.getHorarioEntrada()) / 60);
				}
				
				if(lista.getDia().equalsIgnoreCase("Wednesday")) {
					perQuarta = perQuarta + (permanenciaEstacionamento(lista.getHorarioSaida(), lista.getHorarioEntrada()) / 60);
				}
				
				if(lista.getDia().equalsIgnoreCase("Thursday")) {
					perQuinta = perQuinta + (permanenciaEstacionamento(lista.getHorarioSaida(), lista.getHorarioEntrada()) / 60);
				}
				
				if(lista.getDia().equalsIgnoreCase("Friday")) {
					perSexta = perSexta + (permanenciaEstacionamento(lista.getHorarioSaida(), lista.getHorarioEntrada()) / 60);
				}
				
				if(lista.getDia().equalsIgnoreCase("Saturday")) {
					perSabado = perSabado + (permanenciaEstacionamento(lista.getHorarioSaida(), lista.getHorarioEntrada()) / 60);
				}
			}
		});
		
		List<Integer> xs = Arrays.asList(1, 2, 3, 4, 5, 6);
		List<Double> ys = List.of(perSegunda, perTerca, perQuarta, perQuinta, perSexta, perSabado);
		
		chart.addSeries("Tempo médio (em horas)", xs, ys);

		// Aparência
		chart.getStyler().setLegendVisible(false);
		
		/*
		 * Alterando marcações (ticks) no eixo X, para exibir nomes dos dias da semana.
		 * Implementação baseada no exemplo disponível em:
		 * https://github.com/knowm/XChart/blob/36977a3dc5be67085a086528b84dae67856ba38b/xchart-demo/src/main/java/org/knowm/xchart/demo/charts/date/DateChart06.java#L63
		 */
		String[] diasSemana = new String[] {"Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"};
		chart.getStyler().setxAxisTickLabelsFormattingFunction(d -> diasSemana[d.intValue() - 1]);
		
		return chart;
	}

	public static void main(String[] args) {
		new GraficoPermanencia().execute();
	}
}
