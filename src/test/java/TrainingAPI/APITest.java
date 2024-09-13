package TrainingAPI;

import DBConnection.DBConnection;
import Models.Products;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class APITest {

    @BeforeClass
    public void dbconnection(){
        try{
            DBConnection connection=new DBConnection();
            Connection connect= connection.getConnection();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @Test
    public void getCategories(){
        String endpoint="http://localhost/api_testing/category/read.php";
        /*ValidatableResponse response=given().when().get(endpoint).then();
        response.statusCode(200);*/

        given().when().get(endpoint).then().assertThat().statusCode(200);




    }

    @Test
    public void getProduct(){ //Verifying status code of a response
        String endpoint="http://localhost/api_testing/product/read_one.php";
        /*RequestSpecification request =given().queryParam("id","1");
        ValidatableResponse response=request.when().get(endpoint).then();
        response.statusCode(200);*/
        given().queryParam("id,2").when().get(endpoint).then().assertThat().statusCode(200).
                body("id",equalTo("2")).
                body("name",equalTo("Cross-Back Training Tank")).
                body("description",equalTo("The most awesome phone of 2013!")).
                body("price",equalTo("299.00")).
                body("category_id",equalTo("2")).
                body("category_name",equalTo("Active Wear - Women"));
    }
    @Test
    public void createProduct(){ //Create a product
        String endpoint="http://localhost/api_testing/product/create.php";
        String body="""
                {
                "name":"Water Bottle",
                "description": "Blue water bottle. Holds 64 ounces",
                "price" : 12,
                "category_id" : 3
                }
                """;
       // ValidatableResponse response=given().body(body).when().post(endpoint).then();
        //response.log().body();
        //response.statusCode(201);

        given().body(body).when().post(endpoint).then().assertThat().statusCode(201);
    }
    @Test
    public void updateProduct(){
        String endpopint="http://localhost/api_testing/product/update.php";
        String body= """
                {
                "id":19,
                "name":"Water Bottle",
               "description": "Blue water bottle. Holds 64 ounces",
                "price" : 15,
                "category_id" : 3
                }
                """;
        ValidatableResponse response=given().body(body).when().put(endpopint).then();
       // response.log().body();
        response.statusCode(200);
    }

    @Test
    public void deleteProduct(){
        String endpopint="http://localhost/api_testing/product/delete.php";
        String body= """
                {
                "id":19,
               
                }
                """;
        ValidatableResponse response=given().body(body).when().delete(endpopint).then();
        //response.log().body();
        response.statusCode(204);
    }

    @Test
    public void createSerializedProducts(){
        String endpoint="http://localhost/api_testing/product/create.php";
        Products product=new Products(
                "Water Bottle",
                "Blue water bottle",
                12,
                3
        );
        ValidatableResponse response=given().body(product).when().post(endpoint).then();
        //response.log().body();
        response.statusCode(201);
    }

    @Test
    public void getAllProducts(){  //Verifying complex response bodies
        String endpoint="http://localhost/api_testing/product/read.php";
        given().when().get(endpoint).then().log().body().assertThat().
                statusCode(200).body("records.size()",equalTo(19)); //Check 19 records are retrieved

        given().when().get(endpoint).then().log().body().assertThat().
                statusCode(200).body("records.size()",greaterThan(0)).// Check at least 1 record is retrieved
                body("records.id",everyItem(notNullValue())).//Check each and every record is not null
                body("records.name",everyItem(notNullValue())).
                body("records.description",everyItem(notNullValue())).
                body("records.price",everyItem(notNullValue())).
                body("records.category_id",everyItem(notNullValue())).
                body("records.category_name",everyItem(notNullValue())).
                body("records.id[0]",equalTo("25"));//check whether the first record's id equals to 25


    }

    @Test
    public void GetAllProducts(){  //verifying the response headers
        String endpoint="http://localhost/api_testing/product/read.php";


        given().when().get(endpoint).then().log().headers().assertThat().
                statusCode(200).header("Content-Type",equalTo("application/json; charset=UTF-8")).
                body("records.size()",greaterThan(0)).// Check at least 1 record is retrieved
                body("records.id",everyItem(notNullValue())).//Check each and every record is not null
                body("records.name",everyItem(notNullValue())).
                body("records.description",everyItem(notNullValue())).
                body("records.price",everyItem(notNullValue())).
                body("records.category_id",everyItem(notNullValue())).
                body("records.category_name",everyItem(notNullValue())).
                body("records.id[0]",equalTo("25"));//check whether the first record's id equals to 25


    }

    @Test
    public void getDeserializedProduct(){ //Deserializing response body
        String endpoint="http://localhost/api_testing/product/read.php";
        Products expectProducts= new Products(
                2,
                "Cross-Back Training Tank",
                "The most awesome phone of 2013!",
                299.00,
                2,
                "Active Wear - Women"
        );
        Products actualProduct=
                        given().
                        queryParam("id","2").
                        when().
                        get(endpoint).
                        as(Products.class);

        assertThat(actualProduct,samePropertyValuesAs(expectProducts));
    }


/*
    How can you assure you have records in your API response, and each record has an id that exists?

    assertThat().
    statusCode(200).
    body("records.size()", greaterThan(0)).
    body("records.id", everyItem(notNullValue()



    You have an API response and need to verify that xml data is being returned. What is the correct way to do this?

    header("Content-Type", equalTo("application/xml; charset=UTF-8"))



    Why would you want to deserialize an API response?

    to model a body and represent it as a Java object



    Which method directly precedes the assertThat() method?
    then()

    When examining an API response, which component is the most critical to validate?
    the body




    What is the correct statement to verify a status code of 200?

    given().
     queryParam("id",2).
     when().get(endpoint).
     then().assertThat().statusCode(200);



    How can you print the body of an API response to the console?

     public void printProduct() {
     String endpoint = "http:///localhost:8888/api_testing/product/read_one.php";
     given().queryParam("id", 2).
     when().get(endpoint).
     then().log().body(); }




    You want to verify that category 2 has the name "active wear". Which assertThat() statement is correct?


     assertThat().
     statusCode(200).
     body("category_id", equalTo("2")).
     body("category_name", equalTo("active wear"))






    What is the correct method to delete a sweatband product and log the incoming response?

     public void deleteSweatband(){
     String endpoint = "http://localhost:8888/api_testing/product/delete.php";
     String body = """  {
          "id": 26 }  """;
     var response = given().body(body).when().delete(endpoint).then();
     response.log().body(); }





    Which component of an API request is not always required?

     the data




    When would you want to use an API PUT request?
     when you want to update the price for an article of clothing




    How would you form an API GET request to get product information using a query parameter?

     public void getProduct() {
     String endpoint = "http://localhost:8888/api_testing/product/read_one.php";
     var response =
          given().
               queryParam("id",2).
          when().
               get(endpoint).
         then(); }





How would you form an API POST request to create a new product and log the incoming response?

         public void createProduct(){
     String endpoint = "http://localhost:8888/api_testing/product/create.php";
     String body = """ {
          "name": "Bottle",
          "description": "Holds 64 ounces"   }  """;
     var response = given().body(body).when().post(endpoint).then();
     response.log().body(); }








How would you form an API PUT request to update an existing product and log the incoming response?

     public void updateProduct(){
     String endpoint = "http://localhost:8888/api_testing/product/update.php";
     String body = """  {
          "id": 19,
          "name": "Bottle",
          "description": "Holds 64 ounces" }  """;
     var response = given().body(body).when().put(endpoint).then();
     response.log().body(); }










How would you form an API DELETE request to delete an existing product and log the incoming response?

     public void deleteProduct(){
     String endpoint = "http://localhost:8888/api_testing/product/delete.php";
     String body = """  {
          "id": 19   }  """;
     var response = given().body(body).when().delete(endpoint).then();
     response.log().body(); }










When given the following fields for a Product object, which constructor will allow for the serialization of a body for a PUT request?

     public Product(int id, String name, String description, double price, int category_id){
}









What is the best description of REST Assured?

an open-sourced Java library used for automated testing





Why would you not want to examine API responses manually?

Some API responses are long and can lead to errors in verifying.

 */
}
