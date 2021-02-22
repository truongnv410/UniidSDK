package com.ttc.uniid.view.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ttc.uniid.R
import com.ttc.uniid.databinding.DialogFragmentPrivaceAndTermsBinding
import com.ttc.uniid.view.base.BaseDialogFragmentSimple

class PrivacyAndTermsDialog : BaseDialogFragmentSimple<DialogFragmentPrivaceAndTermsBinding>() {

    private var content: String? = null

    override val layoutId: Int
        get() = R.layout.dialog_fragment_privace_and_terms

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.run {
            content = getString(Params.CONTENT, "")
        }
        isCancelable = false
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() = with(viewDataBinding) {
        tvTitle.text= getString(R.string.privacy_and_terms)
        tvDescription.text = getString(R.string.mess_confirm_privacy_and_terms)
        tvContent.text = content
        tvAction1.text = getString(R.string.close)
        tvAction1.setOnClickListener {
            dismiss()
        }
        tvClose.setOnClickListener {
            dismiss()
        }
    }

    object Params {
        const val CONTENT = "CONTENT"
    }

}