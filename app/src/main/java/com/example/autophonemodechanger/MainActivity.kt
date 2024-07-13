package com.example.autophonemodechanger

import AddLocationDialog
import BottomScreen
import MapScreen
import TopScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.autophonemodechanger.ui.theme.AutoPhoneModeChangerTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.LatLng

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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AutoPhoneModeChangerTheme {
        HomeScreen()
    }
}