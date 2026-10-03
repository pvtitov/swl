package com.github.pvtitov.simplewishlist.ui.composable.element

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import com.github.pvtitov.simplewishlist.R

@Composable
fun imagePreviewPlaceholder(): Painter {
    val backgroundColor = MaterialTheme.colorScheme.secondaryContainer
    val contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    val textStyle = MaterialTheme.typography.bodyMedium.copy(
        color = contentColor,
        textAlign = TextAlign.Center,
    )
    val text = stringResource(R.string.wish_image_placeholder)
    val iconPainter = painterResource(R.drawable.ic_placeholder_24)
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val iconSizePx = with(density) { dimensionResource(R.dimen.icon_m).toPx() }
    val spacingPx = with(density) { dimensionResource(R.dimen.padding_s).toPx() }

    return remember(backgroundColor, contentColor, textStyle, text, iconPainter, iconSizePx, spacingPx) {
        object : Painter() {
            // Unspecified so Coil stretches the placeholder over the whole image bounds
            // instead of scaling a fixed-size bitmap with the AsyncImage's ContentScale.
            override val intrinsicSize: Size = Size.Unspecified

            override fun DrawScope.onDraw() {
                drawRect(color = backgroundColor)

                val textLayout = textMeasurer.measure(
                    text = text,
                    style = textStyle,
                    constraints = Constraints(maxWidth = size.width.toInt().coerceAtLeast(0)),
                )
                val contentHeight = iconSizePx + spacingPx + textLayout.size.height
                val top = (size.height - contentHeight) / 2f

                translate(left = (size.width - iconSizePx) / 2f, top = top) {
                    with(iconPainter) {
                        draw(
                            size = Size(iconSizePx, iconSizePx),
                            colorFilter = ColorFilter.tint(contentColor),
                        )
                    }
                }

                drawText(
                    textLayoutResult = textLayout,
                    topLeft = Offset(
                        x = (size.width - textLayout.size.width) / 2f,
                        y = top + iconSizePx + spacingPx,
                    ),
                )
            }
        }
    }
}

@Preview
@Composable
private fun ImagePreviewPlaceholderPreview() {
    Image(
        painter = imagePreviewPlaceholder(),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.wish_image_preview_height)),
    )
}
