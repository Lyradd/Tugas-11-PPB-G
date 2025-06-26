package com.starbucks.membership

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StarbucksApp() {
    var selectedTab by remember { mutableStateOf(0) }
    var showRewardDetails by remember { mutableStateOf(false) }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF00704A),
            secondary = Color(0xFFD4AF37),
            background = Color(0xFF0D1B2A),
            surface = Color(0xFF1B263B),
            onPrimary = Color.White,
            onSecondary = Color.Black,
            onBackground = Color.White,
            onSurface = Color.White
        )
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = Color(0xFF1B263B),
                    contentColor = Color.White
                ) {
                    val tabs = listOf(
                        "Home" to Icons.Default.Home,
                        "Rewards" to Icons.Default.Star,
                        "Order" to Icons.Default.ShoppingCart,
                        "Stores" to Icons.Default.LocationOn,
                        "Profile" to Icons.Default.Person
                    )

                    tabs.forEachIndexed { index, (title, icon) ->
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            icon = {
                                Icon(
                                    icon,
                                    contentDescription = title,
                                    tint = if (selectedTab == index) Color(0xFFD4AF37) else Color.Gray
                                )
                            },
                            label = {
                                Text(
                                    title,
                                    color = if (selectedTab == index) Color(0xFFD4AF37) else Color.Gray,
                                    fontSize = 10.sp
                                )
                            }
                        )
                    }
                }
            }
        ) { paddingValues ->
            when (selectedTab) {
                0 -> HomeScreen(paddingValues) { showRewardDetails = true }
                1 -> RewardsScreen(paddingValues)
                2 -> OrderScreen(paddingValues)
                3 -> StoresScreen(paddingValues)
                4 -> ProfileScreen(paddingValues)
            }
        }
    }
}

@Composable
fun HomeScreen(paddingValues: PaddingValues, onRewardClick: () -> Unit) {
    var stars by remember { mutableStateOf(287) }
    // FIX: Mengubah 'var' menjadi 'val'
    // Properti yang didelegasikan oleh animate...AsState bersifat read-only.
    val animatedStars by animateIntAsState(
        targetValue = stars,
        animationSpec = tween(1000)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF0D1B2A), Color(0xFF1B263B))
                )
            ),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Good Morning ☀️",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                    Text(
                        "Daryl",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .background(
                            Color(0xFF00704A),
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White
                    )
                }
            }
        }

        // Membership Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable { onRewardClick() },
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF00704A)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF00704A),
                                    Color(0xFF228B22)
                                ),
                                start = Offset(0f, 0f),
                                end = Offset(1000f, 1000f)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "★ GOLD MEMBER",
                                color = Color(0xFFD4AF37),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFD4AF37),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column {
                            Text(
                                "$animatedStars ⭐",
                                color = Color.White,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "113 stars to next reward",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            LinearProgressIndicator(
                                progress = 0.72f,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = Color(0xFFD4AF37),
                                trackColor = Color.White.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
            }
        }

        // Quick Actions
        item {
            Text(
                "Quick Actions",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    title = "Scan & Pay",
                    icon = Icons.Default.QrCode,
                    color = Color(0xFF00704A),
                    modifier = Modifier.weight(1f)
                )
                QuickActionCard(
                    title = "Order Ahead",
                    icon = Icons.Default.ShoppingBag,
                    color = Color(0xFFD4AF37),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Featured Offers
        item {
            Text(
                "Featured Offers",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(3) { index ->
                    OfferCard(
                        title = when(index) {
                            0 -> "Buy 1 Get 1 Free"
                            1 -> "50% Off Frappuccino"
                            else -> "Free Birthday Drink"
                        },
                        description = when(index) {
                            0 -> "Any handcrafted beverage"
                            1 -> "Today only special offer"
                            else -> "Celebrate with us!"
                        },
                        color = when(index) {
                            0 -> Color(0xFFE74C3C)
                            1 -> Color(0xFF9B59B6)
                            else -> Color(0xFFF39C12)
                        }
                    )
                }
            }
        }

        // Recent Orders
        item {
            Text(
                "Recent Orders",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        items(3) { index ->
            RecentOrderCard(
                drinkName = when(index) {
                    0 -> "Caramel Macchiato"
                    1 -> "Vanilla Latte"
                    else -> "Americano"
                },
                size = "Grande",
                price = when(index) {
                    0 -> "Rp 65,000"
                    1 -> "Rp 58,000"
                    else -> "Rp 35,000"
                },
                date = when(index) {
                    0 -> "Today, 10:30 AM"
                    1 -> "Yesterday"
                    else -> "2 days ago"
                }
            )
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable { },
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun OfferCard(
    title: String,
    description: String,
    color: Color
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(120.dp)
            .clickable { },
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                description,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun RecentOrderCard(
    drinkName: String,
    size: String,
    price: String,
    date: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1B263B)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        Color(0xFF00704A),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.LocalCafe,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    drinkName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    "$size • $date",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    price,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                TextButton(
                    onClick = { },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFD4AF37)
                    )
                ) {
                    Text("Reorder", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun RewardsScreen(paddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color(0xFF0D1B2A)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "My Rewards",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFD4AF37)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        "Available Rewards",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "You have 2 free drinks ready!",
                        color = Color.Black.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            }
        }

        items(5) { index ->
            RewardItemCard(
                title = when(index) {
                    0 -> "Free Handcrafted Drink"
                    1 -> "Free Food Item"
                    2 -> "Bonus Star Challenge"
                    3 -> "Double Star Tuesday"
                    else -> "Birthday Reward"
                },
                points = when(index) {
                    0 -> "150 ⭐"
                    1 -> "200 ⭐"
                    2 -> "Complete 3 purchases"
                    3 -> "Every Tuesday"
                    else -> "Free on your birthday"
                },
                available = index < 2
            )
        }
    }
}

@Composable
fun RewardItemCard(
    title: String,
    points: String,
    available: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        colors = CardDefaults.cardColors(
            containerColor = if (available) Color(0xFF00704A) else Color(0xFF1B263B)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                if (available) Icons.Default.CheckCircle else Icons.Default.Lock,
                contentDescription = null,
                tint = if (available) Color(0xFFD4AF37) else Color.Gray,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    points,
                    color = if (available) Color(0xFFD4AF37) else Color.Gray,
                    fontSize = 14.sp
                )
            }

            if (available) {
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD4AF37)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        "Redeem",
                        color = Color.Black,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun OrderScreen(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color(0xFF0D1B2A))
            .padding(16.dp)
    ) {
        Text(
            "Order Ahead",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF00704A)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Start Your Order",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Choose your favorite store and customize your drinks",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD4AF37)
                    ),
                    shape = RoundedCornerShape(25.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Find a Store",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun StoresScreen(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color(0xFF0D1B2A))
            .padding(16.dp)
    ) {
        Text(
            "Store Locator",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search bar placeholder
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1B263B)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Search for stores near you...",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Mock store list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(3) { index ->
                StoreCard(
                    name = when(index) {
                        0 -> "Starbucks Kuta Beach"
                        1 -> "Starbucks Denpasar Mall"
                        else -> "Starbucks Ubud Center"
                    },
                    address = when(index) {
                        0 -> "Jl. Pantai Kuta No. 123, Badung"
                        1 -> "Jl. Teuku Umar No. 456, Denpasar"
                        else -> "Jl. Monkey Forest Rd No. 789, Ubud"
                    },
                    distance = "${(index + 1) * 2}.${index + 3} km",
                    isOpen = index != 2
                )
            }
        }
    }
}

@Composable
fun StoreCard(
    name: String,
    address: String,
    distance: String,
    isOpen: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1B263B)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        name,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        address,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            if (isOpen) "Open" else "Closed",
                            color = if (isOpen) Color.Green else Color.Red,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            distance,
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }

                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        Icons.Default.Directions,
                        contentDescription = "Get directions",
                        tint = Color(0xFFD4AF37)
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(paddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color(0xFF0D1B2A)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            Color(0xFF00704A),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "AR",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Ahmad Rizki",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Gold Member",
                    color = Color(0xFFD4AF37),
                    fontSize = 16.sp
                )
            }
        }

        items(6) { index ->
            ProfileMenuItem(
                title = when(index) {
                    0 -> "Payment Methods"
                    1 -> "Order History"
                    2 -> "Favorites"
                    3 -> "Settings"
                    4 -> "Help & Support"
                    else -> "Sign Out"
                },
                icon = when(index) {
                    0 -> Icons.Default.CreditCard
                    1 -> Icons.Default.History
                    2 -> Icons.Default.Favorite
                    3 -> Icons.Default.Settings
                    4 -> Icons.Default.Help
                    else -> Icons.Default.ExitToApp
                },
                isDestructive = index == 5
            )
        }
    }
}

@Composable
fun ProfileMenuItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isDestructive: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1B263B)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (isDestructive) Color.Red else Color.White,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                title,
                color = if (isDestructive) Color.Red else Color.White,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// Additional Components for Enhanced Features

@Composable
fun QRCodeScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Text(
                "Scan & Pay",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier
                .size(280.dp)
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // QR Code placeholder
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "QR CODE",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Show this code to the cashier",
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Balance: Rp 125,000",
            color = Color(0xFFD4AF37),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00704A)
            ),
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(
                "Top Up Balance",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
fun NotificationScreen(onBack: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    "Notifications",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        items(5) { index ->
            NotificationCard(
                title = when(index) {
                    0 -> "🎉 New Reward Available!"
                    1 -> "☕ Your order is ready"
                    2 -> "⭐ Double Stars Today"
                    3 -> "🎂 Happy Birthday!"
                    else -> "📱 App Update Available"
                },
                message = when(index) {
                    0 -> "You've earned a free drink! Redeem now."
                    1 -> "Your Caramel Macchiato is ready for pickup at Kuta Beach store."
                    2 -> "Get double stars on all purchases today only!"
                    3 -> "Enjoy a free birthday drink on us!"
                    else -> "Update to the latest version for new features."
                },
                time = when(index) {
                    0 -> "2 minutes ago"
                    1 -> "15 minutes ago"
                    2 -> "1 hour ago"
                    3 -> "2 hours ago"
                    else -> "1 day ago"
                },
                isUnread = index < 2
            )
        }
    }
}

@Composable
fun NotificationCard(
    title: String,
    message: String,
    time: String,
    isUnread: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        colors = CardDefaults.cardColors(
            containerColor = if (isUnread) Color(0xFF1B263B) else Color(0xFF0F1419)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (isUnread) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color(0xFFD4AF37), CircleShape)
                        .align(Alignment.Top)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    message,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    time,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun FavoritesScreen(paddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color(0xFF0D1B2A)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "My Favorites",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        items(4) { index ->
            FavoriteItemCard(
                name = when(index) {
                    0 -> "Caramel Macchiato"
                    1 -> "Vanilla Latte"
                    2 -> "Americano"
                    else -> "Frappuccino"
                },
                size = "Grande",
                customization = when(index) {
                    0 -> "Extra shot, Oat milk"
                    1 -> "Decaf, Almond milk"
                    2 -> "Extra hot"
                    else -> "Less sweet, Extra whip"
                },
                price = when(index) {
                    0 -> "Rp 68,000"
                    1 -> "Rp 58,000"
                    2 -> "Rp 38,000"
                    else -> "Rp 72,000"
                }
            )
        }
    }
}

@Composable
fun FavoriteItemCard(
    name: String,
    size: String,
    customization: String,
    price: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1B263B)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        Color(0xFF00704A),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.LocalCafe,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    "$size • $customization",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    price,
                    color = Color(0xFFD4AF37),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Row {
                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Gray
                    )
                }
                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = "Add to cart",
                        tint = Color(0xFFD4AF37)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StarbucksAppPreview() {
    StarbucksApp()
}
