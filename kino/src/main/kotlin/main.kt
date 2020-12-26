import javafx.application.Application
import javafx.concurrent.Task
import javafx.geometry.Insets
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.HBox
import javafx.stage.Screen



class main: Application(){


    var key = String()
    var search = Search().build()
    var window = BorderPane()
    var screen = Screen.getPrimary().bounds
    var scrollPane = ScrollPane()


    var scene = Scene(window, screen.width, screen.height)




    override fun start(primaryStage: Stage){


        primaryStage.scene = scene
        window.center = search
        window.right = scrollPane
        window.padding = Insets(30.0, 0.0, 30.0,0.0)

        search.addEventHandler(KeyEvent.KEY_PRESSED) { ev ->
            if (ev.code === KeyCode.ENTER) {
                key = search.text
                var result = Kino.main(key)
                window.center = searchResult().build(result!!)
            }
        }

        primaryStage.show()

    }

    companion object {
        @JvmStatic

        fun main(args: Array<String>)
        {
            launch(main::class.java)
        }

    }
}