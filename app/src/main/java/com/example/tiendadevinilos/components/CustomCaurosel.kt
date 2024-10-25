package com.example.tiendadevinilos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import com.example.tiendadevinilos.model.ProductModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState


@OptIn(ExperimentalPagerApi::class)
@Composable
fun CustomCarousel(
    productLists: ArrayList<ProductModel>,
    navController: NavController
) {

    Column(
        Modifier
            .size(250.dp)
            .padding()
            .background(Color.White)
    ) {
        val pagerState = rememberPagerState()
        HorizontalPager(
            count = 6,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 32.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) { page ->
            PagerSampleItem(
                page = page,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("productDetail/${productLists[page].id_vinyl}")
                    }
                    .padding(horizontal = 8.dp),
                image = productLists[page].img_url
            )
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp),
            activeColor = Color.Black,
            inactiveColor = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
        )
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
internal fun PagerSampleItem(
    page: Int,
    modifier: Modifier = Modifier,
    image: String,
) {
    Box(
        modifier = modifier

            .background(
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        AsyncImage(
            model = image,
            contentDescription = "Carousel image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()

                .clip(RoundedCornerShape(16.dp))
        )

    }
}