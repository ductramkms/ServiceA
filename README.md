### NOTE:

- The common bugs usually occurs:

```java
public class CamelRouterBuilder extends RouteBuilder {

  @Override
  public void configure() throws Exception {
    restConfiguration().host("230be57f37d3").port(8081).component("http")
        .endpointProperty("bridgeEndpoint", "true")
        .endpointProperty("throwExceptionOnFailure", "false"); // Add this line

    from("direct:employee")
        .toD("http://230be57f37d3:8081/employee?bridgeEndpoint=true")
        .log("${body}")
        .process(exchange -> {
          String result = exchange.getIn().getBody(String.class);
          System.out.println("@D_LOG: " + result);
          exchange.getMessage().setBody(result);
        });

    from("direct:event")
        .log("Sending message with body and header")
        .to("log:output")
        .process(new EventProcessor());
  }
}
```

At the "host" config, when deploy to docker it can't use 'localhost', it use the host name when inspect the container on docker.

```bash
docker inspect service_b_container
```

And then you find the hostname, in this case I got the value "230be57f37d3".
