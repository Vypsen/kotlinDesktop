import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.stage.Stage
import library.Notify


class project: Application(){
    var apps = Notify.builder()




    override fun start(primaryStage: Stage){






        //apps.setBorder(library.Notify.Border.SQUARE)
        //apps.setImage("https://avatars.mds.yandex.net/get-zen_doc/758638/pub_5be3111b3907cd00abe9104d_5be3118a817c4c00aa4980e3/scale_1200")
        apps.addImageMsg("Shrek", "Shrek", "app", "https://avatars.mds.yandex.net/get-zen_doc/758638/pub_5be3111b3907cd00abe9104d_5be3118a817c4c00aa4980e3/scale_1200", Notify.Border.SQUARE)
        //apps.setMessage("Сообщение ")
        //apps.setPressed(true)
        apps.addTextField()
        //apps.setComboBox()
        apps.SetWaitTime(100000)

        //apps.addProgressBar()


        apps.addButtons()


        apps.clickOk{ println(apps.textField?.text)}
        apps.clickCancel { println(111) }



            apps.SetPos(Notify.Position.RIGHT_TOP)


            var button = Button("Нажми")
            button.setOnAction {
                apps.build()

            }

            primaryStage.scene = Scene(button, 100.0, 100.0)
            primaryStage.show()
        }



    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(project::class.java)
        }

    }
}