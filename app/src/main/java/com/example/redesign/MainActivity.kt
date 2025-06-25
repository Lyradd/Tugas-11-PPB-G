import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Define Starbucks color palette for consistency
val StarbucksGreen = Color(0xFF006241)
val StarbucksDarkGreen = Color(0xFF004D33)
val AccentGold = Color(0xFFCBAA6C)
val LightGreen = Color(0xFFD4E9E2)
val DarkGray = Color(0xFF2D2D2D)
val LightGray = Color(0xFFF2F2F2)

// --- Main App Composable ---
@Composable
fun StarbucksApp() {
    // State to manage which screen is currently selected
    var selectedScreen by remember { mutableStateOf("Beranda") }

    Scaffold(
        bottomBar = {
            StarbucksBottomNavigationBar(
                selectedScreen = selectedScreen,
                onScreenSelected = { screen -> selectedScreen = screen }
            )
        }
    ) { paddingValues ->
        // Content of the screen based on selection
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedScreen) {
                "Beranda" -> HomeScreen()
                "Bayar" -> PayScreen()
                "Pesan" -> PlaceholderScreen("Pesan", Icons.Default.ShoppingCart)
                "Hadiah" -> PlaceholderScreen("Hadiah", Icons.Default.Redeem)
                "Toko" -> PlaceholderScreen("Toko", Icons.Default.Store)
            }
        }
    }
}

// --- Screens ---

@Composable
fun HomeScreen() {
    // Mock data for UI representation
    val userName = "Daryl"
    val starBalance = 125
    val starsToNextReward = 200
    val cardBalance = "Rp 150.250"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGray)
    ) {
        // Header Section
        item {
            Header(userName = userName)
        }

        // Main Card for Balance and Stars
        item {
            BalanceAndStarsCard(
                cardBalance = cardBalance,
                starBalance = starBalance,
                starsToNextReward = starsToNextReward
            )
        }

        // Quick Actions
        item {
            QuickActions()
        }

        // Promotions Section
        item {
            SectionTitle(title = "Promosi Untukmu")
            PromotionsCarousel()
        }

        // News or Articles Section
        item {
            SectionTitle(title = "Kabar Terbaru")
            NewsItem(
                title = "Nikmati Minuman Baru Musim Ini!",
                description = "Rasakan kesegaran Strawberry Cheesecake Frappuccino."
            )
        }
    }
}

@Composable
fun PayScreen() {
    var showQrCode by remember { mutableStateOf(true) }
    var isScannerOpen by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top bar with Scanner Icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { isScannerOpen = true }) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = "Pindai QR",
                        tint = StarbucksGreen,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                "Tunjukkan untuk Membayar",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = DarkGray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Saldo Kartu: Rp 150.250",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Placeholder for QR Code or Barcode
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (showQrCode) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.QrCode,
                            contentDescription = "QR Code",
                            modifier = Modifier.size(120.dp),
                            tint = DarkGray
                        )
                        Text("Kode QR Anda", color = DarkGray, fontWeight = FontWeight.Medium)
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Placeholder for Barcode
                        Icon(
                            Icons.Default.ViewAgenda, // A simple representation of a barcode
                            contentDescription = "Barcode",
                            modifier = Modifier.size(120.dp),
                            tint = DarkGray
                        )
                        Text("Barcode Kartu Anda", color = DarkGray, fontWeight = FontWeight.Medium)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { showQrCode = !showQrCode },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = StarbucksGreen),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(
                    text = if(showQrCode) "Tampilkan Barcode Kartu" else "Tampilkan Kode QR",
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.weight(1.5f))
        }

        // Show scanner overlay if isScannerOpen is true
        if (isScannerOpen) {
            ScannerScreen(onClose = { isScannerOpen = false })
        }
    }
}

@Composable
fun ScannerScreen(onClose: () -> Unit) {
    // For a real app, you would use a library like CameraX here.
    // This is a UI placeholder.
    // You also need to request Camera permission in AndroidManifest.xml
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f))
    ) {
        // Close button
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Tutup Pemindai", tint = Color.White, modifier = Modifier.size(32.dp))
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Scanner frame
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Posisikan kode QR di dalam kotak",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun PlaceholderScreen(name: String, icon: ImageVector) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(80.dp), tint = Color.LightGray)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Halaman $name",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Gray
            )
            Text(
                text = "Fitur ini sedang dalam pengembangan.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp),
                color = Color.Gray
            )
        }
    }
}

// --- Reusable UI Components ---

@Composable
fun Header(userName: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(StarbucksGreen)
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = "Selamat Datang,",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            // Placeholder for user avatar
            Icon(
                Icons.Default.AccountCircle,
                contentDescription = "Profil",
                modifier = Modifier.size(50.dp),
                tint = Color.White
            )
        }
    }
}

@Composable
fun BalanceAndStarsCard(cardBalance: String, starBalance: Int, starsToNextReward: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Card Balance
            Text("Saldo Kartu", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
            Text(
                text = cardBalance,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = DarkGray
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            // Stars Balance
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Bintang Saya", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.StarBorder, contentDescription = "Bintang", tint = AccentGold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$starBalance",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = DarkGray
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Progress to next reward
            val progress = if (starsToNextReward > 0) {
                (starBalance.toFloat() / starsToNextReward.toFloat()).coerceIn(0f, 1f)
            } else {
                0f
            }

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = AccentGold,
                trackColor = LightGreen
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${starsToNextReward - starBalance} bintang lagi untuk hadiah berikutnya",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun QuickActions() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        QuickActionButton(title = "Isi Ulang", icon = Icons.Default.Payment, onClick = {})
        QuickActionButton(title = "Lihat Kartu", icon = Icons.Default.CreditCard, onClick = {})
        QuickActionButton(title = "Riwayat", icon = Icons.Default.Receipt, onClick = {})
    }
}

@Composable
fun QuickActionButton(title: String, icon: ImageVector, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.size(60.dp),
            shape = CircleShape,
            border = BorderStroke(1.dp, LightGreen),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(icon, contentDescription = title, tint = StarbucksGreen)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = title, style = MaterialTheme.typography.bodySmall, color = DarkGray)
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
fun PromotionsCarousel() {
    // Mock data for promotions
    val promoItems = listOf(
        "Beli 1 Gratis 1" to StarbucksDarkGreen,
        "Diskon 50% Minuman Kedua" to AccentGold,
        "Poin Ganda Hari Ini" to StarbucksGreen
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(promoItems) { (title, color) ->
            PromoItemCard(title = title, color = color)
        }
    }
}

@Composable
fun PromoItemCard(title: String, color: Color) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(160.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = "Ketuk untuk detail selengkapnya >",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun NewsItem(title: String, description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder for a news image
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(LightGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Article, contentDescription = "Berita", tint = StarbucksGreen)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(text = title, fontWeight = FontWeight.Bold, color = DarkGray)
                Text(text = description, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        }
    }
}


// --- Bottom Navigation ---
@Composable
fun StarbucksBottomNavigationBar(selectedScreen: String, onScreenSelected: (String) -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = StarbucksGreen,
    ) {
        val items = listOf(
            NavigationItem("Beranda", Icons.Default.Home),
            NavigationItem("Bayar", Icons.Default.QrCodeScanner),
            NavigationItem("Pesan", Icons.Default.ShoppingCart),
            NavigationItem("Hadiah", Icons.Default.Redeem),
            NavigationItem("Toko", Icons.Default.Store)
        )

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = selectedScreen == item.title,
                onClick = { onScreenSelected(item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = StarbucksGreen,
                    unselectedIconColor = Color.Gray.copy(0.8f),
                    selectedTextColor = StarbucksGreen,
                    unselectedTextColor = Color.Gray.copy(0.8f),
                    indicatorColor = LightGreen
                )
            )
        }
    }
}

data class NavigationItem(val title: String, val icon: ImageVector)

// --- Preview ---
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    MaterialTheme {
        StarbucksApp()
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PayScreenPreview() {
    MaterialTheme {
        PayScreen()
    }
}
