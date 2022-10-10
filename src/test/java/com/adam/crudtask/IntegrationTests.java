package com.adam.crudtask;

import com.adam.crudtask.entity.Task;
import com.adam.crudtask.util.RetrieveUtil;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@SpringBootTest
public class IntegrationTests {



    @Test
    public void givenTaskDoesNotExists_whenTaskIsRetrieved_then404IsReceived()
            throws IOException {

        //given
        String id = "99999";
        HttpUriRequest request = new HttpGet("http://localhost:2137/api/tasks/" + id);

        //when
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        //then
        Assertions.assertEquals(HttpStatus.SC_NOT_FOUND,httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void givenRequestWithNoAcceptHeader_whenRequestIsExecuted_thenDefaultResponseContentTypeIsJson()
            throws IOException {

        //given
        String jsonMimeType = "application/json";
        HttpUriRequest request = new HttpGet("http://localhost:2137/api/tasks/1");

        //when
        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        //then

        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        Assertions.assertEquals(jsonMimeType, mimeType);
    }

    @Test
    public void givenTaskExists_whenTaskIsRetrieved_thenRetrievedResourceIsCorrect()
        throws IOException{

        //given
        HttpUriRequest request = new HttpGet("http://localhost:2137/api/tasks/1");

        //when
        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        Task resource = RetrieveUtil.retrieveResourceFromResponse(response, Task.class);

        Assertions.assertEquals("Repair PC", resource.getTitle());
    }
}
