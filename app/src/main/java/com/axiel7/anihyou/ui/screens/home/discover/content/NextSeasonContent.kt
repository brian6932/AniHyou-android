package com.axiel7.anihyou.ui.screens.home.discover.content

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.axiel7.anihyou.R
import com.axiel7.anihyou.data.model.media.AnimeSeason
import com.axiel7.anihyou.data.repository.PagedResult
import com.axiel7.anihyou.ui.composables.list.HorizontalListHeader
import com.axiel7.anihyou.ui.composables.media.MEDIA_ITEM_VERTICAL_HEIGHT
import com.axiel7.anihyou.ui.composables.media.MediaItemVertical
import com.axiel7.anihyou.ui.composables.media.MediaItemVerticalPlaceholder
import com.axiel7.anihyou.ui.composables.scores.SmallScoreIndicator
import com.axiel7.anihyou.ui.screens.home.discover.DiscoverViewModel
import com.axiel7.anihyou.ui.screens.home.discover.composables.DiscoverLazyRow

@Composable
fun NextSeasonContent(
    viewModel: DiscoverViewModel,
    navigateToAnimeSeason: (AnimeSeason) -> Unit,
    navigateToMediaDetails: (mediaId: Int) -> Unit,
) {
    HorizontalListHeader(
        text = stringResource(R.string.next_season),
        onClick = {
            navigateToAnimeSeason(viewModel.nextAnimeSeason)
        }
    )
    val seasonAnime by viewModel.nextSeasonAnime.collectAsState()
    DiscoverLazyRow(
        minHeight = MEDIA_ITEM_VERTICAL_HEIGHT.dp
    ) {
        when (seasonAnime) {
            is PagedResult.Loading -> {
                items(10) {
                    MediaItemVerticalPlaceholder(
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            is PagedResult.Success -> {
                items((seasonAnime as PagedResult.Success).data) { item ->
                    MediaItemVertical(
                        title = item.title?.userPreferred ?: "",
                        imageUrl = item.coverImage?.large,
                        modifier = Modifier.padding(start = 8.dp),
                        subtitle = {
                            item.meanScore?.let { score ->
                                SmallScoreIndicator(score = "${score}%")
                            }
                        },
                        minLines = 2,
                        onClick = { navigateToMediaDetails(item.id) }
                    )
                }
            }

            is PagedResult.Error -> {
                item {
                    Text(text = (seasonAnime as PagedResult.Error).message)
                }
            }
        }
    }//:LazyRow
}