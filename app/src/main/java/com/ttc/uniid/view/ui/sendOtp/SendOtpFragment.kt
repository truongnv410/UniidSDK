package com.ttc.uniid.view.ui.sendOtp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ttc.uniid.R
import com.ttc.uniid.data.remote.request.RegisterPhonesRequest
import com.ttc.uniid.data.remote.response.RegisterPhonesRespond
import com.ttc.uniid.databinding.LayoutCreateUniidAccountVerifyOtpBinding
import com.ttc.uniid.extension.replaceFragment
import com.ttc.uniid.view.ui.updateAccountInfo.UpdateAccountInfoFragment
import kotlinx.android.synthetic.main.layout_create_uniid_account_verify_otp.*

class SendOtpFragment :Fragment(){

    private lateinit var viewDataBinding: LayoutCreateUniidAccountVerifyOtpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = LayoutCreateUniidAccountVerifyOtpBinding.inflate(inflater, container, false).apply {
            viewModel = ViewModelProviders.of(this@SendOtpFragment).get(SendOtpViewModel::class.java)
            setLifecycleOwner(viewLifecycleOwner)
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        subscribeUI()
    }
    private fun subscribeUI() = with(viewDataBinding?.viewModel!!) {
        verifierPhoneSuccess?.observe(viewLifecycleOwner, Observer {
            verifierPhoneRespond?.let {
                if (it.value?.errorCode==100) {
                    var bundle = Bundle()
                    bundle.putParcelable("VerifierPhoneRespond", verifierPhoneRespond?.value)
                    val updateAccountInfoFragment = UpdateAccountInfoFragment()
                    updateAccountInfoFragment.arguments = bundle
                    requireActivity().replaceFragment(updateAccountInfoFragment, updateAccountInfoFragment.arguments)
                } else {
                    Toast.makeText(context, it.value?.message, Toast.LENGTH_SHORT).show()
                }

            }
        })
        otpCode?.observe(viewLifecycleOwner ,Observer { account ->
            account.isNotEmpty().let {
                if (it) {
                    invalidOTPCode?.value = ""
                    layoutOTP.background =
                        resources.getDrawable(R.drawable.bg_border_edittext_10dp)
                    layoutOTP.suffixTextView.visibility = View.GONE
                    layoutOTP.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                        layoutOTP.suffixTextView.visibility = View.GONE
                    }
                }
            }
        })
        invalidOTPCode?.observe(viewLifecycleOwner, Observer { error ->
            error.isNotEmpty().let {
                if (it) {
                    layoutOTP.suffixTextView.visibility = View.VISIBLE
                    layoutOTP.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                        layoutOTP.suffixTextView.visibility = View.VISIBLE
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
        toastMessage?.observe(viewLifecycleOwner, Observer { msg ->
            activity?.let {
                Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun setupUI() = with(viewDataBinding) {
        viewModel?.registerPhonesRespond?.postValue(arguments?.getParcelable<RegisterPhonesRespond>("RegisterPhonesRespond"))
        viewModel?.registerPhonesRequest = arguments?.getParcelable<RegisterPhonesRequest>("RegisterPhonesRequest")

        btnNext.setOnClickListener {
            viewModel?.verifyPhoneRegister()
        }
        tvResendOTP.setOnClickListener {
            viewModel?.reSendOtp()
        }
    }

}