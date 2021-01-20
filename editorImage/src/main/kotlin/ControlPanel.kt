import javafx.application.Application
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import javafx.stage.Stage







class ControlPanel : Application() {


    @FXML
    lateinit var control: AnchorPane


    @FXML
    fun addImage(){
        val filter = DraggableNode("addImageNode.fxml")
        Node.pool.add(filter)
        control.children.addAll(filter)
    }

    @FXML
    fun blackWhiteFilter(){
        val filter = DraggableNode("blackAndWhiteNode.fxml")
        Node.pool.add(filter)
        control.children.addAll(filter)
    }

    @FXML
    fun negativeFilter(){
        val filter = DraggableNode("negativeNode.fxml")
        Node.pool.add(filter)
        control.children.addAll(filter)
    }
    @FXML
    fun histogram(){
        val filter = DraggableNode("histogramNode.fxml")
        Node.pool.add(filter)
        control.children.addAll(filter)
    }

    @FXML
    fun brightnessSaturationFilter(){
        val filter = DraggableNode("saturationNode.fxml")
        Node.pool.add(filter)
        control.children.addAll(filter)
    }

    @FXML
    fun save(){

    }


    override fun start(primaryStage: Stage) {

        val root = FXMLLoader.load<Parent>(javaClass.getResource("controlPanel.fxml"))
        val scene = Scene(root, 1000.0, 400.0)
        primaryStage.scene = scene


        primaryStage.show()


    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(ControlPanel::class.java)
        }
    }
}