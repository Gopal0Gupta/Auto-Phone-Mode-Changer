import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.autophonemodechanger.Location
import com.example.autophonemodechanger.R

@Composable
fun LocationList(locations: List<Location>) {

    Surface(color = colorResource(id = R.color.Light_Purple)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(locations) { location ->
                LocationItem(location)
                Divider(
                    color = Color.Black,
                    thickness = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}
