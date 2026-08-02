package project.llmevaluation.utils

import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import project.llmevaluation.models.EvaluationResult

object ChartManager {

    fun setupBarChart(barChart: BarChart, results: List<EvaluationResult>) {
        val entries = results.mapIndexed { index, res ->
            BarEntry(index.toFloat(), res.overallScore.toFloat())
        }

        val dataSet = BarDataSet(entries, "Overall Score")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        
        val barData = BarData(dataSet)
        barChart.data = barData
        
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(results.map { it.model })
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.granularity = 1f
        barChart.description.isEnabled = false
        barChart.animateY(1000)
        barChart.invalidate()
    }

    fun setupRadarChart(radarChart: RadarChart, results: List<EvaluationResult>) {
        val dataSets = results.map { res ->
            val entries = listOf(
                RadarEntry(res.accuracy.toFloat()),
                RadarEntry(res.coherence.toFloat()),
                RadarEntry(res.bleu.toFloat()),
                RadarEntry(res.rouge.toFloat()),
                RadarEntry(res.semanticSimilarity.toFloat()),
                RadarEntry(res.fluency.toFloat())
            )
            val dataSet = RadarDataSet(entries, res.model)
            dataSet.color = ColorTemplate.COLORFUL_COLORS[results.indexOf(res) % ColorTemplate.COLORFUL_COLORS.size]
            dataSet.fillAlpha = 100
            dataSet.setDrawFilled(true)
            dataSet
        }

        val labels = listOf("Accuracy", "Coherence", "BLEU", "ROUGE", "Semantic", "Fluency")
        radarChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        
        val radarData = RadarData(dataSets)
        radarChart.data = radarData
        radarChart.description.isEnabled = false
        radarChart.animateXY(1000, 1000)
        radarChart.invalidate()
    }

    fun setupPieChart(pieChart: PieChart, results: List<EvaluationResult>) {
        val entries = results.map { res ->
            PieEntry(res.overallScore.toFloat(), res.model)
        }

        val dataSet = PieDataSet(entries, "Performance Distribution")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        
        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.centerText = "Overall Scores"
        pieChart.animateY(1000)
        pieChart.invalidate()
    }
}
