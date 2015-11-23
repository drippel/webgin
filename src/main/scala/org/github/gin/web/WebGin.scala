package org.github.gin.web

import org.springframework.boot._;
import org.springframework.boot.autoconfigure._;
import org.springframework.stereotype._;
import org.springframework.web.bind.annotation._;

object WebGin {

    def main( args: Array[String] ) : Unit = {
        SpringApplication.run( classOf[WebGinConfig]);
    }
}

