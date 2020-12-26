import com.google.gson.Gson
import javafx.concurrent.Task
import javafx.fxml.FXML
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Border
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Rectangle
import javafx.stage.Screen
import java.io.InputStream



class searchResult{
    var list = VBox()



    fun build(result: InfoFilms): VBox {

        var list = VBox()

        for(i in result.films!!){
            println(i.nameRu)

            var item = HBox()
            item.alignment = Pos.CENTER


            var NameRu = Label(i.nameRu)

            var NameEn = Label(i.nameEn)
            var Year = Label(i.year)


            var info = VBox()
            info.children.addAll(NameRu,NameEn,Year)
            item.children.add( info)

            list.children.add(item)
        }

        list.padding = Insets(0.0, 0.0, 0.0, 0.0)
        list.spacing = 20.0
        list.alignment = Pos.CENTER

        return list

    }
}


