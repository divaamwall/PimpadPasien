package com.diva.pimpad.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Users(
    var profileImage: String = "",
    var userId: String = "",
    var username: String = "",
    var status: String = ""
) : Parcelable
