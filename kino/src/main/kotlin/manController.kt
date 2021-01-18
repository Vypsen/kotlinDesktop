import com.google.gson.Gson
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Cursor
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Font

private var manInf = InfoMan()
class manController{
    @FXML
    lateinit var imgMan: ImageView
    @FXML
    lateinit var nameRu: Label
    @FXML
    lateinit var info: VBox
    @FXML
    lateinit var filmsList: VBox
    @FXML
    lateinit var nameEn: Label
    @FXML
    lateinit var growth: Label
    @FXML
    lateinit var profession: Label
    @FXML
    lateinit var birthday: Label
    @FXML
    lateinit var birthplace: Label
    @FXML
    lateinit var countFilms: Label
    @FXML
    lateinit var sex: Label
    @FXML
    lateinit var films: VBox
    @FXML
    lateinit var hboxDeath:HBox
    @FXML
    lateinit var hboxDeathplace: HBox
    @FXML
    lateinit var death: Label
    @FXML
    lateinit var deathplace: Label
    @FXML
    lateinit var content: VBox
    @FXML
    lateinit var contentInfo: HBox
    @FXML
    lateinit var facts: VBox



    var uri = String()

    var countFilm = 0

    @FXML
    fun initialize(){


        imgMan.image = Image(manInf.posterUrl, 300.0, 480.0, false, true)
        nameRu.text = manInf.nameRu
        nameEn.text = manInf.nameEn
        growth.text = manInf.growth
        profession.text = manInf.profession
        birthday.text = "${manInf.birthday}, ${(manInf.age)}"
        birthplace.text = manInf.birthplace
        countFilms.text = manInf.films!!.size.toString()
        println(manInf.death)

        if (manInf.death == null){
            hboxDeath.isVisible = false
            hboxDeathplace.isVisible = false
        }
        else{
            death.text = manInf.death
            deathplace.text = manInf.deathplace
        }

        if (manInf.sex == "MALE"){
            sex.text = "Мужской"
        }
        else
        {
            sex.text = "Женский"
        }


        for(i in manInf.films!!) {
            if(i.nameRu == "") {
                i.nameRu = i.nameEn
            }
            val label = Label(i.nameRu)
            println(i.nameRu)
            label.font = Font(20.0)
            label.style = "-fx-text-fill: white;"  + "-fx-underline: true;"

            label.onMouseEntered = EventHandler {
                label.style = "-fx-text-fill: orange;" + "-fx-underline: true;"
                label.cursor = Cursor.HAND
            }

            label.onMouseExited = EventHandler {
                label.style = "-fx-text-fill: white;" + "-fx-underline: true;"
            }

            label.onMouseClicked = EventHandler {
                filmController().main(i.filmId)
            }

            if(countFilm!=8) {
                films.children.add(label)
                countFilm++
            }
        }


        for (i in manInf.facts!!){
            val fact = Label(i)
            fact.style = " -fx-text-fill: white;" + "-fx-text-size: 35;"
            facts.children.add(fact)
        }





    }

    private fun takeJsonMan(id: String): InfoMan {
        uri = "/api/v1/staff/$id"
        val g = Gson()
        var result = Kino.getJson(uri)
        println(result)
        return (g.fromJson(result, InfoMan::class.java))
    }


    fun main(filmId: String) {

        manInf = takeJsonMan(filmId)

        val root = FXMLLoader.load<ScrollPane>(javaClass.getResource("manForm.fxml"))

        println(root)

        stackPane.children.removeAt(1)

        stack.add(root)
        stackPane.children.add(root)


    }

}