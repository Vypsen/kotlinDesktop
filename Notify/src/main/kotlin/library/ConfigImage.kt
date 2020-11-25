package library

import javafx.geometry.Insets
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import java.io.File


enum class Border {
    SQUARE,
    CIRCLE
}

class ConfigImage{

    var iconBorder = Border.CIRCLE
    var iconPath = ""
    var title = "Title"
    var msg = "MESSAGE"
    var appName = "app_name"

    fun setBorder(iconBorder: Border){
        this.iconBorder = iconBorder
    }

    fun SetIconPath(iconPath: String){
        this.iconPath = iconPath
    }

    fun SetTitle(title: String) {
        this.title = title
    }

    fun setMessage(msg: String){
        this.msg = msg
    }

    fun SetAppName(appName: String){
        this.appName = appName
    }

}


var contentImage = HBox()

class StartConfigImage{

    fun build(config: Notify.Config) {
        contentImage.setPadding(Insets(5.0, 5.0, 5.0, 5.0))
        contentImage.spacing = 10.0
        contentImage.style = "-fx-background-color:" + config.bgColor

        var path = config.conImage.iconPath
        if (!config.conImage.iconPath.isEmpty()) {
            if (config.conImage.iconPath.substring(0, 4) != "http") {
                path = File(config.conImage.iconPath).toURI().toURL().toString();
            }
            var icoBorder = if (config.conImage.iconBorder == Border.CIRCLE) {
                Circle(config.defHeight / 2, config.defHeight / 2, config.defHeight / 2)
            } else {
                Rectangle(config.defHeight / 2, config.defHeight / 2, config.defHeight, config.defHeight)
            }
            icoBorder.setFill(ImagePattern(Image(path)))

            contentImage.children.add(icoBorder)
        }


        var msgLayout = VBox()

        var title = Label(config.conImage.title)
        title.font = Font(24.0)
        title.style = "-fx-font-weight: bold; -fx-text-fill:" + config.textColor

        var message = Label(config.conImage.msg)
        message.font = Font(20.0)
        message.style = "-fx-text-fill:" + config.textColor

        var app = Label(config.conImage.appName)
        app.font = Font(16.0)
        app.style = "-fx-text-fill:" + config.textColor

        msgLayout.children.addAll(title, message, app)
        contentImage.children.add(msgLayout)
    }
}