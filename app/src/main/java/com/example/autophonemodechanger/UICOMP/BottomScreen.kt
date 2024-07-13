import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.autophonemodechanger.Location
import com.example.autophonemodechanger.R

@Composable
fun BottomScreen(locationList: List<Location>) {
    Surface(color = colorResource(id = R.color.Light_Blue)) {
        Column(
            modifier = Modifier
                .border(
                    width = 3.dp,
                    color = colorResource(id = R.color.black)
                )
        ) {
            Text(
                text = "Locations",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(9.dp)
            )
            Divider(
                color = Color.Black,
                thickness = 3.dp,
                modifier = Modifier
                    .fillMaxWidth()
            )
            LocationList(locations = locationList)
        }
    }
}
