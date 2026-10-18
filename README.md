# AEDs III - TP02

## Participantes

- Hector Faria Braz de Carvalho
- João Paulo da Silva
- Vitor Hugo Chagas Maciel
- Gabriel Lima Emerique Caldeira

## Descrição do trabalho

O TP2 dá continuidade ao sistema de perguntas e respostas do TP1. Nesta etapa, o sistema foi ampliado com listagem paginada de perguntas recentes, operações de respostas e avaliações, e índices B+ para relacionar os registros.

As funcionalidades acrescentadas ou modificadas no TP2 incluem:

- listagem das perguntas ativas em ordem decrescente de data, com cinco perguntas por página;
- visualização das respostas de uma pergunta, ordenadas por nota decrescente;
- cadastro de uma resposta por um usuário autenticado;
- arquivamento lógico da própria resposta;
- avaliação positiva ou negativa de respostas de outros usuários, com atualização da nota e da quantidade de avaliações;
- registro da avaliação por usuário e resposta, impedindo que o mesmo usuário avalie a mesma resposta mais de uma vez;
- índices B+ para localizar perguntas recentes e para relacionar respostas a perguntas e usuários.

Na área do usuário, é possível listar e arquivar as próprias respostas, consultar as avaliações realizadas e cancelá-las, além de selecionar uma pergunta própria para ver e arquivar respostas de outros usuários. A busca por palavras-chave ainda não está implementada.

A aplicação foi organizada em camadas. As classes adicionadas ou alteradas no TP2 incluem:

- entidades (`TP2/src/entidades`): novas classes `Resposta` e `Avaliacao`; alteração de `Pergunta`;
- arquivos e índices (`TP2/src/arquivos` e `TP2/src/aed3`): novas classes `ArquivoResposta`, `ArquivoAvaliacao` e `ParIdPerguntaData`; alterações em `ArquivoPergunta` e `ParIdEmail`;
- interfaces e execução (`TP2/src/tela` e `TP2/src`): novo `MenuResposta`, alterações em `MenuPergunta` e `Main`.

## Prints do Projeto

Não foram encontradas capturas de tela específicas do TP2 no repositório.

## Estrutura das alterações do TP2

```text
TP2/
└── src/
    ├── Main.java                         (alterado)
    ├── aed3/
    │   ├── ParIdEmail.java               (alterado)
    │   └── ParIdPerguntaData.java        (novo)
    ├── arquivos/
    │   ├── ArquivoAvaliacao.java         (novo)
    │   ├── ArquivoPergunta.java          (alterado)
    │   └── ArquivoResposta.java          (novo)
    ├── entidades/
    │   ├── Avaliacao.java                (novo)
    │   ├── Pergunta.java                 (alterado)
    │   └── Resposta.java                 (novo)
    └── tela/
        ├── MenuPergunta.java             (alterado)
        └── MenuResposta.java             (novo)
```

## Descrição dos principais arquivos alterados ou adicionados

### `TP2/src/tela/MenuResposta.java`

Implementa o fluxo de perguntas recentes: pagina os resultados em grupos de cinco, permite abrir uma pergunta, mostra as respostas ativas em ordem decrescente de nota e permite enviar uma resposta. Na visualização de uma resposta, o autor pode arquivá-la; outros usuários podem avaliá-la positiva ou negativamente.

### `TP2/src/arquivos/ArquivoPergunta.java`

Acrescenta o índice B+ de perguntas por data e o método `readPerguntasRecentes`, que lê as entradas do índice, desconsidera perguntas arquivadas e retorna os registros em ordem decrescente de data. A interface aplica a paginação sobre essa lista.

### `TP2/src/aed3/ParIdPerguntaData.java`

Novo registro do índice B+, comparado pela data em ordem decrescente e, em caso de empate, pelo ID da pergunta em ordem decrescente.

### `TP2/src/entidades/Resposta.java` e `TP2/src/arquivos/ArquivoResposta.java`

Representam e armazenam respostas. `ArquivoResposta` mantém índices B+ por pergunta e por usuário, permite consultar respostas por esses relacionamentos e atualiza os índices quando um relacionamento muda. O arquivamento feito pela interface é lógico (`ativa = false`).

### `TP2/src/entidades/Avaliacao.java` e `TP2/src/arquivos/ArquivoAvaliacao.java`

Representam e armazenam a avaliação de um usuário sobre uma resposta, indicando voto positivo ou negativo. `ArquivoAvaliacao` impede avaliações duplicadas do mesmo usuário para a mesma resposta. Ao registrar um voto, `MenuResposta` atualiza a nota e a quantidade de avaliações da resposta.

### `TP2/src/Main.java`

Altera a opção “Buscar perguntas” para iniciar o novo `MenuResposta`.

### `TP2/src/aed3/ParIdEmail.java`

Altera a serialização da chave do índice hash extensível para preencher o espaço reservado ao email, mantendo o tamanho fixo do registro.

### `TP2/src/tela/MenuPergunta.java` e `TP2/src/entidades/Pergunta.java`

Alterações de apresentação das perguntas e de suas listagens na área de perguntas.

## Operações especiais que foram implementadas

### Há uma listagem paginada de perguntas, que mostra as perguntas em ordem decrescente de data a partir de uma Árvore B+ acrescentada ao CRUD de Perguntas?

Sim. `ArquivoPergunta.readPerguntasRecentes` lê os registros pelo índice B+ de data, filtra as perguntas ativas e ordena por data decrescente. `MenuResposta` apresenta cinco perguntas por página, com navegação para as páginas anterior e seguinte.

### Há uma listagem das respostas a uma pergunta selecionada, em ordem decrescente de nota?

Sim. Ao abrir uma pergunta, `MenuResposta` consulta as respostas relacionadas, mostra apenas as ativas e as ordena por nota decrescente; em caso de empate, usa a data de criação decrescente.

### O usuário pode responder a uma pergunta?

Sim. A partir da visualização de uma pergunta, o usuário pode cadastrar uma resposta não vazia.

### O usuário pode arquivar a própria resposta?

Sim. Na visualização de uma resposta escrita pelo usuário autenticado, a opção de arquivamento altera seu estado para inativo.

### O usuário pode avaliar uma resposta?

Sim. O usuário pode concordar ou discordar de uma resposta de outra pessoa. A avaliação é registrada e a nota e a quantidade de avaliações da resposta são atualizadas. O autor não pode avaliar a própria resposta, e cada usuário pode avaliar cada resposta uma única vez.

### Na área do usuário, o usuário pode visualizar as respostas às suas perguntas?

Sim. Em “Minhas perguntas”, o usuário seleciona uma pergunta para visualizar as respostas ativas de outros usuários.

### Na área do usuário, o usuário pode arquivar uma resposta de outro usuário a uma das suas perguntas?

Sim. Na visualização das respostas de uma pergunta própria, o usuário pode selecionar e arquivar respostas ativas escritas por outras pessoas.

### Na área do usuário, o usuário pode visualizar e arquivar as respostas que escreveu?

Sim. Em “Minhas respostas”, o usuário vê suas respostas, seleciona uma para visualizar a pergunta e o texto completo da resposta e pode arquivá-la.

### Na área do usuário, o usuário pode visualizar e arquivar a avaliação que realizou em uma resposta?

Sim, conforme o cancelamento descrito no enunciado. “Minhas avaliações” mostra a pergunta, a resposta e o voto realizado; ao cancelar, remove o registro e ajusta a nota e a quantidade de avaliações da resposta. O cancelamento é uma exclusão, não um arquivamento lógico.

### A integridade do relacionamento entre perguntas e respostas está mantida em todas as operações?

Sim. O CRUD de respostas valida a existência e o estado ativo da pergunta ao criar uma resposta, e não permite alterar a pergunta relacionada após a criação. Ao excluir uma pergunta, suas respostas são excluídas em cascata, junto dos índices correspondentes.

### A integridade do relacionamento entre respostas e avaliações está mantida em todas as operações?

Sim. O CRUD de avaliações valida a existência de usuário e resposta, impede autoavaliação e votos duplicados, e não permite alterar uma avaliação existente. Criar ou cancelar uma avaliação ajusta a nota e a quantidade armazenadas na resposta. Ao excluir uma resposta, suas avaliações são excluídas em cascata.

### O trabalho está completo e funcionando sem erros de execução?

Sim. O projeto foi validado com o comando abaixo e compilou corretamente:

```bash
cd TP2
javac -d bin $(find src -name '*.java')
```

### O trabalho é original e não a cópia de um trabalho de outro grupo?

Sim. O projeto foi desenvolvido inteiramente pelo grupo e é totalmente original.

## Como rodar

### Opção 1: executar diretamente no VS Code / IntelliJ

- Abra a pasta `TP2` na IDE.
- Execute a classe `src/Main.java`.

### Opção 2: compilar e executar no terminal

No Linux/macOS, a partir da raiz do projeto:

```bash
cd TP2
javac -d bin $(find src -name '*.java')
java -cp bin Main
```

## Vídeo do projeto

Não foi encontrado um vídeo específico do TP2 no repositório.

## Observações

- A listagem de perguntas recentes percorre as entradas do índice B+ e aplica a ordenação decrescente antes de paginar.
- A busca por palavras-chave ainda exibe uma mensagem informando que não foi implementada.
- “Minhas respostas” e “Minhas avaliações” permitem consultar e gerenciar os registros do usuário.
- O CRUD permite excluir respostas, mas essa exclusão não remove automaticamente as avaliações associadas.

## Objetivo do TP2

O objetivo desta etapa é ampliar o sistema com perguntas recentes paginadas, respostas e avaliações. Foram implementados o fluxo de listagem e navegação de perguntas, a consulta e ordenação de respostas, o envio e arquivamento de respostas próprias e de respostas recebidas nas próprias perguntas, e o registro e cancelamento de avaliações. A busca por palavras-chave ainda precisa ser implementada.
