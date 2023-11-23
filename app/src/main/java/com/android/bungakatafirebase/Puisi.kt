package com.android.bungakatafirebase

import com.google.firebase.firestore.Exclude

data class Puisi(
    @set:Exclude @get:Exclude var id : String = "",
    var judul : String = "",
    var penulis : String = "",
    var puisi : String = ""
)
