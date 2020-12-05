package library

import javafx.animation.ScaleTransition
import javafx.animation.TranslateTransition
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.concurrent.Task
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
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
import javafx.scene.paint.Paint
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.stage.Screen
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration
import java.io.File
import kotlin.system.exitProcess


var sumHeight = 0.0
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
    var waitTime = 9000
    var defWidth = 300.0
    var defHeight = 160.0
    var shift = 30.0


    var list: ObservableList<String?>? = FXCollections.observableArrayList()
    var comboBox = ComboBox(list)

    //элемент ввода
    var textField: TextField? = null

    var buttonOk = Button("Ok")
    var buttonCancel = Button("Cancel")
    var colorButton = "#999999"

    var iconBorder = Border.SQUARE
    var iconPath = ""
    var titleNotify = "Title"

    val musicFile = "src\\main\\resources\\z_uk-aska-z_uk.mp3"
    val sound = Media(File(musicFile).toURI().toString())
    val mediaPlayer = MediaPlayer(sound)

    //форма изображения
    fun setBorder(iconBorder: Border){
        this.iconBorder = iconBorder
    }


    //расположение на экране
    fun SetPos(pos: Position) {
        this.pos = pos
    }

    fun SetTextColor(textColor: String){
        this.textColor = textColor
    }

    //цвет окна
    fun SetBgColor(bgColor: String){
        this.bgColor = bgColor
    }

    //прозрачность
    fun SetBgOpacity(bgOpacity: Double){
        this.bgOpacity = bgOpacity
    }

    //время ожидания
    fun SetWaitTime(waitTime: Int){
        this.waitTime = waitTime
    }

    //ширина
    fun setWidth(defWidth: Double){
        this.defWidth = defWidth
    }

    //высота
    fun setHeight(defHeight: Double){
        this.defHeight = defHeight
    }

    //отступ
    fun SetShift(shift: Double){
        this.shift = shift
    }



    //вставить изображение
    fun setImage(image: String){
        var path = image
        if (!image.isEmpty()) {
            if (image.substring(0, 4) != "http") {
                path = File(iconPath).toURI().toURL().toString();
            }
            var Border = if (iconBorder == Border.CIRCLE) {
                Circle(defHeight / 4, defHeight / 4, defHeight / 4)
            } else {
                Rectangle(defHeight , defHeight / 2)
            }
            Border.setFill(ImagePattern(Image(path)))
            content.children.add(Border)

            sumHeight += defHeight/2

        }
    }

    //вставить контент из другого файла
    fun setImageMsg(title: String = "title", message: String = "message", appName: String = "appName", image: String, border: ConfigImageMsg.Border = ConfigImageMsg.Border.CIRCLE){
        configImageMsg.build(title, message, appName, image, border)
        content.children.add(configImageMsg.contentImageMsg)
        sumHeight += 80

    }

    //установить заголовок
    fun SetTitle(title: String) {
        var title = Label(titleNotify)
        title.font = Font(24.0)
        title.style = "-fx-font-weight: bold; -fx-text-fill:" + textColor

        content.children.add(title)
        sumHeight += 24
    }


    //вставить сообщение
    fun setMessage(msg: String){
        var message = Label(msg)
        message.font = Font(17.0)
        message.style = "-fx-text-fill:" + textColor

        content.children.add(message)
        sumHeight += 17
    }


    //вставить поле для ввода
    fun setTextField(){
        textField = TextField()

        textField?.minHeight = 30.0
        textField?.maxWidth = 250.0

        textField?.font = Font(15.0)
        textField?.alignment = Pos.BASELINE_CENTER


        content.children.add(textField)
        sumHeight += 30
    }

    //вставить кнопки
    fun setButtons(){
        var HboxButtons = HBox()

        buttonOk.textFill = Paint.valueOf("#FFFFFF")
        buttonOk.style = "-fx-background-color: " + colorButton
        buttonOk.minHeight = 25.0
        buttonOk.minWidth = 100.0

        buttonCancel.textFill = Paint.valueOf("#FFFFFF")
        buttonCancel.style = "-fx-background-color :" + colorButton
        buttonCancel.minHeight = 25.0
        buttonCancel.minWidth = 100.0

        HboxButtons.children.addAll(buttonOk, buttonCancel)

        HboxButtons.alignment = Pos.CENTER
        HboxButtons.spacing = 35.0



        content.children.add(HboxButtons)
        sumHeight += 30

    }

    //выпадающий список
    fun setComboBox(){
        content.children.add(comboBox)
        sumHeight += 30
    }

    fun setPressed(flag: Boolean){
        if (flag)
            stage.addEventFilter(MouseEvent.MOUSE_PRESSED){
                closeAnim()
            }

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
                stage.x = screenRect.width - defWidth - shift
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
        Thread(close).start()







        //эвенты на кнопки
        buttonOk.addEventFilter(MouseEvent.MOUSE_PRESSED){
            if(textField != null){
                println(textField?.text)
            }
            else{
                println(comboBox.selectionModel.selectedItem)
            }
            closeAnim()
        }

        buttonCancel.setOnAction {
            closeAnim()
        }


        content.padding = Insets(15.0)
        content.style = "-fx-background-color:" + bgColor
        content.opacity = bgOpacity
        content.alignment = Pos.CENTER
        content.spacing = 10.0

        var scene = Scene(content, defWidth, sumHeight + content.spacing*2 + 10)
        scene.fill = Color.TRANSPARENT

        stage.scene = scene
        stage.initStyle(StageStyle.TRANSPARENT)

        stage.show()
        openAnim()
        
    }

    fun openAnim() {


        mediaPlayer.play()

        val ft = TranslateTransition(Duration.millis(500.0), content)

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
        val scale = ScaleTransition(Duration.millis(500.0), content)
        scale.fromX = 0.5
        scale.fromY = 0.5
        scale.toX = 1.0
        scale.toY = 1.0

        scale.play()
        ft.play()
    }

    fun closeAnim() {

        val ft = TranslateTransition(Duration.millis(100.0), content)

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
            stage.close()
        }

        val scale = ScaleTransition(Duration.millis(50.0), content)
        scale.fromX = 1.0
        scale.fromY = 1.0
        scale.toX = 0.5
        scale.toY = 0.5


        scale.play()
        ft.play()
    }
}




