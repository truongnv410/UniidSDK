package com.ttc.uniid.model.api

import com.ttc.uniid.data.remote.request.*
import com.ttc.uniid.data.remote.response.BaseResponse
import com.ttc.uniid.data.remote.response.RegisterPhonesRespond
import com.ttc.uniid.data.remote.response.UserInfoResponse
import com.ttc.uniid.data.remote.response.VerifierPhoneRespond
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("users/login")
    fun loginByPass(@Body bodyRequest: LoginPasswordRequest): Call<UserInfoResponse>

    @POST("/users/checker-username")
    fun findUserName(@Body body: LoginRequest): Call<UserInfoResponse>

//    @FormUrlEncoded
//    @POST("account/changePassword")
//    fun changePassword(@Field("passOld") passOld: String, @Field("passNew") passNew: String): Single<BaseResponse>


    @PUT("application/change-status")
    fun changeApplicationStatus(
        @Query("applicationId") applicationId: Int,
        @Query("status") status: Int
    ): Single<BaseResponse>


    @GET("application/export")
    fun exportFile(@Query("url") url: String): Single<BaseResponse>

    @GET("fbs/url")
    fun getLinkFacebook(): Single<String>

    //Gửi OTP cho trường hợp quên password
    @GET("users/otp-passwords")
    fun sendOTP(
        @Query("otpDestination") otpDestination: String?,
        @Query("username") username: String?
    ): Call<BaseResponse>

    @POST("users/otp-passwords")
    fun changePassword(@Body bodyRequest: ChangePasswordRequest): Call<UserInfoResponse>

    @POST("users/request-otp")
    fun requestOTPLogin(@Body bodyRequest: GetOTPRequest): Call<BaseResponse>

    @POST("users/login-otp")
    fun loginByOTP(@Body bodyRequest: LoginOTPRequest): Call<UserInfoResponse>

    @POST("users/auth/accuracy")
    fun accuracyUser(
        @Body otpRequest: OTPRequest
    ): Call<BaseResponse>

    @POST("users/social/accuracy")
    fun accuracySocial(
        @Body request: VerifyOTPSocialRequest
    ): Call<BaseResponse>

    @GET("/users/verifiers/otp")
    fun verifyOTP(@Query("otp") otp: String, @Query("username") username: String): Call<BaseResponse>

    @POST("users/auth")
    fun accuracy(
        @Body accuracyRequest: AccuracyRequest
    ): Call<BaseResponse>

    @POST("users/register-phones")
    fun registerPhonesRequest(@Body bodyRequest: RegisterPhonesRequest): Single<RegisterPhonesRespond>

    @POST("users/verifier-phones")
    fun verifyPhoneRegister(@Body bodyRequest: VerifyPhoneRegisterRequest): Single<VerifierPhoneRespond>

    @POST("users/filler")
    fun filler(@Body bodyRequest: FillerRequest): Single<BaseResponse>

    @POST("users/social/request-accuracy")
    fun requestAccuracySocial(@Body request: GetOTPSocialRequest): Call<BaseResponse>
}