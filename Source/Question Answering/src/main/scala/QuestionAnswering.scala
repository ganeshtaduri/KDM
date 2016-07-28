import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Naga on 7/20/2016.
  */
object QuestionAnswering {
  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName("QuestionAnswering").setMaster("local[*]")

    val sc = new SparkContext(sparkConf)

    val spark = SparkSession
      .builder
      .appName("QuestionAnswering")
      .master("local[*]")
      .getOrCreate()

    // Turn off Info Logger for Console
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)

      val input = scala.io.StdIn.readLine("Please enter your question \n")
   /* val input = sc.textFile("data/sentenceSample").map(line => {
      //Getting OpenIE Form of the word using CoreNLP*/
      val   t=CoreNLP.returnTriplets(input)
      println(t)


   // println(input.collect().mkString("\n"))

  }
}
