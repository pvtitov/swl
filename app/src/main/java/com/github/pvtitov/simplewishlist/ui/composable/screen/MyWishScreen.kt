package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.ui.composable.element.imagePreviewPlaceholder

@Preview
@Composable
fun MyWishScreen(
    wishIndex: Int = PREVIEW_INDEX,
    onTopBarTitle: (String) -> Unit = {}
) {
    val (imageUrl, setImageUrl) = remember { mutableStateOf<String?>(null) }
    val (title, setTitle) = remember { mutableStateOf<String?>(null) }
    val (description, setDescription) = remember { mutableStateOf<String?>(null) }
    val (wishUrl, setWishUrl) = remember { mutableStateOf<String?>(null) }
    val paddingM = dimensionResource(id = R.dimen.padding_m)
    val imagePreviewHeight = dimensionResource(id = R.dimen.wish_image_preview_height)
    val borderWidth = dimensionResource(id = R.dimen.border_width)
    val imagePreviewCornerRadius = dimensionResource(id = R.dimen.corner_radius_m)
    val imagePreviewShape = remember { RoundedCornerShape(imagePreviewCornerRadius)}
    val borderColor = MaterialTheme.colorScheme.secondary
    val imageUrlField = stringResource(R.string.wish_image_url_field)
    val titleField = stringResource(R.string.wish_field_title)
    val descriptionField = stringResource(R.string.wish_field_description)
    val linkField = stringResource(R.string.wish_field_url)

    onTopBarTitle("wish $wishIndex") // TODO replace test implementation

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingM),
        verticalArrangement = Arrangement.spacedBy(paddingM)
    ) {
        val modifier = Modifier.fillMaxWidth()
        val nextFieldKeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)

        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = modifier
                .height(imagePreviewHeight)
                .drawWithContent {
                    drawContent()
                    val strokeWidth = borderWidth.toPx()
                    val pathEffect =
                        PathEffect.dashPathEffect(floatArrayOf(strokeWidth, strokeWidth))
                    val offset = borderWidth.value
                    drawRoundRect(
                        color = borderColor,
                        topLeft = Offset(offset, offset),
                        size = Size(size.width - strokeWidth, size.height - strokeWidth),
                        style = Stroke(width = strokeWidth, pathEffect = pathEffect),
                        cornerRadius = CornerRadius(imagePreviewCornerRadius.toPx())
                    )
                }
                .clip(imagePreviewShape),
            placeholder = imagePreviewPlaceholder(),
            error = imagePreviewPlaceholder(),
            contentScale = ContentScale.Crop,
        )

        OutlinedTextField(
            value = imageUrl ?: "",
            onValueChange = { setImageUrl(it) },
            modifier = modifier,
            label = {
                Text(imageUrlField)
            },
            singleLine = true,
            keyboardOptions = nextFieldKeyboardOptions,
        )

        OutlinedTextField(
            value = title ?: "",
            onValueChange = {
                setTitle(it)
            },
            modifier = modifier,
            label = {
                Text(titleField)
            },
            singleLine = true,
            keyboardOptions = nextFieldKeyboardOptions,
        )

        OutlinedTextField(
            value = description ?: "",
            onValueChange = {
                setDescription(it)
            },
            modifier = modifier,
            label = {
                Text(descriptionField)
            },
            keyboardOptions = nextFieldKeyboardOptions,
        )

        OutlinedTextField(
            value = wishUrl ?: "",
            onValueChange = {
                setWishUrl(it)
            },
            modifier = modifier,
            label = {
                Text(linkField)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        )
    }
}

private const val PREVIEW_INDEX = -1