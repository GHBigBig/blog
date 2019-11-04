package com.zjg.blog.controller.admin;

import com.github.pagehelper.PageInfo;
import com.zjg.blog.pojo.Tag;
import com.zjg.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author zjg
 */

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagservice;

    /**
     * 这里的 @GetMapping 中需要放入多个地址，因为 pageNum 或 pageSize 不存在是多个地址
     * tags：请求首页的数据，条目为 5 行
     * tags/{pageNum}：请求的页码，默认的5行条目
     * tags/{pageNum}/{pageSize}：请求的页码，请求的条目
     * tags/{pageNum}/{pageSize}/{navigatePages}：请求的页码，请求的条目，分页导航页数
     *
     * @param pageNum 页码
     * @param pageSize 显示的条数
     * @param navigatePages 显示的页码导航数
     * @param modelAndView

     * @return
     */
    @GetMapping(value = {"/tags/{pageNum}/{pageSize}/{navigatePages}",
            "/tags/{pageNum}/{pageSize}","/tags/{pageNum}","/tags"})
    public ModelAndView tags(@PathVariable(name = "pageNum", required = false) Integer pageNum,
                              @PathVariable(name = "pageSize", required = false) Integer pageSize,
                              @PathVariable(name = "navigatePages", required = false) Integer navigatePages,
                              ModelAndView modelAndView) {
        //用户没有填写，采用系统默认的
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
        PageInfo pageInfo = tagservice.findTagAll(pageNum, pageSize, navigatePages);

        modelAndView.addObject("pageInfo", pageInfo);
        modelAndView.setViewName("admin/tags");
        return modelAndView;
    }

    /**
     * 新增页面
     * @param model 为了前端的 thymeleaf 提示消息可以拿到值
     * @return
     */
    @GetMapping("/tags/input")
    public String input(Model model){
        model.addAttribute("tag", new Tag());
        return "admin/tags-input";
    }

    /**
     * 新增 tag 请求
     * @param tag
     * @param attributes 用于重定向中设置属性的
     * @return
     */

    @PostMapping("/tags/input")
    public String addTag(@Valid Tag tag, BindingResult bindingResult, RedirectAttributes attributes) {
        //检查 tagName 是否已经存在
        if (tagservice.findTagByTagName(tag.getTagName()) != null) {
            // field 要和 tags-input 页面里显示错误信息的名字相同
            bindingResult.rejectValue("tagName", "tagNameError", "此标签名已存在");
        }

        if (bindingResult.getErrorCount()>0) {
            //发生错误，返回到 input 页面，错误回显
            return "admin/tags-input";
        }

        tag = tagservice.saveTag(tag);
        //通过 message 返回，操作结果信息
        if (tag.getTagId() > 0) {
            attributes.addFlashAttribute("message","新增成功");
        }else {
            attributes.addFlashAttribute("message","新增失败");
        }
        return "redirect:/admin/tags";
    }

    /**
     * 修改页面
     * @param tagId 要修该的 tag 的 tagId
     * @param model 放入修改的 tag
     */
    @GetMapping("/tags/{id}/edit")
    public String edit(@PathVariable("id") int tagId, Model model){
        model.addAttribute("tag", tagservice.findTagByTagId(tagId));

        return "admin/tags-input";
    }

    @PutMapping("/tags/edit")
    public String editTag(@Valid Tag tag, BindingResult bindingResult, RedirectAttributes attributes) {
        //检查 tagName 是否已经存在
        if (tagservice.findTagByTagName(tag.getTagName()) != null) {
            // field 要和 tags-input 页面里显示错误信息的名字相同
            bindingResult.rejectValue("tagName", "tagNameError", "此标签名已存在");
        }

        if (bindingResult.getErrorCount()>0) {
            //发生错误，返回到 input 页面，错误回显
            return "admin/tags-input";
        }

        tag = tagservice.modifyTagByTagId(tag);
        //通过 message 返回，操作结果信息
        if (tag != null) {
            attributes.addFlashAttribute("message","修改成功");
        }else {
            attributes.addFlashAttribute("message","修改失败");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable("id") Long tagId, RedirectAttributes attributes, HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        if (tagservice.removeTagByTagId(tagId) >  0) {
            attributes.addFlashAttribute("message","删除成功");
        } else {
            attributes.addFlashAttribute("message","删除失败");
        }

        return "redirect:/admin/tags";
    }


}
