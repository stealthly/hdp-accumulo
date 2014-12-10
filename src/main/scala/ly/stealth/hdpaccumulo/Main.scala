package ly.stealth.hdpaccumulo

import akka.actor.ActorSystem
import com.sclasen.akka.kafka.{AkkaBatchConsumer, AkkaBatchConsumerProps}
import com.typesafe.config.ConfigFactory
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import org.apache.accumulo.core.client.security.tokens.PasswordToken
import org.apache.accumulo.core.client.{BatchWriterConfig, ZooKeeperInstance}

object Main extends App {

  val settings = new HdpAccumuloSettings(ConfigFactory.load())

  implicit val actorSystem = ActorSystem("kafkaAccumulo")

  val inst = new ZooKeeperInstance(settings.AccumuloSettings.ZkInstance, s"${settings.AccumuloSettings.ZkHost}:${settings.AccumuloSettings.ZkPort}")
  val conn = inst.getConnector(settings.AccumuloSettings.User, new PasswordToken(settings.AccumuloSettings.Password))

  val writer = conn.createBatchWriter("hdpaccumulo", new BatchWriterConfig())

  val accumuloPersister = actorSystem.actorOf(AccumuloPersister.props(writer), "AccumuloPersister")

  case class MsgAndMeta(topic:String, partition:Int, offset:Long, key:String, msg:String)
  case class MsgBatch(msgs: IndexedSeq[MsgAndMeta])

  def makeBatch(msgs: IndexedSeq[MsgAndMeta]):MsgBatch = MsgBatch(msgs)

  def messageHandler(msg: MessageAndMetadata[String, String]):MsgAndMeta = MsgAndMeta(msg.topic, msg.partition, msg.offset, msg.key(), msg.message())

  type B = Array[Byte]

  val consumerProps = AkkaBatchConsumerProps.forSystem[String, String,MsgAndMeta,MsgBatch](
  system = actorSystem,
  zkConnect = s"${settings.KafkaConfig.ZkHost}:${settings.KafkaConfig.ZkPort}",
  topic = "your-kafka-topic",
  group = "your-consumer-group",
  streams = settings.AkkaKafkaConfig.NrOfStrems,
  keyDecoder = new StringDecoder(),
  msgDecoder = new StringDecoder(),
  msgHandler = messageHandler,
  batchHandler = makeBatch,
  receiver = accumuloPersister
  )

  val consumer = new AkkaBatchConsumer(consumerProps)

  consumer.start()  //returns a Future[Unit] that completes when the connector is started

//  consumer.stop()   //returns a Future[Unit] that completes when the connector is stopped.
}
