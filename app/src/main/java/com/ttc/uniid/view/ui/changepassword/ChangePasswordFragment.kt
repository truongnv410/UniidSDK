package com.ttc.uniid.view.ui.changepassword

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ttc.uniid.R
import com.ttc.uniid.data.remote.response.UserInfoResponse
import com.ttc.uniid.databinding.ChangePasswordLayoutBinding
import kotlinx.android.synthetic.main.change_password_layout.*

class ChangePasswordFragment : Fragment() {
    private lateinit var viewDataBinding: ChangePasswordLayoutBinding
    lateinit var userInfoResponse: UserInfoResponse
    lateinit var otpCode: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = ChangePasswordLayoutBinding.inflate(inflater, container, false).apply {
            viewModel = ViewModelProviders.of(this@ChangePasswordFragment)
                .get(ChangePasswordViewModel::class.java)
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
        otpCode = arguments?.getString("otpCode", "") as String
        userInfoResponse = arguments?.getParcelable<UserInfoResponse>("user_info")!!

        val sufPassword =
            layoutPassword.findViewById<AppCompatTextView>(com.google.android.material.R.id.textinput_suffix_text)
        sufPassword.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        sufPassword.gravity = Gravity.CENTER

        val sufConfirmPassword =
            layoutConfirmPassword.findViewById<AppCompatTextView>(com.google.android.material.R.id.textinput_suffix_text)
        sufConfirmPassword.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        sufConfirmPassword.gravity = Gravity.CENTER
        btnNext.setOnClickListener {
            otpCode.isNotEmpty().let {
                viewDataBinding.viewModel?.changePassword(otpCode, userInfoResponse)
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun subscribeUI() {
        viewDataBinding.viewModel?.userInfo?.observe(viewLifecycleOwner, Observer { userInfo ->
            if (userInfo.errorCode == 100) {
                Toast.makeText(
                    activity,
                    getString(R.string.change_pass_success),
                    Toast.LENGTH_SHORT
                ).show()
                val fm: FragmentManager = requireActivity().supportFragmentManager
                for (i in 0 until fm.backStackEntryCount) {
                    fm.popBackStack()
                }
            } else {
                Toast.makeText(activity, userInfo?.message, Toast.LENGTH_SHORT).show()
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
        viewDataBinding.viewModel?.invalidCfPassword?.observe(
            viewLifecycleOwner,
            Observer { error ->
                error.isNotEmpty().let {
                    if (it) {
                        layoutConfirmPassword.suffixTextView.visibility = View.VISIBLE
                        layoutConfirmPassword.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                            layoutConfirmPassword.suffixTextView.visibility = View.VISIBLE
                        }
                        layoutConfirmPassword.suffixText = getString(R.string.error)
                        layoutConfirmPassword.background =
                            resources.getDrawable(R.drawable.bg_border_edittext_error_10dp)
                    } else {
                        layoutConfirmPassword.background =
                            resources.getDrawable(R.drawable.bg_border_edittext_10dp)
                    }
                }
            })
        viewDataBinding.viewModel?.confirmPassword?.observe(
            viewLifecycleOwner,
            Observer { confpass ->
                confpass.isNotEmpty().let {
                    if (it) {
                        viewDataBinding.viewModel?.invalidCfPassword?.value = ""
                        layoutConfirmPassword.background =
                            resources.getDrawable(R.drawable.bg_border_edittext_10dp)
                        layoutConfirmPassword.suffixTextView.visibility = View.GONE
                        layoutConfirmPassword.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                            layoutConfirmPassword.suffixTextView.visibility = View.GONE
                        }
                    }
                }
            })
    }
}
