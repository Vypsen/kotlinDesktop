import javafx.geometry.Pos
import javafx.scene.control.TextField
import javafx.scene.text.Font

class Search{
    var search = TextField()

    fun build(): TextField {
        search.minHeight = 25.0
        search.maxHeight = 30.0
        search.maxWidth = 180.0
        search.font = Font(17.0)
        search.alignment = Pos.CENTER

        return search
    }
}