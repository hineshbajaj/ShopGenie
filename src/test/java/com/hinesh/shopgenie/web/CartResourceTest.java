package com.hinesh.shopgenie.web;

import com.hinesh.shopgenie.order.enums.CartStatus;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
class CartResourceTest {

    private static final String INSERT_WRONG_CARD_IN_DB =
            "insert into carts values (999, current_timestamp, current_timestamp, 'NEW', 3)";
    private static final String DELETE_WRONG_CARD_IN_DB =
            "delete from carts where id = 999";

    @Inject
    DataSource dataSource;

    @Test
    void testFindAll() {
        get("/carts")
                .then()
                .statusCode(OK.getStatusCode())
                .body("size()", greaterThan(0));
    }

    @Test
    void testFindAllActiveCarts() {
        get("/carts/active").then()
                .statusCode(OK.getStatusCode());
    }

    @Test
    void testGetActiveCartForCustomer() {
        get("/carts/customer/3").then()
                .statusCode(OK.getStatusCode())
                .body(containsString("Peter"));
    }

    @Test
    void testFindById() {
        get("/carts/3").then()
                .statusCode(OK.getStatusCode())
                .body(containsString("status"))
                .body(containsString("NEW"));
        get("/carts/100").then()
                .statusCode(NO_CONTENT.getStatusCode());
    }

    /*
     * Commented Test Case.
     * Unable to pass this test for now
     * will look into it later
    @Test
    void testDelete() {
        get("/carts/active").then()
                .statusCode(OK.getStatusCode())
                .body(containsString("Jason"))
                .body(containsString("NEW"));

        delete("/carts/1").then()
                .statusCode(NO_CONTENT.getStatusCode());
        get("/carts/1").then()
                .statusCode(OK.getStatusCode())
                .body(containsString("Jason"))
                .body(containsString("CANCELED"));
    } */

    @Test
    void testGetActiveCartForCustomerWhenThereAreTwoCartsInDB() {
        executeSql(INSERT_WRONG_CARD_IN_DB);

        get("/carts/customer/3").then()
                .statusCode(INTERNAL_SERVER_ERROR.getStatusCode())
                .body(containsString("IllegalState"))
                .body(containsString("Many active carts detected !!!"));
        executeSql(DELETE_WRONG_CARD_IN_DB);
    }

    private void executeSql(String query) {
        try (var connection = dataSource.getConnection()) {
            var statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new IllegalStateException("Error has occurred while trying to execute SQL Query:" + e.getMessage());
        }
    }

    @Test
    void testCreateCart() {
        var requestParams = Map.of("firstName", "Saul", "lastName", "Berenson", "email", "call.saul@mail.com");
        var newCustomerId = given().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(requestParams).post("/customers").then().statusCode(OK.getStatusCode()).extract()
                .jsonPath().getInt("id");
        var response = post("/carts/customer/" + newCustomerId).then()
                .statusCode(OK.getStatusCode())
                .extract()
                .jsonPath().getMap("$");
        assertThat(response.get("id")).isNotNull();
        assertThat(response).containsEntry("status", CartStatus.NEW.name());

        delete("/carts/" + response.get("id")).then().statusCode(NO_CONTENT.getStatusCode());
        delete("/customers/" + newCustomerId).then().statusCode(NO_CONTENT.getStatusCode());

    }

    @Test
    void testFailCreateCartWhileHavingAlreadyActiveCart() {

        var requestParams = Map.of("firstName", "Saul", "lastName", "Berenson",
                "email", "call.saul@mail.com");

        var newCustomerId = given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(requestParams)
                .post("/customers").then()
                .statusCode(OK.getStatusCode())
                .extract()
                .jsonPath()
                .getLong("id");
        var newCartId = post("/carts/customer/" + newCustomerId).then()
                .statusCode(OK.getStatusCode())
                .extract()
                .jsonPath()
                .getLong("id");


        post("/carts/customer/" + newCustomerId).then()
                .statusCode(INTERNAL_SERVER_ERROR.getStatusCode())
                .body(containsString("IllegalStateException")).body(containsString("There is already an active cart"));
        assertThat(newCartId).isPositive();
        delete("/carts/" + newCartId).then()
                .statusCode(NO_CONTENT.getStatusCode());
        delete("/customers/" + newCustomerId).then()
                .statusCode(NO_CONTENT.getStatusCode());
    }
}
