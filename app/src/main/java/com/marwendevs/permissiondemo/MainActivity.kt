package com.marwendevs.permissiondemo

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {
    private val cameraResultLauncher : ActivityResultLauncher<String> =
    registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isGranted ->
        if (isGranted){
            Toast.makeText(this,"Permission Granted for Camera",Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, "Permission Denied for Camera", Toast.LENGTH_LONG).show()
        }
    }

    private val cameraAndLocationResultLauncher : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()){
            permissions ->
            permissions.entries.forEach{
                val permissionName = it.key
                val isGranted = it.value
                if (isGranted){
                    if (permissionName == Manifest.permission.ACCESS_FINE_LOCATION){
                        Toast.makeText(this,"Permission Granted for Location",Toast.LENGTH_LONG).show()
                    }else if(permissionName == Manifest.permission.ACCESS_COARSE_LOCATION){
                        Toast.makeText(this,"Permission Granted for Coarse",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this,"Permission Granted for Camera",Toast.LENGTH_LONG).show()
                    }
                }else{
                    if(permissionName == Manifest.permission.ACCESS_FINE_LOCATION){
                        Toast.makeText(this,"Permission denied for Fine Location",Toast.LENGTH_LONG).show()
                    }else if(permissionName == Manifest.permission.ACCESS_COARSE_LOCATION){
                        Toast.makeText(this,"Permission denied for COARSE Location",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this,"Permission denied for CAMERA",Toast.LENGTH_LONG).show()
                    }
                }
            }

        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var btnPermission :Button=findViewById(R.id.btn_Camera)
        btnPermission.setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
              showRationaleDialog("Permission requires camera access",
                  "Camera cannot be used because Camera access is denied")
            }else{
                cameraAndLocationResultLauncher.launch(
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                )
            }
        }
    }
    /**
    *Shows rationale dialog for display why the app needs permission
    *Only shown if the user has denied the permission request previously
    */
    private fun showRationaleDialog(title: String,message : String){
        var builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title).setMessage(message).setPositiveButton("Cancel"){
            dialog,_-> dialog.dismiss()
        }
        builder.create().show()
    }
}