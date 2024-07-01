package com.github.pvtitov.simplewishlist.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun IndicatorComposable(
    modifier: Modifier = Modifier
) {
    val indicatorSize =
        dimensionResource(id = com.github.pvtitov.simplewishlist.R.dimen.indicator_size)
    Canvas(
        modifier = modifier
            .height(indicatorSize)
            .width(indicatorSize)
    ) {
        drawCircle(Color.Green, radius = (indicatorSize / 2).toPx())
    }
}