package com.ttc.uniid.view.ui.forgotpassword

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
import com.ttc.uniid.databinding.ForgotPasswordFragmentBinding
import com.ttc.uniid.extension.replaceFragment
import com.ttc.uniid.view.ui.recovery.RecoveryFragment
import kotlinx.android.synthetic.main.forgot_password_fragment.*

class ForgotPasswordFragment : Fragment() {
    private lateinit var viewDataBinding: ForgotPasswordFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = ForgotPasswordFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = ViewModelProviders.of(this@ForgotPasswordFragment)
                .get(ForgotPasswordViewModel::class.java)
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
        txtSignInStead.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        btnNext.setOnClickListener {
            viewDataBinding.viewModel?.findUserName()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun subscribeUI() {
        viewDataBinding.viewModel?.userInfo?.observe(viewLifecycleOwner, Observer { userInfo ->
            if (userInfo.errorCode == 100) {
                val bundle = bundleOf("user_info" to userInfo)
                val fragmentManager: FragmentManager? = activity?.supportFragmentManager
                val fragmentTransaction: FragmentTransaction? = fragmentManager?.beginTransaction()
                val fragmentRecovery = RecoveryFragment()
                fragmentRecovery.arguments = bundle
                fragmentTransaction?.add(android.R.id.content, fragmentRecovery)
                    ?.addToBackStack("ChangePasswordFragment")?.commit()
                //activity?.replaceFragment(ForgotPasswordFragment(), Bundle())
            } else {
                Toast.makeText(activity, userInfo?.message, Toast.LENGTH_SHORT).show()
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
                    layoutName.background =
                        resources.getDrawable(R.drawable.bg_border_edittext_10dp)
                    layoutName.suffixTextView.visibility = View.GONE
                    layoutName.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                        layoutName.suffixTextView.visibility = View.GONE
                    }
                }
            }
        })
    }
}
