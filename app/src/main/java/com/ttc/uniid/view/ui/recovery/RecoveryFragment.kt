package com.ttc.uniid.view.ui.recovery

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
import com.ttc.uniid.databinding.LayoutRecoveryBinding
import com.ttc.uniid.view.ui.enterotp.EnterOTPFragment
import kotlinx.android.synthetic.main.layout_recovery.*

class RecoveryFragment : Fragment() {
    private lateinit var viewDataBinding: LayoutRecoveryBinding
    private var userInfo: UserInfoResponse? = null
    var isSendOTPEmail = false
    var isLogin = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = LayoutRecoveryBinding.inflate(inflater, container, false).apply {
            viewModel = ViewModelProviders.of(this@RecoveryFragment)
                .get(RecoveryViewModel::class.java)
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
        userInfo = arguments?.getParcelable<UserInfoResponse>("user_info")!!
        isLogin = arguments?.getBoolean("isLogin", false) as Boolean
        userInfo?.userInfoDTO?.endUserDTO?.email.isNullOrEmpty().let {
            if (it) {
                llVerifyByEmail.visibility = View.GONE
            }
        }
        userInfo?.userInfoDTO?.endUserDTO?.email?.let {
            tvOTPEmail.text = String.format(
                getString(R.string.verification_code),
                userInfo?.userInfoDTO?.endUserDTO?.email
            )
        }
        tvOTPPhone.text = String.format(
            getString(R.string.verification_code),
            userInfo?.userInfoDTO?.endUserDTO?.phone
        )
        btnOTPhone.setOnClickListener {
            userInfo?.let { uInfo ->
                if (isLogin) {
                    viewDataBinding.viewModel?.accuracy("PHONE", uInfo)
                } else {
                    viewDataBinding.viewModel?.sendOTP("PHONE", uInfo)
                }
                isSendOTPEmail = false
            }
        }
        btnOTPEmail.setOnClickListener {
            userInfo?.let { uInfo ->
                if (isLogin) {
                    viewDataBinding.viewModel?.accuracy("EMAIL", uInfo)
                } else {
                    viewDataBinding.viewModel?.sendOTP("EMAIL", uInfo)
                }
                isSendOTPEmail = true
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun subscribeUI() {
        viewDataBinding.viewModel?.sendOTPSuccess?.observe(viewLifecycleOwner, Observer {
            val bundle = bundleOf(
                "user_info" to userInfo, "isSendOTPEmail" to isSendOTPEmail
            )
            val fragmentManager: FragmentManager? = activity?.supportFragmentManager
            val fragmentTransaction: FragmentTransaction? = fragmentManager?.beginTransaction()
            val enterOTPFragment = EnterOTPFragment()
            enterOTPFragment.arguments = bundle
            fragmentTransaction?.add(android.R.id.content, enterOTPFragment)
                ?.addToBackStack("EnterOTPFragment")?.commit()
        })
        viewDataBinding.viewModel?.accuracySuccess?.observe(viewLifecycleOwner, Observer{
            val bundle = bundleOf(
                "user_info" to userInfo, "isSendOTPEmail" to isSendOTPEmail, "isLogin" to isLogin
            )
            val fragmentManager: FragmentManager? = activity?.supportFragmentManager
            val fragmentTransaction: FragmentTransaction? = fragmentManager?.beginTransaction()
            val enterOTPFragment = EnterOTPFragment()
            enterOTPFragment.arguments = bundle
            fragmentTransaction?.add(android.R.id.content, enterOTPFragment)
                ?.addToBackStack("EnterOTPFragment")?.commit()
        })
        viewDataBinding.viewModel?.toastMessage?.observe(viewLifecycleOwner, Observer { msg ->
            activity?.let{
                Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
