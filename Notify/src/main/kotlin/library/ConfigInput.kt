package library

import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.layout.VBox



var contentInput = VBox()

class ConfigInput{

    var buttonOk = Button("Ok")
    var buttonCancel = Button("Cancel")



}

class StartConfigInput{
    fun build (config: Notify.Config){
        contentInput.setPadding(Insets(5.0, 5.0, 5.0, 5.0))
        //contentInput.spacing = 10.0










    }
}
