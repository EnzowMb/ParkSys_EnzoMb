package parksys.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import parksys.modelo.Cliente;
import parksys.modelo.ConfiguracaoSistema;


//			------=Classe Mock=-------


public class ConfiguracaoDAO {
	
	private ParksysDataSource dataSource = new ParksysDataSource();
	
	public boolean consultarExiste() throws DataAccessException {
		int existe = 0;
		Boolean existeConfig;
		
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("select COUNT(*) as existe from Configurar;")) {

			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					existe = rs.getInt("existe");
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		
		if(existe == 0) {
			existeConfig = false;
		} else {
			existeConfig = true;
		}
		
		return existeConfig;
	}
	
	public ConfiguracaoSistema Inserir(ConfiguracaoSistema config) throws DataAccessException {
		
		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);
			
			try (
					PreparedStatement inserir = conn
							.prepareStatement("INSERT INTO Configurar VALUES(?, ?, ?, ?);");) {
				
				inserir.setBoolean(1, config.getBloco());
				inserir.setDouble(2, config.getTarifa());
				inserir.setInt(3, config.getDesconto());
				inserir.setInt(4, config.getQtd_hora_min());
				inserir.executeUpdate();
				
				conn.commit();

			} catch(SQLException e) {
				conn.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		
		return config;
	}
	
	public ConfiguracaoSistema Configurar(ConfiguracaoSistema configSis) throws DataAccessException {
		
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("select bloco, tarifa, desconto, qtd_hora from configurar;")) {

			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					configSis.setBloco(rs.getBoolean("bloco"));
					configSis.setTarifa(rs.getDouble("tarifa"));
					configSis.setDesconto(rs.getInt("desconto"));
					configSis.setQtd_hora_min(rs.getInt("qtd_hora"));
					
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		
		return configSis;
	}
	
	public void updateBloco(boolean bloco) throws DataAccessException {
		
		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);
			
			try (
					PreparedStatement up = conn
							.prepareStatement("update configurar set bloco = ?;");) {
				
				up.setBoolean(1, bloco);
				up.executeUpdate();
				
				conn.commit();

			} catch(SQLException e) {
				conn.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
	
	public void updateTarifa(double tarifa) throws DataAccessException {
		
		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);
			
			try (
					PreparedStatement up = conn
							.prepareStatement("update configurar set tarifa = ?;");) {
				
				up.setDouble(1, tarifa);
				up.executeUpdate();
				
				conn.commit();

			} catch(SQLException e) {
				conn.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
	
	public void updateDesconto(int desconto) throws DataAccessException {
		
		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);
			
			try (
					PreparedStatement up = conn
							.prepareStatement("update configurar set desconto = ?;");) {
				
				up.setInt(1, desconto);
				up.executeUpdate();
				
				conn.commit();

			} catch(SQLException e) {
				conn.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
	
	public void updateHoraMin(int horaMin) throws DataAccessException {
		
		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);
			
			try (
					PreparedStatement up = conn
							.prepareStatement("update configurar set qtd_hora = ?;");) {
				
				up.setInt(1, horaMin);
				up.executeUpdate();
				
				conn.commit();

			} catch(SQLException e) {
				conn.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
	
}
