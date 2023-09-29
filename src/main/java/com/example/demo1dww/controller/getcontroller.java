package com.example.demo1dww.controller;

import com.example.demo1dww.domain.TeamDTO;
import com.example.demo1dww.domain.TeamchatDTO;
import com.example.demo1dww.domain.UserDTO;
import com.example.demo1dww.service.DService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.script.ScriptContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Console;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class getcontroller {
    private static final Logger log=
            LoggerFactory.getLogger(getcontroller.class);

    @Autowired
    private DService service;

    @Autowired
    private JavaMailSender mailSender;



    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {

        model.addAttribute("ck" ,service.ck());
        log.info("cnt =" + service.ck());
        return "greeting";
    }
    @GetMapping("/")
    public String Index(){

        return "index";
    }

    @GetMapping("/singup")
    public void Singup(){

    }

    @ResponseBody
    @PostMapping(value = "user_idChk" ,  produces = "application/text; charset=UTF-8" )
    public String user_idCh(@RequestBody String user_id) {
        log.info("user_idCh( call........"+user_id);
        String[] parts = user_id.split("=");
        String username = parts[0]; // user_id

        String user_value = parts[1];//user_idの値
        String user_id_ok = String.valueOf(service.User_Chk(username , user_value));
        return user_id_ok;
    }

    @ResponseBody
    @PostMapping(value = "niknameChk" ,  produces = "application/text; charset=UTF-8" )
    public String user_nameCh(@RequestBody String nikname) throws UnsupportedEncodingException {
        String[] parts = nikname.split("=");

        String username = parts[0]; //user_name

        String user_value_decoded = parts[1]; //user_nameの値
        String user_value = URLDecoder.decode(user_value_decoded, "UTF-8");
        //log.info("user_idCh( call........" +user_value);
        //log.info("user_idCh( call........" +username);

        String user_name_ok = String.valueOf(service.User_Chk(username , user_value));
        return user_name_ok;
    }

    @ResponseBody
    @GetMapping(value="user_mailck")
    public String user_mailCh(@RequestParam("user_mail") String email) {
        log.info("user_mailCh( call........"+email);
        //認証番号の作成
        Random random = new Random();
        int checkNum = random.nextInt(888888)+111111;
       //log.info("認証番号" + checkNum);
        //이메일 보내기
        String setFrom = "cmg97077@gmail.com"; //
        String toMail = email;// 受信メール
        String title = "会員登録の認証メールです。"; //タイトル.
        String content =
                "認証番号は " + checkNum + "です。";
        //メール転送コード
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(setFrom);
            message.setTo(toMail);
            message.setSubject(title);
            message.setText(content);
            mailSender.send(message);
        }catch(Exception e) {
            e.printStackTrace();
        }
        String num = Integer.toString(checkNum);

        return num;
    }

    @PostMapping("/singup")
    public String Psingup(UserDTO dto){

        log.info(dto.getUser_id());
        log.info(dto.getNikname());
        log.info(dto.getUser_pass());
        log.info(dto.getEmail());

        service.User_Insert(dto);

        return "/index";
    }

    @PostMapping("/login")
    public String Login(UserDTO dto, HttpServletRequest request,  Model model){

        log.info(dto.getUser_id());
        log.info(dto.getUser_pass());

        int ck = service.Login_CK(dto);
        log.info(String.valueOf(ck));
        if(ck == 0){
            return "/loginf";
        }

        UserDTO dto1 = service.User_Select(dto.getUser_id());
        byte[] imageData = dto1.getP_img();
        if (imageData != null) {
            String base64Image = Base64.getEncoder().encodeToString(imageData);
            model.addAttribute("base64Image", base64Image);
        }

        log.info(dto1.getNikname());
        HttpSession session = request.getSession();// セッション生成
        session.setAttribute("user", dto1);
        session.setMaxInactiveInterval(500);//セッション維持時間500秒
        model.addAttribute("user" ,session );

        List<TeamDTO>list = service.Team_Select(dto1.getUser_id());
        model.addAttribute("my_id" ,dto1.getUser_id() );
        model.addAttribute("team" , list);

        return "/loginindex";
    }
    @GetMapping("/login")
    public String GLogin(HttpServletRequest request, Model model, @RequestParam("id") String user_id){

        UserDTO dto1 = service.User_Select(user_id);

        byte[] imageData = dto1.getP_img();
        if (imageData != null) {
            String base64Image = Base64.getEncoder().encodeToString(imageData);
            model.addAttribute("base64Image", base64Image);
        }

        HttpSession session = request.getSession();// セッション生成
        session.setAttribute("user", dto1);
        session.setMaxInactiveInterval(500);//セッション維持時間500秒
        model.addAttribute("user" ,session );

        List<TeamDTO>list = service.Team_Select(dto1.getUser_id());
        model.addAttribute("my_id" ,dto1.getUser_id() );
        model.addAttribute("team" , list);

        return "/loginindex";
    }

    @GetMapping("/passsw")
    public void Passsw(){

    }

    @PostMapping("/passemail")
    public String Passemail(@RequestParam("email") String email){

        log.info("email = " + email);
        int mailck = service.Email_Ck(email);

        if(mailck == 0){
            return "/Message";
        }

        //認証番号の作成
        Random random = new Random();
        int checkNum = random.nextInt(888888888)+111111111;
        //log.info("認証番号" + checkNum);
        //이메일 보내기
        String setFrom = "cmg97077@gmail.com"; //
        String toMail = email;// 受信メール
        String title = "変えたパスワードです。"; //タイトル.
        String content =
                "パスワードは " + checkNum + "です。";
        //メール転送コード
        String user_pass = String.valueOf(checkNum);
        log.info(user_pass );
        log.info(email);
        service.Passsw(user_pass , email);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(setFrom);
            message.setTo(toMail);
            message.setSubject(title);
            message.setText(content);
            mailSender.send(message);
        }catch(Exception e) {
            e.printStackTrace();
        }

        return "/loginf";
    }

    @GetMapping("/pop")
    public void Pop(){

    }

    @GetMapping("/logout")
    public String Logout(HttpServletRequest request){
        //세션을 삭제
        HttpSession session = request.getSession(false);
        // session이 null이 아니라는건 기존에 세션이 존재했었다는 뜻이므로
        // 세션이 null이 아니라면 session.invalidate()로 세션 삭제해주기.
        if(session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    @GetMapping("/gpop")
    public String Gpop(HttpServletRequest request, Model model){

        // 세션이 없으면 index
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "/index";
        }

        UserDTO dto = (UserDTO) session.getAttribute("user");


        // 세션에 회원 데이터가 없으면 home
        if (dto == null) {
            return "index";
        }
        log.info("user_id="+dto.getUser_id());
        model.addAttribute("user" , dto);

        return "/gpop";
    }

    @ResponseBody
    @PostMapping("/gpopok")
    public String Gpopko(@RequestBody Map<String, Object> param) {

        String teamname = (String) param.get("teamname");
        List<String> emailList = (List<String>) param.get("emaillist");
        String myemail = (String) param.get("myemail");

        log.info("myemail = " + myemail);
        log.info("teamname =" + teamname);
        log.info("emailListsize = " + emailList.size());

        //그룹생성
        service.Team_Insert(teamname,myemail);
        UserDTO lsdto = service.Leader_Select(myemail);
        service.Member_Insert(lsdto);
        int teamno = service.Seq_Select();


       for(int i=0; i<emailList.size() ; i++){
           log.info("email =" + emailList.get(i));
           UserDTO dto = service.Mail_User(emailList.get(i));
           String invitationLink = "http://localhost:8081/invitation?userId=" + dto.getUser_id() +
                   "&password=" + dto.getUser_pass() +
                   "&teamno=" + teamno +
                   "&nikname=" + dto.getNikname();

           //이메일 보내기
           String setFrom = myemail; //
           String toMail = emailList.get(i);// 受信メール
           String title = "招待です。"; //タイトル.
           String content =
                   "リンクは " + invitationLink + " です。";

           SimpleMailMessage message = new SimpleMailMessage();
           message.setFrom(setFrom);
           message.setTo(toMail);
           message.setSubject(title);
           message.setText(content);
           mailSender.send(message);

       }

        return "Success";
}
    @GetMapping("/invitation")
    public String Invitation(@RequestParam("userId") String userId,@RequestParam("password") String password,
                             @RequestParam("teamno") int teamno,@RequestParam("nikname") String nikname
    ,HttpServletRequest request,  Model model){



        UserDTO dto = service.User_Select(userId);
        service.Member_Insert_no(teamno,userId,nikname);
        byte[] imageData = dto.getP_img();
        if (imageData != null) {
            String base64Image = Base64.getEncoder().encodeToString(imageData);
            model.addAttribute("base64Image", base64Image);
        }
        HttpSession session = request.getSession();// セッション生成
        session.setAttribute("user", dto);
        session.setMaxInactiveInterval(500);//セッション維持時間500秒
        model.addAttribute("user" ,session );
        List<TeamDTO>list = service.Team_Select(dto.getUser_id());
        model.addAttribute("my_id" ,dto.getUser_id() );
        model.addAttribute("team" , list);

        return "/loginindex";
    }

    @ResponseBody
    @PostMapping("/newcalendar")
    public  HashMap<String, Object> Newcalendar(@RequestBody Map<String, Object> param , Model model) {


        String teamname = (String) param.get("teamname");
        String Stringteamno = (String) param.get("teamno");
        log.info(Stringteamno);
        int teamno = Integer.parseInt(Stringteamno);

        List<TeamDTO> memberlist = service.Member_List(teamno);
        List<TeamDTO> memberwork = service.Work_Select(teamno);



        HashMap<String , Object> response  = new HashMap<>();



        response.put("memberwork" , memberwork); //  중복 포함 모든 정보
        response.put("memberlist" , memberlist); // 중복제거

        return response;
    }

    @ResponseBody
    @PostMapping("/workinsert")
    public String WorkInsert(@RequestBody Map<String, Object> param){
        int teamno = (int) param.get("teamno");
        String nikname = (String)param.get("member");
        String work = (String)param.get("title");
        String start_date = (String)param.get("start");
        String end_date = (String)param.get("end");
        String user_id = (String)param.get("user_id");


        TeamDTO dto = new TeamDTO();
        dto.setTeamno(teamno);
        dto.setNikname(nikname);
        dto.setWork(work);
        dto.setStart_date(start_date);
        dto.setEnd_date(end_date);
        dto.setUser_id(user_id);

        service.Work_Insert(dto);

        return "ok";
    }


    @GetMapping("/teamindex")
    public String Teamindex(@RequestParam String Startdate, @RequestParam int teamno, @RequestParam String my_id,
                            Model model) {

        UserDTO dto = service.User_Select(my_id);
        //List<TeamDTO> memberlist = service.Member_List(teamno); //  중복 포함 모든 정보
        List<TeamDTO> memberwork = service.Work_Select(teamno); // id중복 제거

        List<TeamDTO>list = service.Team_Select(my_id);// 내가 속해있는 팀
        List<TeamchatDTO> tc = service.TeamChat_Select(teamno);
        List<TeamDTO>day_work = service.Teamtoday_Work(teamno , Startdate);

        TeamDTO tdto = service.My_team(teamno);

        byte[] imageData = dto.getP_img();
        if (imageData != null) {
            String base64Image = Base64.getEncoder().encodeToString(imageData);
            model.addAttribute("base64Image", base64Image);
        }
        model.addAttribute("userpass", dto.getUser_pass());
        model.addAttribute("teamno" , teamno);
        model.addAttribute("work" , day_work); // 당일 업무
        model.addAttribute("my_id" , my_id ); //유저아이디
        model.addAttribute("user" , dto ); //유저정보
        model.addAttribute("team" , list); //속해있는 팀 리스트
        model.addAttribute("memberwork", memberwork); //id중복 제거 멤버정보
        model.addAttribute("memberchat" , tc); //채팅
        model.addAttribute("fulldate", Startdate);
        model.addAttribute("myteam" , tdto); //현재 채팅방 팀이름/번호/리더
        return "teamindex";
    }

    @ResponseBody
    @PostMapping("teamchatdb")
    public String receiveData(@RequestBody Map<String, Object> param) {

        String id= (String) param.get("id");
        String nikname = (String)param.get("nikname");
        String content = (String) param.get("msg");
        String c_date = (String) param.get("timestamp");
        String teamno = (String) param.get("teamno");

        log.info("id =" + id);
        log.info("nikname =" + nikname);
        log.info("content =" + content);
        log.info("c_date =" + c_date);
        log.info("teamno =" + teamno);

        int teami = Integer.parseInt(teamno);

        TeamchatDTO dto = new TeamchatDTO();
        dto.setTeamno(teami);
        dto.setNikname(nikname);
        dto.setContent(content);
        dto.setC_date(c_date);
        dto.setUser_id(id);

        service.TeamChat_Insert(dto);
        // 받은 데이터 처리
        // data.getId(), data.getNikname(), data.getMsg(), data.getTimestamp() 등으로 접근 가능

        return("성공");
    }
    @ResponseBody
    @PostMapping("/complete")
    public String Complete(@RequestBody Map<String, Object> param) throws ParseException {
        String id= (String) param.get("id");
        String work = (String)param.get("work");
        String temano = (String) param.get("temano");
        String workday = (String) param.get("workday");
        String selectedValue = (String) param.get("selectedValue");

        int teamnol = Integer.parseInt(temano);
        SimpleDateFormat inputFormat = new SimpleDateFormat("yy/MM/dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = inputFormat.parse(workday);
        String outputDate = outputFormat.format(date);


        TeamDTO dto = new TeamDTO();
        dto.setUser_id(id);
        dto.setWork(work);
        dto.setTeamno(teamnol);
        dto.setStart_date(outputDate);
        dto.setComplete(selectedValue);

        service.COMPLETE_Up(dto);

        return "성공";
    }

    @ResponseBody
    @PostMapping("/workdel")
    public String Workdel(@RequestBody Map<String, Object> param) throws ParseException {
        String id= (String) param.get("id");
        String work = (String)param.get("work");
        String temano = (String) param.get("temano");
        String workday = (String) param.get("workday");


        int teamnol = Integer.parseInt(temano);
        SimpleDateFormat inputFormat = new SimpleDateFormat("yy/MM/dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = inputFormat.parse(workday);
        String outputDate = outputFormat.format(date);


        log.info("id =" + id);
        log.info("work =" + work);
        log.info("temano =" + teamnol);
        log.info("outputDate =" + outputDate);

        TeamDTO dto = new TeamDTO();
        dto.setUser_id(id);
        dto.setWork(work);
        dto.setTeamno(teamnol);
        dto.setStart_date(outputDate);

        service.Work_Delect(dto);


        return "성공";
    }

    @GetMapping("/modf")
    public String Modf(@RequestParam("user_id") String user_id, Model model){
        UserDTO dto = service.User_Select(user_id);
        model.addAttribute("user" , dto);
        byte[] imageData = dto.getP_img();
        if (imageData != null) {
            String base64Image = Base64.getEncoder().encodeToString(imageData);
            model.addAttribute("base64Image", base64Image);
        }

        return  "/modf";
    }

    @PostMapping("/modf")
    public String saveUserData(
            @RequestParam(value = "nikname", required = false) String nikname,
            @RequestParam(value = "user_pass", required = false) String user_pass,
            @RequestParam(value = "p_img", required = false) MultipartFile p_img,
            @RequestParam(value = "user_id", required = false) String user_id,
            Model model,
            HttpServletRequest request
    ) {
        byte[] imageData = null;
        if (p_img != null && !p_img.isEmpty()) {
            try {
                imageData = p_img.getBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if( imageData==null && user_pass.equals("") && nikname.equals("")){

        }else{
            // DB에 업데이트할 데이터를 Map에 추가
            Map<String, Object> params = new HashMap<>();
            params.put("nikname", nikname);
            params.put("user_pass", user_pass);
            params.put("p_img", imageData);
            params.put("user_id", user_id);

            log.info("p_img?=" +imageData );
            log.info("nikname?=" +nikname );
            log.info("user_pass?=" +user_pass );
            service.User_Up(params);
        }



        UserDTO dto = service.User_Select(user_id);


        byte[] imageData1 = dto.getP_img();

        if (imageData1 != null) {
            String base64Image = Base64.getEncoder().encodeToString(imageData1);
            model.addAttribute("base64Image", base64Image);
        }

        HttpSession session = request.getSession();// セッション生成
        session.setAttribute("user", dto);
        session.setMaxInactiveInterval(500);//セッション維持時間500秒
        model.addAttribute("user" ,session );

        List<TeamDTO>list = service.Team_Select(dto.getUser_id());
        model.addAttribute("my_id" ,dto.getUser_id() );
        model.addAttribute("team" , list);

        return "/loginindex";
    }

    @ResponseBody
    @PostMapping("/teamdle")
    public HashMap<String, Object>  Teamdle(@RequestBody Map<String, Object> param , Model model) {


        String user_id = (String) param.get("user_id");
        String Stringteamno = (String) param.get("teamno");

        log.info(user_id);
        log.info(Stringteamno);
        int teamno = Integer.parseInt(Stringteamno);

        TeamDTO tdto = new TeamDTO();
        tdto.setTeamno(teamno);
        tdto.setUser_id(user_id);
        service.Team_del(tdto);


        HashMap<String , Object> response  = new HashMap<>();

        UserDTO dto = service.User_Select(user_id);

        response.put("user_id" , dto.getUser_id()); //  중복 포함 모든 정보
        response.put("user_pass" , dto.getUser_pass()); // 중복제거


        return response;
    }


}
