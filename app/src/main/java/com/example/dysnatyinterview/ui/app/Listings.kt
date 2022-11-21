package com.example.dysnatyinterview.ui.app

import android.widget.TextView
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.dysnatyinterview.model.Article
import com.example.dysnatyinterview.viewmodel.ListingViewModel
import com.google.accompanist.coil.rememberCoilPainter
import java.text.SimpleDateFormat
import java.util.*

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@ExperimentalFoundationApi
@Composable
fun AllListings(navController: NavController,
             listingViewModel: ListingViewModel) {
    ArticleList(navController, listingViewModel, listingViewModel.mArticles, listingViewModel::itemClicked)
}

@ExperimentalFoundationApi
@Composable
fun ArticleList(
    navController: NavController,
    listingViewModel: ListingViewModel,
    articleList: List<Article>,
    onItemClicked: (article: Article) -> Unit,
) {
    val listState = rememberLazyGridState()
    LazyVerticalGrid(
        state = listState,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(articleList.count()) { index ->
            ArticleItem(navController = navController, article = articleList[index], onItemClicked = onItemClicked)
        }
    }

    listState.OnBottomReached {
        listingViewModel.getListings()
    }
}

@Composable
fun ArticleItem(navController: NavController,
                article: Article,
                onItemClicked: (article: Article) -> Unit) {

        Column(modifier = Modifier
            .padding(8.dp)
            .clickable {
                onItemClicked(article)
                navController.navigate("articleDetails")
            }) {
            Card {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = rememberCoilPainter(
                        request = article.image
                    ),
                    contentDescription = ""
                )
            }
            Text(
                text = article.title?.english ?: "",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${article.author?.username} . ${article.createdOn?.formatDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "MMM dd, yyyy")}"
            )

        }

}

@ExperimentalFoundationApi
@Composable
fun ArticleDetails(article: Article) {
    Column(modifier = Modifier
        .padding(16.dp)
        .verticalScroll(ScrollState(0))) {
        Html(text = article.content?.english ?: "")
    }
}

@Composable
fun Html(text: String) {
    AndroidView(factory = { context ->
        TextView(context).apply {
            setText(Html.fromHtml(text, ImageGetter(resources, this), null))
        }
    })
}

fun String.formatDate(originalFormat: String, toFormat: String): String? {
    var simpleDateFormatter = SimpleDateFormat(originalFormat, Locale.getDefault())
    val date = simpleDateFormatter.parse(this) ?: return null

    simpleDateFormatter = SimpleDateFormat(toFormat, Locale.getDefault())
    return simpleDateFormatter.format(date)
}


// Class to download Images which extends [Html.ImageGetter]
class ImageGetter(
    private val res: Resources,
    private val htmlTextView: TextView
) : Html.ImageGetter {

    // Function needs to overridden when extending [Html.ImageGetter] ,
    // which will download the image
    override fun getDrawable(url: String): Drawable {
        val holder = BitmapDrawablePlaceHolder(res, null)

        // Coroutine Scope to download image in Background
        GlobalScope.launch(Dispatchers.IO) {
            runCatching {

                // downloading image in bitmap format using [Picasso] Library
                val bitmap = Picasso.get().load(url).get()
                val drawable = BitmapDrawable(res, bitmap)

                // To make sure Images don't go out of screen , Setting width less
                // than screen width, You can change image size if you want
                val width = getScreenWidth() - 150

                // Images may stretch out if you will only resize width,
                // hence resize height to according to aspect ratio
                val aspectRatio: Float =
                    (drawable.intrinsicWidth.toFloat()) / (drawable.intrinsicHeight.toFloat())
                val height = width / aspectRatio
                drawable.setBounds(10, 20, width, height.toInt())
                holder.setDrawable(drawable)
                holder.setBounds(10, 20, width, height.toInt())
                withContext(Dispatchers.Main) {
                    htmlTextView.text = htmlTextView.text
                }
            }
        }
        return holder
    }

    // Actually Putting images
    internal class BitmapDrawablePlaceHolder(res: Resources, bitmap: Bitmap?) :
        BitmapDrawable(res, bitmap) {
        private var drawable: Drawable? = null

        override fun draw(canvas: Canvas) {
            drawable?.run { draw(canvas) }
        }

        fun setDrawable(drawable: Drawable) {
            this.drawable = drawable
        }
    }

    // Function to get screenWidth used above
    fun getScreenWidth() =
        Resources.getSystem().displayMetrics.widthPixels
}


@Composable
fun LazyGridState.OnBottomReached(
    loadMore: () -> Unit
) {
    val shouldLoadMore = remember {
        derivedStateOf {

            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?:
                return@derivedStateOf true

            lastVisibleItem.index >= layoutInfo.totalItemsCount - 4
        }
    }

    LaunchedEffect(shouldLoadMore){
        snapshotFlow { shouldLoadMore.value }
            .collect {
                if (it) loadMore()
            }
    }
}