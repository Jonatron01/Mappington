package com.Jonatron01.Mappington.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
 
import com.Jonatron01.Mappington.model.Pixel;
import com.Jonatron01.Mappington.repository.PixelRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter; 

@Controller
public class WebSocketController {

    private final PixelRepository pixelRepository;
    @Autowired
    private SimpMessagingTemplate simpMsg;
    @Autowired
    public WebSocketController(PixelRepository pixelRepository) {
        this.pixelRepository = pixelRepository;
    }
    @MessageMapping("/pixel")
    @SendTo("/update/pixels")
    public String pixelChange(Pixel px) throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        System.out.println("trolled");
        pixelRepository.findById(px.getId())
            .map(Pixel -> {
                Pixel.setColor(px.getColor());
                Pixel.setAuthor(px.getAuthor());
                return pixelRepository.save(Pixel);
            })
            .orElseGet(() -> {
                return pixelRepository.save(px);
            });
        System.out.println(ow.writeValueAsString(px));
        return ow.writeValueAsString(px);
    }
    @MessageMapping("/reload")
    public void reloadMap(SimpMessageHeaderAccessor smha) throws Exception {
        String sessionId = smha.getSessionId();   
        System.out.println(sessionId);
        simpMsg.convertAndSend("/update/reload/" + sessionId, pixelRepository.findAll());
    }
}