package mlpipeline

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{RegexTokenizer, StopWordsRemover}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.immutable.HashMap
import scala.collection.mutable

/**
  * Created by Naga on 7/19/2016.
  */
object Sample {
  def main(args: Array[String]) {
    System.setProperty("hadoop.home.dir", "D:\\winutils")

    val trainFolder = "data/Categories/Taste/100"
    val conf = new SparkConf().setAppName(s"Sample").setMaster("local[*]").set("spark.driver.memory", "4g").set("spark.executor.memory", "4g")
    val sc = new SparkContext(conf)
    val preprocessStart = System.nanoTime()
    val (input, corpus) =
      preprocess(sc, trainFolder)

    var hm = new HashMap[String, String]()

    val x = corpus.map{f =>
      val ner = CoreNLP.returnNER(f.toString())
      (f.toString, ner.toString)
    }.collect().toMap

    hm foreach(f=>println(f._1 + " " + f._2))
    println("Done")
  }

  private def preprocess(sc: SparkContext,
                         paths: String): (RDD[(String, String)], RDD[String]) = {

    val sqlContext = SQLContext.getOrCreate(sc)
    import sqlContext.implicits._
    val df = sc.textFile(paths).map(f => {
      var ff = f.replaceAll("[^a-zA-Z\\s:]", " ")
      ff = ff.replaceAll(":", "")
      //       println(ff)
      (paths, CoreNLP.returnLemma(ff))
    }).toDF("location", "docs")

    val tokenizer = new RegexTokenizer()
      .setInputCol("docs")
      .setOutputCol("rawTokens")
    val stopWordsRemover = new StopWordsRemover()
      .setInputCol("rawTokens")
      .setOutputCol("tokens")

    val pipeline = new Pipeline()
      .setStages(Array(tokenizer, stopWordsRemover))

    val model = pipeline.fit(df)
    val documents = model.transform(df)
      .select("tokens")
        .rdd
      .flatMap({case Row(tokens: mutable.WrappedArray[String]) => (tokens.toList)})



    val input = model.transform(df).select("location", "docs").rdd.map { case Row(location: String, docs: String) => (location, docs) }
    println(model.transform(df).printSchema())
    (input, documents)
  }
}
