package library

import javafx.animation.TranslateTransition
import javafx.concurrent.Task
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.paint.Color
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.stage.Screen
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration
import java.io.File
import kotlin.system.exitProcess


var notify = Notify()
class Notify{

    enum class Border {
        SQUARE,
        CIRCLE
    }

    enum class Position {
        RIGHT_BOTTOM,
        RIGHT_TOP,
        LEFT_BOTTOM,
        LEFT_TOP
    }


    var content = VBox()

    var pos = Position.LEFT_BOTTOM
    var textColor = "#FFFFFF"
    var bgColor = "#222222"
    var msg = "MESSAGE"
    var bgOpacity = 0.9
    var waitTime = 5000
    var defWidth = 300.0
    var defHeight = 170.0
    var shift = 10.0
    var textField: TextField? = null

    var buttonOk = Button("Сдаться")
    var buttonCancel = Button("Самоубийство")
    var colorButton = "#FFFFFF"

    var iconBorder = Border.SQUARE
    var iconPath = ""
    var titleNotify = "Title"



    fun setBorder(iconBorder: Border){
        this.iconBorder = iconBorder
    }

    fun SetIconPath(iconPath: String){
        this.iconPath = iconPath
    }


    fun setImage(image: String){
        var path = image
        if (!image.isEmpty()) {
            if (image.substring(0, 4) != "http") {
                path = File(iconPath).toURI().toURL().toString();
            }
            var Border = if (iconBorder == Border.CIRCLE) {
                Circle(defHeight / 2, defHeight / 2, defHeight / 2)
            } else {
                Rectangle(defHeight / 2, defHeight / 2, defHeight, defHeight)
            }
            Border.setFill(ImagePattern(Image(path)))
            content.children.add(Border)
        }
    }

    fun setImageMsg(title: String = "title", message: String = "message", appName: String = "appName", image: String, border: ConfigImageMsg.Border = ConfigImageMsg.Border.CIRCLE){
        configImageMsg.build(title, message, appName, image, border)
        content.children.add(configImageMsg.contentImageMsg)

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


    fun SetTitle(title: String) {
        var title = Label(titleNotify)
        title.font = Font(24.0)
        title.style = "-fx-font-weight: bold; -fx-text-fill:" + textColor

        content.children.add(title)
    }


    fun setMessage(msg: String){
        var message = Label(msg)
        message.font = Font(17.0)
        message.style = "-fx-text-fill:" + textColor

        content.children.add(message)
    }


    fun setTextField(){
        textField = TextField()
        textField?.minHeight = 30.0
        textField?.maxWidth = 250.0

        textField?.alignment = Pos.BASELINE_CENTER
        content.alignment = Pos.BASELINE_CENTER
        textField?.font = Font(15.0)

        content.children.add(textField)
    }

    fun setButtons(){
        var HboxButtons = HBox()

        buttonOk.style = "-fx-background-color:" + colorButton;  "-fx-text-fill:" + textColor;
        buttonOk.minHeight = 25.0
        buttonOk.minWidth = 100.0

        buttonCancel.style = "-fx-background-color:" + colorButton; "-fx-text-fill:" + textColor
        buttonCancel.minHeight = 25.0
        buttonCancel.minWidth = 100.0

        HboxButtons.children.addAll(buttonOk, buttonCancel)

        HboxButtons.alignment = Pos.BASELINE_CENTER
        HboxButtons.spacing = 35.0
        HboxButtons.padding = Insets(5.0)


        content.children.add(HboxButtons)

    }


    var screenRect = Screen.getPrimary().bounds
    var stage = Stage()

    fun start() {

        when (pos) {
            Position.LEFT_BOTTOM -> {
                stage.x = shift
                stage.y = screenRect.height - defHeight - shift
            }
            Position.LEFT_TOP -> {
                stage.x = shift
                stage.y = shift
            }
            Position.RIGHT_BOTTOM -> {
                stage.x = screenRect.width - defWidth - shift
                stage.y = screenRect.height - defHeight - shift
            }
            Position.RIGHT_TOP -> {
                stage.x = screenRect.width -defWidth - shift
                stage.y = shift
            }
        }

        val close = object: Task<Void>() {
            @Throws(InterruptedException::class)
            override fun call(): Void? {
                Thread.sleep(waitTime.toLong())
                //closeAnim()
                return null
            }
        }



       // stage.addEventFilter(MouseEvent.MOUSE_ENTERED_TARGET) {
         //   Thread(close).join()
        //}

        //stage.addEventFilter(MouseEvent.MOUSE_EXITED_TARGET) {
          //  Thread(close).start()
        //}

        buttonOk.setOnAction {
            println(textField?.text)
            closeAnim()
        }

        configInput.buttonCancel.setOnAction {
            closeAnim()
        }


        content.style = "-fx-background-color:" + bgColor

        content.setPadding(Insets(10.0, 0.0, 0.0, 0.0))

        var scene = Scene(content , defWidth, defHeight)
        scene.setFill(Color.TRANSPARENT)

        stage.scene = scene
        stage.initStyle(StageStyle.TRANSPARENT)

        stage.show()
        openAnim()

    }
    fun openAnim() {


        //val musicFile = "https://wav-library.net/sounds/icq/icq_aska_zvuk_mp3_skachat/176-1-0-5952.mp3"
        //val sound = Media(File(musicFile).toURI().toString())
        //val mediaPlayer = MediaPlayer(sound)
        //mediaPlayer.play()


        val ft = TranslateTransition(Duration.millis(1000.0), content)

        when (pos) {
            Position.LEFT_BOTTOM, Position.LEFT_TOP -> {
                ft.fromX = -defWidth
                ft.toX = 0.0
            }
            Position.RIGHT_TOP, Position.RIGHT_BOTTOM -> {
                ft.fromX = defWidth
                ft.toX = 0.0
            }
        }
        ft.play()
    }

    fun closeAnim() {
        val ft = TranslateTransition(Duration.millis(1000.0), content)

        when (pos) {
            Position.LEFT_BOTTOM, Position.LEFT_TOP -> {
                ft.fromX = 0.0
                ft.toX = -defWidth
            }
            Position.RIGHT_TOP, Position.RIGHT_BOTTOM -> {
                ft.fromX = 0.0
                ft.toX = defWidth
            }
        }
        ft.setOnFinished {
            exitProcess(0)
        }
        ft.play()
    }
}

