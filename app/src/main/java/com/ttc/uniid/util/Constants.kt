package com.ttc.uniid.util

class Constants {
    companion object {
        const val BASE_URL = "http://directory.ttc-solutions.com:8989/"
        const val EMAIL_REGEX = "[A-Z0-9a-z.]{1,64}+@([A-Za-z0-9.^\\\\-{0,2}\$]){1,253}"
        const val PATERN_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*d)(?=.*[@\$!%*?&])[A-Za-zd@\$!%*?&]{8,}\$"
        const val PATERN_NAME = "[a-zA-Z]"
        const val REQUEST_TIMEOUT_DURATION = 10
        const val DEBUG = true
    }
}