package org.github.gin

import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame
import com.googlecode.lanterna.gui2.Button
import com.googlecode.lanterna.terminal.swing.ScrollingSwingTerminal
import javax.swing.JButton

object Gin {

  def main2( args : Array[String] ) : Unit = {
    Console.println("gin 0.1")

    val terminal = new ScrollingSwingTerminal()

    terminal.setVisible(true);

  }

}