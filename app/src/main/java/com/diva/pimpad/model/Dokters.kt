package com.diva.pimpad.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Dokters(
    var profileImage: String = "",
    var dokterId: String = "",
    var username: String = "",
    var status: String = ""
) : Parcelable