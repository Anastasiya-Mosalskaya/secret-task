package org.epam.secrettask.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class SecurityController {

    private final Map<String, String> secretsMap = new HashMap<>();

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
        secretsMap.put(uniqueId, secretText);
        model.addAttribute("secretLink", link);
        return "secretLink";
    }

    @GetMapping("/secretLink/{secretLink}.html")
    public String getSecretText(Model model, @PathVariable String secretLink) {
        if (!secretsMap.containsKey(secretLink)) {
            return "error";
        }
        String secretText = secretsMap.get(secretLink);
        model.addAttribute("secretText", secretText);
        secretsMap.remove(secretLink);
        return "secretText";
    }
}
