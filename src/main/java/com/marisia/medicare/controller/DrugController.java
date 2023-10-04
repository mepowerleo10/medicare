package com.marisia.medicare.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.marisia.medicare.model.Drug;
import com.marisia.medicare.service.DrugService;

@Controller
@RequestMapping("/drugs")
public class DrugController {

  private final DrugService drugService;

  public DrugController(DrugService drugService) {
    this.drugService = drugService;
  }

  @PreAuthorize("hasRole('USER')")
  @GetMapping
  public ModelAndView getAllDrugs() {
    var mv = new ModelAndView();
    mv.setViewName("all");
    mv.getModel().put("drugs", drugService.findAll());

    return mv;
  }

  @GetMapping("/{id}")
  public ModelAndView getDrug(@PathVariable("id") Drug drug) {
    var mv = new ModelAndView();
    mv.setViewName("detail");
    mv.getModel().put("drug", drug);

    return mv;
  }
}
