package com.example.autophonemodechanger

import android.graphics.Paint.Style
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.ColorScheme
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
import androidx.compose.ui.text.font.FontStyle
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
fun TopScreen() {
    Surface(modifier = Modifier
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
                    onClick = {
                        /*TODO*/
                    },
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
fun MapScreen() {
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    if (userLocation == null) {
        LaunchedEffect(Unit) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        userLocation = LatLng(it.latitude, it.longitude)
                    }
                }
            } catch (e: SecurityException) {
                errorMessage = "An unexpected error occurred: ${e.message}"
            }
        }
    }

    userLocation?.let { location ->
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(location, 15f)
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
                    cameraPositionState = cameraPositionState
                ) {
                    Marker(
                        state = MarkerState(position = location),
                        title = "Your Location"
                    )
                }
            }
        }
    } ?: run {
        Text(
            "Fetching location...",
            fontSize = 16.sp,
            color = colorResource(id = R.color.red)
        )
    }
}

@Composable
fun BottomScreen() {
    Surface(color = colorResource(id = R.color.Light_Blue)) {
        Column(
            modifier = Modifier
                .border(
                    width = 3.dp,
                    color = colorResource(id = R.color.black))
        ){
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
            DemoLocations()
        }
    }
}

@Composable
fun LocationItem(location: Location) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = location.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = location.mode,
            fontSize = 16.sp
        )
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

@Composable
fun DemoLocations() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val locations = listOf(
            Location("Location 1", "Phone Mode for location 1"),
            Location("Location 2", "Phone Mode for location 2"),
            Location("Location 3", "Phone Mode for location 3"),
            Location("Location 4", "Phone Mode for location 4"),
            Location("Location 5", "Phone Mode for location 5"),
            Location("Location 6", "Phone Mode for location 6"),
        )
        LocationList(locations = locations)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen() {
    Column {
        TopScreen()
        val fineLocationPermissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

        LaunchedEffect(key1 = Unit) {
            fineLocationPermissionState.launchPermissionRequest()
        }

        if (fineLocationPermissionState.status.isGranted) {
            MapScreen()
        } else {
            Text(
                "Location permission is required to use this feature.",
                fontSize = 16.sp,
                color = colorResource(id = R.color.red)
            )
        }
        BottomScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AutoPhoneModeChangerTheme {
        HomeScreen()
    }
}