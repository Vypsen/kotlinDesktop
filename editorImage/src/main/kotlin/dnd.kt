import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.geometry.Point2D
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.*
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.TilePane
import javafx.stage.FileChooser
import javafx.stage.Window
import java.util.*

import org.opencv.core.*
import org.opencv.imgproc.Imgproc

import org.opencv.imgproc.CLAHE

import org.opencv.core.Mat
import org.opencv.core.CvType

import org.opencv.core.Core

import org.opencv.core.MatOfFloat

import org.opencv.core.MatOfInt
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc.line

import java.util.ArrayList









enum class Filters{
    addImage,
    blackWhite,
    negative,
    blur,
    saturation,
    mirror
}

var stateAddNode = DataFormat("nodeAdd")
var stateAddLink = DataFormat("linkAdd")

class DraggableNode(link: String, filter: Filters) :HBox(){

    var thisFilters: Filters? = null
    var offset = Point2D(0.0, 0.0)

    @FXML
    var brightness: TextField? = null

    @FXML
    var saturation : TextField? = null

    @FXML
    var widthBlur: TextField? = null

    @FXML
    var heightBlur : TextField? = null


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

    var nextNode: DraggableNode? = null
    var prevNode: DraggableNode? = null


    fun takeFilter(){

        when (thisFilters){

            Filters.addImage-> {
                ControlPanel.SetImage(Node.currentImagePath)
            }
            Filters.saturation -> {
                val brightness = if(brightness!!.text.isEmpty() || brightness!!.text.toDouble() < 1){
                    0.0
                }
                else{
                    brightness!!.text.toDouble()
                }
                val saturation = if(saturation!!.text.isEmpty() || saturation!!.text.toDouble() < 1){
                    0.0
                }
                else{
                    saturation!!.text.toDouble()
                }

                ControlPanel.setSaturation(brightness, saturation)
            }

            Filters.negative -> {
                ControlPanel.setNegative()
            }

            Filters.blackWhite -> {
                ControlPanel.setBlackWhite()
            }

            Filters.blur -> {

                val blurW = if(widthBlur!!.text.isEmpty() || widthBlur!!.text.toDouble() < 1){
                    1.0
                }
                else{
                    widthBlur!!.text.toDouble()
                }
                val blurH = if(heightBlur!!.text.isEmpty() || heightBlur!!.text.toDouble() < 1){
                    1.0
                }
                else{
                    heightBlur!!.text.toDouble()
                }

                ControlPanel.setBlur(blurW, blurH)
            }

            Filters.mirror -> {
                ControlPanel.setMirror()
            }
        }
    }

    @FXML
    private fun initialize() {
        nodeHandlers()
        linkHandlers()

        rightLink.onDragDetected = linkDragDetected
        leftLink?.onDragDropped = linkDragDropped
        leftLink?.onDragDetected = linkDeleteDragDetected

        parentProperty().addListener{ o, old, new -> superParent = parent as AnchorPane?}

        brightness?.textProperty()?.addListener { o, old, new ->
            try {
                brightness?.text = new.toString()
                if(brightness?.text?.toDouble()!! > 255.0)
                    brightness?.text = 255.0.toString()
            }
            catch (e: NumberFormatException){
                brightness?.text = 0.0.toString()
            }
            ControlPanel.RefreshFilters()
        }

        saturation ?.textProperty()?.addListener { o, old, new ->
            try {
                saturation?.text = new.toString()
                if(saturation?.text?.toDouble()!! > 255.0)
                    saturation?.text = 255.0.toString()
            }
            catch (e: NumberFormatException){
                saturation?.text = 0.0.toString()
            }
            ControlPanel.RefreshFilters()
        }


        widthBlur?.textProperty()?.addListener { o, old, new ->
            try {
                widthBlur?.text = new.toString()
            }
            catch (e: NumberFormatException){

            }
            ControlPanel.RefreshFilters()
        }

        heightBlur?.textProperty()?.addListener { o, old, new ->
            try {
                heightBlur?.text = new.toString()
            }
            catch (e: NumberFormatException){

            }
            ControlPanel.RefreshFilters()
        }
    }

    init{

        val fxmlLoader = FXMLLoader(javaClass.getResource(link))

        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        fxmlLoader.load<Any>()

        thisFilters = filter

        id = Node.nodeId.toString()
        Node.nodeId++

    }


    @FXML
    fun addImage(){

        val fileChooser = FileChooser()

        fileChooser.extensionFilters.addAll(
            FileChooser.ExtensionFilter("Image", "*.png", "*.jpg")
        )
        val imagePath = fileChooser.showOpenDialog(null).toURI().toString().drop(6)


        Node.currentImagePath = imagePath
        val imgToAdd: Mat = Imgcodecs.imread(imagePath)
        Node.image = imgToAdd
        ControlPanel.RefreshFilters()
    }


    @FXML
    fun delete(){
        val nextNode = nextNode
        val prevNode = prevNode

        prevNode?.nextNode = null
        nextNode?.prevNode = null

        this.nextNode = null
        this.prevNode = null

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


        ControlPanel.RefreshFilters()
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

        linkDragDetected = EventHandler { event ->
            parent.onDragOver = null
            parent.onDragDropped = null

            Node.nodeDragged = this

            println(Node.nodeDragged)
            if(this.nextNode != null){

                this.nextNode?.prevNode = null
                this.nextNode = null
                ControlPanel.RefreshFilters()
                refreshCurves()

                parent.onDragOver = contextLinkDragOver
                parent.onDragDropped = contextLinkDagDropped

                superParent!!.children.add(0, link)

                val p = Point2D(this.layoutX + width/2, this.layoutY + height/4)
                link.setStart(p)

                val content = ClipboardContent()
                content[stateAddLink] = "link"
                startDragAndDrop(*TransferMode.ANY).setContent(content)
            }
            else {

                parent.onDragOver = contextLinkDragOver
                parent.onDragDropped = contextLinkDagDropped

                try{
                    superParent!!.children.add(0, link)
                }
                catch(e: Exception){

                }

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

        linkDeleteDragDetected = EventHandler { event ->
            if(this.prevNode == null)
                return@EventHandler

            parent.onDragOver = null
            parent.onDragDropped = null

            Node.nodeDragged = this.prevNode
            this.prevNode?.nextNode = null
            this.prevNode = null
            println(Node.nodeDragged)
            ControlPanel.RefreshFilters()
            refreshCurves()

            parent.onDragOver = contextLinkDragOver
            parent.onDragDropped = contextLinkDagDropped

            superParent!!.children.add(0, link)
            link.isVisible = true

            val p = Point2D(Node.nodeDragged!!.layoutX + width/2, Node.nodeDragged!!.layoutY + height/4)

            link.setStart(p)

            val content = ClipboardContent()
            content[stateAddLink] = "link"
            startDragAndDrop(*TransferMode.ANY).setContent(content)
            event.consume()

            ControlPanel.RefreshFilters()
        }

        linkDragDropped = EventHandler { event ->

            parent.onDragOver = null
            parent.onDragDropped = null

            superParent!!.children.removeAt(0)

            if(this.prevNode != null){
                return@EventHandler
            }
            else {

                val link = NodeLink()
                link.bindStartEnd(Node.nodeDragged!!, this)
                Node.curvesNum++
                superParent!!.children.add(0, link)

                Node.nodeDragged!!.nextNode = this
                this.prevNode = Node.nodeDragged
                ControlPanel.RefreshFilters()
                Node.nodeDragged = null
            }

            event.isDropCompleted = true
            event.consume()
        }

        contextLinkDragOver = EventHandler { event ->
            event.acceptTransferModes(*TransferMode.ANY)
            link.setEnd(Point2D(event.x, event.y))

            event.consume()
        }

        contextLinkDagDropped = EventHandler { event ->
            parent.onDragDropped = null
            parent.onDragOver = null

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
            if(it.nextNode != null){

                val link = NodeLink()
                link.bindStartEnd(it, it.nextNode!!)
                Node.curvesNum++
                superParent!!.children.add(0, link)
            }
        }
    }

}