package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.theme.PL_Gold
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(
    onBack: () -> Unit,
    authViewModel: AuthViewModel = viewModel(),
    shopViewModel: ShopViewModel = viewModel()
) {
    val user by authViewModel.currentUser.collectAsState()
    val isPurchasing by shopViewModel.isPurchasing.collectAsState()
    val purchaseError by shopViewModel.purchaseError.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("المتجر", fontWeight = FontWeight.Black) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Text("→", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }
                    },
                    actions = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Text("💎", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = (user?.xp ?: 0).toString(),
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                if (purchaseError != null) {
                    Text(
                        text = purchaseError!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        ShopHeader()
                    }

                    items(shopViewModel.shopItems) { item ->
                        ShopItemCard(
                            item = item,
                            canAfford = (user?.xp ?: 0) >= item.price,
                            isPurchasing = isPurchasing,
                            onPurchase = {
                                shopViewModel.purchaseItem(item, user?.xp ?: 0) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("تم شراء ${item.nameAr} بنجاح! 🎉")
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShopHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = PL_Gold.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🏪", fontSize = 48.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    "مرحباً بك في متجر بليرن",
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = PL_Gold
                )
                Text(
                    "استبدل نقاطك بأشياء رائعة",
                    fontSize = 14.sp,
                    color = PL_Gold.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun ShopItemCard(
    item: ShopItem,
    canAfford: Boolean,
    isPurchasing: Boolean,
    onPurchase: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(item.icon, fontSize = 32.sp)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(item.nameAr, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(item.descriptionAr, fontSize = 12.sp, color = Color.Gray)
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("💎", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        item.price.toString(),
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )
                }
            }
            
            Button(
                onClick = onPurchase,
                enabled = canAfford && !isPurchasing,
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                if (isPurchasing) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White)
                } else {
                    Text("شراء", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
