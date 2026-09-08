# AEDs III - TP01

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

## Prints do Projeto: 

## Estrutura do projeto
### `Main.java` — classe principal que inicia a aplicação.
### `/entidades` — classes de domínio do sistema.
* **Usuario**: Define o modelo do usuário. Usa `toByteArray()` e `fromByteArray()` para serialização.
### `/arquivos` — arquivos específicos para usuários e relacionamento.
* **ArquivoUsuario.java**: CRUD para Usuário com índice indireto baseado em Tabela Hash.
* **ParIdEmail**: Tabela Hash responsável pela associação entre e-mails e IDs de usuários.
### `/aed3` — classes genéricas de CRUD, hash extensível e árvore B+.
* **Arquivo.java**: Classe base utilizada pelos CRUDs para gerenciamento dos arquivos de dados.

### `/tela` — menus e telas da interface textual.
* **MenuUser**: Tela de login, cadastro e gerenciamento de usuario.
### `/auxiliar` — utilitários auxiliares, como leitura de entrada.
* **Leitura.java**: Centraliza a leitura do `System.in`.

## operações especiais que foram implementadas

### Há um CRUD de usuários (que estende a classe Arquivo, acrescentando Tabelas Hash Extensíveis e Árvores B+ como índices diretos e indiretos conforme necessidade) que funciona corretamente?

### Há um CRUD de perguntas (que estende a classe Arquivo, acrescentando Tabelas Hash Extensíveis e Árvores B+ como índices diretos e indiretos conforme necessidade) que funciona corretamente?

### As perguntas estão vinculadas aos usuários usando o idUsuario como chave estrangeira?

### Há uma árvore B+ que registre o relacionamento 1:N entre usuários e perguntas?

### O trabalho compila corretamente?

### O trabalho está completo e funcionando sem erros de execução?

### O trabalho é original e não a cópia de um trabalho de outro grupo?

## Como rodar
### Opção 1: executar diretamente no VS Code / IntelliJ
- Abra o projeto na IDE.
- Execute a classe `TP1/src/Main.java`.

### Opção 2: compilar e executar no terminal
A partir da pasta raiz do projeto:

```powershell
cd TP1
javac -d bin (Get-ChildItem -Recurse -Filter *.java).FullName
java -cp bin Main
```

Se preferir, pode também abrir o terminal dentro da pasta `TP1` e rodar os mesmos comandos.

## Observações
- O sistema usa arquivos locais para armazenar os dados dos usuários e das perguntas.
- As perguntas não são removidas do sistema; elas são arquivadas, preservando o histórico e os vínculos com outras entidades.
- O relacionamento entre usuário e pergunta é feito por meio de `idUsuario` e da árvore B+ para consultas por usuário.

## Objetivo do TP
O objetivo principal deste trabalho foi aplicar os conceitos de armazenamento em arquivos, CRUD, indexação indireta e relacionamento entre entidades em um sistema funcional de perguntas e respostas.