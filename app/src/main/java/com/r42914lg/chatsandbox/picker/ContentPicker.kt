package com.r42914lg.chatsandbox.picker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.r42914lg.chatsandbox.slider.ViewPager

var globalUriList = listOf<Uri>()

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "picker"
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("picker") {
            ContentPicker(
                onNavigateToViewer = { navController.navigate("slider") },
            )
        }
        composable("viewer") {
            ContentPreview()
        }
        composable("slider") {
            ViewPager()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ContentPicker(
    onNavigateToViewer: () -> Unit
) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) {
        globalUriList = it
        onNavigateToViewer()
    }

    Button(onClick = {
        launcher.launch("*/*")
    }) {
        Text(text = "Pick image")
    }
}

class GetMultipleContentsMultipleMimes : ActivityResultContracts.GetMultipleContents() {
    @SuppressLint("MissingSuperCall")
    override fun createIntent(context: Context, input: String): Intent {
        val mimes = input.split(" ")
        return Intent(Intent.ACTION_GET_CONTENT)
            .addCategory(Intent.CATEGORY_OPENABLE)
            .setType(/*mimes[0]*/"image/*")
            .putExtra(Intent.EXTRA_MIME_TYPES, /*arrayOf(mimes)*/"video/*")
            .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
    }
}