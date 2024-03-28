package core.presentation.components.event_card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import core.presentation.components.category_chip.CategoryChip
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.location
import grabit.composeapp.generated.resources.logo
import grabit.composeapp.generated.resources.time_circle
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import ui.domain.ColorVariation
import ui.theme.DarkCardBody
import ui.theme.LightCardBody
import ui.theme.LightLime
import ui.theme.Shapes

@OptIn(ExperimentalResourceApi::class)
@Composable
fun EventCard(image: String) {

    val cardBodyBackground = if (isSystemInDarkTheme()) DarkCardBody else LightCardBody
    Card(
        elevation = 0.dp,
        backgroundColor = cardBodyBackground,
        modifier = Modifier.fillMaxWidth()
            .then(
                if (!isSystemInDarkTheme())
                    Modifier.border(
                        width = 1.dp,
                        color = Color(0x4DE5E5E5),
                        shape = RoundedCornerShape(12.dp)
                    ).shadow(
                        elevation = 12.dp,
                        spotColor = Color(0xFFE6E6E6),
                        ambientColor = Color(0xFFE5E5E5),
                        shape = RoundedCornerShape(12.dp)
                    ) else {
                    Modifier
                }
            )
    )
    {
        Box(Modifier.padding(8.dp).fillMaxWidth()) {
            Column {
                Box {
                    AsyncImage(
                        modifier = Modifier.height(192.dp).clip(RoundedCornerShape(8.dp)),
                        model = image,
                        contentDescription = null,
                        imageLoader = ImageLoader(LocalPlatformContext.current),
                        contentScale = ContentScale.Crop,
                    )

                    Box(
                        Modifier.padding(start = 8.dp, bottom = 12.dp)
                            .clip(Shapes.small)
                            .background(MaterialTheme.colors.background)
                            .align(Alignment.BottomStart)
                    ) {
                        Text(
                            modifier = Modifier.padding(
                                top = 4.dp,
                                bottom = 4.dp,
                                start = 8.dp,
                                end = 8.dp
                            ),
                            text = "0.5 kg zemiakov / hodinu",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onBackground,
                            fontWeight = FontWeight.Normal
                        )
                    }

                    Box(
                        Modifier.padding(end = 8.dp, top = 8.dp)
                            .clip(Shapes.small)
                            .background(LightLime)
                            .align(Alignment.TopEnd)
                    ) {
                        Text(
                            modifier = Modifier.padding(
                                top = 4.dp,
                                bottom = 4.dp,
                                start = 8.dp,
                                end = 8.dp
                            ),
                            text = "Tag here",
                            style = MaterialTheme.typography.body2,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    }
                }

                Column(Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 24.dp)) {
                    Text(
                        "This is name of an event",
                        style = MaterialTheme.typography.h2,
                        color = MaterialTheme.colors.onBackground
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CategoryChip("🥔 Zemiaky", color = ColorVariation.LIME)
                        CategoryChip("🍠 Reťkovka", color = ColorVariation.CHERRY)
                    }
                    Spacer(Modifier.height(24.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                imageVector = vectorResource(Res.drawable.location),
                                contentDescription = stringResource(Res.string.logo),
                                tint = MaterialTheme.colors.secondary
                            )
                            Text(
                                "Družstvo nemšová",
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.secondary
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                imageVector = vectorResource(Res.drawable.time_circle),
                                contentDescription = stringResource(Res.string.logo),
                                tint = MaterialTheme.colors.secondary
                            )
                            Text(
                                "21.4.2024, od 7:00",
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.secondary
                            )
                        }
                    }
                }

            }

        }
    }

}