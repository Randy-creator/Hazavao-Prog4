package hei.school.hazavao.service;

import java.util.List;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatGPTService {

  private static final String API_URL = "https://api.openai.com/v1/chat/completions";
  private static final String API_KEY = "api key ici";

  private final RestTemplate restTemplate = new RestTemplate();

  public String getDefinition(String word) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    headers.setBearerAuth(API_KEY);

    Map<String, Object> userMessage =
        Map.of(
            "role",
            "user",
            "content",
            "Donne-moi une définition du mot et traduis la definition en malagasy : " + word);

    Map<String, Object> requestBody =
        Map.of("model", "gpt-3.5-turbo", "messages", List.of(userMessage), "max_tokens", 100);

    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

    try {
      ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, requestEntity, Map.class);

      if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
        List<Map<String, Object>> choices =
            (List<Map<String, Object>>) response.getBody().get("choices");
        if (choices != null && !choices.isEmpty()) {
          Map<String, Object> messageContent = (Map<String, Object>) choices.get(0).get("message");
          if (messageContent != null) {
            return (String) messageContent.get("content");
          }
        }
      }
      return "Aucune réponse reçue du service.";
    } catch (Exception e) {
      e.printStackTrace();
      return "Erreur lors de la récupération de la définition.";
    }
  }
}
