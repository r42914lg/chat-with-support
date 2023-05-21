package com.r42914lg.chatsandbox.picker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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
                onNavigateToViewer = { navController.navigate("viewer") },
            )
        }
        composable("viewer") { ContentPreview()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ContentPicker(
    onNavigateToViewer: () -> Unit
) {

//    var uriList by remember { mutableStateOf<List<Uri>>(listOf()) }
//    val bitmap =  remember { mutableStateOf<Bitmap?>(null) }
//    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = GetMultipleContentsMultipleMimes()
    ) {
        //uriList = it
        println("LG >>> URI list set ${it.size}")
//        globalUriList = it
//        onNavigateToViewer()
    }

//    Column() {
        Button(onClick = {
            launcher.launch("image/* video/*")
        }) {
            Text(text = "Pick image")
        }

//        Spacer(modifier = Modifier.height(12.dp))
//
//
//        uriList.forEach {
//
//            val source = ImageDecoder.createSource(context.contentResolver,it)
//            bitmap.value = ImageDecoder.decodeBitmap(source)
//
//            bitmap.value?.let {  btm ->
//                Image(bitmap = btm.asImageBitmap(),
//                    contentDescription =null,
//                    modifier = Modifier.size(50.dp))
//            }
//        }
//    }

}

class GetMultipleContentsMultipleMimes : ActivityResultContracts.GetMultipleContents() {
    @SuppressLint("MissingSuperCall")
    override fun createIntent(context: Context, input: String): Intent {
        val mimes = input.split(" ")
        return Intent(Intent.ACTION_GET_CONTENT)
            .addCategory(Intent.CATEGORY_OPENABLE)
            .setType(mimes[0])
            .putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(mimes))
            .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
    }
}