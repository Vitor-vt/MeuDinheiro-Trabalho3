package com.example.myapplicationprimer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DetalheMovimentacaoBanco(item: MovimentacaoEntity, atualizar: (MovimentacaoEntity) -> Unit, excluir: () -> Unit, voltar: () -> Unit) {
    var descricao by remember(item.id) { mutableStateOf(item.descricao) }
    var valor by remember(item.id) { mutableStateOf(item.valor.toString()) }
    var tipo by remember(item.id) { mutableStateOf(item.tipo) }
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Detalhes da movimentação #${item.id}", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(descricao, { descricao = it }, label = { Text("Descrição") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(valor, { valor = it }, label = { Text("Valor") }, modifier = Modifier.fillMaxWidth())
        Text("Tipo: $tipo")
        Row { Button(onClick = { tipo = "Entrada" }) { Text("Entrada") }; Spacer(Modifier.width(8.dp)); Button(onClick = { tipo = "Gasto" }) { Text("Gasto") } }
        Button(onClick = { valor.replace(',', '.').toDoubleOrNull()?.takeIf { it > 0 }?.let { atualizar(item.copy(descricao = descricao, valor = it, tipo = tipo)) } }, modifier = Modifier.fillMaxWidth()) { Text("Salvar alterações") }
        Button(onClick = excluir, modifier = Modifier.fillMaxWidth()) { Text("Excluir movimentação") }
        Button(onClick = voltar, modifier = Modifier.fillMaxWidth()) { Text("Voltar") }
    }
}

@Composable
fun DetalheCaixinhaBanco(item: MovimentacaoCaixinhaEntity, atualizar: (MovimentacaoCaixinhaEntity) -> Unit, excluir: () -> Unit, voltar: () -> Unit) {
    var valor by remember(item.id) { mutableStateOf(item.valor.toString()) }
    var tipo by remember(item.id) { mutableStateOf(item.tipo) }
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Detalhes da caixinha #${item.id}", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(valor, { valor = it }, label = { Text("Valor") }, modifier = Modifier.fillMaxWidth())
        Text("Tipo: $tipo")
        Row { Button(onClick = { tipo = "Depósito" }) { Text("Depósito") }; Spacer(Modifier.width(8.dp)); Button(onClick = { tipo = "Retirada" }) { Text("Retirada") } }
        Button(onClick = { valor.replace(',', '.').toDoubleOrNull()?.takeIf { it > 0 }?.let { atualizar(item.copy(valor = it, tipo = tipo)) } }, modifier = Modifier.fillMaxWidth()) { Text("Salvar alterações") }
        Button(onClick = excluir, modifier = Modifier.fillMaxWidth()) { Text("Excluir registro") }
        Button(onClick = voltar, modifier = Modifier.fillMaxWidth()) { Text("Voltar") }
    }
}
