package ru.alinacozy.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.alinacozy.NauJava.dao.ProjectRepositoryCustom;
import ru.alinacozy.NauJava.entity.Floss;
import ru.alinacozy.NauJava.repository.FlossRepository;

@Controller
@RequestMapping("/custom/floss/view")
public class FlossControllerView {
    private final FlossRepository flossRepository;

    @Autowired
    public FlossControllerView(FlossRepository flossRepository)
    {
        this.flossRepository = flossRepository;
    }

    @GetMapping("/list")
    public String flossListView(Model model)
    {
        Iterable<Floss> products = flossRepository.findAll();
        model.addAttribute("flosses", products);
        return "flossList";
    }

}
