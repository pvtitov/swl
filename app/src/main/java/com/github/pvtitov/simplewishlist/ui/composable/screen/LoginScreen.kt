package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.ui.composable.element.GoogleSignInButton

@Composable
fun LoginScreen(
    onLoginWithGoogle: () -> Unit
) {
    val paddingM = dimensionResource(R.dimen.padding_m)
    val paddingL = dimensionResource(R.dimen.padding_l)
    val paddingXL = dimensionResource(R.dimen.padding_xl)
    val logoSize = dimensionResource(R.dimen.logo_size)
    val title = stringResource(R.string.app_title)
    val subtitle = stringResource(R.string.app_subtitle)
    val cancelButton = stringResource(R.string.cancel_button)
    val imagePreviewCornerRadius = dimensionResource(id = R.dimen.corner_radius_m)
    val imagePreviewShape = remember { RoundedCornerShape(imagePreviewCornerRadius) }

    Box(
        modifier = Modifier.fillMaxSize()
            .padding(paddingM)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(logoSize)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(paddingL),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
            )

            Spacer(modifier = Modifier.size(paddingL))

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.displaySmall,
                softWrap = false
            )

            Spacer(modifier = Modifier.size(paddingM))

            Text(
                text = subtitle,
                fontWeight = FontWeight.Thin,
                style = MaterialTheme.typography.titleMedium,
                softWrap = false
            )

            Spacer(modifier = Modifier.size(paddingXL))

            GoogleSignInButton(
                onClick = { onLoginWithGoogle() }
            )

            Spacer(modifier = Modifier.size(paddingM))

            val activity = LocalActivity.current

            FilledTonalButton(
                onClick = { activity?.finish() }
            ) {
                Text(text = cancelButton)
            }
        }

        TermsAndPrivacyPolicy(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun TermsAndPrivacyPolicy(
    modifier: Modifier
) {
    val agreementAnnouncementStart = stringResource(R.string.agreement_announcement_start)
    val agreementAnnouncementAnd = stringResource(R.string.agreement_announcement_and)
    val agreementAnnouncementTerms = stringResource(R.string.agreement_announcement_terms)
    val agreementAnnouncementPrivacyPolicy =
        stringResource(R.string.agreement_announcement_privacy_policy)
    val termsUrl = stringResource(R.string.terms_url)
    val privacyPolicyUrl = stringResource(R.string.privacy_policy_url)

    Text(
        text = buildAnnotatedString {
            append(agreementAnnouncementStart)

            withLink(
                link = LinkAnnotation.Url(
                    url = termsUrl,
                    styles = TextLinkStyles(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline,
                        )
                    )
                )
            ) {
                append(agreementAnnouncementTerms)
            }

            append(agreementAnnouncementAnd)

            withLink(
                link = LinkAnnotation.Url(
                    url = privacyPolicyUrl,
                    styles = TextLinkStyles(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline,
                        )
                    )
                )
            ) {
                append(agreementAnnouncementPrivacyPolicy)
            }
        },
        modifier = modifier,
        fontStyle = FontStyle.Italic,
        style = MaterialTheme.typography.bodySmall
    )
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen({})
}