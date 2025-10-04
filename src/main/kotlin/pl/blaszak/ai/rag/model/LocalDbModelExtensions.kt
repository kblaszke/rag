package pl.blaszak.ai.rag.model

import org.springframework.ai.chat.messages.UserMessage

fun List<LocalDbMessage>.toUserMessageList() = this
    .map { localDbMessage -> UserMessage.builder().text(localDbMessage.text).build() }
    .toList()