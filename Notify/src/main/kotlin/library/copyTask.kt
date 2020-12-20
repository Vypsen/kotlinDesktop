package library

import javafx.concurrent.Task
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File
import java.lang.Exception
import java.util.ArrayList



class CopyTask : Task<List<File>>() {



    var st = Stage()
    val fileChooser = FileChooser()
    var selectedFiles: MutableList<File> = fileChooser.showOpenMultipleDialog(st)


    override fun call(): List<File> {
        val files = selectedFiles
        val count = files!!.size
        val copied: MutableList<File> = ArrayList()
        var i = 0
        for (file in files) {
            if (file.isFile) {
                copy(file)
                copied.add(file)
            }
            i++
            this.updateProgress(i.toLong(), count.toLong())
        }
        return copied
    }

    private fun copy(file: File) {
        updateMessage("Copying: " + file.absolutePath)
        Thread.sleep(500)
    }
}