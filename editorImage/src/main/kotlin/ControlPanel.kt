import javafx.application.Application
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import org.opencv.imgcodecs.Imgcodecs
import javafx.scene.image.WritablePixelFormat

import javafx.scene.image.WritableImage
import javafx.scene.layout.StackPane
import javafx.stage.Window
import org.opencv.core.*

import org.opencv.imgproc.Imgproc

import org.opencv.core.Mat
import org.opencv.core.Scalar

import org.opencv.core.Core
import java.util.stream.Stream

import javafx.stage.FileChooser


class ControlPanel : Application() {


    @FXML
    lateinit var control: AnchorPane

    @FXML
    lateinit var addImg: Button

    @FXML
    lateinit var buttonSave: Button



    @FXML
    fun addImageScene(){


        val filter = DraggableNode("addImageNode.fxml", Filters.addImage)
        addImg.isDisable = true

        Node.pool.add(filter)
        control.children.add(filter)
    }

    @FXML
    fun blackWhiteFilter(){
        val filter = DraggableNode("blackAndWhiteNode.fxml", Filters.blackWhite)
        Node.pool.add(filter)
        control.children.add(filter)
    }

    @FXML
    fun negativeFilter(){
        val filter = DraggableNode("negativeNode.fxml", Filters.negative)
        Node.pool.add(filter)
        control.children.add(filter)

    }
    @FXML
    fun blurFilter(){
        val filter = DraggableNode("blurNode.fxml", Filters.blur)
        Node.pool.add(filter)
        control.children.add(filter)
    }

    @FXML
    fun brightnessSaturationFilter(){
        val filter = DraggableNode("saturationNode.fxml", Filters.saturation)
        Node.pool.add(filter)
        control.children.add(filter)
    }

    @FXML
    fun mirror(){
        val filter = DraggableNode("mirrorNode.fxml", Filters.mirror)
        Node.pool.add(filter)
        control.children.add(filter)
    }

    @FXML
    fun save(){
        val fileChooser = FileChooser()
        fileChooser.initialFileName = "newImage"
        fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("Image", "*.png", "*.jpg", "*.webp"))
        val someFile = fileChooser.showSaveDialog(null)
        val newFilePath = someFile.toURI().toString().substring(6)
        val st = Imgcodecs.imwrite(newFilePath, Node.image)

    }


    @FXML
    fun initialize(){


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
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
            launch(ControlPanel::class.java)
        }


        fun RefreshFilters() {
            println(Node.image)
            if(Node.image != null) {
                var current: DraggableNode? = Node.pool[0]
                println(Node.pool.size)
                while (current != null) {
                    current.takeFilter()
                    current = current.nextNode
                }
            }
        }


        fun SetImage(path: String?) {

            val open: Stream<Window>? = Stage.getWindows().stream().filter { obj: Window -> obj.isShowing }

            if (path!=null && open!!.count().toInt() == 1)  {

                val qwerty = StackPane(Node.currentImageView)
                val scene = Scene(qwerty, Node.image!!.width().toDouble(), Node.image!!.height().toDouble())
                val imageStage = Stage()

                Node.currentImageView.image = (MatToImageFX(Node.image))

                imageStage.scene = scene
                imageStage.show()

            }
            else if(path!=null){
                Node.image = Imgcodecs.imread(path)
                Node.currentImageView.image = (MatToImageFX(Node.image))
            }


        }

        fun setNegative(){
            val m = Mat(Node.image!!.rows(), Node.image!!.cols(), Node.image!!.type(), Scalar(255.0, 255.0, 255.0))
            val negative = Mat()
            Core.subtract(m, Node.image, negative)
            Node.image = negative
            Node.currentImageView.image = MatToImageFX(Node.image)
        }

        fun setBlur(width: Double, height: Double){

            val img = Mat()
            Imgproc.blur(Node.image, img, Size(width, height))

            Node.image = img
            Node.currentImageView.image = MatToImageFX(Node.image)
        }

        fun setSaturation(brightness : Double, saturation : Double){
            val imgHSV = Mat()
            println("11111111111111111111111")
            println(Node.image)
            println("11111111111111111111111")
            Imgproc.cvtColor(Node.image, imgHSV, Imgproc.COLOR_BGR2HSV)
            Core.add(imgHSV, Scalar(0.0, saturation, brightness), imgHSV)
            val imgBGR = Mat()
            Imgproc.cvtColor(imgHSV, imgBGR, Imgproc.COLOR_HSV2BGR)
            Node.image = imgBGR
            Node.currentImageView.image = MatToImageFX(Node.image)
        }


        fun setBlackWhite(){
            val img = Mat()
            Imgproc.cvtColor(Node.image, img, Imgproc.COLOR_BGR2GRAY)
            val imgBGR = Mat()
            Imgproc.cvtColor(img, imgBGR, Imgproc.COLOR_RGB2BGR)
            Node.image = imgBGR
            Node.currentImageView.image = MatToImageFX(Node.image)

        }

        fun setMirror (){
            val img = Mat()
            Core.flip(Node.image, img, 1)
            Node.image = img
            Node.currentImageView.image = MatToImageFX(Node.image)

        }



        fun MatToImageFX(m: Mat?): WritableImage? {
            var m = m
            if (m == null || m.empty()) return null
            if (m.depth() == CvType.CV_8U) {
            } else if (m.depth() == CvType.CV_16U) {
                val m_16 = Mat()
                m.convertTo(m_16, CvType.CV_8U, 255.0 / 65535)
                m = m_16
            } else if (m.depth() == CvType.CV_32F) {
                val m_32 = Mat()
                m.convertTo(m_32, CvType.CV_8U, 255.0)
                m = m_32
            } else return null
            if (m.channels() == 1) {
                val m_bgra = Mat()
                Imgproc.cvtColor(m, m_bgra, Imgproc.COLOR_GRAY2BGRA)
                m = m_bgra
            } else if (m.channels() == 3) {
                val m_bgra = Mat()
                Imgproc.cvtColor(m, m_bgra, Imgproc.COLOR_BGR2BGRA)
                m = m_bgra
            } else if (m.channels() == 4) {
            } else return null
            val buf = ByteArray(m.channels() * m.cols() * m.rows())
            m[0, 0, buf]
            val wim = WritableImage(m.cols(), m.rows())
            val pw = wim.pixelWriter
            pw.setPixels(0, 0, m.cols(), m.rows(),
                WritablePixelFormat.getByteBgraInstance(),
                buf, 0, m.cols() * 4)
            return wim
        }
    }
}