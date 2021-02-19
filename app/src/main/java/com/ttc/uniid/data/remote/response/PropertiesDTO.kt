package com.ttc.uniid.data.remote.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PropertiesDTO(
    val active: Boolean?,
    val prime: Boolean?,
    val type: String?,
    val value: String?
): Parcelable