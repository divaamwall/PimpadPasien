package com.diva.pimpad.model

class Chat(
    val senderId: String? = null,
    val receiverId: String? = null,
    val message: String? = null,
    val isseen: Boolean = false,
    val urlSentImg: String? = null,
    val messageId: String? = null,
    val timeStamp: Long? = null
)
