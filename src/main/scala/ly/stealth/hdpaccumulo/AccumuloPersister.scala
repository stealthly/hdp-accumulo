package ly.stealth.hdpaccumulo

import akka.actor.{Props, ActorLogging, Actor}
import com.sclasen.akka.kafka.BatchConnectorFSM
import ly.stealth.hdpaccumulo.Main.MsgBatch
import org.apache.accumulo.core.client.BatchWriter
import org.apache.accumulo.core.data.{Value, Mutation}
import org.apache.hadoop.io.Text
import scala.collection.JavaConversions._

object AccumuloPersister{
  def props(writer: BatchWriter):Props = Props(new AccumuloPersister(writer))
}

class AccumuloPersister(writer: BatchWriter) extends Actor with ActorLogging {

  log.info("Starting Persister")

  def receive: Receive = {
    case MsgBatch(xs) =>
      val mutations =
        xs map {
          msgAndMeta =>
            val m = new Mutation(new Text(msgAndMeta.offset.toString))
            val time = System.currentTimeMillis()
            m.put("meta", "topic", time, new Value(msgAndMeta.topic.getBytes))
            m.put("meta", "partition", time, new Value(msgAndMeta.partition.toString.getBytes))

            Option(msgAndMeta.key) foreach {
              key => m.put("data", "key", time, new Value(key.getBytes))
            }
            m.put("data", "message", time, new Value(msgAndMeta.msg.getBytes))

            m
        }

      writer.addMutations(mutations)
      writer.flush()

      log.info("Done: {}", xs.lastOption.map(_.offset))
      sender ! BatchConnectorFSM.BatchProcessed

    case x => log.warning("Received unhandled: {}", x)
  }
}
