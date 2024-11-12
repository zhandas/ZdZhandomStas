package com.example.shoppinglisttwooseven

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ShoppingListState(
    val items: List<ShoppingItem> = emptyList(),
    val isAddDialogVisible: Boolean = false,
    val currentIndex: Int = 1
)

class ShoppingListViewModel : ViewModel() {
    private val _state = MutableStateFlow(ShoppingListState())
    val state: StateFlow<ShoppingListState> = _state.asStateFlow()

    fun showAddDialog() {
        _state.update { it.copy(isAddDialogVisible = true) }
    }

    fun hideAddDialog() {
        _state.update { it.copy(isAddDialogVisible = false) }
    }

    fun addItem(item: ShoppingItem) {
        _state.update { currentState ->
            currentState.copy(
                items = currentState.items + item,
                isAddDialogVisible = false,
                currentIndex = currentState.currentIndex + 1
            )
        }
    }

    fun deleteItem(item: ShoppingItem) {
        _state.update { currentState ->
            currentState.copy(
                items = currentState.items - item
            )
        }
    }

    fun startEditing(itemId: Int) {
        _state.update { currentState ->
            currentState.copy(
                items = currentState.items.map { item ->
                    item.copy(isEditing = item.id == itemId)
                }
            )
        }
    }

    fun updateItem(itemId: Int, title: String, description: String, quantity: Int) {
        _state.update { currentState ->
            currentState.copy(
                items = currentState.items.map { item ->
                    if (item.id == itemId) {
                        item.copy(
                            title = title,
                            description = description,
                            quantity = quantity,
                            isEditing = false
                        )
                    } else {
                        item
                    }
                }
            )
        }
    }

    fun updateItemLocation(itemId: Int, location: Location) {
        _state.update { currentState ->
            currentState.copy(
                items = currentState.items.map { item ->
                    if (item.id == itemId) {
                        item.copy(location = location)
                    } else {
                        item
                    }
                }
            )
        }
    }
}