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
import javafx.scene.control.ProgressBar
import javafx.concurrent.WorkerStateEvent
import javafx.event.EventHandler
import javafx.scene.layout.FlowPane
import javafx.stage.FileChooser
import project
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf
import kotlin.Function as Function


class Notify {



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



    class builder {





        private var content = VBox()
        private var sumHeight = 0.0

        private var contentImageMsg = HBox()

        private var pos = Position.LEFT_BOTTOM
        private var textColor = "#FFFFFF"
        private var bgColor = "#222222"
        private var msg = "MESSAGE"
        private var bgOpacity = 0.9
        private var waitTime = 9000
        private var defWidth = 300.0
        private var defHeight = 160.0
        private var shift = 30.0


        private var list: ObservableList<String?>? = FXCollections.observableArrayList()
        private var comboBox = ComboBox(list)

        //элемент ввода
        internal var textField: TextField? = null


        private var buttonOk = Button("Ok")
        private var buttonCancel = Button("Cancel")

        private var iconBorder = Border.SQUARE
        private var iconPath = ""
        private var titleNotify = "Title"

        private val musicFile = "src\\main\\resources\\z_uk-aska-z_uk.mp3"
        private val sound = Media(File(musicFile).toURI().toString())
        private val mediaPlayer = MediaPlayer(sound)

        private var HboxButtons = HBox()

        private var fileChooser = FileChooser()
        //private var selectedFiles: List<File>? = null



        //форма изображения
        fun setBorder(iconBorder: Border) {
            this.iconBorder = iconBorder
        }


        //расположение на экране
        fun SetPos(pos: Position) {
            this.pos = pos
        }

        fun SetTextColor(textColor: String) {
            this.textColor = textColor
        }

        //цвет окна
        fun SetBgColor(bgColor: String) {
            this.bgColor = bgColor
        }

        //прозрачность
        fun SetBgOpacity(bgOpacity: Double) {
            this.bgOpacity = bgOpacity
        }

        //время ожидания
        fun SetWaitTime(waitTime: Int) {
            this.waitTime = waitTime
        }

        //ширина
        fun setWidth(defWidth: Double) {
            this.defWidth = defWidth
        }

        //высота
        fun setHeight(defHeight: Double) {
            this.defHeight = defHeight
        }

        //отступ
        fun SetShift(shift: Double) {
            this.shift = shift
        }


        //вставить изображение
        fun addImage(image: String) {
            var path = image
            if (!image.isEmpty()) {
                if (image.substring(0, 4) != "http") {
                    path = File(iconPath).toURI().toURL().toString();
                }
                var Border = if (iconBorder == Border.CIRCLE) {
                    Circle(defHeight / 4, defHeight / 4, defHeight / 4)
                } else {
                    Rectangle(defHeight, defHeight / 2)
                }
                Border.setFill(ImagePattern(Image(path)))
                content.children.add(Border)

                sumHeight += defHeight / 2

            }
        }

        //установить заголовок
        fun addTitle(title: String) {
            var title = Label(titleNotify)
            title.font = Font(24.0)
            title.style = "-fx-font-weight: bold; -fx-text-fill:" + textColor

            content.children.add(title)
            sumHeight += 24
        }


        //вставить сообщение
        fun addMessage(msg: String) {
            var message = Label(msg)
            message.font = Font(17.0)
            message.style = "-fx-text-fill:" + textColor

            content.children.add(message)
            sumHeight += 17
        }


        //вставить поле для ввода
        fun addTextField() {
            textField = TextField()

            textField?.minHeight = 30.0
            textField?.maxWidth = 250.0

            textField?.font = Font(15.0)
            textField?.alignment = Pos.BASELINE_CENTER


            content.children.add(textField)
            sumHeight += 30
        }

        //вставить кнопки
        fun addButtons() {

            var colorButton = "#999999"

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
        fun addComboBox() {
            content.children.add(comboBox)
            sumHeight += 30
        }

        fun addPressed(flag: Boolean) {
            if (flag)
                stage.addEventFilter(MouseEvent.MOUSE_PRESSED) {
                    closeAnim()
                }

        }

        fun addImageMsg(title: String, message: String, appName: String, image: String, border: Border) {

            contentImageMsg.setPadding(Insets(5.0, 5.0, 0.0, 5.0))
            contentImageMsg.spacing = 30.0

            var path = image
            if (!image.isEmpty()) {
                if (image.substring(0, 4) != "http") {
                    path = File(image).toURI().toURL().toString();
                }
                var image = if (border == Border.CIRCLE) {
                    Circle(defHeight / 3, defHeight / 3, defHeight / 3)
                } else {
                    Rectangle(defHeight / 4, defHeight / 4, defHeight / 2, defHeight / 2)
                }
                image.setFill(ImagePattern(Image(path)))

                contentImageMsg.children.add(image)
            }


            var msgLayout = VBox()

            var Title = Label(title)
            Title.font = Font(20.0)
            Title.style = "-fx-font-weight: bold; -fx-text-fill:" + textColor

            var Message = Label(message)
            Message.font = Font(17.0)
            Message.style = "-fx-text-fill: #AAAAAA"

            var AppName = Label(appName)
            AppName.font = Font(10.0)
            AppName.style = "-fx-text-fill: #AAAAAA"


            msgLayout.children.addAll(Title, Message, AppName)
            contentImageMsg.children.add(msgLayout)
            contentImageMsg.alignment = Pos.TOP_LEFT

            content.children.add(contentImageMsg)
            sumHeight += 80


        }


        fun addProgressBar() {


            var progressBar = ProgressBar(0.0)

            progressBar.prefWidth = 180.0


            val startButton = Button("Start")
            val cancelButton = Button("Cancel")


            val statusLabel = Label()

            statusLabel.minWidth = 250.0
            statusLabel.textFill = Color.WHITE

            startButton.setOnAction {
                startButton.isDisable = true;
                progressBar.progress = 0.0;

                var copyTask = CopyTask();

                progressBar.progressProperty().bind(copyTask.progressProperty())

                statusLabel.textProperty().bind(copyTask.messageProperty())

                copyTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED)
                {
                    val copied = copyTask.value
                    statusLabel.textProperty().unbind()
                    statusLabel.text = "Copied: " + copied.size
                }



                Thread(copyTask).start()
            }

            cancelButton.setOnAction {
                closeAnim()
            }

            val buttons = HBox()

            buttons.children.addAll(startButton, cancelButton)

            buttons.spacing = 20.0


            val root = FlowPane()
            root.children.addAll(progressBar, statusLabel, buttons)

            content.children.add(root)
            sumHeight += 80
        }



            private var screenRect = Screen.getPrimary().bounds
            private var stage = Stage()

            fun build() {

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

                val close = object : Task<Void>() {
                    @Throws(InterruptedException::class)
                    override fun call(): Void? {
                        Thread.sleep(waitTime.toLong())
                        closeAnim()
                        return null
                    }
                }
                Thread(close).start()




                //эвенты на кнопки
                buttonOk.addEventFilter(MouseEvent.MOUSE_PRESSED) {
                    if (textField != null) {
                        println(textField?.text)
                    } else {
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

                var scene = Scene(content, defWidth, sumHeight + content.spacing * 2 + 10)
                scene.fill = Color.TRANSPARENT

                stage.scene = scene
                stage.initStyle(StageStyle.TRANSPARENT)

                stage.show()
                openAnim()

            }

             private fun openAnim() {


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
}




