package com.example.luminalearn.presentation.common

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

fun Modifier.shimmerEffect(
    shape: Shape = RoundedCornerShape(12.dp),
    baseColor: Color = Color(0xFFE2E8F0),
    highlightColor: Color = Color(0xFFF8FAFC)
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "ShimmerTransition")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ShimmerAnimation"
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            baseColor,
            highlightColor,
            baseColor
        ),
        start = Offset(translateAnim - 400f, translateAnim - 400f),
        end = Offset(translateAnim, translateAnim)
    )

    this.background(brush = brush, shape = shape)
}

@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp)
) {
    Box(
        modifier = modifier.shimmerEffect(shape = shape)
    )
}

@Composable
fun LessonCardShimmerItem(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ShimmerBox(
                    modifier = Modifier
                        .width(60.dp)
                        .height(20.dp),
                    shape = RoundedCornerShape(10.dp)
                )
                ShimmerBox(
                    modifier = Modifier
                        .width(80.dp)
                        .height(16.dp),
                    shape = RoundedCornerShape(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            ShimmerBox(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(20.dp),
                shape = RoundedCornerShape(6.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            ShimmerBox(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(14.dp),
                shape = RoundedCornerShape(4.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ShimmerBox(
                    modifier = Modifier
                        .width(100.dp)
                        .height(14.dp),
                    shape = RoundedCornerShape(4.dp)
                )
                ShimmerBox(
                    modifier = Modifier
                        .width(70.dp)
                        .height(26.dp),
                    shape = RoundedCornerShape(13.dp)
                )
            }
        }
    }
}

@Composable
fun VocabCardShimmerItem(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ShimmerBox(
                        modifier = Modifier
                            .width(60.dp)
                            .height(26.dp),
                        shape = RoundedCornerShape(6.dp)
                    )
                    ShimmerBox(
                        modifier = Modifier
                            .width(70.dp)
                            .height(16.dp),
                        shape = RoundedCornerShape(4.dp)
                    )
                }
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .height(14.dp),
                    shape = RoundedCornerShape(4.dp)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ShimmerBox(
                    modifier = Modifier.size(36.dp),
                    shape = RoundedCornerShape(18.dp)
                )
                ShimmerBox(
                    modifier = Modifier.size(36.dp),
                    shape = RoundedCornerShape(18.dp)
                )
            }
        }
    }
}
