package pl.blaszak.ai.rag

import org.springframework.ai.chat.model.ChatResponse

fun ChatResponse.textResponse() = this.result.output.text.toString()