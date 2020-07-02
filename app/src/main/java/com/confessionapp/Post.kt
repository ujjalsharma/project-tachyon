package com.confessionapp

import com.google.firebase.database.ServerValue
import java.util.*


class Post {

    var confession: String? = null
    var year: String? = null
    var branch: String? = null
    var gender: String? = null
    var timeStamp: Any? = null

    constructor(
        confession: String?,
        year: String?,
        branch: String?,
        gender: String?
    ) {
        this.confession = confession
        this.year =year
        this.branch = branch
        this.gender = gender
        timeStamp = ServerValue.TIMESTAMP
    }

    // make sure to have an empty constructor inside ur model class
    constructor() {}

}