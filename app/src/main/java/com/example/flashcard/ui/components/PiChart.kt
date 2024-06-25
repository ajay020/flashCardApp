import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
//import androidx.compose.ui.graphics.drawscope.drawOval
import androidx.compose.ui.unit.dp

@Composable
fun DonutChart(
    correctAnswers: Int,
    incorrectAnswers: Int,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 40f,
    correctColor: Color = Color.Green,
    incorrectColor: Color = Color.Red,
) {
    val total = correctAnswers + incorrectAnswers
    val correctPercentage = if (total > 0) (correctAnswers.toFloat() / total) * 360 else 0f
    val incorrectPercentage = 360 - correctPercentage

    Canvas(modifier = modifier.padding(16.dp)) {
        drawIntoCanvas { canvas ->
            drawOval(
                color = incorrectColor,
                size = size,
                style = Stroke(width = strokeWidth)
            )
            drawOval(
                color = correctColor,
                size = size,
//                startAngle = 270f,
//                sweepAngle = correctPercentage,
//                useCenter = false,
                style = Stroke(width = strokeWidth)
            )
        }
    }
}
