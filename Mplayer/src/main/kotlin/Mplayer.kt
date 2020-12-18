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
import javafx.scene.input.MouseEvent
import javafx.util.Duration.seconds
import kotlin.math.roundToInt
import javax.sound.sampled.AudioFileFormat
import kotlin.math.ceil
import kotlin.math.floor


class trackInfo(number: Int, name: String, time: String, uri: String) {
    var number: Int = number
    var name: String = name
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






    private var curFile: File? = null
    private val data: ObservableList<trackInfo> = observableArrayList<trackInfo>()
    private var trackNumber = 1
    private var nowPlaying: trackInfo? = null
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
            var row: TableRow<trackInfo> = TableRow()
            row.setOnMouseClicked { e ->
                if (e.clickCount == 2 && !row.isEmpty) {
                    mplayer?.stop()
                    nowPlaying = row.item
                    playButton.text = "Play"
                    playClick()
                }
            }
            row
        }


    }



    private fun playClick() {
        if (playButton.text == "Play") {
            mplayer = MediaPlayer(Media(nowPlaying!!.uri))
            playButton.text = "Pause"
            mplayer?.play()
        }

        else{
            mplayer?.pause()
            playButton.text = "Play"
        }
    }

    private fun nextClick(){
        mplayer?.stop()
        if(nowPlaying!!.number < trackNumber - 1 ) {
            nowPlaying = data[nowPlaying!!.number]
            mplayer = MediaPlayer(Media(nowPlaying!!.uri))
            mplayer?.play()
        }
        else {
            nowPlaying = data[0]
            mplayer = MediaPlayer(Media(nowPlaying!!.uri))
            mplayer?.play()

        }
    }

    private fun  prevClick(){
        mplayer?.stop()
        if(nowPlaying!!.number > 1) {
            nowPlaying = data[nowPlaying!!.number - 2]
            mplayer = MediaPlayer(Media(nowPlaying!!.uri))
            mplayer?.play()
        }
        else {
            nowPlaying = data[trackNumber - 2]
            mplayer = MediaPlayer(Media(nowPlaying!!.uri))
            mplayer?.play()
        }
    }

    private fun selectClick(){
        val fileChooser = FileChooser()
        fileChooser.title = "Open File"
        fileChooser.extensionFilters.addAll(
                ExtensionFilter("Audio file", "*.mp3"))

        var window = select.parentPopup.scene.window

        val selectedFiles: List<File> = fileChooser.showOpenMultipleDialog(window)

        curFile = selectedFiles.first()
        if (curFile != null) {
            var media: Media?
            var uri = curFile!!.toURI()
            media = Media(uri.toString())
            mplayer = MediaPlayer(media)


            slid()


            for (curFile in selectedFiles) {
                var name = curFile.name

                uri = curFile!!.toURI()
                media = Media(uri.toString())
                mplayer = MediaPlayer(media)

                var source = mplayer?.media?.source
                source = source?.substring(0, source.length - ".mp3".length)




                var track = trackInfo(trackNumber, name, "0", curFile!!.toURI().toString())
                data.add(track)
                table.items = data
                trackNumber++

            }


        }
        nowPlaying =  data.first()
    }


    private fun slid(){
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
                        nowPlaying = data[nowPlaying!!.number]
                        mplayer = MediaPlayer(Media(nowPlaying!!.uri))
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




