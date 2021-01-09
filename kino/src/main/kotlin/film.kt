import com.google.gson.Gson
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundImage
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.web.WebView
import javafx.scene.layout.BackgroundSize

import javafx.scene.layout.BackgroundPosition

import javafx.scene.layout.BackgroundRepeat
import java.awt.Color
import java.awt.Color.black


class DataFilms{
    var data = Film()
}

class Film{

    var filmId = String()
    var nameRu = String()
    var nameEn = String()
    var filmLength = String()
    var webUrl = String()
    var posterUrl = String()
    var year = String()
    var slogan = String()
    var description = String()
    var ratingMpaa = String()
    var ratingAgeLimits = String()
    var premiereRu = String()
    var premiereWorld = String()
    var premiereDvd = String()
    var premiereBluRay = String()
    var countries: List<Countries>? = null
    var genres: List<Genres>? = null


}

class DataPeople{
    var people: List<People>? = null
}

class Trailers {
    var trailers: List<Trailer> ?= null
}

class Trailer{
    var url = String()
}


class People{
    var staffId = String()
    var nameRu = String()
    var professionText = String()
}

class Countries{
    var country = String()
}

class Genres{
    var genre = String()
}


var film = DataFilms()
var trailers = Trailers()
var persons = DataPeople()

class FormFilm(var filmId: String){


    private fun takeJsonFilm(id:String): DataFilms {
        val result = Kino.getObject(id.toInt())
        println(result)
        val g = Gson()

        return g.fromJson(result, DataFilms::class.java)
    }

    private fun takeJsonPeople(id: String): DataPeople {

        val g = Gson()
        var result = Kino.getPeople(id.toInt())
        result = "{ people: $result }"
        return (g.fromJson(result, DataPeople::class.java))
    }

    private fun takeJsonTrailers(id: String): Trailers {

        val g = Gson()
        var result = Kino.getTrailer(id.toInt())
        return (g.fromJson(result, Trailers::class.java))
    }


    fun main() {

        film = takeJsonFilm(filmId)
        trailers = takeJsonTrailers(filmId)
        persons = takeJsonPeople(filmId)

        val root = FXMLLoader.load<Parent>(javaClass.getResource("fForm.fxml"))



        window.style = "-fx-background-color: black"
        window.center = root


    }



}