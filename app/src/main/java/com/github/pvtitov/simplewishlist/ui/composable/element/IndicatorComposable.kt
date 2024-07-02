package com.github.pvtitov.simplewishlist.ui.composable.element

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.R

@Preview
@Composable
fun IndicatorComposable(
    isIgnited: Boolean = false
) {
    val indicatorSize =
        dimensionResource(id = R.dimen.indicator_size)
    val color = if (isIgnited) {
        colorResource(id = R.color.red)
    } else {
        Color.Green
    }
    Canvas(
        modifier = Modifier
            .height(indicatorSize)
            .width(indicatorSize)
    ) {
        drawCircle(color, radius = (indicatorSize / 2).toPx())
    }
}