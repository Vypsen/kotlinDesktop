import javafx.geometry.Pos
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.scene.text.Font

class Search{
    var search = TextField()

    fun build(): TextField {
        search.minHeight = 25.0
        search.maxHeight = 30.0
        search.maxWidth = 180.0
        search.font = Font(17.0)
        search.alignment = Pos.CENTER

        AnchorPane.setTopAnchor(search, 10.0)
        AnchorPane.setRightAnchor(search, 10.0)
        AnchorPane.setBottomAnchor(search, 10.0)


        return search
    }
}