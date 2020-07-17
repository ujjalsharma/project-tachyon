package com.confessionapp

import com.google.firebase.database.ServerValue
import java.util.*


class Post {

    var confession: String? = null
    var year: String? = null
    var branch: String? = null
    var gender: String? = null
    var timestamp: String? = null
    var postNumber: String? = null
    var postID: String? = null
    var userID: String? = null

    constructor(
        confession: String?,
        year: String?,
        branch: String?,
        gender: String?,
        timestamp: String?,
        postNumber: String?,
        postID: String?,
        userID: String?
    ) {
        this.confession = confession
        this.year = year
        this.branch = branch
        this.gender = gender
        this.timestamp = timestamp
        this.postNumber = postNumber
        this.postID = postID
        this.userID = userID
    }

    // make sure to have an empty constructor inside ur model class
    constructor() {}

}