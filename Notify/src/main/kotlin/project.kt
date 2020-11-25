import javafx.application.Application
import javafx.stage.Stage
import library.Border
import library.Notify


class project: Application(){



    var apps = Notify()
    var appsConfig = apps.config



    override fun start(primaryStage: Stage){

        appsConfig.SetMode(Notify.Mode.IMAGE)
        appsConfig.conImage.SetTitle("SHREK")
        appsConfig.conImage.setMessage("Shrek")
        appsConfig.conImage.SetAppName("shrek")
        appsConfig.SetPos(Notify.Position.RIGHT_BOTTOM)
        appsConfig.conImage.SetIconPath("https://avatars.mds.yandex.net/get-zen_doc/758638/pub_5be3111b3907cd00abe9104d_5be3118a817c4c00aa4980e3/scale_1200")
        appsConfig.conImage.setBorder(Border.CIRCLE)


        apps.start()

    }


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(project::class.java)
        }

    }
}