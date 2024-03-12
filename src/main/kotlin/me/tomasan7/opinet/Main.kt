package me.tomasan7.opinet

import javax.swing.JOptionPane

fun main()
{
    val program = OpiNet()
    try
    {
        program.init()
        program.start()
    }
    catch (e: Exception)
    {
        JOptionPane.showMessageDialog(null, e.message ?: "There was an unknown error", "Error", JOptionPane.ERROR_MESSAGE)
        e.printStackTrace()
    }
}
