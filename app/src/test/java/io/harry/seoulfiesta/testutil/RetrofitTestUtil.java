package io.harry.seoulfiesta.testutil;

import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class RetrofitTestUtil {

    public static <T> Response<T> createErrorResponse(String contentType, int code, String errorMessage) {
        okhttp3.Response rawResponse =
                new okhttp3.Response.Builder()
                        .request(new Request.Builder().url("http://mockserver").build())
                        .protocol(Protocol.HTTP_1_1)
                        .code(code)
                        .build();
        ResponseBody responseBody = ResponseBody.create(MediaType.parse(contentType), errorMessage);

        return Response.error(responseBody, rawResponse);
    }

}
