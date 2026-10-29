# 💰 Meu Dinheiro — Trabalho 3
## 📱 Persistência de Dados com Room Database

### 📌 Sobre o projeto

O **Meu Dinheiro** é um aplicativo Android de controle financeiro desenvolvido em **Kotlin**, utilizando **Jetpack Compose** e **Android Studio**.

Este repositório corresponde ao **Trabalho 3 da disciplina de Desenvolvimento de Aplicativos Móveis**, dando continuidade ao aplicativo desenvolvido nos Trabalhos 1 e 2.

Nesta etapa, o principal objetivo foi implementar um banco de dados local utilizando **Room Database**, permitindo armazenar as movimentações financeiras de maneira persistente.

Agora, mesmo depois de fechar e abrir o aplicativo, os registros continuam armazenados.

---

## 🎯 Objetivo do Trabalho 3

Implementar a persistência dos dados financeiros utilizando a biblioteca Room, substituindo o armazenamento temporário das movimentações por registros em um banco de dados local.

Os principais objetivos foram:

- Implementar duas entidades utilizando `@Entity`.
- Criar duas interfaces DAO utilizando `@Dao`.
- Implementar as operações CRUD.
- Consultar movimentações diretamente pelo ID.
- Utilizar Kotlin Coroutines nas operações do banco.
- Manter os dados armazenados após fechar o aplicativo.
- Integrar o banco às telas existentes.

---

## 🛠️ Tecnologias utilizadas

- **Kotlin** — linguagem de programação.
- **Android Studio** — ambiente de desenvolvimento.
- **Jetpack Compose** — construção das interfaces.
- **Material 3** — componentes visuais.
- **Navigation Compose** — navegação entre telas.
- **Room Database** — persistência dos dados.
- **SQLite** — banco de dados local utilizado pelo Room.
- **KSP** — processamento das anotações do Room.
- **Kotlin Coroutines** — execução de operações assíncronas.
- **Flow** — observação dos registros armazenados.

---

## 🗄️ Estrutura do banco de dados

Foi criado o arquivo `BancoDados.kt`, responsável por organizar as entidades, interfaces DAO e a classe principal do banco de dados.

O aplicativo utiliza duas tabelas.

### 1. MovimentacaoEntity

Representa a tabela `movimentacoes`, responsável pelo armazenamento das entradas, gastos e demais movimentações financeiras.

| Campo | Tipo | Descrição |
|---|---|---|
| id | Long | Identificador único |
| descricao | String | Descrição da movimentação |
| valor | Double | Valor da movimentação |
| tipo | String | Tipo da movimentação |
| data | Long | Data do registro |

O campo `id` utiliza `@PrimaryKey(autoGenerate = true)`.

### 2. MovimentacaoCaixinhaEntity

Representa a tabela `movimentacoes_caixinha`, responsável pelo armazenamento dos depósitos e retiradas da Caixinha.

| Campo | Tipo | Descrição |
|---|---|---|
| id | Long | Identificador único |
| valor | Double | Valor movimentado |
| tipo | String | Depósito ou retirada |
| data | Long | Data do registro |

Essa entidade também utiliza `@PrimaryKey(autoGenerate = true)`.

---

## 🔧 Interfaces DAO

Foram criadas duas interfaces responsáveis pela comunicação com o banco de dados.

### MovimentacaoDao

Responsável pelas operações relacionadas às movimentações financeiras.

Possui os métodos:

- `observarTodas()` — consultar todas as movimentações.
- `observarPorId(id)` — consultar uma movimentação pelo ID.
- `inserir(item)` — adicionar uma movimentação.
- `atualizar(item)` — editar uma movimentação.
- `excluir(item)` — remover uma movimentação.

### CaixinhaDao

Responsável pelas operações relacionadas à Caixinha.

Possui os métodos:

- `observarTodas()` — consultar os registros da Caixinha.
- `observarPorId(id)` — consultar um registro pelo ID.
- `inserir(item)` — adicionar um registro.
- `atualizar(item)` — atualizar um registro.
- `excluir(item)` — remover um registro.

As interfaces utilizam as anotações `@Insert`, `@Query`, `@Update` e `@Delete`.

---

## 🏗️ Classe MeuDinheiroDatabase

Foi implementada a classe `MeuDinheiroDatabase`, responsável por centralizar o acesso ao banco de dados.

A classe utiliza a anotação `@Database` e herda de `RoomDatabase`.

O banco foi configurado utilizando `Room.databaseBuilder()` e o padrão **Singleton**, evitando a criação desnecessária de várias instâncias.

O armazenamento é realizado localmente no dispositivo Android.

---

## 🔄 Operações CRUD

O aplicativo implementa as quatro operações fundamentais do banco de dados.

| Operação | Funcionalidade |
|---|---|
| Create | Adicionar movimentações |
| Read | Consultar movimentações e detalhes |
| Update | Editar registros existentes |
| Delete | Excluir movimentações |

As operações foram integradas às telas já desenvolvidas no Trabalho 2.

---

## 🔎 Tela de detalhes consultando o banco

Uma das mudanças realizadas foi adaptar a tela de detalhes para consultar as informações diretamente no banco de dados.

Quando o usuário seleciona uma movimentação, seu identificador é utilizado para buscar o registro correspondente.

Essa implementação utiliza consultas filtradas pelo campo `id`, evitando depender exclusivamente dos objetos armazenados em memória.

---

## ⚡ Kotlin Coroutines e Flow

Foram utilizadas Kotlin Coroutines para realizar operações de banco de dados sem bloquear a interface principal.

As operações de escrita utilizam `Dispatchers.IO`, permitindo executar tarefas de entrada e saída em uma thread apropriada.

Também foi utilizado `Flow` para observar os registros do banco de dados.

Dessa forma, quando uma movimentação é adicionada, atualizada ou removida, as listas podem refletir automaticamente as alterações.

---

## 🔄 Evolução do Trabalho 2 para o Trabalho 3

No Trabalho 2, as movimentações eram armazenadas principalmente em listas na memória do aplicativo.

Isso significava que os dados poderiam ser perdidos ao fechar o aplicativo.

No Trabalho 3, foram realizadas as seguintes mudanças:

1. Adição das dependências do Room e KSP.
2. Criação das entidades do banco de dados.
3. Criação das interfaces DAO.
4. Implementação da classe `MeuDinheiroDatabase`.
5. Integração do banco com a `MainActivity`.
6. Adaptação das listas para consultar os registros persistidos.
7. Implementação das operações de inserção, consulta, edição e exclusão.
8. Adaptação das telas de detalhes para consultar os registros pelo ID.
9. Realização de testes de persistência.

---

## 🧠 Dificuldades encontradas

Durante a implementação do Trabalho 3, foram encontradas algumas dificuldades.

**Configuração do Gradle e KSP:** ocorreram problemas de configuração dos plugins Kotlin e KSP. A solução envolveu revisar os arquivos Gradle e sincronizar novamente o projeto.

**Compilação do aplicativo:** ocorreram erros relacionados aos arquivos temporários de compilação. Para resolver, foi necessário limpar os arquivos de build e executar uma nova sincronização.

**Configuração do emulador:** inicialmente, o Android Studio não reconhecia um dispositivo disponível. O problema foi resolvido configurando e iniciando um emulador Pixel 9.

**Persistência dos dados:** foi necessário adaptar o funcionamento das telas para utilizar os registros armazenados no Room, em vez de depender apenas das listas em memória.

Após os ajustes, o aplicativo foi executado e as principais operações foram testadas.

---

## 🧪 Testes realizados

Foram realizados testes manuais no emulador Android Pixel 9.

| Teste | Resultado |
|---|---|
| Abrir o aplicativo | ✅ Aprovado |
| Adicionar movimentação | ✅ Aprovado |
| Consultar movimentação | ✅ Aprovado |
| Editar movimentação | ✅ Aprovado |
| Excluir movimentação | ✅ Aprovado |
| Fechar e reabrir mantendo os registros | ✅ Aprovado |
| Guardar dinheiro na Caixinha | ✅ Aprovado |
| Fechar e reabrir mantendo o saldo da Caixinha | ✅ Aprovado |
| Confirmar a persistência da edição | ✅ Aprovado |
| Confirmar a persistência da exclusão | ✅ Aprovado |

### Teste de persistência

Para verificar o funcionamento do banco de dados, foi realizado o seguinte procedimento:

1. Abrir o aplicativo.
2. Adicionar uma entrada de R$ 200.
3. Consultar a movimentação cadastrada.
4. Fechar completamente o aplicativo.
5. Abrir novamente.
6. Confirmar que a movimentação continuava armazenada.
7. Editar o valor para R$ 150.
8. Fechar e abrir novamente para verificar a alteração.
9. Excluir a movimentação.
10. Reiniciar o aplicativo e confirmar que o registro não reapareceu.

O teste demonstrou que as operações de cadastro, consulta, atualização e exclusão funcionaram com persistência nos cenários avaliados.

---

## ▶️ Como executar o projeto

1. Instale o Android Studio.
2. Clone o repositório:

   `git clone https://github.com/Vitor-vt/MeuDinheiro-Trabalho3.git`

3. Abra a pasta do projeto no Android Studio.
4. Aguarde a sincronização do Gradle.
5. Configure um emulador Android ou conecte um dispositivo físico.
6. Selecione o módulo `app`.
7. Clique em **Run ▶**.

O banco de dados Room é criado automaticamente quando inicializado pelo aplicativo.

Não é necessário configurar um servidor externo.

---

## 📸 Evidências e demonstração

A apresentação do Trabalho 3 demonstra:

- Aplicativo funcionando no emulador.
- Registro de uma nova movimentação.
- Consulta dos dados cadastrados.
- Edição de uma movimentação.
- Exclusão de uma movimentação.
- Funcionamento da Caixinha.
- Persistência dos dados após fechar e reabrir o aplicativo.

**[Vídeo de demonstração do Trabalho 3:](https://youtu.be/22sGwhTUWVk)**

---

## 📌 Repositório GitHub

**https://github.com/Vitor-vt/MeuDinheiro-Trabalho3**

---

## 👨‍💻 Autores

**Vitor Augusto Costa**

**Rafael**

---

## ✅ Conclusão

O Trabalho 3 permitiu evoluir o aplicativo Meu Dinheiro com a implementação de um banco de dados local utilizando Room Database.

Com essa implementação, as movimentações financeiras passaram a ser armazenadas de forma persistente, permitindo realizar operações CRUD e recuperar as informações após fechar e reabrir o aplicativo.

O desenvolvimento proporcionou conhecimentos práticos sobre entidades, DAOs, SQLite, Kotlin Coroutines, Flow e integração de banco de dados com interfaces desenvolvidas em Jetpack Compose.
