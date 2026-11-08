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

## Operações especiais implementadas

### Há busca por palavras-chave nas perguntas?

Sim. O método `ArquivoPergunta.readPerguntasPorPalavras(String consulta)` recebe uma string com termos de busca, normaliza os termos, consulta o índice invertido e retorna as perguntas ativas ordenadas pela pontuação de relevância.

### A busca ignora termos comuns?

Sim. O sistema considera uma lista de stop words (como artigos e preposições) e elimina esses termos durante a normalização da busca e da indexação.

### Os termos são normalizados antes de serem indexados?

Sim. O método de normalização remove acentos, converte para minúsculas e elimina espaços extras e termos vazios. Isso torna a comparação textual mais consistente.

### O índice invertido é integrado ao cadastro e manutenção das perguntas?

Sim. Ao criar, atualizar ou arquivar uma pergunta, a estrutura do índice invertido é mantida em sincronia para garantir que a busca reflita o estado atual do sistema.

### O sistema trata inconsistências no índice invertido?

Sim. Na inicialização do `ArquivoPergunta`, o programa valida a consistência do índice de palavras. Caso o índice esteja divergente, ele reconstrói automaticamente os dados.

## Como rodar

### Opção 1: executar diretamente no VS Code / IntelliJ

- Abra a pasta `TP3` na IDE.
- Execute a classe `src/Main.java`.

### Opção 2: compilar e executar no terminal

No Linux/macOS, a partir da raiz do projeto:

```bash
cd TP3
javac -d bin $(find src -name '*.java')
java -cp bin Main
```

## Observações

- Os dados e índices persistem em `TP3/src/dados/` e em seus subdiretórios.
- A busca por palavras-chave é uma extensão do sistema já existente, mantendo os fluxos de login, pergunta, resposta e avaliação.
- O uso de lista invertida permite consultas mais rápidas e mais relevantes em grandes conjuntos de perguntas.

## Objetivo do TP3

O objetivo desta etapa foi ampliar o sistema com busca textual por palavras-chave, usando uma estrutura de lista invertida para indexar os termos presentes nas perguntas e recuperar as perguntas mais relevantes para a consulta informada pelo usuário.
