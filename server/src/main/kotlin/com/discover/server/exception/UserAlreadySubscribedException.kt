package com.discover.server.exception

import java.lang.RuntimeException

class UserAlreadySubscribedException(val url: String): RuntimeException("user.already.subscribed")