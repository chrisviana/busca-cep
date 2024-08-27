package br.com.cep;

import com.google.gson.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class index {

    public static void main(String[] args) throws IOException, InterruptedException {

        Scanner read = new Scanner(System.in);
        String cep = "";

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .create();

        while (!cep.equalsIgnoreCase("sair")){
            System.out.println("Digite um CEP: ");
            cep = read.nextLine();

            String url = "https://viacep.com.br/ws/" + cep + "/json/";

            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .build();
                HttpResponse<String> response = client
                        .send(request, HttpResponse.BodyHandlers.ofString());

                String json = response.body();

                JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
                if (jsonObject.has("erro") && jsonObject.get("erro").getAsBoolean()) {
                    System.out.println("CEP inválido ou não encontrado.");
                } else {
                    System.out.println(gson.toJson(jsonObject));
                }
            } catch (Exception e) {
                throw new RuntimeException("Não foi possível obter o endereço");
            }
        }
    }
}
