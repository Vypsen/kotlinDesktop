import javafx.application.Application
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import javafx.scene.Scene

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.stage.Screen

var result = InfoFilms()

class main: Application(){
    var search = Search().build()
    var window = BorderPane()
    var screen = Screen.getPrimary().bounds
    var scene = Scene(window, screen.width, screen.height)


    override fun start(primaryStage: Stage){
        primaryStage.scene = scene
        window.center = search

        search.addEventHandler(KeyEvent.KEY_PRESSED) { ev ->
            if (ev.code === KeyCode.ENTER) {
                result = Kino.main(search.text)
                window.center = searchResult().build(result.films)
                primaryStage.show()

            }
        }


        primaryStage.show()

    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(main::class.java)
        }

    }
}