package org.github.gin.web

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam


@Controller
@RequestMapping(Array("/gin"))
class GinController {

    @RequestMapping( method = Array( RequestMethod.GET ) )
    def welcome() =  { "index"; }

    @RequestMapping( method = Array( RequestMethod.POST ) )
    def post( @RequestParam(required=true, value="action") action : String ) =  {
      Console.println(action)
      "index";
    }
}

