package com.cyrus.base_kotlin_mvvm.eventbus

class MessageEvent : ObjectEvent {

    companion object {}

    var message: String
    var valueString: String? = null
    var valueInt: Int? = null
    var obj: Any? = null
    var valueBoolean: Boolean? = null

    constructor(message: String) {
        this.message = message
    }

    constructor(message: String, valueString: String?) {
        this.message = message
        this.valueString = valueString
    }

    constructor(message: String, valueBoolean: Boolean?) {
        this.message = message
        this.valueBoolean = valueBoolean
    }

    constructor(message: String, valueInt: Int?) {
        this.message = message
        this.valueInt = valueInt
    }

    constructor(message: String, obj: Any?) {
        this.message = message
        this.obj = obj
    }
}