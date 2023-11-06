package org.epam.secrettask.controller;

import org.epam.secrettask.entity.Secret;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class SecurityController {

    private final Map<String, Secret> secretsMap = new HashMap<>();

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/generateLink")
    public String generateLink() {
        return "generateLink";
    }

    @PostMapping("/secretLink")
    public String getSecretLink(Model model, @RequestParam String secretText) {
        String uniqueId = UUID.randomUUID().toString().replaceAll("-", "");
        String link = "secretLink/" + uniqueId + ".html";
        secretsMap.put(uniqueId, new Secret(secretText));
        model.addAttribute("secretLink", link);
        return "secretLink";
    }

    @GetMapping("/secretLink/{secretLink}.html")
    public String getSecretText(Model model, @PathVariable String secretLink) {
        if (!secretsMap.containsKey(secretLink)) {
            return "error";
        }
        String secretText = secretsMap.get(secretLink).getSecretText();
        model.addAttribute("secretText", secretText);
        secretsMap.remove(secretLink);
        return "secretText";
    }

    @Scheduled(fixedRate = 10000)
    public void cleanUpSecretsStorage() {
        secretsMap.entrySet().removeIf(entry -> isOlderThanFiveMinutes(entry.getValue().getTimestamp()));
    }

    private boolean isOlderThanFiveMinutes(long timestamp) {
        Instant instant = Instant.ofEpochSecond(timestamp);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime currentDateMinusFiveMinutes = currentDate.minusMinutes(5);
        return localDateTime.isBefore(currentDateMinusFiveMinutes);
    }
}
