package org.github.gin.web

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMethod


@Controller
@RequestMapping(Array("/hello"))
class WebTestController {

    @RequestMapping( method = Array( RequestMethod.GET ) )
    @ResponseBody
    def home() =  { "Hello World!"; }

}

