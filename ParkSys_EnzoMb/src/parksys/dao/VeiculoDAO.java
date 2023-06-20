package parksys.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import parksys.modelo.Veiculo;

public class VeiculoDAO {
	
	private ParksysDataSource dataSource = new ParksysDataSource();
	
	
	//Inserir um Veiculo
	
	public Veiculo InserirVeiculo(Veiculo veiculo) throws DataAccessException {
		
		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);
			
			try (
					PreparedStatement inserir = conn
							.prepareStatement("INSERT INTO Veiculo VALUES (?, ?, ?, ?);");) {
				
				inserir.setString(1, veiculo.getCpf_dono());
				inserir.setString(2, veiculo.getTelefone_dono());
				inserir.setString(3, veiculo.getPlaca().toUpperCase());
				inserir.setString(4, veiculo.getObsVeiculo());
				inserir.executeUpdate();
				
				conn.commit();

			} catch(SQLException e) {
				conn.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		
		return veiculo;
	}
}
