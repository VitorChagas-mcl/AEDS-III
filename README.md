# AEDs III - TP03

## Participantes
- Hector Faria Braz de Carvalho
- João Paulo da Silva
- Vitor Hugo Chagas Maciel
- Gabriel Lima Emerique Caldeira

## Descrição do trabalho
Este projeto consiste em implementar um sistema simplificado de perguntas e respostas, inspirado no funcionamento do StackOverflow, com foco na persistência de dados em arquivos e no uso de estruturas de indexação como Tabelas Hash Extensíveis e Árvores B+.

O sistema desenvolvido permite:
- cadastro de usuários;
- login com email e senha;
- recuperação de senha por meio de pergunta e resposta secreta;
- gerenciamento dos dados do usuário;
- cadastro, listagem, alteração e arquivamento de perguntas;
- relacionamento 1:N entre usuário e pergunta, mantendo a integridade dos dados.

A aplicação foi organizada em camadas para separar:
- entidades (`entidades`): `Usuario` e `Pergunta`;
- arquivos e índices (`aed3`, `arquivos`): estruturas de CRUD, hash extensível e árvore B+;
- interfaces de interação (`tela`): menus e fluxos do sistema;
- ponto de entrada (`Main.java`): execução da aplicação.

## Estrutura do projeto
- `TP1/src/Main.java` — classe principal que inicia a aplicação.
- `TP1/src/entidades/` — classes de domínio do sistema.
- `TP1/src/arquivos/` — arquivos específicos para usuários e relacionamento.
- `TP1/src/aed3/` — classes genéricas de CRUD, hash extensível e árvore B+.
- `TP1/src/tela/` — menus e telas da interface textual.
- `TP1/src/auxiliar/` — utilitários auxiliares, como leitura de entrada.

## Como rodar
### Opção 1: executar diretamente no VS Code / IntelliJ
- Abra o projeto na IDE.
- Execute a classe `TP1/src/Main.java`.

### Opção 2: compilar e executar no terminal
A partir da pasta raiz do projeto:

```powershell
cd TP1
javac -d out (Get-ChildItem -Recurse -Filter *.java).FullName
java -cp out Main
```

Se preferir, pode também abrir o terminal dentro da pasta `TP1` e rodar os mesmos comandos.

## Observações
- O sistema usa arquivos locais para armazenar os dados dos usuários e das perguntas.
- As perguntas não são removidas do sistema; elas são arquivadas, preservando o histórico e os vínculos com outras entidades.
- O relacionamento entre usuário e pergunta é feito por meio de `idUsuario` e da árvore B+ para consultas por usuário.

## Objetivo do TP
O objetivo principal deste trabalho foi aplicar os conceitos de armazenamento em arquivos, CRUD, indexação indireta e relacionamento entre entidades em um sistema funcional de perguntas e respostas.