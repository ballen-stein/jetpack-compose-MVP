package com.example.filmenthusiasts.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.filmenthusiasts.R
import com.example.filmenthusiasts.main.HomeActivity
import com.example.filmenthusiasts.model.MovieData

class MovieScreenDisplay {

    companion object {
        @Composable
        fun MovieRow(movieData: MovieData, index: Int?, onMovieClick: (MovieData) -> Unit) {
            Row(modifier = Modifier
                .clickable(onClick = { onMovieClick(movieData) })
                .wrapContentHeight()
                .padding(4.dp)
            ) {

                val imageModifier = Modifier
                    .size(75.dp)

                index?.let {
                    Column (modifier = Modifier.width(32.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(),
                            text = (index).toString(),
                            style = MaterialTheme.typography.h6,
                            color = if (index <= 10) colorGold else Color.White
                        )
                    }
                }

                movieData.getBitmapPoster()?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        modifier = imageModifier,
                        contentScale = ContentScale.Fit
                    )
                }

                Column (modifier = Modifier.padding(start = 8.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Text(modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(), text = movieData.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.h6, color = Color.White)
                }
            }
        }

        private var colorPurple: Color? = null
        private var colorGold: Color = Color.Yellow

        @Composable
        fun MovieList(movieList: List<MovieData>, context: Context, onlyShowTopTen: Boolean = false) {
            colorPurple = Color(context.getColor(R.color.purple_700))
            colorGold = Color.Yellow

            Column {
                val headerText = if (onlyShowTopTen) { context.getString(R.string.data_top_films) } else { context.getString(
                    R.string.data_all_films) }

                Row {
                    Surface(color = Color(context.getColor(R.color.purple_700)), modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth())
                    {
                        Text(modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .padding(8.dp),
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            text = headerText)
                    }
                }

                LazyColumn {
                    items(movieList) { movieData ->
                        Surface(color = Color(context.getColor(R.color.surface)), modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth())
                        {
                            MovieRow(
                                movieData = movieData,
                                index = movieList.indexOf(movieData) + 1,
                                onMovieClick = {
                                    if (context is HomeActivity)
                                        context.showMessage(it)
                                }
                            )

                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(colorGold)
                            )
                        }
                    }
                }
            }
        }

        @Composable
        fun MovieFragmentData(movieData: MovieData, context: Context) {
            val baseModifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()

            Surface(color = Color(context.getColor(R.color.surface)), modifier = baseModifier) {
                Column (modifier = baseModifier
                    .padding(4.dp)) {
                    Column (modifier = baseModifier, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        // Movie Title
                        Text(
                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .padding(8.dp),
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            text = movieData.name,
                            style = MaterialTheme.typography.h6,
                            color = colorGold
                        )
                        // Movie Poser
                        movieData.getBitmapPoster()?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .padding(8.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    Column(modifier = baseModifier
                        .padding(4.dp)) {
                        Row(modifier = baseModifier.padding(0.dp, 8.dp)) {
                            // Description Title
                            Text(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .wrapContentWidth(),
                                fontSize = 13.sp,
                                textAlign = TextAlign.Left,
                                text = context.getString(R.string.data_description),
                                style = MaterialTheme.typography.h6,
                                color = colorGold
                            )
                            // Runtime
                            Text(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .fillMaxWidth(),
                                fontSize = 13.sp,
                                textAlign = TextAlign.Right,
                                text = movieData.duration,
                                style = MaterialTheme.typography.h6,
                                color = colorGold
                            )
                        }
                        // Description Text
                        Text(
                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth(),
                            fontSize = 11.sp,
                            textAlign = TextAlign.Left,
                            text = movieData.description,
                            style = MaterialTheme.typography.body1,
                            color = Color.White
                        )
                        // Director
                        Text(
                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .padding(0.dp, 8.dp),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Right,
                            text = "${context.getString(R.string.data_director)} ${movieData.director}",
                            style = MaterialTheme.typography.h6,
                            color = colorGold
                        )
                    }

                    Column(modifier = baseModifier
                        .padding(4.dp)) {
                        // Genres Title
                        Text(
                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .padding(0.dp, 0.dp, 0.dp, 2.dp),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Left,
                            text = "Actors",
                            style = MaterialTheme.typography.h4,
                            color = colorGold
                        )
                        // Actors text
                        Text(
                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth(),
                            fontSize = 11.sp,
                            textAlign = TextAlign.Left,
                            text = movieData.actors?.joinToString(prefix = "", postfix = "", separator = ", ") ?: "",
                            style = MaterialTheme.typography.body1,
                            color = Color.White
                        )
                        // Genres Title
                        Text(
                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .padding(0.dp, 0.dp, 8.dp, 2.dp),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Left,
                            text = context.getString(R.string.data_genre),
                            style = MaterialTheme.typography.h4,
                            color = colorGold
                        )
                        // Genres text
                        Text(
                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth(),
                            fontSize = 11.sp,
                            textAlign = TextAlign.Left,
                            text = movieData.genres?.joinToString(prefix = "", postfix = "", separator = " | ") ?: "",
                            style = MaterialTheme.typography.body1,
                            color = Color.White
                        )
                    }

                    Row(modifier = baseModifier.padding(16.dp)) {
                        Button(onClick = { if (context is HomeActivity) context.launchBuyingWebView()  },
                            modifier = baseModifier,
                        )
                        {
                            Text(text = context.getString(R.string.data_purchase_tickets))
                        }
                    }
                }
            }
        }
    }
}