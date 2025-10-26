package com.tecsup.metrolimago1.ui.screens.intro

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tecsup.metrolimago1.R
import androidx.compose.ui.unit.Dp

@SuppressLint("Range")
@Composable
fun IntroScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E0E0E))
    ) {
        Image(
            painter = painterResource(R.drawable.fondo2),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.72f)
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .background(Color.White)
                .padding(66.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Comienza tu recorrido por Lima con MetroLimaGO",
                color = Color(0xFF121212),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            val infiniteTransition = rememberInfiniteTransition(label = "arrowBounce")

            val offsetX by infiniteTransition.animateValue(
                initialValue = 52.dp,
                targetValue = 60.dp,
                typeConverter = Dp.VectorConverter,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 700, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "offsetX"
            )

            Button(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("intro") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth(1.95f)
                    .height(73.dp)
                    .padding(top = 18.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Continuar",
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.labelLarge
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.Rounded.ArrowForwardIos,
                        contentDescription = "Ir",
                        tint = Color.White.copy(alpha = 0.95f),
                        modifier = Modifier
                            .size(20.dp)
                            .offset(x = offsetX)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewIntroScreen() {
    IntroScreen(navController = rememberNavController())
}
