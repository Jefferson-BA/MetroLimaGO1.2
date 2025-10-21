package com.tecsup.metrolimago1.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .graphicsLayer {
                alpha = 0.9f
                shadowElevation = 8f
                ambientShadowColor = Color.Black.copy(alpha = 0.3f)
                spotShadowColor = Color.Black.copy(alpha = 0.3f)
            }
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.25f),
                        Color.White.copy(alpha = 0.05f)
                    )
                )
            )
            .blur(24.dp)
            .padding(16.dp)
    ) {
        // Capa de luz interior simulando reflexión tipo iOS
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.12f),
                            Color.Transparent
                        )
                    )
                )
        )

        Surface(
            color = Color.White.copy(alpha = 0.08f),
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier.matchParentSize()
        ) {
            Box(modifier = Modifier.padding(20.dp)) {
                content()
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101010)
@Composable
fun PreviewGlassCard() {
    GlassCard {
        androidx.compose.material3.Text(
            "✨ Estilo GlassMorphism realista tipo iOS",
            color = Color.White
        )
    }
}
