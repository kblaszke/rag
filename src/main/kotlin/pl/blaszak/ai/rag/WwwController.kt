package pl.blaszak.ai.rag

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import pl.blaszak.ai.rag.service.ChatService

@Controller
class WwwController(val chatService: ChatService) {

    companion object {
        const val CONVERSATION_ID = "conversationId"
        const val ATTACH_DOC = "attachDocumentation"
        const val PROMPT = "prompt"
        const val ANSWER = "answer"
    }

    @GetMapping("/")
    fun index(): ModelAndView {
        val model = ModelAndView()
        model.addObject(CONVERSATION_ID, "")
        model.addObject(ATTACH_DOC, "true")
        model.addObject(ANSWER, "")
        model.viewName = "index"
        return model
    }

    @PostMapping("/")
    fun postIndex(@RequestParam requestParams: Map<String, String>) : ModelAndView {
        val conversationId = if(requestParams[CONVERSATION_ID].isNullOrEmpty()) chatService.initConversation() else requestParams[CONVERSATION_ID].toString()
        val prompt = requestParams[PROMPT]
        val attachDocumentation = requestParams[ATTACH_DOC].toBoolean()
        val model = ModelAndView()
        val answer = chatService.handle(conversationId,prompt, attachDocumentation)
        model.addObject(ANSWER, answer)
        model.addObject(CONVERSATION_ID, conversationId)
        model.viewName = "index"
        return model
    }
}