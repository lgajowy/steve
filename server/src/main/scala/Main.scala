package steve

import com.comcast.ip4s.port
import com.comcast.ip4s.host

import org.http4s.ember.server.EmberServerBuilder
import cats.effect.IO
import cats.effect.IOApp

import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter

object Main extends IOApp.Simple {

  def run: IO[Unit] =
    EmberServerBuilder
      .default[IO]
      .withHost(host"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp {

        val exec = ServerSideExecutor.instance[IO]

        // bind protocol to server side executor
        val endpoints: List[ServerEndpoint[_, _, _, Any, IO]] = List(
          protocol.build.serverLogicInfallible(exec.build),
          protocol.run.serverLogicInfallible(exec.run),
        )

        // transform to routes
        Http4sServerInterpreter[IO]()
          .toRoutes(endpoints)
          .orNotFound
      }
      .build
      .useForever

}
