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
import static org.hamcrest.Matchers.equalTo;

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
    public void getProduct(){
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
    public void createProduct(){
        String endpoint="http://localhost/api_testing/product/create.php";
        String body="""
                {
                "name":"Water Bottle",
                "description": "Blue water bottle. Holds 64 ounces",
                "price" : 12,
                "category_id" : 3
                }
                """;
        ValidatableResponse response=given().body(body).when().post(endpoint).then();
        //response.log().body();
        response.statusCode(201);
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

}
