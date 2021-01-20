import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.layout.TilePane
import javafx.stage.FileChooser
import javax.swing.text.html.ImageView


class ControllerAddImage {



    @FXML
    fun addImage() {

        val fileChooser = FileChooser()
        fileChooser.extensionFilters.addAll(
            FileChooser.ExtensionFilter("Image", "*.png", "*.jpg")
        )

    }

}