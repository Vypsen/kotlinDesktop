import com.google.gson.Gson
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor.CLOSED_HAND
import javafx.scene.Cursor.HAND
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.effect.DropShadow
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.stage.Screen
import javafx.scene.layout.BackgroundPosition

import javafx.scene.layout.BackgroundRepeat

import javafx.scene.layout.BackgroundImage

import javafx.scene.layout.Background

import javafx.scene.layout.BackgroundSize





class InfoFilms {
    var keyword = String()
    var pagesCount = String()
    var films: List<Films>? = null
    var searchFilmsCountResult = 0
}

    class Films {
        var filmId = String()
        var nameRu = String()
        var nameEn = String()
        var year = String()
        var posterUrlPreview = String()
    }


    class searchResult {

        var search = VBox()

        var list = VBox()

        var screen = Screen.getPrimary().bounds

        var n = 0

        val shadow = DropShadow(10.0, Color.BLACK)

        var page = 1

        var count = 1
        var filmPage = 0

        val addList = Label("Добавить ещё фильмов")


        private fun addFilm(film: Films): Node {

            val nameRu = Label(film.nameRu)
            nameRu.font = Font(14.0)
            val nameEn = Label(film.nameEn)
            nameEn.font = Font(12.0)

            if (nameRu.text == "") {
                nameRu.text = nameEn.text
                nameEn.text = ""
            }

            nameRu.style = "-fx-text-fill: orange;" + "-fx-underline: true;"

            nameRu.onMouseEntered = EventHandler{
                nameRu.style = "-fx-text-fill: red;" + "-fx-underline: true;"
                nameRu.cursor = HAND
            }

            nameRu.onMouseExited = EventHandler{
                nameRu.style = "-fx-text-fill: orange;" + "-fx-underline: true;"
            }

            nameRu.onMouseClicked = EventHandler {
                Film().takeJsom(film.filmId)
            }

            val year = Label(film.year)
            year.font = Font(10.0)

            println(film.nameEn)


            val image = Image(film.posterUrlPreview, 55.0, 80.0, false, true)

            val imageView = ImageView(image)
            imageView.effect = shadow


            val count = Label(count.toString())
            count.alignment = Pos.TOP_RIGHT
            val item = HBox()



            item.spacing = 20.0
            val info = VBox()
            info.spacing = 5.0


            info.children.addAll(nameRu, nameEn, year)

            item.children.addAll(count, imageView, info)
            item.alignment = Pos.CENTER_LEFT
            item.padding = Insets(10.0)

            n++

            item.style = "-fx-border-color: grey;" + "-fx-border-width: 0 0 1 0;" + "-fx-border-style: segments(3)"

            return item

        }

        fun addPage(key: String, page: Int): InfoFilms? {
            val result = Kino.getSearch(key, page)
            val g = Gson()
            return g.fromJson(result, InfoFilms::class.java)
        }


        fun build(key: String): ScrollPane {

            page = 1

            var result = addPage(key, page)

            for (i in 0..9) {
                if (page == result!!.pagesCount.toInt() && filmPage == result!!.films!!.size) {

                    addList.onMouseClicked = EventHandler {
                        addList.text = "Это все фильмы"
                    }
                    break
                }
                val film = result!!.films!![filmPage]
                if (filmPage != result!!.films!!.size) {
                    list.children.add(addFilm(film))
                    count++
                    filmPage++
                }
            }


            addList.onMouseClicked = EventHandler {

                if (filmPage == result!!.films!!.size) {
                    println(filmPage)
                    println(page)
                    page++
                    result = addPage(key, page)
                    filmPage = 0
                    println(result!!.films!!.size)
                }
                for (i in 0..9) {
                    if (page == result!!.pagesCount.toInt() && filmPage == result!!.films!!.size) {

                        addList.onMouseClicked = EventHandler {
                            addList.text = "Это все фильмы"
                        }
                        break
                    }




                    if (filmPage != result!!.films!!.size) {
                        println(11)
                        val film = result!!.films!![filmPage]
                        list.children.add(addFilm(film))
                        count++
                        filmPage++
                    }
                }
            }


            addList.style = "-fx-text-fill: orange;" + "-fx-underline: true;"

            addList.onMouseEntered = EventHandler{
                addList.style = "-fx-text-fill: red;" + "-fx-underline: true;"
                addList.cursor = HAND
            }

            addList.onMouseExited = EventHandler{
                addList.style = "-fx-text-fill: orange;" + "-fx-underline: true;"
            }


            list.spacing = 20.0




            VBox.setMargin(list, Insets(20.0, 0.0, 0.0, screen.width/6))
            VBox.setMargin(addList, Insets(10.0, 10.0, 50.0, screen.width*5/6))
            search.children.addAll(list, addList)

            val scrollPane = ScrollPane(search)
            list.prefWidth = screen.width*2/3
            list.padding = Insets(30.0)
            //scrollPane.padding = Insets(0.0, 0.0, 0.0, 0.0)
            //scrollPane.maxHeight = screen.height - 70
            //scrollPane.style = "-fx-background-color: #EEEEEE"

            list.style =  "-fx-background-color: white;" +
                    "-fx-effect: dropshadow(gaussian, black, 10 , 0, 0, 0);"
            //list.style = "-fx-border-color: grey;" + "-fx-border-width: 1 1 1 1;"



            return scrollPane

        }
    }



