package sample

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import sample.resources.Res
import sample.resources.poster_0
import sample.resources.poster_1
import sample.resources.poster_10
import sample.resources.poster_11
import sample.resources.poster_12
import sample.resources.poster_13
import sample.resources.poster_2
import sample.resources.poster_3
import sample.resources.poster_4
import sample.resources.poster_5
import sample.resources.poster_6
import sample.resources.poster_7
import sample.resources.poster_8
import sample.resources.poster_9

private val posters: List<DrawableResource> = listOf(
    Res.drawable.poster_0, Res.drawable.poster_1, Res.drawable.poster_2, Res.drawable.poster_3,
    Res.drawable.poster_4, Res.drawable.poster_5, Res.drawable.poster_6, Res.drawable.poster_7,
    Res.drawable.poster_8, Res.drawable.poster_9, Res.drawable.poster_10, Res.drawable.poster_11,
    Res.drawable.poster_12, Res.drawable.poster_13,
)

/** Poster picture for a movie id (800x1200 JPEG from composeResources). Cropped to fill the box. */
@Composable
fun PosterImage(id: Int, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(posters[id.mod(posters.size)]),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop,
    )
}
