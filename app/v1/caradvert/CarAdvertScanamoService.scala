//package v1.caradvert
//
//import java.time.LocalDate
//import java.util.UUID
//
//import executioncontexts.CarAdvertExecutionContext
//import javax.inject.{Inject, Singleton}
//import models.{CarAdvert, Diesel, FuelType, Gasoline, NotSelected}
//import play.api.{Logger, MarkerContext}
//import org.scanamo._
//import org.scanamo.syntax._
//import org.scanamo.auto._
//import org.scanamo.refined._
//import akka.actor.ActorSystem
//import akka.stream.ActorMaterializer
//import akka.stream.alpakka.dynamodb.scaladsl.DynamoClient
//import akka.stream.alpakka.dynamodb.impl.DynamoSettings
//import cats.free.Free
//import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType._
//import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync
//import org.scanamo.error.DynamoReadError
//import org.scanamo.ops.ScanamoOpsA
//
//import scala.concurrent.duration._
//import scala.concurrent.{ExecutionContextExecutor, Future}
//
//@Singleton
//class CarAdvertScanamoService @Inject()()(implicit ec: CarAdvertExecutionContext) extends CarAdvertRepository {
//
//  implicit val system: ActorSystem                = ActorSystem("scanamo-alpakka")
//  implicit val materializer: ActorMaterializer    = ActorMaterializer.create(system)
//  implicit val executor: ExecutionContextExecutor = system.dispatcher
//
//  implicit val javaLocalDateStringFormat: AnyRef with DynamoFormat[LocalDate] =
//    DynamoFormat.coercedXmap[LocalDate, String, IllegalArgumentException](
//      LocalDate.parse(_)
//    )(
//      _.toString
//    )
//
//  private val alpakkaClient: DynamoClient = DynamoClient(
//    DynamoSettings(
//      region = "",
//      host = "localhost",
//      port = 8042,
//      parallelism = 2,
//      credentialsProvider = DefaultAWSCredentialsProviderChain.getInstance
//    )
//  )
//
//  val client: AmazonDynamoDBAsync = LocalDynamoDB.client()
//  LocalDynamoDB.createTable(client)("car-adverts")('id -> S)
//
//  /*
//  // Example instantiations.
//  val id = UUID.fromString("9e5fd6e9-65ef-472c-ad89-e5fe658f14c6")
//  val create = EventEnvelope(id, 0, Create("Something"))
//  val delete = EventEnvelope(id, 1, Delete("Oops"))
//
//  val attributeValue = DynamoFormat[EventEnvelope].write(create)
//  // attributeValue: com.amazonaws.services.dynamodbv2.model.AttributeValue = {M: {seqNo={N: 0,}, id={S: 9e5fd6e9-65ef-472c-ad89-e5fe658f14c6,}, event={M: {Create={M: {name={S: Something,}},}},}},}
//
//  val dynamoRecord = DynamoFormat[EventEnvelope].read(attributeValue)
//  // dynamoRecord: Either[org.scanamo.error.DynamoReadError,EventEnvelope] = Right(EventEnvelope(9e5fd6e9-65ef-472c-ad89-e5fe658f14c6,0,Create(Something)))
//   */
//
//  private val logger: Logger = Logger(this.getClass)
//
//  override def create(carAdvert: CarAdvert)(implicit mc: MarkerContext): Future[Option[UUID]] = {
//
//    implicit val fuelTypeStringFormat: AnyRef with DynamoFormat[FuelType] =
//      DynamoFormat.coercedXmap[FuelType, String, IllegalArgumentException](
//        s => if (s.equals("GASOLINE")) Gasoline else if (s.equals("DIESEL")) Diesel else NotSelected
//      )(
//        _.toString
//      )
//
//    val carAdvertTable: Table[CarAdvert] = Table[CarAdvert]("caradvert")
//
//    val ops: Free[ScanamoOpsA, Option[Either[DynamoReadError, CarAdvert]]] = for {
//      _ <- carAdvertTable.put(
//        carAdvert
//      )
//      justInsertedCarAdvert <- carAdvertTable.get('id -> carAdvert.id.toString)
//
//    } yield justInsertedCarAdvert
//
//    val createResult: Option[Either[DynamoReadError, CarAdvert]] =
//      concurrent.Await.result(ScanamoAlpakka.exec(alpakkaClient)(ops), 5.seconds)
//
//    createResult.get match {
//      case Left(e) =>
//        Future {
//          logger.trace(s"DynamoReadError: ${e.toString}")
//          None
//        }
//
//      case Right(v) =>
//        Future {
//          logger.trace(s"create: carAdvert = $v")
//          Some(v.id)
//        }
//    }
//
//  }
//
//  override def update(data: CarAdvert)(implicit mc: MarkerContext): Future[Boolean] = ???
//
//  override def list()(implicit mc: MarkerContext): Future[Iterable[CarAdvert]] = ???
//
//  override def get(id: UUID)(implicit mc: MarkerContext): Future[Option[CarAdvert]] = ???
//
//  override def delete(id: UUID)(implicit mc: MarkerContext): Future[Boolean] = ???
//}
