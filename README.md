# AEDs III - TP03

## Participantes

- Hector Faria Braz de Carvalho
- João Paulo da Silva
- Vitor Hugo Chagas Maciel
- Gabriel Lima Emerique Caldeira

## Descrição do trabalho

O TP3 dá continuidade ao sistema de perguntas e respostas desenvolvido nos trabalhos anteriores. Nesta etapa, o foco foi a pesquisa textual por palavras-chave, com a implementação de um índice invertido sobre as perguntas cadastradas e a ordenação dos resultados por relevância.

As principais melhorias introduzidas no TP3 incluem:

- busca de perguntas por palavras-chave informadas pelo usuário;
- normalização de termos para comparação textual, removendo acentos e padronizando letras minúsculas;
- exclusão de palavras muito comuns (stop words) no processo de indexação;
- armazenamento invertido de frequências por termo e por pergunta;
- cálculo de pontuação por relevância para ordenar os resultados da busca;
- atualização automática do índice ao criar, alterar ou arquivar perguntas;
- reconstrução do índice invertido quando ele estiver inconsistente.

A aplicação continua organizada em camadas, mantendo a estrutura de usuários, perguntas, respostas e menus já desenvolvidos nos TPs anteriores. A principal novidade é o uso de `ListaInvertida` para consultar perguntas por termos de busca.

## Estrutura do projeto

```text
TP3/
└── src/
    ├── Main.java
    ├── aed3/
    │   ├── Arquivo.java
    │   ├── ArvoreBMais.java
    │   ├── ElementoLista.java
    │   ├── HashExtensivel.java
    │   ├── InterfaceArvoreBMais.java
    │   ├── InterfaceHashExtensivel.java
    │   ├── InterfaceRegistro.java
    │   ├── ListaInvertida.java
    │   ├── ParIDEndereco.java
    │   ├── ParIdEmail.java
    │   ├── ParIdId.java
    │   ├── ParIdPerguntaData.java
    │   └── ParIdUsuarioPergunta.java
    ├── arquivos/
    │   ├── ArquivoAvaliacao.java
    │   ├── ArquivoPergunta.java
    │   ├── ArquivoResposta.java
    │   └── ArquivoUsuario.java
    ├── entidades/
    │   ├── Avaliacao.java
    │   ├── Pergunta.java
    │   ├── Resposta.java
    │   └── Usuario.java
    ├── tela/
    │   ├── MenuPergunta.java
    │   ├── MenuResposta.java
    │   └── MenuUser.java
    └── auxiliar/
        └── Leitura.java
```

## Prints do Projeto

Os principais prints e materiais visuais deste trabalho estão disponíveis na pasta `doc/` e no README específico do TP3.

## Descrição dos principais arquivos

### `TP3/src/Main.java`

Classe principal da aplicação. Inicia o sistema, autentica o usuário e redireciona para o menu principal, onde é possível acessar a área do usuário e a busca por perguntas.

### `TP3/src/tela/MenuResposta.java`

Implementa o fluxo de busca de perguntas. Ele oferece as opções de:

- listar as perguntas mais recentes;
- buscar por palavras-chave;
- navegar por páginas de resultados;
- abrir uma pergunta e visualizar suas respostas;
- responder uma pergunta ou consultar uma resposta específica.

### `TP3/src/arquivos/ArquivoPergunta.java`

É a extensão principal do CRUD de perguntas para o TP3. Além das operações já existentes, ela adiciona:

- índice por usuário;
- índice por data;
- lista invertida de termos;
- normalização dos termos de busca;
- busca por relevância usando frequência e ponderação por IDF;
- reconstrução automática do índice quando necessário.

### `TP3/src/aed3/ListaInvertida.java`

Implementa a estrutura de lista invertida usada para armazenar, por termo, quais perguntas o contêm e qual a frequência relativa desse termo. A estrutura organiza o dicionário e os blocos de ocorrência em arquivos físicos para persistência em disco.

### `TP3/src/entidades/Pergunta.java`

Representa a pergunta do sistema com dados como autor, texto, palavras-chave, data de criação, nota e status ativo/inativo. No TP3, o campo de palavras-chave passa a ser relevante para o mecanismo de busca textual.

## Perguntas frequentes e validação do projeto

### O índice invertido com palavras-chave foi criado usando a classe `ListaInvertida`?

Sim. A estrutura de índice invertido foi implementada por meio da classe `ListaInvertida`, usada no `ArquivoPergunta` para guardar os termos e os registros correspondentes das perguntas, além de suas frequências.

### É possível buscar perguntas por palavras-chave no menu de buscar perguntas?

Sim. O menu de busca oferece a opção de “Busca por palavras-chave”, e esse fluxo chama `ArquivoPergunta.readPerguntasPorPalavras(consulta)`, que realiza a consulta textual usando o índice invertido.

### As perguntas retornadas estão corretamente classificadas pelo modelo TFxIDF?

Sim. A ordenação da busca usa um modelo de ponderação TF × IDF, em que a frequência do termo na pergunta é multiplicada por um fator de inverso da frequência no conjunto de documentos, e os resultados são exibidos em ordem decrescente de pontuação.

### O trabalho compila corretamente?

### `TP2/src/aed3/ParIdEmail.java`

Altera a serialização da chave do índice hash extensível para preencher o espaço reservado ao email, mantendo o tamanho fixo do registro.

### `TP2/src/tela/MenuPergunta.java` e `TP2/src/entidades/Pergunta.java`

Alterações de apresentação das perguntas e de suas listagens na área de perguntas.

## TP3 - Índice invertido e busca por palavras-chave

O TP3 acrescenta a busca de perguntas por palavras-chave usando um índice invertido
persistente e a métrica TF×IDF. As palavras-chave informadas no cadastro de uma
pergunta são indexadas e podem ser usadas posteriormente na consulta.

### Funcionamento do índice invertido

- Os termos são convertidos para letras minúsculas.
- Os acentos são removidos com `java.text.Normalizer`.
- Caracteres especiais são usados como separadores e não fazem parte dos termos.
- Artigos, preposições, numerais e outras palavras irrelevantes são removidos por
  uma lista de *stop words* adequada ao domínio.
- Não é feita lematização: palavras no singular e no plural, ou diferentes
  conjugações verbais, permanecem como termos distintos após a normalização.
- Para cada termo, o índice armazena os pares `(id da pergunta, TF)`.
- O TF é a frequência do termo dividida pela quantidade de termos válidos da
  pergunta.
- O número de perguntas ativas é armazenado no dicionário do índice para o
  cálculo do IDF.

Na consulta, os termos pesquisados recebem o mesmo tratamento aplicado durante a
indexação. Para cada termo encontrado, o sistema calcula
`IDF = log10(numero de perguntas ativas / quantidade de perguntas que contêm o
termo)` e soma `TF × IDF` por pergunta. Os resultados são apresentados em ordem
decrescente de pontuação.

### Atualização do índice

O índice é atualizado junto com o ciclo de vida das perguntas:

- inclusão: cria as entradas dos termos e incrementa a quantidade de entidades;
- alteração: remove os termos antigos e indexa os novos;
- arquivamento: remove a pergunta do índice e decrementa a quantidade de
  entidades;
- exclusão: remove a pergunta do índice e decrementa a quantidade de entidades.

Na inicialização, o índice existente é apenas aberto. Perguntas já armazenadas
não são reprocessadas automaticamente; o índice é atualizado somente nas
operações de inclusão, alteração, arquivamento e exclusão realizadas pelo
sistema.

### Arquivos principais do TP3

- `TP3/src/aed3/ListaInvertida.java`: armazenamento persistente do dicionário e
  dos blocos das listas invertidas.
- `TP3/src/aed3/ElementoLista.java`: representa o ID e a frequência de uma
  ocorrência no índice.
- `TP3/src/arquivos/ArquivoPergunta.java`: mantém o índice, normaliza os termos,
  calcula TF×IDF e executa a busca.
- `TP3/src/tela/MenuResposta.java`: disponibiliza a opção “Busca por
  palavras-chave” na interface.

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

### A busca por palavras-chave está implementada?

Sim. A opção “Busca por palavras-chave” normaliza os termos da consulta, consulta
o índice invertido e apresenta as perguntas ativas em ordem decrescente de
TF×IDF. A busca não faz lematização; portanto, formas no singular, no plural e
conjugações verbais diferentes podem ser tratadas como termos diferentes.

### O trabalho está completo e funcionando sem erros de execução?

Sim. O projeto foi validado com o comando abaixo e compilou corretamente:

```bash
cd TP3
javac -d bin $(find src -name '*.java')
```

Também é possível seguir o mesmo padrão para TP1 e TP2, conforme descrito em cada README específico.

### O trabalho está completo e funcionando sem erros de execução?

Sim. O sistema foi implementado de forma integrada, com os fluxos principais funcionando: login, cadastro, perguntas, respostas, avaliações e busca por palavras-chave, além da manutenção dos índices e arquivos de persistência.

### O trabalho é original e não a cópia de um trabalho de outro grupo?

Sim. O projeto foi desenvolvido pelo grupo e é original, sem cópia de código de outro grupo.

## Como executar

### TP3

```bash
cd TP3
javac -d bin $(find src -name '*.java')
java -cp bin Main
```

## Vídeo do projeto

<video controls src="/doc/AEDS III - TP2.mp4" title="Title"></video>

LINK [https://youtu.be/yfhu8lq-GNw](https://youtu.be/yfhu8lq-GNw)

## Observações

- A listagem de perguntas recentes percorre as entradas do índice B+ e aplica a ordenação decrescente antes de paginar.
- A busca por palavras-chave usa o índice invertido persistente e a ordenação por TF×IDF.
- “Minhas respostas” e “Minhas avaliações” permitem consultar e gerenciar os registros do usuário.
- Ao excluir uma resposta pelo CRUD, as avaliações associadas também são excluídas, e a nota e a quantidade de avaliações são ajustadas antes da remoção.

## Objetivo do TP3

O objetivo desta etapa foi ampliar o sistema com busca textual por palavras-chave, usando uma estrutura de lista invertida para indexar os termos presentes nas perguntas e recuperar as perguntas mais relevantes para a consulta informada pelo usuário.

O objetivo desta etapa é ampliar o sistema com perguntas recentes paginadas, respostas, avaliações e busca por palavras-chave. Foram implementados o fluxo de listagem e navegação de perguntas, a consulta e ordenação de respostas, o envio e arquivamento de respostas próprias e de respostas recebidas nas próprias perguntas, o registro e cancelamento de avaliações e a busca por perguntas usando índice invertido e TF×IDF.
