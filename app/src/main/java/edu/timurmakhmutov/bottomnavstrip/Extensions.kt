package edu.timurmakhmutov.bottomnavstrip

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


//function to check for permissions
fun Fragment.isPermissionGranted(p:String):Boolean {

    return ContextCompat.checkSelfPermission(
        activity as AppCompatActivity,
        p)==PackageManager.PERMISSION_GRANTED
}