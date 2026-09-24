package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.ui.composable.common.Navigation
import com.github.pvtitov.simplewishlist.ui.model.Screen

@Preview
@Composable
fun LoginScreen(
    navigation: Navigation = PREVIEW_NAVIGATION
) {
    val paddingM = dimensionResource(R.dimen.padding_m)
    val paddingL = dimensionResource(R.dimen.padding_l)
    val paddingXL = dimensionResource(R.dimen.padding_xl)
    val logoSize = dimensionResource(R.dimen.logo_size)
    val googleButtonHeight = dimensionResource(R.dimen.google_button_height)
    val title = stringResource(R.string.app_title)
    val subtitle = stringResource(R.string.app_subtitle)
    val signInWithGoogleContentDescription = stringResource(R.string.sign_in_with_google_content_description)
    val cancelButton = stringResource(R.string.cancel_button)

    Box(
        modifier = Modifier
            .padding(paddingM)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_awl_logo),
                contentDescription = null,
                modifier = Modifier.size(logoSize)
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

            OutlinedButton(
                onClick = { navigation.open(Screen.MyWishes) },
                modifier = Modifier.height(googleButtonHeight),
                contentPadding = PaddingValues.Zero
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_google_button),
                    contentDescription = signInWithGoogleContentDescription
                )
            }

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
    val agreementAnnouncementPrivacyPolicy = stringResource(R.string.agreement_announcement_privacy_policy)
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

private val PREVIEW_NAVIGATION = object : Navigation {
    override val currentScreenState: State<Screen>
        get() = mutableStateOf(Screen.Login)

    override val isBackAvailable: Boolean
        get() = false

    override fun open(screen: Screen) {
    }

    override fun back() {
    }
}