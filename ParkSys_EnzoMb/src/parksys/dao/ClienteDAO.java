package parksys.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import parksys.modelo.Cliente;

public class ClienteDAO {
	
	private ParksysDataSource dataSource = new ParksysDataSource();
	
	//Inserir um Cliente
	
	public Cliente Inserir(Cliente cliente) throws DataAccessException {
		
		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);
			
			try (
					PreparedStatement inserir = conn
							.prepareStatement("INSERT INTO Cliente VALUES (?, ?, ?, ?, ?);");) {
				
				inserir.setString(1, cliente.getCpf());
				inserir.setString(2, cliente.getNome());
				inserir.setString(3, cliente.getTelefone());
				inserir.setString(4, cliente.getObs());
				inserir.setDouble(5, cliente.getSaldo());
				inserir.executeUpdate();
				
				conn.commit();

			} catch(SQLException e) {
				conn.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		
		return cliente;
	}
	
	//Consultar Saldo do Cliente
	
	public Cliente consultarSaldo(String placa) throws DataAccessException {
		Cliente cliente = null;
		
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("select c.saldo as saldo, c.cpf as cpf, c.nome as nome from cliente c\r\n inner join veiculo v on c.cpf = v.cpf "
								+ "where v.placa = ?;")) {
			ps.setString(1, placa);

			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					cliente = new Cliente();
					cliente.setNome(rs.getString("nome"));
					cliente.setSaldo(rs.getDouble("saldo"));
					cliente.setCpf(rs.getString("cpf"));
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		
		return cliente;
	}
	
	public void updateSaldo(double precoTotal, String cpf) throws DataAccessException {
		
		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);
			
			try (
					PreparedStatement saque = conn
							.prepareStatement("UPDATE Cliente SET saldo = (saldo - ?) WHERE cpf = ?;");) {
				
				saque.setDouble(1, precoTotal);
				saque.setString(2, cpf);
				saque.executeUpdate();			
				
				conn.commit();

			} catch(SQLException e) {
				conn.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
	
	public void updateSaldoDeposito(double precoTotal, String cpf) throws DataAccessException {
		
		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);
			
			try (
					PreparedStatement deposito = conn
							.prepareStatement("UPDATE Cliente SET saldo = (saldo + ?) WHERE cpf = ?;");) {
				
				deposito.setDouble(1, precoTotal);
				deposito.setString(2, cpf);
				deposito.executeUpdate();			
				
				conn.commit();

			} catch(SQLException e) {
				conn.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
	
	//Consultar um cliente pelo CPF ou Telefone
	
	public Cliente findByCPForTelefone(String telCpf) throws DataAccessException {
		Cliente cliente = null;
		
	//Consulta pelo CPF
		
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("select cpf, nome, telefone, saldo from cliente "
								+ "where cpf = ?;")) {
			ps.setString(1, telCpf);

			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					cliente = new Cliente();
					cliente.setCpf(rs.getString("cpf"));
					cliente.setNome(rs.getString("nome"));
					cliente.setTelefone(rs.getString("telefone"));
					cliente.setSaldo(rs.getDouble("saldo"));
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		
	//Consulta pelo telefone
		
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("select cpf, nome, telefone, saldo from cliente "
								+ "where telefone = ?;")) {
			ps.setString(1, telCpf);

			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					cliente = new Cliente();
					cliente.setCpf(rs.getString("cpf"));
					cliente.setNome(rs.getString("nome"));
					cliente.setTelefone(rs.getString("telefone"));
					cliente.setSaldo(rs.getDouble("saldo"));
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		
		return cliente;
	}
	
	//Consultar cliente pela Placa
	
	public Cliente findByPlaca(String placa) throws DataAccessException {
		Cliente cliente = null;
		
		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("select c.cpf from cliente c inner join veiculo v on c.cpf = v.cpf "
								+ "where v.placa = ?;")) {
			ps.setString(1, placa);

			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					cliente = new Cliente();
					cliente.setCpf(rs.getString("cpf"));
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		
		return cliente;
	}
	
}
