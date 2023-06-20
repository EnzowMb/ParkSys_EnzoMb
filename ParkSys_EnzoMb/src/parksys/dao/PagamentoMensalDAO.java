package parksys.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import parksys.modelo.PagamentoMensal;
import parksys.modelo.Veiculo;

public class PagamentoMensalDAO {
	
	private ParksysDataSource dataSource = new ParksysDataSource();
	
	public PagamentoMensal InserirPagamento(PagamentoMensal pagamento) throws DataAccessException {
		
		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);
			
			try (
					PreparedStatement inserir = conn
							.prepareStatement("INSERT INTO PagamentoMensal VALUES (?, ?, ?, ?);");) {
				
				inserir.setString(1, pagamento.getCpf());
				inserir.setString(2, pagamento.getData());
				inserir.setDouble(3, pagamento.getValor());
				inserir.setInt(4, pagamento.getSaldoHoras());
				inserir.executeUpdate();
				
				conn.commit();

			} catch(SQLException e) {
				conn.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		
		return pagamento;
	}

}
