import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jakondev.a2048_game.ui.theme.main.Rowdies
import com.jakondev.a2048_game.ui.theme.main.getPalette

@Composable
fun StylizedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    val palette = getPalette()
    val cornerRadius = 12.dp

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(palette.surface)
            .border(2.dp, palette.tertiary, RoundedCornerShape(cornerRadius))
            .padding(8.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontFamily = Rowdies,
            color = palette.tertiary
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = androidx.compose.ui.text.TextStyle(
                fontFamily = Rowdies,
                fontSize = 18.sp,
                color = palette.tertiary
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = palette.tertiary,
                unfocusedBorderColor = palette.tertiary,
                cursorColor = palette.tertiary,
                focusedLabelColor = palette.tertiary,
                unfocusedLabelColor = palette.tertiary
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

