package parksys.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import parksys.modelo.Cliente;
import parksys.modelo.EntradaVeiculo;

public class EntradaVeiculoDAO {
	
	private ParksysDataSource dataSource = new ParksysDataSource();
	
	public EntradaVeiculo Inserir(EntradaVeiculo entrada) throws DataAccessException {
		
		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);
			
			try (
					PreparedStatement inserir = conn
							.prepareStatement("INSERT INTO EntradaVeiculo VALUES (?, ?, ?, ?, ?);");) {
				
				inserir.setString(1, entrada.getPlaca().toUpperCase());
				inserir.setString(2, entrada.getDesVeiculo());
				inserir.setString(3, entrada.getHorarioEntrada());
				inserir.setString(4, entrada.getDuracaoBloco());
				inserir.setDouble(5, entrada.getPrecoBloco());
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
	
	public int ConsultarVagas() throws DataAccessException {
		
		int vagas = 0;
		
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("select count(*) as vagas from EntradaVeiculo;")) {

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
	
	public EntradaVeiculo resgatarDados(String placa) throws DataAccessException {
		EntradaVeiculo entrada = null;
		
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("select placa, desVeiculo, convert(horarioEntrada, char) as data, duracaoBloco, precoBloco"
								+ " from EntradaVeiculo"
								+ " where placa = ?"
								+ " order by convert(horarioEntrada, char) desc;")) {
			ps.setString(1, placa);

			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					entrada = new EntradaVeiculo();
					entrada.setPlaca(rs.getString("placa"));
					entrada.setDesVeiculo(rs.getString("desVeiculo"));
					entrada.setHorarioEntrada(rs.getString("data"));
					entrada.setDuracaoBloco(rs.getString("duracaoBloco"));
					entrada.setPrecoBloco(rs.getDouble("precoBloco"));
				}
			}
			
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		
		return entrada;
	}

}
