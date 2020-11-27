import javafx.application.Application
import javafx.stage.Stage
import library.ConfigImageMsg
import library.Notify

class project: Application(){

    var apps = Notify()

    override fun start(primaryStage: Stage){



        //apps.setImage("https://avatars.mds.yandex.net/get-zen_doc/758638/pub_5be3111b3907cd00abe9104d_5be3118a817c4c00aa4980e3/scale_1200")
        apps.setImageMsg("Shrek", "Shrek", "app", "https://avatars.mds.yandex.net/get-zen_doc/758638/pub_5be3111b3907cd00abe9104d_5be3118a817c4c00aa4980e3/scale_1200", ConfigImageMsg.Border.CIRCLE)
        //apps.setMessage("Сообщение ")
        apps.SetPos(Notify.Position.RIGHT_BOTTOM)
        //apps.setTextField()
        //apps.setButtons()
        //apps.setMessage("weqweqweqweqweqwe")
        //apps.setComboBox()
        apps.setButtons()
        apps.start()

    }


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(project::class.java)
        }

    }
}