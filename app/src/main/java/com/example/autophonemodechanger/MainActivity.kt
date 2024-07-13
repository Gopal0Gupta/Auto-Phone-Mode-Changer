package com.example.autophonemodechanger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.autophonemodechanger.ui.theme.AutoPhoneModeChangerTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.google.accompanist.permissions.*
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AutoPhoneModeChangerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}

@Composable
fun TopScreen(onAddClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 54.dp)
            .border(
                width = 3.dp,
                color = colorResource(id = R.color.black),
                shape = RoundedCornerShape(0.dp)
            )
    ) {
        Surface(color = colorResource(id = R.color.Light_Blue)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Add Location",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 16.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    modifier = Modifier
                        .heightIn(min = 54.dp)
                        .border(
                            width = 3.dp,
                            color = colorResource(id = R.color.black),
                            shape = RoundedCornerShape(0.dp)
                        ),
                    onClick = onAddClick,
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.Light_Green)
                    )
                ) {
                    Text(
                        text = "Add",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}


@Composable
fun MapScreen(
    userLocation: LatLng?,
    onLocationSelected: (LatLng) -> Unit
) {
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(userLocation) {
        userLocation?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
        }
    }

    Surface(color = colorResource(id = R.color.Light_Purple)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(410.dp)
                .padding(6.dp)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    selectedLocation = latLng
                    onLocationSelected(latLng)
                }
            ) {
                userLocation?.let { location ->
                    Marker(
                        state = MarkerState(position = location),
                        title = "Your Location"
                    )
                }
                selectedLocation?.let { location ->
                    Marker(
                        state = MarkerState(position = location),
                        title = "Selected Location"
                    )
                }
            }
        }
    }
}

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

@Composable
fun LocationItem(location: Location) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(start = 10.dp),
            text = location.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        DropdownMenuBox()
    }
}

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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val fineLocationPermissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
    var showAddLocationDialog by remember { mutableStateOf(false) }
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var locationList by remember { mutableStateOf(listOf<Location>()) }

    LaunchedEffect(key1 = Unit) {
        fineLocationPermissionState.launchPermissionRequest()
    }

    if (fineLocationPermissionState.status.isGranted) {
        LaunchedEffect(Unit) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        userLocation = LatLng(it.latitude, it.longitude)
                    }
                }
            } catch (e: SecurityException) {
                errorMessage = "An error occurred while getting location: ${e.message}"
            }
        }
    }

    Column {
        TopScreen(onAddClick = { showAddLocationDialog = true })
        if (fineLocationPermissionState.status.isGranted) {
            MapScreen(userLocation = userLocation, onLocationSelected = { latLng ->
                selectedLocation = latLng
            })
        } else {
            Text(
                "Location permission is required to use this feature.",
                fontSize = 16.sp,
                color = colorResource(id = R.color.red)
            )
        }
        BottomScreen(locationList = locationList)
    }

    if (showAddLocationDialog) {
        AddLocationDialog(
            onDismiss = { showAddLocationDialog = false },
            onAddLocation = { locationName ->
                selectedLocation?.let { location ->
                    locationList = locationList + Location(locationName)
                }
                showAddLocationDialog = false
            }
        )
    }
}

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

// to create an Outlined Text Field
// Calling this function as content
// in the above function
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBox() {
    val context = LocalContext.current
    val phoneModes = arrayOf("Vibration", "Silent", "Ringer")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(phoneModes[0]) }

    Box(
        modifier = Modifier
            .width(160.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                phoneModes.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedText = item
                            expanded = false
                            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                        }
                    )
                    Divider()
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AutoPhoneModeChangerTheme {
        HomeScreen()
    }
}