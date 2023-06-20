package parksys.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import parksys.modelo.ConsultaFaturamento;

public class ConsultaFaturamentoDAO {
	
	private ParksysDataSource dataSource = new ParksysDataSource();
	
	public List<ConsultaFaturamento> Consultar(String dataInicial, String dataFinal) throws DataAccessException {
		
		List<ConsultaFaturamento> faturamento = null;
		
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("select date_format(horarioSaida, \"%d/%m/%y\") as dataSaida, sum(precoTotal) as valor "
								+ "from saidaveiculo "
								+ "where horarioSaida between STR_TO_DATE( ?, \"%d/%m/%y\" ) and STR_TO_DATE( ?, \"%d/%m/%y\" ) "
								+ "group by dataSaida;")) {
			ps.setString(1, dataInicial);
			ps.setString(2, dataFinal);

			try (ResultSet rs = ps.executeQuery();) {
				faturamento = new ArrayList<>();
				while(rs.next()) {
					ConsultaFaturamento fat = new ConsultaFaturamento();
					fat.setData(rs.getString("dataSaida"));
					fat.setValor(rs.getDouble("valor"));
					faturamento.add(fat);
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		
		return faturamento;
	}
}
