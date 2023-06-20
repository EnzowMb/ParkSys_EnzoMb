
/* Conjunto de instruções INSERT */

use parksys;

INSERT INTO cliente VALUES ('63743526171', 'Enzo Martinelli', '16988234212', 'Estuda DSI', 3.00),('12345678910', 'Gustavo Pires', '16988764532', 'Estuda DSI', 20.00);

INSERT INTO Veiculo VALUES ('63743526171', '16988234212', 'ABC1234', 'Corsa 2006 com frigobar Brastemp(esse é bom)'), ('12345678910', '16988764532', 'BDC9876', '');

INSERT INTO EntradaVeiculo VALUES ('HJC3456', 'Celta Vermelho', '2021-12-02 11:00:00', '1 hora', 3.00), ('DEF5678', 'Corolla Prata', '2021-12-03 11:00:00', '1 hora', 3.00), 
('XYZ0123', 'Fiat Uno', '2021-12-02 11:00:00', '1 hora', 3.00);

INSERT INTO SaidaVeiculo VALUES('HJC3456', '2021-12-02 14:00:00', 3, 9.00), ('DEF5678', '2021-12-03 16:00:00', 5, 15), ('XYZ0123', '2021-12-02 16:30:00', 6, 18.00);

INSERT INTO Configurar VALUES(TRUE, 3.00, 25, 1);