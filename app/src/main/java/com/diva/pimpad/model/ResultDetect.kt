package com.diva.pimpad.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResultDetect(
    val userId: String? = null,
    val username: String? = null,
    val resultId: String? = null,
    val resultImage: String? = null,
    val resultAcne: String? = null,
    val timeStamp: Long? = null
) : Parcelable
