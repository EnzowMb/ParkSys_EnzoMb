# ParkSys_EnzoMb
<h1>Um sistema para operação de estacionamentos.</h1>
<h2>Neste projeto, foi desenvolvido um sistema de operação de estacionamentos que:</h2>
<ul>
  <li>Registra o tempo de permanência de veículos em um estacionamento</li>
  <li>Calcula tarifas</li>
  <li>Gera dados de apoio à decisão, na forma de gráficos</li>
</ul>
<h2>Video de apresentação do sistema! ✌</h2>
https://www.youtube.com/watch?v=fbFtwT13gfA&ab_channel=ENZOMARTINELLIBRUNOZI
<hr>
<h2>Visão Geral</h2>
O estacionamento atende clientes avulsos e mensalistas. A cobrança é realizada por blocos de tempo com duração de 1 (uma) ou de 1/2 (meia) hora. O usuário pode configurar a duração dos blocos de tempo, assim como o valor da tarifa cobrada por bloco — R$ 2,00 a cada bloco de meia hora, por exemplo.
<br>
O sistema possue interface gráfica com o usuário (GUI), baseada em janelas e foi desenvolvido na linguagem Java, por meio do framework Swing. Os dados de operação do estacionamento foram armazenados em um banco de dados relacional, gerenciado pelo sistema MySQL.
<br>
A interface do sistema é composta por uma tela principal, que da acesso a telas auxiliares conforme a função escolhida pelo usuário. Por exemplo, o usuário pode selecionar a opção cadastrar cliente na tela principal e o sistema apresentará uma tela auxiliar de cadastro.
<hr>
<h2>Exemplos de Gráficos</h2>
![1](https://github.com/EnzowMb/ParkSys_EnzoMb/assets/89809584/e9c90c3d-450a-4a1c-ba1d-426a57e65bc3)
<br>
Gráfico de faturamento diário.
<br>
<br>
![image](https://github.com/EnzowMb/ParkSys_EnzoMb/assets/89809584/0f9dfcbf-9f8f-48f7-8b27-6817f92f0132)
<br>
Gráfico de tempo médio de permanência por dia da semana.
