package library

import com.jfoenix.converters.ButtonTypeConverter
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Font


var contentInput = VBox()

class ConfigInput{

    var buttonOk = Button("Ok")
    var buttonCancel = Button("Cancel")
    var colorButton = "#FFFFFF"
}

var configInput = ConfigInput()
class StartConfigInput{
    fun build(config: Notify.Config){
        contentInput.style = "-fx-background-color:" + config.bgColor


        contentInput.setPadding(Insets(15.0, 5.0, 5.0, 5.0))
        contentInput.spacing = 25.0

        var message = Label(config.msg)
        message.font = Font(20.0)
        message.style = "-fx-text-fill:" + config.textColor

        val textField = TextField()
        textField.alignment = Pos.BASELINE_CENTER

        var HboxButtons = HBox()
        configInput.buttonOk.style = "-fx-background-color:" + configInput.colorButton;  "-fx-text-fill:" + config.textColor
        configInput.buttonCancel.style = "-fx-background-color:" + configInput.colorButton; "-fx-text-fill:" + config.textColor
        configInput
        HboxButtons.children.addAll(configInput.buttonOk, configInput.buttonCancel)
        HboxButtons.alignment = Pos.BASELINE_CENTER
        HboxButtons.spacing = 40.0

        configInput.buttonOk.setOnAction {
            println(textField.text)
            Notify().cloaseAnim(contentInput)
        }

        configInput.buttonCancel.setOnAction {
            Notify().cloaseAnim(contentInput)
        }


        contentInput.children.addAll(message, textField, HboxButtons)


    }
}
