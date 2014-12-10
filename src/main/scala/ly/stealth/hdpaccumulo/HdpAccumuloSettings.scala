package ly.stealth.hdpaccumulo

import com.typesafe.config.Config

class HdpAccumuloSettings(_config: Config) {
  private val config = _config.getConfig("hdp-accumulo")

  object AccumuloSettings {
    private val accumuloConfig = config.getConfig("accumulo")

    val ZkInstance = accumuloConfig.getString("zk.instance")
    val ZkHost = accumuloConfig.getString("zk.host")
    val ZkPort = accumuloConfig.getLong("zk.port")

    val User = accumuloConfig.getString("user")
    val Password = accumuloConfig.getString("password")
  }

  object KafkaConfig {
    private val kafkaConfig = config.getConfig("kafka")

    val ZkHost = kafkaConfig.getString("zk.host")
    val ZkPort = kafkaConfig.getLong("zk.port")
  }

  object AkkaKafkaConfig {
    private val akkaKafkaConfig = config.getConfig("akka-kafka")

    val NrOfStrems = akkaKafkaConfig.getInt("nr-of-streams")
  }

}
