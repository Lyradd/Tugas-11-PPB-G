package com.example.starbuckmembership.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import java.util.UUID

data class User(
    val name: String,
    val membershipLevel: String,
    val stars: Int,
    var balance: Long
)

data class Product(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val category: String,
    val description: String,
    val basePrice: Long,
    val icon: ImageVector,
    val availableSizes: List<CustomizationOption> = emptyList(),
    val availableMilks: List<CustomizationOption> = emptyList()
)

data class CustomizationOption(
    val name: String,
    val additionalPrice: Long = 0
)

data class CartItem(
    val id: String = UUID.randomUUID().toString(),
    val product: Product,
    val quantity: Int,
    val customizations: Map<String, CustomizationOption>,
    val finalPrice: Long
)

data class Order(
    val id: String = "SBX-${UUID.randomUUID().toString().take(6).uppercase()}",
    val items: List<CartItem>,
    val totalPrice: Long,
    val date: String
)

data class PaymentMethod(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val details: String,
    val icon: ImageVector
)

data class RecentOrder(
    val id: String,
    val drinkName: String,
    val size: String,
    val price: String,
    val date: String
)

data class Offer(
    val id: String,
    val title: String,
    val description: String,
    val color: Color
)

data class Reward(
    val id: String,
    val title: String,
    val points: String,
    var available: Boolean,
    var redeemed: Boolean = false
)

data class Store(
    val id: String,
    val name: String,
    val address: String,
    val distance: String,
    val isOpen: Boolean,
    val specialMenu: Product
)

data class NotificationItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val message: String,
    val time: String,
    val isUnread: Boolean
)
