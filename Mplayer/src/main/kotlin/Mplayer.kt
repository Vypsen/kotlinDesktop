import javafx.application.Application
import javafx.collections.FXCollections.observableArrayList
import javafx.collections.ObservableList
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import javafx.stage.Stage
import javafx.util.Duration.seconds
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode


class trackInfo(number: Int, name: String, time: BigDecimal?, uri: String) {
    var number: Int = number
    var name: String = name
    var time: BigDecimal? = time
    var uri: String = uri
}

class MPlayer : Application() {
    internal var curFile: File? = null
    internal var mplayer: MediaPlayer? = null
    internal var musicSlider = Slider(0.0, 100.0, 0.0)
    internal var volumeSlider = Slider(0.0, 50.0, 15.0)
    internal var musicTable = TableView<trackInfo>()
    internal val data: ObservableList<trackInfo> = observableArrayList<trackInfo>()
    internal var trackNumber = 1
    internal var nowPlaying: trackInfo? = null


    override fun start(primaryStage: Stage) {


        val root = object : BorderPane() {
            init {
                val fileChooser = FileChooser()
                fileChooser.title = "Open File"
                fileChooser.extensionFilters.addAll(
                        ExtensionFilter("Audio file", "*.mp3")
                )

                val vbox = object : VBox() {
                    init {

                        val hBox = object : HBox() {
                            init {
                                val playButton = Button("Play")
                                val pauseButton = Button("Pause")
                                val selectButton = Button("Select")
                                val stopButton = Button("Stop")

                                selectButton.setOnAction {
                                    val selectedFiles: List<File> = fileChooser.showOpenMultipleDialog(primaryStage)
                                    curFile = selectedFiles.first()
                                    if (curFile != null) {
                                        var media: Media? = null

                                        var uri = curFile!!.toURI()


                                        media = Media(uri.toString())


                                        mplayer = MediaPlayer(media)


                                        for (curFile in selectedFiles) {
                                            var time: BigDecimal? = null
                                            mplayer!!.setOnReady {
                                                Runnable {}

                                                time = BigDecimal(mplayer!!.totalDuration!!.toSeconds()).setScale(2, RoundingMode.HALF_EVEN)




                                                media = Media(curFile!!.toURI().toString())
                                                var name = curFile.name
                                                var track = trackInfo(trackNumber, name, time, curFile!!.toURI().toString())


                                                data.add(track)
                                                musicTable.items = data
                                                trackNumber++
                                            }
                                        }
                                    }
                                }

                                playButton.setOnAction {
                                    if (nowPlaying == null) {
                                        mplayer = MediaPlayer(Media(data.first().uri))
                                        nowPlaying = data.first()
                                    }

                                    mplayer?.play()
                                }



                                pauseButton.setOnAction { mplayer?.pause() }
                                stopButton.setOnAction { mplayer?.stop() }

                                children.addAll(playButton, pauseButton, stopButton, selectButton, volumeSlider)
                            }
                        }
                        val hBox2 = object : HBox() {
                            init {
                                val nextButton = Button("Next")
                                nextButton.setOnAction {
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

                                val prevButton = Button("Prev")
                                prevButton.setOnAction {
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

                                children.addAll(prevButton, nextButton)
                            }

                        }
                        hBox2.style = ("-fx-padding: 20 0 0 10 ")



                        children.addAll(hBox, musicSlider, hBox2)
                    }

                }

                center = vbox

                musicTable.setPrefWidth(400.0)
                musicTable.setPrefHeight(300.0)


                var numberColumn: TableColumn<trackInfo, Int> = TableColumn<trackInfo, Int>("Number")
                numberColumn.cellValueFactory = PropertyValueFactory("number")


                var nameColumn: TableColumn<trackInfo, String> = TableColumn<trackInfo, String>("Name")
                nameColumn.cellValueFactory = PropertyValueFactory("name")


                var timeColumn: TableColumn<trackInfo, BigDecimal?> = TableColumn<trackInfo, BigDecimal?>("Time")
                timeColumn.cellValueFactory = PropertyValueFactory("time")


                musicTable.getColumns().addAll(numberColumn, nameColumn, timeColumn)

                musicTable.setRowFactory {
                    var row: TableRow<trackInfo> = TableRow()
                    row.setOnMouseClicked { e ->
                        if (e.clickCount == 2 && !row.isEmpty) {
                            //row.style =  ("-fx-font-style: italic")
                            //println(row)
                            mplayer?.stop()
                            mplayer = MediaPlayer(Media(row.item.uri))
                            mplayer?.play()
                        }
                    }

                    row
                }

                bottom = musicTable

            }

        }




        Thread(Runnable {


            //var row: TablePosition<trackInfo, trackInfo> = TablePosition(musicTable, 1)
            while (true) {


                if (mplayer != null) {
                    var currentTime = mplayer?.currentTime!!.toSeconds()
                    val allTime = mplayer?.stopTime!!.toSeconds()

                    if (!musicSlider.isValueChanging)
                        musicSlider.value = currentTime * 100.0 / allTime

                    //println("Cur time " + currentTime * 100.0 / allTime)
                    musicSlider.setOnMouseClicked {
                        mplayer?.seek(seconds(allTime * (it.x / primaryStage.width)))
                        musicSlider.value = (it.x / primaryStage.width) * 100
                    }
                    mplayer?.volume = volumeSlider.value / 100.0




                    if (musicSlider.value >= musicSlider.max){
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


        val scene = Scene(root, 600.0, 400.0)

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



