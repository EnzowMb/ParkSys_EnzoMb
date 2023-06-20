
/* Consultas usadas no sistema */

use parksys;

-- Consultar Clientes
select * from cliente;


-- Consultar Veiculos
select * from Veiculo;


-- Consultar entradas de veiculos
select * from EntradaVeiculo;


-- Consultar saida de veiculos
select * from SaidaVeiculo;


-- Consultar veiculos no estacionamento
select * from Estacionamento;


-- Consultar historico de pagamentos
select * from pagamentoMensal;


-- Consultar configuração sistema
select * from Configurar;


SET SQL_SAFE_UPDATES=0;


-- Deletar Clientes
DELETE FROM Cliente;


-- Deletar Veiculos
DELETE FROM Veiculo;


-- Deletar historico de entrada de veiculos
DELETE FROM entradaveiculo;


-- Deletar historico de saida de veiculos
DELETE FROM saidaveiculo;


-- Deletar carro do estacionamento / Atualizar a vaga do estacionamento
DELETE FROM estacionamento;


-- Deletar historico de pagamentos
DELETE FROM pagamentomensal;


-- Deletar Configuração pradão do Sistema
DELETE FROM Configurar;


--     Consultas Usadas no Sistema OBS: valores usados como exemplo das instruções do INSERT!


select cpf, nome, telefone, saldo from cliente 
where cpf = '12345678910';


select cpf, nome, telefone, saldo from cliente 
where telefone = '16988764532';


select c.cpf from cliente c
inner join veiculo v on c.cpf = v.cpf
where v.placa = 'BDC9876';


select c.saldo from cliente c
inner join veiculo v on c.cpf = v.cpf
where v.placa = 'ABC1234';


-- select count(*) as vagas from EntradaVeiculo;
select count(*) as vagas from Estacionamento;


select placa, desVeiculo, convert(horarioEntrada, char) as data, duracaoBloco, precoBloco
from EntradaVeiculo
where placa = 'DEF5678'
order by convert(horarioEntrada, char) desc;


select placa from Estacionamento where placa = 'DEF5678';


select COUNT(*) as existe from Configurar;


select bloco, tarifa, desconto, qtd_hora from configurar;


update configurar
set bloco = true;


update configurar
set tarifa = 3.00;


update configurar
set desconto = 25;


update configurar
set qtd_hora = 1;



-- Graficos

-- Grafico Permanencia
select dayname(s.horarioSaida), s.horarioSaida, e.horarioEntrada 
from SaidaVeiculo s inner join EntradaVeiculo e on s.placa = e.placa
where s.horarioSaida between STR_TO_DATE( "01/12/21", "%d/%m/%y" ) and STR_TO_DATE( "31/12/21", "%d/%m/%y" );

-- Grafico Faturamento
select date_format(horarioSaida, "%d/%m/%y") as dataSaida, sum(precoTotal)
from saidaveiculo 
where horarioSaida between STR_TO_DATE( "01/12/21", "%d/%m/%y" ) and STR_TO_DATE( "31/12/21", "%d/%m/%y" )
group by dataSaida;