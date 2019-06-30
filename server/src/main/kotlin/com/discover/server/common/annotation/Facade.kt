package com.discover.server.common.annotation

import org.springframework.core.annotation.AliasFor
import org.springframework.stereotype.Service

@Target(AnnotationTarget.CLASS)
@Service
annotation class Facade(@get:AliasFor(annotation = Service::class) val value: String = "")