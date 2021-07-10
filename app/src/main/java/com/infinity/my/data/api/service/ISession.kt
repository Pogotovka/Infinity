package com.infinity.my.data.api.service

import com.infinity.my.BuildConfig

interface ISession {
    var baseHost: String
}

class Session : ISession {

    override var baseHost: String = BuildConfig.HOST_API_RELEASE
}