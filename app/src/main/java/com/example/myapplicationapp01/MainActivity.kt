package com.example.myapplicationprimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val banco = MeuDinheiroDatabase.obter(applicationContext)
        setContent { MaterialTheme { MeuAplicativo(banco) } }
    }
}

@Composable
fun MeuAplicativo(banco: MeuDinheiroDatabase) {
    val dao = remember(banco) { banco.movimentacaoDao() }
    val caixaDao = remember(banco) { banco.caixinhaDao() }
    val movimentacoes by dao.observarTodas().collectAsState(initial = emptyList())
    val caixa by caixaDao.observarTodas().collectAsState(initial = emptyList())
    val escopo = rememberCoroutineScope()
    val navController = rememberNavController()
    val contexto = androidx.compose.ui.platform.LocalContext.current
    val prefs = remember(contexto) { contexto.getSharedPreferences("preferencias_meu_dinheiro", android.content.Context.MODE_PRIVATE) }
    var meta by remember { mutableDoubleStateOf(prefs.getFloat("meta", 0f).toDouble()) }
    val entradas = movimentacoes.filter { it.tipo == "Entrada" }.sumOf { it.valor }
    val gastos = movimentacoes.filter { it.tipo != "Entrada" }.sumOf { it.valor }
    val saldo = entradas - gastos
    val guardado = caixa.sumOf { if (it.tipo == "Depósito") it.valor else -it.valor }

    fun salvarMovimento(descricao: String, valor: Double, tipo: String) {
        if (valor > 0 && valor.isFinite()) escopo.launch {
            withContext(Dispatchers.IO) { dao.inserir(MovimentacaoEntity(descricao = descricao, valor = valor, tipo = tipo)) }
        }
    }
    fun salvarCaixa(valor: Double, tipo: String) {
        if (valor > 0 && valor.isFinite()) escopo.launch {
            withContext(Dispatchers.IO) { caixaDao.inserir(MovimentacaoCaixinhaEntity(valor = valor, tipo = tipo)) }
        }
    }

    NavHost(navController, startDestination = Rotas.INICIO) {
        composable(Rotas.INICIO) {
            TelaInicial(saldo, { salvarMovimento("Entrada", it, "Entrada") },
                { salvarMovimento("Gasto", it, "Gasto") },
                { navController.navigate(Rotas.MOVIMENTACOES) },
                { navController.navigate(Rotas.SOBRE) },
                { navController.navigate(Rotas.RESUMO) },
                { navController.navigate(Rotas.META) },
                { navController.navigate(Rotas.CAIXINHA) },
                { navController.navigate(Rotas.TRANSFERENCIA) })
        }
        composable(Rotas.MOVIMENTACOES) {
            TelaMovimentacoes(movimentacoes.map { Movimentacao(id = it.id, descricao = it.descricao, valor = it.valor, tipo = it.tipo) },
                { item ->
                    val id = item.id
                    if (id != null) navController.navigate("${Rotas.DETALHE_MOVIMENTACAO}/$id")
                }, { navController.popBackStack() })
        }
        composable("${Rotas.DETALHE_MOVIMENTACAO}/{id}") { entry ->
            val id = entry.arguments?.getString("id")?.toLongOrNull() ?: -1L
            val item by remember(id) { dao.observarPorId(id) }.collectAsState(initial = null)
            if (item != null) {
                DetalheMovimentacaoBanco(item!!,
                    { atualizado -> escopo.launch { withContext(Dispatchers.IO) { dao.atualizar(atualizado) } } },
                    { escopo.launch { withContext(Dispatchers.IO) { dao.excluir(item!!) }; navController.popBackStack() } },
                    { navController.popBackStack() })
            } else { Text("Carregando movimentação ou registro excluído") }
        }
        composable(Rotas.CAIXINHA) {
            TelaCaixinha(guardado, caixa.map { MovimentacaoCaixinha(id = it.id, valor = it.valor, tipo = it.tipo) },
                { item ->
                    val id = item.id
                    if (id != null) navController.navigate("${Rotas.DETALHE_CAIXINHA}/$id")
                },
                { item -> caixa.firstOrNull { it.id == item.id }?.let { registro ->
                    escopo.launch { withContext(Dispatchers.IO) { caixaDao.excluir(registro) } }
                } },
                { salvarCaixa(it, "Depósito") }, { if (it <= guardado) salvarCaixa(it, "Retirada") },
                { navController.popBackStack() })
        }
        composable("${Rotas.DETALHE_CAIXINHA}/{id}") { entry ->
            val id = entry.arguments?.getString("id")?.toLongOrNull() ?: -1L
            val item by remember(id) { caixaDao.observarPorId(id) }.collectAsState(initial = null)
            if (item != null) {
                DetalheCaixinhaBanco(item!!,
                    { atualizado -> escopo.launch { withContext(Dispatchers.IO) { caixaDao.atualizar(atualizado) } } },
                    { escopo.launch { withContext(Dispatchers.IO) { caixaDao.excluir(item!!) }; navController.popBackStack() } },
                    { navController.popBackStack() })
            } else { Text("Carregando movimentação ou registro excluído") }
        }
        composable(Rotas.SOBRE) { TelaSobre { navController.popBackStack() } }
        composable(Rotas.RESUMO) { TelaResumoMes(entradas, gastos) { navController.popBackStack() } }
        composable(Rotas.META) { TelaMetaEconomia(meta, guardado, {
            meta = it; prefs.edit().putFloat("meta", it.toFloat()).apply()
        }, { navController.popBackStack() }) }
        composable(Rotas.TRANSFERENCIA) { TelaTransferencia(saldo, { valor, destino ->
            if (valor <= saldo) salvarMovimento("Transferência para $destino", valor, "Transferência")
        }, { navController.popBackStack() }) }
    }
}

@Composable
fun TelaInicial(
    saldo: Double, adicionarEntrada: (Double) -> Unit, adicionarGasto: (Double) -> Unit,
    abrirMovimentacoes: () -> Unit, abrirSobre: () -> Unit, abrirResumo: () -> Unit,
    abrirMeta: () -> Unit, abrirCaixinha: () -> Unit, abrirTransferencia: () -> Unit
) {
    var valor by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
        Text("Meu Dinheiro", fontSize = 28.sp)
        Spacer(Modifier.height(24.dp))
        Text("Saldo atual")
        Text("R$ %.2f".format(saldo), fontSize = 30.sp)
        Spacer(Modifier.height(24.dp))
        OutlinedTextField(valor, { valor = it }, label = { Text("Digite um valor") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))
        Button(onClick = { valor.replace(',', '.').toDoubleOrNull()?.takeIf { it > 0 }?.let { adicionarEntrada(it); valor = "" } }, modifier = Modifier.fillMaxWidth()) { Text("Adicionar entrada") }
        Button(onClick = { valor.replace(',', '.').toDoubleOrNull()?.takeIf { it > 0 }?.let { adicionarGasto(it); valor = "" } }, modifier = Modifier.fillMaxWidth()) { Text("Adicionar gasto") }
        Spacer(Modifier.height(12.dp))
        Button(onClick = abrirMovimentacoes, modifier = Modifier.fillMaxWidth()) { Text("Ver movimentações") }
        Button(onClick = abrirResumo, modifier = Modifier.fillMaxWidth()) { Text("Resumo do mês") }
        Button(onClick = abrirMeta, modifier = Modifier.fillMaxWidth()) { Text("Meta de economia") }
        Button(onClick = abrirCaixinha, modifier = Modifier.fillMaxWidth()) { Text("Minha Caixinha") }
        Button(onClick = abrirTransferencia, modifier = Modifier.fillMaxWidth()) { Text("Transferência") }
        Button(onClick = abrirSobre, modifier = Modifier.fillMaxWidth()) { Text("Sobre") }
    }
}
