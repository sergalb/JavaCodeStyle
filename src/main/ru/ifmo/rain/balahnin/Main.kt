package ru.ifmo.rain.balahnin

import JavaLexer
import JavaParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream
import java.io.OutputStreamWriter

fun main() {
    val name = readLine()
    val lexer = JavaLexer(CharStreams.fromFileName("C:\\Users\\Sergalb\\Desktop\\Java code style\\src\\test\\ru\\ifmo\\rain\\balahnin\\$name.input"))
    val parser = JavaParser(CommonTokenStream(lexer))
    val tree = parser.file()
    val outputStreamWriter = OutputStreamWriter(FileOutputStream("C:\\Users\\Sergalb\\Desktop\\Java code style\\src\\test\\ru\\ifmo\\rain\\balahnin\\$name.output"))
    val visitor = JavaCodeStyleVisitor(outputStreamWriter)
    visitor.visitFile(tree)

    outputStreamWriter.close()

}