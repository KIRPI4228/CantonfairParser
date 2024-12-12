package ru.KIRPI4.chinaparser.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.KIRPI4.chinaparser.EncryptionManager;
import ru.KIRPI4.chinaparser.http.models.*;

import javax.script.ScriptException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

public final class Client {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    public static ImageCheckModel getImageCheckData() throws URISyntaxException, IOException, InterruptedException, ExecutionException {
        return send(HttpRequest.newBuilder(new URI("https://cfapi.cantonfair.org.cn/uac/slider/getImageCode")).GET().build(), ImageCheckModel.class);
    }

    public static RsaPublicKeyModel getRsaPublicKey() throws URISyntaxException, IOException, InterruptedException, ExecutionException {
        return send(HttpRequest.newBuilder(new URI("https://cfapi.cantonfair.org.cn/uac/secret/getRsaPublicKey")).GET().build(), RsaPublicKeyModel.class);
    }

    public static ResultImageCheckModel submitImageCheck(int blockX, String sliderKey) throws URISyntaxException, IOException, InterruptedException, ExecutionException {
        return send(HttpRequest.newBuilder(new URI("https://cfapi.cantonfair.org.cn/uac/slider/checkImageCode"))
                .method("POST",HttpRequest.BodyPublishers.ofString("{\"sliderKey\":\"" + sliderKey + "\",\"blockX\":" + blockX + "}"))
                .header("Content-Type", "application/json")
                .build(), ResultImageCheckModel.class);
    }

    public static ResultAuthenticationModel submitAuthentication(String email, String password, String sliderKey, String publicKey) throws URISyntaxException, IOException, InterruptedException, ScriptException, NoSuchMethodException, ExecutionException {
        return send(HttpRequest.newBuilder(new URI("https://cfapi.cantonfair.org.cn/uac/sso/getAuthorCode"))
                .POST(HttpRequest.BodyPublishers.ofString("{\"userAuthenticationEncrypt\":\"" + EncryptionManager.encryptAuthenticationParameters(publicKey, email, password, sliderKey) + "\",\"sourceType\":\"OFFICIAL-WEBSITE\",\"sourceDetailCode\":\"\"}"))
                .header("Content-Type", "application/json")
                .build(), ResultAuthenticationModel.class);
    }

    public static ResultLoginModel login(String code) throws URISyntaxException, IOException, InterruptedException, ScriptException, NoSuchMethodException, ExecutionException {
        return send(HttpRequest.newBuilder(new URI("https://www.cantonfair.org.cn/public/api/o2oOverseaBuyer/login"))
                .POST(HttpRequest.BodyPublishers.ofString("{\"bestCode\":\"" + code + "\"}"))
                .header("Content-Type", "application/json")
                .build(), ResultLoginModel.class);
    }

    private static <T> T send(HttpRequest request, Class<T> clazz) throws IOException, InterruptedException, ExecutionException {
        HttpResponse<String> response = HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get();

        return objectMapper.readValue(response.body(), clazz);
    }

    private static <T> T send(HttpRequest request, Class<T> clazz, String subDirectory) throws IOException, InterruptedException {
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readValue(response.body(), clazz);
    }
}
