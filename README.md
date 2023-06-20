# Projeto de Modelagem de Sistemas
Modelar uma API REST para um Sistema de Gestão de Recompensa para Tarefas de Crianças e Implementar o modelo de classe de domínio

## O que faz?

O sistema de gerenciamento de tarefas de crianças, faz o gerenciamento de atividades (realizado pelo responsável), que é atribuída à crianças, no qual cada atividade tem seu peso, que será proporcional (%) ao valor total a receber pela criança. A criança poderá acompanhar as atividades que lhe foram atribuídas juntamente ao valores ganhos. 

## Como faz?

### Cadastro de responsável:

- O responsável se cadastra na plataforma através do nome e-mail e senha. O reponsável poderá acessar a plataforma por meio de login e senha após efetuar o primeiro cadastro.

### Cadastro de criança:

- O responsável cadastra a criança através de nome completo e idade. Da mesma maneira, esta poderia ser editada e excluída. A criança poderá acessar a plataforma por meio de login e senha após o responsável efetuar o seu cadastro.

### Cadastrar valor mensal total: 

- O responsável entraria no sistema, efetuaria o login e adicionaria um valor mensal de recompensa para cada criança cadastrada, podendo também simplesmente editá-lo ou remove-lo.

### Cadastrar atividade:

- O responsável entraria no sistema, efetuaria o login e criaria a atividade, inserindo o nome, peso em porcentagem e descrição. Da mesma maneira, esta poderia ser editada e excluída. O responsável poderia fazer a confirmação da atividade como realizada no sistema.

### Atribuir bônus: 

- O responsável no decorrer do mês de tarefas, poderá adicionar um valor bônus que será somado ao valor mensal total, mas que estará registrado como bônus.

### Atribuir penalização:

- O responsável no decorrer do mês de tarefas poderá retirar um valor que será subtraído do valor mensal total, mas que será registrado como penalização.

### Visualização da tarefas:

- A criança entraria no sistema, efetuaria o login e poderia visualizar as tarefas planejadas pelo responsável.      

### Visualização da recompensa ganha:

- A criança entraria no sistema, efetuaria o login e poderá visualizar o valor ganho pelas atividades realizadas.

- A criança poderá visualizar as tarefas planejadas pelo responsável.      

## Atores:

- Responsável:
    - cadastrar-se

    - cadastrar criança

    - criar tarefas

    - excluir tarefas

    - editar tarefas

    - atribuir pesos as tarefas

    - atribuir valor total mensal

    - Adicionais:

        - criar valor bônus (adicionar um valor ao total);

        - retirar valores do total, representando um ato disciplinar;

## Criança:

- consultar valores ganhos

- consultar lista de tarefas e seus pesos

## Como rodar o projeto?

### Requistos de sistema:
Kit de desenvovimento do java instalado (JDK)
O projeto foi feito com java 17 e Spring boot 3.1.0

### Passos
- Baixar o repositório na máquina.
  
      URL: https://github.com/Matheus-Juliao/Projeto-Modelagem-De-Sistemas
  
- Abrir a pasta api-rest em alguma IDE de sua escolha.
  
      Sugestão: Intellij.
  
- Configurar o banco de dados:
  
      Entre na pasta da pasta api-rest -> src -> main -> resources 
      Abra o arquivo application.properties.
      Configure o nome e senha do seu banco de dados. Nesse projeto utilizamos o PostgresSQL.
  
- Após configurar o banco de dados coloque para rodar o servidor local.
- Na pasta raiz do reposítorio baixado existe uma pasta chamada postman, onde estão todas as request configuradas. Utilize-a para fazer testes.
  OBS: Na url http://localhost:8080/swagger-ui/index.html#/ está configurado o swaager, que é uma documentação de apoio para realizar as requests necessárias.
