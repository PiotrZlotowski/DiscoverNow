package com.discover.server.custom.dialect

import org.hibernate.dialect.PostgreSQL95Dialect
import org.hibernate.dialect.function.SQLFunctionTemplate
import org.hibernate.type.StandardBasicTypes

class CustomPostgresSqlgitDialect: PostgreSQL95Dialect {

    constructor(): super() {
        registerFunction("datediff", SQLFunctionTemplate(StandardBasicTypes.INTEGER, "cast(?1 as varchar)"))
    }


}