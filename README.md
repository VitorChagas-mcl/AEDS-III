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

Sim. Os módulos do projeto seguem a compilação Java padrão por pasta. O procedimento esperado é:

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

- Os dados e índices persistem em `TP3/src/dados/` e em seus subdiretórios.
- A busca por palavras-chave é uma extensão do sistema já existente, mantendo os fluxos de login, pergunta, resposta e avaliação.
- O uso de lista invertida permite consultas mais rápidas e mais relevantes em grandes conjuntos de perguntas.

## Objetivo do TP3

O objetivo desta etapa foi ampliar o sistema com busca textual por palavras-chave, usando uma estrutura de lista invertida para indexar os termos presentes nas perguntas e recuperar as perguntas mais relevantes para a consulta informada pelo usuário.

