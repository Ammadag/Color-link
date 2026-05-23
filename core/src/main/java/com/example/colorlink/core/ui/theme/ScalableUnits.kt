package com.example.colorlink.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Int.sdp(): Dp {
    val context = LocalContext.current
    val id = context.resources.getIdentifier("_" + this + "sdp", "dimen", context.packageName)
    return if (id != 0) dimensionResource(id) else this.dp
}

@Composable
fun Int.ssp(): TextUnit {
    val context = LocalContext.current
    val id = context.resources.getIdentifier("_" + this + "ssp", "dimen", context.packageName)
    return if (id != 0) {
        val size = dimensionResource(id)
        size.value.sp
    } else {
        this.sp
    }
}
