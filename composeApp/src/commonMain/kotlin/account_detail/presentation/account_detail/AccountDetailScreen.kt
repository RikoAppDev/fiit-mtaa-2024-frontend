package account_detail.presentation.account_detail

import account_detail.presentation.account_detail.component.AccountDetailComponent
import account_detail.presentation.account_detail.component.AccountDetailScreenEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import auth.domain.model.AccountType
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import core.presentation.components.button_primary.ButtonPrimary
import core.presentation.components.filled_input.FilledInput
import core.presentation.components.snackbar.CustomSnackbar
import core.presentation.components.snackbar.SnackbarVisualWithError
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.account_detail__discard_changed
import grabit.composeapp.generated.resources.account_detail__save_changes
import grabit.composeapp.generated.resources.account_detail__screen_title
import grabit.composeapp.generated.resources.account_detail__update
import grabit.composeapp.generated.resources.cancel
import grabit.composeapp.generated.resources.company_name
import grabit.composeapp.generated.resources.delete
import grabit.composeapp.generated.resources.delete_account
import grabit.composeapp.generated.resources.delete_account_dialog_text
import grabit.composeapp.generated.resources.delete_account_dialog_title
import grabit.composeapp.generated.resources.harvester
import grabit.composeapp.generated.resources.log_out
import grabit.composeapp.generated.resources.logout
import grabit.composeapp.generated.resources.organiser
import grabit.composeapp.generated.resources.phone_number
import grabit.composeapp.generated.resources.profile
import grabit.composeapp.generated.resources.register_screen__role_harvester_title
import grabit.composeapp.generated.resources.register_screen__role_organiser_title
import grabit.composeapp.generated.resources.top_bar_navigation__back
import grabit.composeapp.generated.resources.your_name
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import ui.domain.ColorVariation
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
    val error by component.error.subscribeAsState()
    val snackBarText by component.snackBarText.subscribeAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    val accountType by component.accountType.subscribeAsState()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val isVisible = remember { mutableStateOf(false) }

    val nameFieldCopy = when (accountType) {
        AccountType.HARVESTER.toString() -> stringResource(Res.string.your_name)
        AccountType.ORGANISER.toString() -> stringResource(Res.string.company_name)
        else -> {
            stringResource(Res.string.your_name)
        }
    }

    val accountIcon = when (accountType) {
        AccountType.HARVESTER.toString() -> vectorResource(Res.drawable.harvester)
        AccountType.ORGANISER.toString() -> vectorResource(Res.drawable.organiser)
        else -> {
            vectorResource(Res.drawable.profile)
        }
    }

    val accountName = when (accountType) {
        AccountType.HARVESTER.toString() -> stringResource(Res.string.harvester)
        AccountType.ORGANISER.toString() -> stringResource(Res.string.organiser)
        else -> {
            ""
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize().navigationBarsPadding(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState, snackbar = {
                CustomSnackbar(
                    data = SnackbarVisualWithError(
                        snackbarData = it,
                        isError = error != "",
                    )
                )
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
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(Res.string.top_bar_navigation__back),
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
                    modifier = Modifier.size(64.dp),
                    imageVector = accountIcon,
                    contentDescription = stringResource(Res.string.account_detail__screen_title),
                    tint = MaterialTheme.colors.onBackground
                )
            }

            Text(
                accountName,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.secondary
            )

            Column(
                Modifier.padding(top = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledInput(
                    modifierOutlinedField = Modifier.focusRequester(focusRequester),
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
                            focusManager.clearFocus()
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
            Row(
                Modifier
                    .padding(top = 18.dp).clickable {
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

            Box(Modifier.fillMaxHeight()) {
                Row(
                    Modifier.align(Alignment.BottomCenter).navigationBarsPadding()
                        .padding(bottom = 18.dp).clickable {
                            showDeleteDialog = true
                        },
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(Res.string.delete_account),
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.body1
                    )
                    Icon(
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colors.error,
                        imageVector = vectorResource(Res.drawable.delete),
                        contentDescription = stringResource(Res.string.delete_account),
                    )

                }
            }
        }

        if (showDeleteDialog) {
            DeleteAccountDialog(
                onDismissRequest = {
                    showDeleteDialog = false
                },
                onConfirmation = {
                    component.onEvent(AccountDetailScreenEvent.DeleteAccount)
                },
            )
        }


        if (!isVisible.value && snackBarText != "") {
            coroutineScope.launch {
                isVisible.value = true
                val snackbarResult = snackbarHostState.showSnackbar(
                    message = snackBarText,
                    duration = SnackbarDuration.Short
                )

                when (snackbarResult) {
                    SnackbarResult.Dismissed -> {
                        isVisible.value = false
                        component.onEvent(AccountDetailScreenEvent.RemoveSnackbarText)
                    }

                    SnackbarResult.ActionPerformed -> {
                        isVisible.value = false
                        component.onEvent(AccountDetailScreenEvent.RemoveSnackbarText)
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun DeleteAccountDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    AlertDialog(
        backgroundColor = MaterialTheme.colors.background,
        title = {
            Text(
                text = stringResource(Res.string.delete_account_dialog_title),
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.onBackground
            )
        },
        text = {
            Text(
                text =  stringResource(Res.string.delete_account_dialog_text),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onBackground
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(
                    text =  stringResource(Res.string.delete_account),
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.error
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(
                    text =  stringResource(Res.string.cancel),
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.secondary

                )
            }
        }
    )
}