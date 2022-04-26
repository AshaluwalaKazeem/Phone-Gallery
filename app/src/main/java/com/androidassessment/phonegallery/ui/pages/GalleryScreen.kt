package com.androidassessment.phonegallery.ui.pages

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.androidassessment.phonegallery.R
import com.androidassessment.phonegallery.model.LoadingState
import com.androidassessment.phonegallery.ui.theme.Black
import com.androidassessment.phonegallery.ui.theme.Gray500
import com.androidassessment.phonegallery.ui.theme.Purple500
import com.androidassessment.phonegallery.ui.theme.White
import com.androidassessment.phonegallery.viewmodel.MainActivityViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@OptIn(ExperimentalUnitApi::class)
@Composable
fun GalleryScreen(viewModel: MainActivityViewModel) {
    // Remember a SystemUiController
    val systemUiController = rememberSystemUiController()
    val context = LocalContext.current
    var refreshState by rememberSaveable {
        mutableStateOf(true)
    }
    var loadingState by rememberSaveable {
        mutableStateOf(LoadingState.Loading)
    }

    LaunchedEffect(key1 = true) {
        systemUiController.setSystemBarsColor(
            color = White,
            darkIcons = true
        )
        systemUiController.setNavigationBarColor(
            color = White,
            darkIcons = true
        )
        loadingState = LoadingState.Loading
        viewModel.fetchExhibitListFromServer(
            context = context,
            loadingState = {
                refreshState = false
                loadingState = it
            },
        )
    }
    Scaffold(backgroundColor = White, modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Black,
                            fontSize = MaterialTheme.typography.h5.fontSize,
                            fontWeight = FontWeight.W900
                        )
                    ) {
                        append("Phone Gallery")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Purple500,
                            fontSize = TextUnit(60f, TextUnitType.Sp),
                            fontWeight = FontWeight.W900
                        )
                    ) {
                        append(".")
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 15.dp)
            )

            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = refreshState),
                swipeEnabled = !refreshState,
                onRefresh = {
                    loadingState = LoadingState.Loading
                    viewModel.fetchExhibitListFromServer(
                        context = context,
                        loadingState = {
                            refreshState = false
                            loadingState = it
                        },
                    )
                }
            ) {
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(vertical = 10.dp, horizontal = 5.dp)) {
                    when (loadingState) {
                        LoadingState.Loading -> {
                            items(3) { index ->
                                LazyRow(
                                    modifier = Modifier
                                        .padding(start = 10.dp, bottom = 10.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    items(3) {
                                        GalleryCustomLoadingCard()
                                    }
                                }
                            }
                        }
                        LoadingState.Error -> {
                            item {
                                ErrorOccurredView()
                            }
                        }
                        LoadingState.NoInternet -> {
                            item {
                                NoInternetView()
                            }
                        }
                        LoadingState.Done -> {
                            items(viewModel.exhibitList.size) { index ->
                                LazyRow(
                                    modifier = Modifier
                                        .padding(start = 10.dp, bottom = 10.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    val exhibitData = viewModel.exhibitList[index]
                                    items(exhibitData.images.size) { index ->
                                        GalleryCustomCard(title = exhibitData.title, imageUrl = exhibitData.images[index])
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun GalleryCustomCard(
    title: String,
    imageUrl: String
) {
    Card(
        elevation = 5.dp,
        modifier = Modifier
            .width(165.dp)
            .padding(5.dp)
            .background(
                MaterialTheme.colors.surface,
                MaterialTheme.shapes.medium
            )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 2.dp)) {
            Text(text = title, style = MaterialTheme.typography.h6, textAlign = TextAlign.Center, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(5.dp))
            var painter = rememberImagePainter(
                data = imageUrl
            )
            if(painter.state is ImagePainter.State.Error) {
                painter = rememberImagePainter(
                    data = R.drawable.broken_image
                )
            }
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(170.dp, 200.dp)
                    .placeholder(
                        visible = painter.state is ImagePainter.State.Loading,
                        highlight = PlaceholderHighlight.shimmer(),
                    )
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun GalleryCustomLoadingCard() {
    Card(
        elevation = 5.dp,
        modifier = Modifier
            .width(165.dp)
            .padding(5.dp)
            .background(
                MaterialTheme.colors.surface,
                MaterialTheme.shapes.medium
            )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 2.dp)) {
            Text(text = "Text Loading",
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(5.dp)
                    .placeholder(
                        visible = true,
                        highlight = PlaceholderHighlight.shimmer(),
                    )
            )
            var painter = rememberImagePainter(
                data = "imageUrl"
            )
            if(painter.state is ImagePainter.State.Error) {
                painter = rememberImagePainter(
                    data = R.drawable.broken_image
                )
            }
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(170.dp, 200.dp)
                    .placeholder(
                        visible = true,
                        highlight = PlaceholderHighlight.shimmer(),
                    )
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun ErrorOccurredView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = rememberImagePainter(data = R.drawable.ic_undraw_server_down),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Text(
            text = "Error occurred. Please swipe down to refresh",
            style = MaterialTheme.typography.caption,
            color = Gray500
        )
    }
}

@ExperimentalFoundationApi
@Composable
fun NoInternetView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = rememberImagePainter(data = R.drawable.ic_undraw_no_internet),
            contentDescription = null,
            modifier = Modifier.size(150.dp)
        )
        Text(
            text = "No Internet. Swipe down to refresh",
            style = MaterialTheme.typography.caption,
            color = Gray500
        )
    }
}