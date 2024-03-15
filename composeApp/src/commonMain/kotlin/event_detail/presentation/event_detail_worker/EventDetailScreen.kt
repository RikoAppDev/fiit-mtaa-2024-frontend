package event_detail.presentation.event_detail_worker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.Card
import androidx.compose.material.Colors
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import auth.presentation.login.component.LoginScreenComponent
import auth.presentation.login.component.LoginScreenEvent
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import event_detail.presentation.event_detail_worker.component.EventDetailScreenComponent
import event_detail.presentation.event_detail_worker.component.EventDetailScreenEvent
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.capacity
import grabit.composeapp.generated.resources.categories
import grabit.composeapp.generated.resources.event_detail_screen__featured_image
import grabit.composeapp.generated.resources.grabit_logo
import grabit.composeapp.generated.resources.location
import grabit.composeapp.generated.resources.login_screen__create_account
import grabit.composeapp.generated.resources.login_screen__email
import grabit.composeapp.generated.resources.login_screen__login
import grabit.composeapp.generated.resources.login_screen__logo
import grabit.composeapp.generated.resources.login_screen__no_account
import grabit.composeapp.generated.resources.login_screen__password
import grabit.composeapp.generated.resources.organizer
import grabit.composeapp.generated.resources.placeholder
import grabit.composeapp.generated.resources.placeholder_icon
import grabit.composeapp.generated.resources.starts_at
import grabit.composeapp.generated.resources.tooling
import grabit.composeapp.generated.resources.tooling_provided
import grabit.composeapp.generated.resources.tooling_required
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.theme.Apple
import ui.theme.Lemon
import ui.theme.LightGrey
import ui.theme.Lime
import ui.theme.OnApple
import ui.theme.OnLime
import ui.theme.OnOrange
import ui.theme.Orange
import ui.theme.SecondaryText
import ui.theme.Shapes
import ui.theme.Typography

@OptIn(ExperimentalResourceApi::class)
@Preview
@Composable
fun EventDetailScreen(component: EventDetailScreenComponent) {
    Box(Modifier.fillMaxHeight().verticalScroll(rememberScrollState()).padding(bottom = 112.dp).background(
        Color.White)) {
        Column(modifier = Modifier.padding(24.dp)) {
            Image(
                modifier = Modifier.fillMaxWidth().clip(Shapes.large),
                contentScale = ContentScale.Fit,
                painter = painterResource(Res.drawable.placeholder),
                contentDescription = stringResource(Res.string.event_detail_screen__featured_image),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Názov zberu",
                style = Typography.h1,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Lorem ipsum dolor  sit amet, consectetur adipiscing elit. Quisque blandit convallis eros in lobortis. Praesent sagittis sem non felis",
                style = Typography.body1,
                color = SecondaryText
            )

            Spacer(Modifier.height(40.dp))

            Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                InfoRow(
                    stringResource(Res.string.capacity),
                    Res.drawable.placeholder_icon,
                    "Free places: 4"
                )

                InfoRow(
                    stringResource(Res.string.organizer),
                    Res.drawable.placeholder_icon,
                    "Družstvo Nemšová"
                )

                InfoRow(
                    stringResource(Res.string.starts_at),
                    Res.drawable.placeholder_icon,
                    "21.06.2002 - 21:50"
                )

                Column {
                    Text(
                        text = stringResource(Res.string.tooling),
                        style = Typography.h2,
                        color = Color.Black
                    )
                    Spacer(Modifier.height(4.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            Modifier.fillMaxWidth().clip(Shapes.medium).background(LightGrey),

                            ) {
                            Column(
                                Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = stringResource(Res.string.tooling_provided),
                                    style = Typography.h3,
                                )
                                Text(
                                    text = "Rukavice , kýble, hrable",
                                    style = Typography.body1,
                                    color = SecondaryText
                                )
                            }
                        }

                        Box(
                            Modifier.fillMaxWidth().clip(Shapes.medium).background(LightGrey),

                            ) {
                            Column(
                                Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = stringResource(Res.string.tooling_required),
                                    style = Typography.h3,
                                )
                                Text(
                                    text = "Monterky, kalíšok",
                                    style = Typography.body1,
                                    color = SecondaryText
                                )
                            }
                        }
                    }
                }

                InfoRow(
                    stringResource(Res.string.location),
                    Res.drawable.placeholder_icon,
                    "Družstvo Vyšné Žrďky" +
                            "\nPalackého 24/11" +
                            "\nNemšová"
                )

                Column {
                    Text(
                        text = stringResource(Res.string.categories),
                        style = Typography.h2
                    )
                    Spacer(Modifier.height(8.dp))

                    Row(
                        Modifier,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CategoryTag("🥔 Zemiaky")
                        CategoryTag("🥔 Zemiaky")
                    }
                }
            }
        }
    }

    Box(Modifier.fillMaxSize(), Alignment.BottomCenter){
        Box(Modifier.fillMaxWidth().background(Color.White)){

            Box(Modifier.padding(20.dp, 32.dp)){
                Button(
                    onClick = {component.onEvent(EventDetailScreenEvent.SignInForEvent)},
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Apple,
                        contentColor = OnApple
                    ),
                    elevation = ButtonDefaults.elevation( // Set the elevation to zero to remove the shadow
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        disabledElevation = 0.dp
                    ),
                    shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(
                                32.dp, 20.dp, 36.dp, 20.dp
                            )

                ){
                    Text(
                        style = Typography.button,
                        text = "Prihlásiť sa na zber"
                    )
                }
            }

        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun InfoRow(title: String, icon: DrawableResource, text: String) {
    Column {
        Text(
            text = title,
            style = Typography.h2,
            color = Color.Black
        )
        Spacer(Modifier.height(4.dp))

        Row(
            Modifier,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            Image(
                modifier = Modifier.width(16.dp).height(16.dp),
                contentScale = ContentScale.Fit,
                painter = painterResource(icon),
                contentDescription = stringResource(Res.string.event_detail_screen__featured_image),
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = text,
                style = Typography.body1,
                color = SecondaryText
            )
        }
    }
}

@Composable
fun CategoryTag(text: String){
    Box(Modifier.clip(Shapes.large)){
        Text(
            text= text,
            Modifier.background(Orange).padding(16.dp, 8.dp),
            color = OnOrange,
            style = Typography.body2,
            fontWeight = FontWeight.SemiBold
        )
    }
}