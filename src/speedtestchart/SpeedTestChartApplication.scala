package speedtestchart


import speedtestchart.fetcher.Fetcher

import scala.swing.BorderPanel.Position._
import scala.swing._
import scala.util.matching.Regex


/**
 * Created by beenotung on 3/23/15.
 */
object SpeedTestChartApplication extends Thread {
  //private val runnable = new SpeedTestChartApplicationRunnable
  private val runnable = new SpeedTestChartJavaFxApplication

  override def run = {
    runnable.run
  }
}

import javafx.application.Application

private class SpeedTestChartJavaFxApplication extends Application with Runnable {

  import javafx.scene.Scene
  import javafx.scene.control.Label
  import javafx.scene.layout.StackPane
  import javafx.scene.layout.FlowPane
  import javafx.stage.Stage
  import javafx.scene.control.Button

  override def start(stage: Stage): Unit = {
    println("loading FX stage")
    stage.setTitle("SpeedTest Chart JavaFxApplication")
    val root = new StackPane()
    val loading=new Label("loading")
    //root.getChildren.add(loading)
    val hk=new Button("Speedtest.cn (Mainland)")
    val mainland=new Button("Speedtest.com (Hong Kong)")
    root.getChildren.add(hk)
    root.getChildren.add(mainland)
    val panel=new FlowPane
    panel.getChildren.add(hk)
    panel.getChildren.add(mainland)
    stage.setScene(new Scene(panel, 600, 450))
    root.getChildren.remove(loading)
    stage.show()
  }

  override def run(): Unit = {
    println("opening gui window")
    Application.launch(classOf[SpeedTestChartJavaFxApplication])
  }
}

private class SpeedTestChartApplicationRunnable extends SimpleSwingApplication with Runnable {
  val pathDialog: PathDialog = new PathDialog
  val fileListTextField = new TextField


  var fetcher: Fetcher = _
  var path: String = _
  var regex: String = _

  def init = {
    fetcher = new Fetcher(path, new Regex(regex))
    updateView
  }

  def updateView = {
    //TODO show all files from fetcher
    println("start files")
    fetcher.files.foreach(f => println(f.getPath))
    println("end files")
  }

  def setup = {
    println("list")
    for (file <- fetcher.files)
      println(file.toString)
  }

  override def run = {
    top.visible = true
    println("opening gui window")
    top.open
  }

  override def top: Frame = new MainFrame {
    title = "SpeedTest.cn Chart Application"
    contents = new BorderPanel {
      layout(new BoxPanel(Orientation.Vertical) {
        override val border = Swing.EmptyBorder(5, 5, 5, 5)
        contents += Button("Speedtest.cn (Mainland)") {
          pathDialog.open
        }
        contents += Button("Speedtest.com (Hong Kong)") {
          pathDialog.open
        }
      }) = Center
    }
    size = new Dimension(800, 600)
    location = new Point(100, 100)
  }

  class PathDialog extends Dialog {
    title = "Input Path"
    modal = true
    val pathTextField = new TextField
    val regexTextField = new TextField

    pathTextField.text = "/home/beenotung/files"
    regexTextField.text = """.*_Mar_.*\.html$"""

    contents = new BorderPanel {
      layout(new BoxPanel(Orientation.Vertical) {

        import scala.swing.Label

        override val border = Swing.EmptyBorder(5, 5, 5, 5)
        contents += new Label("Path:")
        contents += pathTextField
        contents += new Label("Regex:")
        contents += regexTextField
      }) = Center

      layout(new FlowPanel(FlowPanel.Alignment.Right)(
        Button("OK") {
          path = pathTextField.text
          regex = regexTextField.text
          close()
          init
          //setup
        }
      )) = South
    }

    override def open = {
      size = top.size
      location = top.location
      super.open
    }
  }

}
