import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox

class manController{
    @FXML
    lateinit var imgMan: ImageView
    @FXML
    lateinit var nameRu: Label
    @FXML
    lateinit var nameEn: Label
    @FXML
    lateinit var growth: Label
    @FXML
    lateinit var profession: HBox
    @FXML
    lateinit var birthday: Label
    @FXML
    lateinit var birthplace: Label
    @FXML
    lateinit var countFilms: Label
    @FXML
    lateinit var films: VBox


    @FXML
    fun initialize(){

    }

}