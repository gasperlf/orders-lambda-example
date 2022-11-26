package net.ontopsolutions.api;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.ontopsolutions.dto.Order;
import net.ontopsolutions.utils.UtilityJackson;

public class CreateOrderLambda {

    private final DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());

    public APIGatewayProxyResponseEvent createOrder(APIGatewayProxyRequestEvent requestEvent) throws JsonProcessingException {

        Order order = UtilityJackson.parser(requestEvent.getBody(), Order.class);

        Table ordersTable = dynamoDB.getTable(System.getenv("ORDERS_TABLE"));
        Item item = new Item()
                .withPrimaryKey("id", order.getId())
                .with("itemName", order.getItemName())
                .with("quantity", order.getQuantity());

        ordersTable.putItem(item);
        return new APIGatewayProxyResponseEvent().withStatusCode(200)
                .withBody("Order ID=" + order.getId());
    }

}
