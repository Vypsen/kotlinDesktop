import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.scene.control.Label

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.stage.Screen
import javafx.geometry.Pos
import javafx.scene.image.Image
import javafx.scene.layout.BackgroundPosition

import javafx.scene.layout.BackgroundRepeat

import javafx.scene.layout.BackgroundImage

import javafx.scene.layout.Background

import javafx.scene.layout.BackgroundSize









var window = BorderPane()
class main: Application(){


    var key = String()
    var search = Search().build()

    var screen = Screen.getPrimary().bounds



    var scene = Scene(window, screen.width, screen.height)





    override fun start(primaryStage: Stage){

        BorderPane.setAlignment(search, Pos.CENTER_RIGHT)
        BorderPane.setMargin(search, Insets(10.0, 20.0, 10.0, 0.0) )

        scene.stylesheets.add(javaClass.getResource("qwerty.css").toExternalForm())
        primaryStage.scene = scene
        window.style = ("-fx-background-image: url(" + "ki.jpg" +")")
        window.top = search


        search.addEventHandler(KeyEvent.KEY_PRESSED) { ev ->
            if (ev.code === KeyCode.ENTER) {
                key = search.text
                //scene = Scene(searchResult().build(result!!), screen.width, screen.height)
                var scroll = searchResult().build(key)
                //BorderPane.setAlignment(scroll, Pos.CENTER)
                //BorderPane.setMargin(scroll, Insets(20.0, 0.0, 20.0, 100.0) )

                window.center = scroll
                primaryStage.scene = scene
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