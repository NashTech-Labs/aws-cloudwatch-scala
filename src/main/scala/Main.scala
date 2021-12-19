import software.amazon.awssdk.auth.credentials.{AwsBasicCredentials, StaticCredentialsProvider}

object Main extends App {
    val awsCustomMetric = new AwsCustomMetric("eu-west-1", StaticCredentialsProvider.create(new AwsBasicCredentials("accessKeyID", "secretAccessKey")))

    awsCustomMetric.createCustomMetricWork(90.28)

}
