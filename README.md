# NHS API

```
Projeto de Conclusão de Curso apresentado
como requisito parcial para obtenção do grau
Tecnólogo em Análise e Desenvolvimento de
Sistemas, pela Faculdade de Tecnologia de
Americana.
Time: 
RAMON LACAVA GUTIERREZ GONÇALES
LEONARDO MARTINS DE OLIVEIRA
NATÁLIA AKINA UESUGI
Orientador: Prof. Dr. Kleber de Oliveira Andrade
Área de concentração: Engenharia de Software

Americana, SP
2018


Este trabalho se trata de uma plataforma de saúde digital unificada, que permite melhor gestão de informações de saúde e processos inteligentes. 
A plataforma visa auxiliar o dia a dia das instituições de saúde e pacientes, buscando diminuir a quantidade de erros na área médica através de uma coleção consistente de dados do paciente, possibilitando que o sistema atue em qualquer instituição de saúde, e que tenha uma melhor eficiência e eficácia em atendimentos, sejam estes comuns ou de urgência e emergência.
O trabalho foi realizado em colaboração com a universidade de Durban, na África do Sul. 
A metodologia empregada durante o decorrer do trabalho foi o SCRUM, que visa a transparência, dinamicidade e agrega valor ao produto final. 
Foram desenvolvidos dois aplicativos para dispositivos móveis e um para computador, que realizam gestão de exames, diagnósticos, dados de saúde, medicamentos, instituições, médicos(as), dentre outros. 
Diversos requisitos foram coletados de forma dinâmica com as equipes sul africanas para possibilitar a integração dos sistemas tanto no Brasil quanto na África do Sul.
Todo o desenvolvimento do sistema se voltou para agregar valor aos processos e interfaces de usuário (se focando em facilidade de uso e experiencia de usuário).
Os resultados foram dois aplicativos publicados na Google Play e um sistema computadorizado, sendo que os três estão em fase de testes na África do Sul.
Conclui-se que o sistema poderá auxiliar muito no ambiente da saúde, facilitando a gestão, fornecendo processos inteligentes e uma maior agilidade no atendimento, permitindo com que pacientes possuam acesso a seus dados de saúde, e com que funcionários de saúde possuam uma maior facilidade e uma maior quantidade de dados relevantes para análise durante os atendimentos.
```


##### Palavras Chave: Saúde; Sistema; Internacionalização; Prontuário Médico; NFC; Arduino; Android; Java

## Recursos e Ferramentas
<p>Esta se&ccedil;&atilde;o contempla as ferramentas de programa&ccedil;&atilde;o e os conceitos necess&aacute;rios para o desenvolvimento do NFC Health System.</p>
<ul>
<li><strong>Android Studio</strong>: ambiente de desenvolvimento integrado (IDE) oficial para o desenvolvimento de aplicativos Android, baseado no InteliJ IDEA<a href="#_ftn1" name="_ftnref1">[1]</a>, que oferece um ambiente unificado para o desenvolvimento de aplicativos. No qual &eacute; poss&iacute;vel desenvolver, fazer debugs, testes e interfaces para smartphones e tablets Android e dispositivos Android Wear, Android TV e Android Auto (ANDROID, 2017).</li>
<li><strong>Eclipse</strong>: &Eacute; uma plataforma aberta que facilita o processo de desenvolvimento, fornecendo ferramentas para codifica&ccedil;&atilde;o, constru&ccedil;&atilde;o, execu&ccedil;&atilde;o e <em>debug</em> de aplica&ccedil;&otilde;es (UMBC, 2008). Esta ferramenta suporta o desenvolvimento em diversas linguagens e foi utilizada para o desenvolvimento dos <em>webservices</em> do sistema.</li>
<li><strong>SQL Server Management Studio</strong>: O SSMS &eacute; um ambiente integrado que &eacute; utilizado para gerenciamento da infraestrutura de SQL, atuando do SQL Server para o Banco de dados SQL do Azure. Fornece ferramentas para configurar, monitorar e gerenciar as inst&acirc;ncias do SQL, e para implantar, monitorar e atualizar componentes da camada de dados (Microsoft, 2018).</li>
<li><strong>Netbeans</strong>: Permite que de forma r&aacute;pida e f&aacute;cil seja desenvolvido aplica&ccedil;&otilde;es em Java <em>destkop</em>, <em>mobile</em>, <em>web</em>, assim como aplica&ccedil;&otilde;es HTML5, JavaScript e CSS. A IDE tamb&eacute;m fornece boas ferramentas para PHP, C e C++. &Eacute; uma plataforma de c&oacute;digo aberto com uma vasta comunidade de usu&aacute;rios e desenvolvedores (Netbeans, 2018). &Eacute; utilizado para o desenvolvimento do sistema <em>desktop</em>.</li>
<li><strong><em>NFC (Near Field Communication)</em></strong>: Tecnologia de curto alcance, dist&acirc;ncia de cerca de 4 a 20 cent&iacute;metros (TORRES, 2008), alta frequ&ecirc;ncia e baixa largura de banda, permitindo a comunica&ccedil;&atilde;o sem fio atrav&eacute;s de dois dispositivos habilitados com NFC (COSKUN, 2011).</li>
<li><strong>Adesivos <em>NFC</em></strong>: Equipamentos de acesso utilizados, comumente conhecidos como <em>INLAY</em>, possuem apenas <em>DIE </em>(pastilha de sil&iacute;cio onde se encontra o circuito do <em>NFC</em>). Ao adicionar um encapsulamento pl&aacute;stico ao <em>INLAY</em>, o resultado &eacute; uma <em>tag</em> com maior durabilidade e resist&ecirc;ncia, porem esta possui um custo maior do que as que n&atilde;o est&atilde;o encapsuladas (CUNHA, 2016).</li>
<li><strong>M&oacute;dulo leitor <em>RFID NFC PN532</em></strong>: O PN532 &eacute; um m&oacute;dulo de alta integra&ccedil;&atilde;o para comunica&ccedil;&atilde;o sem contato que atua na frequ&ecirc;ncia de 13.56 MHz. O modulo de transmiss&atilde;o utiliza um conceito de modula&ccedil;&atilde;o e demodula&ccedil;&atilde;o completamente integrados para diferentes tipos de comunica&ccedil;&atilde;o. O m&oacute;dulo suporta tanto leitura quanto escrita e funciona em uma dist&acirc;ncia de at&eacute; 7 cent&iacute;metros. &Eacute; utilizado para a leitura dos equipamentos de acesso dos pacientes.</li>
<li><strong>Arduino</strong>: O Arduino &eacute; um placa eletr&ocirc;nica de c&oacute;digo aberto criado pelo time Massimo Banzi, David Cuartilles, Tom Igoe, Gianluca Martino, e David Mellis com o objetivo de desenvolver hardwares micro controladores de f&aacute;cil uso que estaria dispon&iacute;vel a todos (BARRETT, 2013).</li>
<li><strong><em>REST</em></strong>: <em>REST</em> (<em>Representational State Transfer</em>) define um conjunto de princ&iacute;pios arquiteturais os quais podem ser utilizados para projetar <em>web services</em> focados em recursos do sistema, incluindo como os estados dos recursos s&atilde;o endere&ccedil;ados e transferidos atrav&eacute;s do protocolo HTTP<a href="#_ftn2" name="_ftnref2">[2]</a> por uma ampla variedade de clientes escritos em diferentes linguagens (RODRIGUEZ, 2008). Tamb&eacute;m pode ser definido como um estilo h&iacute;brido derivado de v&aacute;rios estilos de arquitetura baseados em rede e combinado com restri&ccedil;&otilde;es adicionais que definem uma interface de conex&atilde;o uniforme (FIELDING, 2000). Este conjunto de princ&iacute;pios arquiteturais foi escolhido devido &agrave; grande popularidade que vem ganhando em meio aos desenvolvedores, juntamente a diversas implementa&ccedil;&otilde;es nas mais variadas linguagens de programa&ccedil;&atilde;o e bibliotecas (RODRIGUEZ, 2008).</li>
<li><strong>Android</strong>: &Eacute; um Sistema Operacional para dispositivos m&oacute;veis que foi criado com base em Linux de c&oacute;digo aberto (ANDROID DEVELOPERS, 2018). Atualmente, o Android pode ser encontrado em diversos dispositivos, como smartphones, rel&oacute;gios, TVs e at&eacute; mesmo carros. De acordo com a pesquisa realizada pelo jornal EL P&Aacute;IS em Abril de 2017, o Android vem cada vez mais crescendo seus n&uacute;meros de usu&aacute;rios, fazendo com que seja ultrapassado o n&uacute;mero de usu&aacute;rios de Windows (MENDIOLA, 2017).</li>
<li><strong>Java</strong>: &Eacute; uma tecnologia utilizada para desenvolvimento de aplica&ccedil;&otilde;es, que podem ser <em>Web</em>, <em>Desktop</em> ou at&eacute; mesmo <em>mobile</em>, sendo orientada a objetos, compilada e interpretada. Atualmente o Java &eacute; uma das tecnologias mais utilizadas do mundo, que de acordo com a Oracle, cerca de 97% dos <em>Desktops</em> executam Java e 89% dos <em>desktops</em> nos Estados Unidos tamb&eacute;m o executa. Esses n&uacute;meros se d&atilde;o ao fato de que essa tecnologia foi projetada para permitir o desenvolvimento de aplicativos port&aacute;teis de alto desempenho e para que se possa abranger todas as plataformas poss&iacute;veis (ORACLE, 2018).</li>
<li><strong>C</strong><em>: </em>&Eacute; uma linguagem de m&eacute;dio n&iacute;vel (permitindo manipula&ccedil;&atilde;o de bits, bytes e endere&ccedil;os), de grande portabilidade , estruturada e compilada (ou seja, o c&oacute;digo em C &eacute; traduzido em um <em>c&oacute;digo-objeto </em>que pode ser entendido pelo computador) e muito utilizado na programa&ccedil;&atilde;o de sistemas operacionais, compiladores, gerenciadores de banco de dados, dentre diversas outras aplica&ccedil;&otilde;es (SCHILDT, 1997). A linguagem foi utilizada para o desenvolvimento do sistema em Arduino.</li>
<li><strong>Armazenamento em nuvem</strong>: Armazenamento em nuvem &eacute; a disponibiliza&ccedil;&atilde;o de capacidade de armazenamento em hardwares remotos (data-centers<a href="#_ftn3" name="_ftnref3">[3]</a>), fazendo assim que n&atilde;o exista mais a necessidade do gerenciamento de hardware para armazenamento de dados(DRAGO et al., 2012).Existem muitas empresas que prov&ecirc;m armazenamento em nuvem como: Microsoft, Google, Amazon, dentre outros (DRAGO et al., 2012). Para este trabalho de gradua&ccedil;&atilde;o estaremos utilizando a Microsoft Azure para a utiliza&ccedil;&atilde;o do servi&ccedil;o de armazenamento em nuvem.</li>
<li><strong>Azure</strong>: &Eacute; uma plataforma em na nuvem para hospedar aplicativos e simplificar o processo de desenvolvimento. A Microsoft Azure integra diversos servi&ccedil;os de nuvem muito uteis para desenvolver, testar, implantar e gerenciar aplicativos, tudo aproveitando a computa&ccedil;&atilde;o em nuvem (Microsoft, 2017).</li>
<li><strong>SQL Server</strong>: &Eacute; uma das partes centrais da plataforma de dados da Microsoft, sendo l&iacute;der em sistemas de gerenciamento de banco de dados operacionais. &Eacute; um sistema de gerenciamento de banco de dados relacional altamente escal&aacute;vel (Quackit, 2018).</li>
<li><strong>Apache Tomcat</strong>: &Eacute; um sistema de c&oacute;digo aberto que implementa diversas tecnologias, tais como Java Servlet, Java Server Pages, dentre outras (Apache Tomcat, 2018). Ela &eacute; utilizada para o desenvolvimento dos <em>webservices</em>.</li>
<li><strong>RabbitMQ</strong>: &Eacute; um intermedi&aacute;rio de mensagens constru&iacute;do em Erlang, que implementa o protocolo AMQP, respons&aacute;vel por aceitar e encaminhar mensagens. Basicamente, um produtor envia uma mensagem, que atrav&eacute;s de uma &aacute;rea de troca (<em>Exchange</em>) &eacute; encaminhada a uma fila, onde esta mensagem dever&aacute; ser consumida por um consumidor (RabbitMQ, 2018). &Eacute; utilizado para gerenciamento das filas de atendimento dos(as) enfermeiros(as).</li>
<li><strong>CloudAMQP</strong>: &Eacute; um servi&ccedil;o que gerencia os servidores que executam RabbitMQ na nuvem (CloudAMQP, 2018).</li>
<li><strong>Maven</strong>: &Eacute; uma ferramenta de gest&atilde;o de projetos e se baseia no conceito de projeto de objeto de modelo (<em>POM</em>). O maven pode gerenciar a cria&ccedil;&atilde;o, relat&oacute;rio e documenta&ccedil;&atilde;o de um projeto a partir de um centro de informa&ccedil;&otilde;es (Apache Maven, 2018). &Eacute; a ferramenta utilizada para gest&atilde;o dos projetos desenvolvidos em Java.</li>
<li><strong>Pusher Channels</strong>: Fornece comunica&ccedil;&atilde;o em tempo real entre servidor, aplicativo e dispositivos, sendo usado para notifica&ccedil;&otilde;es, chats, jogos, <em>web</em>, internet das coisas, e outros sistemas que utilizam comunica&ccedil;&atilde;o em tempo real (Pusher, 2018).</li>
<li><strong>Astah UML</strong><em>: </em>&Eacute; uma ferramenta que suporta os requerimentos da UML 2.x para constru&ccedil;&atilde;o de diagramas de classe, caso de uso, sequencia, m&aacute;quina de estado, atividade, componente, dentre outros (ASTAH, 2018). &Eacute; a ferramenta utilizada para a constru&ccedil;&atilde;o dos diagramas de classe e caso de uso.</li>
<li><strong>Fritzing</strong><em>: </em>&Eacute; um sistema para automa&ccedil;&atilde;o de design eletr&ocirc;nico, com o objetivo de fornecer ferramentas f&aacute;ceis para documentar e compartilhar projetos de computa&ccedil;&atilde;o f&iacute;sica (FRITZING, 2018). &Eacute; a ferramenta utilizada para constru&ccedil;&atilde;o do circuito do sistema Arduino.</li>
<li><strong>Arduino <em>IDE</em></strong>: Esta ferramenta de c&oacute;digo aberto faz com que seja f&aacute;cil desenvolver o c&oacute;digo e realizar o <em>upload</em> para a placa utilizada (Arduino, 2018). &Eacute; a ferramenta utilizada para o desenvolvimento do sistema do Arduino.</li>
</ul>
<p>&nbsp;</p>
<p><a href="#_ftnref1" name="_ftn1">[1]</a> Intellij IDEA &eacute; um JAVA IDE da empresa JetBrains. Dispon&iacute;vel em: &lt;https://www.jetbrains.com/idea&gt;.</p>
<p><a href="#_ftnref2" name="_ftn2">[2]</a> O HTTP (<em>Hypertext Transfer Protocol)</em> &eacute; um protocolo no n&iacute;vel de aplica&ccedil;&atilde;o para informa&ccedil;&otilde;es distribu&iacute;das, colaborativas e de hiperm&iacute;dia</p>
<p><a href="#_ftnref3" name="_ftn3">[3]</a> Centrais de armazenamento de dados</p>


## API de usuários

<p><a name="_Toc530233485"></a>Tabela - Recursos da <em>API</em> de usu&aacute;rio</p>
<table>
<tbody>
<tr>
<td colspan="4" width="604">
<p><strong>Recursos disponibilizados pela <em>API</em> de usu&aacute;rios</strong></p>
</td>
</tr>
<tr>
<td width="222">
<p><strong>Endere&ccedil;o</strong></p>
</td>
<td width="66">
<p><strong>M&eacute;todo</strong></p>
</td>
<td width="61">
<p><strong>Autentica&ccedil;&atilde;o</strong></p>
</td>
<td width="255">
<p><strong>Descri&ccedil;&atilde;o</strong></p>
</td>
</tr>
<tr>
<td width="222">
<p>/user</p>
</td>
<td width="66">
<p>POST</p>
</td>
<td width="61">
<p>Sim</p>
</td>
<td width="255">
<p>Adiciona um usu&aacute;rio</p>
</td>
</tr>
<tr>
<td width="222">
<p>/user/checkUserStatus</p>
</td>
<td width="66">
<p>POST</p>
</td>
<td width="61">
<p>N&atilde;o</p>
</td>
<td width="255">
<p>Checa se um usu&aacute;rio j&aacute; est&aacute; cadastrado</p>
</td>
</tr>
<tr>
<td width="222">
<p>/user</p>
</td>
<td width="66">
<p>PUT</p>
</td>
<td width="61">
<p>Sim</p>
</td>
<td width="255">
<p>Atualiza um usu&aacute;rio</p>
</td>
</tr>
<tr>
<td width="222">
<p>/user/updateUserType</p>
</td>
<td width="66">
<p>PUT</p>
</td>
<td width="61">
<p>Sim</p>
</td>
<td width="255">
<p>Atualiza o tipo de usu&aacute;rio (ex: paciente passa a ser m&eacute;dico).</p>
</td>
</tr>
<tr>
<td width="222">
<p>/user</p>
</td>
<td width="66">
<p>DELETE</p>
</td>
<td width="61">
<p>Sim</p>
</td>
<td width="255">
<p>Exclui um usu&aacute;rio</p>
</td>
</tr>
<tr>
<td width="222">
<p>/user/{idUser}</p>
</td>
<td width="66">
<p>GET</p>
</td>
<td width="61">
<p>Sim</p>
</td>
<td width="255">
<p>Obt&eacute;m os dados do usu&aacute;rio</p>
</td>
</tr>
<tr>
<td width="222">
<p>/user/secretCode</p>
</td>
<td width="66">
<p>GET</p>
</td>
<td width="61">
<p>Sim</p>
</td>
<td width="255">
<p>Obt&eacute;m o c&oacute;digo secreto do usu&aacute;rio.</p>
</td>
</tr>
<tr>
<td width="222">
<p>/user/search/{login}</p>
</td>
<td width="66">
<p>GET</p>
</td>
<td width="61">
<p>Sim</p>
</td>
<td width="255">
<p>Obtem os dados do usu&aacute;rio a partir do login.</p>
</td>
</tr>
<tr>
<td width="222">
<p>/user/auth</p>
</td>
<td width="66">
<p>POST</p>
</td>
<td width="61">
<p>N&atilde;o</p>
</td>
<td width="255">
<p>Autentica o usu&aacute;rio em rela&ccedil;&atilde;o as apis que possuem autentica&ccedil;&atilde;o.</p>
</td>
</tr>
<tr>
<td width="222">
<p>/user/healthinstitutionbind/{idUser}</p>
</td>
<td width="66">
<p>GET</p>
</td>
<td width="61">
<p>Sim</p>
</td>
<td width="255">
<p>Obt&eacute;m as institui&ccedil;&otilde;es de sa&uacute;de que o usu&aacute;rio est&aacute; vinculado.</p>
</td>
</tr>
<tr>
<td width="222">
<p>/user/sendPasswordMail</p>
</td>
<td width="66">
<p>POST</p>
</td>
<td width="61">
<p>Sim</p>
</td>
<td width="255">
<p>Envia um e-mail com login e senha para o usu&aacute;rio rec&eacute;m-criado.</p>
</td>
</tr>
<tr>
<td width="222">
<p>/user/uploadImage</p>
</td>
<td width="66">
<p>PUT</p>
</td>
<td width="61">
<p>Sim</p>
</td>
<td width="255">
<p>Atualiza a imagem do usu&aacute;rio.</p>
</td>
</tr>
<tr>
<td width="222">
<p>/user/image</p>
</td>
<td width="66">
<p>GET</p>
</td>
<td width="61">
<p>N&atilde;o</p>
</td>
<td width="255">
<p>Obt&eacute;m a imagem do usu&aacute;rio.</p>
</td>
</tr>
</tbody>
</table>
<p><strong>Fonte: Elaborado pelo autor</strong></p>

## Licença

    Copyright 2019 
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.

