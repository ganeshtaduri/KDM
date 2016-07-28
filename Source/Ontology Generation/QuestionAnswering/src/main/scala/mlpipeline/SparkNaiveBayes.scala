package mlpipeline

import ontInterface.{ElectionsOwl, OwlPizza}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{RegexTokenizer, StopWordsRemover}
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}
import rita.RiWordNet

object SparkNaiveBayes {


  def main(args: Array[String]) {
    System.setProperty("hadoop.home.dir", "C:\\winutils")
    val wordnet: RiWordNet = new RiWordNet("E:\\Tutorial code\\WordNet-3.0.tar\\WordNet-3.0")
    val trainFolder = "data/test/*"
    val conf = new SparkConf().setAppName(s"NBExample").setMaster("local[*]").set("spark.driver.memory", "4g").set("spark.executor.memory", "4g")
    val sc = new SparkContext(conf)

    Logger.getRootLogger.setLevel(Level.WARN)



    // Load documents, and prepare them for NB.
    val preprocessStart = System.nanoTime()
    val (input, tokenizedData) = preprocess(sc, trainFolder)

    val x = tokenizedData.map { f =>
      val ner = CoreNLP.returnNER(f.toString())
      (f.toString, ner.toString)
    }.collect().toMap

    val broadcastedNER = sc.broadcast(x)

    x.foreach(println(_))


    val predefinedPredicates = Map("located" -> "isLocated", "timed" -> "hasTimeDate", "dated" -> "hasTimeDate", "person" -> "isPerson")
    val predicateMap = sc.broadcast(predefinedPredicates.map(f => {
      val pos = wordnet.getPos(f._1)
      val synonyms = pos.flatMap(p => {
        val synonyms: Array[String] = wordnet.getAllSynonyms(f._1, p)
        synonyms.toList
      })
      (f._2, synonyms)
    }))



    val tripletData = sc.wholeTextFiles(trainFolder).flatMap(f => {
      val triplet = CoreNLP.returnOpenIE(f._2)
      triplet.toList
    }).map(f => {
      println(f)
      val splitTriple = f.split(";")
      val subj = CoreNLP.returnLemma(splitTriple(0))
      val pred = splitTriple(1)
      var p = ""
      predicateMap.value.foreach(f => {
        if (f._2.mkString(" ").contains(pred)) {
          p = f._1
        }
        else if (pred == "is") {
          p = "subClass"
        }
      })

      val obj = CoreNLP.returnLemma(splitTriple(2))
      (subj, p, obj)
    })

    val owl = new ElectionsOwl()

    tripletData.collect().foreach(f => {
      println(f)
      if (f._2 == "is") {
        owl.createClass(":" + f._3)
        owl.createIndividual(":" + f._1, ":" + f._3)
      }
      else {
        owl.createClass(":" + broadcastedNER.value.get(f._3))
        owl.createIndividual(":" + f._3, ":" + broadcastedNER.value.get(f._3))
        owl.createObjectProperty(":" + f._1, ":" + f._2, ":" + f._3)
      }
    })

    owl.saveOntology()
    sc.stop()
  }

  private def preprocess(sc: SparkContext,
                         paths: String): (RDD[(String, String)], RDD[String]) = {

    val sqlContext = SQLContext.getOrCreate(sc)
    import sqlContext.implicits._
    val df = sc.wholeTextFiles(paths).map(f => {
      var ff = f._2.replaceAll("[^a-zA-Z\\s:]", " ")
      ff = ff.replaceAll(":", "")
      // println(ff)
      (f._1, CoreNLP.returnLemma(ff))
    }).toDF("location", "docs")


    val tokenizer = new RegexTokenizer()
      .setInputCol("docs")
      .setOutputCol("rawTokens")
    val stopWordsRemover = new StopWordsRemover()
      .setInputCol("rawTokens")
      .setOutputCol("tokens")

    val tf = new org.apache.spark.ml.feature.HashingTF()
      .setInputCol("tokens")
      .setOutputCol("features")
    val idf = new org.apache.spark.ml.feature.IDF()
      .setInputCol("features")
      .setOutputCol("idfFeatures")

    val pipeline = new Pipeline()
      .setStages(Array(tokenizer, stopWordsRemover,tf,idf))

    val model = pipeline.fit(df)

    val corpus = model.transform(df)
      .select("tokens")
      .rdd
      .flatMap { case Row(tokens: scala.collection.mutable.WrappedArray[String]) => (tokens.toList) }

    val input = model.transform(df).select("location", "docs").rdd.map { case Row(location: String, docs: String) => (location, docs) }
    println(model.transform(df).printSchema())
    (input, corpus)
  }

  def getTFIDFVector(sc: SparkContext, input: Array[String]): RDD[Vector] = {

    val sqlContext = SQLContext.getOrCreate(sc)
    import sqlContext.implicits._
    val df = sc.parallelize(input.toSeq).toDF("docs")


    val tokenizer = new RegexTokenizer()
      .setInputCol("docs")
      .setOutputCol("rawTokens")
    val stopWordsRemover = new StopWordsRemover()
      .setInputCol("rawTokens")
      .setOutputCol("tokens")

    val tf = new org.apache.spark.ml.feature.HashingTF()
      .setInputCol("tokens")
      .setOutputCol("features")
    val idf = new org.apache.spark.ml.feature.IDF()
      .setInputCol("features")
      .setOutputCol("idfFeatures")



    val pipeline = new Pipeline()
      .setStages(Array(tokenizer, stopWordsRemover, tf, idf))

    val model = pipeline.fit(df)
    val documents = model.transform(df)
      .select("idfFeatures")
      .rdd
      .map { case Row(features: Vector) => features }

    documents
  }
}
