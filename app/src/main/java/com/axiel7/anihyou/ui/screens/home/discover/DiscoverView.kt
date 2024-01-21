package com.axiel7.anihyou.ui.screens.home.discover

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.axiel7.anihyou.type.MediaType
import com.axiel7.anihyou.ui.common.navigation.NavActionManager
import com.axiel7.anihyou.ui.composables.list.OnBottomReached
import com.axiel7.anihyou.ui.screens.home.discover.content.AiringContent
import com.axiel7.anihyou.ui.screens.home.discover.content.SeasonAnimeContent
import com.axiel7.anihyou.ui.screens.home.discover.content.TrendingMediaContent
import com.axiel7.anihyou.ui.screens.mediadetails.edit.EditMediaSheet
import com.axiel7.anihyou.ui.theme.AniHyouTheme
import com.axiel7.anihyou.utils.DateUtils.currentAnimeSeason
import com.axiel7.anihyou.utils.DateUtils.nextAnimeSeason
import java.time.LocalDateTime

enum class DiscoverInfo {
    AIRING,
    THIS_SEASON,
    TRENDING_ANIME,
    NEXT_SEASON,
    TRENDING_MANGA,
}

@Composable
fun DiscoverView(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    navActionManager: NavActionManager
) {
    val viewModel: DiscoverViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DiscoverContent(
        uiState = uiState,
        event = viewModel,
        navActionManager = navActionManager,
        modifier = modifier,
        contentPadding = contentPadding,
    )
}

@Composable
private fun DiscoverContent(
    uiState: DiscoverUiState,
    event: DiscoverEvent?,
    navActionManager: NavActionManager,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val listState = rememberLazyListState()
    listState.OnBottomReached(buffer = 0, onLoadMore = { event?.addNextInfo() })

    val haptic = LocalHapticFeedback.current
    var showEditSheet by remember { mutableStateOf(false) }

    fun showEditSheetAction() {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        showEditSheet = true
    }

    if (showEditSheet && uiState.selectedMediaDetails != null) {
        EditMediaSheet(
            mediaDetails = uiState.selectedMediaDetails,
            listEntry = uiState.selectedMediaListEntry,
            onEntryUpdated = {
                //TODO: update corresponding list item
                //viewModel.onUpdateListEntry(updatedListEntry)
            },
            onDismissed = { showEditSheet = false }
        )
    }

    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = contentPadding
    ) {
        items(uiState.infos) { item ->
            when (item) {
                DiscoverInfo.AIRING -> {
                    LaunchedEffect(uiState.airingOnMyList) {
                        if (uiState.airingOnMyList == true) event?.fetchAiringAnimeOnMyList()
                        else if (uiState.airingOnMyList == false) event?.fetchAiringAnime()
                    }
                    AiringContent(
                        airingOnMyList = uiState.airingOnMyList,
                        airingAnime = uiState.airingAnime,
                        airingAnimeOnMyList = uiState.airingAnimeOnMyList,
                        isLoading = uiState.isLoadingAiring,
                        onLongClickItem = { details, listEntry ->
                            event?.selectItem(details, listEntry)
                            showEditSheetAction()
                        },
                        navigateToCalendar = navActionManager::toCalendar,
                        navigateToMediaDetails = navActionManager::toMediaDetails,
                    )
                }

                DiscoverInfo.THIS_SEASON -> {
                    LaunchedEffect(uiState.nowAnimeSeason) {
                        event?.fetchThisSeasonAnime()
                    }
                    SeasonAnimeContent(
                        animeSeason = uiState.nowAnimeSeason,
                        seasonAnime = uiState.thisSeasonAnime,
                        isLoading = uiState.isLoadingThisSeason,
                        isNextSeason = false,
                        onLongClickItem = {
                            event?.selectItem(
                                details = it.basicMediaDetails,
                                listEntry = it.mediaListEntry?.basicMediaListEntry
                            )
                            showEditSheetAction()
                        },
                        navigateToAnimeSeason = navActionManager::toAnimeSeason,
                        navigateToMediaDetails = navActionManager::toMediaDetails,
                    )
                }

                DiscoverInfo.TRENDING_ANIME -> {
                    LaunchedEffect(MediaType.ANIME) {
                        event?.fetchTrendingAnime()
                    }
                    TrendingMediaContent(
                        mediaType = MediaType.ANIME,
                        trendingMedia = uiState.trendingAnime,
                        isLoading = uiState.isLoadingTrendingAnime,
                        onLongClickItem = {
                            event?.selectItem(
                                details = it.basicMediaDetails,
                                listEntry = it.mediaListEntry?.basicMediaListEntry
                            )
                            showEditSheetAction()
                        },
                        navigateToExplore = navActionManager::toExplore,
                        navigateToMediaDetails = navActionManager::toMediaDetails,
                    )
                }

                DiscoverInfo.NEXT_SEASON -> {
                    LaunchedEffect(uiState.nextAnimeSeason) {
                        event?.fetchNextSeasonAnime()
                    }
                    SeasonAnimeContent(
                        animeSeason = uiState.nextAnimeSeason,
                        seasonAnime = uiState.nextSeasonAnime,
                        isLoading = uiState.isLoadingNextSeason,
                        isNextSeason = true,
                        onLongClickItem = {
                            event?.selectItem(
                                details = it.basicMediaDetails,
                                listEntry = it.mediaListEntry?.basicMediaListEntry
                            )
                            showEditSheetAction()
                        },
                        navigateToAnimeSeason = navActionManager::toAnimeSeason,
                        navigateToMediaDetails = navActionManager::toMediaDetails,
                    )
                }

                DiscoverInfo.TRENDING_MANGA -> {
                    LaunchedEffect(MediaType.MANGA) {
                        event?.fetchTrendingManga()
                    }
                    TrendingMediaContent(
                        mediaType = MediaType.MANGA,
                        trendingMedia = uiState.trendingManga,
                        isLoading = uiState.isLoadingTrendingManga,
                        onLongClickItem = {
                            event?.selectItem(
                                details = it.basicMediaDetails,
                                listEntry = it.mediaListEntry?.basicMediaListEntry
                            )
                            showEditSheetAction()
                        },
                        navigateToExplore = navActionManager::toExplore,
                        navigateToMediaDetails = navActionManager::toMediaDetails,
                    )
                }
            }
        }
    }//: LazyColumn
}

@Preview
@Composable
fun DiscoverViewPreview() {
    val now = remember { LocalDateTime.now() }
    AniHyouTheme {
        Surface {
            DiscoverContent(
                uiState = DiscoverUiState(
                    infos = DiscoverInfo.entries.toMutableStateList(),
                    nowAnimeSeason = now.currentAnimeSeason(),
                    nextAnimeSeason = now.nextAnimeSeason(),
                ),
                event = null,
                navActionManager = NavActionManager.rememberNavActionManager(),
            )
        }
    }
}