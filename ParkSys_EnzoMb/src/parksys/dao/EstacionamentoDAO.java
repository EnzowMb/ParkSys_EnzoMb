package parksys.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import parksys.modelo.Cliente;
import parksys.modelo.EntradaVeiculo;
import parksys.modelo.Estacionamento;
import parksys.modelo.SaidaVeiculo;

public class EstacionamentoDAO {
	
	private ParksysDataSource dataSource = new ParksysDataSource();
	
	public EntradaVeiculo Inserir(EntradaVeiculo entrada) throws DataAccessException {
		
		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);
			
			try (
					PreparedStatement inserir = conn
							.prepareStatement("INSERT INTO Estacionamento VALUES(?);");) {
				
				inserir.setString(1, entrada.getPlaca().toUpperCase());
				inserir.executeUpdate();
				
				conn.commit();

			} catch(SQLException e) {
				conn.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		
		return entrada;
	}
	
	public SaidaVeiculo Deletar(SaidaVeiculo saida) throws DataAccessException {
		
		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);
			
			try (
					PreparedStatement inserir = conn
							.prepareStatement("DELETE FROM Estacionamento where placa = ?");) {
				
				inserir.setString(1, saida.getPlaca().toUpperCase());
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
	
	public Estacionamento Consultar(String placa) throws DataAccessException {
		
		Estacionamento estacionamento = null;
		
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("select placa from Estacionamento where placa = ?;")) {
			ps.setString(1, placa);

			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					estacionamento = new Estacionamento();
					estacionamento.setPlaca(rs.getString("placa"));
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		
		return estacionamento;
	}
	
	public int ConsultarVagas() throws DataAccessException {
		
		int vagas = 0;
		
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("select count(*) as vagas from Estacionamento;")) {

			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					vagas = rs.getInt("vagas");
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		
		return vagas;
	}

}
