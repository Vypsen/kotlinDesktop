import com.google.gson.Gson
import javafx.stage.Stage
import org.apache.http.NameValuePair
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.utils.URIBuilder
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import java.util.ArrayList


class InfoFilms{
    var keyword = String()
    var pagesCount = String()
    var films: List<Films>? = null
}

class Films{
    var filmId = String()
    var nameRu = String()
    var nameEn = String()
    var type = String()
    var year = String()
    var description = String()
    var countries: List<Countries>? = null
    var genres: List<Genres>? = null
    var rating = String()
    var ratingVoteCount = String()
    var posterUrl = String()
    var posterUrlPreview = String()
}

class Countries{
    var country = String()
}

class Genres{
    var genre = String()
}

object Kino {

    private val baseURL = "https://kinopoiskapiunofficial.tech"
    private val filmById = "/api/v2.1/films/search-by-keyword?keyword"


    fun main(keyWord: String): InfoFilms {


        val paratmers = ArrayList<NameValuePair>()

        val headerParams = ArrayList<NameValuePair>()
        headerParams.add(BasicNameValuePair("accept", "application/json"))
        headerParams.add(BasicNameValuePair("X-API-KEY", "f1d94351-2911-4485-b037-97817098724e"))

        val result = makeAPICall("$baseURL$filmById=$keyWord", paratmers, headerParams)

        val g = Gson()
        var info = g.fromJson(result, InfoFilms::class.java)

        return info
    }

    fun makeAPICall(uri: String, parameters: List<NameValuePair>, headerParams: List<NameValuePair>): String {
        var response_content = ""

        val query = URIBuilder(uri)
        query.addParameters(parameters)

        val client = HttpClients.createDefault()
        val request = HttpGet(query.build())
        for (p in headerParams) {
            request.addHeader(p.name, p.value)
        }

        val response = client.execute(request)


            System.out.println(response.getStatusLine())
            val entity = response.getEntity()
            response_content = EntityUtils.toString(entity)
            EntityUtils.consume(entity)

        return response_content
    }
}


