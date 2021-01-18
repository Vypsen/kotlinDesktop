import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.layout.AnchorPane

class buttonBack {
    var button = Button("<")

    fun build(): Button {
        button.prefWidth = 70.0
        button.prefHeight = 30.0
        AnchorPane.setLeftAnchor(button, 10.0)
        AnchorPane.setTopAnchor(button, 10.0)
        AnchorPane.setBottomAnchor(button, 10.0)

        button.setOnAction {
            if(stack.size > 1) {

                stackPane.children.removeAt(1)
                stack.removeAt(stack.size - 1)
                stackPane.children.add(stack[stack.size - 1])

//                if(stack.size == 1){
//                    window.style = ("-fx-background-image: url(" + "ki.jpg" +")")
//
//                }
            }
        }

        return button
    }
}