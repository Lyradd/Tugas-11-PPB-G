package com.example.starbuckmembership.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbuckmembership.data.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class StarbucksViewModel : ViewModel() {

    private val _snackbarChannel = Channel<String>()
    val snackbarFlow = _snackbarChannel.receiveAsFlow()

    private val _user = mutableStateOf(User("Made Daryl", "Gold Member", 287, 125000L))
    val user: State<User> = _user

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _selectedProduct = mutableStateOf<Product?>(null)
    val selectedProduct: State<Product?> = _selectedProduct

    // PERBARUAN: State untuk mengelola tema
    private val _isDarkMode = mutableStateOf(true)
    val isDarkMode: State<Boolean> = _isDarkMode

    private val defaultSizes = listOf(CustomizationOption("Grande", 0), CustomizationOption("Venti", 3000))
    private val defaultMilks = listOf(CustomizationOption("Whole Milk", 0), CustomizationOption("Oat Milk", 5000), CustomizationOption("Almond Milk", 5000))

    private val _generalProducts = listOf(
        Product(name = "Caramel Macchiato", category = "Espresso", basePrice = 62000L, icon = Icons.Default.LocalCafe, description = "Espresso with vanilla-flavored syrup, milk and caramel sauce.", availableSizes = defaultSizes, availableMilks = defaultMilks),
        Product(name = "Vanilla Latte", category = "Espresso", basePrice = 55000L, icon = Icons.Default.LocalCafe, description = "A classic espresso-based drink with steamed milk and vanilla syrup.", availableSizes = defaultSizes, availableMilks = defaultMilks),
        Product(name = "Americano", category = "Espresso", basePrice = 35000L, icon = Icons.Default.Coffee, description = "Espresso shots topped with hot water create a light layer of crema.", availableSizes = defaultSizes),
        Product(name = "Java Chip Frappuccino", category = "Frappuccino", basePrice = 69000L, icon = Icons.Default.Coffee, description = "Coffee, milk, and ice blended with chocolate chips and a mocha drizzle.", availableSizes = defaultSizes),
        Product(name = "Croissant", category = "Food", basePrice = 30000L, icon = Icons.Default.BakeryDining, description = "A classic buttery, flaky, and delicious croissant.")
    )

    private val _stores = mutableStateOf(
        listOf(
            Store("1", "Starbucks Kuta Beach", "Jl. Pantai Kuta No. 123", "2.3 km", true,
                Product(name = "Kuta's Sunset Brew", category = "Store Special", basePrice = 75000L, icon = Icons.Default.WbSunny, description = "A refreshing cold brew infused with tropical citrus notes, perfect for a beach day.", availableSizes = defaultSizes)
            ),
            Store("2", "Starbucks Denpasar Mall", "Jl. Teuku Umar No. 456", "4.6 km", true,
                Product(name = "Denpasar's Urban Blend", category = "Store Special", basePrice = 70000L, icon = Icons.Default.LocationCity, description = "A bold and robust espresso blend for the busy city life.", availableSizes = defaultSizes, availableMilks = defaultMilks)
            ),
            Store("3", "Starbucks Ubud Center", "Jl. Monkey Forest No. 789", "6.9 km", false,
                Product(name = "Ubud's Serenity Tea", category = "Store Special", basePrice = 68000L, icon = Icons.Default.Spa, description = "A calming green tea latte with a hint of lavender and honey.", availableSizes = defaultSizes, availableMilks = defaultMilks)
            )
        )
    )
    val stores: State<List<Store>> = _stores

    private val _offers = mutableStateOf(
        listOf(
            Offer("1", "Buy 1 Get 1 Free", "Any handcrafted beverage", Color(0xFFE74C3C)),
            Offer("2", "50% Off Frappuccino", "Today only special offer", Color(0xFF9B59B6)),
            Offer("3", "Free Birthday Drink", "Celebrate with us!", Color(0xFFF39C12))
        )
    )
    val offers: State<List<Offer>> = _offers

    private val _recentOrders = mutableStateOf(
        listOf(
            RecentOrder("1", "Caramel Macchiato", "Grande", "Rp 65,000", "Today, 10:30 AM"),
            RecentOrder("2", "Vanilla Latte", "Grande", "Rp 58,000", "Yesterday"),
            RecentOrder("3", "Americano", "Grande", "Rp 35,000", "2 days ago")
        )
    )
    val recentOrders: State<List<RecentOrder>> = _recentOrders

    val filteredMenu: State<List<Product>> = derivedStateOf {
        val query = _searchQuery.value
        if (query.isBlank()) {
            getFullMenu()
        } else {
            getFullMenu().filter {
                it.name.contains(query, ignoreCase = true) || it.category.contains(query, ignoreCase = true)
            }
        }
    }

    fun getFullMenu(): List<Product> {
        val specialMenus = _stores.value.map { it.specialMenu }
        return specialMenus + _generalProducts
    }

    private val _favorites = mutableStateListOf<Product>()
    val favorites: List<Product> = _favorites

    private val _cart = mutableStateListOf<CartItem>()
    val cart: List<CartItem> = _cart

    private val _orderHistory = mutableStateListOf<Order>()
    val orderHistory: List<Order> = _orderHistory

    private val _paymentMethods = mutableStateOf(listOf(
        PaymentMethod(name = "Starbucks Card", details = "Balance: Rp ${"%,d".format(_user.value.balance)}", icon = Icons.Default.CreditCard),
        PaymentMethod(name = "Gopay", details = "Connected", icon = Icons.Default.Wallet),
        PaymentMethod(name = "OVO", details = "Not Connected", icon = Icons.Default.AccountBalanceWallet),
        PaymentMethod(name = "Credit/Debit Card", details = "Add Card", icon = Icons.Default.Payment)
    ))
    val paymentMethods: State<List<PaymentMethod>> = _paymentMethods

    private val _notifications = mutableStateListOf(
        NotificationItem("1", "🎉 New Reward Available!", "You've earned a free drink! Redeem now.", "2 minutes ago", true),
        NotificationItem("2", "☕ Your order is ready", "Your Caramel Macchiato is ready for pickup at Kuta Beach store.", "15 minutes ago", true),
        NotificationItem("3", "⭐ Double Stars Today", "Get double stars on all purchases today only!", "1 hour ago", false),
        NotificationItem("4", "🎂 Happy Birthday!", "Enjoy a free birthday drink on us!", "2 hours ago", false),
        NotificationItem("5", "📱 App Update Available", "Update to the latest version for new features.", "1 day ago", false)
    )
    val notifications: List<NotificationItem> = _notifications

    private val _rewards = mutableStateListOf(
        Reward("1", "Free Handcrafted Drink", "150 ⭐", true),
        Reward("2", "Free Food Item", "200 ⭐", true),
        Reward("3", "Bonus Star Challenge", "Complete 3 purchases", false),
        Reward("4", "Double Star Tuesday", "Every Tuesday", false),
        Reward("5", "Birthday Reward", "Free on your birthday", false)
    )
    val rewards: List<Reward> = _rewards

    private val _selectedTab = mutableStateOf(0)
    val selectedTab: State<Int> = _selectedTab
    private val _showNotifications = mutableStateOf(false)
    val showNotifications: State<Boolean> = _showNotifications
    private val _showFavorites = mutableStateOf(false)
    val showFavorites: State<Boolean> = _showFavorites
    private val _showQRCode = mutableStateOf(false)
    val showQRCode: State<Boolean> = _showQRCode
    private val _showCart = mutableStateOf(false)
    val showCart: State<Boolean> = _showCart
    private val _showPaymentMethods = mutableStateOf(false)
    val showPaymentMethods: State<Boolean> = _showPaymentMethods
    private val _showOrderHistory = mutableStateOf(false)
    val showOrderHistory: State<Boolean> = _showOrderHistory

    private val _showHelpScreen = mutableStateOf(false)
    val showHelpScreen: State<Boolean> = _showHelpScreen

    private val _showSettingsScreen = mutableStateOf(false)
    val showSettingsScreen: State<Boolean> = _showSettingsScreen

    // --- Aksi / Event dari UI ---
    fun onTabSelected(index: Int) { _selectedTab.value = index }
    fun showNotificationsScreen(show: Boolean) { _showNotifications.value = show }
    fun showFavoritesScreen(show: Boolean) { _showFavorites.value = show }
    fun showQRCodeScreen(show: Boolean) { _showQRCode.value = show }
    fun showCartScreen(show: Boolean) { _showCart.value = show }
    fun showPaymentMethodsScreen(show: Boolean) { _showPaymentMethods.value = show }
    fun showOrderHistoryScreen(show: Boolean) { _showOrderHistory.value = show }
    fun showHelpScreen(show: Boolean) { _showHelpScreen.value = show }
    fun showSettingsScreen(show: Boolean) { _showSettingsScreen.value = show }

    fun toggleTheme() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun onSearchQueryChanged(newQuery: String) { _searchQuery.value = newQuery }
    fun selectProduct(product: Product?) { _selectedProduct.value = product }
    fun deselectProduct() { _selectedProduct.value = null }

    fun addCustomizedItemToCart(product: Product, customizations: Map<String, CustomizationOption>) {
        val additionalPrice = customizations.values.sumOf { it.additionalPrice }
        val finalPrice = product.basePrice + additionalPrice

        val existingItem = _cart.find { it.product.id == product.id && it.customizations == customizations }
        if (existingItem != null) {
            val index = _cart.indexOf(existingItem)
            _cart[index] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            val newItem = CartItem(product = product, quantity = 1, customizations = customizations, finalPrice = finalPrice)
            _cart.add(newItem)
        }

        viewModelScope.launch { _snackbarChannel.send("${product.name} added to cart") }
    }

    fun toggleFavorite(product: Product) { if (isFavorite(product)) _favorites.remove(product) else _favorites.add(product) }
    fun isFavorite(product: Product): Boolean = _favorites.any { it.id == product.id }

    fun decreaseCartItem(item: CartItem) {
        val index = _cart.indexOf(item)
        if (index != -1) {
            if (item.quantity > 1) {
                _cart[index] = item.copy(quantity = item.quantity - 1)
            } else {
                _cart.removeAt(index)
            }
        }
    }

    fun placeOrder() {
        viewModelScope.launch {
            val total = _cart.sumOf { it.finalPrice * it.quantity }
            if (_user.value.balance >= total) {
                _user.value = _user.value.copy(balance = _user.value.balance - total)
                val newOrder = Order(items = _cart.toList(), totalPrice = total, date = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault()).format(Date()))
                _orderHistory.add(0, newOrder)
                addNotification("✅ Order Successful!", "Your order #${newOrder.id} has been placed.")
                _snackbarChannel.send("Order successful!")
                _cart.clear()
                showCartScreen(false)
                updatePaymentMethodDetails()
            } else {
                addNotification("❌ Payment Failed", "Your balance is not enough.")
                _snackbarChannel.send("Payment failed: Insufficient balance.")
            }
        }
    }

    fun topUpBalance(amount: Long, method: String) {
        viewModelScope.launch {
            if (amount > 0) {
                _user.value = _user.value.copy(balance = _user.value.balance + amount)
                addNotification("💰 Top Up Successful", "Rp ${"%,d".format(amount)} via $method has been added.")
                _snackbarChannel.send("Top up successful!")
                updatePaymentMethodDetails()
            }
        }
    }

    private fun updatePaymentMethodDetails() {
        _paymentMethods.value = _paymentMethods.value.map {
            if(it.name == "Starbucks Card") it.copy(details = "Balance: Rp ${"%,d".format(_user.value.balance)}") else it
        }
    }

    fun redeemReward(rewardId: String) {
        viewModelScope.launch {
            val rewardIndex = _rewards.indexOfFirst { it.id == rewardId }
            if (rewardIndex != -1) {
                val reward = _rewards[rewardIndex]
                if (reward.available && !reward.redeemed) {
                    _rewards[rewardIndex] = reward.copy(available = false, redeemed = true)
                    addNotification("🎁 Reward Redeemed!", "You have successfully redeemed '${reward.title}'.")
                    _snackbarChannel.send("Reward Redeemed!")
                }
            }
        }
    }

    private fun addNotification(title: String, message: String) {
        _notifications.add(0, NotificationItem(title = title, message = message, time = "Just now", isUnread = true))
    }
}
