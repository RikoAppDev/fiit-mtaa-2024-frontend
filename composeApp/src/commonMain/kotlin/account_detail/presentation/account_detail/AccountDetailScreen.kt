package account_detail.presentation.account_detail

import account_detail.presentation.account_detail.component.AccountDetailComponent
import account_detail.presentation.account_detail.component.AccountDetailScreenEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import auth.domain.model.AccountType
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import core.presentation.components.button_primary.ButtonPrimary
import core.presentation.components.filled_input.FilledInput
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.account_detail__discard_changed
import grabit.composeapp.generated.resources.account_detail__save_changes
import grabit.composeapp.generated.resources.account_detail__screen_title
import grabit.composeapp.generated.resources.account_detail__update
import grabit.composeapp.generated.resources.company_name
import grabit.composeapp.generated.resources.log_out
import grabit.composeapp.generated.resources.logo_dark
import grabit.composeapp.generated.resources.logout
import grabit.composeapp.generated.resources.phone_number
import grabit.composeapp.generated.resources.profile
import grabit.composeapp.generated.resources.your_name
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import ui.domain.ColorVariation
import ui.theme.LightGrey
import ui.theme.LightOnOrange

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailScreen(
    component: AccountDetailComponent
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val phoneNumber by component.phoneNumber.subscribeAsState()
    val name by component.name.subscribeAsState()
    val isEditing by component.isEditing.subscribeAsState()
    val accountType = component.accountType
    val focusRequester = remember { FocusRequester() }

    val snackbarHostState by component.snackbarHostState.subscribeAsState()


    val nameFieldCopy = when (accountType) {
        AccountType.HARVESTER -> stringResource(Res.string.your_name)
        AccountType.ORGANISER -> stringResource(Res.string.company_name)
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier.navigationBarsPadding()
                    .padding(bottom = 24.dp, start = 12.dp, end = 12.dp),
                hostState = snackbarHostState,
                snackbar = { data ->
                    Snackbar(backgroundColor = MaterialTheme.colors.error) {
                        Text(
                            data.message,
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onSurface
                        )
                    }

                })
        },
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colors.surface,
                    titleContentColor = Color.Black,
                ),
                title = {
                    Text(
                        stringResource(Res.string.account_detail__screen_title),
                        style = MaterialTheme.typography.h3,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colors.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        component.onEvent(AccountDetailScreenEvent.NavigateBack)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description",
                            tint = LightOnOrange
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },

        ) {
        Column(
            Modifier.background(MaterialTheme.colors.background)
                .padding(24.dp).fillMaxWidth().fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Column(
                Modifier.padding(8.dp).clip(CircleShape).size(128.dp)
                    .background(MaterialTheme.colors.surface),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                Icon(
                    modifier = Modifier.size(80.dp),
                    imageVector = vectorResource(Res.drawable.profile),
                    contentDescription = stringResource(Res.string.account_detail__screen_title),
                    tint = MaterialTheme.colors.onBackground
                )
            }

            Column(
                Modifier.padding(top = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledInput(
                    modifier = Modifier.focusRequester(focusRequester),
                    value = name,
                    enabled = isEditing,
                    onValueChange = {
                        component.onEvent(AccountDetailScreenEvent.ChangeName(it))
                    },
                    label = nameFieldCopy
                )

                FilledInput(
                    value = phoneNumber,
                    enabled = isEditing,
                    onValueChange = {
                        component.onEvent(AccountDetailScreenEvent.ChangePhone(it))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    ),
                    label = stringResource(Res.string.phone_number)
                )
            }
            Spacer(Modifier.height(32.dp))

            if (isEditing) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ButtonPrimary(
                        ColorVariation.LIME,
                        onClick = {
                            component.onEvent(AccountDetailScreenEvent.SaveChanges)
                        },
                        text = stringResource(Res.string.account_detail__save_changes)
                    )

                    ButtonPrimary(
                        ColorVariation.CHERRY,
                        onClick = {
                            component.onEvent(AccountDetailScreenEvent.DiscardChanges)
                        },
                        text = stringResource(Res.string.account_detail__discard_changed)
                    )
                }
            } else {
                ButtonPrimary(
                    ColorVariation.ORANGE,
                    onClick = {
                        component.onEvent(AccountDetailScreenEvent.UpdateAccount)
                    },

                    text = stringResource(Res.string.account_detail__update)
                )
            }

            Box(Modifier.fillMaxHeight()) {
                Row(
                    Modifier.align(Alignment.BottomCenter).navigationBarsPadding()
                        .padding(bottom = 24.dp).clickable {
                            component.onEvent(AccountDetailScreenEvent.LogOut)
                        },
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(Res.string.log_out),
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.body1
                    )
                    Icon(
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colors.error,
                        imageVector = vectorResource(Res.drawable.logout),
                        contentDescription = stringResource(Res.string.log_out),
                    )

                }
            }
        }
    }
}
