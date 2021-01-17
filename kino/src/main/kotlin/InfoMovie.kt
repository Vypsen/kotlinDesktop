import com.google.gson.Gson
import org.apache.http.NameValuePair
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.utils.URIBuilder
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import java.util.ArrayList




object Kino {

        private val baseURL = "https://kinopoiskapiunofficial.tech"


    fun getJsonListSearch(keyWord: String, page: Int): String {

        val api = "/api/v2.1/films/search-by-keyword?keyword"

        val paratmers = ArrayList<NameValuePair>()

        val headerParams = ArrayList<NameValuePair>()
        headerParams.add(BasicNameValuePair("accept", "application/json"))
        headerParams.add(BasicNameValuePair("X-API-KEY", "c5be2f7c-bcb2-449b-99af-5fe44a31e868"))


        val result = makeAPICall("$baseURL$api=$keyWord&page=$page", paratmers, headerParams)

        println(result)

        val g = Gson()
        val infoFilms = g.fromJson(result, InfoFilms::class.java)

        return result
    }


    fun getJson(uri: String): String {

        val paratmers = ArrayList<NameValuePair>()

        val headerParams = ArrayList<NameValuePair>()
        headerParams.add(BasicNameValuePair("accept", "application/json"))
        headerParams.add(BasicNameValuePair("X-API-KEY", "c5be2f7c-bcb2-449b-99af-5fe44a31e868"))


        val result = makeAPICall("$baseURL$uri", paratmers, headerParams)


        return result

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


