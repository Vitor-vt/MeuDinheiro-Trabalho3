# MeuDinheiro — Persistência com Room

## Implementação

- Duas tabelas: `movimentacoes` e `movimentacoes_caixinha`, com IDs auto-gerados.
- Dois DAOs com `@Insert`, `@Query`, `@Update` e `@Delete`.
- `MeuDinheiroDatabase`: Room, `@Database`, singleton e banco local `meu_dinheiro.db`.
- Operações de escrita executadas com coroutines e `Dispatchers.IO`.
- Telas observam `Flow` dos DAOs; a lista e o saldo se atualizam após alterações.
- Telas de detalhes usam a rota com ID e consultam `observarPorId(id)` diretamente no banco.
- Entradas, gastos, transferências e operações da caixinha persistem entre execuções.
- Meta de economia é salva em SharedPreferences (separada das duas entidades Room).

## Como executar

1. Abrir a pasta do projeto no Android Studio (JDK 17).
2. Sincronizar Gradle; é necessário acesso à internet para baixar as dependências.
3. Selecionar um emulador ou celular com Android 7.0 ou superior e clicar em Run.
4. Adicionar uma entrada, abrir Movimentações, editar/excluir pelo detalhe e reiniciar o aplicativo para verificar a persistência.
5. Testar depósitos e retiradas da Caixinha da mesma forma.

**Observação:** o projeto foi alterado em ambiente sem SDK Android/Gradle, portanto a compilação e o teste em dispositivo ainda precisam ser realizados no Android Studio.
