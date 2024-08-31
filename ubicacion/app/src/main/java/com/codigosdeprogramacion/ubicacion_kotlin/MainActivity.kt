package com.codigosdeprogramacion.ubicacion_kotlin

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationTextView = findViewById(R.id.locationTextView)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getCurrentLocation()
    }

    private fun getCurrentLocation() {
        // Verificar permisos
        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Si los permisos no han sido concedidos, solicitarlos
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
            return
        }

        // Obtener la última ubicación conocida
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            // Puede que location sea null si no hay ubicación disponible
            if (location != null) {
                // Mostrar la ubicación en el TextView
                val latitud = location.latitude
                val longitud = location.longitude
                locationTextView.text = "Latitud: $latitud, Longitud: $longitud"
            } else {
                // Mostrar un mensaje en el TextView
                locationTextView.text = "No se pudo obtener la ubicación."
            }
        }
    }

    // Manejar la respuesta de la solicitud de permisos
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Si los permisos han sido concedidos, llama nuevamente a obtener la ubicación
            getCurrentLocation()
        }
    }
}