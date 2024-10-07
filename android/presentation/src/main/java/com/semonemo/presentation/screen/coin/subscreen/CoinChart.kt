package com.semonemo.presentation.screen.coin.subscreen

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.column.ColumnChart
import com.patrykandpatrick.vico.core.chart.composed.plus
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.composed.ComposedChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.plus
import com.patrykandpatrick.vico.core.entry.entryOf
import com.semonemo.presentation.theme.Gray03
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.SemonemoTheme

@Composable
fun CustomCoinChart(
    modifier: Modifier = Modifier,
    maxPrice: Long = 10L,
    minPrice: Long = 0L,
    lineColor: List<Color>,
    columnColor: List<Color>,
    lowPrice: List<Long>,
    highPrice: List<Long>,
    averagePrice: List<Long>,
    date: List<String>,
) {
    val lowPriceProducer = ChartEntryModelProducer(intListAsFloatEntryList(lowPrice))
    val highPriceProducer =
        ChartEntryModelProducer(intListAsFloatEntryList(highPrice))
    val averagePriceProducer =
        ChartEntryModelProducer(intListAsFloatEntryList(averagePrice))

    ProvideChartStyle(
        rememberChartStyle(
            lineChartColors = lineColor,
            columnChartColors = columnColor,
        ),
    ) {
        val highPriceChart =
            LineChart(
                lines =
                    listOf(
                        lineSpec(
                            lineColor = Color.Blue,
                            lineThickness = 1.dp,
                            dataLabel = TextComponent.Builder().build(),
                        ),
                    ),
            )

        val lowPriceChart =
            LineChart(
                lines =
                    listOf(
                        lineSpec(
                            lineColor = Color.Red,
                            lineThickness = 1.dp,
                            dataLabel = TextComponent.Builder().build(),
                        ),
                    ),
            )

        val averagePriceChart =
            columnChart(
                mergeMode = ColumnChart.MergeMode.Stack,
                axisValuesOverrider =
                    AxisValuesOverrider.fixed(
                        minY = minPrice.toFloat(),
                        maxY = maxPrice.toFloat(),
                    ),
                spacing = 10.dp,
                columns =
                    listOf(
                        LineComponent(
                            color = Gray03.toArgb(), // 컬럼 색상을 지정
                            thicknessDp = 7f, // 컬럼 두께를 설정
                            shape = Shapes.cutCornerShape(topRightPercent = 20, topLeftPercent = 20),
                        ),
                    ),
            )
        val chartModelProducer =
            ComposedChartEntryModelProducer(
                averagePriceProducer,
                highPriceProducer,
                lowPriceProducer,
            )

        Chart(
            modifier =
                modifier
                    .fillMaxHeight()
                    .height(400.dp),
            chart =
                remember(averagePriceChart + highPriceChart + lowPriceChart) {
                    averagePriceChart + lowPriceChart + highPriceChart
                },
            legend = rememberLegend(colors = lineColor),
            chartModelProducer = chartModelProducer,
            startAxis =
                rememberStartAxis(
                    itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = date.size),
                ),
            bottomAxis =
                rememberBottomAxis(
                    valueFormatter = { value, _ ->
                        date.getOrNull(value.toInt()) ?: ""
                    },
                ),
            runInitialAnimation = true,
            chartScrollState = rememberChartScrollState(),
        )
    }
}

fun createChartData(): List<ChartEntry> =
    listOf(
        FloatEntry(x = 0f, y = 10f),
        FloatEntry(x = 1f, y = 20f),
        FloatEntry(x = 2f, y = 30f),
        FloatEntry(x = 3f, y = 40f),
        FloatEntry(x = 4f, y = 50f),
    )

private fun intListAsFloatEntryList(list: List<Long>): List<FloatEntry> {
    val floatEntryList = arrayListOf<FloatEntry>()
    floatEntryList.clear()

    list.forEachIndexed { index, item ->
        floatEntryList.add(entryOf(x = index.toFloat(), y = item.toFloat()))
    }

    return floatEntryList
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun CoinChartPreview() {
    SemonemoTheme {
        CustomCoinChart(
            modifier = Modifier.fillMaxSize(),
            lineColor = listOf(Color.Red, Color.Blue,GunMetal),
            columnColor = listOf(GunMetal),
            lowPrice = listOf(0L, 1L, 2L, 3L, 4L, 5L),
            highPrice = listOf(0L, 3L, 2L, 3L, 4L, 5L),
            averagePrice = listOf(0L, 3L, 2L, 3L, 4L, 5L),
            date =
                listOf(
                    "10-01",
                    "10-02",
                    "10-03",
                    "10-04",
                    "10-05",
                    "10-06",
                ),
        )
    }
}
