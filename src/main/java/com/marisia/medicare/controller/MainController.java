package com.marisia.medicare.controller;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.marisia.medicare.model.Payment;
import com.marisia.medicare.service.AppUserService;
import com.marisia.medicare.service.CartService;
import com.marisia.medicare.service.DrugService;
import com.marisia.medicare.service.payment.PaymentService;

import jakarta.validation.Valid;

@Controller
public class MainController {
  @Autowired
  final AppUserService userService;

  @Autowired
  final DrugService drugService;

  @Autowired
  final CartService cartService;

  @Autowired
  final PaymentService paymentService;

  public MainController(
      AppUserService userService,
      DrugService drugService,
      CartService cartService,
      PaymentService paymentService) {
    this.userService = userService;
    this.drugService = drugService;
    this.cartService = cartService;
    this.paymentService = paymentService;
  }

  @GetMapping("/")
  public String index(Model model) {

    model.addAttribute("drugs", drugService.findAllEnabled());
    model.addAttribute("cart", cartService.getActiveCartForUser(userService.getCurrentUser()));
    return "index";
  }

  @PostMapping("/add-to-cart/{drugId}")
  public String addToCart(Model model, @PathVariable("drugId") Long drugId) {

    var cart = cartService.getActiveCartForUser(userService.getCurrentUser());
    var drug = drugService.findById(drugId);

    if (drug != null && drug.inStock()) {
      cart.addToCart(drug);
      cart = cartService.save(cart);
    }

    model.addAttribute("cart", cart);
    return "_cart_button";
  }

  @GetMapping("/checkout")
  public String getCheckout(Model model) {
    var currentUser = userService.getCurrentUser();
    var cart = cartService.getActiveCartForUser(
        currentUser);
    model.addAttribute(
        "cart",
        cart);

    var payment = new Payment();
    payment.setAccount(currentUser.getAccount());
    model.addAttribute("payment", payment);

    return "checkout";
  }

  @PostMapping("/checkout")
  public String checkout(
      Model model,
      @Valid @ModelAttribute("payment") Payment payment,
      BindingResult result) {

    var currentUser = userService.getCurrentUser();
    var cart = cartService.getActiveCartForUser(
        currentUser);
    model.addAttribute(
        "cart",
        cart);

    model.addAttribute("payment", payment);

    if (result.hasErrors()) {
      return "checkout";
    }

    payment.setCart(cart);
    payment.setAmount(cart.getTotalPrice().floatValue());
    payment.setFrom(currentUser);
    payment.setTo(userService.getSystemAsUser());

    payment = paymentService.make(payment);

    if (payment.getComplete()) {
      cart.setIsCheckedOut(true);
      cart.setCheckedOutAt(Calendar.getInstance().getTime());
      cart.setPayment(payment);
      cartService.save(cart);
    }

    return "redirect:/?message=Checked Out Successfully";
  }
}
