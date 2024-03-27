package com.vicky7230.mvi

import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

object Helper {
    fun readFileResource(filename: String): String{
        val loader = ClassLoader.getSystemClassLoader()
        val json: String = Files.lines(Paths.get(loader.getResource(filename).toURI()))
            .parallel()
            .collect(Collectors.joining())
       return json
    }
}