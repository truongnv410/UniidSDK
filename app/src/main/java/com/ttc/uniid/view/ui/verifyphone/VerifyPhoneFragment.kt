package com.ttc.uniid.view.ui.verifyphone

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ttc.uniid.R
import com.ttc.uniid.data.remote.response.UserInfoResponse
import com.ttc.uniid.databinding.VerifyPhoneLayoutBinding
import com.ttc.uniid.extension.addFragment
import com.ttc.uniid.util.convertStringToMap
import com.ttc.uniid.view.ui.enterotp.EnterOTPFragment
import kotlinx.android.synthetic.main.verify_phone_layout.*

class VerifyPhoneFragment : Fragment() {
    private lateinit var viewDataBinding: VerifyPhoneLayoutBinding
    lateinit var userInfo: UserInfoResponse
    var dataUrlSocial: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = VerifyPhoneLayoutBinding.inflate(inflater, container, false).apply {
            viewModel = ViewModelProviders.of(this@VerifyPhoneFragment)
                .get(VerifyPhoneViewModel::class.java)
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
        arguments?.let {
            dataUrlSocial = arguments?.getString("data_url", "")
        }
        dataUrlSocial?.let {
            val mapData = convertStringToMap(it)
            val username = mapData?.get("username")
            val socialType = mapData?.get("socialType")
            btnNext.setOnClickListener {
                username?.let { username ->
                    viewDataBinding.viewModel?.getOTPSocialFacebook(username)
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun subscribeUI() {
        viewDataBinding.viewModel?.sendOTPSuccess?.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, getString(R.string.send_otp_success), Toast.LENGTH_SHORT)
                .show()
            dataUrlSocial?.let {
                requireActivity().addFragment(
                    EnterOTPFragment(),
                    bundleOf(
                        "dataUrlSocial" to dataUrlSocial,
                        "phone" to viewDataBinding.viewModel?.phone?.value
                    )
                )
            }
        })
    }
}
