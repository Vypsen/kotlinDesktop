package library

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import java.io.File

var configImageMsg = ConfigImageMsg()
class ConfigImageMsg {

    enum class Border {
        SQUARE,
        CIRCLE
    }



    var contentImageMsg = HBox()

    fun build(title: String, message: String, appName: String, image: String, border: Border) {

        contentImageMsg.setPadding(Insets(3.0, 0.0, 0.0, 0.0))
        contentImageMsg.spacing = 10.0

        var path = image
        if (!image.isEmpty()) {
            if (image.substring(0, 4) != "http") {
                path = File(image).toURI().toURL().toString();
            }
            var image = if (border == Border.CIRCLE) {
                Circle(notify.defHeight / 6, notify.defHeight / 6, notify.defHeight / 6)
            } else {
                Rectangle(notify.defHeight / 8, notify.defHeight / 8, notify.defHeight / 4, notify.defHeight / 4)
            }
            image.setFill(ImagePattern(Image(path)))

            contentImageMsg.children.add(image)
        }


        var msgLayout = VBox()

        var Title = Label(title)
        Title.font = Font(20.0)
        Title.style = "-fx-font-weight: bold; -fx-text-fill:" + notify.textColor

        var Message = Label(message)
        Message.font = Font(17.0)
        Message.style = "-fx-text-fill: #AAAAAA"

        var AppName = Label(appName)
        AppName.font = Font(10.0)
        AppName.style = "-fx-text-fill: #AAAAAA"


        msgLayout.children.addAll(Title, Message, AppName)
        contentImageMsg.children.add(msgLayout)
        contentImageMsg.alignment = Pos.TOP_LEFT

    }
}