import javafx.geometry.Pos
import javafx.scene.control.TextField
import javafx.scene.text.Font

class Search{
    var search = TextField()

    fun build(): TextField {
        search.minHeight = 30.0
        search.maxHeight = 60.0
        search.maxWidth = 300.0
        search.font = Font(32.0)
        search.alignment = Pos.CENTER

        return search
    }
}