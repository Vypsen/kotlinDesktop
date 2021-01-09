import com.google.gson.Gson
import com.sun.javafx.scene.control.skin.Utils.getResource
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor.HAND
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.ScrollBar
import javafx.scene.control.ScrollPane
import javafx.scene.effect.DropShadow
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.DataFormat.URL
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.stage.Screen

import javafx.stage.StageStyle


    class InfoFilms {
        var keyword = String()
        var pagesCount = String()
        var films: List<Films>? = null
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
                FormFilm(film.filmId).main()
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

            for (i in 0..4) {
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
                for (i in 0..4) {
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






            val scrollPane = ScrollPane(search)


            list.padding = Insets(30.0)






            val image = Image("ki.jpg")


            scrollPane.minWidth = screen.width
            search.minWidth = scrollPane.minWidth




            search.alignment = Pos.CENTER


            search.children.addAll(list, addList)


            list.maxWidthProperty().bind(search.widthProperty().multiply(0.60))
            VBox.setMargin(list, Insets(20.0, 0.0, 0.0, 0.0))
            VBox.setMargin(addList, Insets(10.0, 0.0, 50.0, 0.0))

            list.style =  "-fx-background-color: white;" +
                    "-fx-effect: dropshadow(gaussian, black, 10 , 0, 0, 0);"
            println(search.minWidth)


            var img = Image("ki.jpg")

            ("-fx-background-color: transparent; ")

            search.style =

            //scrollPane. style =  "-fx-background-color: red;"
           //scrollPane.style = ("-fx-background-image: url(" + "ki.jpg" +")")

            //search.style = ("-fx-background-image: url(" + "ki.jpg" +")")


            return scrollPane

        }
    }



