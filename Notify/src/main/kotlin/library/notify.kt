package library

import javafx.animation.TranslateTransition
import javafx.concurrent.Task
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.input.MouseEvent
import javafx.stage.Screen
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration

class Notify{

   enum class Mode{
       IMAGE,
       Input
   }

    enum class Position {
        RIGHT_BOTTOM,
        RIGHT_TOP,
        LEFT_BOTTOM,
        LEFT_TOP
    }


    var config = Config()

    class Config{


        var conImage = ConfigImage()
        var mode = Mode.IMAGE
        var pos = Position.LEFT_BOTTOM
        var textColor = "#FFFFFF"
        var bgColor = "#000000"
        var bgOpacity = 1.0
        var waitTime = 3000
        var defWidth = 300.0
        var defHeight = 150.0
        var shift = 10.0


        fun SetMode(mode: Mode){
            this.mode = mode
        }

        fun SetPos(pos: Position) {
            this.pos = pos
        }

        fun SetTextColor(textColor: String){
            this.textColor = textColor
        }

        fun SetBgColor(bgColor: String){
            this.bgColor = bgColor
        }

        fun SetBgOpacity(bgOpacity: Double){
            this.bgOpacity = bgOpacity
        }

        fun SetWaitTime(waitTime: Int){
            this.waitTime = waitTime
        }

        fun setWidth(defWidth: Double){
            this.defWidth = defWidth
        }

        fun setHeight(defHeight: Double){
            this.defHeight = defHeight
        }

        fun SetShift(shift: Double){
            this.shift = shift
        }

    }


    var screenRect = Screen.getPrimary().bounds
    var stage = Stage()






    lateinit var content: Any

    fun start() {


        when(config.mode) {
            Mode.IMAGE -> {
                StartConfigImage().build(config)
                content = contentImage
            }
        }

        when (config.pos) {
            Position.LEFT_BOTTOM -> {
                stage.x = config.shift
                stage.y = screenRect.height - config.defHeight - config.shift
            }
            Position.LEFT_TOP -> {
                stage.x = config.shift
                stage.y = config.shift
            }
            Position.RIGHT_BOTTOM -> {
                stage.x = screenRect.width - config.defWidth - config.shift
                stage.y = screenRect.height - config.defHeight - config.shift
            }
            Position.RIGHT_TOP -> {
                stage.x = screenRect.width - config.defWidth - config.shift
                stage.y = config.shift
            }
        }

        val close = object: Task<Void>() {
            @Throws(InterruptedException::class)
            override fun call(): Void? {
                Thread.sleep(config.waitTime.toLong())
                cloaseAnim()
                return null
            }
        }
        Thread(close).start()

        stage.scene = Scene(content as Parent?, config.defWidth, config.defHeight)
        stage.initStyle(StageStyle.TRANSPARENT)
        stage.show()
        openAnim()

        stage.addEventFilter(MouseEvent.MOUSE_ENTERED_TARGET) {

        }


    }
    fun openAnim() {

        val ft = TranslateTransition(Duration.millis(2000.0), content as Node?)

        when (config.pos) {
            Position.LEFT_BOTTOM, Position.LEFT_TOP -> {
                ft.fromX = -config.defWidth
                ft.toX = 0.0
            }
            Position.RIGHT_TOP, Position.RIGHT_BOTTOM -> {
                ft.fromX = config.defWidth
                ft.toX = 0.0
            }
        }
        ft.play()
    }

    fun cloaseAnim() {
        val ft = TranslateTransition(Duration.millis(2000.0), content as Node?)

        when (config.pos) {
            Position.LEFT_BOTTOM, Position.LEFT_TOP -> {
                ft.fromX = 0.0
                ft.toX = -config.defWidth
            }
            Position.RIGHT_TOP, Position.RIGHT_BOTTOM -> {
                ft.fromX = 0.0
                ft.toX = config.defWidth
            }
        }
        ft.setOnFinished {
            stage.close()
        }
        ft.play()
    }
}