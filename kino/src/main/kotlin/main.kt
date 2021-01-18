import javafx.application.Application
import javafx.geometry.Insets
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.scene.control.Label

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.stage.Screen
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.effect.MotionBlur
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.layout.StackPane.setAlignment
import javafx.scene.shape.Rectangle


var stack: ArrayList<ScrollPane> = arrayListOf()
var stackPane = StackPane()
class main: Application(){


    var key = String()

    var window = BorderPane()
    var back = buttonBack().build()
    var search = Search().build()
    var screen = Screen.getPrimary().bounds

    var scene = Scene(window, screen.width, screen.height)

    override fun start(primaryStage: Stage){

        scene.stylesheets.add(javaClass.getResource("qwerty.css").toExternalForm())
        val motionBlur = MotionBlur()
        motionBlur.radius = 200.0
        motionBlur.angle = -10.0



        window.style = ("-fx-background-image: url(" + "ki.jpg" +")")


        val rectangle = Rectangle()

        rectangle.width = screen.width
        rectangle.height = window.height  - 60.0
        rectangle.style = "-fx-background-color: black"
        rectangle.opacity = 0.8

        rectangle.effect = motionBlur

        window.center = stackPane

        stackPane.children.add(rectangle)
        val menuTop = AnchorPane(back, search)

        primaryStage.scene = scene

        window.top = menuTop
        search.addEventHandler(KeyEvent.KEY_PRESSED) { ev ->
            if (ev.code === KeyCode.ENTER) {
                key = search.text

                stackPane.children.add(searchResult().build(key))
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