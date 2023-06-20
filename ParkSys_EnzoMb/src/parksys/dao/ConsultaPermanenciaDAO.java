package parksys.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import parksys.modelo.ConsultaPermanencia;
import parksys.modelo.Estacionamento;

public class ConsultaPermanenciaDAO {
	
	private ParksysDataSource dataSource = new ParksysDataSource();
	
	public List<ConsultaPermanencia> Consultar(String dataInicial, String dataFinal) throws DataAccessException {
		
		List<ConsultaPermanencia> permanencia = null;
		
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("select dayname(s.horarioSaida) as dia, s.horarioSaida as saida, e.horarioEntrada as entrada "
								+ "from SaidaVeiculo s inner join EntradaVeiculo e on s.placa = e.placa "
								+ "where s.horarioSaida between STR_TO_DATE( ? , \"%d/%m/%y\" ) and STR_TO_DATE( ? , \"%d/%m/%y\" );")) {
			ps.setString(1, dataInicial);
			ps.setString(2, dataFinal);

			try (ResultSet rs = ps.executeQuery();) {
				permanencia = new ArrayList<>();
				while(rs.next()) {
					ConsultaPermanencia per = new ConsultaPermanencia();
					per.setDia(rs.getString("dia"));
					per.setHorarioSaida(rs.getString("saida"));
					per.setHorarioEntrada(rs.getString("entrada"));
					permanencia.add(per);
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		
		return permanencia;
	}

}
