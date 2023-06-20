/*
Armazene neste arquivo o _schema_ do seu banco de dados (CREATE TABLE, etc.).
*/

CREATE DATABASE  IF NOT EXISTS `parksys`;

use parksys;

CREATE TABLE Cliente (
	cpf varchar(11) primary key,
	nome varchar(40) not null,
    telefone varchar(11) not null,
    obs varchar(100),
    saldo decimal(11, 2) not null
);

CREATE TABLE PagamentoMensal (
	cpf varchar(11) not null,
	dataPagamento date not null,
    valorPago decimal(11, 2) not null,
    saldoHoras int not null
);

CREATE TABLE Veiculo (
	cpf varchar(11),
    telefone varchar(11),
    placa varchar(7) primary key,
    obsVeiculo varchar(100)
);

CREATE TABLE EntradaVeiculo (
	placa varchar(7),
    desVeiculo varchar(100),
    horarioEntrada datetime not null, -- Trabalhar com datetime não esta dando certo
    duracaoBloco varchar(10) not null,
    precoBloco decimal(11, 2) not null
);

CREATE TABLE Estacionamento (
	placa varchar(7) primary key
);

CREATE TABLE SaidaVeiculo (
	placa varchar(7),
    horarioSaida datetime not null, -- Trabalhar com datetime não esta dando certo
    qtdBlocos int not null,
    precoTotal decimal(11, 2) not null
);

CREATE TABLE Configurar (
	bloco boolean, -- True: 1h | False: 1/2h
	tarifa decimal(11, 2),
    desconto int,
    qtd_hora int
);

alter table Estacionamento add constraint
foreign key placa_fk (placa) references EntradaVeiculo (placa);

alter table Estacionamento add constraint
foreign key placa_fk (placa) references SaidaVeiculo (placa);

alter table SaidaVeiculo add constraint
foreign key placa_fk (placa) references EntradaVeiculo (placa);

alter table Veiculo add constraint
foreign key cpf_fk (cpf) references Cliente (cpf);

alter table Veiculo add constraint
foreign key telefone_fk (telefone) references Cliente (telefone);

alter table PagamentoMensal add constraint
foreign key cpf_fk (cpf) references Cliente (cpf);