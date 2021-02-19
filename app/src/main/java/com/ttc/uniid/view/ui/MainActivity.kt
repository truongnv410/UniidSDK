package com.ttc.uniid.view.ui

import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import com.ttc.uniid.R
import com.ttc.uniid.extension.addFragment
import com.ttc.uniid.util.convertStringToMap
import com.ttc.uniid.view.base.BaseActivityNew
import com.ttc.uniid.view.ui.login.LoginFragment
import com.ttc.uniid.view.ui.verifyphone.VerifyPhoneFragment

class MainActivity : BaseActivityNew() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addFragment(R.id.container, LoginFragment())
        /**
         * Check intent từ webview khi đăng nhập từ facebook.
         */
        if (intent != null && intent.data != null && intent.data?.scheme
                .equals("unitelsdk")
        ) {
            val mapData = convertStringToMap(intent.data?.query)
            val type = mapData?.get("type")
            type?.let {
                if (type == "ACCURACY") {
                    addFragment(
                        R.id.container,
                        VerifyPhoneFragment(),
                        bundleOf("data_url" to intent.data?.query)
                    )
                } else {
                    Toast.makeText(this, "Đóng SDK", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}