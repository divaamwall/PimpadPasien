package com.diva.pimpad.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Acne(
    val type: String ?= null,
    val name: String ?= null,
    val description: String ?= null,
    val cause: String ?= null,
    val solution: String ?= null,
    val image: String ?= null,
    val reference: String ?= null
) : Parcelable
