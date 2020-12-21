import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.image.Image
import javafx.scene.layout.Border
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Rectangle
import javafx.stage.Screen


class searchResult{
    var list = VBox()
    var scroll = ScrollPane(list)

    fun createItem(poster: String, nameRu: String, nameEn: String, year: String): HBox {
        var item = HBox()

        var NameRu = Label(nameRu)

        var NameEn = Label(nameEn)
        var Year = Label(year)

        var image = Rectangle(80.0, 80.0, 40.0, 40.0)
        image.fill = ImagePattern(Image(poster))

        var info = VBox()

        info.children.addAll(NameRu,NameEn,Year)

        item.children.addAll(image, info)

        return item
    }


    fun build(Films: List<Films>?): ScrollPane {
        println("!!!")
        scroll.maxWidth = 400.0


        for(i in Films!!){
            list.children.add(createItem(i.posterUrlPreview, i.nameRu, i.nameEn, i.year))
            println(i.nameRu)
        }


        list.padding = Insets(40.0, 5.0, 0.0, 5.0)

        return scroll
    }
}