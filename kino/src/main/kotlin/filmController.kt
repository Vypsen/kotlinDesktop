import com.google.gson.Gson
import javafx.beans.binding.Bindings
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.text.Font
import javafx.scene.web.WebView
import javafx.stage.Screen
import javafx.stage.Stage
import javafx.beans.property.DoubleProperty.doubleProperty
import javafx.beans.property.SimpleDoubleProperty

import javafx.beans.property.DoubleProperty
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.stage.StageStyle
import java.io.IOException


private var film = DataFilms()
private var trailers = Trailers()
private var listPeople = ListPeople()
private var frame = FramesFilm()
class filmController {


    @FXML
    lateinit var content: HBox

    @FXML
    lateinit var nameRu: Label

    @FXML
    lateinit var nameEn: Label

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
    lateinit var scenes: ImageView

    @FXML
    lateinit var actor: VBox

    @FXML
    lateinit var genre: HBox

    @FXML
    lateinit var country: HBox

    @FXML
    lateinit var clickLeft: Button

    @FXML
    lateinit var clickRight: Button

    @FXML
    lateinit var info: VBox

    @FXML
    lateinit var actors: VBox

    @FXML
    lateinit var linkKino: Button

    val screen = Screen.getPrimary().bounds


    var linkTrailer = String()

    var countFrame = 0

    var countDir = 0
    var countProd = 0
    var countWrit = 0
    var countDis = 0
    var countAct = 0


    @FXML
    fun clickScenes(){
        val img = StackPane()
        val close = Button("Close")
        val cadr = ImageView(scenes.image)
        val sceneCadr = Scene(img, 450.0, 300.0)
        val stage = Stage()


        close.prefWidth = 50.0
        close.prefHeight = 30.0
        StackPane.setAlignment(close, Pos.TOP_RIGHT)

        cadr.fitHeight = 300.0
        cadr.fitWidth = 450.0


        img.children.addAll(cadr, close)

        stage.initStyle(StageStyle.TRANSPARENT)
        stage.scene = sceneCadr
        stage.show()
        close.setOnAction {
            stage.close()
        }

    }

    @FXML
    fun openTrailer() {


        if (trailers.trailers!!.isNotEmpty()) {
            linkTrailer = trailers.trailers!![0].url
            val trailer = WebView()

            trailer.engine.load(linkTrailer)

            val sceneTrailer = Scene(trailer, 700.0, 500.0)

            val stage = Stage()
            stage.scene = sceneTrailer
            stage.show()

        }
    }




    @FXML
    fun imgPref() {
        if (countFrame != 0) {
            countFrame--
            scenes.image = Image(frame.frames!![countFrame].image, 300.0, 180.0, false, true)
        }
    }

    @FXML
    fun imgNext() {
        if (countFrame != frame.frames!!.size - 1) {
            countFrame++
            scenes.image = Image(frame.frames!![countFrame].image, 300.0, 180.0, false, true)
        }
    }


    @FXML
    fun initialize() {



        info.style = "-fx-background-color: #282727;" +
                "-fx-effect: dropshadow(gaussian, black, 10 , 0, 0, 0);"

        actors.style = "-fx-background-color: #282727;" +
                "-fx-effect: dropshadow(gaussian, black, 10 , 0, 0, 0);"





        content.style = "-fx-background-color: transparent"
        nameRu.text = film.data.nameRu
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




        for (i in listPeople.people!!) {
            val label = Label(i.nameRu)
            label.font = Font(14.0)
            label.style = "-fx-text-fill: white;" + "-fx-underline: true;"

            label.onMouseEntered = EventHandler {
                label.style = "-fx-text-fill: orange;" + "-fx-underline: true;"
                label.cursor = Cursor.HAND
            }

            label.onMouseExited = EventHandler {
                label.style = "-fx-text-fill: white;" + "-fx-underline: true;"
            }

            label.onMouseClicked = EventHandler {
                manController().main(i.staffId)
            }

            when (i.professionText) {
                "Режиссеры" -> {
                    if (countDir != 2) {
                        director.children.add(label)
                        countDir++
                    }
                }
                "Продюсеры" -> {
                    if (countProd != 2) {
                        producer.children.add(label)
                        countProd++
                    }
                }
                "Сценаристы" -> {
                    if (countWrit != 2) {
                        writer.children.add(label)
                        countWrit++
                    }
                }
                "Художники" -> {
                    if (countDis != 2) {
                        design.children.add(label)
                        countDis++
                    }
                }
                "Актеры" -> {
                    label.font = Font(20.0)
                    if (countAct != 8) {
                        actor.children.add(label)
                        countAct++
                    }
                }
            }
        }

        for (i in film.data.countries!!) {
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



        if (frame.frames != null)
            scenes.image = Image(frame.frames!![0].image, 300.0, 180.0, false, true)
        else {
            clickLeft.isVisible = false
            clickRight.isVisible = false
        }
    }


    var uri = String()

    private fun takeJsonFilm(id: String): DataFilms {
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
        println(result)
        result = "{ people: $result }"
        return (g.fromJson(result, ListPeople::class.java))
    }

    private fun takeJsonTrailers(id: String): Trailers {
        uri = "/api/v2.1/films/$id/videos"
        val g = Gson()
        var result = Kino.getJson(uri)
        println(result)

        return (g.fromJson(result, Trailers::class.java))
    }

    private fun takeJsonFrames(id: String): FramesFilm {
        uri = "/api/v2.1/films/$id/frames"
        val g = Gson()
        val result = Kino.getJson(uri)
        println(result)


        return if (result != "")
            (g.fromJson(result, FramesFilm::class.java))
        else
            FramesFilm()
    }


    fun main(filmId: String) {


        film = takeJsonFilm(filmId)
        trailers = takeJsonTrailers(filmId)
        listPeople = takeJsonPeople(filmId)
        frame = takeJsonFrames(filmId)

        val root = FXMLLoader.load<ScrollPane>(javaClass.getResource("filmForm.fxml"))
        println(root)

        stackPane.children.removeAt(1)
        stack.add(root)
        stackPane.children.add(root)


    }
}


