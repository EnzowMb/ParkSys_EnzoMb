package parksys.graficos;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import parksys.dao.ConsultaFaturamentoDAO;
import parksys.dao.ConsultaPermanenciaDAO;
import parksys.dao.DataAccessException;
import parksys.modelo.ConsultaFaturamento;
import parksys.modelo.ConsultaPermanencia;

/*
 * Implementação baseada no exemplo disponível em:
 * https://github.com/knowm/XChart/blob/develop/xchart-demo/src/main/java/org/knowm/xchart/demo/charts/date/DateChart05.java
 */
public class GraficoFaturamentoDiario {
	
	private List<String> datas;
	private List<Double> valores;
	
	private void execute() {
		XYChart chart = buildChart();
		new SwingWrapper<>(chart).displayChart();
	}
	
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
				.title("Faturamento Diário")
				.yAxisTitle("R$")
				.build();
		
		// Series
		
		ConsultaFaturamentoDAO dao = new ConsultaFaturamentoDAO();
		List<ConsultaFaturamento> faturamento = new ArrayList<>();
		
		try {
			faturamento = dao.Consultar(dataInicial, dataFinal);
		} catch (DataAccessException e1) {
			e1.printStackTrace();
		}
		
		datas = new ArrayList<>();
		valores = new ArrayList<>();
		
		faturamento.forEach(new Consumer<ConsultaFaturamento>() {

			public void accept(ConsultaFaturamento fat) {
				//System.out.println(fat.getData());
				//System.out.println(fat.getValor());
				
				datas.add(fat.getData());
				valores.add(fat.getValor());
			}
		});
		
		List<Date> xs;
		List<Double> ys;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
		
		if(datas.isEmpty() || valores.isEmpty()) {
			xs = new ArrayList<>();
			ys = List.of(0.0);
			String[] dates = new String[] {"00/00/00"};		
			for (String d : dates) {
				try {
					xs.add(sdf.parse(d));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		} else {
			xs = new ArrayList<>();
			ys = valores;
		for (String d : datas) {
			try {
				xs.add(sdf.parse(d));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		}
		
		chart.addSeries("Faturamento diário", xs, ys);

		// Aparência
		chart.getStyler().setLegendVisible(false);
		
		/*
		 * Alterando marcações (ticks) no eixo X, para exibir nomes dos dias da semana.
		 * Implementação baseada no exemplo disponível em:
		 * https://github.com/knowm/XChart/blob/36977a3dc5be67085a086528b84dae67856ba38b/xchart-demo/src/main/java/org/knowm/xchart/demo/charts/date/DateChart06.java#L63
		 */
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM");
		chart.getStyler().setxAxisTickLabelsFormattingFunction(date -> formatador.format(date));
		
		return chart;
	}

	public static void main(String[] args) {
		new GraficoFaturamentoDiario().execute();
	}
}
