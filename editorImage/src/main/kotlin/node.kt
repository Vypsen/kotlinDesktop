import javafx.scene.image.ImageView
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Scalar
import java.util.ArrayList

object Node {
    var nodeDragged: DraggableNode? = null
    var pool: MutableList<DraggableNode> = mutableListOf()
    var nodeId = 0
    var curvesNum = 0
    var image: Mat? = null
    var currentImageView = ImageView()
    var currentImagePath: String? = null
}