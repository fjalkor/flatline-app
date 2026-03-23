package com.example.flatline.common.extensions

import java.net.URLEncoder

fun String.utf8Encoded() = URLEncoder.encode(this, "UTF-8")