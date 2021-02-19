package com.ttc.uniid.view.ui.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ttc.uniid.R
import com.ttc.uniid.databinding.LayoutLoginUserUniidBinding
import com.ttc.uniid.extension.addFragment
import com.ttc.uniid.extension.replaceFragment
import com.ttc.uniid.extension.showDialogResult
import com.ttc.uniid.view.ui.forgotpassword.ForgotPasswordFragment
import com.ttc.uniid.view.ui.social.SocialWebFragment
import kotlinx.android.synthetic.main.layout_login_user_uniid.*

class LoginFragment : Fragment() {

    private lateinit var viewDataBinding: LayoutLoginUserUniidBinding
    private var checkerCode: String? = null
    private var isLoginByPass = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = LayoutLoginUserUniidBinding.inflate(inflater, container, false).apply {
            viewModel = ViewModelProviders.of(this@LoginFragment).get(LoginViewModel::class.java)
            setLifecycleOwner(viewLifecycleOwner)
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        subscribeUI()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun subscribeUI() {

        viewDataBinding.viewModel?.responseOTP?.observe(viewLifecycleOwner, Observer {
            activity?.let { act ->
                Toast.makeText(
                    act,
                    it.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        viewDataBinding.viewModel?.loginOTPResponse?.observe(
            viewLifecycleOwner,
            Observer { response ->
                if (response.errorCode == 100) {
                    activity?.let { act ->
                        Toast.makeText(
                            act,
                            act.getString(R.string.login_success),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        viewDataBinding.viewModel?.invalidPhone?.observe(viewLifecycleOwner, Observer { error ->
            error.isNotEmpty().let {
                if (it) {
                    phoneLayout.suffixTextView.visibility = View.VISIBLE
                    phoneLayout.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                        phoneLayout.suffixTextView.visibility = View.VISIBLE
                    }
                    phoneLayout.suffixText = getString(R.string.error)
                    phoneLayout.background =
                        resources.getDrawable(R.drawable.bg_border_edittext_error_10dp)
                } else {
                    phoneLayout.background =
                        resources.getDrawable(R.drawable.bg_border_edittext_10dp)
                }
            }
        })
        viewDataBinding.viewModel?.phone?.observe(viewLifecycleOwner, Observer { account ->
            account.isNotEmpty().let {
                if (it) {
                    viewDataBinding.viewModel?.invalidPhone?.value = ""
                    phoneLayout.suffixTextView.visibility = View.GONE
                    phoneLayout.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                        phoneLayout.suffixTextView.visibility = View.GONE
                    }
                    phoneLayout.background =
                        resources.getDrawable(R.drawable.bg_border_edittext_10dp)
                }
            }
        })
        viewDataBinding.viewModel?.invalidUserName?.observe(viewLifecycleOwner, Observer { error ->
            error.isNotEmpty().let {
                if (it) {
                    layoutName.suffixTextView.visibility = View.VISIBLE
                    layoutName.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                        layoutName.suffixTextView.visibility = View.VISIBLE
                    }
                    layoutName.suffixText = getString(R.string.error)
                    layoutName.background =
                        resources.getDrawable(R.drawable.bg_border_edittext_error_10dp)
                } else {
                    layoutName.background =
                        resources.getDrawable(R.drawable.bg_border_edittext_10dp)
                }
            }
        })
        viewDataBinding.viewModel?.userName?.observe(viewLifecycleOwner, Observer { account ->
            account.isNotEmpty().let {
                if (it) {
                    viewDataBinding.viewModel?.invalidUserName?.value = ""
                    layoutName.suffixTextView.visibility = View.GONE
                    layoutName.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                        layoutName.suffixTextView.visibility = View.GONE
                    }
                    layoutName.background =
                        resources.getDrawable(R.drawable.bg_border_edittext_10dp)
                }
            }
        })
        viewDataBinding.viewModel?.invalidPassword?.observe(viewLifecycleOwner, Observer { error ->
            error.isNotEmpty().let {
                if (it) {
                    layoutPassword.suffixTextView.visibility = View.VISIBLE
                    layoutPassword.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                        layoutPassword.suffixTextView.visibility = View.VISIBLE
                    }
                    layoutPassword.suffixText = getString(R.string.error)
                    layoutPassword.background =
                        resources.getDrawable(R.drawable.bg_border_edittext_error_10dp)
                } else {
                    layoutPassword.background =
                        resources.getDrawable(R.drawable.bg_border_edittext_10dp)
                }
            }
        })
        viewDataBinding.viewModel?.password?.observe(viewLifecycleOwner, Observer { account ->
            account.isNotEmpty().let {
                if (it) {
                    viewDataBinding.viewModel?.invalidPassword?.value = ""
                    layoutPassword.background =
                        resources.getDrawable(R.drawable.bg_border_edittext_10dp)
                    layoutPassword.suffixTextView.visibility = View.GONE
                    layoutPassword.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                        layoutPassword.suffixTextView.visibility = View.GONE
                    }
                }
            }
        })
        viewDataBinding.viewModel?.invalidOTP?.observe(viewLifecycleOwner, Observer { error ->
            error.isNotEmpty().let {
                if (it) {
                    OTPLayout.suffixTextView.visibility = View.VISIBLE
                    OTPLayout.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                        OTPLayout.suffixTextView.visibility = View.VISIBLE
                    }
                    OTPLayout.suffixText = getString(R.string.error)
                    OTPLayout.background =
                        resources.getDrawable(R.drawable.bg_border_edittext_error_10dp)
                } else {
                    OTPLayout.background =
                        resources.getDrawable(R.drawable.bg_border_edittext_10dp)
                }
            }
        })
        viewDataBinding.viewModel?.otp?.observe(viewLifecycleOwner, Observer { account ->
            account.isNotEmpty().let {
                if (it) {
                    OTPLayout.suffixTextView.visibility = View.GONE
                    OTPLayout.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                        OTPLayout.suffixTextView.visibility = View.GONE
                    }
                    viewDataBinding.viewModel?.invalidOTP?.value = ""
                    OTPLayout.background =
                        resources.getDrawable(R.drawable.bg_border_edittext_10dp)
                }
            }
        })
        viewDataBinding.viewModel?.facebookLink?.observe(viewLifecycleOwner, Observer { httpUrl ->
            httpUrl?.let {
                val url = it.toString()
                val bundle = bundleOf("httpUrl" to url)
                activity?.addFragment(SocialWebFragment(), bundle)
            }
        })
        viewDataBinding.viewModel?.lockPassword?.observe(viewLifecycleOwner, Observer {
            showDialogResult(
                title = context?.getString(R.string.lock_title),
                message = Pair(
                    context?.getString(R.string.lock_desc) ?: "",
                    R.color.black
                ),
                actionOneMessage = context?.getString(R.string.close)
            )
        })
        viewDataBinding.viewModel?.ggLink?.observe(viewLifecycleOwner, Observer { httpUrl ->
            httpUrl?.let {
                val url = it.toString()
                val bundle = bundleOf("httpUrl" to url)
                activity?.addFragment(SocialWebFragment(), bundle)
            }
        })
        txtLoginWithOtp.setOnClickListener {
            resetError(viewDataBinding.viewModel)
            isLoginByPass = false
            layoutPhone.visibility = View.VISIBLE
            layoutAccount.visibility = View.GONE
            layoutOTP.visibility = View.VISIBLE
            layoutLoginWithPassword.visibility = View.GONE
            lnLayoutPassword.visibility = View.GONE
            layoutLoginWithOTP.visibility = View.VISIBLE
        }
        txtLoginWithPassword.setOnClickListener {
            resetError(viewDataBinding.viewModel)
            layoutPhone.visibility = View.GONE
            layoutAccount.visibility = View.VISIBLE
            isLoginByPass = true
            layoutOTP.visibility = View.GONE
            layoutLoginWithPassword.visibility = View.VISIBLE
            lnLayoutPassword.visibility = View.VISIBLE
            layoutLoginWithOTP.visibility = View.GONE
        }
        viewDataBinding.viewModel?.responsePassword?.observe(viewLifecycleOwner, Observer {
            if (it.errorCode == 100) {
                it.userInfoDTO?.checkerCode = checkerCode
                if (it?.userInfoDTO?.useAuthenticator != null) {
                    var bundle = bundleOf("user_info" to it, "isLogin" to true)
//                    val fragmentRecovery = RecoveryFragment()
//                    fragmentRecovery.arguments = bundle
//                    requireActivity().replaceFragment(fragmentRecovery, fragmentRecovery.arguments)
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Login Success",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
        imgFacebook.setOnClickListener {
            viewDataBinding.viewModel?.loginFacebook()
        }
        imgGoogle.setOnClickListener {
            viewDataBinding.viewModel?.loginGG()
        }
    }

    private fun resetError(viewModel: LoginViewModel?) {
        viewModel?.let {
            viewModel.otp.value = ""
            viewModel.phone.value = ""
            viewModel.userName.value = ""
            viewModel.password.value = ""
            viewModel.invalidOTP.value = ""
            viewModel.invalidPhone.value = ""
            viewModel.invalidUserName.value = ""
            viewModel.invalidPassword.value = ""
            OTPLayout.suffixTextView.visibility = View.GONE
            OTPLayout.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                OTPLayout.suffixTextView.visibility = View.GONE
            }
            phoneLayout.suffixTextView.visibility = View.GONE
            phoneLayout.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                phoneLayout.suffixTextView.visibility = View.GONE
            }
        }
        viewDataBinding.viewModel?.userInfo?.observe(viewLifecycleOwner, Observer { userInfo ->
            if (userInfo.errorCode == 100) {
                if (!userInfo.userInfoDTO?.checkerCode.isNullOrEmpty() || !userInfo.userInfoDTO?.endUserDTO?.username.isNullOrEmpty()) {
                    checkerCode = userInfo.userInfoDTO?.checkerCode
                    if (isLoginByPass) {
                        viewDataBinding.viewModel?.apply {
                            requestPasswordLogin(checkerCode)
                        }
                    } else {
                        viewDataBinding.viewModel?.apply {
                            requestOTPLogin(
                                checkerCode,
                                userInfo.userInfoDTO?.endUserDTO?.username
                            )
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
                Toast.makeText(
                    requireActivity(),
                    userInfo.message,
                    Toast.LENGTH_SHORT
                ).show()
                viewDataBinding.viewModel?.loading?.value = false
            }
        })
    }

    private fun setupUI() {
        val sufPassword =
            layoutPassword.findViewById<AppCompatTextView>(com.google.android.material.R.id.textinput_suffix_text)
        sufPassword.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        sufPassword.gravity = Gravity.CENTER
        txtForgotPassword.setOnClickListener {
            activity?.replaceFragment(ForgotPasswordFragment(), Bundle())
        }
        btnSignIn.setOnClickListener {
            if (isLoginByPass) {
                viewDataBinding.viewModel?.findUsername(isLoginByPass)
            } else {
                viewDataBinding.viewModel?.loginByOTP(checkerCode)
            }
        }
        txtSendOTP.setOnClickListener {
            viewDataBinding.viewModel?.findUsername(isLoginByPass)
        }
    }
}
