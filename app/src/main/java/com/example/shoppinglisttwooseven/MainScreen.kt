package com.example.shoppinglisttwooseven

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController

@Composable
fun MainScreen(navController: NavHostController) {
    val viewModel: ShoppingListViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp)
            .background(Color(0xFF212121)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.isAddDialogVisible) {
            AddDialog(
                index = state.currentIndex,
                navController = navController,
                onDismissRequest = {
                    viewModel.hideAddDialog()
                },
                onAddClicked = { newItem ->
                    viewModel.addItem(newItem)
                }
            )
        }

        Button(
            modifier = Modifier.padding(vertical = 8.dp),
            onClick = {
                viewModel.showAddDialog()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFC0CB)
            )
        ) {
            Text("Добавить", color = Color.Black)
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.items) { element ->
                if (element.isEditing) {
                    EditShoppingItemCard(
                        shoppingItem = element,
                        onEditCompleted = { name, description, quantity ->
                            viewModel.updateItem(
                                itemId = element.id,
                                title = name,
                                description = description,
                                quantity = quantity
                            )
                        }
                    )
                } else {
                    ShoppingItemCard(
                        shoppingItem = element,
                        onEditClicked = {
                            viewModel.startEditing(element.id)
                        },
                        onDeleteClicked = { viewModel.deleteItem(it) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDialog(
    index: Int,
    navController: NavController,
    onDismissRequest: () -> Unit,
    onAddClicked: (ShoppingItem) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var quantity by remember { mutableIntStateOf(1) }
    var location by remember { mutableStateOf<Location?>(null) }

    // Слушаем результат выбора локации
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    savedStateHandle?.get<Location>("selected_location")?.let {
        location = it
        savedStateHandle.remove<Location>("selected_location")
    }

    BasicAlertDialog(
        modifier = Modifier.background(
            color = (Color(0xFF212121)),
            shape = RoundedCornerShape(8.dp)
        ),
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(
                    color = (Color(0xFF212121)),
                    shape = RoundedCornerShape(8.dp)
                ),
        ) {
            TextField(
                placeholder = { Text("Название", color = Color.Black) },
                value = title,
                onValueChange = { title = it },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor =Color(0xFFFFC0CB),
                    unfocusedIndicatorColor = Color.Gray
                )
            )
            Spacer(Modifier.height(8.dp))
            TextField(
                placeholder = { Text("Описание", color = Color.Black) },
                value = description,
                onValueChange = { description = it },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor =Color(0xFFFFC0CB),
                    unfocusedIndicatorColor = Color.Gray
                )
            )
            Spacer(Modifier.height(8.dp))
            TextField(
                placeholder = { Text("Количество", color = Color.Black) },
                value = quantity.toString(),
                onValueChange = { quantity = it.toInt() },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor =Color(0xFFFFC0CB),
                    unfocusedIndicatorColor = Color.Gray
                )
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                onClick = { navController.navigate(Screen.MapScreen.route) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFC0CB)
                )
            ) {
                Text(location?.address ?: "Выбрать место покупки", color = Color.Black)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onDismissRequest, colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFC0CB)
                )
                )
                {
                    Text("Отмена", color =Color.Black)
                }
                Button(
                    onClick = {
                        onAddClicked(
                            ShoppingItem(
                                id = index,
                                title = title,
                                description = description,
                                quantity = quantity,
                                location = location
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC0CB))
                ) {
                    Text("Добавить", color = Color.Black)
                }
            }
        }
    }
}
@Composable
fun ShoppingItemCard(shoppingItem: ShoppingItem, onEditClicked: () -> Unit, onDeleteClicked: (ShoppingItem) -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .background(color = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .background(color = Color.White),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = shoppingItem.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = shoppingItem.description,
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
            }
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(0.4f)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Quantity:",
                    textAlign = TextAlign.End,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = shoppingItem.quantity.toString(),
                    textAlign = TextAlign.End,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            Column {
                IconButton(onClick = onEditClicked) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "")
                }
                IconButton(onClick = { onDeleteClicked(shoppingItem) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                }
            }
        }
    }
}

@Composable
fun EditShoppingItemCard(
    shoppingItem: ShoppingItem,
    onEditCompleted: (String, String, Int) -> Unit
){
    var editedName by remember {
        mutableStateOf(shoppingItem.title)
    }

    var editedDescription by remember {
        mutableStateOf(shoppingItem.description)
    }

    var editedQuantity by remember {
        mutableIntStateOf(shoppingItem.quantity)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                BasicTextField(
                    value = editedName,
                    onValueChange = {editedName = it},
                )
                Spacer(Modifier.height(4.dp))
                BasicTextField(
                    value = editedDescription,
                    onValueChange = {editedDescription = it}
                )
            }
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(0.4f),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Quantity:",
                    textAlign = TextAlign.End,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                BasicTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = editedQuantity.toString(),
                    onValueChange = {editedQuantity = it.toInt()},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(textAlign = TextAlign.End)
                )
            }
            IconButton(onClick = {
                onEditCompleted(editedName, editedDescription, editedQuantity)
            }) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "")
            }
        }
    }
}

