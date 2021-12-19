import AwsCustomMetric.{METRIC_NAME, NAMESPACE, STORAGE_RESOLUTION_SECONDS}
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient
import software.amazon.awssdk.services.cloudwatch.model.{MetricDatum, PutMetricDataRequest, StandardUnit}

import java.time.Instant
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure => ScalaFailure, Success => ScalaSuccess}
import scala.jdk.FutureConverters.CompletionStageOps


object AwsCustomMetric {
    val METRIC_NAME = s"Metric"
    val STORAGE_RESOLUTION_SECONDS = 60
    val NAMESPACE = "AutoScalingMetrics"
}
class AwsCustomMetric(m_region : String, m_credentials : AwsCredentialsProvider) {

    val cloudWatchAsyncClient: CloudWatchAsyncClient = CloudWatchAsyncClient
    .builder()
    .credentialsProvider(m_credentials)
    .region(Region.of(m_region))
    .build()

    def createCustomMetricWork(value : Double)
    {
        val metricDatum = MetricDatum
        .builder()
        .metricName(METRIC_NAME)
        .value(value)
        .unit(StandardUnit.NONE)
        .storageResolution(STORAGE_RESOLUTION_SECONDS)
        .timestamp(Instant.now())
        .build()

        val putMetricDataRequest = PutMetricDataRequest.builder().metricData(metricDatum).namespace(NAMESPACE).build()

        cloudWatchAsyncClient.putMetricData(putMetricDataRequest).asScala.onComplete
        {
            case ScalaSuccess(putMetricDataResponse) =>
            println(s"Metric published with $putMetricDataResponse")

            case ScalaFailure(exception) =>
            println(exception, "Got an exception while publishing metric")
        }
    }
}
