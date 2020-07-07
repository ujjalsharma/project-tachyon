package com.confessionapp

class Comment {


    var comment: String? = null
    var userID: String? = null
    var timestamp: String? = null
    var commentID: String? = null

    constructor(
        comment: String?,
        userID: String?,
        timestamp: String?,
        commentID: String?
    ) {
        this.comment = comment
        this.userID = userID
        this.timestamp = timestamp
        this.commentID = commentID
    }

    // make sure to have an empty constructor inside ur model class
    constructor() {}

}