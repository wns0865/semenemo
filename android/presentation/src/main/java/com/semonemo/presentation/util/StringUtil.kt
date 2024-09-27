package com.semonemo.presentation.util

import android.net.Uri
import androidx.core.net.toUri

fun String.toUriOrDefault(): Uri = Uri.decode(this).let { Uri.parse(it) } ?: "".toUri()
