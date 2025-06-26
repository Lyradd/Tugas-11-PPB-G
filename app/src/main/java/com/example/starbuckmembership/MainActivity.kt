package com.example.starbuckmembership

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.*
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.starbuckmembership.data.*
import com.example.starbuckmembership.viewmodel.StarbucksViewModel
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006A4E),
    secondary = Color(0xFFC9A22D),
    background = Color(0xFFF0F0F0),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00704A),
    secondary = Color(0xFFD4AF37),
    background = Color(0xFF0D1B2A),
    surface = Color(0xFF1B263B),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

class MainActivity : ComponentActivity() {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val cameraPermissionGranted = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                cameraPermissionGranted.value = isGranted
            }

        updateCameraPermission()

        setContent {
            StarbucksApp(
                viewModel = viewModel(),
                hasCameraPermission = cameraPermissionGranted.value,
                requestPermission = { requestPermissionLauncher.launch(Manifest.permission.CAMERA) }
            )
        }
    }

    private fun updateCameraPermission() {
        cameraPermissionGranted.value = ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onResume() {
        super.onResume()
        updateCameraPermission()
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun StarbucksApp(
    viewModel: StarbucksViewModel,
    hasCameraPermission: Boolean,
    requestPermission: () -> Unit
) {
    val selectedTab by viewModel.selectedTab
    val showNotifications by viewModel.showNotifications
    val showFavorites by viewModel.showFavorites
    val showQRCode by viewModel.showQRCode
    val showCart by viewModel.showCart
    val showPaymentMethods by viewModel.showPaymentMethods
    val showOrderHistory by viewModel.showOrderHistory
    val selectedProduct by viewModel.selectedProduct
    val showHelpScreen by viewModel.showHelpScreen
    val showSettingsScreen by viewModel.showSettingsScreen

    // PERBARUAN: State untuk tema
    val isDarkMode by viewModel.isDarkMode

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = snackbarHostState) {
        viewModel.snackbarFlow.collect { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    MaterialTheme(colorScheme = if (isDarkMode) DarkColorScheme else LightColorScheme) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                bottomBar = {
                    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                        val tabs = listOf("Home" to Icons.Default.Home, "Rewards" to Icons.Default.Star, "Order" to Icons.Default.ShoppingCart, "Stores" to Icons.Default.LocationOn, "Profile" to Icons.Default.Person)
                        tabs.forEachIndexed { index, (title, icon) ->
                            NavigationBarItem(
                                selected = selectedTab == index,
                                onClick = { viewModel.onTabSelected(index) },
                                icon = { Icon(imageVector = icon, contentDescription = title, tint = if (selectedTab == index) MaterialTheme.colorScheme.secondary else Color.Gray) },
                                label = { Text(text = title, color = if (selectedTab == index) MaterialTheme.colorScheme.secondary else Color.Gray, fontSize = 10.sp) }
                            )
                        }
                    }
                },
                floatingActionButton = {
                    if (selectedTab == 2 || selectedTab == 4) {
                        val cartItemsCount = viewModel.cart.sumOf { it.quantity }
                        if (cartItemsCount > 0) {
                            ExtendedFloatingActionButton(onClick = { viewModel.showCartScreen(true) }, icon = { Icon(Icons.Default.ShoppingBag, "Cart") }, text = { Text("$cartItemsCount item(s)") }, containerColor = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            ) { paddingValues ->
                when (selectedTab) {
                    0 -> HomeScreen(paddingValues, viewModel)
                    1 -> RewardsScreen(paddingValues, viewModel)
                    2 -> OrderScreen(paddingValues, viewModel)
                    3 -> StoresScreen(paddingValues, viewModel)
                    4 -> ProfileScreen(paddingValues, viewModel)
                }
            }

            val overlayVisible = showNotifications || showFavorites || showQRCode || showCart || showPaymentMethods || showOrderHistory || selectedProduct != null || showHelpScreen || showSettingsScreen
            AnimatedVisibility(
                visible = overlayVisible,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                when {
                    showNotifications -> NotificationScreen(onBack = { viewModel.showNotificationsScreen(false) }, viewModel = viewModel)
                    showFavorites -> FavoritesScreen(onBack = { viewModel.showFavoritesScreen(false) }, viewModel = viewModel)
                    showQRCode -> QRCodeScreen(onBack = { viewModel.showQRCodeScreen(false) }, viewModel = viewModel, hasPermission = hasCameraPermission, requestPermission = requestPermission)
                    showCart -> CartScreen(onBack = { viewModel.showCartScreen(false) }, viewModel = viewModel)
                    showPaymentMethods -> PaymentMethodsScreen(onBack = { viewModel.showPaymentMethodsScreen(false) }, viewModel = viewModel)
                    showOrderHistory -> OrderHistoryScreen(onBack = { viewModel.showOrderHistoryScreen(false) }, viewModel = viewModel)
                    showHelpScreen -> HelpScreen(onBack = { viewModel.showHelpScreen(false) })
                    showSettingsScreen -> SettingsScreen(onBack = { viewModel.showSettingsScreen(false) }, viewModel = viewModel)
                    selectedProduct != null -> ProductDetailScreen(product = selectedProduct!!, viewModel = viewModel, onBack = { viewModel.deselectProduct() })
                }
            }
        }
    }
}

// --- SCREEN COMPOSABLES ---

@Composable
fun HomeScreen(paddingValues: PaddingValues, viewModel: StarbucksViewModel) {
    val user by viewModel.user
    val offers by viewModel.offers
    val recentOrders by viewModel.recentOrders
    val animatedStars by animateIntAsState(targetValue = user.stars, animationSpec = tween(1000), label = "starAnimation")

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(paddingValues).background(Brush.verticalGradient(listOf(Color(0xFF0D1B2A), Color(0xFF1B263B)))),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(text = "Good Morning ☀", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
                    Text(text = user.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
                }
                IconButton(onClick = { viewModel.showNotificationsScreen(true) }, modifier = Modifier.background(Color(0xFF00704A), CircleShape)) {
                    Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White)
                }
            }
        }
        // Membership Card
        item {
            Card(modifier = Modifier.fillMaxWidth().height(200.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF00704A)), shape = RoundedCornerShape(20.dp)) {
                Box(modifier = Modifier.fillMaxSize().background(Brush.linearGradient(listOf(Color(0xFF00704A), Color(0xFF228B22)))).padding(20.dp)) {
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "★ ${user.membershipLevel.uppercase()}", color = Color(0xFFD4AF37), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Color(0xFFD4AF37), modifier = Modifier.size(24.dp))
                        }
                        Column {
                            Text(text = "$animatedStars ⭐", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                            Text(text = "113 stars to next reward", color = Color.White.copy(0.8f), fontSize = 14.sp)
                            Spacer(Modifier.height(8.dp))
                            LinearProgressIndicator(progress = 0.72f, modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)), color = Color(0xFFD4AF37), trackColor = Color.White.copy(0.3f))
                        }
                    }
                }
            }
        }
        // Quick Actions
        item {
            Text(text = "Quick Actions", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                QuickActionCard(title = "Scan & Pay", icon = Icons.Default.QrCode, color = Color(0xFF00704A), modifier = Modifier.weight(1f)) { viewModel.showQRCodeScreen(true) }
                QuickActionCard(title = "Order Ahead", icon = Icons.Default.ShoppingBag, color = Color(0xFFD4AF37), modifier = Modifier.weight(1f)) { viewModel.onTabSelected(2) }
            }
        }
        // Featured Offers
        item {
            Text(text = "Featured Offers", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(offers) { offer -> OfferCard(offer) }
            }
        }
        // Recent Orders
        item {
            Text(text = "Recent Orders", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
        }
        items(recentOrders) { order -> RecentOrderCard(order) }
    }
}

@Composable
fun QuickActionCard(title: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(modifier = modifier.height(100.dp).clickable(onClick = onClick), colors = CardDefaults.cardColors(containerColor = color), shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
            Spacer(Modifier.height(8.dp))
            Text(text = title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun OfferCard(offer: Offer) {
    Card(modifier = Modifier.width(220.dp).height(120.dp), colors = CardDefaults.cardColors(containerColor = offer.color), shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Text(text = offer.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = offer.description, color = Color.White.copy(0.9f), fontSize = 12.sp)
        }
    }
}

@Composable
fun RecentOrderCard(order: RecentOrder) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1B263B))) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(60.dp).background(Color(0xFF00704A), CircleShape), contentAlignment = Alignment.Center) {
                Icon(imageVector = Icons.Default.LocalCafe, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(text = order.drinkName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "${order.size} • ${order.date}", color = Color.Gray, fontSize = 14.sp)
            }
            TextButton(onClick = {}, colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFD4AF37))) {
                Text(text = "Reorder", fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun OrderScreen(paddingValues: PaddingValues, viewModel: StarbucksViewModel) {
    val filteredMenu by viewModel.filteredMenu
    val searchQuery by viewModel.searchQuery

    Column(modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color(0xFF0D1B2A))) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            label = { Text("Search menu...") },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF1B263B),
                unfocusedContainerColor = Color(0xFF1B263B),
                focusedIndicatorColor = Color(0xFF00704A),
                cursorColor = Color(0xFF00704A)
            )
        )
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredMenu, key = { it.id }) { product ->
                ProductCard(product = product, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, viewModel: StarbucksViewModel) {
    val isFavorite by remember(product.id, viewModel.favorites.size) { mutableStateOf(viewModel.isFavorite(product)) }
    Card(
        modifier = Modifier.fillMaxWidth().clickable { viewModel.selectProduct(product) },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B263B)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = product.icon, contentDescription = product.name, tint = Color(0xFF00704A), modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.name, color = Color.White, fontWeight = FontWeight.Bold)
                Text(text = "Starts from Rp ${"%,d".format(product.basePrice)}", color = Color.Gray, fontSize = 12.sp)
            }
            IconButton(onClick = { viewModel.toggleFavorite(product) }) {
                Icon(imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder, contentDescription = "Favorite", tint = if (isFavorite) Color.Red else Color.Gray)
            }
        }
    }
}


@Composable
fun StoresScreen(paddingValues: PaddingValues, viewModel: StarbucksViewModel) {
    val stores by viewModel.stores
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color(0xFF0D1B2A)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Text(text = "Store Locator", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = Color.White) }
        items(stores) { store ->
            StoreCard(store = store, onOrderHere = { viewModel.onTabSelected(2) })
        }
    }
}

@Composable
fun StoreCard(store: Store, onOrderHere: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1B263B))) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = store.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = store.address, color = Color.Gray, fontSize = 14.sp)
            Row(modifier = Modifier.fillMaxWidth().background(Color(0xFFD4AF37).copy(0.1f), RoundedCornerShape(8.dp)).padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = store.specialMenu.icon, contentDescription = "Special", tint = Color(0xFFD4AF37))
                Spacer(Modifier.width(8.dp))
                Column {
                    Text(text = "Store Special: ${store.specialMenu.name}", color = Color(0xFFD4AF37), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(text = "Rp ${"%,d".format(store.specialMenu.basePrice)}", color = Color.Gray, fontSize = 12.sp)
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = if (store.isOpen) "Open" else "Closed", color = if (store.isOpen) Color.Green else Color.Red, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Button(onClick = onOrderHere, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00704A))) { Text("Order From Menu") }
            }
        }
    }
}

@Composable
fun RewardsScreen(paddingValues: PaddingValues, viewModel: StarbucksViewModel) {
    val rewards = viewModel.rewards
    var showDialog by remember { mutableStateOf<Reward?>(null) }

    if (showDialog != null) {
        AlertDialog(
            onDismissRequest = { showDialog = null },
            title = { Text("Redeem Reward?") },
            text = { Text("Are you sure you want to redeem '${showDialog!!.title}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.redeemReward(showDialog!!.id)
                        showDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00704A))
                ) { Text("Redeem") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = null }) { Text("Cancel") }
            }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color(0xFF0D1B2A)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Text(text = "My Rewards", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = Color.White) }
        items(rewards) { reward ->
            RewardItemCard(reward = reward, onRedeem = { showDialog = reward })
        }
    }
}

@Composable
fun RewardItemCard(reward: Reward, onRedeem: () -> Unit) {
    val cardColor = when {
        reward.redeemed -> Color.Gray.copy(alpha = 0.5f)
        reward.available -> Color(0xFF00704A)
        else -> Color(0xFF1B263B)
    }
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = cardColor)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = if (reward.available) Icons.Default.CheckCircle else Icons.Default.Lock, contentDescription = null, tint = if (reward.available) Color(0xFFD4AF37) else Color.Gray)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(text = reward.title, color = Color.White, fontWeight = FontWeight.Bold)
                Text(text = reward.points, color = if (reward.available) Color(0xFFD4AF37) else Color.Gray)
            }
            if (reward.redeemed) {
                Text(text = "REDEEMED", color = Color.LightGray, fontWeight = FontWeight.Bold)
            } else if (reward.available) {
                Button(onClick = onRedeem, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))) {
                    Text(text = "Redeem", color = Color.Black)
                }
            }
        }
    }
}


@Composable
fun ProfileScreen(paddingValues: PaddingValues, viewModel: StarbucksViewModel) {
    val user by viewModel.user
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color(0xFF0D1B2A)),
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(vertical = 24.dp)) {
                Box(modifier = Modifier.size(100.dp).background(Color(0xFF00704A), CircleShape), contentAlignment = Alignment.Center) {
                    Text(text = user.name.split(" ").mapNotNull { it.firstOrNull() }.joinToString(""), color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(16.dp))
                Text(text = user.name, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = user.membershipLevel, color = Color(0xFFD4AF37), fontSize = 16.sp)
            }
        }
        val menuItems = listOf(
            Triple("Payment Methods", Icons.Default.CreditCard) { viewModel.showPaymentMethodsScreen(true) },
            Triple("Order History", Icons.Default.History) { viewModel.showOrderHistoryScreen(true) },
            Triple("Favorites", Icons.Default.Favorite) { viewModel.showFavoritesScreen(true) },
            Triple("Settings", Icons.Default.Settings) { viewModel.showSettingsScreen(true) },
            Triple("Help & Support", Icons.Default.Help) { viewModel.showHelpScreen(true) }
        )
        items(menuItems) { (title, icon, onClick) ->
            ProfileMenuItem(title = title, icon = icon, isDestructive = false, onClick = onClick)
        }
        item {
            ProfileMenuItem(title = "Sign Out", icon = Icons.Default.ExitToApp, isDestructive = true, onClick = {})
        }
    }
}

@Composable
fun ProfileMenuItem(title: String, icon: ImageVector, isDestructive: Boolean, onClick: () -> Unit) {
    val contentColor = if (isDestructive) Color.Red else Color.White
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), colors = CardDefaults.cardColors(containerColor = Color(0xFF1B263B))) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = title, tint = contentColor)
            Spacer(Modifier.width(16.dp))
            Text(text = title, color = contentColor, modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
        }
    }
}


// --- Overlay Screens ---
@Composable
fun BaseOverlayScreen(title: String, onBack: () -> Unit, actions: @Composable RowScope.() -> Unit = {}, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF0D1B2A)).clickable(enabled = false, onClick = {}).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White) }
            Text(text = title, style = MaterialTheme.typography.headlineMedium, color = Color.White, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Row(modifier = Modifier.width(48.dp), horizontalArrangement = Arrangement.End, content = actions)
        }
        Spacer(Modifier.height(16.dp))
        content()
    }
}

@Composable
fun CartScreen(onBack: () -> Unit, viewModel: StarbucksViewModel) {
    val cartItems = viewModel.cart
    val total = cartItems.sumOf { it.finalPrice * it.quantity }
    val user by viewModel.user

    BaseOverlayScreen(title = "My Cart", onBack = onBack) {
        if (cartItems.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) { Text("Your cart is empty.", color = Color.Gray) }
        } else {
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(cartItems, key = { it.id }) { item -> CartItemCard(item = item, viewModel = viewModel) }
            }
        }
        Column {
            Divider(color = Color.Gray.copy(0.5f))
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Total", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                Text(text = "Rp ${"%,d".format(total)}", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            }
            Text(text = "Your balance: Rp ${"%,d".format(user.balance)}", color = if (user.balance >= total) Color.Gray else Color.Red)
            Spacer(Modifier.height(16.dp))
            Button(onClick = { viewModel.placeOrder() }, modifier = Modifier.fillMaxWidth().height(50.dp), enabled = user.balance >= total && cartItems.isNotEmpty(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00704A), disabledContainerColor = Color.Gray), shape = MaterialTheme.shapes.large) {
                Text("Pay Now", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CartItemCard(item: CartItem, viewModel: StarbucksViewModel) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Icon(imageVector = item.product.icon, contentDescription = null, tint = Color(0xFF00704A), modifier = Modifier.size(32.dp))
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(text = item.product.name, color = Color.White)
            Text(text = "Rp ${"%,d".format(item.finalPrice)}", color = Color.Gray, fontSize = 12.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(onClick = { viewModel.decreaseCartItem(item) }, modifier = Modifier.size(28.dp).background(Color.Gray.copy(0.3f), CircleShape)) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease", tint = Color.White)
            }
            Text(text = item.quantity.toString(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            IconButton(onClick = { viewModel.addCustomizedItemToCart(item.product, item.customizations) }, modifier = Modifier.size(28.dp).background(Color(0xFF00704A), CircleShape)) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Increase", tint = Color.White)
            }
        }
    }
}

@Composable
fun PaymentMethodsScreen(onBack: () -> Unit, viewModel: StarbucksViewModel) {
    val paymentMethods by viewModel.paymentMethods
    BaseOverlayScreen(title = "Payment Methods", onBack = onBack) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(paymentMethods) { method -> PaymentMethodCard(method) }
        }
    }
}

@Composable
fun PaymentMethodCard(method: PaymentMethod) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1B263B))) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = method.icon, contentDescription = method.name, tint = Color(0xFF00704A), modifier = Modifier.size(32.dp))
            Spacer(Modifier.width(16.dp))
            Column {
                Text(text = method.name, color = Color.White, fontWeight = FontWeight.Bold)
                Text(text = method.details, color = Color.Gray, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun OrderHistoryScreen(onBack: () -> Unit, viewModel: StarbucksViewModel) {
    val orderHistory = viewModel.orderHistory
    BaseOverlayScreen(title = "Order History", onBack = onBack) {
        if (orderHistory.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(text = "You have no past orders.", color = Color.Gray) }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(orderHistory) { order -> OrderHistoryCard(order) }
            }
        }
    }
}

@Composable
fun OrderHistoryCard(order: Order) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1B263B))) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = order.id, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = order.date, color = Color.Gray, fontSize = 12.sp)
            }
            Divider(color = Color.Gray.copy(0.5f))
            order.items.forEach { cartItem ->
                Row(Modifier.fillMaxWidth()) {
                    Text(text = "${cartItem.quantity}x ${cartItem.product.name}", color = Color.LightGray, modifier = Modifier.weight(1f), fontSize = 14.sp)
                    Text(text = "Rp ${"%,d".format(cartItem.finalPrice * cartItem.quantity)}", color = Color.LightGray, fontSize = 14.sp)
                }
            }
            Divider(color = Color.Gray.copy(0.5f))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(text = "Total: Rp ${"%,d".format(order.totalPrice)}", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun QRCodeScreen(onBack: () -> Unit, viewModel: StarbucksViewModel, hasPermission: Boolean, requestPermission: () -> Unit) {
    var showMethodSelection by remember { mutableStateOf(false) }
    var showAmountDialogFor by remember { mutableStateOf<String?>(null) }
    var showCamera by remember { mutableStateOf(false) }

    if (showMethodSelection) {
        TopUpMethodSelectionDialog(
            onDismiss = { showMethodSelection = false },
            onSelect = { method ->
                showMethodSelection = false
                showAmountDialogFor = method
            }
        )
    }

    showAmountDialogFor?.let { method ->
        TopUpAmountDialog(
            method = method,
            onDismiss = { showAmountDialogFor = null },
            onConfirm = { amount ->
                viewModel.topUpBalance(amount, method)
                showAmountDialogFor = null
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF0D1B2A)).padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) { IconButton(onClick = onBack) { Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White) } }
        Spacer(Modifier.weight(1f))
        Text(text = "Scan to Pay", style = MaterialTheme.typography.headlineMedium, color = Color.White)
        Spacer(Modifier.height(16.dp))

        AnimatedContent(targetState = showCamera, label = "qr-cam-switch") { isCameraVisible ->
            if (isCameraVisible) {
                if (hasPermission) {
                    CameraPreview(onQrScanned = { qrValue -> Log.d("QRCode", "Scanned: $qrValue"); onBack() })
                } else {
                    Box(Modifier.size(250.dp).clip(RoundedCornerShape(20.dp)).background(Color.Black), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Camera permission is needed.", color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 16.dp))
                            Button(onClick = requestPermission) { Text("Grant Permission") }
                        }
                    }
                }
            } else {
                Box(Modifier.size(250.dp).clip(RoundedCornerShape(20.dp)).background(Color.White), contentAlignment = Alignment.Center) {
                    Icon(imageVector = Icons.Default.QrCode2, contentDescription = "QR Code", tint = Color.Black, modifier = Modifier.size(220.dp))
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        OutlinedButton(onClick = { showCamera = !showCamera }, border = BorderStroke(1.dp, Color.White)) {
            Icon(if (showCamera) Icons.Default.QrCode else Icons.Default.CameraAlt, contentDescription = "Toggle View", tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text(if (showCamera) "Tampilkan Kode QR" else "Buka Kamera", color = Color.White)
        }

        Spacer(Modifier.height(24.dp))
        Text(text = "Balance: Rp ${"%,d".format(viewModel.user.value.balance)}", color = Color(0xFFD4AF37), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.weight(1f))
        Button(onClick = { showMethodSelection = true }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00704A)), modifier = Modifier.fillMaxWidth(0.8f).height(50.dp), shape = RoundedCornerShape(25.dp)) {
            Text(text = "Top Up Balance", color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
fun CameraPreview(onQrScanned: (String) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    Box(modifier = Modifier.size(250.dp).clip(RoundedCornerShape(20.dp))) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                cameraProviderFuture.addListener({
                    try {
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also { it.setSurfaceProvider(previewView.surfaceProvider) }
                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                        val imageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also { it.setAnalyzer(cameraExecutor, QrCodeAnalyzer(onQrScanned)) }

                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalysis)
                    } catch (e: Exception) {
                        Log.e("CameraPreview", "Use case binding failed", e)
                    }
                }, ContextCompat.getMainExecutor(ctx))
                previewView
            }
        )
    }
}

class QrCodeAnalyzer(private val onQrScanned: (String) -> Unit) : ImageAnalysis.Analyzer {
    private val scanner = BarcodeScanning.getClient(BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build())
    private var isScanning = true

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {
        if (!isScanning) {
            image.close()
            return
        }
        image.image?.let { mediaImage ->
            val inputImage = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        isScanning = false
                        barcodes.firstOrNull()?.rawValue?.let(onQrScanned)
                    }
                }
                .addOnCompleteListener {
                    image.close()
                }
        }
    }
}

@Composable
fun TopUpMethodSelectionDialog(onDismiss: () -> Unit, onSelect: (String) -> Unit) {
    val paymentOptions = listOf("BCA Virtual Account", "Mandiri Virtual Account", "Gopay", "OVO")
    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(text = "Select Top Up Method", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(16.dp))
                LazyColumn {
                    items(paymentOptions) { option ->
                        Text(option, modifier = Modifier.fillMaxWidth().clickable { onSelect(option) }.padding(vertical = 12.dp))
                    }
                }
                TextButton(onClick = onDismiss, modifier = Modifier.align(Alignment.End).padding(top = 8.dp)) {
                    Text("CANCEL")
                }
            }
        }
    }
}

@Composable
fun TopUpAmountDialog(method: String, onDismiss: () -> Unit, onConfirm: (Long) -> Unit) {
    var text by remember { mutableStateOf("") }
    AlertDialog(onDismissRequest = onDismiss,
        title = { Text("Top Up via $method") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it.filter { c -> c.isDigit() } },
                label = { Text("Amount") },
                prefix = { Text("Rp ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        },
        confirmButton = { Button(onClick = { onConfirm(text.toLongOrNull() ?: 0L) }) { Text("Confirm") } },
        dismissButton = { TextButton(onClick = onDismiss, modifier = Modifier.padding(end=8.dp)) { Text("Cancel") } }
    )
}


@Composable
fun NotificationScreen(onBack: () -> Unit, viewModel: StarbucksViewModel) {
    val notifications = viewModel.notifications
    BaseOverlayScreen(title = "Notifications", onBack = onBack) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(notifications, key = { it.id }) { notification -> NotificationCard(notification) }
        }
    }
}

@Composable
fun NotificationCard(notification: NotificationItem) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1B263B)), border = if (notification.isUnread) BorderStroke(1.dp, Color(0xFFD4AF37)) else null) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = notification.title, color = Color.White, fontWeight = FontWeight.Bold)
            Text(text = notification.message, color = Color.Gray, fontSize = 14.sp)
            Text(text = notification.time, color = Color.Gray, fontSize = 12.sp, modifier = Modifier.align(Alignment.End))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(onBack: () -> Unit, viewModel: StarbucksViewModel) {
    val favorites = viewModel.favorites
    BaseOverlayScreen(
        title = "My Favorites",
        onBack = onBack,
        actions = {
            if (viewModel.cart.isNotEmpty()) {
                BadgedBox(
                    badge = { Badge { Text(text = "${viewModel.cart.sumOf { it.quantity }}") } }
                ) {
                    IconButton(onClick = { viewModel.showCartScreen(true) }) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = "View Cart", tint = Color(0xFFD4AF37))
                    }
                }
            }
        }
    ) {
        if (favorites.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("You haven't added any favorites yet.", color = Color.Gray)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(top=16.dp)) {
                items(favorites, key = { it.id }) { product ->
                    ProductCard(product = product, viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(product: Product, viewModel: StarbucksViewModel, onBack: () -> Unit) {
    var selectedSize by remember { mutableStateOf(product.availableSizes.firstOrNull()) }
    var selectedMilk by remember { mutableStateOf(product.availableMilks.firstOrNull()) }

    val totalPrice = product.basePrice + (selectedSize?.additionalPrice ?: 0) + (selectedMilk?.additionalPrice ?: 0)

    BaseOverlayScreen(title = product.name, onBack = onBack) {
        Column(modifier = Modifier.weight(1f)) {
            Icon(imageVector = product.icon, contentDescription = product.name, tint = Color(0xFF00704A), modifier = Modifier.size(120.dp).align(Alignment.CenterHorizontally).padding(16.dp))
            Text(text = product.description, color = Color.Gray, modifier = Modifier.padding(horizontal = 16.dp), textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))

            if (product.availableSizes.isNotEmpty()) {
                CustomizationGroup(title = "Size", options = product.availableSizes, selectedOption = selectedSize) { selectedSize = it }
            }

            if (product.availableMilks.isNotEmpty()) {
                CustomizationGroup(title = "Milk", options = product.availableMilks, selectedOption = selectedMilk) { selectedMilk = it }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Text("Price", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                Text("Rp ${"%,d".format(totalPrice)}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    val customizations = mutableMapOf<String, CustomizationOption>()
                    selectedSize?.let { customizations["Size"] = it }
                    selectedMilk?.let { customizations["Milk"] = it }
                    viewModel.addCustomizedItemToCart(product, customizations)
                    onBack()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF00704A)),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Add to Cart", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizationGroup(title: String, options: List<CustomizationOption>, selectedOption: CustomizationOption?, onOptionSelected: (CustomizationOption) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge, color = Color.White)
        Spacer(Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(options) { option ->
                FilterChip(
                    selected = option == selectedOption,
                    onClick = { onOptionSelected(option) },
                    label = { Text("${option.name} ${if (option.additionalPrice > 0) "(+Rp${option.additionalPrice/1000}k)" else ""}") }
                )
            }
        }
    }
}

@Composable
fun HelpScreen(onBack: () -> Unit) {
    val faqItems = listOf(
        "Bagaimana cara mendapatkan bintang?" to "Anda mendapatkan 1 bintang untuk setiap Rp 10.000 yang dibelanjakan menggunakan kartu Starbucks Anda yang terdaftar.",
        "Bagaimana cara menukarkan reward?" to "Anda dapat menukarkan reward Anda di kasir. Cukup informasikan kepada barista bahwa Anda ingin menggunakan reward Anda.",
        "Apakah reward saya bisa kedaluwarsa?" to "Ya, reward memiliki masa berlaku. Silakan periksa detailnya di halaman 'Rewards' untuk melihat tanggal kedaluwarsa."
    )
    BaseOverlayScreen(title = "Help & Support", onBack = onBack) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Text("Frequently Asked Questions", style = MaterialTheme.typography.titleLarge, color = Color.White)
            }
            items(faqItems) { (question, answer) ->
                var expanded by remember { mutableStateOf(false) }
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1B263B)), onClick = { expanded = !expanded }) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(question, modifier = Modifier.weight(1f), color = Color.White, fontWeight = FontWeight.Bold)
                            Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, tint = Color.Gray)
                        }
                        AnimatedVisibility(visible = expanded) {
                            Text(answer, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(onBack: () -> Unit, viewModel: StarbucksViewModel) {
    val isDarkMode by viewModel.isDarkMode
    var notificationsEnabled by remember { mutableStateOf(true) }

    BaseOverlayScreen(title = "Settings", onBack = onBack) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item { Text("ACCOUNT", style = MaterialTheme.typography.labelSmall, color = Color.Gray, modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)) }
            item { SettingsItem(title = "Edit Profile", icon = Icons.Default.Person, onClick = {}) }
            item { SettingsItem(title = "App Language", icon = Icons.Default.Language, onClick = {}) }

            item { Text("PREFERENCES", style = MaterialTheme.typography.labelSmall, color = Color.Gray, modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)) }
            item {
                SettingsItem(title = "Notifications", icon = Icons.Default.Notifications, onClick = { notificationsEnabled = !notificationsEnabled }) {
                    Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
                }
            }
            item {
                SettingsItem(title = "Dark Mode", icon = Icons.Default.DarkMode, onClick = { viewModel.toggleTheme() }) {
                    Switch(checked = isDarkMode, onCheckedChange = { viewModel.toggleTheme() })
                }
            }

            item { Text("ABOUT", style = MaterialTheme.typography.labelSmall, color = Color.Gray, modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)) }
            item { SettingsItem(title = "Privacy Policy", icon = Icons.Default.Shield, onClick = {}) }
            item { SettingsItem(title = "About This App", icon = Icons.Default.Info, onClick = {}) }
        }
    }
}

@Composable
fun SettingsItem(title: String, icon: ImageVector, onClick: () -> Unit, trailingContent: (@Composable () -> Unit)? = null) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1B263B)), onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = if (trailingContent != null) 8.dp else 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = Color.White)
            Spacer(Modifier.width(16.dp))
            Text(text = title, color = Color.White, modifier = Modifier.weight(1f))
            if (trailingContent == null) {
                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
            } else {
                trailingContent()
            }
        }
    }
}
