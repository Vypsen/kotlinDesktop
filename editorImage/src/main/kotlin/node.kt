import java.util.ArrayList

object Node {
    var nodeDragged: DraggableNode? = null
    var pool: MutableList<DraggableNode> = mutableListOf()
    var globalId = 1
    var curvesNum = 0
    var currentImagePath: String? = null
}