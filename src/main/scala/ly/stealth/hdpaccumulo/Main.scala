package ly.stealth.hdpaccumulo

import akka.actor.ActorSystem
import com.sclasen.akka.kafka.{AkkaBatchConsumer, AkkaBatchConsumerProps}
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import org.apache.accumulo.core.client.security.tokens.PasswordToken
import org.apache.accumulo.core.client.{BatchWriterConfig, ZooKeeperInstance}

object Main extends App {

  implicit val actorSystem = ActorSystem("kafkaAccumulo")

  val inst = new ZooKeeperInstance("dev", "172.16.25.10:2121")
  val conn = inst.getConnector("root", new PasswordToken("dev"))

  val writer = conn.createBatchWriter("akka-kafka", new BatchWriterConfig())

  val accumuloPersister = actorSystem.actorOf(AccumuloPersister.props(writer), "AccumuloPersister")

  // generic
  case class MsgAndMeta(topic:String, partition:Int, offset:Long, key:String, msg:String)
  case class MsgBatch(msgs: IndexedSeq[MsgAndMeta])

  def makeBatch(msgs: IndexedSeq[MsgAndMeta]):MsgBatch = MsgBatch(msgs)

  def messageHandler(msg: MessageAndMetadata[String, String]):MsgAndMeta = MsgAndMeta(msg.topic, msg.partition, msg.offset, msg.key(), msg.message())

  type B = Array[Byte]

  val consumerProps = AkkaBatchConsumerProps.forSystem[String, String,MsgAndMeta,MsgBatch](
  system = actorSystem,
  zkConnect = "192.168.86.5:2121",
  topic = "your-kafka-topic",
  group = "your-consumer-group",
  streams = 4, //one per partition
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
