package com.example.indiebeauty.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.indiebeauty.domain.Category;
import com.example.indiebeauty.domain.Product;
import com.example.indiebeauty.domain.Review;
import com.example.indiebeauty.exception.NoSuchProductException;
import com.example.indiebeauty.service.CategoryService;
import com.example.indiebeauty.service.ProductService;
import com.example.indiebeauty.service.RankService;
import com.example.indiebeauty.service.ReviewService;

import jakarta.servlet.http.HttpSession;

@Controller
public class RankController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private RankService rankService;

    @GetMapping("/rank/product-detail/{productId}")
    public ModelAndView viewProductDetail(@PathVariable("productId") int productId, RedirectAttributes ra) {
        try {
            Product product = productService.getProductById(productId);
            List<Review> reviews = reviewService.getReviewsByProductId(productId);
            double averageRating = reviewService.getAverageRating(productId);

            ModelAndView mav = new ModelAndView("productDetails");
            mav.addObject("product", product);
            mav.addObject("reviews", reviews);
            mav.addObject("averageRating", averageRating);

            return mav;
        } catch (NoSuchProductException e) {
            String msg = e.getMessage();

            ra.addAttribute("msg", msg);
            ra.addAttribute("url", "/rank");

            ModelAndView mav = new ModelAndView();
            mav.setViewName("redirect:/rank/productDetail/error");

            return mav;
        }
    }

    @GetMapping("/rank/category/{categoryId}")
    @SuppressWarnings("unchecked")
    public ModelAndView viewProductByCategory(@PathVariable("categoryId") int categoryId, @RequestParam("pageNum") int pageNum, HttpSession session) {
        List<Category> categoryList = categoryService.getCategoryList();
        session.setAttribute("categoryList", categoryList);

        Map<String, Object> resultMap = productService.getProductByCategoryIdWithTitleImage(categoryId, pageNum, 9);
        List<Product> products = (List<Product>) resultMap.get("products");
        int totalPages = (int) resultMap.get("totalPages");

        ModelAndView mav = new ModelAndView("rank");
        mav.addObject("products", products);
        mav.addObject("totalPages", totalPages);
        mav.addObject("currentPage", pageNum);
        mav.addObject("currentUrl", "/rank/category/" + categoryId);

        return mav;
    }

    @GetMapping("/rank")
    @SuppressWarnings("unchecked")
    public ModelAndView viewAllProduct(HttpSession session, @RequestParam("pageNum") int pageNum) {
        List<Category> categoryList = categoryService.getCategoryList();
        session.setAttribute("categoryList", categoryList);

        Map<String, Object> resultMap = rankService.getAllProductWithTitleImage(pageNum, 9);
        List<Product> products = (List<Product>) resultMap.get("products");
        int totalPages = (int) resultMap.get("totalPages");

        ModelAndView mav = new ModelAndView("rank");
        mav.addObject("products", products);
        mav.addObject("totalPages", totalPages);
        mav.addObject("currentPage", pageNum);
        mav.addObject("currentUrl", "/rank");

        return mav;
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/rank/search")
    public ModelAndView searchProduct(@RequestParam("keyword") String keyword, @RequestParam("pageNum") int pageNum, HttpSession session) {
        List<Category> categoryList = categoryService.getCategoryList();
        session.setAttribute("categoryList", categoryList);

        Map<String, Object> resultMap = productService.getProductListByKeywordWithTitleImage(keyword, pageNum);
        List<Product> products = (List<Product>) resultMap.get("products");
        int totalPages = (int) resultMap.get("totalPages");

        ModelAndView mav = new ModelAndView("rank");
        mav.addObject("products", products);
        mav.addObject("totalPages", totalPages);
        mav.addObject("currentPage", pageNum);
        mav.addObject("currentUrl", "/rank/search?keyword=" + keyword);

        return mav;
    }

    @GetMapping("/rank/productDetail/error")
    public String alert(@ModelAttribute("msg") String msg, @ModelAttribute("url") String url) {
        return "alert";
    }
}