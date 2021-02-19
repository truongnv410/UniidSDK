package com.ttc.uniid.customview

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.ttc.uniid.R
import com.ttc.uniid.databinding.DialogFragmentResultBinding
import com.ttc.uniid.extension.setVisible
import com.ttc.uniid.util.autoCleared


class CustomAlertDialog : DialogFragment() {

    var viewDataBinding by autoCleared<DialogFragmentResultBinding>()

    private var title: String = ""

    @DrawableRes
    private var icon: Int = -1
    private var description: String = ""
    private var mCancelable: Boolean = false
    private var messageActionOne: String = ""
    var callbackActionOne: (() -> Unit)? = null
    private var messageColor: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.run {
            title = getString(Params.TITLE, "")
            icon = getInt(Params.ICON, -1)
            description = getString(Params.MESSAGE, "")
            mCancelable = getBoolean(Params.CANCELABLE, false)
            messageActionOne = getString(Params.ACTION_ONE_MESSAGE, "")
            messageColor = getInt(Params.COLOR_MESSAGE, R.color.black)
        }
        isCancelable = mCancelable
        viewDataBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_result, container, false)
        return viewDataBinding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // the content
        val root = RelativeLayout(activity)
        root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        // creating the fullscreen dialog
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window!!.setWindowAnimations(R.style.DialogAnimation)
        return dialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        with(viewDataBinding) {
            when (title.isEmpty()) {
                true -> tvTitle.setVisible(false)
                false -> {
                    tvTitle.setVisible(true)
                    tvTitle.text = title
                }
            }

            when (description.isEmpty()) {
                true -> tvDescription.setVisible(false)
                false -> {
                    tvDescription.setVisible(true)
                    tvDescription.text = description
                    context?.let {
                        tvDescription.setTextColor(ContextCompat.getColor(it, messageColor))
                    }
                }
            }
            when (messageActionOne.isEmpty()) {
                true -> tvAction1.setVisible(false)
                false -> {
                    tvAction1.setVisible(true)
                    tvAction1.text = messageActionOne
                }
            }
            tvAction1.setOnClickListener {
                dismiss()
                callbackActionOne?.invoke()
            }
        }
    }

    data class Builder(
        val title: String? = null,
        @DrawableRes val icon: Int? = null,
        val message: Pair<String, Int>? = null,
        val cancelable: Boolean? = false,
        val actionOneMessage: String? = null,
        val actionOneAction: (() -> Unit)? = null
    ) {

        fun build(): CustomAlertDialog {
            val dialog = CustomAlertDialog()
            val bundle = Bundle()
            bundle.apply {
                title?.let { putString(Params.TITLE, it) }
                icon?.let { putInt(Params.ICON, it) }
                message?.let {
                    putString(Params.MESSAGE, it.first)
                    putInt(Params.COLOR_MESSAGE, it.second)
                }
                cancelable?.let { putBoolean(Params.CANCELABLE, it) }
                actionOneMessage?.let { putString(Params.ACTION_ONE_MESSAGE, it) }
            }
            dialog.arguments = bundle
            actionOneAction?.let {
                dialog.callbackActionOne = it
            }
            return dialog
        }
    }

    object Params {
        const val TITLE = "TITLE"
        const val ICON = "ICON"
        const val MESSAGE = "MESSAGE"
        const val COLOR_MESSAGE = "COLOR_MESSAGE"
        const val CANCELABLE = "CANCELABLE"
        const val ACTION_ONE_MESSAGE = "ACTION_ONE_MESSAGE"
        const val ACTION_TWO_MESSAGE = "ACTION_TWO_MESSAGE"
    }
}