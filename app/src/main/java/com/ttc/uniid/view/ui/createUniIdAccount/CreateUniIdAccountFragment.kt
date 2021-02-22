package com.ttc.uniid.view.ui.createUniIdAccount

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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputLayout
import com.ttc.uniid.R
import com.ttc.uniid.databinding.LayoutCreateUniidAccountBinding
import com.ttc.uniid.extension.replaceFragment
import com.ttc.uniid.extension.showDialogFragment
import com.ttc.uniid.view.ui.dialog.PrivacyAndTermsDialog
import com.ttc.uniid.view.ui.sendOtp.SendOtpFragment
import kotlinx.android.synthetic.main.change_password_layout.*
import kotlinx.android.synthetic.main.change_password_layout.layoutPassword
import kotlinx.android.synthetic.main.layout_create_uniid_account.*

class CreateUniIdAccountFragment : Fragment(){
    private lateinit var viewDataBinding: LayoutCreateUniidAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = LayoutCreateUniidAccountBinding.inflate(inflater, container, false).apply {
            viewModel = ViewModelProviders.of(this@CreateUniIdAccountFragment)
                .get(CreateUniIdAccountViewModel::class.java)
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
    private fun subscribeUI()= with(viewDataBinding?.viewModel!!){
        registerSuccess.observe(viewLifecycleOwner, Observer {
            registerPhonesRespond?.let {
                if (it.value?.errorCode == 100) {
                    it.value?.registerPhonesData?.let {
                        var bundle = Bundle()
                        bundle.putParcelable(
                            "RegisterPhonesRespond",
                            registerPhonesRespond.value
                        )
                        bundle.putParcelable(
                            "RegisterPhonesRequest",
                            registerPhonesRequest
                        )
                        val sendOtpFragment = SendOtpFragment()
                        sendOtpFragment.arguments = bundle
                        requireActivity().replaceFragment(
                            sendOtpFragment,
                            sendOtpFragment.arguments
                        )
                    }
                }else{
                    Toast.makeText(
                        requireActivity(),
                        it.value?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        invalidPhoneNumber.observe(viewLifecycleOwner, Observer { error ->
            error.isNotEmpty().let {
                setInvalidUI(it,layoutPhoneNumber)
            }
        })
        phoneNumber.observe(viewLifecycleOwner, Observer { account ->
            account.isNotEmpty().let {
                setErrorUI(it,layoutPhoneNumber,invalidPhoneNumber)
            }
        })
        invalidPassWord.observe(viewLifecycleOwner, Observer { error ->
            error.isNotEmpty().let {
                setInvalidUI(it,layoutPassword)
            }
        })
        passWord.observe(viewLifecycleOwner, Observer { account ->
            account.isNotEmpty().let {
                setErrorUI(it,layoutPassword,invalidPassWord)
            }
        })
        invalidConfirmPassword.observe(viewLifecycleOwner, Observer { error ->
            error.isNotEmpty().let {
                setInvalidUI(it,layout_confirm_password)
            }
        })
        confirmPassword.observe(viewLifecycleOwner, Observer { account ->
            account.isNotEmpty().let {
                setErrorUI(it,layout_confirm_password,invalidConfirmPassword)
            }
        })
        invalidUserName.observe(viewLifecycleOwner, Observer { error ->
            error.isNotEmpty().let {
                setInvalidUI(it,layouUsertName)
            }
        })
        userName.observe(viewLifecycleOwner, Observer { account ->
            account.isNotEmpty().let {
                setErrorUI(it,layouUsertName,invalidUserName)
            }
        })
        invalidFirstName.observe(viewLifecycleOwner, Observer { error ->
            error.isNotEmpty().let {
                setInvalidUI(it,layout_first_name)
            }
        })
        firstName.observe(viewLifecycleOwner, Observer { account ->
            account.isNotEmpty().let {
                setErrorUI(it,layout_first_name,invalidFirstName)
            }
        })
        invalidLastName.observe(viewLifecycleOwner, Observer { error ->
            error.isNotEmpty().let {
                setInvalidUI(it,layout_last_name)
            }
        })
        lastName.observe(viewLifecycleOwner, Observer { account ->
            account.isNotEmpty().let {
                setErrorUI(it,layout_last_name,invalidLastName)
            }
        })
    }


    private fun setupUI() = with(viewDataBinding) {
        resetUIErrorPassword(layoutPassword)
        resetUIErrorPassword(layoutConfirmPassword)
        btnNext.setOnClickListener {
            viewModel?.registerPhones()
        }
        tvPrivacy.setOnClickListener {
            val bundle = bundleOf("CONTENT" to getString(R.string.content))
            var dialog = PrivacyAndTermsDialog()
            dialog.arguments = bundle
            showDialogFragment(dialog,"",false)
        }

    }

    private fun resetUIErrorPassword(layoutPassword: TextInputLayout) {
        val sufPassword =
            layoutPassword.findViewById<AppCompatTextView>(com.google.android.material.R.id.textinput_suffix_text)
        sufPassword.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        sufPassword.gravity = Gravity.CENTER
    }
    private fun setErrorUI(error: Boolean, layout: TextInputLayout, invalid: MutableLiveData<String>) {
        if (error) {
            invalid.value = ""
            layout.background =
                resources.getDrawable(R.drawable.bg_border_edittext_10dp)
            layout.suffixTextView.visibility = View.GONE
            layout.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                layout.suffixTextView.visibility = View.GONE
            }
        }
    }
    private fun setInvalidUI(error: Boolean,layout: TextInputLayout) {
        if (error) {
            layout.suffixTextView.visibility = View.VISIBLE
            layout.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                layout.suffixTextView.visibility = View.VISIBLE
            }
            layout.suffixText = getString(R.string.error)
            layout.background =
                resources.getDrawable(R.drawable.bg_border_edittext_error_10dp)
        } else {
            layout.background =
                resources.getDrawable(R.drawable.bg_border_edittext_10dp)
        }
    }

}