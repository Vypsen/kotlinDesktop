import com.google.gson.Gson
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Cursor
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.text.Font
import javafx.scene.web.WebView


class filmController {

    @FXML
    lateinit var content: HBox

    @FXML
    lateinit var nameRu: Label

    @FXML
    lateinit var nameEn: Label

    @FXML
    lateinit var age: Label

    @FXML
    lateinit var year: Label

    @FXML
    lateinit var slogan: Label

    @FXML
    lateinit var writer: HBox

    @FXML
    lateinit var director: HBox

    @FXML
    lateinit var producer: HBox

    @FXML
    lateinit var design: HBox

    @FXML
    lateinit var premiereRu: Label

    @FXML
    lateinit var premiereWorld: Label

    @FXML
    lateinit var premiereDvd: Label

    @FXML
    lateinit var premiereBluRay: Label

    @FXML
    lateinit var ratingMpaa: Label

    @FXML
    lateinit var filmLength: Label

    @FXML
    lateinit var poster: ImageView

    @FXML
    lateinit var trailer: WebView

    @FXML
    lateinit var actor: VBox

    @FXML
    lateinit var genre: HBox

    @FXML
    lateinit var country: HBox

    var film = DataFilms()
    var trailers = Trailers()
    var listPeople = ListPeople()

    var linkTrailer = String()

    var countDir = 0
    var countProd = 0
    var countWrit = 0
    var countDis = 0
    var countAct = 0

    var uri = String()


    private fun takeJsonFilm(id:String): DataFilms {
        uri = "/api/v2.1/films/$id"
        val result = Kino.getJson(uri)
        println(result)
        val g = Gson()

        return g.fromJson(result, DataFilms::class.java)
    }

    private fun takeJsonPeople(id: String): ListPeople {
        uri = "/api/v1/staff?filmId=$id"
        val g = Gson()
        var result = Kino.getJson(uri)
        result = "{ people: $result }"
        return (g.fromJson(result, ListPeople::class.java))
    }

    private fun takeJsonTrailers(id: String): Trailers {
        uri = "/api/v2.1/films/$id/videos"
        val g = Gson()
        var result = Kino.getJson(uri)
        return (g.fromJson(result, Trailers::class.java))
    }

    @FXML
    fun initialize(){



        nameRu.text = film.data.nameRu
        println(nameRu.text)
        println(11)
        nameEn.text = film.data.nameEn
        year.text = film.data.year
        slogan.text = film.data.slogan
        premiereRu.text = film.data.premiereRu
        premiereWorld.text = film.data.premiereWorld
        premiereDvd.text = film.data.premiereDvd
        premiereBluRay.text = film.data.premiereBluRay
        ratingMpaa.text = film.data.ratingMpaa
        filmLength.text = film.data.filmLength


        poster.image = Image(film.data.posterUrl, 300.0, 480.0, false, true)

        linkTrailer = trailers.trailers!![0].url

        trailer.engine.load(linkTrailer)
        trailer.setPrefSize(300.0, 150.0)





        for(i in listPeople.people!!){
            val label = Label(i.nameRu)
            label.font = Font(14.0)
            label.style = "-fx-text-fill: white;" + "-fx-font-style: Italic;" + "-fx-underline: true;"

            label.onMouseEntered = EventHandler{
                label.style = "-fx-text-fill: orange;" + "-fx-underline: true;" + "-fx-font-style: Italic;"
                label.cursor = Cursor.HAND
            }

            label.onMouseExited = EventHandler{
                label.style = "-fx-text-fill: white;" + "-fx-underline: true;" + "-fx-font-style: Italic;"
            }

            when (i.professionText) {
                "Режиссеры" -> {
                        if (countDir!=2){
                        director.children.add(label)
                        countDir++
                    }
                }
                "Продюсеры" -> {
                    if (countProd!=2) {
                        producer.children.add(label)
                        countProd++
                    }
                }
                "Сценаристы" -> {
                    if(countWrit!=2) {
                        writer.children.add(label)
                        countWrit++
                    }
                }
                "Художники" -> {
                    if(countDis!=2) {
                        design.children.add(label)
                        countDis++
                    }
                }
                "Актеры"-> {
                    label.font = Font(20.0)
                    if(countAct!=8) {
                        actor.children.add(label)
                        countAct++
                    }
                }
            }
        }

        for (i in film.data.countries!!){
            val label = Label(i.country)
            label.font = Font(14.0)
            label.style = "-fx-text-fill: white;"
            country.children.add(label)
        }

        for (i in film.data.genres!!) {
            val label = Label(i.genre)
            label.font = Font(14.0)
            label.style = "-fx-text-fill: white;"
            genre.children.add(label)
        }
    }

    fun main(filmId: String) {

        film = takeJsonFilm(filmId)
        trailers = takeJsonTrailers(filmId)
        listPeople = takeJsonPeople(filmId)

        val root = FXMLLoader.load<Parent>(javaClass.getResource("fForm.fxml"))
        println(root)

        window.style = "-fx-background-color: black"
        window.center = root


    }

}