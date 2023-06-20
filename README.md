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

### Cadastrar valor mensal total: 

- O responsável entraria no sistema e adicionaria um valor mensal de recompensa para cada criança cadastrada, podendo também simplesmente editá-lo ou remove-lo.

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

## Modelo implementado dos pontos no storypoint
- 1/2: até 2 horas
- 1: até 4 horas
- 2: até 8 horas
- 3: até 2 dias
- 5: até 3 dias
- 8: até 1 semana
- 13: até 2 semanas
- 20: mais de 2 semanas 
