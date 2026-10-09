package com.github.pvtitov.simplewishlist.ui.composable.element

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.ui.theme.AwlTheme

// Colors, sizes, logo and typography are fixed by Google's branding guidelines
// (https://developers.google.com/identity/branding-guidelines), so they intentionally
// don't come from the app's MaterialTheme.
private class GoogleButtonColors(
    val container: Color,
    val content: Color,
    val stroke: Color
)

private val LightColors = GoogleButtonColors(
    container = Color(0xFFFFFFFF),
    content = Color(0xFF1F1F1F),
    stroke = Color(0xFF747775)
)

private val DarkColors = GoogleButtonColors(
    container = Color(0xFF131314),
    content = Color(0xFFE3E3E3),
    stroke = Color(0xFF8E918F)
)

// Static Medium (wght 500) instance of the Google Sans variable font: variable font axes
// aren't supported below API 26, and minSdk is 23.
private val GoogleSansMedium = FontFamily(Font(R.font.google_sans_medium, FontWeight.Medium))

@Composable
fun GoogleSignInButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Follows the theme actually applied rather than the system setting, so the button
    // matches the screen it sits on even if the app forces a theme.
    val colors =
        if (MaterialTheme.colorScheme.background.luminance() < 0.5f) DarkColors else LightColors

    Surface(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        shape = CircleShape,
        color = colors.container,
        contentColor = colors.content,
        border = BorderStroke(1.dp, colors.stroke)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_google_logo),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = stringResource(R.string.sign_in_with_google),
                style = TextStyle(
                    fontFamily = GoogleSansMedium,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                ),
                maxLines = 1
            )
        }
    }
}

@Preview(locale = "en")
@Preview(locale = "ru")
@Composable
private fun GoogleSignInButtonLightPreview() {
    AwlTheme(darkTheme = false) {
        GoogleSignInButton(onClick = {})
    }
}

@Preview(locale = "en")
@Preview(locale = "ru")
@Composable
private fun GoogleSignInButtonDarkPreview() {
    AwlTheme(darkTheme = true) {
        GoogleSignInButton(onClick = {})
    }
}
