package com.ttc.uniid.data.remote.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EndUserDTO(
    val avatar: String?,
    val birthday: String?,
    val createdAt: String?,
    val email: String?,
    val familyName: String?,
    val gender: String?,
    val givenName: String?,
    val is2Step: Boolean?,
    val locate: String?,
    val name: String?,
    val phone: String?,
    val primary: Boolean?,
    val propertiesDTOList: List<List<PropertiesDTO>>?,
    val provider: String?,
    val usePassword: Boolean?,
    val username: String?
):Parcelable