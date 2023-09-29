package com.example.demo1dww.service;

import com.example.demo1dww.domain.TeamDTO;
import com.example.demo1dww.domain.TeamchatDTO;
import com.example.demo1dww.domain.UserDTO;
import com.example.demo1dww.mapper.DMapper;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class DServiceimpl implements DService{
    private static final Logger log=
            LoggerFactory.getLogger(DServiceimpl.class);

    @Autowired
    private DMapper mapper;

    @Override
    public int ck() {
        return mapper.ck();
    }

    @Override
    public int User_Chk(String username , String user_value) {
        return mapper.User_Chk(username , user_value);
    }

    @Override
    public void User_Insert(UserDTO dto) {
        mapper.User_Insert(dto);
    }

    @Override
    public int Login_CK(UserDTO dto) {
        return mapper.Login_CK(dto);
    }

    @Override
    public int Email_Ck(String email) {
        return mapper.Email_Ck(email);
    }

    @Override
    public void Passsw(String user_pass , String email) {
        mapper.Passsw(user_pass , email);
    }

    @Override
    public UserDTO User_Select(String user_id) {
        return mapper.User_Select(user_id);
    }

    @Override
    public UserDTO Mail_User(String email) {
        return mapper.Mail_User(email);
    }

    @Override
    public void Team_Insert(String teamname, String myeamil) {
        mapper.Team_Insert(teamname,myeamil);
    }

    @Override
    public UserDTO Leader_Select(String email) {
        return mapper.Leader_Select(email);
    }

    @Override
    public void Member_Insert(UserDTO dto) {
        mapper.Member_Insert(dto);
    }

    @Override
    public int Seq_Select() {
        return mapper.Seq_Select();
    }

    @Override
    public void Member_Insert_no(int teamno, String user_id, String nikname) {
        mapper.Member_Insert_no(teamno, user_id, nikname);
    }

    @Override
    public List<TeamDTO> Team_Select(String user_id) {
        return mapper.Team_Select(user_id);
    }

    @Override
    public List<TeamDTO> Member_List(int teamno) {
        return mapper.Member_List(teamno);
    }

    @Override
    public List<TeamDTO> Work_Select(int teamno) {
        return mapper.Work_Select(teamno);
    }

    @Override
    public void Work_Insert(TeamDTO dto) {
        mapper.Work_Insert(dto);
    }

    @Override
    public List<TeamchatDTO> TeamChat_Select(int teamno) {
        return mapper.TeamChat_Select(teamno);
    }

    @Override
    public List<TeamDTO> Teamtoday_Work(int teamno, String start_date) {
        return mapper.Teamtoday_Work(teamno, start_date);
    }

    @Override
    public TeamDTO My_team(int teamno) {
        return mapper.My_team(teamno);
    }

    @Override
    public void TeamChat_Insert(TeamchatDTO dto) {
       mapper.TeamChat_Insert(dto);
    }

    @Override
    public void COMPLETE_Up(TeamDTO dto) {
        mapper.COMPLETE_Up(dto);
    }

    @Override
    public void Work_Delect(TeamDTO dto) {
        mapper.Work_Delect(dto);
    }

    @Override
    public void User_Up(Map<String, Object> params) {
        mapper.User_Up(params);
    }

    @Override
    public void Team_del(TeamDTO dto) {
        mapper.Team_del(dto);
    }
}
