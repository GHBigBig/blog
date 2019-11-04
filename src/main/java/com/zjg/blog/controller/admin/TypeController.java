package com.zjg.blog.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.istack.internal.Nullable;
import com.zjg.blog.pojo.Type;
import com.zjg.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.lang.model.element.TypeParameterElement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author zjg
 */

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    //当 IOC 容器里存在多个类型兼容的 Bean 时, 通过类型的自动装配将无法工作.
    // 此时可以在 @Qualifier 注解里提供 Bean 的名称
    //Spring 允许对方法的入参标注 @Qualifiter 已指定注入 Bean 的名称, 多个类型的
    //通过注解装载的类，名称默认是类名加小写
    @Qualifier(value = "typeServiceImpl")
    private TypeService typeService;

    /**
     * 这里的 @GetMapping 中需要放入多个地址，因为 pageNum 或 pageSize 不存在是多个地址
     * types：请求首页的数据，条目为 5 行
     * types/{pageNum}：请求的页码，默认的5行条目
     * types/{pageNum}/{pageSize}：请求的页码，请求的条目
     * types/{pageNum}/{pageSize}/{navigatePages}：请求的页码，请求的条目，分页导航页数
     *
     * @param pageNum 页码
     * @param pageSize 显示的条数
     * @param navigatePages 显示的页码导航数
     * @param modelAndView

     * @return
     */
    @GetMapping(value = {"/types/{pageNum}/{pageSize}/{navigatePages}",
            "/types/{pageNum}/{pageSize}","/types/{pageNum}","/types"})
    public ModelAndView types(@PathVariable(name = "pageNum", required = false) Integer pageNum,
                              @PathVariable(name = "pageSize", required = false) Integer pageSize,
                              @PathVariable(name = "navigatePages", required = false) Integer navigatePages,
                              ModelAndView modelAndView) {
        //用户没有填写，系统补上
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 5;
        }
        //如果没有指定分页导航页码，就 navigatePages 为 5
        if (navigatePages == null) {
            navigatePages = 5;
        }
        PageInfo pageInfo = typeService.findTypeAll(pageNum, pageSize, navigatePages);

        modelAndView.addObject("pageInfo", pageInfo);
        modelAndView.setViewName("admin/types");
        return modelAndView;
    }

    /**
     * 新增页面
     * @param model 为了前端的 thymeleaf 提示消息可以拿到值
     * @return
     */
    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type", new Type());
        return "admin/types-input";
    }

    /**
     * 新增 type 请求
     * @param type
     * @param attributes 用于重定向中设置属性的
     * @return
     */

    @PostMapping("/types/input")
    public String addType(@Valid Type type, BindingResult bindingResult, RedirectAttributes attributes) {
        //检查 typeName 是否已经存在
        if (typeService.findTypeByTypeName(type.getTypeName()) != null) {
            // field 要和 types-input 页面里显示错误信息的名字相同
            bindingResult.rejectValue("typeName", "typeNameError", "此类型名已存在");
        }

        if (bindingResult.getErrorCount()>0) {
            //发生错误，返回到 input 页面，错误回显
            return "admin/types-input";
        }

        type = typeService.saveType(type);
        //通过 message 返回，操作结果信息
        if (type.getTypeId() > 0) {
            attributes.addFlashAttribute("message","新增成功");
        }else {
            attributes.addFlashAttribute("message","新增失败");
        }
        return "redirect:/admin/types";
    }

    /**
     * 修改页面
     * @param typeId 要修该的 type 的 typeId
     * @param model 放入修改的 type
     */
    @GetMapping("/types/{id}/edit")
    public String edit(@PathVariable("id") int typeId, Model model){
        model.addAttribute("type", typeService.findTypeByTypeId(typeId));

        return "admin/types-input";
    }

    @PutMapping("/types/edit")
    public String editType(@Valid Type type, BindingResult bindingResult, RedirectAttributes attributes) {
        //检查 typeName 是否已经存在
        if (typeService.findTypeByTypeName(type.getTypeName()) != null) {
            // field 要和 types-input 页面里显示错误信息的名字相同
            bindingResult.rejectValue("typeName", "typeNameError", "此类型名已存在");
        }

        if (bindingResult.getErrorCount()>0) {
            //发生错误，返回到 input 页面，错误回显
            return "admin/types-input";
        }

        Type type1 = typeService.modifyType(type);
        //通过 message 返回，操作结果信息
        if (type1 != null) {
            attributes.addFlashAttribute("message","修改成功");
        }else {
            attributes.addFlashAttribute("message","修改失败");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable("id") Long typeId, RedirectAttributes attributes, HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        if (typeService.removeTypeByTypeId(typeId)>0) {
            attributes.addFlashAttribute("message","删除成功");
        } else {
            attributes.addFlashAttribute("message","删除失败");
        }

        return "redirect:/admin/types";
    }


}
