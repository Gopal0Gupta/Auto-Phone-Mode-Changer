import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLocationDialog(
    onDismiss: () -> Unit,
    onAddLocation: (String) -> Unit
) {
    var locationName by remember { mutableStateOf("") }
    val maxLength = 15

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add Location") },
        text = {
            Column {
                TextField(
                    value = locationName,
                    onValueChange = {
                        if (it.length <= maxLength) {
                            locationName = it
                        }
                    },
                    label = { Text("Location Name") },
                    isError = locationName.length > maxLength
                )
                if (locationName.length > maxLength) {
                    Text(
                        text = "Maximum length is $maxLength characters",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (locationName.isNotEmpty() && locationName.length <= maxLength) {
                        onAddLocation(locationName)
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
