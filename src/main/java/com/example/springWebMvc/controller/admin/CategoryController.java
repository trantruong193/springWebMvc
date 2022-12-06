package com.example.springWebMvc.controller.admin;

import com.example.springWebMvc.persistent.dto.CategoryDTO;
import com.example.springWebMvc.persistent.entities.Category;
import com.example.springWebMvc.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("admin/categories")
// http://localhost:8080/admin/categories
    public class CategoryController {
    private final CategoryService categoryService;
    private final List<String> list = Arrays.asList("catId,asc","catName,asc","catId,desc","catName,desc");
    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @RequestMapping("")
    public String loadAll (Model model,@RequestParam(name = "sort",required = false) String sortType,
            @PageableDefault(size = 5,sort = "catId",direction = Sort.Direction.ASC) Pageable pageable){
        if (!list.contains(sortType)){
            pageable = PageRequest.of(0,10, Sort.Direction.ASC,"catId");
        }
        Page<Category> pages = categoryService.getAll(pageable);
        List<Sort.Order> orderList = pages.getSort().stream().toList();
        if (orderList.size()>0){
            Sort.Order order = orderList.get(0);
            model.addAttribute("sort",
                    (order.getProperty() + "," + (order.getDirection() == Sort.Direction.ASC?"asc":"desc")));
        }else {
            model.addAttribute("sort","catId,asc");
        }
        model.addAttribute("pages",pages);
        return "admin/fragment/categories/list";
    }
    @GetMapping("add")
    public String add (Model model){
        model.addAttribute("category",new CategoryDTO());
        return "admin/fragment/categories/add";
    }
    @PostMapping("save")
    public ModelAndView save(ModelMap model,
                             @Valid @ModelAttribute("category") CategoryDTO categoryDTO,
                             BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return new ModelAndView("admin/fragment/categories/add",model);
        }
        if(categoryDTO.getCatId() == null){
            List<Category> categories = categoryService.getAllCategories();
            for(Category category : categories){
                if (category.getCatName().equalsIgnoreCase(categoryDTO.getCatName())){
                    model.addAttribute("message","Danh mục đã tồn tại");
                    return new ModelAndView("admin/fragment/categories/add",model);
                }
            }
        }
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        categoryService.updateCategory(category);
        model.addAttribute("message","Category id saved!");
        return new ModelAndView("forward:/admin/categories",model);
    }
    @GetMapping("edit/{catId}")
    public String edit (Model model,
                        @PathVariable("catId") Long catId)
    {
        Category foundCategory = categoryService.findById(catId);
        CategoryDTO categoryDto = new CategoryDTO();
        if (foundCategory != null){
            BeanUtils.copyProperties(foundCategory,categoryDto);
        }else {
            model.addAttribute("message","Không tìm thấy danh mục");
        }
        model.addAttribute("category",categoryDto);
        return "admin/fragment/categories/add";
    }
    @GetMapping("sort")
    public String sort (Model model,@RequestParam(name = "sort",required = false) String sortType,
                        @PageableDefault(size = 5,sort = "sort",direction = Sort.Direction.ASC)Pageable pageable){
        if (!list.contains(sortType)){
            pageable = PageRequest.of(0,5, Sort.Direction.ASC,"catId");
            sortType = "catId,asc";
        }
        Page<Category> pages = categoryService.getAll(pageable);
        model.addAttribute("pages",pages);
        model.addAttribute("sort",sortType);
        model.addAttribute("message","Sort by "+sortType);
        return "admin/fragment/categories/list";
    }
}
