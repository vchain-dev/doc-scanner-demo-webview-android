package com.example.zamnadocscannerdemo

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Build
import com.example.zamnadocscannerdemo.CameraPermissionActivity
import android.content.pm.PackageManager
import android.annotation.TargetApi

abstract class CameraPermissionActivity : AppCompatActivity() {
    private var permissionDeniedOnce = false
    private var paused = true
    override fun onPause() {
        super.onPause()
        paused = true
    }

    override fun onResume() {
        super.onResume()
        paused = false
    }

    protected fun hasCameraPermission(): Boolean {
        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || checkSelfPermission(CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED)
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected fun requestCameraPermission() {
        // For Android M and onwards we need to request the camera permission from the user.
        if (!hasCameraPermission()) {
            // The user already denied the permission once, we don't ask twice.
            if (!permissionDeniedOnce) {
                // It's clear why the camera is required. We don't need to give a detailed reason.
                requestPermissions(arrayOf(CAMERA_PERMISSION), CAMERA_PERMISSION_REQUEST)
            }
        } else {
            // We already have the permission or don't need it.
            onCameraPermissionGranted()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionDeniedOnce = false
                if (!paused) {
                    // Only call the function if not paused - camera should not be used otherwise.
                    onCameraPermissionGranted()
                }
            } else {
                // The user denied the permission - we are not going to ask again.
                permissionDeniedOnce = true
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    abstract fun onCameraPermissionGranted()

    companion object {
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
        private const val CAMERA_PERMISSION_REQUEST = 0
    }
}