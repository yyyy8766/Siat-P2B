package com.example.p2b.controller;

import com.example.p2b.dto.MemberDTO;
import com.example.p2b.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    // 생성자 주입
    private final MemberService memberService;

    // 회원가입 페이지 출력 요청
    @GetMapping("/join")
    public String joinForm() {
        return "join";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute MemberDTO memberDTO) {
        System.out.println("MemberController.join");
        System.out.println("memberDTO = " + memberDTO);
        memberService.join(memberDTO);
        return "login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/main")
    public String mainPage(HttpSession session, Model model) {
        String sessionId = session.getId();
        model.addAttribute("sessionId", sessionId);
        return "main";
    }




//    @GetMapping("/member/")
//    public String findAll(Model model) {
//        List<MemberDTO> memberDTOList = memberService.findAll();
//        // 어떠한 html로 가져갈 데이터가 있다면 model사용
//        model.addAttribute("memberList", memberDTOList);
//        return "list";
//    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "detail";
    }

    @GetMapping("/update")
    public String updateForm(HttpSession session, Model model) {
        String myEmail = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.updateForm(myEmail);
        model.addAttribute("updateMember", memberDTO);
        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute MemberDTO memberDTO) {
        memberService.update(memberDTO);
        return "redirect:/member/" + memberDTO.getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        memberService.deleteById(id);
        return "redirect:/member/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    @PostMapping("/id-check")
    public @ResponseBody String idCheck(@RequestParam("memberId") String memberId) {
        System.out.println("memberId = " + memberId);
        String checkResult = memberService.idCheck(memberId);
        return checkResult;
//        if (checkResult != null) {
//            return "ok";
//        } else {
//            return "no";
//        }
    }

    @PostMapping("/email-check")
    public @ResponseBody String emailCheck(@RequestParam("memberEmail") String memberEmail) {
        System.out.println("memberEmail = " + memberEmail);
        String checkResult = memberService.emailCheck(memberEmail);
        return checkResult;
//        if (checkResult != null) {
//            return "ok";
//        } else {
//            return "no";
//        }
    }

    @GetMapping("/find-id")
    public String findIdForm() {
        return "findId";
    }

    @PostMapping("/find-id")
    public String findId(@RequestParam("memberEmail") String memberEmail, Model model) {
        String memberId = memberService.findIdByEmail(memberEmail);
        model.addAttribute("foundId", memberId);
        return "findIdResult";
    }

    @GetMapping("/find-password")
    public String findPasswordForm() {
        return "findPassword";
    }

    @PostMapping("/find-password")
    public String findPassword(@RequestParam("memberId") String memberId, @RequestParam("memberEmail") String memberEmail, Model model) {
        boolean isFound = memberService.checkMemberIdAndEmail(memberId, memberEmail);
        if (isFound) {
            model.addAttribute("memberId", memberId);
            return "changePassword";
        } else {
            return "findPassword";
        }
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("memberId") String memberId, @RequestParam("newPassword") String newPassword) {
        memberService.changePassword(memberId, newPassword);
        return "passwordChanged";
    }
}