import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.stage.Stage
import library.ConfigImageMsg
import library.Notify

class project: Application(){

    override fun start(primaryStage: Stage){

        var apps = Notify()
        apps.setBorder(Notify.Border.SQUARE)
       // apps.setImage("https://avatars.mds.yandex.net/get-zen_doc/758638/pub_5be3111b3907cd00abe9104d_5be3118a817c4c00aa4980e3/scale_1200")
        apps.setImageMsg("Shrek", "Shrek", "app", "https://avatars.mds.yandex.net/get-zen_doc/758638/pub_5be3111b3907cd00abe9104d_5be3118a817c4c00aa4980e3/scale_1200", ConfigImageMsg.Border.SQUARE)
        //apps.setMessage("Сообщение ")
        apps.setPressed(true)
        apps.setTextField()
        apps.setButtons()
        apps.SetPos(Notify.Position.RIGHT_TOP)




        //
        //apps.setMessage("weqweqweqweqweqwe")
        //apps.setComboBox()
        //apps.setButtons()



        var button = Button("Нажми")
        button.setOnAction {
            apps.start()

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