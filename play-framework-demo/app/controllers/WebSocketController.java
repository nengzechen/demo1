package controllers;

import actors.ActorSystemManager;
import actors.WebSocketActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.F;
import play.libs.streams.ActorFlow;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.WebSocket;

import javax.inject.Inject;
import java.util.UUID;

/**
 * WebSocket控制器
 * 处理WebSocket连接和消息推送
 */
public class WebSocketController extends Controller {

    private final ActorSystem actorSystem;
    private final Materializer materializer;

    @Inject
    public WebSocketController(ActorSystemManager actorSystemManager, Materializer materializer) {
        this.actorSystem = actorSystemManager.getActorSystem();
        this.materializer = materializer;
    }

    /**
     * WebSocket连接端点
     * ws://localhost:9000/ws
     */
    public WebSocket socket() {
        return WebSocket.Json.accept(request -> {
            // 为每个连接生成唯一的客户端ID
            String clientId = generateClientId(request);

            // 使用ActorFlow创建WebSocket流
            // 每个WebSocket连接都会创建一个新的WebSocketActor
            return ActorFlow.actorRef(
                    out -> WebSocketActor.props(out, clientId),
                    actorSystem,
                    materializer
            );
        });
    }

    /**
     * 带客户端ID的WebSocket连接端点
     * ws://localhost:9000/ws/:clientId
     */
    public WebSocket socketWithId(String clientId) {
        return WebSocket.Json.accept(request -> {
            return ActorFlow.actorRef(
                    out -> WebSocketActor.props(out, clientId),
                    actorSystem,
                    materializer
            );
        });
    }

    /**
     * 生成客户端ID
     */
    private String generateClientId(Http.RequestHeader request) {
        // 可以从请求中提取一些信息来生成更有意义的ID
        // 例如：用户ID、session ID等
        return "client-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
