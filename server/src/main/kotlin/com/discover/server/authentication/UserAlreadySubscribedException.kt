package com.discover.server.authentication

import java.lang.RuntimeException

class UserAlreadySubscribedException(val url: String): RuntimeException("user.already.subscribed")