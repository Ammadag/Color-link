package com.example.colorlink.feature.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colorlink.core.ui.components.ColorLinkScaffold
import com.example.colorlink.core.ui.components.PrimaryGlowButton
import com.example.colorlink.core.ui.theme.ColorLinkTheme

@Composable
fun OnboardingScreen(
    state: OnboardingState,
    onIntent: (OnboardingIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { state.totalPages })

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onIntent(OnboardingIntent.PageChanged(page))
        }
    }

    LaunchedEffect(state.currentPage) {
        if (pagerState.currentPage != state.currentPage) {
            pagerState.animateScrollToPage(state.currentPage)
        }
    }

    ColorLinkScaffold(
        modifier = modifier,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ColorLinkTheme.spacing.gutter, vertical = ColorLinkTheme.spacing.unit * 2)
            ) {
                TextButton(
                    onClick = { onIntent(OnboardingIntent.SkipClicked) },
                    modifier = Modifier.align(Alignment.CenterEnd),
                    shape = ColorLinkTheme.shapes.default
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Skip",
                            style = ColorLinkTheme.typography.labelLg,
                            color = ColorLinkTheme.colors.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.width(ColorLinkTheme.spacing.unit))
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = ColorLinkTheme.colors.onSurfaceVariant.copy(alpha = 0.8f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    ) { innerModifier ->
        Column(
            modifier = innerModifier
                .fillMaxSize()
                .padding(bottom = ColorLinkTheme.spacing.stackLg),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                OnboardingPageContent(page)
            }

            OnboardingProgressIndicator(pagerState.currentPage, state.totalPages)

            Spacer(modifier = Modifier.height(ColorLinkTheme.spacing.stackLg))

            PrimaryGlowButton(
                text = if (pagerState.currentPage == state.totalPages - 1) "Start Playing" else "Next",
                onClick = { 
                    if (pagerState.currentPage < state.totalPages - 1) {
                        onIntent(OnboardingIntent.NextClicked)
                    } else {
                        onIntent(OnboardingIntent.StartPlayingClicked)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ColorLinkTheme.spacing.containerPadding)
            )
        }
    }
}

@Composable
private fun OnboardingPageContent(page: Int) {
    val title = when (page) {
        0 -> "Connect matching\ncolors"
        1 -> "Fill the whole\nboard"
        else -> "No crossing\npaths"
    }
    
    val description = when (page) {
        0 -> "Draw lines between same-colored dots. Fill the whole board without crossing paths."
        1 -> "Every cell must be part of a path. A level is only complete when the board is 100% full."
        else -> "Paths cannot overlap or cross each other. Plan your routes carefully to win."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = ColorLinkTheme.spacing.containerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(1000)) + scaleIn(tween(800, easing = FastOutSlowInEasing))
        ) {
            OnboardingHeroIllustration(page)
        }

        Spacer(modifier = Modifier.height(ColorLinkTheme.spacing.containerPadding))

        Text(
            text = title,
            style = ColorLinkTheme.typography.headlineLg.copy(
                fontWeight = FontWeight.Bold,
                lineHeight = 40.sp
            ),
            color = ColorLinkTheme.colors.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(ColorLinkTheme.spacing.stackMd))

        Text(
            text = description,
            style = ColorLinkTheme.typography.bodyMd.copy(
                lineHeight = 24.sp
            ),
            color = ColorLinkTheme.colors.onSurfaceVariant.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = ColorLinkTheme.spacing.stackMd)
        )
    }
}

@Composable
private fun OnboardingHeroIllustration(page: Int) {
    val cyan = ColorLinkTheme.colors.gameCyan
    val purple = ColorLinkTheme.colors.gamePurple
    val yellow = ColorLinkTheme.colors.gameYellow
    
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val pathProgress = remember { Animatable(0f) }
    LaunchedEffect(page) {
        pathProgress.snapTo(0f)
        pathProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(1500, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = Modifier
            .size(width = 320.dp, height = 300.dp)
            .clip(ColorLinkTheme.shapes.large)
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, Color.White.copy(alpha = 0.10f), ColorLinkTheme.shapes.large),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gridCount = 8
            val stepX = size.width / gridCount
            val stepY = size.height / gridCount
            
            for (i in 0..gridCount) {
                drawLine(
                    color = Color.White.copy(alpha = 0.02f),
                    start = Offset(i * stepX, 0f),
                    end = Offset(i * stepX, size.height),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = Color.White.copy(alpha = 0.02f),
                    start = Offset(0f, i * stepY),
                    end = Offset(size.width, i * stepY),
                    strokeWidth = 1.dp.toPx()
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(width = 240.dp, height = 140.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val startDot = Offset(size.width * 0.2f, size.height * 0.6f)
                    val endDot = Offset(size.width * 0.8f, size.height * 0.4f)
                    
                    val curvePath = Path().apply {
                        moveTo(startDot.x, startDot.y)
                        when(page) {
                            0 -> cubicTo(
                                x1 = size.width * 0.4f, y1 = size.height * 0.2f,
                                x2 = size.width * 0.6f, y2 = size.height * 0.9f,
                                x3 = endDot.x, y3 = endDot.y
                            )
                            1 -> cubicTo(
                                x1 = size.width * 0.3f, y1 = size.height * 0.8f,
                                x2 = size.width * 0.7f, y2 = size.height * 0.2f,
                                x3 = endDot.x, y3 = endDot.y
                            )
                            else -> lineTo(endDot.x, endDot.y)
                        }
                    }

                    val pathMeasure = PathMeasure()
                    pathMeasure.setPath(curvePath, false)
                    val length = pathMeasure.length
                    val partialPath = Path()
                    pathMeasure.getSegment(0f, length * pathProgress.value, partialPath, true)

                    val color1 = if (page == 1) yellow else cyan
                    val color2 = purple

                    drawPath(
                        path = partialPath,
                        brush = Brush.linearGradient(listOf(color1, color2)),
                        style = Stroke(
                            width = 14.dp.toPx(),
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        ),
                        alpha = 0.5f
                    )

                    drawPath(
                        path = partialPath,
                        color = Color.White.copy(alpha = 0.9f),
                        style = Stroke(
                            width = 4.dp.toPx(),
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )

                    drawCircle(color = color1, radius = 16.dp.toPx(), center = startDot)
                    drawCircle(color = color1.copy(alpha = 0.4f * glowAlpha), radius = 32.dp.toPx(), center = startDot)

                    drawCircle(color = color2, radius = 16.dp.toPx(), center = endDot)
                    drawCircle(color = color2.copy(alpha = 0.4f * glowAlpha), radius = 32.dp.toPx(), center = endDot)
                }
            }

            Spacer(modifier = Modifier.height(ColorLinkTheme.spacing.stackLg))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "COLOR LINK",
                    style = ColorLinkTheme.typography.headlineMd.copy(
                        letterSpacing = 4.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(ColorLinkTheme.spacing.stackSm))
                
                Box(
                    modifier = Modifier
                        .width(140.dp)
                        .height(4.dp)
                        .blur(8.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    cyan.copy(alpha = 0.8f),
                                    purple.copy(alpha = 0.8f),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Composable
private fun OnboardingProgressIndicator(current: Int, total: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(ColorLinkTheme.spacing.stackSm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(total) { index ->
            val isSelected = index == current
            val width by animateDpAsState(
                targetValue = if (isSelected) 36.dp else 12.dp,
                label = "width",
                animationSpec = tween(400, easing = FastOutSlowInEasing)
            )
            val color = if (isSelected) ColorLinkTheme.colors.primary else Color.White.copy(alpha = 0.15f)
            
            Box(
                modifier = Modifier
                    .size(width = width, height = 8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}
