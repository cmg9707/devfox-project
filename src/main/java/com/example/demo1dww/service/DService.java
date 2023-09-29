package com.example.demo1dww.service;

import com.example.demo1dww.domain.TeamDTO;
import com.example.demo1dww.domain.TeamchatDTO;
import com.example.demo1dww.domain.UserDTO;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DService {

    public int ck();

    public int User_Chk(String username , String user_value);

    public void User_Insert(UserDTO dto);

    public int Login_CK(UserDTO dto);

    public int Email_Ck(String email);

    public void Passsw(String user_pass , String email);

    public UserDTO User_Select(String user_id);

    public UserDTO Mail_User(String email);

    public void Team_Insert(String teamname , String myeamil);

    public UserDTO Leader_Select(String email);

    public void Member_Insert(UserDTO dto);

    public int Seq_Select();

    public void Member_Insert_no(int teamno, String user_id, String nikname);

    public List<TeamDTO> Team_Select(String user_id);

    public List<TeamDTO> Member_List(int teamno);

    public List<TeamDTO> Work_Select(int teamno);

    public void Work_Insert(TeamDTO dto);

    public List<TeamchatDTO> TeamChat_Select(int teamno);

    public  List<TeamDTO> Teamtoday_Work(int teamno, String start_date);

    public TeamDTO My_team(int teamno);

    public void TeamChat_Insert(TeamchatDTO dto);

    public void COMPLETE_Up(TeamDTO dto);

    public void Work_Delect(TeamDTO dto);

    public void User_Up(Map<String, Object> params);

    public void Team_del(TeamDTO dto);
}
