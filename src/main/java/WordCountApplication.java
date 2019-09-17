import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.h3c.iot.wordcount.ClusterDemo;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class WordCountApplication {
    public static void main(String[] args) {
        String port = args[0];
        String sysName = args[1];
        Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load());
        ActorSystem actorSystem = ActorSystem.create(sysName, config);
        actorSystem.actorOf(Props.create(ClusterDemo.class), "clusterDemo" + port);
    }
}
