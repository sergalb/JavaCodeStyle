package ru.ifmo.rain.balahnin

import JavaBaseVisitor
import JavaParser
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.TerminalNode
import java.io.OutputStreamWriter

class JavaCodeStyleVisitor(val outputStreamWriter: OutputStreamWriter) : JavaBaseVisitor<Void?>() {
    private val LINE_SEPARATOR: String = System.lineSeparator()
    private var codeBlockDepth = 0
    private fun writeIntends() {
        outputStreamWriter.write((" ".repeat(4 * codeBlockDepth)))
    }

    private fun newLine() {
        outputStreamWriter.write(System.lineSeparator())
    }

    private fun writeLine(line: String) {
        writeIntends()
        outputStreamWriter.write(line)
    }

    override fun visitFile(ctx: JavaParser.FileContext): Void? {
        visitPackage_(ctx.package_())

        var hasImport = false;
        for (import in ctx.import_()) {
            visitImport_(import)
            hasImport = true
        }
        if (hasImport) newLine()
        visitClassDefinition(ctx.classDefinition())
        return null
    }

    override fun visitPackage_(ctx: JavaParser.Package_Context?): Void? {
        if (ctx != null) {
            outputStreamWriter.write("package ")
            visitClassType(ctx.classType())
            outputStreamWriter.write(";")
            newLine()
            newLine()
        }
        return null
    }

    override fun visitImport_(ctx: JavaParser.Import_Context): Void? {
        visitTerminal(ctx.IMPORT())
        outputStreamWriter.write(" ")
        visitClassType(ctx.classType())
        visitTerminal(ctx.DOT())
        visitTerminal(ctx.MUL())
        visitTerminal(ctx.SEMI())
        newLine()
        return null
    }

    override fun visitClassDefinition(ctx: JavaParser.ClassDefinitionContext): Void? {
        for (child in ctx.children) {
            child.accept(this)
            if (child is TerminalNode && child.text == "{") {
                codeBlockDepth++
                newLine()
            }
            if (codeBlockDepth == 0) {
                outputStreamWriter.write(" ")
            }
        }
        codeBlockDepth--
        return null
    }

    override fun visitField(ctx: JavaParser.FieldContext?): Void? {
        if (ctx != null) {
            writeIntends()
            for (childInd in ctx.children.indices) {
                val child = ctx.getChild(childInd)
                child.accept(this)
                if (childInd != ctx.childCount - 2) {
                    outputStreamWriter.write(" ")
                }

            }
            newLine()
        }
        return null
    }

    override fun visitNewWithCreation(ctx: JavaParser.NewWithCreationContext?): Void? {
        if (ctx !== null) {
            visitTerminal(ctx.NEW())
            outputStreamWriter.write(" ")
            ctx.getChild(1).accept(this)
        }
        return null
    }

    override fun visitArgumentList(ctx: JavaParser.ArgumentListContext?): Void? {
        visitList(ctx)
        return null
    }

    override fun visitMethodDefinition(ctx: JavaParser.MethodDefinitionContext?): Void? {
        if (ctx !== null) {
            newLine()
            writeIntends()
            if (ctx.accessModifiers() !== null) {
                visitAccessModifiers(ctx.accessModifiers())
                outputStreamWriter.write(" ")
            }
            visitTypeWithVoid(ctx.typeWithVoid())
            outputStreamWriter.write(" ")
            visitTerminal(ctx.Identifier())
            visitTerminal(ctx.LPAREN())
            visitParametrList(ctx.parametrList())
            visitTerminal(ctx.RPAREN())
            visitCodeBlock(ctx.codeBlock())
            newLine()
        }
        return null
    }

    override fun visitParametrList(ctx: JavaParser.ParametrListContext?): Void? {
        visitList(ctx)
        return null
    }

    private fun visitList(ctx: ParserRuleContext?) {
        if (ctx !== null) {
            for (child in ctx.children) {
                child.accept(this)
                if (child is TerminalNode) {
                    outputStreamWriter.write(" ")
                }
            }
        }
    }

    override fun visitParametrElement(ctx: JavaParser.ParametrElementContext?): Void? {
        if (ctx !== null) {
            visitType(ctx.type())
            outputStreamWriter.write(" ")
            visitTerminal(ctx.Identifier())
        }
        return null
    }

    override fun visitCodeBlock(ctx: JavaParser.CodeBlockContext?): Void? {
        if (ctx !== null) {
            outputStreamWriter.write(" ")
            visitTerminal(ctx.LBRACE())
            newLine()
            codeBlockDepth++
            for (body in ctx.body()) {
                if (body.functionOrConstructorCall() != null) {
                    writeIntends()
                    visitBody(body)
                    newLine()
                } else {
                    visitBody(body)
                }
            }
            codeBlockDepth--
            writeLine(ctx.RBRACE().text)
        }
        return null
    }

    override fun visitAssigment(ctx: JavaParser.AssigmentContext?): Void? {
        if (ctx !== null) {
            writeIntends()
            visitChildren(ctx)
            newLine()
        }
        return null
    }

    override fun visitWhileLoop(ctx: JavaParser.WhileLoopContext?): Void? {
        if (ctx !== null) {
            writeIntends()
            visitTerminal(ctx.WHILE())
            outputStreamWriter.write(" ")
            for (i in 1 until ctx.childCount) {
                ctx.getChild(i).accept(this)
            }
            newLine()
        }
        return null
    }

    override fun visitIfExpression(ctx: JavaParser.IfExpressionContext?): Void? {
        if (ctx !== null) {
            writeIntends()
            visitTerminal(ctx.IF())
            outputStreamWriter.write(" ")
            for (i in 1 until ctx.childCount) {
                if (ctx.getChild(i) is TerminalNode && ctx.getChild(i).text == "else") {
                    outputStreamWriter.write(" ")
                }
                ctx.getChild(i).accept(this)
            }
            newLine()
        }
        return null
    }

    override fun visitComparisonOperator(ctx: JavaParser.ComparisonOperatorContext?): Void? {
        commonVisitOperator(ctx)
        return null
    }

    override fun visitLogicOperator(ctx: JavaParser.LogicOperatorContext?): Void? {
        commonVisitOperator(ctx)
        return null
    }

    override fun visitReturnStatement(ctx: JavaParser.ReturnStatementContext?): Void? {
        if (ctx !== null) {
            writeIntends()
            visitTerminal(ctx.RETURN())
            outputStreamWriter.write(" ")
            visitExpression(ctx.expression())
            visitTerminal(ctx.SEMI())
        }
        return null
    }

    override fun visitTerminal(node: TerminalNode?): Void? {
        if (node != null) {
            outputStreamWriter.write(node.text)
        }
        return null
    }

    override fun visitOperator(ctx: JavaParser.OperatorContext?): Void? {
        commonVisitOperator(ctx)
        return null
    }

    private fun commonVisitOperator(ctx: ParserRuleContext?) {
        if (ctx !== null) {
            outputStreamWriter.write(" ")
            visitChildren(ctx)
            outputStreamWriter.write(" ")
        }
    }

    override fun visitAssignmentOperator(ctx: JavaParser.AssignmentOperatorContext?): Void? {
        commonVisitOperator(ctx)
        return null
    }
}