package ir.ehsannarmani.compose_charts.ui

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiendadevinilos.ui.charts.StatisticsViewModel
import com.example.tiendadevinilos.ui.components.LoadingScreen
import com.razzaghi.compose_loading_dots.core.Dot
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie

private const val unselectedPieAlpha = 1f

val colors = listOf(
    Color(0xFFeb3b5a),
    Color(0xFFfa8231),
    Color(0xFFf7b731),
    Color(0xFF20bf6b),
    Color(0xFF0fb9b1),
    Color(0xFF8854d0),
    Color(0xFF26de81),
    Color(0xFFfd9644),
    Color(0xFFf7b731),
    Color(0xFF2bcbba),
    Color(0x3F20bf20),
    Color(0xFF4b7bec),
    Color(0xFFa55eea),
    Color(0xFFd1d8e0),
    Color(0xFF778ca3),
    Color(0xFF2d98da),
    Color(0xFFfc5c9c),
    Color(0xFFfd964c),
    Color(0xFFfa7e7a)
)
private val months = listOf(
    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
    "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
)

private val genres = listOf(
    "Pop", "Rap", "Country", "HipHop", "Reggaeton", "K-Pop"
)


@Composable
fun ChartPage(user_id: String, statisticsModel: StatisticsViewModel = viewModel()) {
    if (user_id.isNotBlank()) {
        LaunchedEffect(user_id) {
            statisticsModel.getStatistics(user_id)
        }
    }
    val isLoading by statisticsModel.isLoading.observeAsState(true)
    val response by statisticsModel.responseStatus.observeAsState(false)
    val statistics by statisticsModel.statisticsList.observeAsState(null)
    Log.d("statistics", "$statistics")

    val pieData = statistics?.mapIndexed { index, statistic ->
        Pie(
            label = statistic.genre_name ?: "",
            data = statistic.total_quantity.toDouble(),
            color = colors[index].copy(alpha = unselectedPieAlpha),
            selectedColor = colors[index]
        )
    }

    if (isLoading) {
        LoadingScreen(isLoading = isLoading)
    } else {
        PieChart(pieData = pieData ?: emptyList())
    }


}


@Composable
fun PieChart(pieData: List<Pie>) {
    var data by remember {
        mutableStateOf(pieData ?: emptyList())
    }

    val floatSpec = spring<Float>(
        dampingRatio = .3f,
        stiffness = Spring.StiffnessLow
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color.Transparent, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )

    )
    {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Mis generos favoritos",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 25.dp)
            )
            PieChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(200.dp),
                data = data,
                onPieClick = {
                    println("${it.label} Clicked")
                    val pieIndex = data.indexOf(it)
                    data =
                        data.mapIndexed { mapIndex, pie -> pie.copy(selected = pieIndex == mapIndex) }
                },
                selectedScale = 1.2f,
                spaceDegreeAnimEnterSpec = floatSpec,
                colorAnimEnterSpec = tween(300),
                scaleAnimEnterSpec = floatSpec,
                colorAnimExitSpec = tween(300),
                scaleAnimExitSpec = tween(300),
                spaceDegreeAnimExitSpec = tween(300),
                selectedPaddingDegree = 0f,
                style = Pie.Style.Fill
            )
        }

        data.chunked(3).forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                rowItems.forEach { item ->
                    Row(
                        modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 5.dp),

                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Dot(color = item.color, size = 20.dp)
                        Text(
                            text = "${item.label}: ${item.data.toInt()}",
                            color = Color.Black,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
                repeat(3 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }


    }
}



