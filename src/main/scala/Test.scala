import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by yuch on 2017/7/29.
  */
object Test {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Spark WordCount")
    conf.setMaster("yarn-client")
    // 该参数是否添加只会影响是否显示Warn——hdp.version is not found
    conf.set("spark.yarn.am.extraJavaOptions", "-Dhdp.version=2.5.0.0-1245")
    // 该参数很重要,填写hdfs目录下的jar包,提交测试时就不用每次上传了,180M上传还是需要一定时间的
    conf.set("spark.yarn.jar",
      "hdfs://192.168.1.200:8020/hdp/apps/2.5.0.0-1245/spark/spark-hdp-assembly.jar")

    conf.set("num-executors","2")
    conf.set("driver-memory","1g")
    conf.set("executor-memory","1g")
    conf.set("spark.driver.host","10.8.0.6")

    val sc = new SparkContext(conf)
    // 测试程序的代码需要打包,并SparkContext.addJar
    // 此处引用的是项目打包后的本地jar位置,每次更新代码就只需打包,不用上传hdfs了
    //sc.addJar("hdfs://mycluster/user/linxu/spark-1.0.0.jar")
    sc.addJar("E:\\project-install\\yarnSpark\\target\\yarnSpark-1.0-SNAPSHOT.jar")

    val textFile = sc.textFile("hdfs://192.168.1.200:8020/test/yuch/wordCount.txt")
    val counts = textFile.flatMap(line => line.split(" "))
      .map(word => (word, 1))
      .reduceByKey(_ + _)

    counts.collect().foreach(println)

    sc.stop()
  }
}
