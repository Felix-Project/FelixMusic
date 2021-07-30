package com.felix.log

interface ILog {
    fun i(tag: String, msg: String)
    fun d(tag: String, msg: String)
    fun v(tag: String, msg: String)
    fun w(tag: String, msg: String, throwable: Throwable? = null)
    fun e(tag: String, msg: String, throwable: Throwable? = null)
}

object FLog : ILog {
    var iLog: ILog? = null
    override fun i(tag: String, msg: String) {
        iLog?.i(tag, msg) ?: kotlin.run {
            println("$tag:$msg")
        }
    }

    override fun d(tag: String, msg: String) {
        iLog?.d(tag, msg) ?: kotlin.run {
            println("$tag:$msg")
        }
    }

    override fun v(tag: String, msg: String) {
        iLog?.v(tag, msg) ?: kotlin.run {
            println("$tag:$msg")
        }
    }

    override fun w(tag: String, msg: String, throwable: Throwable?) {
        iLog?.w(tag, msg, throwable) ?: kotlin.run {
            println("$tag:$msg\r\n${throwable?.message}")
        }
    }

    override fun e(tag: String, msg: String, throwable: Throwable?) {
        iLog?.e(tag, msg, throwable) ?: kotlin.run {
            println("$tag:$msg\r\n${throwable?.message}")

        }
    }
}