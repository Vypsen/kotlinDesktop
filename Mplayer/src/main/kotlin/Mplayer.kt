import javafx.application.Application
import javafx.collections.FXCollections.observableArrayList
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import javafx.stage.Stage
import java.io.File
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.control.cell.PropertyValueFactory
import javafx.util.Duration.seconds
import kotlin.math.ceil
import kotlin.math.floor


class trackInfo(number: Int, name: String?, time: String, uri: String) {
    var number: Int = number
    var name: String? = name
    var time: String = time
    var uri: String = uri
}

class MPlayer : Application() {


    @FXML
    lateinit var select: MenuItem

    @FXML
    lateinit var playButton: Button

    @FXML
    lateinit var pauseButton: Button

    @FXML
    lateinit var stopButton: Button

    @FXML
    lateinit var numberColumn: TableColumn<trackInfo, Int>

    @FXML
    lateinit var nameColumn: TableColumn<trackInfo, String>

    @FXML
    lateinit var durationColumn: TableColumn<trackInfo, String>

    @FXML
    lateinit var table: TableView<trackInfo>

    @FXML
    lateinit var sliderDuration: Slider

    @FXML
    lateinit var nextButton: Button

    @FXML
    lateinit var prevButton: Button

    @FXML
    lateinit var sliderVolume: Slider


    private val data: ObservableList<trackInfo> = observableArrayList()
    private val listMusic: ArrayList<String> = arrayListOf()

    private var nowTrack = 0

    private var trackNumber = 1
    private var nowPlaying: String? = null
    private var mplayer: MediaPlayer? = null


    @FXML
    fun initialize() {




        select.setOnAction { selectClick() }


        playButton.setOnAction {
            playClick()
        }

        nextButton.setOnAction {
            nextClick()
        }

        prevButton.setOnAction {
            prevClick()
        }


        sliderDuration.value = 0.0
        sliderVolume.value = 30.0

        stopButton.setOnAction {
            playButton.text = "Play"
            mplayer?.stop()
        }


        numberColumn.cellValueFactory = PropertyValueFactory("number")
        nameColumn.cellValueFactory = PropertyValueFactory("name")
        durationColumn.cellValueFactory = PropertyValueFactory("time")


        table.setRowFactory {
            val row: TableRow<trackInfo> = TableRow()
            row.setOnMouseClicked { e ->
                if (e.clickCount == 2 && !row.isEmpty) {
                    mplayer?.stop()
                    nowPlaying = row.item.uri
                    nowTrack = listMusic.indexOf(nowPlaying)
                    table.selectionModel.select(nowTrack)
                    mplayer = MediaPlayer(Media(nowPlaying))
                    playButton.text = "Play"
                    playClick()

                }
            }
            row
        }

    }


    private fun playClick() {

        if (playButton.text == "Play") {
            playButton.text = "Pause"
            mplayer?.play()


        } else {
            mplayer?.pause()
            playButton.text = "Play"
        }
        table.selectionModel.select(nowTrack)
    }

    private fun nextClick() {
        mplayer?.stop()
        if (listMusic.indexOf(nowPlaying) < trackNumber - 2) {
            nowPlaying = listMusic[listMusic.indexOf(nowPlaying) + 1]
            mplayer = MediaPlayer(Media(nowPlaying))
            nowTrack++
            mplayer?.play()
        } else {
            nowPlaying = listMusic[0]
            mplayer = MediaPlayer(Media(nowPlaying))
            mplayer?.play()
            nowTrack = 0

        }
        table.selectionModel.select(nowTrack)
    }

    private fun prevClick() {
        mplayer?.stop()
        if (listMusic.indexOf(nowPlaying) != 0) {
            nowPlaying = listMusic[listMusic.indexOf(nowPlaying) - 1]
            mplayer = MediaPlayer(Media(nowPlaying))
            nowTrack--
            mplayer?.play()

        } else {
            nowPlaying = listMusic[trackNumber - 2]
            mplayer = MediaPlayer(Media(nowPlaying))
            mplayer?.play()
            nowTrack = listMusic.size - 1
        }
        table.selectionModel.select(nowTrack)
    }

    private fun selectClick() {
        val fileChooser = FileChooser()
        fileChooser.title = "Open File"
        fileChooser.extensionFilters.addAll(
            ExtensionFilter("Audio file", "*.mp3"))

        var window = select.parentPopup.scene.window

        val selectedFiles: List<File> = fileChooser.showOpenMultipleDialog(window)

        var media: Media?

        sliders()


        for (curFile in selectedFiles) {

            if (!listMusic.contains(curFile.toURI().toString())) {

                var uri = curFile.toURI()
                media = Media(uri.toString())
                var play = MediaPlayer(media)


                play.onReady = Runnable {

                    val name = curFile.name.substringBefore('.')
                    var time = play.totalDuration.toSeconds().toInt()
                    var track = trackInfo(trackNumber, name, "$time seconds", curFile.toURI().toString())
                    data.add(track)
                    table.items = data
                    trackNumber++

                }
                listMusic.add(curFile.toURI().toString())
            }
        }


        nowPlaying =  listMusic.first()
        mplayer = MediaPlayer(Media(nowPlaying))

    }




    private fun sliders(){
        Thread(Runnable {

            while (true) {

                if (mplayer != null) {

                    var currentTime = mplayer?.currentTime!!.toSeconds()

                    val allTime = mplayer?.stopTime!!.toSeconds()

                    if (!sliderDuration.isValueChanging)
                        sliderDuration.value = currentTime * 100.0 / allTime



                    sliderDuration.setOnMouseClicked {
                        mplayer?.seek(seconds(allTime * (it.x / playButton.parent.scene.width)))
                        sliderDuration.value = (it.x / playButton.parent.scene.width) * 100
                    }
                    mplayer?.volume = sliderVolume.value / 100.0

                    if (sliderDuration.value >= sliderDuration.max){
                        nowPlaying = listMusic[listMusic.indexOf(nowPlaying)+1]
                        mplayer = MediaPlayer(Media(nowPlaying))
                        mplayer?.play()
                    }




                }
                try {
                    Thread.sleep(300)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }).start()

    }



    override fun start(primaryStage:Stage) {

        var root = FXMLLoader.load<Parent>(javaClass.getResource("main.fxml"))
        var scene = Scene(root, 600.0, 400.0)
        primaryStage.scene = scene

        primaryStage.show()

    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(MPlayer::class.java)
        }
    }
}






