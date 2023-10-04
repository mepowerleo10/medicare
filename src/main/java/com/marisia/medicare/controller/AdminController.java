package com.marisia.medicare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.marisia.medicare.model.AppUser;
import com.marisia.medicare.model.Drug;
import com.marisia.medicare.service.AppUserService;
import com.marisia.medicare.service.CartService;
import com.marisia.medicare.service.DrugService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {
  private final DrugService drugService;
  private final CartService cartService;
  private final AppUserService userService;

  public AdminController(DrugService drugService, CartService cartService, AppUserService userService) {
    this.drugService = drugService;
    this.cartService = cartService;
    this.userService = userService;
  }

  @GetMapping
  public ModelAndView home() {
    var mv = new ModelAndView();
    mv.setViewName("admin/index");
    mv.getModel().put("drugs", drugService.findAll());

    return mv;
  }

  @RequestMapping("/drugs")
  public String allDrugs(Model model, @RequestHeader(value = "HX-Request", required = false) String htmxRequestHeader,
      @RequestParam(value = "_hxm", required = false) String htmxRequestParameter) {

    model.addAttribute("drugs", drugService.findAll());

    // Check if it's an HTMX request
    boolean isHtmxRequest = htmxRequestHeader != null || htmxRequestParameter != null;
    if (isHtmxRequest) {
      return "admin/_drugs_table";
    } else {
      return "admin/index";
    }
  }

  @GetMapping("/edit-drug/{id}")
  public String editDrug(Model model, @PathVariable("id") Long id) {

    var drug = drugService.findById(id);
    if (drug != null) {
      model.addAttribute("drug", drug);
      model.addAttribute("postLocation", String.format("/admin/edit-drug/%d", id));
    } else {
      return "redirect:/admin/add-drug";
    }

    return "admin/_drug_form";
  }

  @PostMapping("/edit-drug/{id}")
  public String updateDrug(Model model,
      @PathVariable("id") Long id,
      @Valid @ModelAttribute("drug") Drug drugData,
      BindingResult result) {

    boolean hasErrors = false;

    if (drugService.drugWithNameAlreadyExists(drugData.getName(), id)) {
      result.rejectValue("name",
          "error.drug",
          "Drug with name: " + drugData.getName() + " already exists!");
      hasErrors = true;
    }

    if (result.hasErrors()) {
      hasErrors = true;
    }

    String postLocation = String.format("/admin/edit-drug/%d", id);
    if (hasErrors) {
      model.addAttribute("postLocation", postLocation);
      return "admin/_drug_form";
    }

    drugData.setId(id);

    var drug = drugService.update(drugData);
    model.addAttribute("drug", drug);
    model.addAttribute("postLocation", postLocation);

    var redirectUrl = String.format("%s?success&message=Updated", postLocation);
    return "redirect:" + redirectUrl;
  }

  @GetMapping("/add-drug")
  public ModelAndView newDrugForm() {
    var mv = new ModelAndView("admin/_drug_form.html");
    mv.addObject("postLocation", "/admin/add-drug");
    mv.addObject("drug", new Drug());
    return mv;
  }

  @PostMapping("/add-drug")
  public String addDrug(
      Model model,
      @Valid @ModelAttribute("drug") Drug drugData,
      BindingResult result) {

    boolean hasErrors = false;

    if (drugService.drugWithNameAlreadyExists(drugData.getName())) {
      result.rejectValue("name",
          "error.drug",
          "Drug with name: " + drugData.getName() + " already exists!");
      hasErrors = true;
    }

    if (result.hasErrors()) {
      hasErrors = true;
    }

    if (hasErrors) {
      model.addAttribute("postLocation", "/admin/add-drug");
      return "admin/_drug_form";
    }

    drugService.addDrug(drugData);

    return "redirect:/admin/add-drug?success&message=Saved";
  }

  @DeleteMapping("/delete-drug/{id}")
  public String deleteDrug(@PathVariable("id") Long id) {
    var drug = drugService.findById(id);

    if (drug != null) {
      drugService.delete(drug);
    }

    return "redirect:/admin/drugs";
  }

  @GetMapping("/customers")
  public String customers(Model model) {
    model.addAttribute("customers", userService.getAllCustomers());

    return "admin/customers.html";
  }

  @GetMapping("/orders")
  public String orders(Model model) {
    model.addAttribute("orders", cartService.getOrders());

    return "admin/orders.html";
  }

}
