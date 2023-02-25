package okhttp;

import com.google.gson.Gson;
import dto.ContactDto;
import dto.ContactResponseDto;
import kotlin.jvm.JvmDefaultWithoutCompatibility;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class DeleteContactByIdOkhttp {
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoia2F0ZUBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTY3NzgyNDY1NCwiaWF0IjoxNjc3MjI0NjU0fQ.sYNRyRWQbUrnb1iZSOjVQE2__CG5Bx93g1pEI68uShU";
    private final MediaType JSON = MediaType.get("application/json;charset=utf-8");

    private String id;
    @BeforeMethod
    public void preCondition() throws IOException {
        int i = new Random().nextInt(1000) + 1000;
        ContactDto dto = ContactDto.builder()
                .name("Max")
                .lastName("Payne")
                .email("max" + i + "@gmail.com")
                .phone("5468783" + i)
                .address("NY")
                .description("Friend")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(dto), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body).build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        ContactResponseDto resDto = gson.fromJson(response.body().string(), ContactResponseDto.class);
        System.out.println(resDto.getMessage());//Contact was added! ID: 39016b85-8296-44f5-a689-401023f28965
        String message = resDto.getMessage();
        String[] ar = message.split(": ");
        id = ar[1];

    }

    @Test
    public void deleteContactByIdSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/"+id)
                .delete()
                .addHeader("Authorization", token).build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        ContactResponseDto resdto = gson.fromJson(response.body().string(), ContactResponseDto.class);
        Assert.assertEquals(resdto.getMessage(), "Contact was deleted!");


    }
}
