package parksys.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;
import javax.swing.text.MaskFormatter;

import parksys.dao.ClienteDAO;
import parksys.dao.ConfiguracaoDAO;
import parksys.dao.DataAccessException;
import parksys.dao.EntradaVeiculoDAO;
import parksys.dao.EstacionamentoDAO;
import parksys.dao.PagamentoMensalDAO;
import parksys.dao.SaidaVeiculoDAO;
import parksys.dao.VeiculoDAO;
import parksys.graficos.GraficoPermanencia;
import parksys.modelo.Cliente;
import parksys.modelo.ConfiguracaoSistema;
import parksys.modelo.EntradaVeiculo;
import parksys.modelo.Estacionamento;
import parksys.modelo.PagamentoMensal;
import parksys.modelo.SaidaVeiculo;
import parksys.modelo.Veiculo;


public class ParksysApp {
	
	private static final Locale BRAZIL = new Locale("pt", "br");
	
	
	//ATUALIZAR VAGAS DISPONIVEIS AQUI!!
		private int vagasDisponiveis = 10;
	//
	
	
	//Tela Principal
	private JFrame frame;
	private JPanel panel;
	private JLabel statusVagas;
	//
	
	//Botões
	private JButton buttonCadastrar; //Botao Cadastrar Mensalista
	private JButton buttonEntradaSaida; //Botao Registrar Entrada e Saida
	private JButton buttonConfig; //Botao Configuração do sistema
	private JButton buttonGereVei; //Botao Gerenciar Veiculos
	private JButton buttonRegistrarEntrada; //Botao Registrar Entrada
	private JButton buttonRegistrarSaida; //Botão Registrar Saida
	private JButton buttonPagamento; //Botão Realizar Pagamento
	private JButton buttonVisu; //Botão Visualizar Pagamento
	//
	
	//Cadastrar Mensalista Variaveis
	private JTextField cpfTxt;
	private String stringCpf;
	private JTextField nomeTxt;
	private String stringNome;
	private JTextField telefoneTxt;
	private String stringTelefone;
	private JTextField obsTxt;
	private String stringObs;
	private JTextField saldoTxt;
	private double doubleSaldo;
	//
	
	//Gerenciar Veiculo variaveis
	private JTextField placaTxt;
	private JTextField obsVeiculoTxt;
	private JFrame frameGVeiculo;
	private JPanel panelGerenciar;
	private Veiculo veiculo;
	private JPanel panelInserir;
	private JFrame frameVeiculo;
	//
	
	//Fazer Pagamento Mensal
	private JFrame framePagamento;
	private JPanel panelPagamento;
	private JPanel panelValor;
	private JTextField valorTxt;
	private PagamentoMensal pagamento;
	//
	
	//Registrar Entrada Variaveis
	private JFrame frameEntSaida;
	private JPanel panelEntSaida;
	private JPanel panelEntrada;
	private JTextField placaEntradaTxt;
	private String stringPlacaEntrada;
	private JLabel placaLabelEntrada;
	private JLabel horarioEntradaLabel;
	private JLabel duracaoBlocoLabel;
	private JLabel precoBlocoLabel;
	private String entradaHorario;
	//
	
	//Registrar Saida Variaveis
	private JPanel panelSaida;
	private String saidaHorario;
	private long tempoPermanencia;
	private int qtdBlocos;
	private double precoTotal;
	private JLabel horarioSaidaLabel;
	private JLabel tempoLabel;
	private JLabel qtdBlocoLabel;
	private JLabel totalLabel;
	private String placaSaida;
	private boolean ClienteMensalista;
	private boolean tagPagamentoMensalSaida;
	
	//Visualizar Saldo
	private JPanel panelVisu;
	JLabel saldoLabel;
	
	//Configuracao Sistema
	private JFrame frameConfig;
	private boolean tagFirstExecute;
	private ConfiguracaoSistema configSis;
	private JRadioButton radio1hora;
	private JRadioButton radio30min;
	private JTextField tarifaTxt;
	private JTextField descontoTxt;
	private JTextField qtdTxt;
	//
	
	
	//  		----------------=Tela Principal=------------------
	
	private void inserirTelaPrincipal() throws DataAccessException {
		tagFirstExecute = false;
		
		frame = new JFrame("Parksys");
		
		//Painel Principal
		frame.getContentPane().setLayout(new BorderLayout());
		panel = buildPanelPrincipal();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		statusVagas = new JLabel("Carregando...");
		//statusVagas.setForeground(Color.YELLOW);
		frame.getContentPane().add(statusVagas, BorderLayout.NORTH);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setSize( 650, 450 );
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				confirmExit();
			}			
		});
		
		frame.setVisible(true);
		
		inserirVagas();
	}
	
	private void createAndShowGUI() {
		
		ConfiguracaoDAO verificaExiste = new ConfiguracaoDAO();
		try {
			if(verificaExiste.consultarExiste()) {
				
			inserirTelaPrincipal();
				
			} else {
				
				tagFirstExecute = true;
				
				frameConfig = new JFrame("Configurar Sistema");
				
				frameConfig.getContentPane().setLayout(new BorderLayout());
				frameConfig.getContentPane().add(buildPanelConfig(), BorderLayout.CENTER);
				frameConfig.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frameConfig.pack();
				frameConfig.setLocationRelativeTo(null);
				frameConfig.setVisible(true);

			}
		} catch (DataAccessException e1) {
			e1.printStackTrace();
		}
		
	}
	
	private void inserirVagas() {
		
		SwingWorker<String, Void> worker = new SwingWorker<>() {

			@Override
			protected String doInBackground() throws DataAccessException {
				EstacionamentoDAO dao = new EstacionamentoDAO();
				String vagas = "" + dao.ConsultarVagas();
				return vagas;
			}

			@Override
   			protected void done() {
   				try {
   					String vagasConvertido = get();
   					Integer vagas = Integer.parseInt(vagasConvertido);
   					vagasDisponiveis = vagasDisponiveis - vagas;
   					if (vagasDisponiveis == 0) {
   						JOptionPane.showMessageDialog(frame, "ESTACIONAMENTO CHEIO!!!!", "Aviso", JOptionPane.WARNING_MESSAGE);
   						statusVagas.setText("ESTACIONAMENTO CHEIO!!!!");
   						statusVagas.setForeground(Color.RED);
   					} else {
   						statusVagas.setText("Vagas Disponiveis : " + vagasDisponiveis);
   						statusVagas.setForeground(Color.BLACK);
   					}
   				} catch (InterruptedException | ExecutionException e) {
   					JOptionPane.showMessageDialog(frame, e, "Erro", JOptionPane.ERROR_MESSAGE);
   				}
   			};
		};
		
		worker.execute();
		
	}
	
	private void confirmExit() {
		int answer = JOptionPane.showConfirmDialog(frame,
				"Deseja sair do sistema Parksys?", "Cancelar", JOptionPane.YES_NO_OPTION);

		if (answer == JOptionPane.YES_OPTION) {
			//frame.dispose();
			System.exit(0);
		}
	}
	
	private void verificarOSaldo() {
		JOptionPane.showMessageDialog(framePagamento, "Pagamento não realizado!!!", "AVISO", JOptionPane.WARNING_MESSAGE);
	}
	
	
	private JPanel buildPanelPrincipal() throws DataAccessException {
		JPanel panel = new JPanel();
		
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc;
		
		//Configurar Sistema
		ConfiguracaoDAO configDao = new ConfiguracaoDAO();
		
		configSis = new ConfiguracaoSistema();
		
		configDao.Configurar(configSis);
		
		//Cadastrar Mensalista
		
		cadastrarMensalista(panel);
		
		//Gerenciar Veiculos
		
		gerenciarVeiculo(panel);
		
		//Entrada e Saida
		
		entradaSaida(panel);
		
		//Fazer Pagamento Mensal
		
		pagamentoMensal(panel);
		
		buttonVisu = new JButton("Visualizar Saldo");
		buttonVisu.addActionListener(this::onVisuClick);
	
		buttonConfig = new JButton("Configurar Sistema");
		buttonConfig.addActionListener(this::onConfigClick);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(10, 10, 10, 10);		
		panel.add(buttonConfig, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(10, 10, 10, 10);		
		panel.add(buttonVisu, gbc);
		
		panel.setBackground(Color.orange);
		
		return panel;
	}
	//			-------=END TELA PRINCIPAL=---------
	
	
	//  ----------------=Visualizar Saldo=------------------
	
	private void onVisuClick(ActionEvent e) {
		frameGVeiculo  = new JFrame("Visualizar Saldo");
		
		frameGVeiculo.getContentPane().setLayout(new BorderLayout());
		frameGVeiculo.getContentPane().add(buildPanelVisualizarSaldo(), BorderLayout.CENTER);
		frameGVeiculo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frameGVeiculo.pack();
		frameGVeiculo.setLocationRelativeTo(null);
		frameGVeiculo.setVisible(true);
	}
	
	private JPanel buildPanelVisualizarSaldo() {
		panelVisu = new JPanel();
		
		panelVisu.setLayout(new BorderLayout());
		
		JPanel BuscarPanel = new JPanel(new FlowLayout());
		
		JLabel buscaLabel = new JLabel("Buscar pelo CPF ou Telefone: ");
		JTextField buscaTxt = new JTextField(30);
		
		saldoLabel = new JLabel("Saldo : R$");
		
		JButton btnBuscar = new JButton("Buscar");
	       btnBuscar.addActionListener((e) -> {
	   		
	   		SwingWorker<Cliente, Void> worker = new SwingWorker<>() {
	   			
	   			@Override
	   			protected Cliente doInBackground() throws DataAccessException {
	   				ClienteDAO dao = new ClienteDAO();
	   				Cliente cliente = dao.findByCPForTelefone(buscaTxt.getText());
	   				return cliente;
	   			}

	   			@Override
	   			protected void done() {
	   				try {
	   					Cliente cliente = get();
	   					if (cliente != null) {
	   						saldoLabel.setText("Saldo : R$" + cliente.getSaldo());
	   						panelVisu.add(saldoLabel);
	   						frameGVeiculo.pack();
	   					} else {
	   						JOptionPane.showMessageDialog(frameGVeiculo, "Erro!! Cliente não encontrado", "Erro", JOptionPane.ERROR_MESSAGE);
	   						saldoLabel.setText("Cliente não encontrado");
	   						panelVisu.add(saldoLabel);
	   						frameGVeiculo.pack();
	   					}
	   				} catch (InterruptedException | ExecutionException e) {
	   					JOptionPane.showMessageDialog(frameGVeiculo, e, "Erro", JOptionPane.ERROR_MESSAGE);
	   				}
	   			}

	   		};

	   		worker.execute();
	   
	       });
	       
	    BuscarPanel.add(buscaLabel);
	    BuscarPanel.add(buscaTxt);
	    BuscarPanel.add(btnBuscar);
	    panelVisu.add(BuscarPanel, BorderLayout.NORTH);
		
		return panelVisu;
	}
	
	
	//               ----------------=Cadastrar Mensalista=------------------
	
	private void cadastrarMensalista(JPanel panel) {
		
		GridBagConstraints gbc;
		
		JLabel CadastrarCliente = new JLabel("Cadastro do Cliente");
				
		JLabel cpfLabel = new JLabel("Cpf :");
		JLabel nomeLabel = new JLabel("Nome :");
		JLabel telefoneLabel = new JLabel("Telefone Celular :");
		JLabel obsLabel = new JLabel("Observaçao :");
		JLabel saldoLabel = new JLabel("Saldo : ");
				
		cpfTxt = new JTextField(20);

		nomeTxt = new JTextField(20);
				
		telefoneTxt = new JTextField(20);
						
		obsTxt = new JTextField(20);
		
		saldoTxt = new JTextField(20);
				
		buttonCadastrar = new JButton("Cadastrar Cliente");
		buttonCadastrar.addActionListener(this::onCadastrarClick);
				
		gbc = new GridBagConstraints();
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(10, 5, 0, 10);
		panel.add(CadastrarCliente, gbc);
				
		gbc = new GridBagConstraints();
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(10, 5, 0, 10);
		panel.add(cpfLabel, gbc);
				
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 5, 0, 10);
		panel.add(cpfTxt, gbc);
				
		gbc = new GridBagConstraints();
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(10, 5, 0, 10);
		panel.add(nomeLabel, gbc);
				
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 5, 0, 10);
		panel.add(nomeTxt, gbc);
				
		gbc = new GridBagConstraints();
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(10, 5, 0, 10);
		panel.add(telefoneLabel, gbc);
				
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 5, 0, 10);
		panel.add(telefoneTxt, gbc);
				
		gbc = new GridBagConstraints();
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(10, 5, 0, 10);
		panel.add(obsLabel, gbc);
				
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 5, 0, 10);
		panel.add(obsTxt, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(10, 5, 0, 10);
		panel.add(saldoLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 5, 0, 10);
		panel.add(saldoTxt, gbc);
				
		gbc = new GridBagConstraints();
		gbc.gridx = 10;
		gbc.gridy = 6;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		gbc.insets = new Insets(10, 10, 10, 10);		
		panel.add(buttonCadastrar, gbc);
		
	}
	
	//Botões Cadastrar Mensalista
	private void onCadastrarClick(ActionEvent e) {
		
		Cliente cliente = new Cliente();
		
		stringCpf = cpfTxt.getText();
		stringNome = nomeTxt.getText();
		stringTelefone = telefoneTxt.getText();
		stringObs = obsTxt.getText();
		
		// Medidas de segurança (pré checagem dos dados informados)
		if (stringCpf.isEmpty()|| stringNome.isEmpty() || stringTelefone.isEmpty() || saldoTxt.getText().isEmpty()) /*observação é opcional*/ {
			JOptionPane.showMessageDialog(frame, "Todos os campos devem ser preenchidos.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (contadorCaracteres(stringCpf)!=11) {
			JOptionPane.showMessageDialog(frame, "CPF deve possuir 11 digitos!\n", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if (contadorCaracteres(stringTelefone)!=11) {
			JOptionPane.showMessageDialog(frame, "Telefone deve possuir 11 digitos!\n", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}
		//
		
		doubleSaldo = Double.parseDouble(saldoTxt.getText());
		
		cliente.setCpf(stringCpf);
		cliente.setNome(stringNome);
		cliente.setTelefone(stringTelefone);
		cliente.setObs(stringObs);
		cliente.setSaldo(doubleSaldo);
		
		SwingWorker<Void, Void> worker = new SwingWorker<>() {

			@Override
			protected Void doInBackground() throws DataAccessException {
				ClienteDAO dao = new ClienteDAO();
				dao.Inserir(cliente);				
				return null;
			}

			protected void done() {
				try {
					get();
					
					frameVeiculo = new JFrame("Inserir Veiculos");
					
					frameVeiculo.getContentPane().setLayout(new BorderLayout());
					frameVeiculo.getContentPane().add(buildPanelInserirVeiculo(), BorderLayout.CENTER);
					frameVeiculo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frameVeiculo.pack();
					frameVeiculo.setLocationRelativeTo(null);
					frameVeiculo.setVisible(true);
					
					JOptionPane.showMessageDialog(frameVeiculo, "Cadastro realizado com sucesso!"
							+ "\n\nInserir Veiculos Para o Mensalista Cadastrado!!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
					
				} catch (InterruptedException | ExecutionException e) {
					JOptionPane.showMessageDialog(frame, e, "Erro", JOptionPane.ERROR_MESSAGE);
				}
			};
		};
		
		worker.execute();
		
	}
	
	//			-------=END CADASTRAR MENSALISTA=--------
	
	
	// 			----------=Fazer Pagamento Mensal=---------------
	
	private void pagamentoMensal(JPanel panel) {
		
		GridBagConstraints gbc;
		
		buttonPagamento = new JButton("Pagamento Mensal");
		buttonPagamento.addActionListener(this::onPagamentoClick);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(10, 10, 10, 10);		
		panel.add(buttonPagamento, gbc);
	}
	
	private void onPagamentoClick(ActionEvent e) {
		framePagamento = new JFrame("Pagamento Mensal");
		
		framePagamento.getContentPane().setLayout(new BorderLayout());
		framePagamento.getContentPane().add(buildPanelPagamentoMensal(), BorderLayout.CENTER);
		framePagamento.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		framePagamento.pack();
		framePagamento.setLocationRelativeTo(null);
		framePagamento.setVisible(true);
		
		tagPagamentoMensalSaida = false;
	}
	
	private void onValorClick(ActionEvent e) {
		
		if (valorTxt.getText().isEmpty()) {
			JOptionPane.showMessageDialog(framePagamento, "Todos os campos devem ser preenchidos.", "Aviso", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String data = dtf.format(LocalDateTime.now());
		
		double valor = Double.parseDouble(valorTxt.getText());
		
		if(tagPagamentoMensalSaida) {
		if(valor < precoTotal) {
			JOptionPane.showMessageDialog(framePagamento, "Valor minimo : " + precoTotal, "Aviso", JOptionPane.ERROR_MESSAGE);
			return;
		}
		}
		
		int saldoHoras;
		if(configSis.getBloco()) { //Blocos em horas
			saldoHoras = (int) (valor / configSis.getTarifa());
		} else { //Blocos em 1/2 hora
			saldoHoras = (int) (valor / (configSis.getTarifa() * 2));
		}
		
		pagamento.setData(data);
		pagamento.setValor(valor);
		pagamento.setSaldoHoras(saldoHoras);
       
		SwingWorker<Void, Void> worker = new SwingWorker<>() {

			@Override
			protected Void doInBackground() throws DataAccessException {
				PagamentoMensalDAO dao = new PagamentoMensalDAO();
				dao.InserirPagamento(pagamento);
				return null;
			}

			protected void done() {
				try {
					get();
					JOptionPane.showMessageDialog(framePagamento, "Pagamento registrado com Sucesso!!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
				} catch (InterruptedException | ExecutionException e) {
					JOptionPane.showMessageDialog(framePagamento, e, "Erro", JOptionPane.ERROR_MESSAGE);
				}
			};
		};
		
		worker.execute();
		
		ClienteDAO dao = new ClienteDAO();
		try {
			dao.updateSaldoDeposito(valor, pagamento.getCpf());
			if(tagPagamentoMensalSaida) {
				dao.updateSaldo(precoTotal, pagamento.getCpf());
				framePagamento.dispose();
			}
		} catch (DataAccessException e1) {
			e1.printStackTrace();
		}
		
	}
	
	private JPanel buildPanelInfomraValor()  {
		panelValor = new JPanel();
		
		JLabel labelValor = new JLabel("Informe o valor : ");
		valorTxt = new JTextField(15);
		
		JButton buttonValor = new JButton("Fazer Pagamento");
		buttonValor.addActionListener(this::onValorClick);
		
		panelValor.add(labelValor);
		panelValor.add(valorTxt);
		panelValor.add(buttonValor);
		
		return panelValor;
	}
	
	private JPanel buildPanelPagamentoMensal() {
		panelPagamento = new JPanel();
		
		panelPagamento.setLayout(new BorderLayout());
		
		JPanel BuscarPanel = new JPanel(new FlowLayout());
		
		JLabel buscaLabel = new JLabel("Buscar pelo CPF ou Telefone : ");
		JTextField buscaTxt = new JTextField(30);
		
		JButton btnBuscar = new JButton("Buscar");
	       btnBuscar.addActionListener((e) -> {
	   		
	   		SwingWorker<Cliente, Void> worker = new SwingWorker<>() {
	   			
	   			@Override
	   			protected Cliente doInBackground() throws DataAccessException {
	   				ClienteDAO dao = new ClienteDAO();
	   				Cliente cliente = dao.findByCPForTelefone(buscaTxt.getText());
	   				return cliente;
	   			}

	   			@Override
	   			protected void done() {
	   				try {
	   					Cliente cliente = get();
	   					if (cliente != null) {
	   						
	   						panelPagamento.add(buildPanelInfomraValor(), BorderLayout.SOUTH);
	   						panelValor.setVisible(true);
	   						framePagamento.pack();
	   						int bloco1h = (int) (cliente.getSaldo() / (configSis.getBloco()?configSis.getTarifa():(configSis.getTarifa() * 2)));
	   						JOptionPane.showMessageDialog(framePagamento, "Cliente : " + cliente.getNome() + "\nTelefone : " 
	   						+ cliente.getTelefone() + "\nSaldo : " + cliente.getSaldo() + " | Blocos de 1h : " + bloco1h, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
	   						
	   						pagamento = new PagamentoMensal();
	   						pagamento.setCpf(cliente.getCpf());
	   						
	   					} else {
	   						if(panelValor!= null) {
		   						panelValor.setVisible(false);
		   						}
	   						framePagamento.pack();
	   						JOptionPane.showMessageDialog(framePagamento, "Erro!! Cliente não encontrado", "Erro", JOptionPane.ERROR_MESSAGE);
	   						buscaTxt.setText("Cliente não encontrado");
	   					}
	   				} catch (InterruptedException | ExecutionException e) {
	   					JOptionPane.showMessageDialog(framePagamento, e, "Erro", JOptionPane.ERROR_MESSAGE);
	   				}
	   			}

	   		};

	   		worker.execute();
	   
	       });
	       
	    BuscarPanel.add(buscaLabel);
	    BuscarPanel.add(buscaTxt);
	    BuscarPanel.add(btnBuscar);
	    panelPagamento.add(BuscarPanel, BorderLayout.NORTH);
		
		return panelPagamento;
	}
	
	
	//  	   	----------------=Entrada e Saida=------------------
	
	private void entradaSaida(JPanel panel) {
		
		GridBagConstraints gbc;
		
		buttonEntradaSaida = new JButton("Entrada/Saida");
		buttonEntradaSaida.addActionListener(this::onEntradaSaidaClick);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 10;
		gbc.gridy = 7;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(10, 10, 10, 10);		
		panel.add(buttonEntradaSaida, gbc);
		
	}
	
	public void inserirPainelEntrada() {
		frameEntSaida.getContentPane().setLayout(new BorderLayout());
   		frameEntSaida.getContentPane().add(buildPanelEntrada());
   		frameEntSaida.pack();
	}
	
	public void inserirPainelSaida() {
		frameEntSaida.getContentPane().setLayout(new BorderLayout());
		frameEntSaida.getContentPane().add(buildPanelSaida());
		frameEntSaida.pack();
	}
	
	public void verificaEntradaSaida() {
		
		SwingWorker<Estacionamento, Void> worker = new SwingWorker<>() {
   			
   			@Override
   			protected Estacionamento doInBackground() throws DataAccessException {
   				EstacionamentoDAO dao = new EstacionamentoDAO();
   				Estacionamento estacionamento = dao.Consultar(stringPlacaEntrada);
   				return estacionamento;
   			}

   			@Override
   			protected void done() {
   				try {
   					Estacionamento estacionamento = get();
   					if(estacionamento != null) {
   						inserirPainelSaida();
					} else {
						if (vagasDisponiveis != 0) {
						inserirPainelEntrada();
						} else {
						JOptionPane.showMessageDialog(frameEntSaida, "ESTACIONAMENTO CHEIO!!!!", "Aviso", JOptionPane.WARNING_MESSAGE);
		   				statusVagas.setText("ESTACIONAMENTO CHEIO!!!!");
		   				statusVagas.setForeground(Color.RED);
		   				return;
						}
					}
   				} catch (InterruptedException | ExecutionException e) {
   					JOptionPane.showMessageDialog(frameEntSaida, e, "Erro", JOptionPane.ERROR_MESSAGE);
   				}
   			}

   		};

   		worker.execute();
		
	}
	
	private long permanenciaEstacionamento(EntradaVeiculo entrada) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		LocalDateTime entradaData = LocalDateTime.parse(entrada.getHorarioEntrada(), formatter);
		LocalDateTime saidaData = LocalDateTime.parse(saidaHorario, formatter);
		
		long diffInMinutes = java.time.Duration.between(entradaData, saidaData).toMinutes();
		
		return diffInMinutes;
	}
	
	private int qtdBlocos(EntradaVeiculo entrada) {
		
		int duracao;
		
		if(entrada.getDuracaoBloco().equalsIgnoreCase("1 hora")) {
			duracao = 60;
		} else {
			duracao = 30;
		}
		
		int nBlocos = (int) Math.round(((double)tempoPermanencia / duracao) + 0.5d);
		
		return nBlocos;
	}
	
	private double totalPagar(EntradaVeiculo entrada) {
		
		double total;
		
		total = qtdBlocos * entrada.getPrecoBloco();
		
		return total;
	}
	
	public void atualizaSaldoCliente() {
   			
   			ClienteDAO dao = new ClienteDAO();
   			try {
   				if(qtdBlocos < configSis.getQtd_hora_min()) {
					JOptionPane.showMessageDialog(framePagamento, "Cliente sem desconto :(", "Aviso", JOptionPane.WARNING_MESSAGE);
				} else {
					double porcentagem = Double.valueOf(configSis.getDesconto()) / 100;
					double desconto = 1.00 - porcentagem;
					precoTotal = precoTotal * desconto;
					JOptionPane.showMessageDialog(framePagamento, "Cliente com desconto :)\nDesconto : " + configSis.getDesconto() + "%\nValor Total : " + precoTotal, "Aviso", JOptionPane.WARNING_MESSAGE);
				}
				Cliente cliente = dao.consultarSaldo(stringPlacaEntrada);
				if(precoTotal <= cliente.getSaldo()) {
					dao.updateSaldo(precoTotal, cliente.getCpf());
					JOptionPane.showMessageDialog(frameEntSaida, "Saldo do Cliente " + cliente.getNome() + " Atualizado para " + (cliente.getSaldo() - precoTotal), "Sucesso", JOptionPane.DEFAULT_OPTION);
				} else {
					JOptionPane.showMessageDialog(frameEntSaida, "Saldo Insuficiente", "Erro", JOptionPane.ERROR_MESSAGE);
					
					
					
					framePagamento = new JFrame("Pagamento Mensal | Valor Minimo : " + precoTotal);
					
					framePagamento.getContentPane().setLayout(new BorderLayout());
					framePagamento.getContentPane().add(buildPanelInfomraValor(), BorderLayout.CENTER);
					framePagamento.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					framePagamento.pack();
					framePagamento.setLocationRelativeTo(null);
					framePagamento.setVisible(true);
					
					framePagamento.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							verificarOSaldo();
						}			
					});
					
					
					pagamento = new PagamentoMensal();
					pagamento.setCpf(cliente.getCpf());
					
				}
			} catch (DataAccessException e) {
				JOptionPane.showMessageDialog(frameEntSaida, e, "Erro", JOptionPane.ERROR_MESSAGE);
				return;
			}
		
	}
	
	//Botões Entrada e Saida
	private void onEntradaSaidaClick(ActionEvent e) {
		frameEntSaida = new JFrame("Entrada e Saida de Veiculos");
		
		//Entrada e Saida de Veiculos
		frameEntSaida.getContentPane().setLayout(new BorderLayout());
		frameEntSaida.getContentPane().add(buildPanelEntradaSaida(), BorderLayout.CENTER);
		placaLabelEntrada = new JLabel("Tipo Cliente");
		frameEntSaida.getContentPane().add(placaLabelEntrada, BorderLayout.NORTH);
		frameEntSaida.pack();
		frameEntSaida.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frameEntSaida.setLocationRelativeTo(null);
		frameEntSaida.setVisible(true);
	}
	
	public void onPlacaClick(ActionEvent e) {
		
		stringPlacaEntrada = placaEntradaTxt.getText();
		
		panelEntrada.removeAll();
		panelSaida.removeAll();
		panelEntrada.setVisible(false);
		panelSaida.setVisible(false);
		
		if(stringPlacaEntrada.isEmpty()) {
			JOptionPane.showMessageDialog(frameEntSaida, "Todos os campos devem ser preenchidos.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if (contadorCaracteres(stringPlacaEntrada)!=7) {
			JOptionPane.showMessageDialog(frameEntSaida, "Placa precisa ter 7 caracteres!", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		SwingWorker<Cliente, Void> worker = new SwingWorker<>() {
   			
   			@Override
   			protected Cliente doInBackground() throws DataAccessException {
   				ClienteDAO dao = new ClienteDAO();
   				Cliente cliente = dao.findByPlaca(stringPlacaEntrada);
   				return cliente;
   			}

   			@Override
   			protected void done() {
   				try {
   					Cliente cliente = get();
   						if (cliente != null) {
   							ClienteMensalista = true;
   							verificaEntradaSaida();
   							placaLabelEntrada.setText("Cliente Mensalista");
   						} else {
   							ClienteMensalista = false;
   							verificaEntradaSaida();
   							placaLabelEntrada.setText("Cliente Avulso");
   						}
   				} catch (InterruptedException | ExecutionException e) {
   					JOptionPane.showMessageDialog(frameEntSaida, e, "Erro", JOptionPane.ERROR_MESSAGE);
   				} 
   			}

   		};

   		worker.execute();

	}
	
	public void onSaidaClick(ActionEvent e) {
		
		tagPagamentoMensalSaida = true;
		
		SaidaVeiculo saida = new SaidaVeiculo();
		
		saida.setPlaca(placaSaida);
		saida.setHorarioSaida(saidaHorario);
		saida.setQtdBlocoDev(qtdBlocos);
		saida.setTotalPagar(precoTotal);
		
		SwingWorker<Void, Void> worker = new SwingWorker<>() {

			@Override
			protected Void doInBackground() throws DataAccessException {
				SaidaVeiculoDAO dao = new SaidaVeiculoDAO();
				if(ClienteMensalista == true) {
					atualizaSaldoCliente();
				}
				dao.Inserir(saida);				
				return null;
			}

			protected void done() {
				try {
					get();
					JOptionPane.showMessageDialog(frameEntSaida, "Saida Realizada com sucesso!"
													, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
				} catch (InterruptedException | ExecutionException e) {
					JOptionPane.showMessageDialog(frameEntSaida, e, "Erro", JOptionPane.ERROR_MESSAGE);
				}
			};
		};
		
		worker.execute();
		
		EstacionamentoDAO dao = new EstacionamentoDAO();
		try {
			dao.Deletar(saida);
			vagasDisponiveis = vagasDisponiveis + 1;
			statusVagas.setText("Vagas Disponiveis : " + vagasDisponiveis);
			statusVagas.setForeground(Color.BLACK);
		} catch (DataAccessException e1) {
			JOptionPane.showMessageDialog(frameEntSaida, e1, "Erro", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
			return;
		}
		
		panelSaida.setVisible(false);
		panelEntSaida.setPreferredSize(new Dimension(300,300));
	}
	
	public void onEntradaClick(ActionEvent e) {
		
		EntradaVeiculo entrada = new EntradaVeiculo();
		
		if (obsVeiculoTxt.getText().isEmpty()) {
			JOptionPane.showMessageDialog(frameEntSaida, "Todos os campos devem ser preenchidos.", "Aviso", JOptionPane.ERROR_MESSAGE);
			return;
		}

		entrada.setPlaca(stringPlacaEntrada);
		entrada.setDesVeiculo(obsVeiculoTxt.getText());
		entrada.setHorarioEntrada(entradaHorario);
		entrada.setDuracaoBloco(configSis.getBloco()?"1 hora":"1/2 hora");
		entrada.setPrecoBloco(configSis.getTarifa());
		
		SwingWorker<Void, Void> worker = new SwingWorker<>() {

			@Override
			protected Void doInBackground() throws DataAccessException {
				EntradaVeiculoDAO dao = new EntradaVeiculoDAO();
				dao.Inserir(entrada);				
				return null;
			}

			protected void done() {
				try {
					get();
					JOptionPane.showMessageDialog(frameEntSaida, "Entrada Realizada com sucesso!"
													, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
				} catch (InterruptedException | ExecutionException e) {
					JOptionPane.showMessageDialog(frameEntSaida, e, "Erro", JOptionPane.ERROR_MESSAGE);
				}
			};
		};
		
		worker.execute();
		
		EstacionamentoDAO dao = new EstacionamentoDAO();
		try {
			dao.Inserir(entrada);
			vagasDisponiveis = vagasDisponiveis - 1;
			if (vagasDisponiveis == 0) {
				JOptionPane.showMessageDialog(frameEntSaida, "ESTACIONAMENTO CHEIO!!!!", "Aviso", JOptionPane.WARNING_MESSAGE);
   				statusVagas.setText("ESTACIONAMENTO CHEIO!!!!");
   				statusVagas.setForeground(Color.RED);
			} else {
				statusVagas.setText("Vagas Disponiveis : " + vagasDisponiveis);
				statusVagas.setForeground(Color.BLACK);
			}
		} catch (DataAccessException e1) {
			JOptionPane.showMessageDialog(frameEntSaida, e1, "Erro", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
		
		panelEntrada.setVisible(false);
		panelEntSaida.setPreferredSize(new Dimension(300,300));
	}
	
	//

	//Paineis Entrada e Saida
	private JPanel buildPanelEntradaSaida() {
		
		panelEntrada = new JPanel();
		panelSaida = new JPanel();
		
		panelEntSaida = new JPanel();
		
		panelEntSaida.setLayout(new GridBagLayout());
		GridBagConstraints gbc;
		
		JLabel placaLabel = new JLabel("Insira a Placa do Veiculo :");
		
		JButton buttonPlaca = new JButton("Inserir");
		buttonPlaca.addActionListener(this::onPlacaClick);
		
		placaEntradaTxt = new JTextField(20);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(10, 5, 0, 10);
		panelEntSaida.add(placaLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 5, 0, 10);
		panelEntSaida.add(placaEntradaTxt, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(10, 5, 0, 10);
		panelEntSaida.add(buttonPlaca, gbc);
		
		return panelEntSaida;
	}
	
	private JPanel buildPanelEntrada() {
		
		panelEntrada.setVisible(true);
		panelSaida.setVisible(false);
		
		panelEntrada.setLayout(new GridBagLayout());
		GridBagConstraints gbc;
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		entradaHorario = dtf.format(LocalDateTime.now());
		
		horarioEntradaLabel = new JLabel("Horário de entrada : " + entradaHorario);
		
		if(configSis.getBloco() == true) {
			duracaoBlocoLabel = new JLabel("Duração do bloco de cobranca : Bloco 1 hora");
		}
		
		if(configSis.getBloco() == false) {
			duracaoBlocoLabel = new JLabel("Duração do bloco de cobranca : Bloco 1/2 hora");
		}
		
		precoBlocoLabel = new JLabel("Preço por bloco de cobrança : " + configSis.getTarifa());
		
		JLabel obsVeiculoLabel = new JLabel("Descrição do veículo");
		obsVeiculoTxt = new JTextField(30);
		
		buttonRegistrarEntrada = new JButton("Registrar Entrada");
		buttonRegistrarEntrada.addActionListener(this::onEntradaClick);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(10, 5, 0, 10);
		panelEntrada.add(horarioEntradaLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(10, 5, 0, 10);
		panelEntrada.add(duracaoBlocoLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(10, 5, 0, 10);
		panelEntrada.add(precoBlocoLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(10, 5, 0, 10);
		panelEntrada.add(obsVeiculoLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(0, 5, 10, 10);
		panelEntrada.add(obsVeiculoTxt, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(0, 5, 10, 10);
		panelEntrada.add(buttonRegistrarEntrada, gbc);
		
		panelEntrada.setPreferredSize(new Dimension(500, 500));
		frameEntSaida.pack();
		return panelEntrada;
	}
	
	private JPanel buildPanelSaida() {
		
		panelSaida.setVisible(true);
		panelEntrada.setVisible(false);
		
		panelSaida.setLayout(new GridBagLayout());
		GridBagConstraints gbc;
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		saidaHorario = dtf.format(LocalDateTime.now());
		
		SwingWorker<EntradaVeiculo, Void> worker = new SwingWorker<>() {
   			
   			@Override
   			protected EntradaVeiculo doInBackground() throws DataAccessException {
   				EntradaVeiculoDAO dao = new EntradaVeiculoDAO();
   				EntradaVeiculo entrada = dao.resgatarDados(stringPlacaEntrada);
   				return entrada;
   			}

   			@Override
   			protected void done() {
   				try {
   					EntradaVeiculo entrada = get();
   					
   					tempoPermanencia = permanenciaEstacionamento(entrada);
   					
   					qtdBlocos = qtdBlocos(entrada);
   					
   					precoTotal = totalPagar(entrada);
   					
   					placaSaida = entrada.getPlaca();
   					
   					tempoLabel.setText("O tempo de permanência no estacionamento (em minutos) : " + tempoPermanencia + "m");
   					qtdBlocoLabel.setText("A quantidade de blocos de cobrança devida : " + qtdBlocos);
   					totalLabel.setText("Total a pagar : " + precoTotal);
   					horarioEntradaLabel.setText("Horário de entrada : " + entrada.getHorarioEntrada());
   					duracaoBlocoLabel.setText("Duração do bloco de cobranca : " + entrada.getDuracaoBloco());
   					precoBlocoLabel.setText("Preço por bloco de cobrança : " + entrada.getPrecoBloco());
   					
   				} catch (InterruptedException | ExecutionException e) {
   					JOptionPane.showMessageDialog(frameEntSaida, e, "Erro", JOptionPane.ERROR_MESSAGE);
   				} 
   			}

   		};

   		worker.execute();
   		
   		horarioEntradaLabel = new JLabel("");
		duracaoBlocoLabel = new JLabel("");
		precoBlocoLabel = new JLabel("");
		
		horarioSaidaLabel = new JLabel("Horário de saída : " + saidaHorario);
		tempoLabel = new JLabel("");
		qtdBlocoLabel = new JLabel("");
		totalLabel = new JLabel("");
		
		buttonRegistrarSaida = new JButton("Registrar Saida");
		buttonRegistrarSaida.addActionListener(this::onSaidaClick);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(10, 5, 0, 10);
		panelSaida.add(horarioEntradaLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(10, 5, 0, 10);
		panelSaida.add(horarioSaidaLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(10, 5, 0, 10);
		panelSaida.add(tempoLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(10, 5, 0, 10);
		panelSaida.add(duracaoBlocoLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(10, 5, 0, 10);
		panelSaida.add(precoBlocoLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(10, 5, 0, 10);
		panelSaida.add(qtdBlocoLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(10, 5, 0, 10);
		panelSaida.add(totalLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(10, 5, 0, 10);
		panelSaida.add(buttonRegistrarSaida, gbc);
		
		panelSaida.setPreferredSize(new Dimension(500, 500));
		frameEntSaida.pack();
		
		return panelSaida;
	}
	
	//			--------=END ENTRADA E SAIDA=--------
	
	
	//			-----------=Gerenciar Veiculo=--------------
	
	private void gerenciarVeiculo(JPanel panel) {
		
		GridBagConstraints gbc;
		
		buttonGereVei = new JButton("Gerenciar Veiculos");
		buttonGereVei.addActionListener(this::onGerenciarClick);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 10;
		gbc.gridy = 8;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(10, 10, 10, 10);		
		panel.add(buttonGereVei, gbc);
		
	}
	
	//Botões Gerenciar Veiculo
	
	private void onGerenciarClick(ActionEvent e) {
		frameGVeiculo  = new JFrame("Gerenciar Veiculos");
		
		frameGVeiculo.getContentPane().setLayout(new BorderLayout());
		frameGVeiculo.getContentPane().add(buildPanelGerenciarVeiculo(), BorderLayout.CENTER);
		frameGVeiculo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frameGVeiculo.pack();
		frameGVeiculo.setLocationRelativeTo(null);
		frameGVeiculo.setVisible(true);
	}
	//
	
	//Paineis Gerenciar Veiculo
	private JPanel buildPanelGerenciarVeiculo() {
		panelGerenciar = new JPanel();
		panelGerenciar.setLayout(new BorderLayout());
		
		JPanel BuscarPanel = new JPanel(new FlowLayout());
		
		JLabel buscaLabel = new JLabel("Buscar pelo CPF ou Telefone: ");
		JTextField buscaTxt = new JTextField(30);
		
		veiculo = new Veiculo();
		
		JButton btnBuscar = new JButton("Buscar");
	       btnBuscar.addActionListener((e) -> {
	    	   
	    	String stringBusca = buscaTxt.getText();
	   		
	   		SwingWorker<Cliente, Void> worker = new SwingWorker<>() {
	   			
	   			@Override
	   			protected Cliente doInBackground() throws DataAccessException {
	   				ClienteDAO dao = new ClienteDAO();
	   				Cliente cliente = dao.findByCPForTelefone(stringBusca);
	   				return cliente;
	   			}

	   			@Override
	   			protected void done() {
	   				try {
	   					Cliente cliente = get();
	   					if (cliente != null) {
	   						panelGerenciar.add(buildPanelInserirVeiculosBusca(), BorderLayout.SOUTH);
	   						panelInserir.setVisible(true);
	   						frameGVeiculo.pack();
	   						veiculo.setCpf_dono(cliente.getCpf());
	   						veiculo.setTelefone_dono(cliente.getTelefone());
	   						int bloco1h = (int) (cliente.getSaldo() / (configSis.getBloco()?configSis.getTarifa():(configSis.getTarifa() * 2)));
	   						JOptionPane.showMessageDialog(frameGVeiculo, "Cliente : " + cliente.getNome() + "\nTelefone : " 
	   						+ cliente.getTelefone() + "\nSaldo : " + cliente.getSaldo() + " | Blocos de 1h : " + bloco1h, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
	   					} else {
	   						if(panelInserir != null) {
	   						panelInserir.setVisible(false);
	   						}
	   						frameGVeiculo.pack();
	   						JOptionPane.showMessageDialog(frameGVeiculo, "Erro!! Cliente não encontrado", "Erro", JOptionPane.ERROR_MESSAGE);
	   						buscaTxt.setText("Cliente não encontrado");
	   					}
	   				} catch (InterruptedException | ExecutionException e) {
	   					JOptionPane.showMessageDialog(frameGVeiculo, e, "Erro", JOptionPane.ERROR_MESSAGE);
	   				}
	   			}

	   		};

	   		worker.execute();
	   
	       });
	       
	    BuscarPanel.add(buscaLabel);
	    BuscarPanel.add(buscaTxt);
	    BuscarPanel.add(btnBuscar);
	    panelGerenciar.add(BuscarPanel, BorderLayout.NORTH);

		return panelGerenciar;
	}
	
	private JPanel buildPanelInserirVeiculosBusca() {
		panelInserir = new JPanel();
		
		JLabel placaLabel = new JLabel("Insira a placa : ");
		placaTxt = new JTextField(20);
		
		JLabel obsVeiculoLabel = new JLabel("Descrição do Veiculo : ");
		obsVeiculoTxt = new JTextField(50);
		
		JButton btnInserirVeiculo = new JButton("Inserir Veiculo");
		btnInserirVeiculo.addActionListener((e) -> {
			
			veiculo.setPlaca(placaTxt.getText());
			veiculo.setObsVeiculo(obsVeiculoTxt.getText());
			
			if (contadorCaracteres(placaTxt.getText())!=7) {
				JOptionPane.showMessageDialog(frameGVeiculo, "Placa precisa ter 7 caracteres!", "Aviso", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			if (veiculo.getObsVeiculo().isEmpty() || veiculo.getPlaca().isEmpty()) {
				JOptionPane.showMessageDialog(frameGVeiculo, "Todos os campos devem ser preenchidos.", "Aviso", JOptionPane.ERROR_MESSAGE);
				return;
			}
           
			SwingWorker<Void, Void> worker = new SwingWorker<>() {

				@Override
				protected Void doInBackground() throws DataAccessException {
					VeiculoDAO dao = new VeiculoDAO();
					dao.InserirVeiculo(veiculo);
					return null;
				}

				protected void done() {
					try {
						panelInserir.setVisible(false);
						frameGVeiculo.pack();
						get();
						JOptionPane.showMessageDialog(frameGVeiculo, "Veiculo Inserido com Sucesso!!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
					} catch (InterruptedException | ExecutionException e) {
						JOptionPane.showMessageDialog(frameGVeiculo, e, "Erro", JOptionPane.ERROR_MESSAGE);
					}
				};
			};
			
			worker.execute();
    	   
       });
		
		panelInserir.setLayout(new GridBagLayout());
		GridBagConstraints gbc;
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(10, 10, 10, 10);		
		panelInserir.add(placaLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 10, 10, 10);		
		panelInserir.add(placaTxt, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(10, 10, 10, 10);		
		panelInserir.add(obsVeiculoLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(10, 10, 10, 10);		
		panelInserir.add(obsVeiculoTxt, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(10, 10, 10, 10);		
		panelInserir.add(btnInserirVeiculo, gbc);
		
		return panelInserir;
	}
	
	private void GerenciarVeiculo(JPanel panel) {
		
		JLabel placaLabel = new JLabel("Insira a placa : ");
		placaTxt = new JTextField(20);
		
		JLabel obsVeiculoLabel = new JLabel("Descrição do Veiculo : ");
		obsVeiculoTxt = new JTextField(50);
		
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc;
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(10, 5, 0, 10);
		panel.add(placaLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 5, 0, 10);
		panel.add(placaTxt, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(10, 5, 0, 10);
		panel.add(obsVeiculoLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 5, 0, 10);
		panel.add(obsVeiculoTxt, gbc);
		
	}
	
	private JPanel buildPanelInserirVeiculo() {
		JPanel panelInserirVeiculo = new JPanel();
		
		GerenciarVeiculo(panelInserirVeiculo);
		
		JButton btnInserirVeiculo = new JButton("Inserir Veiculos para o Mensalista Cadastrado!!");
		btnInserirVeiculo.addActionListener((e) -> {
			
			if (contadorCaracteres(placaTxt.getText())!=7) {
				JOptionPane.showMessageDialog(frameVeiculo, "Placa precisa ter 7 caracteres!", "Aviso", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			Veiculo veiculo = new Veiculo();
			
			veiculo.setCpf_dono(stringCpf);
			veiculo.setPlaca(placaTxt.getText());
			veiculo.setObsVeiculo(obsVeiculoTxt.getText());
			veiculo.setTelefone_dono(stringTelefone);
			
			if (veiculo.getCpf_dono().isEmpty()|| veiculo.getObsVeiculo().isEmpty() || veiculo.getPlaca().isEmpty() || veiculo.getTelefone_dono().isEmpty()) {
				JOptionPane.showMessageDialog(frameVeiculo, "Todos os campos devem ser preenchidos.", "Aviso", JOptionPane.WARNING_MESSAGE);
				return;
			}
           
			SwingWorker<Void, Void> worker = new SwingWorker<>() {

				@Override
				protected Void doInBackground() throws DataAccessException {
					VeiculoDAO dao = new VeiculoDAO();
					dao.InserirVeiculo(veiculo);
					return null;
				}

				protected void done() {
					try {
						get();
						JOptionPane.showMessageDialog(frameVeiculo, "Veiculo Inserido com Sucesso!!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
					} catch (InterruptedException | ExecutionException e) {
						JOptionPane.showMessageDialog(frameVeiculo, e, "Erro", JOptionPane.ERROR_MESSAGE);
					}
				};
			};
			
			worker.execute();
    	   
       });
		
		panelInserirVeiculo.setLayout(new GridBagLayout());
		GridBagConstraints gbc;
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 3;
		gbc.gridheight = 3;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(10, 5, 10, 10);
		panelInserirVeiculo.add(btnInserirVeiculo, gbc);
		
		
		
		return panelInserirVeiculo;
	}
	//
	
	//			-------=END Gerenciar Veiculo=--------
	
	
	//			--------=PREVIA CONFIGURAR SISTEMA(nao sera usado por enquanto)=--------
	
	
	//Botões Configurar Sistema
	private void onConfigClick(ActionEvent e) {
		
		System.out.println("-=Configuraçao do Sistema=-");
		System.out.println("Bloco : " + configSis.getBloco());
		System.out.println("Tarifa : R$" + configSis.getTarifa());
		System.out.println("Desconto : " + configSis.getDesconto() + "%");
		System.out.println("Quantidade de Hora Minima : " + configSis.getQtd_hora_min() + "h");
		
		frameConfig = new JFrame("Configurar Sistema");
		
		frameConfig.getContentPane().setLayout(new BorderLayout());
		frameConfig.getContentPane().add(buildPanelConfig(), BorderLayout.CENTER);
		frameConfig.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frameConfig.pack();
		frameConfig.setLocationRelativeTo(null);
		frameConfig.setVisible(true);
		
	}
	
	private void onAtualizarClick(ActionEvent e) {
		if(tagFirstExecute) {
			if(radio1hora.isSelected() == false && radio30min.isSelected() == false) {
				JOptionPane.showMessageDialog(frameConfig, "Selecione algum bloco!!", "Erro!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(tarifaTxt.getText().isEmpty() || descontoTxt.getText().isEmpty() || qtdTxt.getText().isEmpty()) {
				JOptionPane.showMessageDialog(frameConfig, "Todos os campos devem ser preenchidos!!", "Erro!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(!tarifaTxt.getText().matches("[0-9]+.[0-9]+") || !qtdTxt.getText().matches("[0-9]+") || !descontoTxt.getText().matches("[0-9]+")) {
				JOptionPane.showMessageDialog(frameConfig, "Informe apenas numeros!!\nTarifa exemplo de formato: R$3.0", "Erro!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			ConfiguracaoDAO configDAO = new ConfiguracaoDAO();
			ConfiguracaoSistema config = new ConfiguracaoSistema();
			
			if(radio1hora.isSelected()) {
				config.setBloco(true);
			}
			if(radio30min.isSelected()) {
				config.setBloco(false);
			}
			
			double tarifa = Double.parseDouble(tarifaTxt.getText());
			config.setTarifa(tarifa);
			int desconto = Integer.parseInt(descontoTxt.getText());
			config.setDesconto(desconto);
			int horaMin = Integer.parseInt(qtdTxt.getText());
			config.setQtd_hora_min(horaMin);
			
			try {
				configDAO.Inserir(config);
				frameConfig.dispose();
				inserirTelaPrincipal();
			} catch (DataAccessException e1) {
				JOptionPane.showMessageDialog(frameConfig, e1, "Erro!", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
			
		} else {
			
			if(!tarifaTxt.getText().matches("[0-9]+.[0-9]+") && !tarifaTxt.getText().isEmpty()) {
				JOptionPane.showMessageDialog(frameConfig, "Informe numeros no formato correto em tarifa | Ex: R$3.0", "Atenção!", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			if(!descontoTxt.getText().matches("[0-9]+") && !descontoTxt.getText().isEmpty()) {
				JOptionPane.showMessageDialog(frameConfig, "Informe apenas numeros em desconto", "Atenção!", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			if(!qtdTxt.getText().matches("[0-9]+") && !qtdTxt.getText().isEmpty()) {
				JOptionPane.showMessageDialog(frameConfig, "Informe apenas numeros em horas minimas", "Atenção!", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			ConfiguracaoDAO configDAO = new ConfiguracaoDAO();
			
			if(radio1hora.isSelected()) {
				try {
					configDAO.updateBloco(true);
				} catch (DataAccessException e1) {
					JOptionPane.showMessageDialog(frameConfig, e1, "Erro!! Não foi possivel atualizar o bloco", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
			
			if(radio30min.isSelected()) {
				try {
					configDAO.updateBloco(false);
				} catch (DataAccessException e1) {
					JOptionPane.showMessageDialog(frameConfig, e1, "Erro!! Não foi possivel atualizar o bloco", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
			
			if(!tarifaTxt.getText().isEmpty()) {
				double tarifa = Double.parseDouble(tarifaTxt.getText());
				try {
					configDAO.updateTarifa(tarifa);
				} catch (DataAccessException e1) {
					JOptionPane.showMessageDialog(frameConfig, e1, "Erro!! Não foi possivel atualizar a tarifa", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
			
			if(!descontoTxt.getText().isEmpty()) {
				int desconto = Integer.parseInt(descontoTxt.getText());
				try {
					configDAO.updateDesconto(desconto);
				} catch (DataAccessException e1) {
					JOptionPane.showMessageDialog(frameConfig, e1, "Erro!! Não foi possivel atualizar o desconto", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
			
			if(!qtdTxt.getText().isEmpty()) {
			    int horaMin = Integer.parseInt(qtdTxt.getText());
			    try {
					configDAO.updateHoraMin(horaMin);
				} catch (DataAccessException e1) {
					JOptionPane.showMessageDialog(frameConfig, e1, "Erro!! Não foi possivel atualizar a quantidade de hora minima", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
			
			try {
				configDAO.Configurar(configSis);
			} catch (DataAccessException e1) {
				JOptionPane.showMessageDialog(frameConfig, e1, "Erro ao atualizar no sistema!!", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
			
			JOptionPane.showMessageDialog(frameConfig, "Informações atualizadas", "Sucesso", JOptionPane.DEFAULT_OPTION);
			
		}
		
		
		
	}
	
	//
	
	//Paineis Configurar Sistema
	private JPanel buildPanelConfig() {
		JPanel panelConfig = new JPanel();
		
		panelConfig.setLayout(new GridBagLayout());
		GridBagConstraints gbc;
		
		//Bloco
		JLabel blocoLabel = new JLabel("Bloco :");
		
		radio1hora = new JRadioButton("1 Hora");
		radio30min = new JRadioButton("1/2 Hora");
		
		ButtonGroup group = new ButtonGroup();
		group.add(radio1hora);
		group.add(radio30min);
		//
		
		//Tarifa
		JLabel tarifaLabel = new JLabel("Tarifa :");
		
		tarifaTxt = new JTextField(20);
		
		//Desconto
		JLabel descontoLabel = new JLabel("Desconto :");
		JLabel porcentagemLabel = new JLabel("%");
		
		descontoTxt = new JTextField(20);
		//
		
		//Quantidade de Horas Minima para mensalistas ganharem desconto
		JLabel qtdLabel = new JLabel("Quantidade Horas Minima :");
		JLabel horasLabel = new JLabel("Hora(s)");
		
		qtdTxt = new JTextField(20);
		//
		
		//Bloco
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(10, 5, 0, 10);
		panelConfig.add(blocoLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 0;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(10, 5, 0, 10);
		panelConfig.add(radio1hora, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 0;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(10, 5, 0, 10);
		panelConfig.add(radio30min, gbc);
		//
		
		//Tarifa
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(10, 5, 0, 10);
		panelConfig.add(tarifaLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 5, 0, 10);
		panelConfig.add(tarifaTxt, gbc);
		//
		
		//Desconto
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(10, 5, 0, 10);
		panelConfig.add(descontoLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 5, 0, 10);
		panelConfig.add(descontoTxt, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(10, 1, 1, 1);
		panelConfig.add(porcentagemLabel, gbc);
		//
		
		//Quantidade de Horas Minima para mensalistas ganharem desconto
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(10, 5, 0, 10);
		panelConfig.add(qtdLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 5, 0, 10);
		panelConfig.add(qtdTxt, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(10, 0, 0, 10);
		panelConfig.add(horasLabel, gbc);
		//
		
		JButton buttonAtualizar = new JButton("Atualizar");
		buttonAtualizar.addActionListener(this::onAtualizarClick);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(10, 5, 10, 10);
		panelConfig.add(buttonAtualizar, gbc);
		
		return panelConfig;
	}
	
	//
	
	//			------=END CONFIGURAR SISTEMA=-------
	
	// contador de caracteres
	public int contadorCaracteres(String frase) {
		return frase.length();
	}
	
	//EXECUTAR PROGRAMA
	
	public static void main(String[] args) {
		ParksysApp pkApp = new ParksysApp();
		pkApp.createAndShowGUI();
	}
}
