package com.ttc.uniid.view.ui.enterotp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ttc.uniid.R
import com.ttc.uniid.data.remote.response.UserInfoResponse
import com.ttc.uniid.databinding.LayoutForgotPasswordVerifyOtpBinding
import com.ttc.uniid.util.convertStringToMap
import com.ttc.uniid.view.ui.changepassword.ChangePasswordFragment
import kotlinx.android.synthetic.main.layout_forgot_password_verify_otp.*

class EnterOTPFragment : Fragment() {
    private lateinit var viewDataBinding: LayoutForgotPasswordVerifyOtpBinding
    var isSendOTPEmail: Boolean = false
    var isLogin: Boolean = false
    var dataUrlSocial: String? = null
    var phone: String? = null
    var userInfo: UserInfoResponse? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding =
            LayoutForgotPasswordVerifyOtpBinding.inflate(inflater, container, false).apply {
                viewModel = ViewModelProviders.of(this@EnterOTPFragment)
                    .get(EnterOTPViewModel::class.java)
                setLifecycleOwner(viewLifecycleOwner)
            }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        subscribeUI()
    }

    private fun setupUI() {
        userInfo = arguments?.getParcelable<UserInfoResponse>("user_info")
        isSendOTPEmail = arguments?.getBoolean("isSendOTPEmail", false) as Boolean
        isLogin = arguments?.getBoolean("isLogin", false) as Boolean
        if (!displayTryAnotherWay(isSendOTPEmail)) {
            btnTryAnotherWay.visibility = View.GONE
        }
        tvTitle.text = getString(R.string.second_step_verify)
        tv2ndTitle.text = getString(R.string.demo_email)
        if (isSendOTPEmail) {
            tvDesc.text = String.format(
                getString(R.string.sent_verification_code),
                userInfo?.userInfoDTO?.endUserDTO?.email
            )
        } else {
            tvDesc.text = String.format(
                getString(R.string.sent_verification_code),
                userInfo?.userInfoDTO?.endUserDTO?.phone
            )
        }
        tvResendOTP.setOnClickListener {
            userInfo?.let { userData ->
                if (isSendOTPEmail) {
                    if (isLogin) {
                        viewDataBinding.viewModel?.accuracy("EMAIL", userData, false)
                    } else {
                        viewDataBinding.viewModel?.resendOTP("EMAIL", userData, false)
                    }
                } else {
                    if (isLogin) {
                        viewDataBinding.viewModel?.accuracy("PHONE", userData, false)
                    } else {
                        viewDataBinding.viewModel?.resendOTP("PHONE", userData, false)
                    }
                }
            }
        }
        btnNext.setOnClickListener {
            if (!dataUrlSocial.isNullOrEmpty()) {
                val mapData = convertStringToMap(dataUrlSocial)
                val username = mapData?.get("username")
                val socialType = mapData?.get("socialType")
                if (!username.isNullOrEmpty() && !socialType.isNullOrEmpty()) {
                    phone?.let {
                        if (it.isNotEmpty()) {
                            viewDataBinding.viewModel?.accuracySocial(it, socialType, username)
                        }
                    }
                } else {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.unspecific_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                userInfo?.let { userInfo ->
                    if (isLogin) {
                        if (isSendOTPEmail) {
                            viewDataBinding.viewModel?.accuracyUsername("EMAIL", userInfo)
                        } else {
                            viewDataBinding.viewModel?.accuracyUsername("PHONE", userInfo)
                        }
                    } else {
                        viewDataBinding.viewModel?.sentOTP(userInfo)
                    }
                }
            }

        }
        btnTryAnotherWay.setOnClickListener {
            if (isSendOTPEmail) {
                userInfo?.let { it1 -> viewDataBinding.viewModel?.resendOTP("PHONE", it1, true) }
            } else {
                userInfo?.let { it1 -> viewDataBinding.viewModel?.resendOTP("EMAIL", it1, true) }
            }
        }
    }

    private fun displayTryAnotherWay(isSendOTPEmail: Boolean): Boolean {
        if (isSendOTPEmail) {
            if (userInfo?.userInfoDTO?.endUserDTO?.phone.isNullOrEmpty()) {
                return false
            }
        } else {
            if (userInfo?.userInfoDTO?.endUserDTO?.email.isNullOrEmpty()) {
                return false
            }
        }
        return true
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun subscribeUI() {
        viewDataBinding.viewModel?.sendOTPSuccess?.observe(viewLifecycleOwner, Observer {
            if (it.errorCode == 600) {
                Toast.makeText(
                    activity,
                    context?.getString(R.string.send_otp_success),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            }
        })
        viewDataBinding.viewModel?.sendOTPAnotherWaySuccess?.observe(viewLifecycleOwner, Observer {
            Toast.makeText(
                activity,
                getString(R.string.sent_otp_success),
                android.widget.Toast.LENGTH_SHORT
            )
                .show()
            updateUI()
        })

        viewDataBinding.viewModel?.accuracyUsernameSuccess?.observe(viewLifecycleOwner, Observer {
            if (isLogin) {
                // dong module
                requireActivity().finish()
            } else {
                val bundle = bundleOf(
                    "user_info" to userInfo,
                    "otpCode" to viewDataBinding.viewModel?.otpCode?.value
                )
                val fragmentManager: FragmentManager? = activity?.supportFragmentManager
                val fragmentTransaction: FragmentTransaction? = fragmentManager?.beginTransaction()
                val fragment = ChangePasswordFragment()
                fragment.arguments = bundle
                fragmentTransaction?.add(android.R.id.content, fragment)
                    ?.addToBackStack("ChangePasswordFragment")?.commit()
            }
        })
        viewDataBinding.viewModel?.invalidOTPCode?.observe(viewLifecycleOwner, Observer { error ->
            error.isNotEmpty().let {
                if (it) {
                    layoutOTP.suffixTextView.visibility = android.view.View.VISIBLE
                    layoutOTP.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                        layoutOTP.suffixTextView.visibility = android.view.View.VISIBLE
                    }
                    layoutOTP.suffixText = getString(R.string.error)
                    layoutOTP.background =
                        resources.getDrawable(R.drawable.bg_border_edittext_error_10dp)
                } else {
                    layoutOTP.background =
                        resources.getDrawable(R.drawable.bg_border_edittext_10dp)
                }
            }
        })
        viewDataBinding.viewModel?.accuracySocialSuccess?.observe(viewLifecycleOwner, Observer {
            if (it.errorCode == 600) {
                Toast.makeText(
                    activity,
                    context?.getString(R.string.login_success),
                    Toast.LENGTH_SHORT
                ).show()
                activity?.finish()
            } else {
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            }
        })
        viewDataBinding.viewModel?.otpCode?.observe(viewLifecycleOwner, Observer { account ->
            account.isNotEmpty().let {
                if (it) {
                    viewDataBinding.viewModel?.invalidOTPCode?.value = ""
                    layoutOTP.background =
                        resources.getDrawable(R.drawable.bg_border_edittext_10dp)
                    layoutOTP.suffixTextView.visibility = View.GONE
                    layoutOTP.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                        layoutOTP.suffixTextView.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun updateUI() {
        if (isSendOTPEmail) {
            isSendOTPEmail = false
            tvDesc.text = String.format(
                getString(R.string.sent_verification_code),
                userInfo?.userInfoDTO?.endUserDTO?.phone
            )
        } else {
            isSendOTPEmail = true
            tvDesc.text = String.format(
                getString(R.string.sent_verification_code),
                userInfo?.userInfoDTO?.endUserDTO?.email
            )
        }
    }
}
