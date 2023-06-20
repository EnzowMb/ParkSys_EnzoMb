package parksys.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import parksys.modelo.EntradaVeiculo;
import parksys.modelo.SaidaVeiculo;

public class SaidaVeiculoDAO {
	
	private ParksysDataSource dataSource = new ParksysDataSource();
	
	public SaidaVeiculo Inserir(SaidaVeiculo saida) throws DataAccessException {
		
		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);
			
			try (
					PreparedStatement inserir = conn
							.prepareStatement("INSERT INTO SaidaVeiculo VALUES( ?, ?, ?, ?);");) {
				
				inserir.setString(1, saida.getPlaca().toUpperCase());
				inserir.setString(2, saida.getHorarioSaida());
				inserir.setInt(3, saida.getQtdBlocoDev());
				inserir.setDouble(4, saida.getTotalPagar());
				inserir.executeUpdate();
				
				conn.commit();

			} catch(SQLException e) {
				conn.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		
		return saida;
	}
	
}
