import javafx.beans.binding.Bindings
import javafx.beans.binding.When
import javafx.beans.property.SimpleDoubleProperty
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.geometry.Point2D
import javafx.scene.control.Label
import javafx.scene.input.*
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.TilePane
import javafx.scene.shape.CubicCurve
import javafx.stage.FileChooser
import java.io.File
import java.io.IOException
import java.util.*



class NodeLink : AnchorPane() {

    @FXML
    var node_link: CubicCurve? = null

    val offsetX = SimpleDoubleProperty()
    val offsetY = SimpleDoubleProperty()
    val offsetDirX1 = SimpleDoubleProperty()
    val offsetDirX2 = SimpleDoubleProperty()
    val offsetDirY1 = SimpleDoubleProperty()
    val offsetDirY2 = SimpleDoubleProperty()

    @FXML
    private fun initialize() {
        offsetX.set(100.0)
        offsetY.set(50.0)

        offsetDirX1.bind(
            When(node_link!!.startXProperty().greaterThan(node_link!!.endXProperty())).then(-1.0).otherwise(1.0))

        offsetDirX2.bind(
            When(node_link!!.startXProperty().greaterThan(node_link!!.endXProperty())).then(1.0).otherwise(-1.0))

        node_link!!.controlX1Property().bind(Bindings.add(node_link!!.startXProperty(), offsetX.multiply(offsetDirX1)))
        node_link!!.controlX2Property().bind(Bindings.add(node_link!!.endXProperty(), offsetX.multiply(offsetDirX2)))
        node_link!!.controlY1Property().bind(Bindings.add(node_link!!.startYProperty(), offsetY.multiply(offsetDirY1)))
        node_link!!.controlY2Property().bind(Bindings.add(node_link!!.endYProperty(), offsetY.multiply(offsetDirY2)))
    }

    fun setStart(point: Point2D) {
        println(node_link!!.startX)
        println(point.x)

        node_link!!.startX = point.x
        node_link!!.startY = point.y
    }

    fun setEnd(point: Point2D) {
        node_link!!.endX = point.x
        node_link!!.endY = point.y
    }

    fun bindStartEnd(source1: DraggableNode, source2: DraggableNode) {
        node_link!!.startXProperty().bind(Bindings.add(source1.layoutXProperty(), source1.width/2.0))
        node_link!!.startYProperty().bind(Bindings.add(source1.layoutYProperty(), source1.height/2.0))
        node_link!!.endXProperty().bind(Bindings.add(source2.layoutXProperty(), source2.width/2.0))
        node_link!!.endYProperty().bind(Bindings.add(source2.layoutYProperty(), source2.height/2.0))
    }

    init {
        val fxmlLoader = FXMLLoader(
            javaClass.getResource("link.fxml")
        )
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        try {
            fxmlLoader.load<Any>()
        } catch (exception: IOException) {
            throw RuntimeException(exception)
        }
        id = UUID.randomUUID().toString()
    }
}



















var stateAddNode = DataFormat("nodeAdd")
var stateAddLink = DataFormat("linkAdd")
class DraggableNode(link: String) : HBox() {


    var offset = Point2D(0.0, 0.0)




    @FXML
    var title: Label? = null

    var link = NodeLink()



    @FXML
    lateinit var rightLink: TilePane

    @FXML
    var leftLink: TilePane? = null

    lateinit var contextDragOver: EventHandler<DragEvent>
    lateinit var contextDragDropped: EventHandler<DragEvent>
    var superParent: AnchorPane? = null

    lateinit var linkDragDetected: EventHandler<MouseEvent>
    lateinit var linkDeleteDragDetected: EventHandler<MouseEvent>
    lateinit var linkDragDropped: EventHandler<DragEvent>
    lateinit var contextLinkDragOver: EventHandler<DragEvent>
    lateinit var contextLinkDagDropped: EventHandler<DragEvent>

    var nextActionNode: DraggableNode? = null
    var prevActionNode: DraggableNode? = null


    @FXML
    private fun initialize() {
        nodeHandlers()
        linkHandlers()

        rightLink.onDragDetected = linkDragDetected
        leftLink?.onDragDropped = linkDragDropped
        leftLink?.onDragDetected = linkDeleteDragDetected

        parentProperty().addListener{ o, old, new -> superParent = parent as AnchorPane?}


    }


    init{

        val fxmlLoader = FXMLLoader(javaClass.getResource(link))

        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        fxmlLoader.load<Any>()



    }


    @FXML
    fun addImage(){

        println(1)
        val fileChooser = FileChooser()
        var imagePath = String()
        fileChooser.extensionFilters.addAll(
            FileChooser.ExtensionFilter("Image", "*.png", "*.jpg")
        )
        val selectedFile: File = fileChooser.showOpenDialog(null)
        imagePath = selectedFile.toURI().toString().drop(6)
        Node.currentImagePath = imagePath
//        ImageEdit.RefreshImage()

    }

    @FXML
    fun delete(){
        println(11)
        val prevTemplate = prevActionNode
        prevTemplate?.nextActionNode = null

        val nextTemplate = nextActionNode
        nextTemplate?.prevActionNode = null

        this.nextActionNode = null
        this.prevActionNode = null

        refreshCurves()



        this.isVisible = false
        var counter: Int = 1
        while(counter < Node.pool.size){

            if(Node.pool[counter].id == this.id)
            {
                Node.pool.remove(Node.pool[counter])
                break
            }
            counter++
        }


        //  ImageEdit.RefreshImage()
    }


    fun updatePoint(p: Point2D) {
        val local = parent.sceneToLocal(p)
        relocate(
            (local.x - offset.x),
            (local.y - offset.y)
        )
    }

    fun nodeHandlers() {

        contextDragOver = EventHandler { event ->
            updatePoint(Point2D(event.sceneX, event.sceneY))
            event.consume()
        }

        contextDragDropped = EventHandler { event ->
            parent.onDragDropped = null
            parent.onDragOver = null
            event.isDropCompleted = true
            event.consume()
        }



        title!!.onDragDetected = EventHandler { event->
            parent.onDragOver = contextDragOver
            parent.onDragDropped = contextDragDropped

            offset = Point2D(event.x, event.y)
            updatePoint(Point2D(event.sceneX, event.sceneY))

            val content = ClipboardContent()
            content[stateAddNode] = "node"

            startDragAndDrop(*TransferMode.ANY).setContent(content)
        }

    }



    fun linkHandlers() {

        //добавление новой линии
        linkDragDetected = EventHandler { event ->
            parent.onDragOver = null
            parent.onDragDropped = null

            Node.nodeDragged = this

            println(Node.nodeDragged)
            if(this.nextActionNode != null){

                this.nextActionNode?.prevActionNode = null
                this.nextActionNode = null
//                ImageEdit.RefreshImage()
                refreshCurves()

                //добавление новой
                parent.onDragOver = contextLinkDragOver
                parent.onDragDropped = contextLinkDagDropped

                superParent!!.children.add(0, link)
                link.isVisible = true

                //позиция начала кривой во время перетаскивания
                val p = Point2D(this.layoutX + width/2, this.layoutY + height/4)
                link.setStart(p)

                val content = ClipboardContent()
                content[stateAddLink] = "link"
                startDragAndDrop(*TransferMode.ANY).setContent(content)
            }
            else {

                parent.onDragOver = contextLinkDragOver
                parent.onDragDropped = contextLinkDagDropped

                superParent!!.children.add(0, link)

                //позиция начала кривой во время перетаскивания
                val p = Point2D(layoutX + width / 2, layoutY + height / 2)
                link.setStart(p)
                println(p.x)
                println(p.y)

                val content = ClipboardContent()
                content[stateAddLink] = "link"
                startDragAndDrop(*TransferMode.ANY).setContent(content)
            }

            event.consume()
        }

        //драг для удаления уже присоединенной линии
        linkDeleteDragDetected = EventHandler { event ->
            if(this.prevActionNode == null)
                return@EventHandler

            parent.onDragOver = null
            parent.onDragDropped = null

            //удаление и пересчет линий
            Node.nodeDragged = this.prevActionNode
            this.prevActionNode?.nextActionNode = null
            this.prevActionNode = null
            println(Node.nodeDragged)
//            ImageEdit.RefreshImage()
            refreshCurves()

            //добавление новой
            parent.onDragOver = contextLinkDragOver
            parent.onDragDropped = contextLinkDagDropped

            superParent!!.children.add(0, link)
            link.isVisible = true

            //позиция начала кривой во время перетаскивания
            val p = Point2D(Node.nodeDragged!!.layoutX + width/2, Node.nodeDragged!!.layoutY + height/4)

            link.setStart(p)

            val content = ClipboardContent()
            content[stateAddLink] = "link"
            startDragAndDrop(*TransferMode.ANY).setContent(content)
            event.consume()

//            ImageEdit.RefreshImage()
        }

        //конец кривой лежит на другом ноде
        linkDragDropped = EventHandler { event ->

            parent.onDragOver = null
            parent.onDragDropped = null

            superParent!!.children.removeAt(0)

            if(this.prevActionNode != null){
                println("Уже соединен")
                return@EventHandler
            }
            else {

                val link = NodeLink()
                link.bindStartEnd(Node.nodeDragged!!, this)
                Node.curvesNum++
                superParent!!.children.add(0, link)

                Node.nodeDragged!!.nextActionNode = this
                this.prevActionNode = Node.nodeDragged
//                ImageEdit.RefreshImage()
                Node.nodeDragged = null
            }

            event.isDropCompleted = true
            event.consume()
        }

        //позиция конца прямой во время перетаскивания
        contextLinkDragOver = EventHandler { event ->
            event.acceptTransferModes(*TransferMode.ANY)
            if (!link.isVisible) link.isVisible = true
            link.setEnd(Point2D(event.x, event.y))

            event.consume()
        }

        //конец кривой не лежит на другом ноде
        contextLinkDagDropped = EventHandler { event ->
            parent.onDragDropped = null
            parent.onDragOver = null

            link.isVisible = false
            superParent!!.children.removeAt(0)
            Node.nodeDragged = null

            event.isDropCompleted = true
            event.consume()
        }
    }

    fun refreshCurves(){
        println( Node.pool.size)
        while(Node.curvesNum > 0){

            superParent!!.children.removeAt(0)
            Node.curvesNum--
        }
        Node.pool.forEach {
            if(it.nextActionNode != null){

                val link = NodeLink()
                link.bindStartEnd(it, it.nextActionNode!!)
                Node.curvesNum++
                superParent!!.children.add(0, link)
            }
        }
    }
}